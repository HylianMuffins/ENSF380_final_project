package edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Schedule {
  private Hour[] hourList = new Hour[24];
  private LocalDate DATE;
  private Animal[] animals;
  private ArrayList<Task> cleaningTasks = new ArrayList<Task>();
  private ArrayList<Task> feedingTasks = new ArrayList<Task>();
  private ArrayList<Task> treatmentTasks = new ArrayList<Task>();
  private HashMap<String, ArrayList<Task>> tasks = new HashMap<String, ArrayList<Task>>();

  public Schedule(Animal[] animals, ArrayList<Task> treatments) {

    this.treatmentTasks = treatments;
    this.animals = animals;

    // creates a list of ids for orphaned animals that don't need to be fed regularly.
    ArrayList<Integer> orphans = new ArrayList<Integer>();

    for (Task treatment : treatments) {
      if(treatment.getTreatmentID() == 1 && !orphans.contains(treatment.getAnimalID())) {
        orphans.add(treatment.animalID);
      }
    }

    // creates tasks to be organized base on number and speceies of animals
    generateTasks(orphans);

    // organizes the tasks into cleaning and feeding
    this.tasks.put("cleaning", this.cleaningTasks);
    this.tasks.put("feeding", this.feedingTasks);
    this.tasks.put("treatment", this.treatmentTasks);

    // places the tasks that have been created, and the treatments from the database in the most time effecient way possible.
    placeTasks();
  }

  public ArrayList<Integer> listBackups() {
    return new ArrayList<Integer>();
  }

  public Animal[] getAnimals() { return this.animals; }
  public HashMap<String, ArrayList<Task>> getTasks() { return this.tasks; }
  public LocalDate getDate() { return this.DATE; }
  public Hour[] getHourList() { return this.hourList; }

  private void placeTasks() {
    // TODO: make this
  }

  private void generateTasks(ArrayList<Integer> orphanIDs) {

    // initiate arraylists to organise species
    ArrayList<Animal> coyotes = new ArrayList<Animal>();
    ArrayList<Animal> foxes = new ArrayList<Animal>();
    ArrayList<Animal> porcupines = new ArrayList<Animal>();
    ArrayList<Animal> beavers = new ArrayList<Animal>();
    ArrayList<Animal> raccoons = new ArrayList<Animal>();

    for (Animal animal : this.animals) {
      if (animal.getSpecies().toLowerCase() == "coyote") { coyotes.add(animal); }
      else if (animal.getSpecies().toLowerCase() == "fox") { foxes.add(animal); }
      else if (animal.getSpecies().toLowerCase() == "porcupine") { porcupines.add(animal); }
      else if (animal.getSpecies().toLowerCase() == "beaver") { beavers.add(animal); }
      else if (animal.getSpecies().toLowerCase() == "raccoon") { raccoons.add(animal); }
    }

    // Make lists of animals to be fed
    ArrayList<Animal> hungryCoyotes = new ArrayList<Animal>();
    for (Animal coyote : coyotes) {
      if (!orphanIDs.contains(coyote.getAnimalID())) { hungryCoyotes.add(coyote); }
    }
    ArrayList<Animal> hungryFoxes = new ArrayList<Animal>();
    for (Animal fox : foxes) {
      if (!orphanIDs.contains(fox.getAnimalID())) { hungryFoxes.add(fox); }
    }
    ArrayList<Animal> hungryPorcupines = new ArrayList<Animal>();
    for (Animal porcupine : porcupines) {
      if (!orphanIDs.contains(porcupine.getAnimalID())) { hungryPorcupines.add(porcupine); }
    }
    ArrayList<Animal> hungryBeavers = new ArrayList<Animal>();
    for (Animal beaver : beavers) {
      if (!orphanIDs.contains(beaver.getAnimalID())) { hungryBeavers.add(beaver); }
    }
    ArrayList<Animal> hungryRaccoons = new ArrayList<Animal>();
    for (Animal raccoon : raccoons) {
      if (!orphanIDs.contains(raccoon.getAnimalID())) { hungryRaccoons.add(raccoon); }
    }

    // create feeding tasks
    this.feedingTasks.add(new Feeding("coyote", hungryCoyotes));
    this.feedingTasks.add(new Feeding("fox", hungryFoxes));
    this.feedingTasks.add(new Feeding("porcupine", hungryPorcupines));
    this.feedingTasks.add(new Feeding("beaver", hungryBeavers));
    this.feedingTasks.add(new Feeding("raccoon", hungryRaccoons));

    // create cleaning tasks
    for (Animal animal : this.animals) {
      this.cleaningTasks.add(new Cleaning(animal.getNickname(), animal.getSpecies()));
    }
  }
}