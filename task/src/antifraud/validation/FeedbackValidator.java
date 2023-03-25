package antifraud.validation;

import antifraud.common.TransactionResult;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class FeedbackValidator implements ConstraintValidator<ValidFeedback, String> {


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        TransactionResult tR = null;
        try {
            tR = TransactionResult.valueOf(value);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
