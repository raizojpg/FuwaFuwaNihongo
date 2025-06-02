package jdbc.services;

import entities.Reading;
import java.util.List;
import jdbc.dao.ReadingDAO;

public class ReadingService implements CrudService<Reading> {
    private static ReadingService instance;
    private final ReadingDAO dao = new ReadingDAO();

    private ReadingService() {}

    public static ReadingService getInstance() {
        if (instance == null) {
            instance = new ReadingService();
        }
        return instance;
    }

    @Override
    public void create(Reading entity) throws Exception {
        for (Reading r : dao.getAll()) {
            if (r.getOnyomi().toDatabaseString().equals(entity.getOnyomi().toDatabaseString()) &&
                r.getKunyomi().toDatabaseString().equals(entity.getKunyomi().toDatabaseString())) {
                throw new Exception("Reading with these onyomi and kunyomi already exists!");
            }
        }
        System.out.println("Inserting Reading.");
        dao.insertAndGetId(entity);
        AuditService.getInstance().logAction("READING: create_reading");
    }

    @Override
    public Reading read(int id) throws Exception {
        System.out.println("Reading Reading with ID: " + id);
        Reading result = dao.getById(id);
        if (result == null) {
            throw new Exception("Reading with ID " + id + " does not exist!");
        }
        AuditService.getInstance().logAction("READING: read_reading");
        return result;
    }

    @Override
    public List<Reading> readAll() throws Exception {
        System.out.println("Reading all Readings");
        List<Reading> result = dao.getAll();
        AuditService.getInstance().logAction("READING: read_all_readings");
        return result;
    }

    @Override
    public void update(Reading entity, int id) throws Exception {
        System.out.println("Updating Reading with ID: " + id);
        Reading result = dao.getById(id);
        if (result == null) {
            throw new Exception("Reading with ID " + id + " does not exist!");
        }
        dao.update(entity, id);
        AuditService.getInstance().logAction("READING: update_reading");
    }

    @Override
    public void delete(int id) throws Exception {
        System.out.println("Deleting Reading with ID: " + id);
        Reading result = dao.getById(id);
        if (result == null) {
            throw new Exception("Reading with ID " + id + " does not exist!");
        }
        dao.delete(id);
        AuditService.getInstance().logAction("READING: delete_reading");
    }

    public int getIdByOnyomiKunyomi(String onyomi, String kunyomi) throws Exception {
        return dao.getIdByOnyomiKunyomi(onyomi, kunyomi);
    }

}