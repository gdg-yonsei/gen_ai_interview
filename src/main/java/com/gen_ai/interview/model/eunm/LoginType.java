package com.gen_ai.interview.model.eunm;

import lombok.Getter;

@Getter
public enum LoginType {
    GOOGLE("google"),
    EMAIL("email"),
    GUEST("guest");

    private String type;

    LoginType(String type) {
        this.type = type;
    }
}
