package antifraud.validation;

import antifraud.common.Constants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.*;
import org.springframework.stereotype.Component;


@Component
@NoArgsConstructor
public class RoleValidator implements ConstraintValidator<ValidRole, String> {


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (Constants.ENUM_MAP.containsKey(value)) {
            return true;
        }
        return false;
    }
}
