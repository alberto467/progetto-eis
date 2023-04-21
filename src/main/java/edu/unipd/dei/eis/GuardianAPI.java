package edu.unipd.dei.eis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import com.google.gson.Gson;

public class GuardianAPI {
  private URIBuilder uriBuilder;
  private int timeout = 2000;
  private Gson g = new Gson();

  public GuardianAPI(String apiKey) {
    uriBuilder = new URIBuilder(URI.create("http://content.guardianapis.com/"))
        .setParam("api-key", apiKey);
  }

  static private class SearchResponse {
    static private class _response {
      public ArrayList<Article> results;
    }

    public _response response;
  }

  static public class GetArticlesOptions {
    private String[] showFields;
    private Integer limit = 10;

    public GetArticlesOptions setShowFields(String[] showFields) {
      this.showFields = showFields;
      return this;
    }

    public GetArticlesOptions setLimit(Integer limit) {
      this.limit = limit;
      return this;
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

  public ArrayList<Article> getArticles(GetArticlesOptions options)
      throws InterruptedException, IOException, InvalidParameterException {
    if (options.limit == null || options.limit < 1)
      throw new InvalidParameterException("limit must be positive and not null");

    URIBuilder b = uriBuilder.clone().setPath("search")
        .setParam("show-fields", String.join(",", options.showFields))
        .setParam("page-size", "50");

    ArrayList<Article> out = new ArrayList<>();

    Integer left = options.limit;
    Integer page = 1;
    while (left > 0) {
      if (left < 50)
        b.setParam("page-size", left.toString());
      b.setParam("page", page.toString());

      SearchResponse sr = execSearchRequest(b.build().toURL());
      out.addAll(sr.response.results);

      left -= sr.response.results.size();
      page++;
    }

    return out;
  }
}
