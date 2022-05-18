package uz.app.OptiFin.entities;


import uz.app.Anno.orm.AnnoValidationException;
import uz.app.Anno.orm.IEntity;
import uz.app.Anno.orm.annotations.*;


@Schema("public")
@Table("Categories")
public class Category implements IEntity {

    @Id
    @Generated
    @Column("id")
    Integer id;

    @Column("code")
    String code;

    @Column("type")
    String type;

    @Column("label")
    String label;

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void validate() throws AnnoValidationException {
        this.code = this.code.toLowerCase();
    }

    public Integer getId() {
        return id;
    }

    public String getCode(){
        return this.code;
    }

    public String getType() {return this.type;}

    public void setType(String type) {this.type = type;}
    
    public String getLabel() {return this.label;}

    public void setLabel(String label) {this.label = label;}
    
}
