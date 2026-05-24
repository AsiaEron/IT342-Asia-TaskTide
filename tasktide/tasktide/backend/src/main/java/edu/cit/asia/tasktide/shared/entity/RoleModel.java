package edu.cit.asia.tasktide.shared.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class RoleModel {

    @OneToMany(mappedBy = "role")
    private List<UserModel> users;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int role_id;

    @Column(name = "role_name")
    private String roleName;

    public int getRole_id() {
        return role_id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<UserModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserModel> users) {
        this.users = users;
    }
}
