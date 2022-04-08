package Tourfirma.Tourfirma.services.impl;

import Tourfirma.Tourfirma.entities.Roles;
import Tourfirma.Tourfirma.entities.Users;
import Tourfirma.Tourfirma.repositories.RolesRepository;
import Tourfirma.Tourfirma.repositories.UserRepository;
import Tourfirma.Tourfirma.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(email);
        if(user!=null){
            return user;
        }
        else{
            throw new UsernameNotFoundException("USER NOT FOUND");
        }
    }

    @Override
    public Users registerUser(Users user) {

        Users checkUser = userRepository.findByEmail(user.getEmail());
        if(checkUser==null){

            Roles userRole = rolesRepository.findByRole("ROLE_USER");
            List<Roles> roles = new ArrayList<>();
            roles.add(userRole);
            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }

        return null;

    }

    @Override
    public boolean updatePassword(Users user, String oldPassword, String newPassword) {

        Users checkUser = userRepository.findByEmail(user.getEmail());

        if(passwordEncoder.matches(oldPassword,checkUser.getPassword())){
            user.setPassword(passwordEncoder.encode(newPassword));
            return userRepository.save(user)!=null;
        }
        return false;
    }
}
