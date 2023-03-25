package antifraud.service;

import antifraud.common.Constants;
import antifraud.common.RoleEnum;
import antifraud.exception.BadRequestException;
import antifraud.exception.DuplicateException;

import antifraud.entities.*;
import antifraud.dtos.UserAccessDTO;
import antifraud.dtos.UserDTO;
import antifraud.dtos.UserRoleDTO;
import antifraud.repository.UserRepository;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static antifraud.dtos.ResponseMessage.deletedUserMessage;
import static antifraud.dtos.ResponseMessage.updatedAccessMessage;


@Setter
@Getter
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    Function<Optional<UserEntity>, UserEntity> unwrapUser = Optional::orElseThrow;

    Function<String, UserEntity> findByUsername = s -> userRepository.findByUsernameIgnoreCase(s).orElseThrow();
    Function<UserRoleDTO, UserEntity> findByRoleDTO = u -> userRepository.findByUsernameIgnoreCase(u.getUsername()).orElseThrow();
    Supplier<UserEntity> newUserEntity = UserEntity::new;

    BiFunction<UserDTO, PasswordEncoder, UserEntity> mapToUser = (dto, e) -> dto.mapToUser(newUserEntity.get(), e);

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        UserEntity user  = mapToUser.apply(userDTO, encoder);
        return setNewUserRole(user);
    }

    private UserDTO setNewUserRole(UserEntity user) {
        if (!userRepository.existsByRole(RoleEnum.ROLE_ADMINISTRATOR)) {
            user.setRole(RoleEnum.ROLE_ADMINISTRATOR);
            userRepository.save(user);
            return new UserDTO(user);
        }
        user.setRole(RoleEnum.ROLE_MERCHANT);
        user.setAccountLocked(true);
        userRepository.save(user);
        return new UserDTO(user);
    }

    @Override
    public Map<Object, Object> deleteUser(String username) {
        UserEntity user = findByUsername.apply(username);
        checkIfAdmin(user);
        userRepository.deleteUsersByUsernameIgnoreCase(user.getUsername());
        return deletedUserMessage(user.getUsername());
    }

    @Override
    public Map<Object, Object> updateAccess(UserAccessDTO userAccess) {
        UserEntity user = findByUsername(userAccess.getUsername());
        checkIfAdmin(user);
        user.setAccountLocked(userAccess.getOperation().equals("LOCK"));
        userRepository.save(user);
        return updatedAccessMessage(userAccess.getOperation().equals("LOCK"), user.getUsername());

    }
    @Override
    public UserDTO updateRole(UserRoleDTO userRole) {
        UserEntity user = findByRoleDTO.apply(userRole);
        checkUserRole(user, userRole.getRole());
        userRepository.updateRoleById(Constants.ENUM_MAP.get(userRole.getRole()),
                user.getId());
        return new UserDTO(user, userRole.getRole());
    }

    private void checkUserRole(UserEntity user, String newRole) {
        checkIfAdmin(user);
        if (user.getRoleText().equals(newRole)) {
            throw new DuplicateException();
        }
    }

    private void checkIfAdmin(UserEntity user) {
        if (user.getRole().equals(RoleEnum.ROLE_ADMINISTRATOR)) {
            throw new BadRequestException();
        }
    }


    public boolean doesUsernameExist(String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

    @Override
    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::new).toList();
    }

    @Override
    public UserEntity findByUsername(String username) {
        return findByUsername.apply(username);
    }


}