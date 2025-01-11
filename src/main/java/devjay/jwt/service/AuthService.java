package devjay.jwt.service;

import devjay.jwt.Tokens;
import devjay.jwt.domain.Member;
import devjay.jwt.domain.Role;
import devjay.jwt.repository.MemberRepository;
import devjay.jwt.web.JwtUtil;
import devjay.jwt.web.dto.LoginDTO;
import devjay.jwt.web.dto.RegisterDTO;
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
}
