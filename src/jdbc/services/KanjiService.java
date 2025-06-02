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
        AuditService.getInstance().logAction("KANJI: create_kanji");
        for (Kanji k : dao.getAll()) {
            if (k.getTerm().equals(entity.getTerm())) {
                throw new Exception("Kanji with this term already exists!");
            }
        }
        System.out.println("Inserting Kanji: " + entity.getTerm());
        dao.insert(entity);
    }

    @Override
    public Kanji read(int id) throws Exception {
        AuditService.getInstance().logAction("KANJI: read_kanji");
        System.out.println("Reading Kanji with ID: " + id);
        return dao.getById(id);
    }

    @Override
    public List<Kanji> readAll() throws Exception {
        AuditService.getInstance().logAction("KANJI: read_all_kanji");
        System.out.println("Reading all Kanji");
        return dao.getAll();
    }

    @Override
    public void update(Kanji entity, int id) throws Exception {
        AuditService.getInstance().logAction("KANJI: update_kanji");
        System.out.println("Updating Kanji: " + entity.getTerm());
        Kanji existing = dao.getById(id);
        if (existing == null) {
            throw new Exception("Kanji with ID " + id + " does not exist!");
        }
        dao.update(entity, id);
    }

    @Override
    public void delete(int id) throws Exception {
        AuditService.getInstance().logAction("KANJI: delete_kanji");
        System.out.println("Deleting Kanji with ID: " + id);
        Kanji existing = dao.getById(id);
        if (existing == null) {
            throw new Exception("Kanji with ID " + id + " does not exist!");
        }
        int readingId = dao.getReadingIdByKanjiId(id);
        dao.delete(id);
        if (readingId > 0) {
            readingDAO.delete(readingId);
            System.out.println("Deleted associated Reading with ID: " + readingId);
        }
        Reading r = readingDAO.getById(readingId);
        System.out.println("Reading after delete: " + r); 
    }

    public int getIdByTerm(String term) throws Exception {
        return dao.getIdByTerm(term);
    }
}