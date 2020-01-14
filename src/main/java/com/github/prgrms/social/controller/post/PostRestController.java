package com.github.prgrms.social.controller.post;

import com.github.prgrms.social.model.api.request.post.PostingRequest;
import com.github.prgrms.social.model.api.response.ApiResult;
import com.github.prgrms.social.model.commons.Id;
import com.github.prgrms.social.model.post.Post;
import com.github.prgrms.social.model.post.Writer;
import com.github.prgrms.social.model.user.User;
import com.github.prgrms.social.security.JwtAuthentication;
import com.github.prgrms.social.service.post.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.github.prgrms.social.model.api.response.ApiResult.OK;

@RestController
@RequestMapping("api")
public class PostRestController {

    private final PostService postService;

    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(path = "post")
    public ApiResult<Post> posting(@AuthenticationPrincipal JwtAuthentication authentication,
                                   @RequestBody PostingRequest request) {
        return OK(postService.write(request.newPost(authentication.id, new Writer(authentication.email))));
    }

    @GetMapping(path = "user/{userId}/post/list")
    public ApiResult<List<Post>> posts(@PathVariable Long userId) {
        return OK(postService.findAll(Id.of(User.class, userId)));
    }

}
