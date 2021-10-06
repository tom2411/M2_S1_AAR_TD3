package services;

import entities.Message;
import entities.User;
import exceptions.UserAllreadyExistsException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service
public class Facade {
    // Injection de l'entity manager, pour accès à la BD
    @PersistenceContext
    private EntityManager em;

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

    public List findByTextAAR() {
        Query q=em.createQuery("Select m From Message m where lower(m.text) like '%aar%'");
        return q.getResultList();
    }

    public List<Message> findByMotif(String motif) {
        Query q=em.createQuery("Select m From Message m where lower(m.text) like :m");
        q.setParameter("m", "%"+motif.toLowerCase()+"%");
        return q.getResultList();
    }

}
