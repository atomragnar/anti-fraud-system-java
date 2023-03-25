package antifraud.dtos;

import antifraud.validation.DoesUserExist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAccessDTO {

    // TODO set validation for username and the operation

    @NotNull
    @DoesUserExist
    private String username;

    @NotNull
    @NotBlank
    private String operation;


}
