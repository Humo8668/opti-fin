package uz.app.OptiFin;

import java.io.IOException;

public class App {
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
    }
}
