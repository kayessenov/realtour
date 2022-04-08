package Tourfirma.Tourfirma.services;

import Tourfirma.Tourfirma.entities.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    Users registerUser(Users user);

}
