public class Config {
    private String CHECK_URL = "http://www.ccnu.edu.cn";
    private String LOGIN_URL = "http://10.220.250.50";
    private String USERNAME = "DDDDD";
    private String PASSWORD = "upass";
    private String NETWORK = "suffix";
    private String USERNAME_VALUE = "realAccount@cmcc";
    private String PASSWORD_VALUE = "realPassword";
    private String NETWORK_VALUE = "2";
    private String SUCCESS_CHECK = "您已登录成功，欢迎使用！请不要关闭本页。";
    private String INDEX_CHECK = "鄂ICP备05003325号-1";
    private Integer RETRY = 5;
    private Integer SLEEP = 5;

    public String getNETWORK_VALUE() {
        return NETWORK_VALUE;
    }

    public void setNETWORK_VALUE(String NETWORK_VALUE) {
        this.NETWORK_VALUE = NETWORK_VALUE;
    }

    @Override
    public String toString() {
        return "Config{" +
                "CHECK_URL='" + CHECK_URL + '\'' +
                ", LOGIN_URL='" + LOGIN_URL + '\'' +
                ", USERNAME='" + USERNAME + '\'' +
                ", PASSWORD='" + PASSWORD + '\'' +
                ", NETWORK='" + NETWORK + '\'' +
                ", USERNAME_VALUE='" + USERNAME_VALUE + '\'' +
                ", PASSWORD_VALUE='" + PASSWORD_VALUE + '\'' +
                ", NETWORK_VALUE='" + NETWORK_VALUE + '\'' +
                ", SUCCESS_CHECK='" + SUCCESS_CHECK + '\'' +
                ", INDEX_CHECK='" + INDEX_CHECK + '\'' +
                ", RETRY=" + RETRY +
                ", SLEEP=" + SLEEP +
                '}';
    }

    public String getCHECK_URL() {
        return CHECK_URL;
    }

    public void setCHECK_URL(String CHECK_URL) {
        this.CHECK_URL = CHECK_URL;
    }

    public String getLOGIN_URL() {
        return LOGIN_URL;
    }

    public void setLOGIN_URL(String LOGIN_URL) {
        this.LOGIN_URL = LOGIN_URL;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getNETWORK() {
        return NETWORK;
    }

    public void setNETWORK(String NETWORK) {
        this.NETWORK = NETWORK;
    }

    public String getUSERNAME_VALUE() {
        return USERNAME_VALUE;
    }

    public void setUSERNAME_VALUE(String USERNAME_VALUE) {
        this.USERNAME_VALUE = USERNAME_VALUE;
    }

    public String getPASSWORD_VALUE() {
        return PASSWORD_VALUE;
    }

    public void setPASSWORD_VALUE(String PASSWORD_VALUE) {
        this.PASSWORD_VALUE = PASSWORD_VALUE;
    }

    public String getSUCCESS_CHECK() {
        return SUCCESS_CHECK;
    }

    public void setSUCCESS_CHECK(String SUCCESS_CHECK) {
        this.SUCCESS_CHECK = SUCCESS_CHECK;
    }

    public String getINDEX_CHECK() {
        return INDEX_CHECK;
    }

    public void setINDEX_CHECK(String INDEX_CHECK) {
        this.INDEX_CHECK = INDEX_CHECK;
    }

    public Integer getRETRY() {
        return RETRY;
    }

    public void setRETRY(Integer RETRY) {
        this.RETRY = RETRY;
    }

    public Integer getSLEEP() {
        return SLEEP;
    }

    public void setSLEEP(Integer SLEEP) {
        this.SLEEP = SLEEP;
    }
}
