package com.github.prgrms.social.security;

import com.github.prgrms.social.model.commons.Id;
import com.github.prgrms.social.model.user.Email;
import com.github.prgrms.social.model.user.User;

import static com.google.common.base.Preconditions.checkNotNull;

public class JwtAuthentication {

    public final Id<User, Long> id;

    public final Email email;

    // TODO 이름 프로퍼티 추가

    JwtAuthentication(Long id, Email email) {
        checkNotNull(id, "id must be provided.");
        checkNotNull(email, "email must be provided.");

        this.id = Id.of(User.class, id);
        this.email = email;
    }

}