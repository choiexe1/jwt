package devjay.jwt.domain;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {
    private Long id;
    private String username;
    private String password;
    private Role role;
    private String refreshToken;
}
