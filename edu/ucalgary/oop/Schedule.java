package edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Schedule {
  private ArrayList<Hour> hourList = new ArrayList<Hour>();
  private LocalDate DATE;
  private ArrayList<Animal> animals;
  private ArrayList<Task> cleaningTasks = new ArrayList<Task>();
  private ArrayList<Task> feedingTasks = new ArrayList<Task>();
  private ArrayList<Task> treatmentTasks = new ArrayList<Task>();
  private HashMap<String, ArrayList<Task>> tasks = new HashMap<String, ArrayList<Task>>();

  public Schedule(ArrayList<Animal> animals, ArrayList<Task> treatments) throws TimeConflictException, CloneNotSupportedException {

    // Initializing variables
    this.treatmentTasks = treatments;
    this.animals = animals;

    for (int hour = 0; hour < 24; hour++) {
      hourList.set(hour, new Hour(hour));
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
    placeTasks(this.treatmentTasks, false);
    placeTasks(this.feedingTasks, true);
    placeTasks(this.cleaningTasks, false);
  }

  // getters
  public ArrayList<Animal> getAnimals() { return this.animals; }
  public HashMap<String, ArrayList<Task>> getTasks() { return this.tasks; }
  public LocalDate getDate() { return this.DATE; }
  public ArrayList<Hour> getHourList() { return this.hourList; }

  private void placeTasks(ArrayList<Task> tasks, boolean splittable) throws TimeConflictException, CloneNotSupportedException {
    ArrayList<Task> sortedTasks = sortByWindow(tasks);
    for(Task task : sortedTasks) {
      // get info about task
      int startTime = task.getStartTime();
      int maxWindow = task.getMaxWindow();
      int duration = task.getDuration();

      // loop through hours until one with enough room to fit the hour is found
      boolean placed = false;
      int hoursChecked = 0;
      while (!placed && hoursChecked < maxWindow) {
        // get info from currnt hour being considered
        Hour hour = this.hourList.get(startTime + hoursChecked);
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

        // create list of hours within the window
        ArrayList<Hour> window = (ArrayList<Hour>)this.hourList.subList(startTime, startTime + maxWindow);

        // choose most empty hours in window first one at a time, until all animals are fed, or all hours have been checked
        // places tasks in copy first until it is decided they will fit

        Task taskToBeSplit = task;
        boolean canBePlaced = false;
        while (true) {
          // clone window of hours
          ArrayList<Hour> windowCopy = new ArrayList<Hour>();
          for (Hour hour : sortByTimeAvailable(window)) {
            windowCopy.add((Hour)hour.clone());
          }
          
          canBePlaced = splitFeeding(windowCopy, taskToBeSplit, canBePlaced);

          // handle exiting the loop or scheduling backups
          if (canBePlaced) {
            break;
          } else {
            // look for place to schedule backup
            boolean scheduled = false;
            for (Hour hour : window) {
              if (!hour.getBackupBoolean()) {
                hour.setBackupBoolean(true);
                hour.updateTimeAvailable(-60);
                scheduled = true;
                break;
              }
            }
            if (!scheduled) { throw new TimeConflictException(); }
          }
        }
        if(canBePlaced) {
          splitFeeding(window, task, canBePlaced);
        }

      } else if (!placed) {
        // for calling in a backup when there is no room available in the window but the task can not be split
        hoursChecked = 0;
        while (!placed && hoursChecked < maxWindow) {
          // get info from currnt hour being considered
          Hour hour = this.hourList.get(startTime + hoursChecked);
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
        throw new TimeConflictException();
      }
    }
  }

  private boolean splitFeeding(ArrayList<Hour> window, Task taskToBeSplit, boolean willBePlaced) {

    boolean canBePlaced = false;

    // fill hours
    for (Hour hour : sortByTimeAvailable(window)) {
      // get info about hour and the task
      int freeTime = hour.getTimeAvailable();
      String species = ((Feeding)taskToBeSplit).getSpecies();
      int prepTime = AnimalTypes.valueOf(species).getTime()[0];
      int feedingTime = AnimalTypes.valueOf(species).getTime()[1];

      // calculate # of animals that can be fed
      int animalRoom = (freeTime - prepTime) / feedingTime;
      if (animalRoom == 0) { continue; }
      ArrayList<Animal> hungryAnimalsToSplit = ((Feeding)taskToBeSplit).getHungryAnimals();

      // take the first (animalRoom) animals and make a new task for feeding them
      ArrayList<Animal> splitOffAnimals = new ArrayList<Animal>();
      for (Animal animal : (ArrayList<Animal>)hungryAnimalsToSplit.subList(0, animalRoom)) { 
        splitOffAnimals.add(animal); 
      }
      Feeding splitOffTask = new Feeding(species, splitOffAnimals);

      // add new task to hour
      hour.getTasks().add(splitOffTask);
      hour.updateTimeAvailable(splitOffTask.getDuration());

      // update the task to be split and go again, or exit the loop, if there are no more animals to feed
      ArrayList<Animal> leftOverAnimals = new ArrayList<Animal>();
      for (Animal animal : (ArrayList<Animal>)hungryAnimalsToSplit.subList(animalRoom, hungryAnimalsToSplit.size())) { 
        leftOverAnimals.add(animal); 
      }

      if(leftOverAnimals.size() > 0) {
        taskToBeSplit = new Feeding(species, leftOverAnimals);
      } else {
        canBePlaced = true;
        break;
      }

    }

    return canBePlaced;
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

  private ArrayList<Hour> sortByTimeAvailable(ArrayList<Hour> hourList) {
    // copy array
    ArrayList<Hour> hours2 = new ArrayList<Hour>();
    for (Hour hour : hourList) { hours2.add(hour); }

    // create list to store sorted hours
    ArrayList<Hour> sortedHours = new ArrayList<Hour>();

    // move emptiest hour to the sorted array, one at a time
    while (hours2.size() > 0) {

      // find hour with most time available
      int highestTimeI = 0;
      for (int i = 1; i < hours2.size(); i++) {
        if (hours2.get(i).getTimeAvailable() < hours2.get(highestTimeI).getTimeAvailable()) {
          highestTimeI = i;
        }
      }

      // add it to the sorted array and remove it from the copy
      sortedHours.add(hours2.get(highestTimeI));
      hours2.remove(highestTimeI);
    }

    return sortedHours;
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
      if (animal.getSpecies().toLowerCase().equals("coyote")) { coyotes.add(animal); }
      else if (animal.getSpecies().toLowerCase().equals("fox")) { foxes.add(animal); }
      else if (animal.getSpecies().toLowerCase().equals("porcupine")) { porcupines.add(animal); }
      else if (animal.getSpecies().toLowerCase().equals("beaver")) { beavers.add(animal); }
      else if (animal.getSpecies().toLowerCase().equals("raccoon")) { raccoons.add(animal); }
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