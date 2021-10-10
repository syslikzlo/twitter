package nia.corewebapp.twitter.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Role {

    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";

    @Id
    @Column(name = "role_id")
    private long roleId;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public long getRoleId() { return roleId; }

    public void setRoleId(long roleId) { this.roleId = roleId; }

    public String getName() { return name; }

    public void setName(String name) {this.name = name;}

    public List<User> getUsers() { return users; }

    public void setUsers(List<User> users) { this.users = users;
    }
}
