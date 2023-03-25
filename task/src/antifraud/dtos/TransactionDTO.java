package antifraud.dtos;

import antifraud.validation.IsValidIp;
import antifraud.validation.ValidRegion;

import antifraud.validation.ValidateCardNumber;
import jakarta.validation.constraints.*;
import lombok.*;



@Data
@NoArgsConstructor
public class TransactionDTO {

    @NotNull
    @Positive
    private long amount;

    @IsValidIp
    private String ip;

    @ValidateCardNumber
    private String number;

    @ValidRegion
    String region;

    /*@Pattern(regexp = "\\d\\d\\d\\d-\\d\\d-\\d\\d\\w\\d\\d:\\d\\d:\\d\\d")*/

    String date;

}
