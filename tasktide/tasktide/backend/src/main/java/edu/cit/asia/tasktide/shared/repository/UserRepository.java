package edu.cit.asia.tasktide.shared.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.cit.asia.tasktide.shared.entity.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Integer> {

    UserModel findByEmail(String email);

    UserModel findByEmailAndPassword(String email, String password);

}
