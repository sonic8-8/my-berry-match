package com.gongcha.berrymatch.match.Service;

import com.gongcha.berrymatch.match.DTO.MatchingQueueDTO;
import com.gongcha.berrymatch.match.DTO.MatchingResultDto;
import com.gongcha.berrymatch.match.Repository.MatchUserRepository;
import com.gongcha.berrymatch.match.Repository.MatchingQueueRepository;
import com.gongcha.berrymatch.match.domain.MatchQueueStatus;
import com.gongcha.berrymatch.match.domain.MatchType;
import com.gongcha.berrymatch.match.domain.MatchingQueue;
import com.gongcha.berrymatch.match.domain.Sport;
import com.gongcha.berrymatch.user.City;
import com.gongcha.berrymatch.user.District;
import com.gongcha.berrymatch.user.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

@Service
@EnableAsync
public class IndividualMatchingFilterService {

    private final MatchingQueueRepository matchingQueueRepository;  // 매칭 대기열을 관리하는 리포지토리
    private final MatchUserRepository matchUserRepository;  // 매칭된 유저를 관리하는 리포지토리
    private final MatchProcessingCoordinator matchProcessingCoordinator;  // 매칭 처리 조정을 위한 코디네이터
    private final Lock matchLock;  // 매칭 시 동시성 문제를 방지하기 위한 잠금

    // 생성자를 통해 필요한 의존성 주입
    public IndividualMatchingFilterService(MatchingQueueRepository matchingQueueRepository,
                                           MatchUserRepository matchUserRepository,
                                           MatchProcessingCoordinator matchProcessingCoordinator,
                                           Lock matchLock) {
        this.matchingQueueRepository = matchingQueueRepository;
        this.matchUserRepository = matchUserRepository;
        this.matchProcessingCoordinator = matchProcessingCoordinator;
        this.matchLock = matchLock;
    }

    // 비동기 메소드로, 일정 시간 간격으로 매칭 작업을 수행
    @Async("taskScheduler")
    @Scheduled(fixedRate = 5000, initialDelay = 3000)//매칭 대기열 돌아가는시간
    public void runScheduledMatching() {
        List<MatchingQueueDTO> pendingMatches = null;
        matchLock.lock();  // 데이터베이스 조회를 위한 잠금
        try {
            pendingMatches = getPendingMatchesAsync().get();  // 비동기로 대기중인 매칭 데이터를 가져옴
        } catch (Exception e) {
            System.out.println("Scheduled matching task failed: " + e.getMessage());
        } finally {
            matchLock.unlock();  // 데이터베이스에서 데이터를 가져온 후 잠금 해제
        }

        if (pendingMatches != null) {
            List<MatchingResultDto> matchingGroups = createMatchingGroups(pendingMatches);  // 매칭 그룹 생성
            if (matchingGroups.isEmpty()) {
                System.out.println("No matching groups created.");  // 매칭 그룹이 생성되지 않은 경우
            } else {
                matchProcessingCoordinator.processMatchingGroups(matchingGroups);  // 매칭 그룹이 생성된 경우 처리
            }
        }
    }

    // 비동기로 대기중인 매칭 데이터를 가져오는 메소드
    @Async("individual matchingTaskExecutor")
    public CompletableFuture<List<MatchingQueueDTO>> getPendingMatchesAsync() {
        return CompletableFuture.supplyAsync(() -> {
                    // 이미 매칭된 유저 ID를 필터링하기 위해 가져옴
                    Set<Long> matchedUserIds = matchUserRepository.findAll().stream()
                            .map(matchUser -> matchUser.getUser().getId())
                            .collect(Collectors.toSet());

                    // 매칭 대기 상태에 있고 그룹이 지정되지 않은 대기열을 필터링하여 가져옴
                    List<MatchingQueue> pendingMatches = matchingQueueRepository
                            .findByStatusAndGroupCodeIsNullOrderByEnqueuedAtAsc(MatchQueueStatus.PENDING).stream()
                            .filter(queue -> !matchedUserIds.contains(queue.getUser().getId()))
                            .collect(Collectors.toList());

                    // 매칭 대기열 엔티티를 DTO로 변환
                    return pendingMatches.stream()
                            .map(this::convertToDTO)
                            .collect(Collectors.toList());
                }).thenApply(result -> result)  // 결과 반환
                .exceptionally(ex -> new ArrayList<>());  // 예외 발생 시 빈 리스트 반환
    }

    // MatchingQueue 엔티티를 DTO로 변환하는 메소드
    private MatchingQueueDTO convertToDTO(MatchingQueue queue) {
        return new MatchingQueueDTO(
                queue.getId(),
                queue.getUser() != null ? queue.getUser().getId() : null,
                queue.getCity(),
                queue.getDistrict(),
                queue.getMatchTime(),
                queue.getSport(),
                queue.getGroupCode(),
                queue.getMatchType(),
                queue.getEnqueuedAt()
        );
    }

    // 매칭 그룹을 생성하는 메소드
    //여기서 필터조건을 변경하면 될지도
    public List<MatchingResultDto> createMatchingGroups(List<MatchingQueueDTO> filteredMatches) {
        if (filteredMatches.isEmpty()) {
            System.out.println("No filtered matches available.");
            return new ArrayList<>();
        }

        // 스포츠 유형과 지역(도시, 구/군)별로 매칭 대기열을 그룹화
        Map<Sport, Map<City, Map<District, List<MatchingQueueDTO>>>> groupedBySportAndRegion = filteredMatches.stream()
                .collect(Collectors.groupingBy(
                        MatchingQueueDTO::getSport,  // 스포츠 유형별 그룹화
                        Collectors.groupingBy(
                                MatchingQueueDTO::getCity,  // 도시(Enum)별 그룹화
                                Collectors.groupingBy(MatchingQueueDTO::getDistrict)  // 구/군(Enum)별 그룹화
                        )
                ));

        List<MatchingResultDto> resultDtos = new ArrayList<>();

        // 각 스포츠별, 지역별로 매칭 그룹을 생성
        for (Map.Entry<Sport, Map<City, Map<District, List<MatchingQueueDTO>>>> sportEntry : groupedBySportAndRegion.entrySet()) {
            Sport sport = sportEntry.getKey();
            Map<City, Map<District, List<MatchingQueueDTO>>> cityGroups = sportEntry.getValue();

            for (Map.Entry<City, Map<District, List<MatchingQueueDTO>>> cityEntry : cityGroups.entrySet()) {
                City city = cityEntry.getKey();
                Map<District, List<MatchingQueueDTO>> districtGroups = cityEntry.getValue();

                for (Map.Entry<District, List<MatchingQueueDTO>> districtEntry : districtGroups.entrySet()) {
                    List<MatchingQueueDTO> queueList = districtEntry.getValue();
                    int maxSize = sport.getMaxSize();  // 각 스포츠의 최대 매칭 인원 수

                    // 매칭할 그룹을 만들기 위해 리스트를 일정 크기로 나눔
                    for (int i = 0; i < queueList.size(); i += maxSize) {
                        int end = Math.min(i + maxSize, queueList.size());
                        List<MatchingQueueDTO> subList = queueList.subList(i, end);

                        // 매칭된 유저 리스트를 생성
                        List<User> matchedUsers = subList.stream()
                                .map(MatchingQueueDTO::getUser)
                                .collect(Collectors.toList());

                        if (!matchedUsers.isEmpty()) {
                            // 매칭 결과 DTO를 생성하여 리스트에 추가
                            MatchingResultDto matchingResultDto = new MatchingResultDto(
                                    matchedUsers,
                                    MatchType.USER,
                                    sport,
                                    LocalDateTime.now(),
                                    maxSize
                            );
                            resultDtos.add(matchingResultDto);
                        }
                    }
                }
            }
        }
        System.out.println("Matching groups created: " + resultDtos.size());
        return resultDtos;
    }
}
