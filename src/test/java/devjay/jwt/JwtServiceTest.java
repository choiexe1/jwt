package devjay.jwt;

import com.auth0.jwt.interfaces.Claim;
import devjay.jwt.domain.Member;
import devjay.jwt.domain.Role;
import devjay.jwt.web.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Slf4j
class JwtServiceTest {

    @Test
    void signTest() {
        Member member = new Member();
        member.setId(1L);
        member.setUsername("jay");
        member.setRole(Role.ADMIN);

        String token = JwtUtil.accessToken(member);
        Map<String, Claim> claims = JwtUtil.verifyAccessToken(token);

        log.info("token = {}", token);
        log.info("claims = {}", claims);

    }
}