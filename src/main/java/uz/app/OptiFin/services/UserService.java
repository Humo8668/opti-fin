package uz.app.OptiFin.services;

import uz.app.Anno.service.BaseService;
import uz.app.Anno.AnnoContext;
import uz.app.Anno.orm.*;
import uz.app.Anno.util.HttpMethod;
import uz.app.OptiFin.App;
import uz.app.OptiFin.HerokuDB;
import uz.app.OptiFin.entities.User;
import uz.app.OptiFin.repos.UsersRepository;
import uz.app.AnnoDBC.PoolConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.*;

import com.google.gson.Gson;

@Service("/users")
public class UserService extends BaseService {
    UsersRepository usersRepo;
    Gson gson;

    public UserService() {
        try {
            App.Init();
            Class.forName("org.postgresql.Driver");
            AnnoContext.setPoolConnection(new PoolConnection(HerokuDB.getConnectionUrl(), HerokuDB.getLogin(), HerokuDB.getPassword()));
            usersRepo = new UsersRepository();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        gson = new Gson();
    }

    @Route(value="/getall", method = HttpMethod.GET)
    public void getAll(HttpServletRequest req, HttpServletResponse res)
        throws IOException, SQLException
    {
        PrintWriter out = res.getWriter();
        User[] users;
        try {
            users = usersRepo.getAll();
        } catch (SQLException ex) {
            ex.printStackTrace();
            res.sendError(500, "Error occurred: " + ex.getMessage());
            return;
        }

        String json = gson.toJson(users);
        res.setContentType("application/json");
        res.setStatus(200);
        out.print(json);
        return;
    }
}
