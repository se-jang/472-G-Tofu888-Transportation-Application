package ku.cs.transport_application.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        boolean hasUppercase = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasLowercase = Pattern.compile("[a-z]").matcher(password).find();
        boolean hasDigit = Pattern.compile("[0-9]").matcher(password).find();
        boolean isLengthValid = password.length() >= 8 && password.length() <= 128;

        if (hasUppercase && hasLowercase && hasDigit && isLengthValid) {
            return true;
        }

        StringBuilder violationMessage = new StringBuilder("Password must contain: ");
        if (!hasUppercase) violationMessage.append("at least one uppercase letter, ");
        if (!hasLowercase) violationMessage.append("at least one lowercase letter, ");
        if (!hasDigit) violationMessage.append("at least one digit, ");
        violationMessage.append("and must be between 8 and 128 characters long.");

        context.buildConstraintViolationWithTemplate(violationMessage.toString())
                .addConstraintViolation()
                .disableDefaultConstraintViolation();

        return false;
    }

}
