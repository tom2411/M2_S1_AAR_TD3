package entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Message {
    @Id @GeneratedValue
    private int msgId;
    @Basic
    private LocalDateTime msgDate;
    private String text;
    @ManyToOne
    private User from;
    @ManyToOne
    private User to;

    public Message() {
    }

    public Message(String text, User from, User to) {
        this.text = text;
        this.from = from;
        this.to = to;
        this.msgDate=LocalDateTime.now();
    }

    public int getMsgId() {
        return msgId;
    }

    public LocalDateTime getMsgDate() {
        return msgDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }
}
