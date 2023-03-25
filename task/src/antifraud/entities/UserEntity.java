package antifraud.entities;




import antifraud.common.RoleEnum;
import jakarta.persistence.*;





import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;


import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String name;
    private String password;
    private boolean accountExpired = false;
    private boolean accountLocked = false;
    private boolean credentialsExpired = false;
    private boolean accountEnabled = true;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;


    public UserEntity(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.role = RoleEnum.ROLE_MERCHANT;
        this.accountLocked = true;
    }

    public UserEntity(String username, String name, String password, RoleEnum role) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public String getRoleText() {
        return role.getText();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserEntity that = (UserEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
