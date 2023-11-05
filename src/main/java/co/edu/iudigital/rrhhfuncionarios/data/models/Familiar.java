package co.edu.iudigital.rrhhfuncionarios.data.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Familiares")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Familiar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellido;

    private String telefono;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "familiar_de",foreignKey = @ForeignKey(name = "FK_Familiares_Funcionarios"))
    private Funcionario familiarDe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_tipo_relacion",foreignKey = @ForeignKey(name = "FK_Familiares_tipo_relacion_familiar"))
    private RelacionFamiliar tipoRelacion;

    public Familiar(String nombre, String apellido, String telefono, Funcionario familiarDe, RelacionFamiliar tipoRelacion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.familiarDe = familiarDe;
        this.tipoRelacion = tipoRelacion;
    }
}
