import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class LoginService {

    private Config config;

    private String CHECK_URL;
    private String LOGIN_URL;
    private String USERNAME;
    private String PASSWORD;
    private String NETWORK;
    private String USERNAME_VALUE;
    private String PASSWORD_VALUE;
    private String NETWORK_VALUE;
    private String SUCCESS_CHECK;
    private String INDEX_CHECK;
    private Integer RETRY;
    private Integer SLEEP;

    public LoginService() {
        try {
            Yaml yaml = new Yaml();
            //InputStream in = getClass().getClassLoader().getResourceAsStream("config.yml");
            String path = System.getProperty("user.dir") + File.separator + "config.yml";
            config = yaml.loadAs(new FileInputStream(path), Config.class);
            logger.info("Get Logger From Config File" + config);
        } catch (Exception ignore) {
            logger.warn("Use Inner Config");
            config = new Config();
        } finally {
            CHECK_URL = config.getCHECK_URL();
            LOGIN_URL = config.getLOGIN_URL();
            USERNAME = config.getUSERNAME();
            PASSWORD = config.getPASSWORD();
            NETWORK = config.getNETWORK();
            USERNAME_VALUE = config.getUSERNAME_VALUE();
            PASSWORD_VALUE = config.getPASSWORD_VALUE();
            NETWORK_VALUE = config.getNETWORK_VALUE();
            SUCCESS_CHECK = config.getSUCCESS_CHECK();
            INDEX_CHECK = config.getINDEX_CHECK();
            RETRY = config.getRETRY();
            SLEEP = config.getSLEEP();
        }
    }

    private final static Logger logger = LoggerFactory.getLogger(LoginService.class);

    private Boolean testNetWork() {
        String s = "";
        try {
            Response resp = Request.Get(CHECK_URL).execute();
            HttpResponse httpResponse = resp.returnResponse();
            s = EntityUtils.toString(httpResponse.getEntity(),"utf-8").trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s.contains(INDEX_CHECK);
    }

    private Boolean testLogin() {
        String s = "";
        try {
            s = Request.Post(LOGIN_URL).bodyForm(Form.form()
                    .add(USERNAME, USERNAME_VALUE)
                    .add(PASSWORD, PASSWORD_VALUE)
                    .add(NETWORK, NETWORK_VALUE).build()).execute().returnContent().asString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s.contains(SUCCESS_CHECK);

    }

    private  void doLogin() {
        for (int i = 0; i < RETRY; i++) {
            if (!testNetWork()) {
                logger.info("Not Login, try Login now...");
                if (testLogin()) {
                    logger.info("Login Successful.");
                    break;
                }
                else {
                    logger.warn("Login Failed");
                    logger.info("Try again for " + (i+1) + " times");
                    try {
                        TimeUnit.SECONDS.sleep(SLEEP);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                logger.info("OnLine Now...");
                break;
            }
        }
    }
    public static void main(String[] args) {
        String version = "CCNU AUTO LOGIN BY CORKINE MA : 1.0.0";
        logger.info("Version " + version);
        new LoginService().doLogin();
    }
}
