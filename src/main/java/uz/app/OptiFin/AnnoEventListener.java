package uz.app.OptiFin;

import uz.app.Anno.AnnoContext;
import uz.app.Anno.util.AnnoPoolConnection;

public class AnnoEventListener extends uz.app.Anno.AnnoEventListener {

    @Override
    protected void AfterAnnoInitialized() {
    }

    @Override
    protected void AfterServicesInitialized() {
        
    }

    @Override
    protected void BeforeServicesInitializing() {
        
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        App.Init();
        AnnoContext.setPoolConnection(new AnnoPoolConnection(HerokuDB.getConnectionUrl(), HerokuDB.getLogin(), HerokuDB.getPassword()));
    }
    
}
