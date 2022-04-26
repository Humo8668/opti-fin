package uz.app.OptiFin;

public class JsonResponseObject {
    public String errorCode;
    public String message;

    public JsonResponseObject() {};

    public JsonResponseObject(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
