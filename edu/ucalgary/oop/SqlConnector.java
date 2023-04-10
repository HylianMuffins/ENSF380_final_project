package edu.ucalgary.oop;

import java.sql.*;
import java.util.ArrayList;

/**
 * The SqlConnector class represents a connection that is formed with the local
 * MySQL server using 'oop'@'localhost'.
 * 
 * @author Zachariah Blair
 * @version 1.3
 * @since 2023-04-03
 */
public class SqlConnector {
  private ArrayList<Animal> animals = new ArrayList<Animal>();
  private ArrayList<Task> treatments = new ArrayList<Task>();

  /**
   * SqlConnector constructor that forms a connection with database and stores
   * the values it reads in its private variables.
   * 
   * @throws SQLConectionException when the database cannot be connected to
   */
  public SqlConnector() throws SQLConectionException {
    try {
      // create connection with oop user.
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection con = DriverManager.getConnection(
          "jdbc:mysql://localhost/EWR",
          "oop",
          "password");

      // write and execute a query that gets all the animal info.
      Statement stmt = con.createStatement();
      ResultSet animalResults = stmt.executeQuery("SELECT * FROM animals");

      // add an animal to the arraylist for each result.
      while (animalResults.next()) {
        this.animals.add(new Animal(animalResults.getString(2),
            animalResults.getInt(1), animalResults.getString(3)));
      }

      // write a query that joins the correct task info to each
      // treatment in treatments table.
      ResultSet treatmentResults = stmt.executeQuery("SELECT * FROM treatments "
          + "INNER JOIN tasks ON tasks.TaskID=treatments.TaskID;");

      // add a treatment to the arraylist for each result
      while (treatmentResults.next()) {
        this.treatments.add(new Treatment(
            treatmentResults.getString(6),
            treatmentResults.getInt(7),
            treatmentResults.getInt(8),
            treatmentResults.getInt(4),
            treatmentResults.getInt(3),
            treatmentResults.getInt(1),
            treatmentResults.getInt(2)));
      }

      // close connection
      con.close();

    } catch (Exception e) {
      e.printStackTrace();
      throw new SQLConectionException();
    }
  }

  /**
   * Getter method returns ArrayList of animals imported from database.
   * 
   * @return animals
   */
  public ArrayList<Animal> getAnimals() {
    return this.animals;
  }

  /**
   * Getter method returns Arraylist of treatments imported from database.
   * 
   * @return treatments
   */
  public ArrayList<Task> getTreatments() {
    return this.treatments;
  }

  /**
   * Used to update the start time of one of the treatments in the database if
   * scheduling is impossible
   * 
   * @param treatmentID ID of treatment being rescheduled.
   * @param startTime   New start time of treatment being rescheduled.
   * @throws SQLConectionException
   */
  public void setStartTime(int treatmentID, int startTime) throws SQLConectionException {
    try {
      // create connection with oop user
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection con = DriverManager.getConnection(
          "jdbc:mysql://localhost/EWR",
          "oop",
          "password");

      // write and execute a statement to update the correct treatment
      Statement stmt = con.createStatement();
      stmt.executeUpdate(String.format("UPDATE TREATMENTS SET " +
          "StartHour = %d WHERE TreatmentID = %d",
          startTime,
          treatmentID));

      // close connection
      con.close();

    } catch (Exception e) {
      e.printStackTrace();
      throw new SQLConectionException();
    }
  }
}
