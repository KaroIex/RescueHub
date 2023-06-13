package com.example.rescuehubproject.accounts.services;

import com.example.rescuehubproject.accounts.entity.Log;
import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.accounts.execeptions.RoleException;
import com.example.rescuehubproject.accounts.execeptions.UserNotFoundException;
import com.example.rescuehubproject.accounts.repositories.UserRepository;
import com.example.rescuehubproject.accounts.request.RoleRequest;
import com.example.rescuehubproject.accounts.responses.DeleteUserResponse;
import com.example.rescuehubproject.accounts.responses.RoleResponse;
import com.example.rescuehubproject.accounts.responses.UserListResponse;
import com.example.rescuehubproject.accounts.util.LogEvent;
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
    private final LogService logService;

    public AdminService(UserRepository userRepository, LogService logService) {
        this.userRepository = userRepository;
        this.logService = logService;
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

                    String witchRole = role.toString().substring(5);
                    String who = user.getEmail();
                    String communicat = String.format("Grant role %s to %s", witchRole, who);

                    Log log = Log.builder().
                            setAction(LogEvent.GRANT_ROLE).
                            setSubject(user.getEmail()).
                            setObject(communicat).
                            setPath("/api/admin/user/role").
                            build();
                    logService.saveLog(log);

                }
            }
            case REMOVE -> {
                checkIfUserHasRole(role, user);
                user.removeRole(role);

                String witchRole = role.toString().substring(5);
                String who = user.getEmail();
                String communicat = String.format("Remove role %s from %s", witchRole, who);

                Log log = Log.builder().
                        setAction(LogEvent.REMOVE_ROLE).
                        setSubject(user.getEmail()).
                        setObject(communicat).
                        setPath("/api/admin/user/role").
                        build();
                logService.saveLog(log);

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

        User admin = userRepository.findById(1L).orElseThrow(UserNotFoundException::new);

        Log log = Log.builder()
                .setAction(LogEvent.DELETE_USER)
                .setSubject(admin.getEmail())
                .setObject(user.getEmail())
                .setPath("/api/admin/user")
                .build();
        logService.saveLog(log);

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

