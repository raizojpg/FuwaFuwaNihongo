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
        AuditService.getInstance().logAction("WORD: create_word");
        for (Word w : dao.getAll()) {
            if (w.getTerm().equals(entity.getTerm())) {
                throw new Exception("Word with this term already exists!");
            }
        }
        System.out.println("Inserting Word: " + entity.getTerm());
        dao.insert(entity);
    }

    @Override
    public Word read(int id) throws Exception {
        AuditService.getInstance().logAction("WORD: read_word");
        System.out.println("Reading Word with ID: " + id);
        return dao.getById(id);
    }

    @Override
    public List<Word> readAll() throws Exception {
        AuditService.getInstance().logAction("WORD: read_all_words");
        System.out.println("Reading all Words");
        return dao.getAll();
    }

    @Override
    public void update(Word entity, int id) throws Exception {
        AuditService.getInstance().logAction("WORD: update_word");
        System.out.println("Updating Word: " + entity.getTerm());
        Word existing = dao.getById(id);
        if (existing == null) {
            throw new Exception("Word with ID " + id + " does not exist!");
        }
        dao.update(entity, id);
    }

    @Override
    public void delete(int id) throws Exception {
        AuditService.getInstance().logAction("WORD: delete_word");
        System.out.println("Deleting Word with ID: " + id);
        Word existing = dao.getById(id);
        if (existing == null) {
            throw new Exception("Word with ID " + id + " does not exist!");
        }
        dao.delete(id);
    }

    public int getIdByTerm(String term) throws Exception {
        return dao.getIdByTerm(term);
    }
}