package com.gongcha.berrymatch.game;

import com.gongcha.berrymatch.match.Repository.MatchRepository;
import com.gongcha.berrymatch.match.domain.Match;
import com.gongcha.berrymatch.match.domain.MatchUser;
import com.gongcha.berrymatch.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final GameResultTempRepository gameResultTempRepository;
    private final MatchRepository matchRepository;

    /**
     * 완료된 경기 정보 등록
     * @param matchId
     */
    @Transactional
    public void saveCompletedGame(Long matchId) {
        // matchId를 받아와서 Id에 해당하는 데이터들을 받아옴
        Match completedMatchInfo = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found for ID: " + matchId));

        // MatchUser 안에 들어있는 User에 대한 정보를 List<User>에 담기
        List<User> matchUsers = completedMatchInfo.getMatchUsers()
                .stream()
                .map(MatchUser::getUser) // MatchUser에서 User를 추출
                .collect(Collectors.toList());

        // 경기 결과를 받아와서 새로운 Game 객체에 담아서
        Game newCompletedMatch = Game.builder()
                .users(matchUsers)
                .gameTitle(completedMatchInfo.getMatchedAt().toString())
                .gameStatus(GameStatus.COMPLETED)
                .build();

        // Game 객체 DB 저장
        gameRepository.save(newCompletedMatch);

        // Match Entity에서 해당 데이터 삭제
        matchRepository.delete(completedMatchInfo);
    }

    // 유저의 모든 경기 리턴
    public List<Game> loadAllGames(Long userId){
        return gameRepository.findAllByUserId(userId);
    }

    // 해당 경기에 등록된 모든 점수 기록 리턴
    public List<GameResultTemp> loadAllInputRecord(Long gameId){
        return gameResultTempRepository.findDistinctResultsByGameId(gameId);
    }

    // 경기 기록 가능 상태 확인
    public GameStatus checkReadyInput(Long gameId) {
        return gameRepository.findGameStatusById(gameId);
    }

    // 경기 투표 가능 상태 확인: 모든 유저의 경기기록 완료 or 경기 시간으로부터 12시간 넘었으면 임시경기기록상태 변경
    public GameRecordTempStatus checkReadyVote(Long gameId) {
        Game gameInfo = gameRepository.findAllById(gameId);
        List<GameResultTemp> recordTempList = gameResultTempRepository.findAllByGameId(gameId);
        int recordCnt =  recordTempList.size();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime gameTime = gameInfo.getMatchedAt();
        long duration = Duration.between(gameTime,now).toHours();
        if(recordCnt == 12 || duration > 12){
            recordTempList.forEach(record -> record.setGameRecordTempStatus(GameRecordTempStatus.FINALLY_UPDATE));
            gameResultTempRepository.saveAll(recordTempList);
            return GameRecordTempStatus.FINALLY_UPDATE;
        }else{
            return null;
        }
    }

    /**
     * 1시간마다 경기 시간으로부터 24시간 지났는지 확인 후
     * 제출된 경기 기록 투표 Data 정리하여 최종 결과 등록 & 상태 변경
     */
    @Scheduled(fixedRate = 3600000)
    public void checkAndFInalizeGames(){
        List<Game> games = gameRepository.findAll();

        for(Game game: games){
            if (game.getGameStatus() != GameStatus.RECORDING_COMPLETED && isGameTimePassed24Hours(game)) {
                finalizeGameRecords(game.getId());
            }
        }
    }

    private boolean isGameTimePassed24Hours(Game game){
        LocalDateTime gameTime = game.getMatchedAt();
        LocalDateTime currentTime = LocalDateTime.now();

        return currentTime.isAfter(gameTime.plusHours(24));
    }

    @Transactional
    public void finalizeGameRecords(Long gameId){
        // 해당하는 경기에 대해 유저들이 제출한 모든 기록을 가져옴
        List<GameResultTemp> tempResultList = gameResultTempRepository.findAllByGameId(gameId);
        // 기록들 중 가장 투표수가 많은 기록을 찾음
        int maxVotes = 0;
        int maxVotesResultTeamA = 0;
        int maxVotesResultTeamB = 0;
        for(GameResultTemp r : tempResultList){
            if(r.getVotes() > maxVotes){
                maxVotes = r.getVotes();
                maxVotesResultTeamA = r.getResultTeamA();
                maxVotesResultTeamB = r.getResultTeamB();
            }
        }
        // 투표수가 가장 많은 기록을 해당 경기의 경기 기록으로 저장 & 경기 상태 업데이트
        Game gameRecord = gameRepository.findAllById(gameId);
        gameRecord.setResultTeamA(maxVotesResultTeamA);
        gameRecord.setResultTeamB(maxVotesResultTeamB);
        gameRecord.finishGame();
        gameRepository.save(gameRecord);
    }

    /**
     * 게시물 작성 가능 상태 확인(경기 기록 투표 제출 Data 정리 후, 최종 경기 결과 등록 되어 있으면)
     */
    public GameStatus checkReadyPost(Long gameId) {
        Game gameRecord = gameRepository.findAllById(gameId);
        if(gameRecord.getResultTeamA() == 0 && gameRecord.getResultTeamB() == 0){
            return GameStatus.COMPLETED;
        }else{
            return GameStatus.RECORDING_COMPLETED;
        }
    }

    // 경기 기록 DB 등록
    @Transactional
    public void submitRecord(GameDTO gameResultTemp) {
        GameResultTemp gameRecordInput = GameResultTemp.builder()
                        .game(gameResultTemp.getGame())
                        .user(gameResultTemp.getUser())
                        .resultTeamA(gameResultTemp.getResultTeamA())
                        .resultTeamB(gameResultTemp.getResultTeamB())
                        .gameRecordTempStatus(GameRecordTempStatus.PERSONNAL_UPDATE)
                        .build();
        gameResultTempRepository.save(gameRecordInput);
    }

    /**
     * 투표 기록 DB 등록
     * @param gameResultTemp
     */
    @Transactional
    public void submitVote(GameDTO gameResultTemp) {
        List<GameResultTemp> submitRecordList = gameResultTempRepository.findAllByGameId(gameResultTemp.getGame().getId());
        // gameDTO로 받은 기록 데이터와 일치하는 것에 votes++
        for(GameResultTemp r:submitRecordList){
            if (r.getResultTeamA() == gameResultTemp.getResultTeamA() && r.getResultTeamB() == gameResultTemp.getResultTeamB()) {
                r.setVotes(r.getVotes()+1);
            } else {
                throw new IllegalArgumentException("Invalid Record ID");
            }
        }
    }

    /**
     * 경기 종료 요청 받으면 해당하는 경기에 대한 정보 DB 등록
     * @param game
     */
    @Transactional
    public void saveGameInfo(GameDTO game) {
        Game endedGame = Game.builder()
                .gameTitle(game.getGame().getGameTitle())
                .users(game.getGame().getUsers())
                .build();
        gameRepository.save(endedGame);
    }
}
