package antifraud.dtos;

import antifraud.validation.DoesUserExist;
import antifraud.validation.ValidRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRoleDTO {

    @NotNull
    @DoesUserExist
    private String username;
    @NotNull
    @ValidRole
    private String role;

}
