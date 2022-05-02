package uz.app.OptiFin.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import uz.app.Anno.orm.AnnoValidationException;
import uz.app.Anno.orm.Repository;
import uz.app.Anno.orm.RepositoryFactory;
import uz.app.Anno.service.BaseService;
import uz.app.Anno.service.annotations.Route;
import uz.app.Anno.service.annotations.Service;
import uz.app.Anno.util.HttpMethod;
import uz.app.OptiFin.App;
import uz.app.OptiFin.entities.Category;
import uz.app.OptiFin.entities.Note;

@Service("/category")
public class CategoryService extends BaseService {

    private Repository<Category> categoryRepo;
    Gson gson;

    public CategoryService() {
        try {
            categoryRepo = (Repository<Category>)App.getRepoFactory().getRepository(Category.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        gson = App.getGsonBuilder().create();
    }

    @Route(value = "/getall", method = HttpMethod.GET)
    void getAll(HttpServletRequest req, HttpServletResponse res) 
        throws IOException
    {
        res.setStatus(200);
        res.setContentType("application/json; charset=utf-8");
        PrintWriter out = res.getWriter();

        Category[] categories;
        try {
            categories = categoryRepo.getAll();
        } catch (SQLException e) {
            res.setStatus(500);
            out.println("[]");
            e.printStackTrace();
            return;
        }   
        
        out.print(gson.toJson(categories));
        return;
    }

    @Route(value = "/getbycode", method = HttpMethod.GET)
    void getByCode(HttpServletRequest req, HttpServletResponse res) 
        throws IOException
    {
        res.setContentType("application/json; charset=utf-8");
        PrintWriter out = res.getWriter();

        String requestedCode = "";
        if(req.getParameter("code") != null)
            requestedCode = req.getParameter("code");
        else {
            out.println("[]");
            return;
        }


        Category[] categories;
        try {
            categories = categoryRepo.where("code").like(requestedCode).get();
        } catch (SQLException e) {
            res.setStatus(500);
            out.println("[]");
            e.printStackTrace();
            return;
        }
        
        out.print(gson.toJson(categories));
        return;
    }

    @Route(value = "/save", method = HttpMethod.POST)
    void save(HttpServletRequest req, HttpServletResponse res) 
        throws IOException
    {
        PrintWriter out = res.getWriter();
        HashMap<String, Object> resMap = new HashMap<String, Object>();
        Category category = gson.fromJson(getReqBody(req), Category.class);
        try {
            categoryRepo.save(category);
            resMap.put("errorCode", 0);
            resMap.put("message", "Success");
        } catch (SQLException e) {
            res.setStatus(500);
            resMap.put("errorCode", 1);
            resMap.put("message", "Internal error");
            e.printStackTrace();
        } catch (AnnoValidationException e) {
            res.setStatus(400);
            resMap.put("errorCode", 2);
            resMap.put("message", "Field <" + e.getInvalidFieldName() + "> is invalid.");
            e.printStackTrace();
        }

        out.println(gson.toJson(resMap));
    }

    @Route(value = "/update", method = HttpMethod.PUT)
    void update(HttpServletRequest req, HttpServletResponse res)
        throws IOException
    {
        PrintWriter out = res.getWriter();
        HashMap<String, Object> resMap = new HashMap<String, Object>();
        Category updatedCategory = gson.fromJson(getReqBody(req), Category.class);
        Category oldCategory;

        try {
            Category[] categories = categoryRepo.where("code").equal(updatedCategory.getCode()).get();
            if(categories.length <= 0) {
                resMap.put("errCode", "2");
                resMap.put("message", "No such category with code = '"+updatedCategory.getCode()+"'");
                out.print(gson.toJson(resMap));
                return;
            } else {
                oldCategory = categories[0];
            }
        } catch (Exception e) {
            res.setStatus(500);
            resMap.put("errCode", "1");
            resMap.put("message", "Internal error");
            e.printStackTrace();
            out.print(gson.toJson(resMap));
            return;
        }

        oldCategory.setType(updatedCategory.getType());
        oldCategory.setLabel(updatedCategory.getLabel());

        try {
            categoryRepo.update(oldCategory);
            resMap.put("errCode", "0");
            resMap.put("message", "Success");
        } catch (SQLException e) {
            res.setStatus(500);
            resMap.put("errCode", "1");
            resMap.put("message", "Internal error");
            e.printStackTrace();
        } catch (AnnoValidationException e) {
            res.setStatus(500);
            resMap.put("errCode", "3");
            resMap.put("message", "Field <" + e.getInvalidFieldName() + "> is invalid.");
        }
        out.print(gson.toJson(resMap));
    }


    @Route(value = "/deleteByCode", method = HttpMethod.DELETE)
    void deleteByCode(HttpServletRequest req, HttpServletResponse res) 
        throws IOException
    {
        PrintWriter out = res.getWriter();
        Repository<Note> noteRepository;
        HashMap<String, Object> respMap = new HashMap<>();
        respMap.put("errCode", "10");
        respMap.put("message", "Category deletion is not available.");
        out.println(gson.toJson(respMap, HashMap.class));
        return;
        /*
        try {
            noteRepository = (Repository<Note>)App.getRepoFactory().getRepository(Note.class);
        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(500);
            respMap.put("errCode", "2");
            respMap.put("message", "Unexpected error occured.");
            out.println(gson.toJson(respMap, HashMap.class));
            return;
        }
        
        String categoryCode = req.getParameter("code");
        Category category;
        
        try {
            Category[] categories = categoryRepo.where("code").equal(categoryCode).get();
            if(categories.length == 0) {
                respMap.put("errCode", "3");
                respMap.put("message", "No such category. code = '" + categoryCode + "'");
                out.println(gson.toJson(respMap, HashMap.class));
                return;
            }
            category = categories[0];
        } catch (Exception e) {
            respMap.put("errCode", "2");
            respMap.put("message", "Unexpected exception.");
            out.println(gson.toJson(respMap, HashMap.class));
            e.printStackTrace();
            return;
        }

        try {
            int nodesCountInCategory = noteRepository.where("category").equal(categoryCode).getCount();
            if(nodesCountInCategory > 0) {
                respMap.put("errCode", "1");
                respMap.put("message", "This category includes notes in it");
                out.println(gson.toJson(respMap, HashMap.class));
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(500);
            respMap.put("errCode", "2");
            respMap.put("message", "Unexpected error occured.");
            out.println(gson.toJson(respMap, HashMap.class));
            return;
        }
        try {
            categoryRepo.delete(category.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            res.setStatus(500);
            respMap.put("errCode", "2");
            respMap.put("message", "Unexpected error occured.");
            out.println(gson.toJson(respMap, HashMap.class));
            return;
        }

        respMap.put("errCode", "0");
        respMap.put("message", "Success");
        out.println(gson.toJson(respMap, HashMap.class));*/
    }
}
