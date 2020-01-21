package com.github.prgrms.social.repository.post;

import com.github.prgrms.social.model.commons.Id;
import com.github.prgrms.social.model.post.Post;
import com.github.prgrms.social.model.user.User;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    void update(Post post);

    Optional<Post> findById(Id<Post, Long> postId /*추가로 필요한 인자들을 선언*/);

    List<Post> findAll(Id<User, Long> userId, /*추가로 필요한 인자들을 선언*/ long offset, int limit);

}
