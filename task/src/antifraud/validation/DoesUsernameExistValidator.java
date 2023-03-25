package antifraud.validation;

import antifraud.exception.EntityNotFoundException;
import antifraud.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class DoesUsernameExistValidator implements ConstraintValidator<DoesUserExist, String> {

    private final UserService userService;

    public DoesUsernameExistValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (userService.doesUsernameExist(value)) {
            return true;
        }
        throw new EntityNotFoundException();
    }
}
