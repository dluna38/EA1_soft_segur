package co.edu.iudigital.rrhhfuncionarios.data.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.logging.Logger;

@Entity
@Table(name = "tipo_identificacion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TipoIdentificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Byte id;
    @Column(nullable = false)
    private String tipo;
    @Column(nullable = false)
    private String nombreCorto;

    public TipoIdentificacion(String tipo, String nombreCorto) {
        this.tipo = tipo;
        this.nombreCorto = nombreCorto;
    }

    @PrePersist
    private void makeUpper(){
        Logger.getGlobal().info("ejecuto pre-persist");
        this.tipo=this.tipo.toUpperCase();
        this.nombreCorto=this.nombreCorto.toUpperCase();
    }
}
