package com.example.rescuehubproject.accounts.entity;

import com.example.rescuehubproject.accounts.util.LogEvent;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Entity
@Table(name = "logs")
@Schema(name = "Log", description = "Log entity")
public class Log {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @Schema(description = "Log id", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Log date", example = "2021-10-01")
    private Date date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Schema(description = "Log action", example = "LOGIN")
    private LogEvent action;

    @Column(nullable = false)
    @Schema(description = "Log subject", example = "Anomymous")
    private String subject;

    @Column(nullable = false)
    @Schema(description = "Log object", example = "User")
    private String object;

    @Column(nullable = false)
    @Schema(description = "Log path", example = "/login")
    private String path;

    public Log() {
    }

    // getters and setters

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public LogEvent getAction() {
        return action;
    }

    public void setAction(LogEvent action) {
        this.action = action != null ? action : LogEvent.DEFAULT_ACTION;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static class Builder {
        private final Log log;

        public Builder() {
            log = new Log();
        }

        public Builder setAction(LogEvent action) {
            log.setAction(action);
            return this;
        }

        public Builder setSubject(String subject) {
            log.setSubject(subject);
            return this;
        }

        public Builder setObject(String object) {
            log.setObject(object);
            return this;
        }

        public Builder setPath(String path) {
            log.setPath(path);
            return this;
        }

        public Log build() {

            log.setDate(new Date());

            if (log.getDate() == null || log.getAction() == null || log.getSubject() == null
                    || log.getObject() == null || log.getPath() == null) {
                throw new IllegalStateException("All fields must be set before building Log object.");
            }

            return log;
        }
    }
}