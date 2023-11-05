package co.edu.iudigital.rrhhfuncionarios.controllers;

import co.edu.iudigital.rrhhfuncionarios.data.dao.implementacion.*;
import co.edu.iudigital.rrhhfuncionarios.data.models.TituloEstudio;
import co.edu.iudigital.rrhhfuncionarios.data.models.*;
import co.edu.iudigital.rrhhfuncionarios.utils.javafx.FormularioFuncionario;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.ArrayList;

public class FormFuncController {

    private Stage stage;
    private Scene previousScene;
    private FuncTableController previousSceneController;
    private FormularioFuncionario formularioFuncionario;
    @FXML
    private Label labelMain;
    @FXML
    private DatePicker datePickerFNacimiento;
    @FXML
    private Button btnEnviarForm;
    @FXML
    private TextField txtNombre,txtApellido,txtNumIdentificacion,txtDireccion,txtTelefono;
    @FXML
    private ChoiceBox<TipoIdentificacion> choiceBoxTIdentificacion;
    @FXML
    private ChoiceBox<EstadoCivil> choiceBoxEstadoCivil;
    @FXML
    private ChoiceBox<Sexo> choiceBoxSexo;
    @FXML
    private ChoiceBox<RelacionFamiliar> choiceBoxTRelacion;
    @FXML
    private ChoiceBox<Universidad> choiceBoxUniversidad;
    @FXML
    private ChoiceBox<NivelEstudio> choiceBoxNEstudio;
    @FXML
    private TextField txtTitulo;
    @FXML
    private TextField txtNombreFamiliar,txtApellidoFamiliar,txtTelefonoFamiliar;
    @FXML
    private TableView<Familiar> tableFamiliares;
    @FXML
    private TableView<TituloEstudio> tableFormacionAcademica;
    private ObservableList<Familiar> listaFamiliares;
    private ObservableList<TituloEstudio> listaEstudios;
    private boolean modoEditar=false;
    public void clickVolver(ActionEvent actionEvent) {
        stage.setScene(previousScene);
        stage.show();
    }
    public void clickEnviarForm(ActionEvent actionEvent) {
        boolean result;
        if(modoEditar){
            result = formularioFuncionario.saveFuncionario(true);
        }else{
            result = formularioFuncionario.saveFuncionario();
        }
        if(result){
            formularioFuncionario.setUpdateFuncionario(null);
            previousSceneController.updateListWithFindAll();
            clickVolver(null);
        }
    }
    public void clickAgregarFamiliar(ActionEvent actionEvent) {
        String nombre = txtNombreFamiliar.getText();
        String apellido = txtApellidoFamiliar.getText();
        String telefono = txtTelefonoFamiliar.getText();
        RelacionFamiliar relacion = choiceBoxTRelacion.getValue();
        if(nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || relacion.getId() == null) return;
        listaFamiliares.add(Familiar.builder().nombre(nombre).apellido(apellido)
                .telefono(telefono)
                .tipoRelacion(relacion).build());
        txtNombreFamiliar.setText("");
        txtApellidoFamiliar.setText("");
        txtTelefonoFamiliar.setText("");
    }
    public void clickAgregarEstudio(ActionEvent actionEvent) {
        Universidad universidad = choiceBoxUniversidad.getValue();
        NivelEstudio nivelEstudio = choiceBoxNEstudio.getValue();
        String titulo = txtTitulo.getText();
        if(universidad.getId()==null || nivelEstudio.getId()==null || titulo.isEmpty()) return;
        listaEstudios.add(TituloEstudio.builder().universidad(universidad).nivelEstudio(nivelEstudio).titulo(titulo).build());
        txtTitulo.setText("");
    }
    public void configurarController(Stage stage,Scene previousScene,FuncTableController sceneController){
        this.previousScene = previousScene;
        this.stage = stage;
        this.previousSceneController = sceneController;
        configurarChoiceBoxs();
        prepararTablaFamiliares();
        prepararTablaEstudios();
        configurarFormulario();
    }
    public void configurarControllerParaEditar(Stage stage,Scene previousScene,FuncTableController sceneController,Funcionario funcionario){
        configurarController(stage,previousScene,sceneController);
        modoEditar=true;
        labelMain.setText("Editando Funcionario");
        txtNumIdentificacion.setDisable(true);
        formularioFuncionario.llenarFields(funcionario);
    }
    public void configurarControllerParaDetalle(Stage stage,Scene previousScene,FuncTableController sceneController,Funcionario funcionario){
        configurarController(stage,previousScene,sceneController);
        labelMain.setText("Detalle Funcionario");
        setAllFieldDisable(false);
        btnEnviarForm.setVisible(false);
        formularioFuncionario.llenarFields(funcionario);
    }
    private void configurarChoiceBoxs(){

        TipoIdentificacionDaoHibernate tIdentificacionDao = new TipoIdentificacionDaoHibernate();
        EstadoCivilDaoHibernate estadoCivilDao = new EstadoCivilDaoHibernate();
        RelacionFamiliarDaoHibernate relacionFamiliarDaoHibernate = new RelacionFamiliarDaoHibernate();

        TipoIdentificacion emptyTipo = TipoIdentificacion.builder().tipo("Selecciona uno").build();
        choiceBoxTIdentificacion.setConverter(new StringConverter<>() {
            @Override
            public String toString(TipoIdentificacion tipoIdentificacion) {
                return tipoIdentificacion.getTipo();
            }
            @Override
            public TipoIdentificacion fromString(String s) {
                return null;
            }
        });
        choiceBoxTIdentificacion.getItems().add(emptyTipo);
        choiceBoxTIdentificacion.getItems().addAll(tIdentificacionDao.findAll());
        choiceBoxTIdentificacion.setValue(emptyTipo);

        EstadoCivil emptyEstado = EstadoCivil.builder().nombre("Selecciona uno").build();
        choiceBoxEstadoCivil.setConverter(new StringConverter<>() {
            @Override
            public String toString(EstadoCivil estadoCivil) {
                return estadoCivil.getNombre();
            }

            @Override
            public EstadoCivil fromString(String s) {
                return null;
            }
        });
        choiceBoxEstadoCivil.getItems().add(emptyEstado);
        choiceBoxEstadoCivil.getItems().addAll(estadoCivilDao.findAll());
        choiceBoxEstadoCivil.setValue(emptyEstado);

        RelacionFamiliar emptyRelacion = new RelacionFamiliar("Selecciona una");
        choiceBoxTRelacion.setConverter(new StringConverter<>() {
            @Override
            public String toString(RelacionFamiliar relacionFamiliar) {
                return relacionFamiliar.getTipoRelacion();
            }

            @Override
            public RelacionFamiliar fromString(String s) {
                return null;
            }
        });
        choiceBoxTRelacion.getItems().add(emptyRelacion);
        choiceBoxTRelacion.getItems().addAll(relacionFamiliarDaoHibernate.findAll());
        choiceBoxTRelacion.setValue(emptyRelacion);

        choiceBoxSexo.getItems().addAll(Sexo.values());
        choiceBoxSexo.setValue(Sexo.HOMBRE);

        Universidad emptyUniversidad = new Universidad("Selecciona una");
        choiceBoxUniversidad.setConverter(new StringConverter<>() {
            @Override
            public String toString(Universidad universidad) {
                return universidad.getNombre();
            }

            @Override
            public Universidad fromString(String s) {
                return null;
            }
        });
        choiceBoxUniversidad.getItems().add(emptyUniversidad);
        choiceBoxUniversidad.getItems().addAll(new UniversidadDaoHibernate().findAll());
        choiceBoxUniversidad.setValue(emptyUniversidad);

        NivelEstudio emptyNivel = new NivelEstudio("Selecciona uno");
        choiceBoxNEstudio.setConverter(new StringConverter<>() {
            @Override
            public String toString(NivelEstudio nivelEstudio) {
                return nivelEstudio.getNivel();
            }

            @Override
            public NivelEstudio fromString(String s) {
                return null;
            }
        });
        choiceBoxNEstudio.getItems().add(emptyNivel);
        choiceBoxNEstudio.getItems().addAll(new NivelEstudioDaoHibernate().findAll());
        choiceBoxNEstudio.setValue(emptyNivel);
    }

    private void prepararTablaFamiliares(){
        TableColumn<Familiar,String> columnNombre = new TableColumn<>("Nombre");
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Familiar,String> columnApellido = new TableColumn<>("Apellido");
        columnApellido.setCellValueFactory(new PropertyValueFactory<>("Apellido"));
        TableColumn<Familiar,String> columnTelefono = new TableColumn<>("Telefono");
        columnTelefono.setCellValueFactory(new PropertyValueFactory<>("Telefono"));
        TableColumn<Familiar,String> columnTRelacion = new TableColumn<>("Tipo de relación");
        columnTRelacion.setCellValueFactory(new PropertyValueFactory<>("tipoRelacion"));
        tableFamiliares.setPlaceholder(new Label("Sin Familiares"));

        tableFamiliares.getColumns().add(columnNombre);
        tableFamiliares.getColumns().add(columnApellido);
        tableFamiliares.getColumns().add(columnTelefono);
        tableFamiliares.getColumns().add(columnTRelacion);
        listaFamiliares = FXCollections.observableList(new ArrayList<>());
        tableFamiliares.setItems(listaFamiliares);
    }

    private void prepararTablaEstudios(){
        TableColumn<TituloEstudio,String> columnTitulo = new TableColumn<>("Titulo");
        columnTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        TableColumn<TituloEstudio,String> columnNivel = new TableColumn<>("Nivel");
        columnNivel.setCellValueFactory(new PropertyValueFactory<>("nivelEstudio"));
        TableColumn<TituloEstudio,String> columnUniversidad = new TableColumn<>("Universidad");
        columnUniversidad.setCellValueFactory(new PropertyValueFactory<>("universidad"));

        tableFormacionAcademica.getColumns().add(columnTitulo);
        tableFormacionAcademica.getColumns().add(columnNivel);
        tableFormacionAcademica.getColumns().add(columnUniversidad);
        listaEstudios = FXCollections.observableList(new ArrayList<>());
        tableFormacionAcademica.setItems(listaEstudios);
    }
    private void configurarFormulario(){
        formularioFuncionario = new FormularioFuncionario();
        formularioFuncionario.setTxtNombre(txtNombre);
        formularioFuncionario.setTxtApellido(txtApellido);
        formularioFuncionario.setTxtDireccion(txtDireccion);
        formularioFuncionario.setTxtTelefono(txtTelefono);
        formularioFuncionario.setTxtNumIdentificacion(txtNumIdentificacion);
        formularioFuncionario.setChoiceBoxTIdentificacion(choiceBoxTIdentificacion);
        formularioFuncionario.setChoiceBoxEstadoCivil(choiceBoxEstadoCivil);
        formularioFuncionario.setChoiceBoxSexo(choiceBoxSexo);
        formularioFuncionario.setDatePickerFNacimiento(datePickerFNacimiento);
        formularioFuncionario.setFamiliares(listaFamiliares);
        formularioFuncionario.setEstudios(listaEstudios);
    }

    private void setAllFieldDisable(boolean state){
        txtNumIdentificacion.setEditable(state);
        txtNombre.setEditable(state);
        txtTelefono.setEditable(state);
        txtApellido.setEditable(state);
        txtDireccion.setEditable(state);
        txtTitulo.setEditable(state);
        choiceBoxSexo.setDisable(!state);
        choiceBoxTRelacion.setDisable(!state);
        choiceBoxTIdentificacion.setDisable(!state);
        choiceBoxEstadoCivil.setDisable(!state);
        choiceBoxNEstudio.setDisable(!state);
        choiceBoxUniversidad.setDisable(!state);
        txtTelefonoFamiliar.setEditable(state);
        txtNombreFamiliar.setEditable(state);
        txtApellidoFamiliar.setEditable(state);
    }


}
