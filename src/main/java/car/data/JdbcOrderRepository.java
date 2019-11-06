// tag::core[]
package car.data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import car.Order;
import car.Car;

@Repository
public class JdbcOrderRepository implements OrderRepository {

  private SimpleJdbcInsert orderInserter;
  private SimpleJdbcInsert orderCarInserter;
  private ObjectMapper objectMapper;

  @Autowired
  public JdbcOrderRepository(JdbcTemplate jdbc) {
    this.orderInserter = new SimpleJdbcInsert(jdbc)
        .withTableName("Car_Order")
        .usingGeneratedKeyColumns("id");

    this.orderCarInserter = new SimpleJdbcInsert(jdbc)
        .withTableName("Car_Order_Cars");

    this.objectMapper = new ObjectMapper();
  }
// end::core[]

// tag::save[]
  @Override
  public Order save(Order order) {
    order.setPlacedAt(new Date());
    long orderId = saveOrderDetails(order);
    order.setId(orderId);
    List<Car> cars = order.getCars();
    for (Car car : cars) {
      saveCarToOrder(car, orderId);
    }

    return order;
  }

  private long saveOrderDetails(Order order) {
    @SuppressWarnings("unchecked")
    Map<String, Object> values =
        objectMapper.convertValue(order, Map.class);
    values.put("placedAt", order.getPlacedAt());

    long orderId =
        orderInserter
            .executeAndReturnKey(values)
            .longValue();
    return orderId;
  }

  private void saveCarToOrder(Car car, long orderId) {
    Map<String, Object> values = new HashMap<>();
    values.put("carOrder", orderId);
    values.put("car", car.getId());
    orderCarInserter.execute(values);
  }
// end::save[]

/*
// tag::core[]

...

// end::core[]
 */

// tag::core[]
}
// end::core[]
