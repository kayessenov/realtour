package Tourfirma.Tourfirma.controllers;

import Tourfirma.Tourfirma.entities.Users;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

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
}
