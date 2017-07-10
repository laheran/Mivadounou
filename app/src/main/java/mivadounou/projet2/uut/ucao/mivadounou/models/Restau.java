package mivadounou.projet2.uut.ucao.mivadounou.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leinad on 7/5/17.
 */

@IgnoreExtraProperties
public class Restau {

    private String uid;
    private String title;
    private String desc;
    private String location;
    private String updateAt;

    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Restau() {
    }

    public Restau(String uid, String title, String desc, String location, String updateAt) {
        this.uid = uid;
        this.title = title;
        this.desc = desc;
        this.location = location;
        this.updateAt = updateAt;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("title", title);
        result.put("desc", desc);
        result.put("location", location);
        result.put("updateAt", updateAt);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public Map<String, Boolean> getStars() {
        return stars;
    }

    public void setStars(Map<String, Boolean> stars) {
        this.stars = stars;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
