package bean;


public class locationBean {
    private String userId;                //本机用户名
    private String otherId;               //监控的用户名
    private double latitude ;             //经度
    private double longitude ;            //纬度
    private float radius ;                //精度
    private String addr;                   //地址
    private String locationDescribe;      //位置描述
    private String speed;
    private String height;

    public locationBean(){

    }
    public locationBean(String userId, String otherId, double latitude, double longitude, float radius, String addr, String locationDescribe) {
        this.userId = userId;
        this.otherId=otherId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.addr = addr;
        this.locationDescribe = locationDescribe;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getLocationDescribe() {
        return locationDescribe;
    }

    public void setLocationDescribe(String locationDescribe) {
        this.locationDescribe = locationDescribe;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
