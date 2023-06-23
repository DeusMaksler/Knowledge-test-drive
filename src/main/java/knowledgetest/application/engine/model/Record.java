package knowledgetest.application.engine.model;

public class Record {
    private String userLogin;
    private String userName;
    private String userGroup;
    private String testName;
    private int userResult;

    public Record(String userLogin, String userName, String userGroup, String testName, int userResult){
        this.userLogin = userLogin;
        this.userName = userName;
        this.userGroup = userGroup;
        this.testName = testName;
        this.userResult = userResult;
    }

    public String getUserLogin() { return userLogin;}
    public void setUserLogin(String userLogin) { this.userLogin = userLogin;}

    public String getUserName() { return userName;}
    public void setUserName(String userName) { this.userName = userName;}

    public String getUserGroup() { return userGroup;}
    public void setUserGroup(String userGroup) { this.userGroup = userGroup;}

    public String getTestName() { return testName;}
    public void setTestName(String testName) { this.testName = testName;}

    public int getUserResult() { return userResult;}
    public void setUserResult(int userResult) { this.userResult = userResult;}
}
