package antifraud.service;
import antifraud.dtos.UserAccessDTO;
import antifraud.dtos.UserDTO;
import antifraud.entities.UserEntity;
import antifraud.dtos.UserRoleDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {


    UserDTO saveUser(UserDTO userDTO);
    Map<Object, Object> deleteUser(String username);

    Map<Object, Object> updateAccess(UserAccessDTO userAccess);
    UserDTO updateRole(UserRoleDTO userRole);
    UserEntity findByUsername(String username);

    boolean doesUsernameExist(String username);

    List<UserDTO> getUsers();

}
