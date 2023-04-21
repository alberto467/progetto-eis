package edu.unipd.dei.eis;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import com.google.gson.Gson;

public class GuardianAPI {
  private URIBuilder uriBuilder;
  private HttpClient client = HttpClient.newHttpClient();

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

  public ArrayList<Article> getArticles(GetArticlesOptions options)
      throws InterruptedException, IOException, InvalidParameterException {
    if (options.limit == null || options.limit < 1)
      throw new InvalidParameterException("limit must be positive and not null");

    URIBuilder b = uriBuilder.clone()
        .setPath("search")
        .setParam("show-fields", String.join(",", options.showFields))
        .setParam("page-size", "50");

    ArrayList<Article> out = new ArrayList<>();

    Gson g = new Gson();

    Integer left = options.limit;
    Integer page = 1;
    while (left > 0) {
      if (left < 50)
        b.setParam("page-size", left.toString());
      b.setParam("page", page.toString());

      HttpRequest req = HttpRequest.newBuilder(b.build()).GET().build();
      HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

      SearchResponse sr = g.fromJson(res.body(), SearchResponse.class);
      out.addAll(sr.response.results);

      left -= sr.response.results.size();
      page++;
    }

    return out;
  }
}
