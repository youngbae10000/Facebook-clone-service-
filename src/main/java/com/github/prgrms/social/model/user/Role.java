package com.github.prgrms.social.model.user;

public enum Role {

    USER("ROLE_USER");

    private String value;

    Role(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
