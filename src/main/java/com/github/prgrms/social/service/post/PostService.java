package com.github.prgrms.social.service.post;

import com.github.prgrms.social.error.NotFoundException;
import com.github.prgrms.social.model.commons.Id;
import com.github.prgrms.social.model.post.Post;
import com.github.prgrms.social.model.user.User;
import com.github.prgrms.social.repository.post.PostRepository;
import com.github.prgrms.social.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class PostService {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    public PostService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public Post write(Post post) {
        return save(post);
    }

    @Transactional
    public Post modify(Post post) {
        update(post);
        return post;
    }

    @Transactional
    public Optional<Post> like(/*필요한 인자들을 선언*/) {
        // TODO PostLikeRepository를 구현하고, 포스트 좋아요 서비스를 구현하세요.
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public Optional<Post> findById(Id<Post, Long> postId /*추가로 필요한 인자들을 선언*/) {
        checkNotNull(postId, "postId must be provided.");
        // TODO likesOfMe를 효율적으로 구하기 위해 변경 필요
        return postRepository.findById(postId);
    }

    @Transactional(readOnly = true)
    public List<Post> findAll(Id<User, Long> userId, /*추가로 필요한 인자들을 선언*/ long offset, int limit) {
        checkNotNull(userId, "userId must be provided.");

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, userId));
        // TODO likesOfMe를 효율적으로 구하기 위해 변경 필요
        return postRepository.findAll(userId, offset, limit);
    }

    private Post save(Post post) {
        return postRepository.save(post);
    }

    private void update(Post post) {
        postRepository.update(post);
    }

}
