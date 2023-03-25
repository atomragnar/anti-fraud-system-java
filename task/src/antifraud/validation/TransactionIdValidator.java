package antifraud.validation;


import antifraud.exception.EntityNotFoundException;
import antifraud.service.TransactionService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.*;
import org.springframework.stereotype.Component;


@Component
public class TransactionIdValidator implements ConstraintValidator<ValidTransactionId, Long> {

    private final TransactionService transactionService;

    public TransactionIdValidator(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {

        if (value == null) {
            return false;
        }

        if (transactionService.existsById(value)) {
            return true;
        } else {
            throw new EntityNotFoundException();
        }

    }
}
