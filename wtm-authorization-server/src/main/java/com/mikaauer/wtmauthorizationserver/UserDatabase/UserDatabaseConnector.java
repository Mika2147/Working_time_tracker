package com.mikaauer.wtmauthorizationserver.UserDatabase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class UserDatabaseConnector {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    public boolean insertUser(Users user){
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            entityManager.persist(user);
            transactionManager.commit(status);
            return true;
        } catch (Exception e) {
            transactionManager.rollback(status);
        }

        return false;
    }

    public Optional<Users> getUser(String username){
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.name = :username");
        query.setParameter("username", username);
        List<Users> users = query.getResultList();
        return users.isEmpty() ? Optional.empty() : Optional.of(users.getFirst());
    }


}
