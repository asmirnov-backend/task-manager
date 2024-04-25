package com.example.taskmanager.user;

import com.example.taskmanager.auth.dto.RegistrationDTO;
import com.example.taskmanager.task.Task;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private  final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User create(RegistrationDTO registrationDTO) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) throw new UserAlreadyExistsException("email");
        if (userRepository.existsByUsername(registrationDTO.getUsername())) throw new UserAlreadyExistsException("username");

        User user = modelMapper.map(registrationDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findByName(RoleName.ROLE_USER)));
        user.setId(UUID.randomUUID());

        return userRepository.save(user);
    }
}