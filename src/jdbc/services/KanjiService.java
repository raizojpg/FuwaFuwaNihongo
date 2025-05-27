package jdbc.services;

import entities.Kanji;
import entities.Reading;
import jdbc.dao.KanjiDAO;
import jdbc.dao.ReadingDAO;
import java.util.List;

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
    }

    @Override
    public Kanji read(int id) throws Exception {
        System.out.println("Reading Kanji with ID: " + id);
        return dao.getById(id);
    }

    @Override
    public List<Kanji> readAll() throws Exception {
        System.out.println("Reading all Kanji");
        return dao.getAll();
    }

    @Override
    public void update(Kanji entity, int id) throws Exception {
        System.out.println("Updating Kanji: " + entity.getTerm());
        Kanji existing = dao.getById(id);
        if (existing == null) {
            throw new Exception("Kanji with ID " + id + " does not exist!");
        }
        dao.update(entity, id);
    }

    @Override
    public void delete(int id) throws Exception {
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