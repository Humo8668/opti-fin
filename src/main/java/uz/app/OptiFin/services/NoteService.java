package uz.app.OptiFin.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import uz.app.Anno.orm.AnnoValidationException;
import uz.app.Anno.orm.Repository;
import uz.app.Anno.service.BaseService;
import uz.app.Anno.service.annotations.Route;
import uz.app.Anno.service.annotations.Service;
import uz.app.Anno.util.HttpMethod;
import uz.app.OptiFin.App;
import uz.app.OptiFin.JsonResponseObject;
import uz.app.OptiFin.entities.Note;

@Service("/notes")
public class NoteService extends BaseService {
    Repository<Note> notesRepo;
    Gson gson;

    public NoteService() {
        try {
            notesRepo = (Repository<Note>)App.getRepoFactory().getRepository(Note.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        gson = App.getGsonBuilder().create();
    }

    @Route(value="/getall", method = HttpMethod.GET)
    public void getAll(HttpServletRequest req, HttpServletResponse res)
        throws IOException, SQLException
    {
        PrintWriter out = res.getWriter();
        Note[] notes;
        try {
            notes = notesRepo.getAll();
        } catch (SQLException ex) {
            ex.printStackTrace();
            res.sendError(500, "Error occurred: " + ex.getMessage());
            return;
        }

        String json = gson.toJson(notes);
        res.setContentType("application/json");
        res.setStatus(200);
        out.print(json);
        return;
    }

    @Route(value = "/getById", method = HttpMethod.GET)
    void getById(HttpServletRequest req, HttpServletResponse res)
        throws IOException
    {
        PrintWriter out = res.getWriter();

        Object notesIdParameter = req.getParameter("id");
        if(notesIdParameter == null || "".equals(notesIdParameter))
        {
            res.setContentType("application/json");
            res.setStatus(200);
            out.print("{}");
            return;
        }
        Note notes;
        int notesId = Integer.parseInt(notesIdParameter.toString());
        try {
            notes = notesRepo.getById(notesId);
        } catch (SQLException ex) {
            ex.printStackTrace();
            res.sendError(500, "Error occurred: " + ex.getMessage());
            return;
        }

        String json = "";
        if(notes != null)
            json = gson.toJson(notes);
        else
            json = "{}";
        res.setContentType("application/json");
        res.setStatus(200);
        out.print(json);
        return;
    }


    @Route(value="/save", method = HttpMethod.POST)
    void saveNote(HttpServletRequest req, HttpServletResponse res) 
        throws IOException
    {
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setStatus(200);

        String requestJson = getReqBody(req);
        
        Note notes = gson.fromJson(requestJson, Note.class);
                    /*new Note(-1, 
                            requestMap.get("login").toString(), 
                            requestMap.get("fullName").toString(), 
                            requestMap.get("email").toString(), 
                            requestMap.get("password").toString(), 
                            requestMap.get("state").toString());*/
        try {
            notesRepo.save(notes);
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
    void updateNote(HttpServletRequest req, HttpServletResponse res) 
        throws IOException
    {
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setStatus(200);

        String requestJson = getReqBody(req);
        
        Note note = gson.fromJson(requestJson, Note.class);
        try {
            Note oldNote = notesRepo.getById(note.getId());
            note.setCreatedOn(oldNote.getCreatedOn());
            notesRepo.update(note);
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
    void deleteNote(HttpServletRequest req, HttpServletResponse res) 
        throws IOException
    {
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setStatus(200);

        String requestJson = getReqBody(req);
        Type type = new TypeToken<HashMap<String, Object>>(){}.getType();

        HashMap<String, Object> requestMap = gson.fromJson(requestJson, type);
        long noteId = (long)Double.parseDouble(requestMap.get("noteId").toString());

        try {
            notesRepo.delete(noteId);
            HashMap<String, Object> resMap = new HashMap<String, Object>();
            resMap.put("errorCode", 0);
            resMap.put("message", "Success");
            out.println(gson.toJson(resMap));
        } catch (SQLException e) {
            res.setStatus(500);
            e.printStackTrace();
        }
    }

    @Route(value="/getByCategory", method = HttpMethod.GET)
    void getByCategory(HttpServletRequest req, HttpServletResponse res) 
        throws IOException
    {
        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setStatus(200);

        Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
        String reqBody = getReqBody(req);
        if(reqBody == null || "".equals(reqBody.trim())) {
            res.setStatus(400);
            JsonResponseObject respObj = new JsonResponseObject("1", "No request body passed");
            out.println(gson.toJson(respObj));
            return;
        }

        HashMap<String, Object> requestMap = gson.fromJson(reqBody, type);
        String noteCategory = null;
        if(requestMap.containsKey("category"))
            noteCategory = requestMap.get("category").toString();
        else {
            res.setStatus(400);
            JsonResponseObject respObj = new JsonResponseObject("2", "Necessary field 'category' is missed");
            out.println(gson.toJson(respObj));
            return;
        }

        AbstractMap<String, Object> filter = null;
        if(requestMap.containsKey("filter")) {
            if(requestMap.get("filter") instanceof AbstractMap<?, ?>)
                filter = (AbstractMap<String, Object>)requestMap.get("filter");
        }
        
        Note[] notes;
        try{
            Repository<Note>.WhereCondition whereCondition = notesRepo.where("category").equal(noteCategory);
            if(filter != null) {
                if(filter.containsKey("sum")) {
                    ArrayList<Float> fromTo;
                    if(filter.get("sum") instanceof ArrayList<?>) {
                        //whereCondition.and("sum").   
                        fromTo = (ArrayList<Float>)filter.get("sum");
                        if(fromTo.size() == 2) {
                            whereCondition.and("sum").greater(fromTo.get(0))
                                .and("sum").less(fromTo.get(1));
                        }
                    }
                }
                if(filter.containsKey("noteDate")) {
                    ArrayList<String> fromTo;
                    if(filter.get("noteDate") instanceof ArrayList<?>) {
                        //whereCondition.and("sum").
                        fromTo = (ArrayList<String>)filter.get("noteDate");
                        if(fromTo.size() == 2) {
                            try {
                                Date from = App.getDateFormat().parse(fromTo.get(0));
                                Date to = App.getDateFormat().parse(fromTo.get(1));
                                whereCondition.and("note_date").greater(from)
                                    .and("note_date").less(to);
                            } catch(ParseException ex) {
                                // ignore date filtering
                            }
                        }
                    }
                }
            }

            notes = whereCondition.get();
        } catch (SQLException e) {
            e.printStackTrace();
            res.setStatus(500);
            return;
        }

        String json = gson.toJson(notes);
        out.print(json);
    }
}
