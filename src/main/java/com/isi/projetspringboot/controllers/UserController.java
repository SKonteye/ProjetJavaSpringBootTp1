package com.isi.projetspringboot.controllers;

import com.isi.projetspringboot.entities.Utilisateur;
import com.isi.projetspringboot.repositories.IUtilisateur;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final IUtilisateur userRepository;

    UserController(IUtilisateur userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("listUsers")
    List<Utilisateur> ListUsers() {
        return (List<Utilisateur>) userRepository.findAll();
    }

    @PostMapping("/createUser")
    Utilisateur newEmployee(@RequestBody Utilisateur newUser) {
        return userRepository.save(newUser);
    }

    @GetMapping("/userById/{id}")
    Utilisateur one(@PathVariable Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @PutMapping("/updateUser/{id}")
    Utilisateur replaceEmployee(@RequestBody Utilisateur newUser, @PathVariable Long id) {
        return userRepository.findById(id)
                .map(employee -> {
                    employee.setNom(newUser.getNom());
                    employee.setEmail(newUser.getEmail());
                    return userRepository.save(employee);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return userRepository.save(newUser);
                });
    }

    @DeleteMapping("/deleteUser/{id}")
    void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
