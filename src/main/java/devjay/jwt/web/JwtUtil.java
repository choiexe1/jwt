package devjay.jwt.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import devjay.jwt.domain.Member;

import java.time.Instant;
import java.util.Map;

public class JwtUtil {
    private static final String ACCESS_SECRET_KEY = "access-key";
    private static final String REFRESH_SECRET_KEY = "refresh-key";
    private static final long MINUTES_AS_SECOND = 60;

    private JwtUtil() {
    }

    public static String accessToken(Member member) {
        return JWT.create()
                .withIssuer("blog.devjay")
                .withPayload(createPayload(member).toMap())
                .withExpiresAt(Instant.now().plusSeconds(MINUTES_AS_SECOND))
                .sign(Algorithm.HMAC256(ACCESS_SECRET_KEY));
    }

    public static String refreshToken(Member member) {
        return JWT.create()
                .withIssuer("blog.devjay")
                .withPayload(createPayload(member).toMap())
                .withExpiresAt(Instant.now().plusSeconds(MINUTES_AS_SECOND * 5))
                .sign(Algorithm.HMAC256(REFRESH_SECRET_KEY));
    }

    public static Map<String, Claim> verifyAccessToken(String token) {
        return JWT.require(Algorithm.HMAC256(ACCESS_SECRET_KEY))
                .withIssuer("blog.devjay")
                .build()
                .verify(token)
                .getClaims();
    }

    public static void verifyRefreshToken(String token) {
        JWT.require(Algorithm.HMAC256(REFRESH_SECRET_KEY))
                .withIssuer("blog.devjay")
                .build()
                .verify(token);
    }

    private static TokenPayload createPayload(Member member) {
        return new TokenPayload(member.getId(), member.getUsername());
    }
}
