package car.data;

import car.Order;

public interface OrderRepository {

  Order save(Order order);
  
}
