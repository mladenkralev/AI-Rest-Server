package model;

/**
 * Created by mladen on 8/21/2017.
 */
public class GuessedDigit {
    private String message;
    private int number;

    public GuessedDigit(String message, int number) {
        this.message = message;
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public int getNumber() {
        return number;
    }

}
