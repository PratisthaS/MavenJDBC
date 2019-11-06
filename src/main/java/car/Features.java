package car;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Features {
  
  private final String id;
  private final String name;
  private final Type type;
  
  public static enum Type {
    TYPE, MAKE, YEAR, COLOR
  }

}
