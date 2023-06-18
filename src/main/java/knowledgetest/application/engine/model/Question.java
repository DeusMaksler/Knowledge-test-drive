package knowledgetest.application.engine.model;

public class Question {
//    public static final int NUMBER_OF_FIELDS = 11;
    private String phrasing;
    private String[] variants; //two or four
    private boolean ynType; //yes/no - true
    private int rightChoice;
    private int checkDigit;

    public Question(){
        this.ynType = false;
    }

    public Question(boolean ynType, String phrasing, String[] variants, int rightChoice, int checkDigit) {
        this.ynType = ynType;
        this.phrasing = phrasing;
        this.variants = variants;
        this.rightChoice = rightChoice;
        this.checkDigit = checkDigit;
    }

    public Question(String phrasing, String[] variants, int rightChoice, int checkDigit) {
        this.ynType = false;
        this.phrasing = phrasing;
        this.variants = variants;
        this.rightChoice = rightChoice;
        this.checkDigit = checkDigit;
    }

    public String getPhrasing() { return phrasing;}
    public void setPhrasing(String phrasing) { this.phrasing = phrasing;}

    public String[] getVariants() { return variants;}
    public void setVariants(String[] variants) { this.variants = variants;}

    public int getRightChoice() { return rightChoice;}
    public void setRightChoice(int rightChoice) { this.rightChoice = rightChoice;}

    public int getCheckDigit() { return checkDigit;}
    public void setCheckDigit(int checkDigit) { this.checkDigit = checkDigit;}

    public boolean isYnType() { return ynType;}
    public void setYnType(boolean ynType) { this.ynType = ynType;}
}
