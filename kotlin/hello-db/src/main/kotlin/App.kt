// Copyright (C) 2019 Dmitry Barashev
package hellodb

import spark.Spark.*

class App(dbUrl: String) {
  private val planetListHandler = PlanetListHandler(dbUrl)

  init {
    exception(Exception::class.java) { e, req, res ->
      e.printStackTrace()
    }
    staticFiles.location("/public")
    port(8080)

    get("/hello") { req, res ->
      "Hello DB"
    }
    get("/planets") { req, res ->
      res.header("Content-type", "text/plain;charset=utf-8");
      val planetId: String? = req.queryParams("planet_id");
      planetListHandler.handle(planetId?.toLongOrNull())
    }
  }
}

fun main(args: Array<String>) {
  App("jdbc:postgresql://127.0.0.1:5432/postgres?user=postgres")
}
