package edu.unipd.dei.eis.ArticleSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import com.google.gson.Gson;
import edu.unipd.dei.eis.Article;
import edu.unipd.dei.eis.URIBuilder;

/**
 * Classe che si occupa di fornire gli articoli tramite l'API del Guardian
 */
public class GuardianAPI {
    private URIBuilder uriBuilder;
    private int timeout = 2000;
    private Gson g = new Gson();

    /**
     * Costruttore
     * 
     * @param apiKey La chiave per l'API del Guardian
     */
    public GuardianAPI(String apiKey) {
        uriBuilder = new URIBuilder(URI.create("http://content.guardianapis.com/"))
            .setParam("api-key", apiKey);
    }

    static private class SearchResponse {
        static private class _response {
            static private class _article {
                public String id;
                public String webTitle;

                static private class Fields {
                    public String bodyText;
                }

                public Fields fields;

                public Article toArticle() {
                    return new Article(id, webTitle, fields.bodyText);
                }
            }

            public ArrayList<_article> results;
        }

        public _response response;
    }

    static public class SearchQuery {
        public String query;
        public String[] showFields;
        public Integer limit;

        public SearchQuery(String query, String[] showFields, Integer limit) {
            this.query = query;
            this.showFields = showFields;
            this.limit = limit;
        }
    }

    static public class SearchQueryBuilder {
        private String query;
        private String[] showFields = {};
        private Integer limit = 50;

        /**
         * Imposta la query
         */
        public SearchQueryBuilder setQuery(String query) {
            this.query = query;
            return this;
        }

        /**
         * Imposta i campi da ottenere
         */
        public SearchQueryBuilder setShowFields(String... showFields) {
            this.showFields = showFields;
            return this;
        }

        /**
         * Imposta il numero massimo di articoli da ottenere
         */
        public SearchQueryBuilder setLimit(Integer limit) {
            this.limit = limit;
            return this;
        }

        /**
         * Costruisce la query
         */
        public SearchQuery build() {
            if (query == null)
                throw new IllegalArgumentException("query must be set");

            return new SearchQuery(query, showFields, limit);
        }
    }

    private SearchResponse execSearchRequest(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);

        int status = conn.getResponseCode();
        if (status >= 300) {
            BufferedReader r = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            StringBuffer errorMessage = new StringBuffer();
            String line;

            while ((line = r.readLine()) != null)
                errorMessage.append(line);

            r.close();
            conn.disconnect();

            throw new IOException(
                "HTTP error: status = " + status + ", data = " + errorMessage.toString());
        }

        InputStreamReader reader = new InputStreamReader(conn.getInputStream());
        SearchResponse res = g.fromJson(reader, SearchResponse.class);
        conn.disconnect();

        return res;
    }

    /**
     * Ottiene un numero di articoli gestendo la paginazione
     * 
     * @param query Le opzioni per la richiesta
     * 
     * @return La lista di articoli
     */
    public ArrayList<Article> search(SearchQuery query) throws Exception {
        if (query.limit == null || query.limit < 1)
            throw new IllegalArgumentException("limit must be positive and not null");

        URIBuilder b = uriBuilder.clone().setPath("search")
            .setParam("show-fields", String.join(",", query.showFields))
            .setParam("page-size", "50");

        ArrayList<Article> out = new ArrayList<>(query.limit);

        Integer left = query.limit;
        Integer page = 1;
        while (left > 0) {
            if (left < 50)
                b.setParam("page-size", left.toString());
            b.setParam("page", page.toString());

            SearchResponse sr = execSearchRequest(b.build().toURL());
            for (SearchResponse._response._article a : sr.response.results)
                out.add(a.toArticle());

            left -= sr.response.results.size();
            page++;
        }

        return out;
    }
}
