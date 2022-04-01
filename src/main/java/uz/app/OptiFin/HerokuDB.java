package uz.app.OptiFin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import com.google.gson.*;

public class HerokuDB {

    public class HerokuDBCredentials{
        String db_jdbc_url;
        String db_login;
        String db_password;
        protected String errcode;
        protected String errmessage;
    }

    public static class HerokuServiceRequest {
        String login;
        String password;
        public HerokuServiceRequest(String login, String password) {
            this.login = login;
            this.password = password;
        }
    }

    private static String url;
    private static String login;
    private static String password;

    private static Gson gson;
    private static HerokuDBCredentials dbCredentials;

    public static void Init(String url, String login, String password){
        HerokuDB.url = url;
        HerokuDB.login = login;
        HerokuDB.password = password;
        gson = new Gson();
    }

    public static void loadDBCredentials() throws IOException, MalformedURLException, RuntimeException {
        URL herokuServiceUrl = new URL(url);
        URLConnection serviceConn = herokuServiceUrl.openConnection();
        HttpURLConnection serviceHttpConn = (HttpURLConnection)serviceConn;
        serviceHttpConn.setRequestMethod("POST");
        serviceHttpConn.setDoOutput(true);
        OutputStream serviceHttpOut = serviceHttpConn.getOutputStream();
        serviceHttpOut.write(gson.toJson(new HerokuServiceRequest(login, password)).getBytes());
        serviceHttpOut.flush();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(serviceHttpConn.getInputStream()));

        String inputLine;
        StringBuilder jsonStr = new StringBuilder();
        while ((inputLine = in.readLine()) != null)  {
            jsonStr.append(inputLine);
        }
        in.close();

        dbCredentials = gson.fromJson(jsonStr.toString(), HerokuDBCredentials.class);
        if(!"0".equals(dbCredentials.errcode)){
            dbCredentials = null;
            throw new RuntimeException("HerokuAppError: " + dbCredentials.errmessage);
        }
    }

    public static String getConnectionUrl(){
        if(dbCredentials == null)
            return null;
        return dbCredentials.db_jdbc_url;
    }
    public static String getLogin() {
        if(dbCredentials == null)
            return null;
        return dbCredentials.db_login;
    }
    public static String getPassword() {
        if(dbCredentials == null)
            return null;
        return dbCredentials.db_password;
    }
}
