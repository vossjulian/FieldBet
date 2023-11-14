package de.hsos.swa.project.fieldbet.shared.control;

/**
 * BetDTO
 * 
 * @author Julian Voss
 */
public class ErrorDTO {
    public String code;
    public Integer status;
    public String source;
    public String title;
    public String detail;

    public ErrorDTO() {
    }

    public ErrorDTO(Integer status, String title, String detail) {
        this.status = status;
        this.title = title;
        this.detail = detail;
    }

    public ErrorDTO(String code, Integer status, String source, String title, String detail) {
        this.code = code;
        this.status = status;
        this.source = source;
        this.title = title;
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "ErrorDTO [code=" + code + ", status=" + status + ", source=" + source + ", title=" + title + ", detail="
                + detail + "]";
    }

}
