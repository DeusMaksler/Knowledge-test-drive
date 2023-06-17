package knowledgetest.application.engine.model;

public class Session {
    private String sessionUserLogin;
    private String sessionUserAccess;

    public Session(){}

    public Session(String selectLogin, String selectRole) {
        this.sessionUserLogin = selectLogin;
        this.sessionUserAccess = selectRole;
    }

    public String getSessionUserAccess() { return sessionUserAccess;}
    public void setSessionUserAccess(String sessionUserAccess) { this.sessionUserAccess = sessionUserAccess;}

    public String getSessionUserLogin() { return sessionUserLogin;}
    public void setSessionUserLogin(String sessionUserLogin) { this.sessionUserLogin = sessionUserLogin;}
}
