package edu.unipd.dei.eis.TermsStore;

import java.util.Map;
import com.google.gson.reflect.TypeToken;
import edu.unipd.dei.eis.JSONFileStore;

public class FileTermsStore extends BaseTermsStore {
    private final JSONFileStore<Map<String, Integer>> store;

    public FileTermsStore(String dir) {
        this.store = new JSONFileStore<>(dir, new TypeToken<Map<String, Integer>>() {}.getType());
    }

    public void save(String id) throws Exception {
        store.save(id, terms);
    }

    public void load(String id) throws Exception {
        terms = store.load(id);
    }
}
