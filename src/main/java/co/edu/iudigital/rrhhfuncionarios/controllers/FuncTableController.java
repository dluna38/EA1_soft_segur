package co.edu.iudigital.rrhhfuncionarios.controllers;

import co.edu.iudigital.rrhhfuncionarios.HelloApplication;
import co.edu.iudigital.rrhhfuncionarios.data.dao.implementacion.FuncionarioDaoHibernate;
import co.edu.iudigital.rrhhfuncionarios.data.models.Funcionario;
import co.edu.iudigital.rrhhfuncionarios.utils.javafx.ButtonForTable;
import co.edu.iudigital.rrhhfuncionarios.utils.javafx.PosForGraphics;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class FuncTableController {
    @FXML
    TableView<Funcionario> tableFuncionarios;
    @FXML
    Button btnCrearFuncionario;
    @FXML
    Button btnBuscarNombre;
    @FXML
    TextField txtFieldBuscarNombre;
    private ObservableList<Funcionario> listaFuncionarios;
    private Stage currentStage;
    private Scene scene;

    private final Logger logger = Logger.getLogger(FuncTableController.class.getName());
    public void configurarController(Stage stage,Scene scene){
        this.currentStage=stage;
        this.scene = scene;
        preparaTabla();
    }

    public void clickBtnCrearFuncionario(ActionEvent actionEvent) {
        moveToFormView();
    }

    public void clickBtnBuscarNombre(ActionEvent actionEvent) {
        String nombre = txtFieldBuscarNombre.getText();
        FuncionarioDaoHibernate funcDao = new FuncionarioDaoHibernate();
        if (!nombre.isEmpty()){
            updateList(funcDao.findByNombre(nombre));
            return;
        }
        updateListWithFindAll();
    }


    private void preparaTabla(){
        TableColumn<Funcionario,String> column1 = new TableColumn<>("Nombre");
        column1.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Funcionario,String> column2 = new TableColumn<>("Apellido");
        column2.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        TableColumn<Funcionario,String> column3 = new TableColumn<>("Identificación");
        column3.setCellValueFactory(new PropertyValueFactory<>("NumeroIdentificacion"));
        TableColumn<Funcionario,String> column4 = new TableColumn<>("Teléfono");
        column4.setCellValueFactory(new PropertyValueFactory<>("Telefono"));


        TableColumn<Funcionario, FlowPane> columnWide = new TableColumn<>("Acciones");
        TableColumn<Funcionario, Button> columnEdit = new TableColumn<>("Editar");
        columnEdit.setCellValueFactory(param ->
                new ButtonForTable("Editar", event -> iniciarParaForm(OperacionForm.EDITAR,param.getValue())));
        columnEdit.setCellFactory(funcionarioButtonTableColumn -> new PosForGraphics<>(Pos.CENTER));

        TableColumn<Funcionario, Button> columnDelete = new TableColumn<>("Eliminar");
        columnDelete.setCellValueFactory(param ->
                new ButtonForTable("Borrar", event -> deleteFuncionario(param.getValue())));
        columnDelete.setCellFactory(tableColumn -> new PosForGraphics<>(Pos.CENTER));
        TableColumn<Funcionario, Button> columnDetail = new TableColumn<>("Detalle");
        columnDetail.setCellValueFactory(param ->
                new ButtonForTable("Detalle", event -> iniciarParaForm(OperacionForm.DETALLE,param.getValue())));
        columnDetail.setCellFactory(tableColumn -> new PosForGraphics<>(Pos.CENTER));

        columnWide.getColumns().add(columnDetail);
        columnWide.getColumns().add(columnEdit);
        columnWide.getColumns().add(columnDelete);

        tableFuncionarios.getColumns().add(column1);
        tableFuncionarios.getColumns().add(column2);
        tableFuncionarios.getColumns().add(column3);
        tableFuncionarios.getColumns().add(column4);
        tableFuncionarios.getColumns().add(columnWide);
        updateList();
        tableFuncionarios.setItems(this.listaFuncionarios);
    }

    private void iniciarParaForm(OperacionForm operacion,Funcionario funcionario){
        logger.info("iniciando: "+operacion.name()+" "+funcionario.getId());
        Optional<Funcionario> detailFun = new FuncionarioDaoHibernate().getFullFuncionario(funcionario);
        if(detailFun.isEmpty()){
            logger.info("no se encontro el funcionario");
            return;
        }
        System.out.println(detailFun.get());
        moveToFormView(operacion, detailFun.get());
    }
    private void updateList(List<Funcionario> lista){
        if(lista == null){
            FuncionarioDaoHibernate funcDAO = new FuncionarioDaoHibernate();
            this.listaFuncionarios= FXCollections.observableList(funcDAO.findAll());
            return;
        }
        this.listaFuncionarios.clear();
        this.listaFuncionarios.addAll(lista);
    }
    public void updateList(){
        updateList(null);
    }
    public void updateListWithFindAll(){
        FuncionarioDaoHibernate funcDao = new FuncionarioDaoHibernate();
        updateList(funcDao.findAll());
    }

    public void moveToFormView(OperacionForm modo,Funcionario funcionario){
        FXMLLoader fxmlFormFunc = new FXMLLoader(HelloApplication.class.getResource("form-func-view.fxml"));
        try {
            Scene sceneToLaunch = new Scene(fxmlFormFunc.load(), 800, 800);
            FormFuncController controller = fxmlFormFunc.getController();
            if(modo == OperacionForm.EDITAR){
                logger.info("move to editar");
                controller.configurarControllerParaEditar(this.currentStage,this.scene,this,funcionario);
            } else if (modo == OperacionForm.DETALLE) {
                logger.info("move to detalle");
                controller.configurarControllerParaDetalle(this.currentStage,this.scene,this,funcionario);
            } else {
                logger.info("move to añadir");
                controller.configurarController(this.currentStage, this.scene, this);
            }
            currentStage.setScene(sceneToLaunch);
            currentStage.show();
        } catch (IOException e) {

        }
    }
    public void moveToFormView(){
        moveToFormView(OperacionForm.CREAR,null);
    }

    public void deleteFuncionario(Funcionario funcionario){
        Alert confirmUser = new Alert(Alert.AlertType.CONFIRMATION);
        confirmUser.setTitle("Confirmar");
        confirmUser.setHeaderText("Borrar Funcionario");
        confirmUser.setContentText("¿Estas seguro de borrar el Funcionario?");
        Optional<ButtonType> result = confirmUser.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            FuncionarioDaoHibernate funcionarioDaoHibernate = new FuncionarioDaoHibernate();
            if (funcionarioDaoHibernate.delete(funcionario)) updateListWithFindAll();
        }
    }

    public enum OperacionForm{
        EDITAR,DETALLE,CREAR
    }
}
