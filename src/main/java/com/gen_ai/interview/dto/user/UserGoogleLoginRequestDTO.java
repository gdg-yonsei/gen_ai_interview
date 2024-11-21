package com.gen_ai.interview.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserGoogleLoginRequestDTO {

    private String tokenType;
    private String accessToken;
}

