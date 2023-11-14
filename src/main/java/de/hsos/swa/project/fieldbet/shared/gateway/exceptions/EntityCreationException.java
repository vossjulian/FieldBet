package de.hsos.swa.project.fieldbet.shared.gateway.exceptions;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;

/**
 * EntityCreationException
 * 
 * @author Patrick Felschen
 */
public class EntityCreationException extends RuntimeException {
    private String entityName;
    private String message;

    public EntityCreationException(String className, String message) {
        this.entityName = className;
        this.message = message;
    }

    public EntityCreationException(String className, Set<? extends ConstraintViolation<?>> violations) {
        this.entityName = className;
        this.message = violations.stream()
                .map(cv -> cv.getPropertyPath() + " : " + cv.getMessage())
                .collect(Collectors.joining(", "));
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String className) {
        this.entityName = className;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
