package com.github.prgrms.social.repository.post;

import com.github.prgrms.social.model.commons.Id;
import com.github.prgrms.social.model.post.Post;
import com.github.prgrms.social.model.user.User;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    void update(Post post);

    Optional<Post> findById(Id<User, Long> writerId, Id<User, Long> userId, Id<Post, Long> postId);

    List<Post> findAll(Id<User, Long> userId, Id<User, Long> writerId, long offset, int limit);

}
