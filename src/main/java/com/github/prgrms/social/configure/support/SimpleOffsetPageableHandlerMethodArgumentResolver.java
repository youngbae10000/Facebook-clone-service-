package com.github.prgrms.social.configure.support;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static java.lang.Math.min;
import static org.apache.commons.lang3.math.NumberUtils.toInt;
import static org.springframework.util.StringUtils.hasText;

public class SimpleOffsetPageableHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String DEFAULT_OFFSET_PARAMETER = "offset";

    private static final String DEFAULT_LIMIT_PARAMETER = "limit";

    private static final int DEFAULT_MAX_LIMIT_SIZE = 5;

    private SimpleOffsetPageRequest fallbackPageable;

    private String offsetParameterName = DEFAULT_OFFSET_PARAMETER;

    private String limitParameterName = DEFAULT_LIMIT_PARAMETER;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        /*
         instanceof는 특정 Object가 어떤 클래스/인터페이스를 상속/구현했는지를 체크
         특정 Class가 어떤 클래스/인터페이스를 상속/구현했는지 체크
         */
        return Pageable.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        String offsetString = webRequest.getParameter(offsetParameterName);
        String limitString = webRequest.getParameter(limitParameterName);

        boolean offsetAndLimitGiven = hasText(offsetString) && hasText(limitString);

        if (!offsetAndLimitGiven && fallbackPageable == null)
            return null;

        long offset = hasText(offsetString) ? parseAndApplyBoundaries(offsetString, Integer.MAX_VALUE) : fallbackPageable.offset();
        int limit  = hasText(limitString) ? parseAndApplyBoundaries(limitString, DEFAULT_MAX_LIMIT_SIZE) : fallbackPageable.limit();

        limit = limit < 1 ? fallbackPageable.limit() : limit;
        limit = min(limit, DEFAULT_MAX_LIMIT_SIZE);

        return new SimpleOffsetPageRequest(offset, limit);
    }

    private int parseAndApplyBoundaries(String offsetString, int upper) {
        int parsed = toInt(offsetString, 0);
        return parsed < 0 ? 0 : min(parsed, upper);
    }

    public void setFallbackPageable(SimpleOffsetPageRequest fallbackPageable) {
        this.fallbackPageable = fallbackPageable;
    }

    public void setOffsetParameterName(String offsetParameterName) {
        this.offsetParameterName = offsetParameterName;
    }

    public void setLimitParameterName(String limitParameterName) {
        this.limitParameterName = limitParameterName;
    }
}
