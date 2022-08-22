package org.effectiveengineer.functionaltesting.user;

import java.util.Optional;

public interface UserRepository {

    /**
     * Store the user record
     * @param id identifier for the user. If a user with the given ID exists, the record will be overridden
     * @param user User record to store
     */
    void store(UserId id, User user);

    /**
     * Retrieves the user record
     * @param id identifier of the record
     * @return {@code Optional.empty} if user is not found or the User.
     */
    Optional<User> get(String id);
}
