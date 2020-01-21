package com.github.prgrms.social.controller.user;

import com.github.prgrms.social.error.NotFoundException;
import com.github.prgrms.social.model.api.request.user.JoinRequest;
import com.github.prgrms.social.model.api.response.ApiResult;
import com.github.prgrms.social.model.api.response.user.JoinResult;
import com.github.prgrms.social.model.user.ConnectedUser;
import com.github.prgrms.social.model.user.Email;
import com.github.prgrms.social.model.user.Role;
import com.github.prgrms.social.model.user.User;
import com.github.prgrms.social.security.JWT;
import com.github.prgrms.social.security.JwtAuthentication;
import com.github.prgrms.social.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.github.prgrms.social.model.api.response.ApiResult.OK;

@RestController
@RequestMapping("api")
@Api(tags = "사용자 APIs")
public class UserRestController {

    private final JWT jwt;

    private final UserService userService;

    public UserRestController(JWT jwt, UserService userService) {
        this.jwt = jwt;
        this.userService = userService;
    }

    @PostMapping(path = "user/exists")
    @ApiOperation(value = "이메일 중복확인 (API 토큰 필요없음)")
    public ApiResult<Boolean> checkEmail(
            @RequestBody
            @ApiParam(value = "example: {\"address\": \"test00@gmail.com\"}")
            Map<String, String> request
    ) {
        Email email = new Email(request.get("address"));
        return OK(userService.findByEmail(email).isPresent());
    }

    @PostMapping(path = "user/join")
    @ApiOperation(value = "회원가입 (API 토큰 필요없음)")
    public ApiResult<JoinResult> join(@RequestBody JoinRequest joinRequest) {
        User user = userService.join(
                joinRequest.getName(),
                new Email(joinRequest.getPrincipal()),
                joinRequest.getCredentials()
        );
        String apiToken = user.newApiToken(jwt, new String[]{Role.USER.value()});
        return OK(new JoinResult(apiToken, user));
    }

    @GetMapping(path = "user/me")
    @ApiOperation(value = "내 정보")
    public ApiResult<User> me(@AuthenticationPrincipal JwtAuthentication authentication) {
        return OK(
                userService.findById(authentication.id)
                        .orElseThrow(() -> new NotFoundException(User.class, authentication.id))
        );
    }

    @GetMapping(path = "user/connections")
    @ApiOperation(value = "내 친구 목록")
    public ApiResult<List<ConnectedUser>> connections(@AuthenticationPrincipal JwtAuthentication authentication) {
        return OK(userService.findAllConnectedUser(authentication.id));
    }

}
