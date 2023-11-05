package co.edu.iudigital.rrhhfuncionarios.data.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estado_civil")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class EstadoCivil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Byte id;
    @Column(nullable = false)
    private String nombre;

    public EstadoCivil(String nombre) {
        this.nombre = nombre;
    }
}
