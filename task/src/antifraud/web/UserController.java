
package antifraud.web;


import antifraud.dtos.UserAccessDTO;
import antifraud.dtos.UserDTO;
import antifraud.dtos.UserRoleDTO;
import antifraud.service.UserService;
import antifraud.validation.DoesUserExist;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/auth/")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("user")
    public ResponseEntity<UserDTO> handleNewUser(@Valid @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.saveUser(userDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("user/{username}")
    public ResponseEntity<Map<Object, Object>> handleDelete(@PathVariable("username") @DoesUserExist String username) {
        return new ResponseEntity<>(userService.deleteUser(username), HttpStatus.OK);
    }

    @GetMapping("list")
    public ResponseEntity<List<UserDTO>> returnUsers() {
        List<UserDTO> list = userService.getUsers();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PutMapping("role")
    public ResponseEntity<UserDTO> updateRole(@Valid @RequestBody UserRoleDTO userRole) {
        return new ResponseEntity<>(userService.updateRole(userRole), HttpStatus.OK);
    }


    @PutMapping("access")
    public ResponseEntity<Map<Object, Object>> updateAccess(@Valid @RequestBody UserAccessDTO userAccess) {
        return new ResponseEntity<>(userService.updateAccess(userAccess), HttpStatus.OK);
    }



}
