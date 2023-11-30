package com.isi.projetspringboot.repositories;

import com.isi.projetspringboot.entities.Utilisateur;
import org.springframework.data.repository.CrudRepository;

public interface IUtilisateur extends CrudRepository<Utilisateur, Long> {
}
