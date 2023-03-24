package exceptions;

public class IllegalTaskTimeException extends RuntimeException{
    public IllegalTaskTimeException() {
        super("На это время уже назначена задача");
    }
}
