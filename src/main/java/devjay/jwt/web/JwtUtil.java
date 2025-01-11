package devjay.jwt.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import devjay.jwt.domain.Member;

import java.time.Instant;
import java.util.HashMap;
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
                .withPayload(createPayload(member))
                .withExpiresAt(Instant.now().plusSeconds(MINUTES_AS_SECOND))
                .sign(Algorithm.HMAC256(ACCESS_SECRET_KEY));
    }

    public static String refreshToken(Member member) {
        return JWT.create()
                .withIssuer("blog.devjay")
                .withPayload(createPayload(member))
                .withExpiresAt(Instant.now().plusSeconds(MINUTES_AS_SECOND))
                .sign(Algorithm.HMAC256(REFRESH_SECRET_KEY));
    }

    public static Map<String, Claim> verifyAccessToken(String token) {
        return JWT.require(Algorithm.HMAC256(ACCESS_SECRET_KEY))
                .withIssuer("blog.devjay")
                .build()
                .verify(token)
                .getClaims();
    }

    public static Map<String, Claim> verifyRefreshToken(String token) {
        return JWT.require(Algorithm.HMAC256(REFRESH_SECRET_KEY))
                .withIssuer("blog.devjay")
                .build()
                .verify(token)
                .getClaims();
    }

    private static Map<String, Object> createPayload(Member member) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("id", member.getId());
        map.put("username", member.getUsername());

        return map;
    }
}
