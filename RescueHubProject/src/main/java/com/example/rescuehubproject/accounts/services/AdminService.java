package com.example.rescuehubproject.accounts.services;

import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.accounts.execeptions.RoleException;
import com.example.rescuehubproject.accounts.execeptions.UserNotFoundException;
import com.example.rescuehubproject.accounts.repositories.UserRepository;
import com.example.rescuehubproject.accounts.request.RoleRequest;
import com.example.rescuehubproject.accounts.responses.DeleteUserResponse;
import com.example.rescuehubproject.accounts.responses.RoleResponse;
import com.example.rescuehubproject.accounts.responses.UserListResponse;
import com.example.rescuehubproject.accounts.util.Role;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.rescuehubproject.accounts.util.Role.ROLE_ADMINISTRATOR;
import static com.example.rescuehubproject.accounts.util.RoleOperation.REMOVE;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponseEntity<?> updateRole(RoleRequest roleRequest) {
        User user = userRepository.findByEmailIgnoreCase(roleRequest.user())
                .orElseThrow(UserNotFoundException::new);

        Role role = Role.getRole(roleRequest.role());

        List<Role> userRoles = user.getRoles();
        checkIfUserIsNotAdministrator(user, roleRequest);
        checkIfUserCanCombineRoles(role, user);

        switch (roleRequest.operation()) {
            case GRANT -> {
                if (!userRoles.contains(role)) {
                    user.addRole(role);
                }
            }
            case REMOVE -> {
                checkIfUserHasRole(role, user);
                user.removeRole(role);
            }
            default -> throw new IllegalArgumentException("Invalid operation: " + roleRequest.operation());
        }

        checkIfUserHasAtLeastOneRole(user);
        userRepository.save(user);

        return new ResponseEntity<>(RoleResponse.response(user), HttpStatus.OK);
    }


    public ResponseEntity<?> getAllUsers() {
        List<UserListResponse> responseList = userRepository.findAll()
                .stream()
                .map(UserListResponse::response)
                .toList();
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteUser(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(UserNotFoundException::new);
        if (user.getRoles().contains(ROLE_ADMINISTRATOR)) {
            throw new RoleException("Can't remove ADMINISTRATOR role!");
        }
        userRepository.delete(user);
        return new ResponseEntity<>(DeleteUserResponse.response(user), HttpStatus.OK);
    }

    private void checkIfUserHasRole(Role role, User user) {
        if (!user.getRoles().contains(role)) {
            throw new RoleException("The user does not have a role!");
        }
    }

    private void checkIfUserHasAtLeastOneRole(User user) {
        if (user.getRoles().isEmpty()) {
            throw new RoleException("The user must have at least one role!");
        }
    }

    private void checkIfUserIsNotAdministrator(User user, RoleRequest roleRequest) {
        if (user.getRoles().contains(ROLE_ADMINISTRATOR) && roleRequest.operation().equals(REMOVE)) {
            throw new RoleException("Can't remove ADMINISTRATOR role!");
        }
    }

    private void checkIfUserCanCombineRoles(Role role, User user) {
        if (user.getRoles().contains(ROLE_ADMINISTRATOR) || role.equals(ROLE_ADMINISTRATOR)) {
            throw new RoleException("The user cannot combine administrative and business roles!");
        }
    }
}

