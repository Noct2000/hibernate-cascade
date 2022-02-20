package core.basesyntax.dao.impl;

import core.basesyntax.dao.MessageDao;
import core.basesyntax.model.Message;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;

public class MessageDaoImpl extends AbstractDao implements MessageDao {
    public MessageDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Message create(Message entity) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (PersistenceException persistenceException) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Couldn't save message: "
                    + entity, persistenceException);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return entity;
    }

    @Override
    public Message get(Long id) {
        Message message;
        try (Session session = factory.openSession()) {
            message = session.get(Message.class, id);
        } catch (PersistenceException persistenceException) {
            throw new RuntimeException("Couldn't get message by id: "
                    + id, persistenceException);
        }
        return message;
    }

    @Override
    public List<Message> getAll() {
        List<Message> allMessages;
        try (Session session = factory.openSession()) {
            Query<Message> allMessageQuery = session
                    .createQuery("from Message", Message.class);
            allMessages = allMessageQuery.getResultList();
        } catch (PersistenceException persistenceException) {
            throw new RuntimeException("Couldn't get all messages from DB",
                    persistenceException);
        }
        return allMessages;
    }

    @Override
    public void remove(Message entity) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.remove(entity);
            transaction.commit();
        } catch (PersistenceException persistenceException) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Couldn't remove message: "
                    + entity, persistenceException);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
