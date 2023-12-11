/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

/**
 *
 * @author PC
 */
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class AdminService {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean authenticate(String userName, String password) {
        Query query = entityManager.createQuery("SELECT a FROM Admin a WHERE a.userName = :userName AND a.password = :password");
        query.setParameter("userName", userName);
        query.setParameter("password", password);

        try {
            return query.getSingleResult() != null;
        } catch (Exception e) {
            return false;
        }
    }
}
