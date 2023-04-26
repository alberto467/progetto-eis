package edu.unipd.dei.eis;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class URIBuilder implements Cloneable {
    private URI baseUri;
    private final HashMap<String, String> params;
    private String path = "";

    public URIBuilder(URI baseUri) {
        this.baseUri = baseUri;
        this.params = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public URIBuilder(URIBuilder other) {
        this.baseUri = other.baseUri;
        this.path = other.path;
        this.params = (HashMap<String, String>) other.params.clone();
    }

    public URIBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public URIBuilder setParam(String key, String value) {
        params.put(key, value);
        return this;
    }

    public URI build() {
        StringBuilder sb = new StringBuilder(baseUri.resolve(path).toString());
        sb.append("?");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
        }
        return URI.create(sb.toString());
    }

    public URIBuilder clone() {
        return new URIBuilder(this);
    }
}
