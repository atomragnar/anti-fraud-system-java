package antifraud.dtos;

import antifraud.validation.ValidFeedback;
import antifraud.validation.ValidTransactionId;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FeedbackDTO {

    @ValidTransactionId
    Long transactionId;
    @ValidFeedback
    String feedback;

}
