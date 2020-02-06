package com.github.prgrms.social.controller.post;

import com.github.prgrms.social.configure.support.Pageable;
import com.github.prgrms.social.error.NotFoundException;
import com.github.prgrms.social.model.api.request.post.PostingRequest;
import com.github.prgrms.social.model.api.response.ApiResult;
import com.github.prgrms.social.model.commons.Id;
import com.github.prgrms.social.model.post.Post;
import com.github.prgrms.social.model.post.Writer;
import com.github.prgrms.social.model.user.User;
import com.github.prgrms.social.security.JwtAuthentication;
import com.github.prgrms.social.service.post.PostService;
import io.swagger.annotations.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.github.prgrms.social.model.api.response.ApiResult.OK;

@RestController
@RequestMapping("api")
@Api(tags = "게시물 APis")
public class PostRestController {

    private final PostService postService;

    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(path = "post")
    @ApiOperation(value = "게시물 올리기")
    public ApiResult<Post> posting(@AuthenticationPrincipal JwtAuthentication authentication,
                                   @RequestBody PostingRequest request) {
        return OK(postService.write(request.newPost(authentication.id, new Writer(authentication.email, authentication.name))));
    }

    @GetMapping(path = "user/{userId}/post/list")
    @ApiOperation(value = "게시물 목록 전체 불러오기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "offset", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "limit", dataType = "integer", paramType = "query", defaultValue = "20", value = "최대 조회 갯수")
    })
    public ApiResult<List<Post>> posts(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable
            @ApiParam(value = "조회대상자 PK (본인 또는 친구)", example = "1") Long userId,
            Pageable pageable
    ) {
        // TODO query parameter에 offset, limit 파라미터를 추가하고 페이징 처리한다.
        return OK(postService.findAll(Id.of(User.class, userId), authentication.id, pageable.offset(), pageable.limit()));
    }

    @PatchMapping(path = "user/{userId}/post/{postId}/like")
    @ApiOperation(value = "게시물 좋아요")
    public ApiResult<Post> like(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable @ApiParam(value = "조회대상자 PK (본인 또는 친구)", example = "1") Long userId,
            @PathVariable @ApiParam(value = "대상 포스트 PK", example = "1") Long postId
    ) {
        return OK(postService.like(authentication.id, Id.of(User.class, userId), Id.of(Post.class, postId))
                .orElseThrow(() -> new NotFoundException(Post.class, Id.of(Post.class, postId), Id.of(User.class, userId)))
        );
    }

}
