package mivadounou.projet2.uut.ucao.mivadounou.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leinad on 7/13/17.
 */

@IgnoreExtraProperties
public class CommandeMenu implements Parcelable {

    public final static String COMMANDE_SENT = "COMMANDE_SENT";
    public final static String COMMANDE_ACCEPT = "COMMANDE_ACCEPT";
    public final static String COMMANDE_REJECT = "COMMANDE_REJECT";
    public final static String COMMANDE_CANCELED = "COMMANDE_CANCELED";
    public final static String COMMANDE_DONE = "COMMANDE_DONE";

    private int number;
    private String menuTitle;
    private String menuType;
    private String menuKey;
    private int quantity;
    private int unitPrice;
    private int totalPrice;
    private String userName;
    private String userKey;
    private String restauTitle;
    private String restauKey;
    private String status;
    private long endAtTimestamp;
    private long createAtTimestamp;

    public CommandeMenu() {
    }

    public CommandeMenu(String menuTitle, String menuType, String menuKey, int unitPrice,
                        String userName, String userKey, String restauTitle, String restauKey) {
        this.menuTitle = menuTitle;
        this.menuType = menuType;
        this.menuKey = menuKey;
        this.unitPrice = unitPrice;
        this.userName = userName;
        this.userKey = userKey;
        this.restauTitle = restauTitle;
        this.restauKey = restauKey;
    }

    protected CommandeMenu(Parcel in) {
        menuTitle = in.readString();
        menuType = in.readString();
        menuKey = in.readString();
        unitPrice = in.readInt();
        userName = in.readString();
        userKey = in.readString();
        restauTitle = in.readString();
        restauKey = in.readString();
    }

    @Exclude
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(menuTitle);
        dest.writeString(menuType);
        dest.writeString(menuKey);
        dest.writeInt(unitPrice);
        dest.writeString(userName);
        dest.writeString(userKey);
        dest.writeString(restauTitle);
        dest.writeString(restauKey);
    }

    @Exclude
    public static final Creator<CommandeMenu> CREATOR = new Creator<CommandeMenu>() {
        @Override
        public CommandeMenu createFromParcel(Parcel in) {
            return new CommandeMenu(in);
        }

        @Override
        public CommandeMenu[] newArray(int size) {
            return new CommandeMenu[size];
        }
    };

    @Exclude
    @Override
    public int describeContents() {
        return 0;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("number", number);

        result.put("menuTitle", menuTitle);
        result.put("menuType", menuType);
        result.put("menuKey", menuKey);

        result.put("quantity", quantity);
        result.put("unitPrice", unitPrice);
        result.put("totalPrice", totalPrice);

        result.put("restauTitle", restauTitle);
        result.put("restauKey", restauKey);

        result.put("userName", userName);
        result.put("userKey", userKey);

        result.put("status", status);

        result.put("endAtTimestamp", endAtTimestamp);
        result.put("createAtTimestamp", createAtTimestamp);

        return result;
    }

    @Override
    public String toString() {
        return "CommandeMenu{" +
                "number=" + number +
                ", menuTitle='" + menuTitle + '\'' +
                ", menuType='" + menuType + '\'' +
                ", menuKey='" + menuKey + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                ", userName='" + userName + '\'' +
                ", userKey='" + userKey + '\'' +
                ", restauTitle='" + restauTitle + '\'' +
                ", restauKey='" + restauKey + '\'' +
                ", status='" + status + '\'' +
                ", endAtTimestamp=" + endAtTimestamp +
                ", createAtTimestamp=" + createAtTimestamp +
                '}';
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getEndAtTimestamp() {
        return endAtTimestamp;
    }

    public void setEndAtTimestamp(long endAtTimestamp) {
        this.endAtTimestamp = endAtTimestamp;
    }

    public long getCreateAtTimestamp() {
        return createAtTimestamp;
    }

    public void setCreateAtTimestamp(long createAtTimestamp) {
        this.createAtTimestamp = createAtTimestamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMenuKey() {
        return menuKey;
    }

    public void setMenuKey(String menuKey) {
        this.menuKey = menuKey;
    }

    public String getRestauKey() {
        return restauKey;
    }

    public void setRestauKey(String restauKey) {
        this.restauKey = restauKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getRestauTitle() {
        return restauTitle;
    }

    public void setRestauTitle(String restauTitle) {
        this.restauTitle = restauTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
