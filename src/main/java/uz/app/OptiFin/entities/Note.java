package uz.app.OptiFin.entities;

import java.util.Date;

import uz.app.Anno.orm.AnnoValidationException;
import uz.app.Anno.orm.IEntity;
import uz.app.Anno.orm.annotations.Column;
import uz.app.Anno.orm.annotations.Generated;
import uz.app.Anno.orm.annotations.Id;
import uz.app.Anno.orm.annotations.Schema;
import uz.app.Anno.orm.annotations.Table;

@Table("Notes")
@Schema("public")
public class Note implements IEntity {

    @Id
    @Generated
    @Column("id")
    Integer id;
    @Column("sum")
    Float sum;
    @Column("currency_code")
    Currency currencyCode;
    @Column("category")
    String category;
    @Column("user_id")
    Integer belongsUserId;
    @Column("note_date")
    Date noteDate;
    @Column("comment")
    String comment;
    @Column("created_on")
    Date createdOn;
    @Column("external_id")
    Integer externalId;
    
    
    public boolean isValid() {
        return true;
    }
    
    public void validate() throws AnnoValidationException {
        /*if(this.createdOn == null)
            this.createdOn = new Date();*/
    }

    public Integer getId() {
        return id;
    }

    public Date getCreatedOn(){
        return (Date)this.createdOn.clone();
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Float getSum() {
        return sum;
    }
    public Currency getCurrencyCode() {
        return currencyCode;
    }
    public void setCurrencyCode(Object currencyCode) {
        this.currencyCode = Currency.valueOf(currencyCode.toString());
    }
    public Integer getBelongsUserId() {
        return belongsUserId;
    }
    public Date getNoteDate() {
        return noteDate;
    }
    public String getComment() {
        return comment;
    }
}
