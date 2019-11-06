package car.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import car.Features;

/**
 * Raw implementation of {@link FeatureRepository} for
 * comparison with {@link JdbcIngredientRepository} to illustrate
 * the power of using {@link JdbcTemplate}. 
 * @author habuma
 */
public class RawJdbcIngredientRepository implements FeatureRepository {

  private DataSource dataSource;

  public RawJdbcIngredientRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }
  
  @Override
  public Iterable<Features> findAll() {
    List<Features> features = new ArrayList<>();
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    try {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(
          "select id, name, type from Features");
      resultSet = statement.executeQuery();
      while(resultSet.next()) {
        Features feature = new Features(
            resultSet.getString("id"),
            resultSet.getString("name"),
            Features.Type.valueOf(resultSet.getString("type")));
        features.add(feature);
      }      
    } catch (SQLException e) {
      // ??? What should be done here ???
    } finally {
      if (resultSet != null) {
        try {
          resultSet.close();
        } catch (SQLException e) {}
      }
      if (statement != null) {
        try {
          statement.close();
        } catch (SQLException e) {}
      }
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {}
      }
    }
    return features;
  }
  
  // tag::rawfindOne[]
  @Override
  public Features findById(String id) {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    try {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(
          "select id, name, type from Features");
      statement.setString(1, id);
      resultSet = statement.executeQuery();
      Features features = null;
      if(resultSet.next()) {
        features = new Features(
            resultSet.getString("id"),
            resultSet.getString("name"),
            Features.Type.valueOf(resultSet.getString("type")));
      } 
      return features;
    } catch (SQLException e) {
      // ??? What should be done here ???
    } finally {
      if (resultSet != null) {
        try {
          resultSet.close();
        } catch (SQLException e) {}
      }
      if (statement != null) {
        try {
          statement.close();
        } catch (SQLException e) {}
      }
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {}
      }
    }
    return null;
  }
  // end::rawfindOne[]
  
  @Override
  public Features save(Features features) {
    // TODO: I only needed one method for comparison purposes, so
    //       I've not bothered implementing this one (yet).
    return null;
  }
  
}
