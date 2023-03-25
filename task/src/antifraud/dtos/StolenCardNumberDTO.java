package antifraud.dtos;


import antifraud.validation.ValidateCardNumber;
import lombok.Data;

@Data
public class StolenCardNumberDTO {

    @ValidateCardNumber
    String number;

}
