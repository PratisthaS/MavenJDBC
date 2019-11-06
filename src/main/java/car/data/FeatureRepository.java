package car.data;

import car.Features;

public interface FeatureRepository {

  Iterable<Features> findAll();
  
  Features findById(String id);
  
  Features save(Features features);
  
}
