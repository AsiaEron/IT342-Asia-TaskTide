package edu.cit.asia.tasktide.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.cit.asia.tasktide.user.entity.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Integer> {

    UserModel findByEmail(String email);

    UserModel findByEmailIgnoreCase(String email);

    UserModel findByEmailAndPassword(String email, String password);

}
