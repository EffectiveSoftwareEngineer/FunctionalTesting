package org.effectiveengineer.functionaltesting.user;

import io.vavr.control.Either;
import org.effectiveengineer.functionaltesting.validation.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

public class UserManagementServiceBadTest {
    @Test
    void testCreate() {
        User user = new User(null, "a", "b", null);
        UserRepository repo = Mockito.mock(UserRepository.class);
        Either<ValidationResult, String> eitherValidationOrId = new UserManagementService(repo).createUser(user);
        String userId = eitherValidationOrId.get();
        Assertions.assertTrue(userId != null);
    }

}
