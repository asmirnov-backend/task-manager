package com.example.taskmanager.user;

import com.example.taskmanager.auth.dto.RegistrationDto;
import com.example.taskmanager.user.dto.ChangePasswordDto;
import com.example.taskmanager.user.dto.UpdateUserDto;
import com.example.taskmanager.user.exception.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow();
    }

    public User getReferenceById(UUID id) {
        return userRepository.getReferenceById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(UUID userId) {
        return userRepository.findById(userId);
    }

    public User findByIdOrThrow(UUID userId) throws UserNotFoundException {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public User create(RegistrationDto registrationDto) throws UserAlreadyExistException {
        if (userRepository.existsByEmail(registrationDto.getEmail())) throw new UserAlreadyExistByEmailException();
        if (userRepository.existsByUsername(registrationDto.getUsername()))
            throw new UserAlreadyExistByUsernameException();

        User user = modelMapper.map(registrationDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findByName(RoleName.ROLE_USER)));
        user.setId(UUID.randomUUID());

        return userRepository.save(user);
    }

    public User update(UUID id, UpdateUserDto updateUserDto) throws UserNotFoundException {
        User user = findByIdOrThrow(id);
        BeanUtils.copyProperties(updateUserDto, user);
        return userRepository.save(user);
    }

    public void changePassword(UUID id, ChangePasswordDto changePasswordDto) throws UserNotFoundException, CurrentPasswordIsIncorrectException {
        User user = findByIdOrThrow(id);
        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword()))
            throw new CurrentPasswordIsIncorrectException();

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
    }
}