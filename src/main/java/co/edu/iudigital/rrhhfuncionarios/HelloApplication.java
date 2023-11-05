package co.edu.iudigital.rrhhfuncionarios;

import co.edu.iudigital.rrhhfuncionarios.controllers.FuncTableController;
import co.edu.iudigital.rrhhfuncionarios.data.dao.EstadoCivilDao;
import co.edu.iudigital.rrhhfuncionarios.data.dao.implementacion.*;
import co.edu.iudigital.rrhhfuncionarios.data.hibernate.AppDatabaseInstance;
import co.edu.iudigital.rrhhfuncionarios.data.models.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {


        FXMLLoader fxmlFuncTable = new FXMLLoader(HelloApplication.class.getResource("func-table-view.fxml"));
        Scene sceneMain = new Scene(fxmlFuncTable.load(), 800, 600);
        FuncTableController controller = fxmlFuncTable.getController();
        controller.configurarController(stage,sceneMain);

        stage.setTitle("Funcionarios");
        stage.setScene(sceneMain);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }













    public void cargarInformacionBase(){
        TipoIdentificacionDaoHibernate tipoIdenDao = new TipoIdentificacionDaoHibernate();
        EstadoCivilDaoHibernate estadoCivilDao = new EstadoCivilDaoHibernate();
        RelacionFamiliarDaoHibernate relacionfamiDaoHibernate = new RelacionFamiliarDaoHibernate();
        FuncionarioDaoHibernate funcionarioDaoHibernate = new FuncionarioDaoHibernate();
        FamiliarDaoHibernate familiarDaoHibernate = new FamiliarDaoHibernate();
        UniversidadDaoHibernate universidadDaoHibernate = new UniversidadDaoHibernate();
        NivelEstudioDaoHibernate nivelEstudioDaoHibernate = new NivelEstudioDaoHibernate();

    }
}