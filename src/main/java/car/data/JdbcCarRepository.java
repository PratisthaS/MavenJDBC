package car.data;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import car.Features;
import car.Car;

@Repository
public class JdbcCarRepository implements CarRepository {

  private JdbcTemplate jdbc;

  public JdbcCarRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  @Override
  public Car save(Car car) {
    long carId = saveTacoInfo(car);
    car.setId(carId);
    for (Features features : car.getFeatures()) {
    	saveFeaturesToCar(features, carId);
    }

    return car;
  }

  private long saveTacoInfo(Car car) {
    car.setCreatedAt(new Date());
    PreparedStatementCreator psc =
        new PreparedStatementCreatorFactory(
            "insert into Car (name, createdAt) values (?, ?)",
            Types.VARCHAR, Types.TIMESTAMP
        ).newPreparedStatementCreator(
            Arrays.asList(
                car.getName(),
                new Timestamp(car.getCreatedAt().getTime())));

    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbc.update(psc, keyHolder);

    return keyHolder.getKey().longValue();
  }

  private void saveFeaturesToCar(
          Features features, long carId) {
    jdbc.update(
        "insert into Car_Features (car, feature) " +
        "values (?, ?)",
        carId, features.getId());
  }

}
