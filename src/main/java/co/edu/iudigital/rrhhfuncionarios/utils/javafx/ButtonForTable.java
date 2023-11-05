package co.edu.iudigital.rrhhfuncionarios.utils.javafx;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;


public class ButtonForTable implements ObservableObjectValue<Button> {

    private final Button button;
    public ButtonForTable(String text, EventHandler<ActionEvent> onAction) {
        this.button = new Button(text);
        this.button.setOnAction(onAction);

    }

    public ButtonForTable(String text, EventHandler<ActionEvent> onAction, String[] styles) {
        this.button = new Button(text);
        this.button.setOnAction(onAction);
        button.getStyleClass().setAll(styles);
    }

    @Override
    public Button get() {
        return this.button;
    }

    @Override
    public void addListener(ChangeListener<? super Button> listener) {

    }

    @Override
    public void removeListener(ChangeListener<? super Button> listener) {

    }

    @Override
    public Button getValue() {
        return this.button;
    }

    @Override
    public void addListener(InvalidationListener listener) {

    }

    @Override
    public void removeListener(InvalidationListener listener) {

    }
}
