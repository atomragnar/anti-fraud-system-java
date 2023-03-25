package antifraud.validation;

import antifraud.exception.DuplicateException;
import antifraud.service.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;


@Component
public class UsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserService userService;

    public UsernameValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!userService.doesUsernameExist(value)) {
            return true;
        }
        throw new DuplicateException();
    }
}
