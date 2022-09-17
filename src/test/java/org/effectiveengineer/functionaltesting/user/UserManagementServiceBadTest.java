package org.effectiveengineer.functionaltesting.user;

import io.vavr.control.Either;
import org.effectiveengineer.functionaltesting.model.Address;
import org.effectiveengineer.functionaltesting.validation.ValidationError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.effectiveengineer.functionaltesting.assertion.EitherValidationErrorAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

public class UserManagementServiceBadTest {
    @Test
    void testCreate() {
        User user = new User(null, "a", "b", new Address("a", "b", "c", "d", "", "x"));
        UserRepository repo = Mockito.mock(UserRepository.class);
        Either<ValidationError, UserId> eitherValidationOrId = new UserManagementService(repo).createUser(user);
        UserId userId = eitherValidationOrId.get();
        Mockito.verify(repo).store(Mockito.eq(userId), Mockito.eq(user));
        Assertions.assertTrue(userId != null);
        user = new User(new UserId("a1"), "a", "b", new Address("a", "b", "c", "d", "", "x"));
        eitherValidationOrId = new UserManagementService(repo).createUser(null);
        Assertions.assertTrue(eitherValidationOrId.getLeft().equals(new ValidationError("User can't be null", "UCE0")));
        eitherValidationOrId = new UserManagementService(repo).createUser(user);
        Assertions.assertTrue(eitherValidationOrId.getLeft().equals(new ValidationError("User ID should be empty", "UCE1")));
    }

    // This test shows refactored testCreate after "A failure of a test should clearly indicate what has failed" chapter
    @Test
    void testCreate_afterAssertImprovements() {
        User user = new User(null, "a", "b", new Address("a", "b", "c", "d", "", "x"));
        UserRepository repo = Mockito.mock(UserRepository.class);
        Either<ValidationError, UserId> eitherValidationOrId = new UserManagementService(repo).createUser(user);
        UserId userId = eitherValidationOrId.get();
        Mockito.verify(repo).store(Mockito.eq(userId), Mockito.eq(user));
        assertThat(userId).isNotNull();
        user = new User(new UserId("a1"), "a", "b", new Address("a", "b", "c", "d", "", "x"));
        eitherValidationOrId = new UserManagementService(repo).createUser(null);
        assertThat(eitherValidationOrId).validationFailed()
                .validationMessageIs("User can't be null")
                .validationCodeIs("UCE0");
        eitherValidationOrId = new UserManagementService(repo).createUser(user);
        assertThat(eitherValidationOrId).validationFailed()
                .validationMessageIs("User ID should be empty")
                .validationCodeIs("UCE1");
    }

    // This shows refactoring after "When one thing is broken, one test fails" chapter

    @Test
    void testCreate_afterOneThing() {
        User user = new User(null, "a", "b", new Address("a", "b", "c", "d", "", "x"));
        UserRepository repo = Mockito.mock(UserRepository.class);
        Either<ValidationError, UserId> eitherValidationOrId = new UserManagementService(repo).createUser(user);
        UserId userId = eitherValidationOrId.get();
        assertThat(userId).isNotNull();
        Mockito.verify(repo).store(Mockito.eq(userId), Mockito.eq(user));
    }

    @Test
    void testCreate_afterOneThing_validate_user_null() {
        UserRepository repo = Mockito.mock(UserRepository.class);
        Either<ValidationError, UserId> eitherValidationOrId = new UserManagementService(repo).createUser(null);
        assertThat(eitherValidationOrId).validationFailed()
                .validationMessageIs("User can't be null")
                .validationCodeIs("UCE0");
    }

    @Test
    void testCreate_afterOneThing_validate_userId_null() {
        UserRepository repo = Mockito.mock(UserRepository.class);
        User user = new User(new UserId("a1"), "a", "b", new Address("a", "b", "c", "d", "", "x"));
        Either<ValidationError, UserId> eitherValidationOrId = new UserManagementService(repo).createUser(user);
        assertThat(eitherValidationOrId).validationFailed()
                .validationMessageIs("User ID should be empty")
                .validationCodeIs("UCE1");
    }


    // Will be needed later.
    private static void assertUser(User expectedUser, User actualUser) {
        Assertions.assertEquals(expectedUser.getId(), actualUser.getId());
        Assertions.assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
        Assertions.assertEquals(expectedUser.getLastName(), actualUser.getLastName());
        Address expectedAddress = expectedUser.getAddress();
        Address actualAddress = actualUser.getAddress();
        Assertions.assertEquals(expectedAddress.getCountry(), actualAddress.getCountry());
        Assertions.assertEquals(expectedAddress.getPostalCode(), actualAddress.getPostalCode());
        Assertions.assertEquals(expectedAddress.getRegion(), actualAddress.getRegion());
        Assertions.assertEquals(expectedAddress.getStreet(), actualAddress.getStreet());
        Assertions.assertEquals(expectedAddress.getStreet2(), actualAddress.getStreet2());
        Assertions.assertEquals(expectedAddress.getUnit(), actualAddress.getUnit());
    }
}
