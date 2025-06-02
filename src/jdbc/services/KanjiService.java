package jdbc.services;

import entities.Kanji;
import entities.Reading;
import java.util.List;
import jdbc.dao.KanjiDAO;
import jdbc.dao.ReadingDAO;

public class KanjiService implements CrudService<Kanji> {
    private static KanjiService instance;
    private final KanjiDAO dao = new KanjiDAO();
    private final ReadingDAO readingDAO = new ReadingDAO();

    private KanjiService() {}

    public static KanjiService getInstance() {
        if (instance == null) {
            instance = new KanjiService();
        }
        return instance;
    }

    @Override
    public void create(Kanji entity) throws Exception {
        for (Kanji k : dao.getAll()) {
            if (k.getTerm().equals(entity.getTerm())) {
                throw new Exception("Kanji with this term already exists!");
            }
        }
        System.out.println("Inserting Kanji: " + entity.getTerm());
        dao.insert(entity);
        AuditService.getInstance().logAction("READING: create_reading");
        AuditService.getInstance().logAction("KANJI: create_kanji");
    }

    @Override
    public Kanji read(int id) throws Exception {
        System.out.println("Reading Kanji with ID: " + id);
        Kanji result = dao.getById(id);
        if (result == null) {
            throw new Exception("Kanji with ID " + id + " does not exist!");
        }
        AuditService.getInstance().logAction("READING: read_reading");
        AuditService.getInstance().logAction("KANJI: read_kanji");
        return result;
    }

    @Override
    public List<Kanji> readAll() throws Exception {
        System.out.println("Reading all Kanji");
        List<Kanji> result = dao.getAll();
        AuditService.getInstance().logAction("READING: read_all_reading");
        AuditService.getInstance().logAction("KANJI: read_all_kanji");
        return result;
    }

    @Override
    public void update(Kanji entity, int id) throws Exception {
        System.out.println("Updating Kanji: " + entity.getTerm());
        Kanji result = dao.getById(id);
        if (result == null) {
            throw new Exception("Kanji with ID " + id + " does not exist!");
        }
        dao.update(entity, id);
        AuditService.getInstance().logAction("READING: update_reading");
        AuditService.getInstance().logAction("KANJI: update_kanji");
    }

    @Override
    public void delete(int id) throws Exception {
        System.out.println("Deleting Kanji with ID: " + id);
        Kanji result = dao.getById(id);
        if (result == null) {
            throw new Exception("Kanji with ID " + id + " does not exist!");
        }
        int readingId = dao.getReadingIdByKanjiId(id);
        dao.delete(id);
        if (readingId > 0) {
            readingDAO.delete(readingId);
            System.out.println("Deleted associated Reading with ID: " + readingId);
        }
        Reading r = readingDAO.getById(readingId);
        AuditService.getInstance().logAction("READING: delete_reading");
        AuditService.getInstance().logAction("KANJI: delete_kanji");
    }
    
    public int getIdByTerm(String term) throws Exception {
        return dao.getIdByTerm(term);
    }
}