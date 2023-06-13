package com.example.rescuehubproject.accounts.entity;

import com.example.rescuehubproject.accounts.util.LogEvent;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;
import java.util.Date;

@Entity
@Table(name = "logs")
public class Log {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LogEvent action;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String object;

    @Column(nullable = false)
    private String path;

    public Log() {
    }

    // getters and setters

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
        private Log log;

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
            return log;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}