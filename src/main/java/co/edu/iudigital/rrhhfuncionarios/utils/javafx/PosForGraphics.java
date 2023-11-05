package co.edu.iudigital.rrhhfuncionarios.utils.javafx;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TableCell;


public class PosForGraphics<S,T> extends TableCell<S,T> {

    public PosForGraphics(Pos posicion) {
        this.setAlignment(posicion);
    }

    @Override
    protected void updateItem(T t, boolean b) {
        super.updateItem(t, b);
        if (b || t == null) {
            setText(null);
            setGraphic(null);
        } else {
            setGraphic((Node) t);
        }
    }
}
