// tag::classShell[]
package car.data;
//end::classShell[]
import java.sql.ResultSet;
import java.sql.SQLException;
//tag::classShell[]

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import car.Features;

@Repository
public class JdbcIngredientRepository
    implements FeatureRepository {

  //tag::jdbcTemplate[]
  private JdbcTemplate jdbc;
  
  //end::jdbcTemplate[]

  @Autowired
  public JdbcIngredientRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }
//end::classShell[]

  //tag::finders[]
  @Override
  public Iterable<Features> findAll() {
    return jdbc.query("select id, name, type from Features",
        this::mapRowToFeature);
  }

  // tag::findOne[]
  @Override
  public Features findById(String id) {
    return jdbc.queryForObject(
        "select id, name, type from Features where id=?",
        this::mapRowToFeature, id);
  }
  
  // end::findOne[]
  
  //end::finders[]

  /*
  //tag::preJava8RowMapper[]
  @Override
  public Ingredient findOne(String id) {
    return jdbc.queryForObject(
        "select id, name, type from Ingredient where id=?",
        new RowMapper<Ingredient>() {
          public Ingredient mapRow(ResultSet rs, int rowNum) 
              throws SQLException {
            return new Ingredient(
                rs.getString("id"), 
                rs.getString("name"),
                Ingredient.Type.valueOf(rs.getString("type")));
          };
        }, id);
  }
  //end::preJava8RowMapper[]
   */
  
  //tag::save[]
  @Override
  public Features save(Features features) {
    jdbc.update(
        "insert into Feature (id, name, type) values (?, ?, ?)",
        features.getId(), 
        features.getName(),
        features.getType().toString());
    return features;
  }
  //end::save[]

  // tag::findOne[]
  //tag::finders[]
  private Features mapRowToFeature(ResultSet rs, int rowNum)
      throws SQLException {
    return new Features(
        rs.getString("id"), 
        rs.getString("name"),
        Features.Type.valueOf(rs.getString("type")));
  }
  //end::finders[]
  // end::findOne[]

  
  /*
//tag::classShell[]

  ...
//end::classShell[]
   */
//tag::classShell[]

}
//end::classShell[]
