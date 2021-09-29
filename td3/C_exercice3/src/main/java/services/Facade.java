package services;

import entities.Message;
import entities.User;
import exceptions.UserAllreadyExistsException;
import exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service
public class Facade {
    // Injection de l'entity manager, pour accès à la BD
    @PersistenceContext
    private EntityManager em;

    // On n'utilise plus de map : on a une base de données
    //private Map<String,String> users;

    // le peuplement se fait maintenant avec un script sql
   /* @PostConstruct
    public void fillMap(){
        users=new HashMap<>();
        users.put("alice","alice");
        users.put("bob","bob");
    }
    */


    public boolean checkLP(String login,String password) {
        // On va maintenant chercher l'utilisateur dans la BD à partir du login
        User user=em.find(User.class,login);
        if (user==null) {
            return false;
        } else {
            return (user.getPassword().equals(password));
        }
   }

   @Transactional
   public User createUser(String login,String password) throws UserAllreadyExistsException {
        User user=em.find(User.class,login);
        if (user!=null) {
            throw new UserAllreadyExistsException();
        }
        user =new User(login,password);
        em.persist(user);
        return user;
   }

    @Transactional
    public void removeUser(String login) throws UserNotFoundException {
        User user=em.find(User.class,login);
        if (user==null) {
            throw new UserNotFoundException();
        }
        em.remove(user);
    }

   @Transactional
   public void createMessage(String strFrom, String strTo, String text){
        User from=em.find(User.class,strFrom);
        User to=em.find(User.class,strTo);
        Message m=new Message(text,from,to);
        em.persist(m);
   }

   public User retrieveUser(String login) {
        return em.find(User.class,login);
   }

   public List<String> getAllUserNames() {
        return em.createQuery("select u.login from User u").getResultList();
   }

    public List<String> getAllUserNamesExcept(String name) {
        return em.createQuery("select u.login from User u where u.login<>:n").setParameter("n",name).getResultList();
    }

   public Collection<Message> getReceivedMessage(String login){
        User u=retrieveUser(login);
        return u.getReceived();
   }

    public Collection<Message> getSentMessage(String login){
        User u=retrieveUser(login);
        return u.getSent();
    }

}
