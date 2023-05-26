package edu.unipd.dei.eis;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.github.slugify.Slugify;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONFileStore<T> {
    private static final Logger logger = LoggerFactory.getLogger(JSONFileStore.class);
    private static final Gson g = new Gson();
    private static final Slugify slug = new Slugify();
    private final Type type;
    private final Path dir;

    private static Path setupDir(File dir) {
        if (dir.exists() && !dir.isDirectory()) {
            throw new IllegalArgumentException("The path " + dir.toPath().toString() +
                "exists and is not a directory");
        }
        dir.mkdirs();
        return dir.toPath();
    }

    public JSONFileStore(File dir, Type type) {
        this.dir = setupDir(dir);
        this.type = type;
    }

    public JSONFileStore(File dir) {
        this(dir, new TypeToken<T>() {}.getClass());
    }

    protected File getFile(String id) {
        String slugId = slug.slugify(id);
        return dir.resolve(slugId + ".json").toFile();
    }

    public void save(String id, T obj) throws Exception {
        File file = getFile(id);

        if (file.exists()) {
            logger.info("File {} of type {} already exists, skipping", file.toPath().toString(),
                type.getTypeName());
            return;
        }

        FileWriter fw = new FileWriter(file);
        g.toJson(obj, type, fw);
        fw.close();
    }

    private T load(File file) throws Exception {
        T obj;
        try {
            FileReader r = new FileReader(file);
            obj = g.fromJson(r, type);
            r.close();
        } catch (Exception e) {
            logger.error("Error loading file " + file.toPath().toString(), e);
            throw e;
        }
        return obj;
    }

    public T load(String id) throws Exception {
        return load(getFile(id));
    }

    public List<T> loadAll() throws Exception {
        return Arrays.stream(dir.toFile().listFiles())
            .filter(file -> file.getName().endsWith(".json")).map(id -> {
                try {
                    return load(id);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
    }

    public void delete(String id) throws Exception {
        File file = getFile(id);
        file.delete();
    }
}
