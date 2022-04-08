package Tourfirma.Tourfirma.services.impl;

import Tourfirma.Tourfirma.entities.Users;
import Tourfirma.Tourfirma.repositories.UserRepository;
import Tourfirma.Tourfirma.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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
}
