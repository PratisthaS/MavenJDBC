package car.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import car.Features;
import car.data.FeatureRepository;

@Component
public class FeatureByIdConverter implements Converter<String, Features> {

  private FeatureRepository featureRepo;

  @Autowired
  public FeatureByIdConverter(FeatureRepository featureRepo) {
    this.featureRepo = featureRepo;
  }
  
  @Override
  public Features convert(String id) {
    return featureRepo.findById(id);
  }

}
