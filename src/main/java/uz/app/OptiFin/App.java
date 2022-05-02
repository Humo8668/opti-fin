package uz.app.OptiFin;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.google.gson.GsonBuilder;

import javassist.Loader.Simple;
import uz.app.Anno.orm.RepositoryFactory;

public class App {
    private static RepositoryFactory repoFactory;
    private static GsonBuilder gsonBuilder;
    private static SimpleDateFormat globalDateFormat;
    private static SimpleDateFormat globalDateTimeFormat;

    public static void Init() {
        HerokuDB.Init(
            System.getProperty("HEROKU_URL"),
            System.getProperty("HEROKU_LOGIN"),
            System.getProperty("HEROKU_PASSWORD")
        );
        
        try {
            HerokuDB.loadDBCredentials();
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }

        gsonBuilder = new GsonBuilder();
        gsonBuilder = gsonBuilder.setDateFormat("dd.MM.yyyy");
        globalDateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        globalDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    }

    public static RepositoryFactory getRepoFactory(){
        if(repoFactory == null){
            repoFactory = new RepositoryFactory();
        }
        return repoFactory;
    }

    public static GsonBuilder getGsonBuilder() {
        return gsonBuilder;
    }

    public static SimpleDateFormat getDateFormat() {
        return globalDateFormat;
    }

    public static SimpleDateFormat getDatetimeFormat() {
        return globalDateTimeFormat;
    }
}
