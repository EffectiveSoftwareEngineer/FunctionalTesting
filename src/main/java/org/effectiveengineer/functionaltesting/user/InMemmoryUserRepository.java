package org.effectiveengineer.functionaltesting.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemmoryUserRepository implements UserRepository {
    private final Map<String, User> storage = new HashMap<>();

    @Override
    public void store(UserId id, User user) {
        storage.put(id.Id(), user);
    }

    @Override
    public Optional<User> get(String id) {
        return Optional.ofNullable(storage.get(id));
    }
}
