package car.web;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import car.Features;
import car.Order;
import car.Car;
import car.Features.Type;
import car.data.FeatureRepository;
import car.data.CarRepository;

// tag::classShell[]
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignCarController {
  
//end::classShell[]

//tag::bothRepoProperties[]
//tag::ingredientRepoProperty[]
  private final FeatureRepository featureRepo;
  
//end::ingredientRepoProperty[]
  private CarRepository designRepo;

//end::bothRepoProperties[]
  
  /*
// tag::ingredientRepoOnlyCtor[]
  @Autowired
  public DesignTacoController(IngredientRepository ingredientRepo) {
    this.ingredientRepo = ingredientRepo;
  }
// end::ingredientRepoOnlyCtor[]
   */

  //tag::bothRepoCtor[]
  @Autowired
  public DesignCarController(
        FeatureRepository featureRepo, 
        CarRepository designRepo) {
    this.featureRepo = featureRepo;
    this.designRepo = designRepo;
  }

  //end::bothRepoCtor[]
  
  // tag::modelAttributes[]
  @ModelAttribute(name = "order")
  public Order order() {
    return new Order();
  }
  
  @ModelAttribute(name = "car")
  public Car car() {
    return new Car();
  }

  // end::modelAttributes[]
  // tag::showDesignForm[]
  
  @GetMapping
  public String showDesignForm(Model model) {
    List<Features> features = new ArrayList<>();
    featureRepo.findAll().forEach(i -> features.add(i));
    
    Type[] types = Features.Type.values();
    for (Type type : types) {
      model.addAttribute(type.toString().toLowerCase(), 
          filterByType(features, type));      
    }

    return "design";
  }
//end::showDesignForm[]

  //tag::processDesign[]
  @PostMapping
  public String processDesign(
      @Valid Car design, Errors errors, 
      @ModelAttribute Order order) {

    if (errors.hasErrors()) {
      return "design";
    }

    Car saved = designRepo.save(design);
    order.addDesign(saved);

    return "redirect:/orders/current";
  }
  //end::processDesign[]
  
  private List<Features> filterByType(
      List<Features> features, Type type) {
    return features
              .stream()
              .filter(x -> x.getType().equals(type))
              .collect(Collectors.toList());
  }

  /*
//tag::classShell[]

  ...

//end::classShell[]
   */
//tag::classShell[]

}
//end::classShell[]
