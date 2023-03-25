package antifraud.dtos;

import antifraud.entities.UserEntity;
import antifraud.validation.UniqueUsername;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    Long id;

    @NotNull
    @NotBlank
    String name;
    @NotNull
    @UniqueUsername
    String username;
    @NotNull
    @NotBlank
    String password;

    String role;

    public UserDTO(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.name = userEntity.getName();
        this.username = userEntity.getUsername();
        this.role = userEntity.getRoleText();
    }

    public UserDTO(UserEntity userEntity, String role) {
        this.id = userEntity.getId();
        this.name = userEntity.getName();
        this.username = userEntity.getUsername();
        this.role = role;
    }

    public Long getId() {
        return this.id;
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return this.role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserEntity mapToUser(UserEntity userEntity, PasswordEncoder encoder) {
        userEntity.setName(this.name);
        userEntity.setUsername(this.username);
        userEntity.setPassword(encoder.encode(this.password));
        return userEntity;
    }

}

