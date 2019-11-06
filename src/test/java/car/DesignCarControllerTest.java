package car;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import car.Features;
import car.Car;
import car.Features.Type;
import car.data.FeatureRepository;
import car.data.OrderRepository;
import car.data.CarRepository;
import car.web.DesignCarController;

@RunWith(SpringRunner.class)
@WebMvcTest(DesignCarController.class)
public class DesignCarControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private List<Features> features;

  private Car design;

  @MockBean
  private FeatureRepository featureRepository;

  @MockBean
  private CarRepository designRepository;

  @MockBean
  private OrderRepository orderRepository;

  @Before
  public void setup() {
    features = Arrays.asList(
    		  new Features("DSL", "Diesel", Type.TYPE	),
    		  new Features("HYB", "Hybrid/Electric", Type.TYPE),
    		  new Features("BMW", "BMW", Type.MAKE),
    		  new Features("CAD", "Cadillac", Type.MAKE),
    		  new Features("BLK", "Black", Type.COLOR),
    		  new Features("SLV", "Silver", Type.COLOR),
    		  new Features("18", "2018", Type.YEAR),
    		  new Features("19", "2019", Type.YEAR)
    );

    when(featureRepository.findAll())
        .thenReturn(features);

    when(featureRepository.findById("DSL")).thenReturn(new Features("DSL", "Diesel", Type.TYPE));
    when(featureRepository.findById("BMW")).thenReturn(new Features("BMW", "BMW", Type.MAKE));
    when(featureRepository.findById("18")).thenReturn(new Features("18", "2018", Type.YEAR));

    design = new Car();
    design.setName("Test Car");

    design.setFeatures(
        Arrays.asList(
            new Features("DSL", "Diesel", Type.TYPE),
            new Features("BMW", "BMW", Type.MAKE),
            new Features("18", "2018", Type.YEAR)));

  }

  @Test
  public void testShowDesignForm() throws Exception {
    mockMvc.perform(get("/design"))
        .andExpect(status().isOk())
        .andExpect(view().name("design"))
        .andExpect(model().attribute("type", features.subList(0, 2)))
        .andExpect(model().attribute("make", features.subList(2, 4)))
        .andExpect(model().attribute("year", features.subList(4, 6)))
        .andExpect(model().attribute("color", features.subList(6, 8)));
  }

  @Test
  public void processDesign() throws Exception {
    when(designRepository.save(design))
        .thenReturn(design);

    mockMvc.perform(post("/design")
        .content("name=Test+Car&features=FLTO,GRBF,CHED")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().stringValues("Location", "/orders/current"));
  }

}
