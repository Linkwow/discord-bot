package ua.projects.discordbot.exceptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message){
        super(message);
    }

    public static EntityNotFoundException notFoundException(String message){
        throw new EntityNotFoundException(message);
    }
}
