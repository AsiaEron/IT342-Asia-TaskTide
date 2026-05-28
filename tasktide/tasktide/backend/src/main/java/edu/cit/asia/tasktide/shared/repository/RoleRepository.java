package edu.cit.asia.tasktide.shared.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.cit.asia.tasktide.user.entity.RoleModel;

public interface RoleRepository extends JpaRepository<RoleModel, Integer> {

    RoleModel findByRoleName(String roleName);

}
