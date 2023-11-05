package co.edu.iudigital.rrhhfuncionarios.data.dao.implementacion;


import co.edu.iudigital.rrhhfuncionarios.data.dao.FuncionarioDao;
import co.edu.iudigital.rrhhfuncionarios.data.models.Familiar;
import co.edu.iudigital.rrhhfuncionarios.data.models.Funcionario;
import co.edu.iudigital.rrhhfuncionarios.data.models.TituloEstudio;
import co.edu.iudigital.rrhhfuncionarios.utils.messages.Message;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class FuncionarioDaoHibernate extends BaseDaoImplHibernate<Long, Funcionario> implements FuncionarioDao {
    private final Logger logger = Logger.getLogger(FuncionarioDaoHibernate.class.getName());

    public List<Funcionario> findByNombre(String nombre) {
        if (sessionFactory == null) {
            logger.severe(Message.SESSION_FACTORY_NULL);
            return Collections.emptyList();
        }
        StatelessSession session = sessionFactory.openStatelessSession();
        try {
            session.getTransaction().begin();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Funcionario> criteriaQuery = cb.createQuery(Funcionario.class);
            Root<Funcionario> root = criteriaQuery.from(Funcionario.class);

            criteriaQuery.select(root).where(cb.like(cb.upper(root.get("nombre")), "%" + nombre + "%"));
            List<Funcionario> result = session.createQuery(criteriaQuery).getResultList();

            session.getTransaction().commit();
            return result;
        } catch (ClassCastException e) {
            logger.severe(Message.FAILED_OBJECT_CONVERSION_TO_ORIGIN_CLASS);
            if (isSessionGoodForRollBack(session)) {
                session.getTransaction().rollback();
            }
        } catch (RuntimeException e) {
            logger.severe(Message.UNKNOWN_ERROR_DATABASE + "\n" + e.getMessage());
            if (isSessionGoodForRollBack(session)) {
                session.getTransaction().rollback();
            }
        } finally {
            session.close();
        }
        return Collections.emptyList();
    }


    public boolean saveWithFamiliares(Funcionario funcionario) {
        if (sessionFactory == null) {
            logger.severe(Message.SESSION_FACTORY_NULL);
            return false;
        }
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            if(funcionario.getId()!=null){
                logger.info("update selected");
                session.merge(funcionario);
                session.flush();
            }else {
                session.persist(funcionario);
            }

            if (funcionario.getFamiliares() != null && !funcionario.getFamiliares().isEmpty()) {
                funcionario.getFamiliares().stream().filter(familiar -> familiar.getId()==null).forEach(familiar -> {
                    familiar.setFamiliarDe(funcionario);
                    session.persist(familiar);
                });
            }
            if (funcionario.getEstudios() != null && !funcionario.getEstudios().isEmpty()) {
                funcionario.getEstudios().stream().filter(estudio -> estudio.getId()==null).forEach(estudio -> {
                    estudio.setPerteceA(funcionario);
                    session.persist(estudio);
                });
            }

            session.getTransaction().commit();
            return true;
        } catch (ClassCastException e) {
            logger.severe(Message.FAILED_OBJECT_CONVERSION_TO_ORIGIN_CLASS);
            if (isSessionGoodForRollBack(session)) {
                session.getTransaction().rollback();
            }
        } catch (RuntimeException e) {
            logger.severe(Message.UNKNOWN_ERROR_DATABASE + "\n" + e.getMessage());
            if (isSessionGoodForRollBack(session)) {
                session.getTransaction().rollback();
            }
        } finally {
            session.close();
        }
        return false;
    }

    public Optional<Funcionario> getFullFuncionario(Funcionario objeto) {
        if (sessionFactory == null) {
            logger.severe(Message.SESSION_FACTORY_NULL);
            return Optional.empty();
        }
        if(objeto.getId() == null || objeto.getId()==0){
            logger.info("id mal: "+objeto.getId());
            return Optional.empty();
        }
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();


            Funcionario result = session.createSelectionQuery(
                    "select f from Funcionario f join fetch f.estadoCivil join fetch f.tipoIdentificacion " +
                            "left join fetch f.familiares fam left join fetch fam.tipoRelacion where f.id = :id", Funcionario.class).setParameter("id",objeto.getId()).getSingleResult();

            List<TituloEstudio> estudios =session.
                    createSelectionQuery("select e from TituloEstudio e join fetch e.universidad join e.nivelEstudio where e.perteceA.id = :id", TituloEstudio.class)
                    .setParameter("id",objeto.getId()).getResultList();


            result.setEstudios(estudios);
            session.getTransaction().commit();

            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (ClassCastException e) {
            logger.severe(Message.FAILED_OBJECT_CONVERSION_TO_ORIGIN_CLASS);
            session.getTransaction().rollback();
        } catch (RuntimeException e) {
            logger.severe(Message.UNKNOWN_ERROR_DATABASE + "\n" + e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return Optional.empty();
    }
}
