package uz.app.OptiFin.repos;

import uz.app.OptiFin.entities.User;

import uz.app.Anno.orm.*;

public class UsersRepository extends Repository<User> {
    public UsersRepository() throws Exception {
        SetTargetEntity(User.class);
    }
}
