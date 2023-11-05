package co.edu.iudigital.rrhhfuncionarios.data.models;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "titulo_estudio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class TituloEstudio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String titulo;
    @ManyToOne
    Universidad universidad;
    @ManyToOne
    NivelEstudio nivelEstudio;

    @ManyToOne(fetch = FetchType.LAZY)
    Funcionario perteceA;



}
