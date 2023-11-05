package co.edu.iudigital.rrhhfuncionarios.data.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_relacion_familiar")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RelacionFamiliar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Byte id;
    @Column(nullable = false)
    private String tipoRelacion;

    public RelacionFamiliar(String tipoRelacion) {
        this.tipoRelacion = tipoRelacion;
    }

    @Override
    public String toString() {
        return tipoRelacion;
    }
}
