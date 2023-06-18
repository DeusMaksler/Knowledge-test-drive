package knowledgetest.application.engine.model;

public class QSection {
    private String name;
    private boolean status;
    private int size;

    public QSection() {}

    public QSection(String name) {
        this.name = name;
        this.status = true;
        this.size = 0;
    }

    public QSection(String name, boolean status, int size) {
        this.name = name;
        this.status = status;
        this.size = size;
    }

    public String getName() { return name;}
    public void setName(String name) { this.name = name;}

    public boolean isStatus() { return status;}
    public void setStatus(boolean status) { this.status = status;}

    public int getSize() { return size;}
    public void setSize(int size) { this.size = size;}
}
