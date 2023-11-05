package co.edu.iudigital.rrhhfuncionarios.data.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "funcionarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombres", nullable = false)
    private String nombre;
    @Column(name = "apellidos", nullable = false)
    private String apellido;
    @Column(name = "numero_identificacion", nullable = false)
    @NaturalId
    private String numeroIdentificacion;
    @Column(nullable = false)
    private String direccion;
    @Column(nullable = false)
    private String telefono;
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_estado_civil",foreignKey = @ForeignKey(name = "FK_funcionario_est_civil"))
    private EstadoCivil estadoCivil;
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_tipo_identificacion",foreignKey = @ForeignKey(name = "FK_funcionario_tipo_identificacion"))
    private TipoIdentificacion tipoIdentificacion;
    @Enumerated(EnumType.STRING)
    private Sexo sexo;
    @OneToMany(mappedBy = "familiarDe",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<Familiar> familiares;
    @OneToMany(mappedBy = "perteceA",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<TituloEstudio> estudios;

    public Funcionario(String nombre, String apellido, String numeroIdentificacion, String direccion, String telefono, EstadoCivil estadoCivil, LocalDate fechaNacimiento, TipoIdentificacion tipoIdentificacion, Sexo sexo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.numeroIdentificacion = numeroIdentificacion;
        this.direccion = direccion;
        this.telefono = telefono;
        this.estadoCivil = estadoCivil;
        this.fechaNacimiento = fechaNacimiento;
        this.tipoIdentificacion = tipoIdentificacion;
        this.sexo = sexo;
    }
}
