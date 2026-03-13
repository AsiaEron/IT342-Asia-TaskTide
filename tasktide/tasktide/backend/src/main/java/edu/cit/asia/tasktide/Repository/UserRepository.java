package edu.cit.asia.tasktide.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.cit.asia.tasktide.Model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Integer> {

    UserModel findByEmail(String email);

    UserModel findByEmailAndPassword(String email, String password);

}