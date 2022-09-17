package org.effectiveengineer.functionaltesting.assertion;

import io.vavr.control.Either;
import org.assertj.core.api.AbstractAssert;
import org.effectiveengineer.functionaltesting.validation.ValidationError;

public class EitherValidationErrorAssert<T> extends AbstractAssert<EitherValidationErrorAssert<T>, Either<ValidationError, T>> {
    protected EitherValidationErrorAssert(Either<ValidationError, T> actual) {
        super(actual, EitherValidationErrorAssert.class);
    }

    public static <T> EitherValidationErrorAssert<T> assertThat(Either<ValidationError, T> actual) {
        return new EitherValidationErrorAssert<>(actual);
    }

    public EitherValidationErrorAssert<T> validationFailed() {
        isNotNull();
        if (actual.isRight()) {
            failWithMessage("Expected validation error but got value %s", actual.get());
        }
        return this;
    }

    public EitherValidationErrorAssert<T> validationMessageIs(String expectedValidationMessage) {
        isNotNull();
        validationFailed();
        String actualMessage = actual.getLeft().message();
        if (!actualMessage.equals(expectedValidationMessage)) {
            failWithMessage("Expected validation message [%s], but got [%s]",
                    expectedValidationMessage, actualMessage
            );
        }

        return this;
    }

    public EitherValidationErrorAssert<T> validationCodeIs(String expectedValidationCode) {
        isNotNull();
        validationFailed();
        String actualCode = actual.getLeft().code();
        if (!actualCode.equals(expectedValidationCode)) {
            failWithMessage("Expected validation code [%s], but got [%s]",
                    expectedValidationCode, actualCode
            );
        }

        return this;
    }
}
