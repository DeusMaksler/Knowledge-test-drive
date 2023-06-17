package knowledgetest.application.engine.model;

public class User {
    public static final int NUMBER_OF_FIELDS = 10;
    private String login;
    private String password;
    private String role;
    private boolean status;
    private String name;
    private String surname;
    private String patronymic;
    private String group;
    private String email;
    private  String answer;


    public User(){
        this.role = "user";
        this.status = true;
    }

    public User(String login, String password, String name, String surname, String patronymic, String group, String email, String answer){
        this.login = login;
        this.password = password;
        this.role = "user";
        this.status = true;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.group = group;
        this.email = email;
        this.answer =answer;
    }

    public User(String login, String password, String role, String name, String surname, String patronymic, String group, String email, String answer){
        this.login = login;
        this.password = password;
        this.role = role;
        this.status = true;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.group = group;
        this.email = email;
        this.answer =answer;
    }

    public String getLogin() { return login;}
    public void setLogin(String login) { this.login = login;}

    public String getPassword() { return password;}
    public void setPassword(String password) { this.password = password;}

    public String getRole() { return role;}
    public void setRole(String accessLevel) { this.role = accessLevel;}

    public Boolean getStatus() {return status;}
    public void setStatus(Boolean status) { this.status = status;}

    public String getName() { return name;}
    public void setName(String name) { this.name = name;}

    public String getSurname() { return this.surname;}
    public void setSurname(String surname) { this.surname = surname;}

    public String getPatronymic() { return this.patronymic;}
    public void setPatronymic(String patronymic) { this.patronymic = patronymic;}

    public String getGroup() { return  this.group;}
    public void setGroup(String group) { this.group = group;}

    public String getEmail() { return this.email;}
    public void setEmail(String email) { this.email = email;}

    public String getAnswer() { return answer;}
    public void setAnswer(String answer) { this.answer = answer;}
}
