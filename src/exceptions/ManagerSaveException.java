package exceptions;

public class ManagerSaveException extends RuntimeException{
    public ManagerSaveException() {
        super("Ошибка к доступу файла истории.");
    }
}
