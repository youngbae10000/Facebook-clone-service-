package com.github.prgrms.social.controller.authentication;

import com.github.prgrms.social.error.UnauthorizedException;
import com.github.prgrms.social.model.api.response.ApiResult;
import com.github.prgrms.social.security.AuthenticationRequest;
import com.github.prgrms.social.security.AuthenticationResult;
import com.github.prgrms.social.security.JwtAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.prgrms.social.model.api.response.ApiResult.OK;

@RestController
@RequestMapping("api/auth")
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;

    public AuthenticationRestController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public ApiResult<AuthenticationResult> authentication(@RequestBody AuthenticationRequest authRequest) throws UnauthorizedException {
        try {
            JwtAuthenticationToken authToken = new JwtAuthenticationToken(authRequest.getPrincipal(), authRequest.getCredentials());
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return OK((AuthenticationResult) authentication.getDetails());
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }

}
