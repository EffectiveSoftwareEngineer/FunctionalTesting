package org.effectiveengineer.functionaltesting.user;

import io.vavr.control.Either;
import org.effectiveengineer.functionaltesting.model.Address;
import org.effectiveengineer.functionaltesting.validation.ValidationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UserManagementServiceBadTest {
    @Test
    void testCreate() {
        User user = new User(null, "a", "b", new Address("a", "b", "c", "d", "", "x"));
        UserRepository repo = Mockito.mock(UserRepository.class);
        Either<ValidationResult, UserId> eitherValidationOrId = new UserManagementService(repo).createUser(user);
        UserId userId = eitherValidationOrId.get();
        Mockito.verify(repo).store(Mockito.eq(userId), Mockito.eq(user));
        Assertions.assertTrue(userId != null);
        user = new User(new UserId("a1"), "a", "b", new Address("a", "b", "c", "d", "", "x"));
        eitherValidationOrId = new UserManagementService(repo).createUser(null);
        Assertions.assertTrue(eitherValidationOrId.getLeft().equals(new ValidationResult("User can't be null", "UCE0")));
        eitherValidationOrId = new UserManagementService(repo).createUser(user);
        Assertions.assertTrue(eitherValidationOrId.getLeft().equals(new ValidationResult("User ID should be empty", "UCE1")));
    }
}
