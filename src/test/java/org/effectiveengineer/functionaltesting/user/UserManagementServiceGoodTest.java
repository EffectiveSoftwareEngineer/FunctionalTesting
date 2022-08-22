package org.effectiveengineer.functionaltesting.user;

import io.vavr.control.Either;
import org.effectiveengineer.functionaltesting.model.Address;
import org.effectiveengineer.functionaltesting.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class UserManagementServiceGoodTest {
    /**
     * We use this method to create a User Builder with default attributes.
     * This method should be called from {@code with} method.
     *
     * @return User Builder with default values
     */
    private static User.UserBuilder defaultUser() {
        return User.builder()
                .id(null) // this set explicitly for readability purpose
                .firstName("firstName")
                .lastName("lastName")
                .address(defaultAddress);
    }

    /**
     * This is just a shortcut for {@code User.UserBuilder.build()} method call.
     * This improves readability and enables code like this:
     * <pre>{@code
     * with(defaultUser().firstName("John"))
     * }</pre>
     */
    private static User with(User.UserBuilder userBuilder) {
        return userBuilder.build();
    }

    /**
     * We use Mockito to mock the dependency of a System Under Test (UserManagementService)
     */
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final String defaultUserID = "userId";
    private final static Address defaultAddress = Address.builder()
            .country("country")
            .region("region")
            .postalCode("postalCode")
            .street("street")
            .street2("street2")
            .unit("unit")
            .build();

    /**
     * I prefer using this name in all tests as it makes it very clear what is being tested.
     */
    UserManagementService underTest = new UserManagementService(userRepository);

    @BeforeEach
    void setUp() {
        // Alternative to reset the mock is to set all values in the setUp function.
        // It is matter of choice and sometimes a language or a framework limitation.
        Mockito.reset(userRepository);
    }

    // Create User

    @Test
    void createUser() {
        // Given new user Record
        User user = with(defaultUser());
        // When creating a user
        UserId actual = underTest.createUser(user).get();
        // Then a UUID is returned
        assertThat(actual.Id()).isNotBlank();
        assertThat(actual.Id()).containsPattern("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
        // And the user is stored
        Mockito.verify(userRepository).store(Mockito.eq(actual), Mockito.eq(user));
    }

    @Test
    void createUser_GeneratesUniqueID() {
        // When creating multiple users
        int numberOfUsers = 100;// arbitrary number that is big enough to develop confidence in randomness
        Set<String> ids = IntStream.range(0, numberOfUsers)
                .mapToObj(ignore -> underTest.createUser(with(defaultUser())).get().Id())
                .collect(Collectors.toSet());// Set allows only unique entries
        // Then the ids are unique
        assertThat(ids).as("This indicates that there was not unique IDs generated").hasSize(numberOfUsers);
    }

    public static Stream<Arguments> invalidUsersToCreate() {
        return Stream.of(
                Arguments.of(null, new ValidationResult("User can't be null", "UCE0")),
                Arguments.of(with(defaultUser().id(new UserId("invalid"))), new ValidationResult("User ID should be empty", "UCE1")),
                Arguments.of(with(defaultUser().firstName(null)), new ValidationResult("User First Name is required", "UCE2")),
                Arguments.of(with(defaultUser().firstName("")), new ValidationResult("User First Name is required", "UCE2")),
                Arguments.of(with(defaultUser().lastName(null)), new ValidationResult("User Last Name is required", "UCE3")),
                Arguments.of(with(defaultUser().lastName("")), new ValidationResult("User Last Name is required", "UCE3"))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidUsersToCreate")
    void getUser_withInvalidId(User invalidUser, ValidationResult expectedValidationResult) {
        // Given
        Mockito.when(userRepository.get(Mockito.any())).thenThrow(new RuntimeException("Shouldn't be called"));
        // When
        Either<ValidationResult, UserId> actual = underTest.createUser(invalidUser);
        // Then there is a validation error
        assertThat(actual.isLeft()).as("Validation should have failed").isTrue();
        assertThat(actual.getLeft()).isEqualTo(expectedValidationResult);
        // And repo was not called
        Mockito.verify(userRepository, Mockito.never()).get(Mockito.any());
    }

    // Get User
    @Test
    void getUser() {
        // Given user exists
        User user = with(defaultUser());
        Mockito.when(userRepository.get(Mockito.eq(defaultUserID))).thenReturn(Optional.of(user));
        // When get the user
        Optional<User> actual = underTest.get(defaultUserID);
        // Then user is returned
        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(user);
    }

    @Test
    void getUser_userDoesNotExist() {
        // Given user exists
        Mockito.when(userRepository.get(Mockito.eq(defaultUserID))).thenReturn(Optional.empty());
        // When get the user
        Optional<User> actual = underTest.get(defaultUserID);
        // Then user is returned
        assertThat(actual).isNotPresent();
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "  "})
    @NullSource
    void getUser_withInvalidId(String invalidId) {
        // Given
        Mockito.when(userRepository.get(Mockito.any())).thenThrow(new RuntimeException("Shouldn't be called"));
        // When
        Optional<User> actual = underTest.get(invalidId);
        // Then
        assertThat(actual).isNotPresent();
        // And repo was not called
        Mockito.verify(userRepository, Mockito.never()).get(Mockito.any());
    }
}
