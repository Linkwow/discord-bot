package ua.projects.discordbot.exceptions;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public static ValidationException notValid(String message){
        throw new ValidationException(message);
    }
}
