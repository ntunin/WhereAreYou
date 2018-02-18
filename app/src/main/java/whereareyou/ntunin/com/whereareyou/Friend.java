package whereareyou.ntunin.com.whereareyou;

/**
 * Created by nik on 17.02.2018.
 */

public class Friend {
    String id;
    String name;
    String image;
    String phone;
    String email;
    double lat;
    double lng;

    public  Friend(String id, String name, String image, String phone, String email) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.phone = phone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setLatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
