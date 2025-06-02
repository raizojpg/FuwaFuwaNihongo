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
        for (Phrase p : dao.getAll()) {
            if (p.getTerm().equals(entity.getTerm())) {
                throw new Exception("Phrase with this term already exists!");
            }
        }
        System.out.println("Inserting Phrase: " + entity.getTerm());
        dao.insert(entity);
        AuditService.getInstance().logAction("PHRASE: create_phrase");
    }

    @Override
    public Phrase read(int id) throws Exception {
        System.out.println("Reading Phrase with ID: " + id);
        Phrase result = dao.getById(id);
        if (result == null) {
            throw new Exception("Phrase with ID " + id + " does not exist!");
        }
        AuditService.getInstance().logAction("PHRASE: read_phrase");
        return result;
    }

    @Override
    public List<Phrase> readAll() throws Exception {
        System.out.println("Reading all Phrases");
        List<Phrase> result = dao.getAll();
        AuditService.getInstance().logAction("PHRASE: read_all_phrases");
        return result;
    }

    @Override
    public void update(Phrase entity, int id) throws Exception {
        System.out.println("Updating Phrase: " + entity.getTerm());
        Phrase result = dao.getById(id);
        if (result == null) {
            throw new Exception("Phrase with ID " + id + " does not exist!");
        }
        dao.update(entity, id);
        AuditService.getInstance().logAction("PHRASE: update_phrase");
    }

    @Override
    public void delete(int id) throws Exception {
        System.out.println("Deleting Phrase with ID: " + id);
        Phrase result = dao.getById(id);
        if (result == null) {
            throw new Exception("Phrase with ID " + id + " does not exist!");
        }
        dao.delete(id);
        AuditService.getInstance().logAction("PHRASE: delete_phrase");
    }

    public int getIdByTerm(String term) throws Exception {
        return dao.getIdByTerm(term);
    }
}