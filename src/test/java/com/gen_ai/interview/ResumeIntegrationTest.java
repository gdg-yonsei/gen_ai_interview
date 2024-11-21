package com.gen_ai.interview;

import com.gen_ai.interview.dto.user.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResumeIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getCurrentUser(){
        ///current-user
        UserDTO forObject = restTemplate.getForObject("/api/users/current-user", UserDTO.class);
        System.out.println(forObject);
    }
}
