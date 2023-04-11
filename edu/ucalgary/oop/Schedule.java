package edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main Scheduling class that creates tasks, organizes them in the most efficient way,
 * and stores them in a list of hours. Also handles deciding when to ask for
 * backups.
 * 
 * @author Zachariah Blair
 * @version 1.6
 * @since 2023-04-01
 * Zachariah Blair, Ken Liu, Sudarshan Naicker, Rutvi Brahmbhatt
 */
public class Schedule {
  private ArrayList<Hour> hourList = new ArrayList<Hour>();
  private final LocalDate DATE;
  private ArrayList<Animal> animals;
  private ArrayList<Task> cleaningTasks = new ArrayList<Task>();
  private ArrayList<Task> feedingTasks = new ArrayList<Task>();
  private ArrayList<Task> treatmentTasks = new ArrayList<Task>();
  private HashMap<String, ArrayList<Task>> tasks = new HashMap<String, ArrayList<Task>>();

  /**
   * Schedule Constructor; first creates an ArrayList of hours, then creates
   * ArrayLists for the different types of tasks, basing the type and number off
   * of the animals from the database. Finally, it schedules tasks in order of
   * importance and then in order of time flexibility.
   * 
   * @param animals    ArrayList of Animal objects imported from database.
   * @param treatments ArrayList of Treatment objects from database.
   * @throws TimeConflictException      when it is impossible to create the
   *                                    schedule
   * @throws CloneNotSupportedException when Hour is not clonable
   */
  public Schedule(ArrayList<Animal> animals, ArrayList<Task> treatments)
      throws TimeConflictException, CloneNotSupportedException {

    // Initializing variables
    this.treatmentTasks = treatments;
    this.animals = animals;
    this.DATE = LocalDate.now().plusDays(1);

    for (int hour = 0; hour < 24; hour++) {
      hourList.add(new Hour(hour));
    }

    // creates a list of ids for orphaned animals that don't need to be
    // fed regularly.
    ArrayList<Integer> orphans = new ArrayList<Integer>();

    for (Task treatment : treatments) {
      if (((Treatment)treatment).getTaskID() == 1
          && !orphans.contains(((Treatment) treatment).getAnimalID())) {
        orphans.add(((Treatment) treatment).getAnimalID());
      }
    }

    // creates tasks to be organized base on number and speceies of animals
    generateTasks(orphans);

    // organizes the tasks into cleaning and feeding
    this.tasks.put("cleaning", this.cleaningTasks);
    this.tasks.put("feeding", this.feedingTasks);
    this.tasks.put("treatment", this.treatmentTasks);

    // places the tasks that have been created, and the treatments from the
    // database in the most time effecient way possible.
    placeTasks(this.treatmentTasks, false);
    placeTasks(this.feedingTasks, true);
    placeTasks(this.cleaningTasks, false);
  }

  /**
   * Getter method; returns ArrayList of animals that are part of the schedule.
   * 
   * @return animals
   */
  public ArrayList<Animal> getAnimals() {
    return this.animals;
  }

  /**
   * Getter method; returns HashMap that links Task type to an ArrayList of all
   * the tasks of that type in the schedule.
   * 
   * @return tasks
   */
  public HashMap<String, ArrayList<Task>> getTasks() {
    return this.tasks;
  }

  /**
   * Getter method; returns the LocalDate representing the day the schedule was
   * made for (i.e. the day after the schedule was made).
   * 
   * @return DATE
   */
  public LocalDate getDate() {
    return this.DATE;
  }

  /**
   * Getter method; returns the ArrayList representing the entire schedule, as
   * a list of 24 Hour objects with tasks assigned during each hour, and a
   * boolean denoting the requirement of a backup volunteer.
   * 
   * @return hourList
   */
  public ArrayList<Hour> getHourList() {
    return this.hourList;
  }

  /**
   * Calculates the best way to organize tasks, and then places them in the
   * schedule based off of that plan, scheduling backups where needed (Also
   * handles splitting the feeding tasks up into smaller tasks when needed).
   * Throws TimeConflictException when it is impossible to generate a schedule
   * based on the animals and treatments given.
   * 
   * @param tasks      ArrayList of Task objects to be scheduled
   * @param splittable boolean; True if tasks are of type Feeding, else false.
   * @throws TimeConflictException
   * @throws CloneNotSupportedException
   */
  private void placeTasks(ArrayList<Task> tasks, boolean splittable)
      throws TimeConflictException, CloneNotSupportedException {

    ArrayList<Task> sortedTasks = sortByWindow(tasks);
    for (Task task : sortedTasks) {
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

      // if task is splitable, split it, calling backups when necessary, else,
      // look for backup as usual
      if (!placed && splittable) {

        // create list of hours within the window
        ArrayList<Hour> window = new ArrayList<Hour>(this.hourList.subList(startTime, startTime + maxWindow));

        // choose most empty hours in window first one at a time, until all
        // animals are fed, or all hours have been checked places tasks in copy
        // first until it is decided they will fit

        Task taskToBeSplit = task;
        boolean canBePlaced = false;
        while (true) {
          // clone window of hours
          ArrayList<Hour> windowCopy = new ArrayList<Hour>();
          for (Hour hour : sortByTimeAvailable(window)) {
            windowCopy.add((Hour) hour.clone());
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
            if (!scheduled) {
              throw new TimeConflictException();
            }
          }
        }
        if (canBePlaced) {
          splitFeeding(window, task, canBePlaced);
          placed = true;
        }

      } else if (!placed) {
        // for calling in a backup when there is no room available in the window
        // but the task can not be split
        hoursChecked = 0;
        while (!placed && hoursChecked < maxWindow) {
          // get info from currnt hour being considered
          Hour hour = this.hourList.get(startTime + hoursChecked);
          int timeAvailable = hour.getTimeAvailable();

          // if there is no backup, and calling one in would help, schedule one
          // and place the task
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

      // for when there is still no room in the available window and backup
      // volunteers are already scheduled
      if (!placed) {
        throw new TimeConflictException(task);
      }
    }
  }

  /**
   * Helper method used to split the feeding tasks into smaller parts
   * 
   * @param window        ArrayList of hours representing the hours the animals
   *                      can be fed
   * @param taskToBeSplit The Task object that must be split into smaller parts
   * @param willBePlaced  Boolean that determines, if the splitting is just being
   *                      tested to see how many backups are needed, or it has
   *                      already been
   *                      confirmed, and is now being placed.
   * @return canBePlaced: a boolean that is used to confirm if the current split
   *         testing was successful. Only used if willBePlaced is false (i.e.
   *         during the
   *         splitting testing phase). Return value goes unused if the task is
   *         actually
   *         being split and scheduled.
   */
  private boolean splitFeeding(
      ArrayList<Hour> window,
      Task taskToBeSplit,
      boolean willBePlaced) {

    boolean canBePlaced = false;

    // fill hours
    for (Hour hour : sortByTimeAvailable(window)) {
      // get info about hour and the task
      int freeTime = hour.getTimeAvailable();
      String species = ((Feeding) taskToBeSplit).getSpecies();
      int prepTime = AnimalTypes.valueOf(species.toUpperCase()).getTime()[0];
      int feedingTime = AnimalTypes.valueOf(species.toUpperCase()).getTime()[1];

      // calculate # of animals that can be fed
      int animalRoom = (freeTime - prepTime) / feedingTime;
      if (animalRoom == 0) {
        continue;
      }
      ArrayList<Animal> hungryAnimalsToSplit = ((Feeding) taskToBeSplit).getHungryAnimals();

      if (hungryAnimalsToSplit.size() < animalRoom) {
        animalRoom = hungryAnimalsToSplit.size();
      }

      // take the first (animalRoom) animals and make a new task for
      // feeding them
      ArrayList<Animal> splitOffAnimals = new ArrayList<Animal>();
      for (Animal animal : new ArrayList<Animal>(hungryAnimalsToSplit.subList(0, animalRoom))) {
        splitOffAnimals.add(animal);
      }
      Feeding splitOffTask = new Feeding(species, splitOffAnimals);

      // add new task to hour
      hour.getTasks().add(splitOffTask);
      hour.updateTimeAvailable(splitOffTask.getDuration());

      // update the task to be split and go again, or exit the loop, if there
      // are no more animals to feed
      ArrayList<Animal> leftOverAnimals = new ArrayList<Animal>();
      for (Animal animal : new ArrayList<Animal>(hungryAnimalsToSplit.subList(
          animalRoom,
          hungryAnimalsToSplit.size()))) {
        leftOverAnimals.add(animal);
      }

      if (leftOverAnimals.size() > 0) {
        taskToBeSplit = new Feeding(species, leftOverAnimals);
      } else {
        canBePlaced = true;
        break;
      }

    }

    return canBePlaced;
  }

  /**
   * Helper method that accepts an ArrayList of tasks and sorts them by
   * maxWindow, lowest first.
   * 
   * @param tasks ArrayList of tasks to be sorted
   * @return sortedTasks: an ArrayList of sorted tasks
   */
  private ArrayList<Task> sortByWindow(ArrayList<Task> tasks) {
    // copy the array
    ArrayList<Task> tasks2 = new ArrayList<Task>();
    for (Task task : tasks) {
      tasks2.add(task);
    }

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

  /**
   * Helper method that accepts an ArrayList of hours and sorts them by
   * timeAvailable, highest first.
   * 
   * @param hourList ArrayList of hours to be sorted
   * @return sortedHours: ArrayList of sorted hours
   */
  private ArrayList<Hour> sortByTimeAvailable(ArrayList<Hour> hourList) {
    // copy array
    ArrayList<Hour> hours2 = new ArrayList<Hour>();
    for (Hour hour : hourList) {
      hours2.add(hour);
    }

    // create list to store sorted hours
    ArrayList<Hour> sortedHours = new ArrayList<Hour>();

    // move emptiest hour to the sorted array, one at a time
    while (hours2.size() > 0) {

      // find hour with most time available
      int highestTimeI = 0;
      for (int i = 1; i < hours2.size(); i++) {
        if (hours2.get(i).getTimeAvailable() > hours2.get(highestTimeI).getTimeAvailable()) {
          highestTimeI = i;
        }
      }

      // add it to the sorted array and remove it from the copy
      sortedHours.add(hours2.get(highestTimeI));
      hours2.remove(highestTimeI);
    }

    return sortedHours;
  }

  /**
   * Helper method that uses the private variable animals to create cleaning and
   * feeding tasks. Accepts an ArrayList of Integers representing the animal ids
   * of the animals that are fed with a treatment and thus do not need a task
   * made for them.
   * 
   * @param orphanIDs ArrayList of ids of orphan animals
   */
  private void generateTasks(ArrayList<Integer> orphanIDs) {

    // initiate arraylists to organise species
    ArrayList<Animal> coyotes = new ArrayList<Animal>();
    ArrayList<Animal> foxes = new ArrayList<Animal>();
    ArrayList<Animal> porcupines = new ArrayList<Animal>();
    ArrayList<Animal> beavers = new ArrayList<Animal>();
    ArrayList<Animal> raccoons = new ArrayList<Animal>();

    for (Animal animal : this.animals) {
      if (animal.getSpecies().toLowerCase().equals("coyote")) {
        coyotes.add(animal);
      } else if (animal.getSpecies().toLowerCase().equals("fox")) {
        foxes.add(animal);
      } else if (animal.getSpecies().toLowerCase().equals("porcupine")) {
        porcupines.add(animal);
      } else if (animal.getSpecies().toLowerCase().equals("beaver")) {
        beavers.add(animal);
      } else if (animal.getSpecies().toLowerCase().equals("raccoon")) {
        raccoons.add(animal);
      }
    }

    // Make lists of animals to be fed
    ArrayList<Animal> hungryCoyotes = new ArrayList<Animal>();
    for (Animal coyote : coyotes) {
      if (!orphanIDs.contains(coyote.getAnimalID())) {
        hungryCoyotes.add(coyote);
      }
    }
    ArrayList<Animal> hungryFoxes = new ArrayList<Animal>();
    for (Animal fox : foxes) {
      if (!orphanIDs.contains(fox.getAnimalID())) {
        hungryFoxes.add(fox);
      }
    }
    ArrayList<Animal> hungryPorcupines = new ArrayList<Animal>();
    for (Animal porcupine : porcupines) {
      if (!orphanIDs.contains(porcupine.getAnimalID())) {
        hungryPorcupines.add(porcupine);
      }
    }
    ArrayList<Animal> hungryBeavers = new ArrayList<Animal>();
    for (Animal beaver : beavers) {
      if (!orphanIDs.contains(beaver.getAnimalID())) {
        hungryBeavers.add(beaver);
      }
    }
    ArrayList<Animal> hungryRaccoons = new ArrayList<Animal>();
    for (Animal raccoon : raccoons) {
      if (!orphanIDs.contains(raccoon.getAnimalID())) {
        hungryRaccoons.add(raccoon);
      }
    }

    // create feeding tasks
    if (hungryCoyotes.size() > 0) {
      this.feedingTasks.add(new Feeding("coyote", hungryCoyotes));
    }
    if (hungryFoxes.size() > 0) {
      this.feedingTasks.add(new Feeding("fox", hungryFoxes));
    }
    if (hungryPorcupines.size() > 0) {
      this.feedingTasks.add(new Feeding("porcupine", hungryPorcupines));
    }
    if (hungryBeavers.size() > 0) {
      this.feedingTasks.add(new Feeding("beaver", hungryBeavers));
    }
    if (hungryRaccoons.size() > 0) {
      this.feedingTasks.add(new Feeding("raccoon", hungryRaccoons));
    }

    // create cleaning tasks
    for (Animal animal : this.animals) {
      this.cleaningTasks.add(
          new Cleaning(animal.getNickname(), animal.getSpecies()));
    }
  }
}