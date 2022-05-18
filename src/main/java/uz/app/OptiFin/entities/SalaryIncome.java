package uz.app.OptiFin.entities;

import java.util.Date;

import uz.app.Anno.orm.AnnoValidationException;
import uz.app.Anno.orm.IEntity;
import uz.app.Anno.orm.annotations.Column;
import uz.app.Anno.orm.annotations.Generated;
import uz.app.Anno.orm.annotations.Id;
import uz.app.Anno.orm.annotations.Schema;
import uz.app.Anno.orm.annotations.Table;

@Schema("public")
@Table("Salary_income")
public class SalaryIncome implements IEntity  {
    @Id
    @Generated
    @Column("id")
    Integer id;
    
    @Column("company_name")
    String companyName;

    Note correspondingNote; 

    public SalaryIncome() {}

    
    public boolean isValid() {
        return true;
    }

    
    public void validate() throws AnnoValidationException {
        return;
    }

    //#region
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public Note getCorrespondingNote() {
        return correspondingNote;
    }
    public void setCorrespondingNote(Note note) {
        this.correspondingNote = note;
    }

    //#endregion
}
