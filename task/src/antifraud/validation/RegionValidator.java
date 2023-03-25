package antifraud.validation;

import antifraud.common.Constants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class RegionValidator implements ConstraintValidator<ValidRegion, String> {


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) {
            return false;
        }

        if (Constants.REGION_SET.contains(value)) {
            return true;
        }
        return false;
    }
}
