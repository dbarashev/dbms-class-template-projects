// Copyright (C) 2019 Dmitry Barashev
package hellodb;

import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.staticFiles;

/**
 * Это main класс приложения. Запускает Spark и вешает два обработчика запросов.
 *
 * @author dbms@barashev.net
 */
public class App {
  private final PlanetListHandler planetListHandler;

  App(String dbUrl) {
    this.planetListHandler = new PlanetListHandler(dbUrl);
    exception(Exception.class, (e, req, res) -> e.printStackTrace());
    staticFiles.location("/public");
    port(8080);

    get("/hello", (req, res) -> {
      return "Hello DB";
    });
    get("/planets", (req, res) -> {
      res.header("Content-type", "text/plain;charset=utf-8");
      String planetId = req.queryParams("planet_id");
      return planetListHandler.handle(planetId  == null ? null : Long.parseLong(planetId));
    });
  }

  public static void main(String[] args) {
    new App("jdbc:postgresql://127.0.0.1:5432/postgres?user=postgres");
  }
}
