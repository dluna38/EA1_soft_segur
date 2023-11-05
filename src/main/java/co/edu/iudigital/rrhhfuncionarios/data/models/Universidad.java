package co.edu.iudigital.rrhhfuncionarios.data.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "universidad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Universidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String nombre;

    public Universidad(String nombre) {
        this.nombre = nombre;
    }

    @PrePersist
    public void prePersist(){
        this.nombre=this.nombre.toUpperCase();
    }

    @Override
    public String toString() {
        return nombre;
    }
}
