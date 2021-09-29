package controllers;

import dtos.MessageDto;
import dtos.UserDto;
import exceptions.UserAllreadyExistsException;
import exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import services.Facade;

@Controller
@SessionAttributes("courant")
@RequestMapping("/")
public class Exemple3Controller {
    @Autowired
    private Facade facade;
    @RequestMapping("")
    public String toLogin(Model model) {
        //ici on doit renvoyer un User du fait traitement avec modelAttribute et path côté jsp
        model.addAttribute(new UserDto());
        return("login");
    }

    // on passe un objet user en paramètre : mapping automatique des champs du formulaire
    // on jour aussi avec les messages d'erreurs (BindingResult) ...
    @PostMapping("login")
    public String checkLP(UserDto userDto, BindingResult result, Model model){
        if (facade.checkLP(userDto.getLogin(), userDto.getPassword())) {
            // on place courant dans le modèle, mais il s'agit d'un attribut de session, il se retrouve ainsi conservé en session
            model.addAttribute("courant", userDto.getLogin());
            model.addAttribute("username", userDto.getLogin());
            addMessageToModel(model, userDto.getLogin());
            return "welcome";
        } else {
            // on crée à la volée un "ObjectError" : erreur globale dans l'objet (ici l'objet c'est l'instance de user où transitent les infos de login)
            result.addError(new ObjectError("user","Les informations saisies ne correspondent pas à un utilisateur connu."));
            System.out.println(result.hasErrors());
            // le user du model est renvoyé tel quel à la jsp, et on préserve les valeurs saisies (comment réinitialiser ?)
            return "login";
        }
    }

    @PostMapping("register")
    public String register(UserDto userDto,BindingResult result, Model model){
        try {
            facade.createUser(userDto.getLogin(),userDto.getPassword());
        } catch (UserAllreadyExistsException e) {
            result.addError(new ObjectError("user","Ce login n'est pas disponible."));
            return "login";
        }
        model.addAttribute("courant", userDto.getLogin());
        model.addAttribute("username", userDto.getLogin());
        addMessageToModel(model, userDto.getLogin());
        return "welcome";
    }

    @RequestMapping("simplecheck")
    public String simpleCheck(@SessionAttribute String courant,Model model){
        System.out.println(courant);
        model.addAttribute("username",courant);
        addMessageToModel(model,courant);
        return "welcome";
    }

    @RequestMapping("logout")
    public String logout(SessionStatus status,Model model) {
        status.setComplete();
        model.addAttribute("courant",null);
        model.addAttribute(new UserDto());
        return "login";
    }

    @RequestMapping("removeuser")
    public String removeUser(@SessionAttribute String courant,Model model){
        System.out.println(courant);
        model.addAttribute("username",courant);
        try {
            facade.removeUser(courant);
            model.addAttribute("courant",null);
            model.addAttribute(new UserDto());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        return "login";
    }


    @PostMapping("write")
    public String write(MessageDto msgDto, @SessionAttribute String courant, Model model){
        facade.createMessage(courant,msgDto.getTo(),msgDto.getText());
        model.addAttribute("username",courant);
        addMessageToModel(model,courant);
        return "welcome";
    }

    /**
     * cette méthode privée évite d'insérer les éléments liées aux messages (existants et nouveaux) dans la page welcome.
     * Quand vous serez à l'aise avec Spring MVC, regardez du côté de @ModelAttribute...
     * pour faire la même chose mais en plus propre...
     * @param model
     */
    private void addMessageToModel(Model model,String courant) {
        model.addAttribute("received",facade.getReceivedMessage(courant));
        model.addAttribute("sent",facade.getSentMessage(courant));
        model.addAttribute("users",facade.getAllUserNamesExcept(courant));
        model.addAttribute("msgDto",new MessageDto());
    }

}
