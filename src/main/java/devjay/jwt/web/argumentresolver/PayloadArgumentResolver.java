package devjay.jwt.web.argumentresolver;

import com.auth0.jwt.interfaces.Claim;
import devjay.jwt.web.JwtUtil;
import devjay.jwt.web.TokenPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

@Slf4j
public class PayloadArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(TokenPayload.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authHeader = webRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }

        String token = authHeader.substring(7);

        Map<String, Claim> claims = JwtUtil.verifyAccessToken(token);
        Long id = claims.get("id").asLong();
        String username = claims.get("username").asString();

        return new TokenPayload(id, username);
    }
}
