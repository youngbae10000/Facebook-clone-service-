package com.github.prgrms.social.security;

public class AuthenticationRequest {

    private String principal;

    private String credentials;

    protected AuthenticationRequest() {}

    public AuthenticationRequest(String principal, String credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }

    public String getPrincipal() {
        return principal;
    }

    public String getCredentials() {
        return credentials;
    }

}
