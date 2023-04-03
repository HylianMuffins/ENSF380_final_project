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

    // Initializing variables
    this.treatmentTasks = treatments;
    this.animals = animals;

    for (int hour = 0; hour < 24; hour++) {
      hourList[hour] = new Hour(hour);
    }

    // creates a list of ids for orphaned animals that don't need to be fed regularly.
    ArrayList<Integer> orphans = new ArrayList<Integer>();

    for (Task treatment : treatments) {
      if(((Treatment)treatment).getTreatmentID() == 1 && !orphans.contains(((Treatment)treatment).getAnimalID())) {
        orphans.add(((Treatment)treatment).getAnimalID());
      }
    }

    // creates tasks to be organized base on number and speceies of animals
    generateTasks(orphans);

    // organizes the tasks into cleaning and feeding
    this.tasks.put("cleaning", this.cleaningTasks);
    this.tasks.put("feeding", this.feedingTasks);
    this.tasks.put("treatment", this.treatmentTasks);

    // places the tasks that have been created, and the treatments from the database in the most time effecient way possible.
    try {
      placeTasks(this.treatmentTasks, false);
      placeTasks(this.feedingTasks, true);
      placeTasks(this.cleaningTasks, false);

    } catch (TimeConflictException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public ArrayList<Integer> listBackups() {
    // TODO: Figure out what this was for
    return new ArrayList<Integer>();
  }

  // getters
  public Animal[] getAnimals() { return this.animals; }
  public HashMap<String, ArrayList<Task>> getTasks() { return this.tasks; }
  public LocalDate getDate() { return this.DATE; }
  public Hour[] getHourList() { return this.hourList; }

  private void placeTasks(ArrayList<Task> tasks, boolean splittable) throws TimeConflictException {
    ArrayList<Task> sortedTasks = sortByWindow(tasks);
    for(Task task : sortedTasks) {
      // get info about treatment
      int startTime = task.getStartTime();
      int maxWindow = task.getMaxWindow();
      int duration = task.getDuration();

      // loop through hours until one with enough room to fit the hour is found
      boolean placed = false;
      int hoursChecked = 0;
      while (!placed && hoursChecked < maxWindow) {
        // get info from currnt hour being considered
        Hour hour = this.hourList[startTime + hoursChecked];
        int timeAvailable = hour.getTimeAvailable();

        // if there is room, place it
        if (timeAvailable - duration >= 0) {
          hour.getTasks().add(task);
          hour.updateTimeAvailable(duration);
          placed = true;
        }

        hoursChecked++;
      }

      // if task is splitable, split it, calling backups when necessary, else, look for backup as usual
      if (!placed && splittable) {
        // TODO: implement splitting feeding tasks and placing them efficiently

      } else if (!placed) {
        // for calling in a backup when there is no room available in the window but the task can not be split
        hoursChecked = 0;
        while (!placed && hoursChecked < maxWindow) {
          // get info from currnt hour being considered
          Hour hour = this.hourList[startTime + hoursChecked];
          int timeAvailable = hour.getTimeAvailable();

          // if there is no backup, and calling one in would help, schedule one and place the task
          if (!hour.getBackupBoolean() && timeAvailable + 60 - duration >= 0) {
            hour.setBackupBoolean(true);
            hour.updateTimeAvailable(-60);
            hour.getTasks().add(task);
            hour.updateTimeAvailable(duration);
            placed = true;
          }

          hoursChecked++;
        }
      }

      // for when there is still no room in the available window and backup volunteers are already scheduled
      if (!placed) {
        // TODO: pass message parameters with this exception
        throw new TimeConflictException();
      }
    }
  }

  private ArrayList<Task> sortByWindow(ArrayList<Task> tasks) {
    // copy the array 
    ArrayList<Task> tasks2 = new ArrayList<Task>();
    for (Task task : tasks) { tasks2.add(task); }

    // move lowest maxWindow task to the sorted array, one at a time
    ArrayList<Task> sortedTasks = new ArrayList<Task>();
    while (tasks2.size() > 0) {

      // find task with smallest window
      int smallestI = 0;
      for (int i = 1; i < tasks2.size(); i++) {
        if (tasks2.get(i).getMaxWindow() < tasks2.get(smallestI).getMaxWindow()) {
          smallestI = i;
        }
      }

      // add it to the sorted array and remove it from the copy
      sortedTasks.add(tasks2.get(smallestI));
      tasks2.remove(smallestI);
    }
    return sortedTasks;
  }

  private void generateTasks(ArrayList<Integer> orphanIDs) {
    // TODO: Refactor this code with loops and the enum

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