package de.hsos.swa.project.fieldbet.shared.gateway.exceptions;

/**
 * EntityQueryException
 * 
 * @author Patrick Felschen
 */
public class EntityQueryException extends RuntimeException {
    private String entityName;
    private String message;

    public EntityQueryException(String entityName, String message) {
        this.entityName = entityName;
        this.message = message;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
