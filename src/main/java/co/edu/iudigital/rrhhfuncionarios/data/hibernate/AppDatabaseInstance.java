package co.edu.iudigital.rrhhfuncionarios.data.hibernate;

import co.edu.iudigital.rrhhfuncionarios.data.models.*;
import co.edu.iudigital.rrhhfuncionarios.exceptions.DatabaseException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.logging.Logger;


public class AppDatabaseInstance {
    private static final Logger logger = Logger.getLogger(AppDatabaseInstance.class.getName());
    private static SessionFactory sessionFactory = null;

    private AppDatabaseInstance() {
    }

    private static void setUp() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .build();
        try {

            sessionFactory =
                    new MetadataSources(registry)
                            //add the models classes
                            .addAnnotatedClasses(TipoIdentificacion.class,RelacionFamiliar.class,
                                    Funcionario.class,Familiar.class, EstadoCivil.class, TituloEstudio.class,
                                    Universidad.class, NivelEstudio.class)
                            .buildMetadata()
                            .buildSessionFactory();
        }
        catch (Exception e){
            StandardServiceRegistryBuilder.destroy(registry);
            throw new DatabaseException(e.getMessage());
        }
    }

    public static SessionFactory getSessionFactory() {
        try {
            if (sessionFactory == null) {
                setUp();
                if(sessionFactory == null){
                    throw new DatabaseException("No se pudo obtener la sessionFactory");
                }
            }
        } catch (DatabaseException e) {
            logger.severe(e.getMessage());
            //e.printStackTrace();
        } catch (IllegalStateException e){
            logger.severe("No se pudo obtener conexión");
        }
        return sessionFactory;
    }
}
