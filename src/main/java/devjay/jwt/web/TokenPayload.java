package devjay.jwt.web;

import java.util.HashMap;
import java.util.Map;

public record TokenPayload(
        Long id,
        String username
) {

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("username", username);

        return map;
    }
}
