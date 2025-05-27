package jdbc.services;

import java.util.List;

public interface CrudService<T> {
    void create(T entity) throws Exception;
    T read(int id) throws Exception;
    List<T> readAll() throws Exception;
    void update(T entity, int id) throws Exception;
    void delete(int id) throws Exception;
}