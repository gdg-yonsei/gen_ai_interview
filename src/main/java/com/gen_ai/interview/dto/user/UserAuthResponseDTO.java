package com.gen_ai.interview.dto.user;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserAuthResponseDTO {
    private UserDTO user;
    private JwtTokenDTO token;
}