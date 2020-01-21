package com.github.prgrms.social.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.prgrms.social.security.JWT;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class User {

    @ApiModelProperty(value = "PK", required = true)
    private final Long seq;

    @ApiModelProperty(value = "사용자명", required = true)
    private final String name;

    @ApiModelProperty(value = "이메일", required = true)
    private final Email email;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String password;

    @ApiModelProperty(value = "로그인 횟수", required = true)
    private int loginCount;

    @ApiModelProperty(value = "최종로그인일시")
    private LocalDateTime lastLoginAt;

    @ApiModelProperty(value = "생성일시", required = true)
    private final LocalDateTime createAt;

    public User(String name, Email email, String password) {
        this(null, name, email, password, 0, null, null);
    }

    public User(Long seq, String name, Email email, String password, int loginCount, LocalDateTime lastLoginAt, LocalDateTime createAt) {
        checkArgument(isNotEmpty(name), "name must be provided.");
        checkArgument(
                name.length() >= 1 && name.length() <= 10,
                "name length must be between 1 and 10 characters."
        );
        checkNotNull(email, "email must be provided.");
        checkNotNull(password, "password must be provided.");

        this.seq = seq;
        this.name = name;
        this.email = email;
        this.password = password;
        this.loginCount = loginCount;
        this.lastLoginAt = lastLoginAt;
        this.createAt = defaultIfNull(createAt, now());
    }

    public void login(PasswordEncoder passwordEncoder, String credentials) {
        if (!passwordEncoder.matches(credentials, password))
            throw new IllegalArgumentException("Bad credential");
    }

    public void afterLoginSuccess() {
        loginCount++;
        lastLoginAt = now();
    }

    public String newApiToken(JWT jwt, String[] roles) {
        JWT.Claims claims = JWT.Claims.of(seq, name, email, roles);
        return jwt.newToken(claims);
    }

    public Long getSeq() {
        return seq;
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public Optional<LocalDateTime> getLastLoginAt() {
        return ofNullable(lastLoginAt);
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(seq, user.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("seq", seq)
                .append("name", name)
                .append("email", email)
                .append("password", "[PROTECTED]")
                .append("loginCount", loginCount)
                .append("lastLoginAt", lastLoginAt)
                .append("createAt", createAt)
                .toString();
    }

    static public class Builder {
        private Long seq;
        private String name;
        private Email email;
        private String password;
        private int loginCount;
        private LocalDateTime lastLoginAt;
        private LocalDateTime createAt;

        public Builder() {}

        public Builder(User user) {
            this.seq = user.seq;
            this.name = user.name;
            this.email = user.email;
            this.password = user.password;
            this.loginCount = user.loginCount;
            this.lastLoginAt = user.lastLoginAt;
            this.createAt = user.createAt;
        }

        public Builder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(Email email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder loginCount(int loginCount) {
            this.loginCount = loginCount;
            return this;
        }

        public Builder lastLoginAt(LocalDateTime lastLoginAt) {
            this.lastLoginAt = lastLoginAt;
            return this;
        }

        public Builder createAt(LocalDateTime createAt) {
            this.createAt = createAt;
            return this;
        }

        public User build() {
            return new User(seq, name, email, password, loginCount, lastLoginAt, createAt);
        }
    }

}
