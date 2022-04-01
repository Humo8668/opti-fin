package uz.app.OptiFin.entities;

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

    public User() {}

    @Override
    public boolean isValid() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void validate() throws AnnoValidationException {
        // TODO Auto-generated method stub
        
    }
}
