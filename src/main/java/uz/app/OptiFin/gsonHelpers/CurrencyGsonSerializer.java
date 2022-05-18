package uz.app.OptiFin.gsonHelpers;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import uz.app.OptiFin.entities.Currency;

/**
 * CurrencyGsonSerializer
 */
public class CurrencyGsonSerializer implements JsonSerializer<Currency> {

    @Override
    public JsonElement serialize(Currency currency, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(currency.toString().toUpperCase());
    }

    
}