package co.edu.iudigital.rrhhfuncionarios.data.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<I,T> {
    Optional<T> save(T objeto);
    List<T> findAll();
    Optional<T> findById(I id);
    boolean update(T objeto);
    boolean delete(T objeto);
}
