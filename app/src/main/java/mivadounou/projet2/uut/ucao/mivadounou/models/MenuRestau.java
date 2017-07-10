package mivadounou.projet2.uut.ucao.mivadounou.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leinad on 7/5/17.
 */

public class MenuRestau {

    private String restauId;
    private String restauName;
    private String title;
    private String desc;
    private String type = "";
    private int price;
    private String updateAt;
    private String md5Hash = "";

    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public MenuRestau() {
    }

    public MenuRestau(String restauId, String restauName, String title, String desc, int price, String type, String updateAt) {
        this.restauName = restauName;
        this.restauId = restauId;
        this.title = title;
        this.desc = desc;
        this.price = price;
        this.updateAt = updateAt;
        this.type = type;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("restauId", restauId);
        result.put("restauName", restauName);
        result.put("title", title);
        result.put("desc", desc);
        result.put("price", price);
        result.put("type", type);
        result.put("updateAt", updateAt);
        result.put("md5Hash", md5Hash);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }

    public String getRestauId() {
        return restauId;
    }

    public void setRestauId(String restauId) {
        this.restauId = restauId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRestauName() {
        return restauName;
    }

    public void setRestauName(String restauName) {
        this.restauName = restauName;
    }

    public String getMd5Hash() {
        return md5Hash;
    }

    public void setMd5Hash(String md5Hash) {
        this.md5Hash = md5Hash;
    }
}
