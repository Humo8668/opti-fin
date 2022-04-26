package uz.app.OptiFin.entities;

import uz.app.Anno.orm.annotations.*;
import uz.app.Anno.orm.*;

@Schema("public")
@Table("Users")
public class User extends BaseEntity {
    @Id
    @Generated
    @Column("id")
    long id;
    
    @Column("login")
    String login;

    @Column("full_name")
    String fullName;

    @Column("password_hashed")
    String passwordHashed;

    String password;

    public User() {}

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void validate() throws AnnoValidationException {
        if(login == null || "".equals(login)) {
            throw new AnnoValidationException("Empty login", "login");
        }
        if(fullName == null || "".equals(fullName)) {
            throw new AnnoValidationException("Empty fullName", "fullName");
        }
        if(password == null || "".equals(password)) {
            throw new AnnoValidationException("Empty password", "password");
        }
        passwordHashed = "hashed-" + password;
    }
}
