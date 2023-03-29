package edu.ucalgary.oop;

import java.time.LocalDate;

public class Schedule {
  private Hour[] hourList = new Hour[24];
  private LocalDate DATE;
  private Animal[] animals;
  private Task[] tasks;

  public Schedule(Animal[] animals, Task[] treatments) {}

  public void confirmBackups() {}

  public Animal[] getAnimals() { return this.animals; }
  public Task[] getTasks() { return this.tasks; }
  public LocalDate getDate() { return this.DATE; }
  public Hour[] getHourList() { return this.hourList; }

  private void placeTasks(Task[] tasks) {}
}