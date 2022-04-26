package uz.app.OptiFin.services;

import uz.app.Anno.service.BaseService;
import uz.app.Anno.service.annotations.*;
import uz.app.Anno.Anno;
import uz.app.Anno.orm.AnnoValidationException;
import uz.app.Anno.orm.Repository;
import uz.app.Anno.util.HttpMethod;
import uz.app.OptiFin.App;
import uz.app.OptiFin.entities.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service("/users")
public class UserService extends BaseService {
    Repository<User> usersRepo;
    Gson gson;

    public UserService() {
        try {
            usersRepo = (Repository<User>)App.getRepoFactory().getRepository(User.class);
        } catch (Exception e) {
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

    @Route(value = "/getById", method = HttpMethod.GET)
    void getById(HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException
    {
        PrintWriter out = res.getWriter();

        Object userIdParameter = req.getParameter("id");
        if(userIdParameter == null || "".equals(userIdParameter))
        {
            res.setContentType("application/json");
            res.setStatus(200);
            out.print("{}");
            return;
        }
        User user;
        int userId = Integer.parseInt(userIdParameter.toString());
        try {
            user = usersRepo.getById(userId);
        } catch (SQLException ex) {
            ex.printStackTrace();
            res.sendError(500, "Error occurred: " + ex.getMessage());
            return;
        }

        String json = "";
        if(user != null)
            json = gson.toJson(user);
        else
            json = "{}";
        res.setContentType("application/json");
        res.setStatus(200);
        out.print(json);
        return;
    }

    @Route(value="/getByLogin", method = HttpMethod.GET)
    void getByLogin(HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException
    {
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setStatus(200);
        User[] users;
        String login = req.getParameter("login");
        if(login == null)
            login = "";
            
        try {
            users = usersRepo.where("login").like(login).get();
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

    @Route(value="/save", method = HttpMethod.POST)
    void saveUser(HttpServletRequest req, HttpServletResponse res) 
        throws IOException, ServletException
    {
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setStatus(200);

        String requestJson = getReqBody(req);
        
        User user = gson.fromJson(requestJson, User.class);
                    /*new User(-1, 
                            requestMap.get("login").toString(), 
                            requestMap.get("fullName").toString(), 
                            requestMap.get("email").toString(), 
                            requestMap.get("password").toString(), 
                            requestMap.get("state").toString());*/
        try {
            usersRepo.save(user);
            HashMap<String, Object> resMap = new HashMap<String, Object>();
            resMap.put("errorCode", 0);
            resMap.put("message", "Success");
            out.println(gson.toJson(resMap));
        } catch (SQLException e) {
            res.setStatus(500);
            e.printStackTrace();
        } catch (AnnoValidationException e) {
            HashMap<String, Object> resMap = new HashMap<String, Object>();
            resMap.put("errorCode", 1);
            resMap.put("invalidField", e.getInvalidFieldName());
            resMap.put("message", e.getMessage());
            out.println(gson.toJson(resMap));
        }
    }

    @Route(value="/update", method = HttpMethod.PUT)
    void updateUser(HttpServletRequest req, HttpServletResponse res) 
        throws IOException, ServletException
    {
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setStatus(200);

        String requestJson = getReqBody(req);
        
        User user = gson.fromJson(requestJson, User.class);
        try {
            usersRepo.update(user);
            HashMap<String, Object> resMap = new HashMap<String, Object>();
            resMap.put("errorCode", 0);
            resMap.put("message", "Success");
            out.println(gson.toJson(resMap));
        } catch (SQLException e) {
            res.setStatus(500);
            e.printStackTrace();
        } catch (AnnoValidationException e) {
            HashMap<String, Object> resMap = new HashMap<String, Object>();
            resMap.put("errorCode", 1);
            resMap.put("invalidField", e.getInvalidFieldName());
            resMap.put("message", e.getMessage());
            out.println(gson.toJson(resMap));
        }
    }

    @Route(value="/delete", method = HttpMethod.DELETE)
    void deleteUser(HttpServletRequest req, HttpServletResponse res) 
        throws IOException, ServletException
    {
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setStatus(200);

        String requestJson = getReqBody(req);
        Type type = new TypeToken<HashMap<String, Object>>(){}.getType();

        HashMap<String, Object> requestMap = gson.fromJson(requestJson, type);
        long userId = (long)Double.parseDouble(requestMap.get("userId").toString());

        try {
            usersRepo.delete(userId);
            HashMap<String, Object> resMap = new HashMap<String, Object>();
            resMap.put("errorCode", 0);
            resMap.put("message", "Success");
            out.println(gson.toJson(resMap));
        } catch (SQLException e) {
            res.setStatus(500);
            e.printStackTrace();
        }
    }

    /*@Route(value="/annotated", method = HttpMethod.GET)
    void isAnnotated(HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException
    {
        PrintWriter out = res.getWriter();
        res.setStatus(200);

        for(Field field : Anno.forEntity(User.class).getAllFields()) {
            out.println(field.getName() + " : " + Anno.forEntity(User.class).isAnnotatedAsColumn(field));
            out.println("<br>");
        }
    }*/
}
