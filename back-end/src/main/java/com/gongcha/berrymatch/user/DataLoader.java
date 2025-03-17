package com.gongcha.berrymatch.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class DataLoader {

    @Autowired
    private UserRepository userRepository;

//    @Bean
//    public CommandLineRunner loadData() {
//        return args -> {
//            Random random = new Random();
//
//            for (int i = 0; i < 2; i++) {
//                User user = User.builder()
//                        .identifier("id"+ i)
//                        .nickname("nickname" + i)
//                        .city(City.values()[random.nextInt(City.values().length)])
//                        .district(District.values()[random.nextInt(District.values().length)])
//                        .gender(Gender.values()[random.nextInt(Gender.values().length)])
//                        .phoneNumber("010-" + (random.nextInt(9000) + 1000) + "-" + (random.nextInt(9000) + 1000))
//                        .profileImageUrl("http://example.com/profile/image" + i)
//                        .introduction("Hello, I am user " + i)
//                        .email("user" + i + "@example.com")
//                        .build();
//
//                userRepository.save(user);
//            }
//        };
//    }
}
