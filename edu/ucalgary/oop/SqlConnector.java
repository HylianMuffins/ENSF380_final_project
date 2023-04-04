package edu.ucalgary.oop;

import java.sql.*;
import java.util.ArrayList;

public class SqlConnector {
  private ArrayList<Animal> animals = new ArrayList<Animal>();
  private ArrayList<Task> treatments = new ArrayList<Task>();

  public SqlConnector() {
    try{
      // create connection with oop user
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection con=DriverManager.getConnection("jdbc:mysql://localhost/EWR","oop","password");

      // write and execute a query that gets all the animal info
      Statement stmt=con.createStatement();
      ResultSet animalResults = stmt.executeQuery("SELECT * FROM animals");

      // add an animal to the arraylist for each result
      while(animalResults.next()) {
        this.animals.add( new Animal(animalResults.getString(2),
          animalResults.getInt(1), animalResults.getString(3)));
      }

      // write a query that joins the correct task info to each treatment in treatments table
      ResultSet treatmentResults = stmt.executeQuery("SELECT * FROM treatments INNER JOIN tasks ON tasks.TaskID=treatments.TaskID;");

      // add a treatment to the arraylist for each result
      while(treatmentResults.next()) {
        this.treatments.add( new Treatment(treatmentResults.getString(6), treatmentResults.getInt(7),
          treatmentResults.getInt(8), treatmentResults.getInt(4), treatmentResults.getInt(1),
          treatmentResults.getInt(2)));
      }

      // close connection
      con.close();
      
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public ArrayList<Animal> getAnimals() {
    return this.animals;
  }

  public ArrayList<Task> getTreatments() {
    return this.treatments;
  }
}
