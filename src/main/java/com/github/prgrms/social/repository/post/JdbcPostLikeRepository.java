package com.github.prgrms.social.repository.post;

import com.github.prgrms.social.model.commons.Id;
import com.github.prgrms.social.model.post.Post;
import com.github.prgrms.social.model.user.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class JdbcPostLikeRepository implements PostLikeRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPostLikeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void likesTableSave(Id<User, Long> userId, Id<Post, Long> postId) {
        jdbcTemplate.update("INSERT INTO likes (seq, user_seq, post_seq, create_at) VALUES (null, ?, ?, ?)",
                new Object[]{userId.value(), postId.value(), LocalDateTime.now()});
    }
}
