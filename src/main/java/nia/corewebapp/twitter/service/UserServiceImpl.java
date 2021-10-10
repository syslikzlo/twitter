package nia.corewebapp.twitter.service;

import nia.corewebapp.twitter.entity.Role;
import nia.corewebapp.twitter.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nia.corewebapp.twitter.repository.RoleRepository;
import nia.corewebapp.twitter.repository.UserRepository;

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow();
        user.getRoles().size();
        return user;
    }

    @Override
    public void create(String username, String password) {
        if (userRepository.findByUsername(username).isPresent())
            throw new EntityExistsException();

        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setRoles(List.of(roleRepository.findByName(Role.USER).orElseThrow()));
        user.setdtCreated(LocalDateTime.now());
        user.setIsActive(true);
        userRepository.save(user);

    }
}
