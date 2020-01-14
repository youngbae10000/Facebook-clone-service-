package com.github.prgrms.social.configure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.github.prgrms.social.security.JWT;
import com.github.prgrms.social.util.MessageUtils;
import com.zaxxer.hikari.HikariDataSource;
import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ServiceConfigure {

    @Bean
    @Profile("test")
    public DataSource testDataSource() {
        DataSourceBuilder factory = DataSourceBuilder
                .create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:test_social_server;MODE=MYSQL;DB_CLOSE_DELAY=-1");
        HikariDataSource dataSource = (HikariDataSource) factory.build();
        dataSource.setPoolName("TEST_H2_DB");
        dataSource.setMinimumIdle(1);
        dataSource.setMaximumPoolSize(1);
        return new Log4jdbcProxyDataSource(dataSource);
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource);
    }

    @Bean
    public MessageUtils messageUtils() {
        return MessageUtils.getInstance();
    }

    @Value("${jwt.token.issuer}") String issuer;
    @Value("${jwt.token.clientSecret}") String clientSecret;
    @Value("${jwt.token.expirySeconds}") int expirySeconds;

    @Bean
    public JWT jwt() {
        return new JWT(issuer, clientSecret, expirySeconds);
    }

    @Bean
    public Jackson2ObjectMapperBuilder configureObjectMapper() {
        // Java time module
        JavaTimeModule jtm = new JavaTimeModule();
        jtm.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder() {
            @Override
            public void configure(ObjectMapper objectMapper) {
                super.configure(objectMapper);
                objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
                objectMapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
                objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            }
        };
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        builder.modulesToInstall(jtm);
        return builder;
    }

}
