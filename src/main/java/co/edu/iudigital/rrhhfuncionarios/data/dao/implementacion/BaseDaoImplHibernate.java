package co.edu.iudigital.rrhhfuncionarios.data.dao.implementacion;

import co.edu.iudigital.rrhhfuncionarios.data.dao.DAO;
import co.edu.iudigital.rrhhfuncionarios.data.hibernate.AppDatabaseInstance;
import co.edu.iudigital.rrhhfuncionarios.utils.messages.Message;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class BaseDaoImplHibernate<I,T> implements DAO<I,T> {

    private static final Logger logger = Logger.getLogger(BaseDaoImplHibernate.class.getName());
    protected final SessionFactory sessionFactory;
    public BaseDaoImplHibernate() {
       sessionFactory = AppDatabaseInstance.getSessionFactory();
    }

    @Override
    public Optional<T> save(T objeto) {
        if(sessionFactory ==null){
            logger.severe(Message.SESSION_FACTORY_NULL);
            return Optional.empty();
        }
        Session session = sessionFactory.openSession();
        try{
            session.getTransaction().begin();
            session.persist(objeto);
            session.getTransaction().commit();
            return Optional.of(objeto);
        } catch(ClassCastException e) {
            logger.severe("No se pudo convertir el objeto insertado en la BD");
            if(isSessionGoodForRollBack(session)){
                session.getTransaction().rollback();
            }
        } catch (RuntimeException e){
            logger.severe(Message.UNKNOWN_ERROR_DATABASE +"\n"+e.getMessage());
            if(isSessionGoodForRollBack(session)){
                session.getTransaction().rollback();
            }
        } finally {
            session.close();
        }
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        if(sessionFactory == null){
            logger.severe(Message.SESSION_FACTORY_NULL);
            return Collections.emptyList();
        }
        Session session = sessionFactory.openSession();
        try{
            session.getTransaction().begin();
            List<T> result = session.createSelectionQuery("from "+getEntityClass().getName(),getEntityClass()).getResultList();
            session.getTransaction().commit();
            return result;
        } catch(ClassCastException e) {
            logger.severe(Message.FAILED_OBJECT_CONVERSION_TO_ORIGIN_CLASS);
            if(isSessionGoodForRollBack(session)){
                session.getTransaction().rollback();
            }
        } catch (RuntimeException e){
            logger.severe(Message.UNKNOWN_ERROR_DATABASE +"\n"+e.getMessage());
            if(isSessionGoodForRollBack(session)){
                session.getTransaction().rollback();
            }
        } finally {
            session.close();
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<T> findById(I id) {
        if(sessionFactory ==null){
            logger.severe(Message.SESSION_FACTORY_NULL);
            return Optional.empty();
        }
        Session session = sessionFactory.openSession();
        try{
            session.getTransaction().begin();
            Optional<T> result = Optional.of( (T) session.createSelectionQuery("from "+getEntityClass().getName()+" where id = :ID",getEntityClass())
                    .setParameter("ID",id).getSingleResult());
            session.getTransaction().commit();
            return result;
        } catch (NoResultException e) {
            return Optional.empty();
        } catch(ClassCastException e) {
            logger.severe(Message.FAILED_OBJECT_CONVERSION_TO_ORIGIN_CLASS);
            if(isSessionGoodForRollBack(session)){
                session.getTransaction().rollback();
            }
        } catch (RuntimeException e){
            logger.severe(Message.UNKNOWN_ERROR_DATABASE +"\n"+e.getMessage());
            if(isSessionGoodForRollBack(session)){
                session.getTransaction().rollback();
            }
        } finally {
            session.close();
        }
        return Optional.empty();
    }

    @Override
    public boolean update(T objeto) {
        if(sessionFactory ==null){
            logger.severe(Message.SESSION_FACTORY_NULL);
            return false;
        }
        Session session = sessionFactory.openSession();
        try{
            session.getTransaction().begin();
            session.merge(objeto);
            session.flush();
            session.getTransaction().commit();
            return true;
        } catch(ClassCastException e) {
            logger.severe(Message.FAILED_OBJECT_CONVERSION_TO_ORIGIN_CLASS);
            if(isSessionGoodForRollBack(session)){
                session.getTransaction().rollback();
            }
        } catch (RuntimeException e){
            logger.severe(Message.UNKNOWN_ERROR_DATABASE +"\n"+e.getMessage());
            if(isSessionGoodForRollBack(session)){
                session.getTransaction().rollback();
            }
        } finally {
            session.close();
        }
        return false;
    }

    @Override
    public boolean delete(T objeto) {
        if(sessionFactory ==null){
            logger.severe(Message.SESSION_FACTORY_NULL);
            return false;
        }
        Session session = sessionFactory.openSession();
        try{
            session.getTransaction().begin();
            session.remove(objeto);
            session.getTransaction().commit();
            return true;
        } catch(ClassCastException e) {
            logger.severe(Message.FAILED_OBJECT_CONVERSION_TO_ORIGIN_CLASS);
            if(isSessionGoodForRollBack(session)){
                session.getTransaction().rollback();
            }
        } catch (RuntimeException e){
            logger.severe(Message.UNKNOWN_ERROR_DATABASE +"\n"+e.getMessage());
            if(isSessionGoodForRollBack(session)){
                session.getTransaction().rollback();
            }
        } finally {
            session.close();
        }
        return false;
    }



    private Class<T> getEntityClass() throws ClassCastException {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    protected boolean isSessionGoodForRollBack(Session session){
        return session != null && (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK);
    }
    protected boolean isSessionGoodForRollBack(StatelessSession session){
        return session != null && (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK);
    }
}
