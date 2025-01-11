package devjay.jwt.repository;

import devjay.jwt.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemberRepository {
    private long sequence = 0L;
    private static final Map<Long, Member> store = new HashMap<>();

    public Long save(Member member) {
        Optional<Member> exist = findByUsername(member.getUsername());

        if (exist.isPresent()) throw new IllegalStateException("exist");

        member.setId(++sequence);
        store.put(sequence, member);
        return sequence;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    public void update(Member member) {
        store.put(member.getId(), member);
    }

    public Optional<Member> findByUsername(String username) {
        return store.values().stream()
                .filter(m -> m.getUsername().equals(username))
                .findFirst();
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }
}
