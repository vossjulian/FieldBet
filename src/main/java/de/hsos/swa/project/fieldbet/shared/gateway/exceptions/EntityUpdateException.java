package de.hsos.swa.project.fieldbet.shared.gateway.exceptions;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;

/**
 * EntityUpdateException
 * 
 * @author Patrick Felschen
 */
public class EntityUpdateException extends RuntimeException {
    private String entityName;
    private String entityId;
    private String message;

    public EntityUpdateException(String className, String entityId, String message) {
        this.entityName = className;
        this.entityId = entityId;
        this.message = message;
    }

    public EntityUpdateException(String className, String entityId, Set<? extends ConstraintViolation<?>> violations) {
        this.entityName = className;
        this.entityId = entityId;
        this.message = violations.stream()
                .map(cv -> cv.getPropertyPath() + " : " + cv.getMessage())
                .collect(Collectors.joining(", "));
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
