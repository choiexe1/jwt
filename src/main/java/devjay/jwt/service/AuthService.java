package devjay.jwt.service;

import devjay.jwt.domain.Member;
import devjay.jwt.domain.Role;
import devjay.jwt.repository.MemberRepository;
import devjay.jwt.web.JwtUtil;
import devjay.jwt.web.TokenPayload;
import devjay.jwt.web.Tokens;
import devjay.jwt.web.dto.request.LoginDTO;
import devjay.jwt.web.dto.request.RegisterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;

    public Tokens authenticate(LoginDTO loginDTO) {
        Optional<Member> optional = memberRepository.findByUsername(loginDTO.username());

        if (optional.isPresent()) {
            Member member = optional.get();
            String accessToken = JwtUtil.accessToken(member);
            String refreshToken = JwtUtil.refreshToken(member);

            if (loginDTO.password().equals(member.getPassword())) {
                member.setRefreshToken(refreshToken);
                memberRepository.update(member);

                return new Tokens(accessToken, refreshToken);
            }

            throw new IllegalStateException("비밀번호가 일치하지 않음");
        }

        throw new IllegalStateException("유저네임 없음");
    }

    public Member register(RegisterDTO registerDTO) {
        Member member = registerDTO.toMember(Role.USER);
        memberRepository.save(member);

        return member;
    }

    public String verifyRefreshToken(Long id, String refreshToken) {
        JwtUtil.verifyRefreshToken(refreshToken);

        Member member = memberRepository.findById(id);
        String currentRefreshToken = member.getRefreshToken();

        if (currentRefreshToken.equals(refreshToken)) {
            String newRefreshToken = JwtUtil.refreshToken(member);
            member.setRefreshToken(newRefreshToken);
            memberRepository.update(member);

            return newRefreshToken;
        } else {
            throw new IllegalStateException("리프레시 토큰 DB랑 다름");
        }
    }

    public void logout(TokenPayload payload) {
        Member member = memberRepository.findById(payload.id());
        member.setRefreshToken(null);
        memberRepository.update(member);
    }
}
