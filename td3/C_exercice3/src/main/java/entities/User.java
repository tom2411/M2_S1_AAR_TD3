package entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {
    @Id
    private String login;
    private String password;
    @OneToMany(mappedBy = "from",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("msgDate")
    private List<Message> sent;
    @OneToMany(mappedBy = "to",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("msgDate")
    private List<Message> received;

    public User(){}

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<Message> getSent() {
        return sent;
    }

    public void setSent(List<Message> sent) {
        this.sent = sent;
    }

    public List<Message> getReceived() {
        return received;
    }

    public void setReceived(List<Message> received) {
        this.received = received;
    }
}
