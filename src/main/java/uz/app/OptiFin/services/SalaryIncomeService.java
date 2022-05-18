package uz.app.OptiFin.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import uz.app.Anno.orm.Repository;
import uz.app.Anno.service.BaseService;
import uz.app.Anno.service.annotations.Route;
import uz.app.Anno.service.annotations.Service;
import uz.app.Anno.util.HttpMethod;
import uz.app.OptiFin.App;
import uz.app.OptiFin.entities.Note;
import uz.app.OptiFin.entities.SalaryIncome;

@Service("/notes")
public class SalaryIncomeService extends BaseService {
    
    private Repository<Note> notesRepo;
    private Repository<SalaryIncome> salIncomeRepo;
    private Gson gson;

    public SalaryIncomeService() {
        try {
            notesRepo = App.getRepoFactory().getRepository(Note.class);
            salIncomeRepo = App.getRepoFactory().getRepository(SalaryIncome.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        gson = App.getGsonBuilder().create();
    }

    @Route(value = "/getsalaryincomes", method = HttpMethod.GET)
    public void getSalaryIncomes(HttpServletRequest req, HttpServletResponse res) 
        throws IOException
    {
        PrintWriter out = res.getWriter();
        SalaryIncome[] salIncomes;
        try {
            salIncomes = salIncomeRepo.getAll();
        } catch (SQLException e) {
            res.setStatus(500);
            e.printStackTrace();
            return;
        }
        try {
            for (SalaryIncome salaryIncome : salIncomes) {
                salaryIncome.setCorrespondingNote(notesRepo.where("external_id").equal(salaryIncome.getId()).getFirst());
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
            res.setStatus(500);
            return;
        }
        
        out.println(gson.toJson(salIncomes));
    }
}
