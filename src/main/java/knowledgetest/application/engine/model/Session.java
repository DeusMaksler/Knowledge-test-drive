package knowledgetest.application.engine.model;

public class Session {

    private User actingUser;
    private String editableSection;

    public Session(User actingUser) { this.actingUser = actingUser;}

    public User getActingUser() { return actingUser;}
    public void setActingUser(User actingUser) { this.actingUser = actingUser;}

    public String getEditableSection() { return editableSection;}
    public void setEditableSection(String editableSection) { this.editableSection = editableSection;}
}
