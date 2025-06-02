package jdbc.services;

import entities.Phrase;
import java.util.List;
import jdbc.dao.PhraseDAO;

public class PhraseService implements CrudService<Phrase> {
    private static PhraseService instance;
    private final PhraseDAO dao = new PhraseDAO();

    private PhraseService() {}

    public static PhraseService getInstance() {
        if (instance == null) {
            instance = new PhraseService();
        }
        return instance;
    }

    @Override
    public void create(Phrase entity) throws Exception {
        AuditService.getInstance().logAction("PHRASE: create_phrase");
        for (Phrase p : dao.getAll()) {
            if (p.getTerm().equals(entity.getTerm())) {
                throw new Exception("Phrase with this term already exists!");
            }
        }
        System.out.println("Inserting Phrase: " + entity.getTerm());
        dao.insert(entity);
    }

    @Override
    public Phrase read(int id) throws Exception {
        AuditService.getInstance().logAction("PHRASE: read_phrase");
        System.out.println("Reading Phrase with ID: " + id);
        return dao.getById(id);
    }

    @Override
    public List<Phrase> readAll() throws Exception {
        AuditService.getInstance().logAction("PHRASE: read_all_phrases");
        System.out.println("Reading all Phrases");
        return dao.getAll();
    }

    @Override
    public void update(Phrase entity, int id) throws Exception {
        AuditService.getInstance().logAction("PHRASE: update_phrase");
        System.out.println("Updating Phrase: " + entity.getTerm());
        Phrase existing = dao.getById(id);
        if (existing == null) {
            throw new Exception("Phrase with ID " + id + " does not exist!");
        }
        dao.update(entity, id);
    }

    @Override
    public void delete(int id) throws Exception {
        AuditService.getInstance().logAction("PHRASE: delete_phrase");
        System.out.println("Deleting Phrase with ID: " + id);
        Phrase existing = dao.getById(id);
        if (existing == null) {
            throw new Exception("Phrase with ID " + id + " does not exist!");
        }
        dao.delete(id);
    }

    public int getIdByTerm(String term) throws Exception {
        return dao.getIdByTerm(term);
    }
}