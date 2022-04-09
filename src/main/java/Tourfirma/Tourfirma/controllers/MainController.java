package Tourfirma.Tourfirma.controllers;

import Tourfirma.Tourfirma.entities.Roles;
import Tourfirma.Tourfirma.entities.Users;
import Tourfirma.Tourfirma.repositories.RolesRepository;
import Tourfirma.Tourfirma.repositories.UserRepository;
import Tourfirma.Tourfirma.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class MainController {

    @Lazy
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @GetMapping(value = "/")
    public String index(Model model)
    {
        model.addAttribute("currentUser", getUser());
        return "index";
    }

    @GetMapping(value = "/signin")
    public String signin(Model model){

        model.addAttribute("currentUser", getUser());
        return "signin";
    }

    @GetMapping(value = "/accessdenied")
    public String accessdenied(Model model){
        model.addAttribute("currentUser", getUser());
        return "403";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model){
        model.addAttribute("currentUser", getUser());
        return "profile";
    }

    @GetMapping(value = "/signup")
    public String signUpPage(Model model){
        model.addAttribute("currentUser",getUser());
        return "signup";
    }

    @PostMapping("/tosignup")
    public String signUp(@RequestParam(name = "user_email") String email,
                         @RequestParam(name = "user_password")String password,
                         @RequestParam(name = "re_user_password")String rePassword,
                         @RequestParam(name = "user_fullName")String fullName){
        if(password.equals(rePassword)){

            Users newUser = userService.registerUser(new Users(null,email,password,fullName,null));
            if(newUser!=null && newUser.getId()!=null){
                return "redirect:/signin";
            }

        }
        return "redirect:/signup?error";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String adminPanel(Model model){
        model.addAttribute("currentUser", getUser());
        return "admin";
    }

    private Users getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            return (Users) authentication.getPrincipal();
        }
        return null;
    }

    @PostMapping("/getadmin")
    public String setAdmin(@RequestParam(name = "user_email_admin")String email, Model model){
        Users currentUser = getUser();
//        boolean isAdmin = false;
//        assert currentUser != null;
//        for(Roles role : currentUser.getRoles()){
//            if (role.getRole().equals("ROLE_ADMIN")) {
//                isAdmin = true;
//                break;
//            }
//        }
//        if(!isAdmin){
//            System.out.println("The current user is not an admin!");
//            return "redirect:/";
//        }
        Users userToDest = userRepository.findByEmail(email);
        if(userToDest == null){
            System.out.println("Cannot find user with this email!");
            return "redirect:/admin";
        }
        if(Objects.equals(userToDest.getEmail(), currentUser.getEmail())){
            System.out.println("Just you cannot!!");
            return "redirect:/admin";
        }
        List<Roles> roles = userToDest.getRoles();
        roles.add(rolesRepository.findByRole("ROLE_ADMIN"));
        userToDest.setRoles(roles);
        userRepository.deleteById(userToDest.getId());
        userRepository.save(userToDest);
        return "redirect:/";
    }

    @PostMapping("/updatepassword")
    public String updatePassword(@RequestParam(name = "old_password")String old_Pass,
                                 @RequestParam(name = "new_password")String newPass,
                                 @RequestParam(name = "retype_new_password")String reNewPass){
        if(newPass.equals(reNewPass)){
            if(userService.updatePassword(getUser(),old_Pass,newPass)){
                return "redirect:/profile?success";
            }
        }
        return "redirect:/profile?error";
    }
}
