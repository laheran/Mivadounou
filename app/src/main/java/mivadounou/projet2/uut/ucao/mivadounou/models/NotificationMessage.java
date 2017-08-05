package mivadounou.projet2.uut.ucao.mivadounou.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leinad on 8/4/17.
 */

@IgnoreExtraProperties
public class NotificationMessage {
    
    private String message;
    private boolean isSent;
    
    public NotificationMessage() {
    }
    
    public NotificationMessage(String message, boolean isSent) {
        this.message = message;
        this.isSent = isSent;
    }
    
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("message", message);
        result.put("isSent", isSent);
        
        return result;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean getIsSent() {
        return isSent;
    }
    
    public void setIsSent(boolean sent) {
        isSent = sent;
    }
}
