package edu.cit.asia.tasktide.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.cit.asia.tasktide.Model.RoleModel;

public interface RoleRepository extends JpaRepository<RoleModel, Integer> {

    RoleModel findByRoleName(String role_name);

}