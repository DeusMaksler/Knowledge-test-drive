package knowledgetest.application.engine.model;

public class Session {

    private User actingUser;
    private boolean testStatistic;
    private String editableSection;

    public Session(User actingUser) { this.actingUser = actingUser;}

    public User getActingUser() { return actingUser;}
    public void setActingUser(User actingUser) { this.actingUser = actingUser;}

    public boolean isTestStatistic() { return testStatistic;}
    public void setTestStatistic(boolean testStatistic) { this.testStatistic = testStatistic;}

    public String getEditableSection() { return editableSection;}
    public void setEditableSection(String editableSection) { this.editableSection = editableSection;}
}
