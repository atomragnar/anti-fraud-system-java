package antifraud.dtos;


import antifraud.entities.Transaction;
import antifraud.validation.IsValidIp;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IpDTO {

    @IsValidIp
    String ip;


}
