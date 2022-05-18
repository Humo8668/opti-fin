package uz.app.OptiFin.gsonHelpers;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import uz.app.OptiFin.App;

/**
 * DateGsonDeserializer
 */
public class DateGsonDeserializer implements JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        Date result = null;
        String dateAsStr = jsonElement.getAsString();
        try {
            result = App.getDateFormat().parse(dateAsStr);
        } catch (ParseException e) {
            try {
                result = App.getDatetimeFormat().parse(dateAsStr);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        return result;
    }

    
}