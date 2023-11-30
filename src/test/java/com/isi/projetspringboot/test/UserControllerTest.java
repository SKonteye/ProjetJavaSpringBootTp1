package com.isi.projetspringboot.test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isi.projetspringboot.controllers.UserController;
import com.isi.projetspringboot.entities.Utilisateur;
import com.isi.projetspringboot.repositories.IUtilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    @Mock
    private IUtilisateur userRepository;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testListUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(List.of(new Utilisateur()));

        mockMvc.perform(get("/listUsers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testCreateUser() throws Exception {
        Utilisateur newUser = new Utilisateur();
        newUser.setNom("John Doe");
        newUser.setEmail("john@example.com");
        when(userRepository.save(any(Utilisateur.class))).thenReturn(newUser);
        mockMvc.perform(post("/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
        verify(userRepository, times(1)).save(any(Utilisateur.class));
    }

    @Test
    void testUserById() throws Exception {
        Utilisateur user = new Utilisateur();
        user.setId(1L);
        user.setNom("John Doe");
        user.setEmail("john@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/userById/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testReplaceEmployee() throws Exception {
        Utilisateur existingUser = new Utilisateur();
        existingUser.setId(1L);
        existingUser.setNom("John Doe");
        existingUser.setEmail("john@example.com");

        Utilisateur updatedUser = new Utilisateur();
        updatedUser.setNom("Jane Doe");
        updatedUser.setEmail("jane@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(ArgumentMatchers.any(Utilisateur.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/updateUser/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane@example.com"));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(ArgumentMatchers.any(Utilisateur.class));
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userRepository).deleteById(1L);
        mockMvc.perform(delete("/deleteUser/{id}", 1))
                .andExpect(status().isOk());
        verify(userRepository, times(1)).deleteById(1L);
    }

}
