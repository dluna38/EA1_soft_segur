package co.edu.iudigital.rrhhfuncionarios.data.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "nivel_estudio")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class NivelEstudio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Short id;
    String nivel;

    public NivelEstudio(String nivel) {
        this.nivel = nivel;
    }

    @PrePersist
    public void prePersist(){
        this.nivel=this.nivel.toUpperCase();
    }

    @Override
    public String toString() {
        return nivel;
    }
}
