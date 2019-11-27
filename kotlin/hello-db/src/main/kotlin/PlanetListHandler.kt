// Copyright (C) 2019 Dmitry Barashev
package hellodb

import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * Этот класс обслуживает запрос на URL /planets.
 * Соединяется с постгресом через JDBC и записывает в результат
 * записи о имеющихся планетах
 *
 * @author dbms@barashev.net
 */
class PlanetListHandler(private val dbUrl: String) {
  fun handle(planetId: Long?): String {
    try {
      DriverManager.getConnection(dbUrl).use { conn ->
        val statement: PreparedStatement
        if (planetId == null) {
          statement = conn.prepareStatement(
              "SELECT id, name, distance FROM Planet")
        } else {
          statement = conn.prepareStatement(
              "SELECT id, name, distance FROM Planet WHERE id = ?")
          statement.setLong(1, planetId)
        }
        val result = mutableListOf<String>()
        statement.executeQuery().use { rs ->
          while (rs.next()) {
            result.add("""
              ${rs.getInt("id")} ${rs.getString("name")} ${rs.getString("distance")}
              """.trimIndent()
            )
          }
        }
        return result.joinToString("\n")
      }
    } catch (e: SQLException) {
      e.printStackTrace()
      throw RuntimeException(e)
    }
  }

}
