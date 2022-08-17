package org.effectiveengineer.functionaltesting.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserRepository {
    private final Map<String, User> storage = new HashMap<>();

    public void store(String id, User user) {
        storage.put(id, user);
    }

    public Optional<User> get(String id) {
        return Optional.ofNullable(storage.get(id));
    }
}
