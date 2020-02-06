package com.github.prgrms.social.service.post;

import com.github.prgrms.social.error.NotFoundException;
import com.github.prgrms.social.model.commons.Id;
import com.github.prgrms.social.model.post.Post;
import com.github.prgrms.social.model.user.User;
import com.github.prgrms.social.repository.post.PostLikeRepository;
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

    private final PostLikeRepository postLikeRepository;

    public PostService(UserRepository userRepository, PostRepository postRepository, PostLikeRepository postLikeRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
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
    public Optional<Post> like(Id<User, Long> writerId, Id<User, Long> userId, Id<Post, Long> postId) {
        // TODO PostLikeRepository를 구현하고, 포스트 좋아요 서비스를 구현하세요.
        /*
        Optional<Post> post = postRepository.findById(writerId, userId, postId);

        if(!post.get().isLikesOfMe()) {
            postLikeRepository.likesTableSave(userId, postId);
            post.get().incrementAndGetLikes();
            update(post.get());
        }
        return post;
         */

        return  postRepository.findById(writerId, userId, postId).map(post -> {
            if(!post.isLikesOfMe()){
                post.incrementAndGetLikes();
                postLikeRepository.likesTableSave(userId, postId);
                update(post);
            }
            return post;
        });
    }

    @Transactional(readOnly = true)
    public Optional<Post> findById(Id<User, Long> writerId, Id<User, Long> userId, Id<Post, Long> postId) {

        checkNotNull(writerId, "writerId must be provided.");
        checkNotNull(userId, "userId must be provided.");
        checkNotNull(postId, "postId must be provided.");

        // TODO likesOfMe를 효율적으로 구하기 위해 변경 필요
        return postRepository.findById(writerId, userId, postId);
    }

    @Transactional(readOnly = true)
    public List<Post> findAll(Id<User, Long> userId, Id<User, Long> writerId, long offset, int limit) {

        checkNotNull(userId, "userId must be provided.");
        checkNotNull(writerId, "writerId must be provided.");

        if(offset < 0)
            offset = 0;
        if(limit < 1 || limit > 5)
            limit = 5;

        // TODO likesOfMe를 효율적으로 구하기 위해 변경 필요
        return postRepository.findAll(userId, writerId, offset, limit);
    }

    private Post save(Post post) {
        return postRepository.save(post);
    }

    private void update(Post post) {
        postRepository.update(post);
    }

}
