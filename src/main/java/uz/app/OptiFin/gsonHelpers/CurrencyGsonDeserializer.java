package uz.app.OptiFin.gsonHelpers;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import uz.app.OptiFin.entities.Currency;

public class CurrencyGsonDeserializer implements JsonDeserializer<Currency> {

    @Override
    public Currency deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
            throws JsonParseException {
                
        Currency[] currencies = Currency.values();
        for (Currency currency : currencies)
        {
            if (currency.equals(jsonElement.getAsString()))
            return currency;
        }
        return null;
    }
    
}
