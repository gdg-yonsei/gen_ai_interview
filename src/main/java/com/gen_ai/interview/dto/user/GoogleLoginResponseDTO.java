package com.gen_ai.interview.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleLoginResponseDTO {
    private UserDTO userDTO;
    private JwtTokenDTO tokenDTO;
}