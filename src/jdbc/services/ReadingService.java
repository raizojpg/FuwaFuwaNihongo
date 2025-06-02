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
        AuditService.getInstance().logAction("READING: create_reading");
        for (Reading r : dao.getAll()) {
            if (r.getOnyomi().toDatabaseString().equals(entity.getOnyomi().toDatabaseString()) &&
                r.getKunyomi().toDatabaseString().equals(entity.getKunyomi().toDatabaseString())) {
                throw new Exception("Reading with these onyomi and kunyomi already exists!");
            }
        }
        System.out.println("Inserting Reading.");
        dao.insertAndGetId(entity);
    }

    @Override
    public Reading read(int id) throws Exception {
        AuditService.getInstance().logAction("READING: read_reading");
        System.out.println("Reading Reading with ID: " + id);
        return dao.getById(id);
    }

    @Override
    public List<Reading> readAll() throws Exception {
        AuditService.getInstance().logAction("READING: read_all_readings");
        System.out.println("Reading all Readings");
        return dao.getAll();
    }

    @Override
    public void update(Reading entity, int id) throws Exception {
        AuditService.getInstance().logAction("READING: update_reading");
        System.out.println("Updating Reading with ID: " + id);
        Reading existing = dao.getById(id);
        if (existing == null) {
            throw new Exception("Reading with ID " + id + " does not exist!");
        }
        dao.update(entity, id);
    }

    @Override
    public void delete(int id) throws Exception {
        AuditService.getInstance().logAction("READING: delete_reading");
        System.out.println("Deleting Reading with ID: " + id);
        Reading existing = dao.getById(id);
        if (existing == null) {
            throw new Exception("Reading with ID " + id + " does not exist!");
        }
        dao.delete(id);
    }

    public int getIdByOnyomiKunyomi(String onyomi, String kunyomi) throws Exception {
        return dao.getIdByOnyomiKunyomi(onyomi, kunyomi);
    }

}