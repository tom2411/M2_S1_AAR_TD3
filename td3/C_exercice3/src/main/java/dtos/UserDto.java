package dtos;

/**
 * La classe User a ici un usage limité : elle sert juste à faire transiter des infos.
 * On pourrait lui attribuer un rôle de stockage, et elle se retrouverait alors dans le modèle.
 * Cette dualité transfert / stockage est souvent utilisée mais elle n'a pas que des avantages...
 * Nous en reparlerons plus tard...
 */
public class UserDto {
    private String login;
    private String password;

    public UserDto(){}

    public UserDto(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
