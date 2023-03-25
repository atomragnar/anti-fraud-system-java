package antifraud.validation;

import antifraud.exception.BadRequestException;

import antifraud.service.AntifraudService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

/*
*  Credit card numberformat used in the project 4000008449433403
*  Validation creditcard number
* */

@Component
public class CardNumberValidator implements ConstraintValidator<ValidateCardNumber, String> {

    private final AntifraudService antifraudService;

    public CardNumberValidator(AntifraudService antifraudService) {
        this.antifraudService = antifraudService;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        antifraudService.luhnCheck(value);
        return true;
    }
}
