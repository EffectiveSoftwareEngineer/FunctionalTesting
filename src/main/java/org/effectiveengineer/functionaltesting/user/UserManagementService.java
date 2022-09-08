package org.effectiveengineer.functionaltesting.user;

import io.vavr.control.Either;
import org.effectiveengineer.functionaltesting.validation.ValidationError;

import java.util.Optional;
import java.util.UUID;

public class UserManagementService {
    private final UserRepository userRepository;

    public UserManagementService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Validates the input and stores the {@code user} in the repository.
     *
     * @param user to store.
     * @return Either validation results, if any validation has failed or a new userId
     */
    public Either<ValidationError, UserId> createUser(final User user) {
        Optional<ValidationError> validationResult = validateUser(user);
        if (validationResult.isPresent()) {
            return Either.left(validationResult.get());
        }
        UserId userId = new UserId(UUID.randomUUID().toString());
        userRepository.store(userId, user);
        return Either.right(userId);
    }

    private Optional<ValidationError> validateUser(User user) {
        if (user == null)
            return Optional.of(new ValidationError("User can't be null", "UCE0"));
        if (user.getId() != null)
            return Optional.of(new ValidationError("User ID should be empty", "UCE1"));
        if (isBlank(user.getFirstName()))
            return Optional.of(new ValidationError("User First Name is required", "UCE2"));
        if (isBlank(user.getLastName()))
            return Optional.of(new ValidationError("User Last Name is required", "UCE3"));
        return Optional.empty();
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().length() == 0;
    }

    public Optional<User> get(String id) {
        if (isBlank(id)) return Optional.empty();
        return userRepository.get(id);
    }
}
