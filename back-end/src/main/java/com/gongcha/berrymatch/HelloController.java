package com.gongcha.berrymatch;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController("/api")
public class HelloController {

    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> hello() {
        System.out.println("요청 들어옴");

        Map<String, String> data = new HashMap<>();
        data.put("응답", "프록시 서버 연동 테스트 성공함 굿");

        System.out.println(data);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
