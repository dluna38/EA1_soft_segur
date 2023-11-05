package co.edu.iudigital.rrhhfuncionarios.utils.javafx;

import co.edu.iudigital.rrhhfuncionarios.data.dao.implementacion.FamiliarDaoHibernate;
import co.edu.iudigital.rrhhfuncionarios.data.dao.implementacion.FuncionarioDaoHibernate;
import co.edu.iudigital.rrhhfuncionarios.data.models.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormularioFuncionario {
    private DatePicker datePickerFNacimiento;
    private TextField txtNombre,txtApellido,txtNumIdentificacion,txtDireccion,txtTelefono;
    private ChoiceBox<TipoIdentificacion> choiceBoxTIdentificacion;
    private ChoiceBox<EstadoCivil> choiceBoxEstadoCivil;
    private ChoiceBox<Sexo> choiceBoxSexo;
    private ObservableList<Familiar> familiares;
    private ObservableList<TituloEstudio> estudios;
    private Funcionario updateFuncionario;
    public boolean validarFormulario(){
        //more validation
        if(txtNombre.getText().isEmpty()) return false;
        if(txtApellido.getText().isEmpty()) return false;
        if(txtTelefono.getText().isEmpty()) return false;
        if(txtNumIdentificacion.getText().isEmpty()) return false;
        if(txtDireccion.getText().isEmpty()) return false;
        if(choiceBoxEstadoCivil.getValue().getId()==null) return false;
        if(choiceBoxTIdentificacion.getValue().getId()==null) return false;
        if(datePickerFNacimiento.getValue() == null) return false;
        return true;
    }
    public void llenarFields(Funcionario funcionario){
        txtNombre.setText(funcionario.getNombre());
        txtApellido.setText(funcionario.getApellido());
        txtDireccion.setText(funcionario.getDireccion());
        txtTelefono.setText(funcionario.getTelefono());
        txtNumIdentificacion.setText(funcionario.getNumeroIdentificacion());
        datePickerFNacimiento.setValue(funcionario.getFechaNacimiento());
        choiceBoxSexo.setValue(funcionario.getSexo());
        choiceBoxEstadoCivil.setValue(funcionario.getEstadoCivil());
        choiceBoxTIdentificacion.setValue(funcionario.getTipoIdentificacion());
        if(funcionario.getFamiliares() != null && !funcionario.getFamiliares().isEmpty()){
            familiares.addAll(funcionario.getFamiliares());
        }
        if(funcionario.getEstudios() != null && !funcionario.getEstudios().isEmpty()){
            estudios.addAll(funcionario.getEstudios());
        }
        this.updateFuncionario = funcionario;
    }
    public boolean saveFuncionario(boolean update){
        if(!validarFormulario()) return false;

        FuncionarioDaoHibernate funcionarioDaoHibernate = new FuncionarioDaoHibernate();

        if(update && updateFuncionario.getId() != null){
            System.out.println("Actualiza a id: "+updateFuncionario.getId());
            updateFuncionario.setNombre(txtNombre.getText());
            updateFuncionario.setApellido(txtApellido.getText());
            updateFuncionario.setTelefono(txtTelefono.getText());
            updateFuncionario.setNumeroIdentificacion(txtNumIdentificacion.getText());
            updateFuncionario.setDireccion(txtDireccion.getText());
            updateFuncionario.setEstadoCivil(choiceBoxEstadoCivil.getValue());
            updateFuncionario.setTipoIdentificacion(choiceBoxTIdentificacion.getValue());
            updateFuncionario.setSexo(choiceBoxSexo.getValue());
            updateFuncionario.setFechaNacimiento(datePickerFNacimiento.getValue());
            updateFuncionario.setFamiliares(familiares);
            updateFuncionario.setEstudios(estudios);
            System.out.println(updateFuncionario);
            return funcionarioDaoHibernate.saveWithFamiliares(updateFuncionario);
        }else {
            Funcionario funcionario = Funcionario.builder()
                    .nombre(txtNombre.getText())
                    .apellido(txtApellido.getText())
                    .telefono(txtTelefono.getText())
                    .numeroIdentificacion(txtNumIdentificacion.getText())
                    .direccion(txtDireccion.getText())
                    .estadoCivil(choiceBoxEstadoCivil.getValue())
                    .tipoIdentificacion(choiceBoxTIdentificacion.getValue())
                    .sexo(choiceBoxSexo.getValue())
                    .fechaNacimiento(datePickerFNacimiento.getValue())
                    .familiares(familiares)
                    .estudios(estudios)
                    .build();
            System.out.println(funcionario);
            return funcionarioDaoHibernate.saveWithFamiliares(funcionario);
        }
    }
    public boolean saveFuncionario(){
        return saveFuncionario(false);
    }

}
