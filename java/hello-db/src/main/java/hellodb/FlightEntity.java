// Copyright (C) 2019 Dmitry Barashev
package hellodb;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Date;

/**
 * @author dbms@barashev.net
 */
@Entity(name = "flight")
public class FlightEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  Date date;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "planet_id")
  PlanetEntity planet;
}
