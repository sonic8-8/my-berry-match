package com.gongcha.berrymatch.springSecurity;

import com.gongcha.berrymatch.springSecurity.service.JwtGenerator;
import com.gongcha.berrymatch.user.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.services.s3.S3Client;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class JwtGeneratorTest {

    private JwtGenerator jwtGenerator;

    @Value("${BERRYMATCH_JWT_ISSUER}")
    private String issuer;   // JWT를 발행한 단체나 사람을 발행자로 합니다.

    @Value("${BERRYMATCH_JWT_SECRETKEY}")
    private SecretKey secretkey;

    @Value("${BERRYMATCH_JWT_EXPIRATION_TIME}")
    private long expirationTime;

    @MockBean
    private S3Client s3Client; // S3Client를 Mock으로 만듭니다.


//    @DisplayName("사용자의 정보를 받아 JWT를 발급할 수 있다.")
//    @Test
//    void createJwt() {
//        // given
//        long now = System.currentTimeMillis();
//        User user = User.builder()
//                .username("smhrd")
//                .nickname("사람임")
//                .password("1234")
//                .city(City.SEOUL)
//                .district(District.SEOUL_DOBONG_GU)
//                .gender(Gender.MALE)
//                .age(20)
//                .phoneNumber("010-1234-5678")
//                .profileImageUrl(null)
//                .introduction("하이요")
//                .email("smhrd1234@naver.com")
//                .role(Role.USER)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//
//        // when
//        String jwt = jwtGenerator.generateAccessToken(user);
//
//        // then
//        assertThat(jwt).isNotNull();
//
//        Jws<Claims> jws = Jwts.parser().verifyWith(secretkey).build().parseSignedClaims(jwt);
//        assertThat(jws.getPayload().getIssuer()).isEqualTo("com.gongcha.berrymatch");
//        assertThat(jws.getPayload().getExpiration()).isEqualTo(new Date(now + expirationTime));
//    }

}