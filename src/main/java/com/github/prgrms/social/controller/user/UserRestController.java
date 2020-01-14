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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.github.prgrms.social.model.api.response.ApiResult.OK;

@RestController
@RequestMapping("api")
public class UserRestController {

    private final JWT jwt;

    private final UserService userService;

    public UserRestController(JWT jwt, UserService userService) {
        this.jwt = jwt;
        this.userService = userService;
    }

    @PostMapping(path = "user/exists")
    public ApiResult<Boolean> checkEmail() {
        // TODO 이메일 중복 확인 로직 구현
        // BODY 예시: {
        //	"email": "iyboklee@gmail.com"
        //}
        return OK(false);
    }

    @PostMapping(path = "user/join")
    public ApiResult<JoinResult> join(@RequestBody JoinRequest joinRequest) {
        User user = userService.join(new Email(joinRequest.getPrincipal()), joinRequest.getCredentials());
        String apiToken = user.newApiToken(jwt, new String[]{Role.USER.value()});
        return OK(new JoinResult(apiToken, user));
    }

    @GetMapping(path = "user/me")
    public ApiResult<User> me(@AuthenticationPrincipal JwtAuthentication authentication) {
        return OK(
                userService.findById(authentication.id)
                        .orElseThrow(() -> new NotFoundException(User.class, authentication.id))
        );
    }

    @GetMapping(path = "user/connections")
    public ApiResult<List<ConnectedUser>> connections(@AuthenticationPrincipal JwtAuthentication authentication) {
        return OK(userService.findAllConnectedUser(authentication.id));
    }

}
