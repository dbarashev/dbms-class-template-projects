// Copyright (C) 2019 Dmitry Barashev
package hellodb;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author dbms@barashev.net
 */
@Entity(name = "planet")
public class PlanetEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  String name;

  BigDecimal distance;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "planet")
  List<FlightEntity> flights;
}
