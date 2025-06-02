package jdbc.services;

import entities.Word;
import java.util.List;
import jdbc.dao.WordDAO;

public class WordService implements CrudService<Word> {
    private static WordService instance;
    private final WordDAO dao = new WordDAO();

    private WordService() {}

    public static WordService getInstance() {
        if (instance == null) {
            instance = new WordService();
        }
        return instance;
    }

    @Override
    public void create(Word entity) throws Exception {
        for (Word w : dao.getAll()) {
            if (w.getTerm().equals(entity.getTerm())) {
                throw new Exception("Word with this term already exists!");
            }
        }
        System.out.println("Inserting Word: " + entity.getTerm());
        dao.insert(entity);
        AuditService.getInstance().logAction("WORD: create_word");
    }

    @Override
    public Word read(int id) throws Exception {
        System.out.println("Reading Word with ID: " + id);
        Word result = dao.getById(id);
        if (result == null) {
            throw new Exception("Word with ID " + id + " does not exist!");
        }
        AuditService.getInstance().logAction("WORD: read_word");
        return result;
    }

    @Override
    public List<Word> readAll() throws Exception {
        System.out.println("Reading all Words");
        List<Word> result = dao.getAll();
        AuditService.getInstance().logAction("WORD: read_all_words");
        return result;
    }

    @Override
    public void update(Word entity, int id) throws Exception {
        System.out.println("Updating Word: " + entity.getTerm());
        Word result = dao.getById(id);
        if (result == null) {
            throw new Exception("Word with ID " + id + " does not exist!");
        }
        dao.update(entity, id);
        AuditService.getInstance().logAction("WORD: update_word");
    }

    @Override
    public void delete(int id) throws Exception {
        System.out.println("Deleting Word with ID: " + id);
        Word result = dao.getById(id);
        if (result == null) {
            throw new Exception("Word with ID " + id + " does not exist!");
        }
        dao.delete(id);
        AuditService.getInstance().logAction("WORD: delete_word");
    }

    public int getIdByTerm(String term) throws Exception {
        return dao.getIdByTerm(term);
    }
}