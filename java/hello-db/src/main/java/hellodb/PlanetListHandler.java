// Copyright (C) 2019 Dmitry Barashev
package hellodb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Этот класс обслуживает запрос на URL /planets.
 * Соединяется с постгресом через JDBC и записывает в результат
 * записи о имеющихся планетах
 *
 * @author dbms@barashev.net
 */
class PlanetListHandler {
  private final String dbUrl;

  public PlanetListHandler(String dbUrl) {
    this.dbUrl = dbUrl;
  }
  String handle(Long planetId) {
    try (Connection conn = DriverManager.getConnection(dbUrl)) {
      PreparedStatement statement;
      if (planetId == null) {
        statement = conn.prepareStatement(
            "SELECT id, name, distance FROM Planet");
      } else {
        statement = conn.prepareStatement(
            "SELECT id, name, distance FROM Planet WHERE id = ?");
        statement.setLong(1, planetId);
      }
      List<String> result = new ArrayList<>();
      try (ResultSet rs = statement.executeQuery()) {
        while (rs.next()) {
          result.add(String.format("\n%d %s %s",
              rs.getInt("id"),
              rs.getString("name"),
              rs.getString("distance")
          ));
        }
      }
      return result.toString();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
