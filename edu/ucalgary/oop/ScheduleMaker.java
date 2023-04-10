package edu.ucalgary.oop;

import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.GridBagLayout;
import java.io.*;

/**
 * Main Program class that handles the user interaction and connection of
 * components.
 * 
 * @author Ken Liu
 * @version 1.5
 * @since 2023-04-04
 */
public class ScheduleMaker implements ActionListener, MouseListener {
    private SqlConnector sqlData;
    private String scheduleString = "";
    private boolean noProblems = true;
    private JFrame frame = new JFrame("Schedule Builder");

    /**
     * Constructor for ScheduleMaker, starts the GUI window
     */
    public ScheduleMaker() {
        setupGUI();
    }

    /**
     * Helper method called by ScheduleMaker constructor to setup and run the GUI.
     * Mosty existing for organizational purposes.
     */
    private void setupGUI() {
        // Sets up the frame called to contain
        // the necessary components, also setting the size and location when created.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 250);
        frame.setLocationRelativeTo(null);

        // Creating the panels
        JPanel mainPanel = new JPanel(new GridBagLayout());

        JPanel schedulePanel = new JPanel();
        schedulePanel.setLayout(new BoxLayout(schedulePanel, BoxLayout.Y_AXIS));

        // Creates the components to add to the panel.
        JButton generateSchedule = new JButton("Generate Schedule");
        generateSchedule.addActionListener(this);
        generateSchedule.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instruction = new JLabel("Click the button below to generate a schedule.");
        instruction.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add the components onto the panel.
        schedulePanel.add(instruction);
        schedulePanel.add(generateSchedule);

        // Add the child panel onto the main panel.
        mainPanel.add(schedulePanel);

        // Add the main panel onto the frame.
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    /**
     * Method for tracking when button is pressed, which creates a new schedule and
     * prompts for backup
     * confirmations, then allows user to move tasks if backups are
     * unavailable.
     * 
     * @param event ActionEvent that is triggered when the "create schedule" button
     *              is pressed
     */
    public void actionPerformed(ActionEvent event) {
        LinkedHashMap<Integer, String> confirmBackupMessage = new LinkedHashMap<Integer, String>();
        // Check if schedule is made successfully, if it is print the message success
        // message
        // else, print the confirm backup
        try {
            this.sqlData = new SqlConnector();
            this.noProblems = true;
            Schedule schedule = new Schedule(sqlData.getAnimals(), sqlData.getTreatments());

            // Checks if backup is needed at all
            for (Hour hour : schedule.getHourList()) {
                if (hour.getBackupBoolean()) {
                    confirmBackupMessage.put(hour.getHour(), "A backup is needed at " + hour.getHour() + ":00");
                }
            }

            if (confirmBackupMessage.size() != 0) {
                throw new UnconfirmedBackupsException();
            }

            createTextFile(createScheduleString(schedule, new HashMap<Integer, Integer>()));

            JOptionPane.showMessageDialog(null, "Schedule has been successfully made! Closing the program...");
            frame.dispose();

        } catch (UnconfirmedBackupsException e) {
            // Notifies user that backup is needed
            JOptionPane.showMessageDialog(null, "Schedule is in need of backup, please confirm");
            Object[] options = { "Available", "Not Available" };
            HashMap<Integer, Integer> confirmation = new HashMap<Integer, Integer>();

            // Confirms backup with the user, if backup is available store as 0, otherwise,
            // store as 1 for not available
            // If the user exits the confirmation prompt, then re-prompt the user until an
            // option is selected.
            confirmBackupMessage.forEach((hour, message) -> {
                while (true) {
                    int userChoice = 0;

                    userChoice = (JOptionPane.showOptionDialog(frame, message, "Backup Confirmation",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]));

                    if (userChoice == 0 || userChoice == 1) {
                        confirmation.put(hour, Integer.valueOf(userChoice));
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "Please confirm the availability of the backup!",
                                "Error - Unconfirmed Backup", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            // If confirmation contains a 1, then the user has selected that a backup is
            // unavailable at a specific time.
            // Otherwise, all backups are available and the schedule will be genereated.
            if (confirmation.containsValue(Integer.valueOf(1))) {
                try {
                    this.sqlData = new SqlConnector();
                    Schedule schedule = new Schedule(sqlData.getAnimals(), sqlData.getTreatments());
                    for (Hour hour : schedule.getHourList()) {
                        if (confirmation.containsKey(Integer.valueOf(hour.getHour())) &&
                                (confirmation.get(Integer.valueOf(hour.getHour())) == Integer.valueOf(1))) {

                            ArrayList<Task> tasks = hour.getTasks();
                            Task treatment = null;
                            for (Task task : tasks) {
                                // chooses to move the last treatment in the hour to avoid moving kit feeding as
                                // much as possible
                                if (task instanceof Treatment) {
                                    treatment = task;
                                }
                            }

                            if (treatment == null) {
                                this.noProblems = false;
                                break;
                            }

                            String dialogMessage = "The treatment " + treatment.getDescription() + " at " +
                                    treatment.getStartTime()
                                    + ":00 must be moved to\naccommodate for unavailable backup." +
                                    "\nPlease enter a number from 0 to 23.";

                            while (true) {
                                // prompts user for new treatment time
                                String userInput = JOptionPane.showInputDialog(frame, dialogMessage,
                                        "Treatment Time Change Required - Please Contact the Vet",
                                        JOptionPane.WARNING_MESSAGE);

                                try {
                                    // Validates treatment and store into the sql database
                                    if (validateTreatmentTime(userInput)) {
                                        int currentTreatmentID = ((Treatment) treatment).getTreatmentID();

                                        sqlData.setStartTime(currentTreatmentID, ((int) Integer.parseInt(userInput)));
                                        JOptionPane.showMessageDialog(null, "Database has been updated successfully!");
                                    } else {
                                        throw new TimeConflictException();
                                    }
                                    break;
                                } catch (NumberFormatException e1) {
                                    JOptionPane.showMessageDialog(null, "Input entered not valid, please try again",
                                            "Error - Incorrect Input", JOptionPane.ERROR_MESSAGE);
                                } catch (TimeConflictException e1) {
                                    JOptionPane.showMessageDialog(null, "Time conflict unable to be solved!",
                                            "Error - Time Conflict", JOptionPane.ERROR_MESSAGE);
                                    this.noProblems = false;
                                    break;
                                } catch (SQLConectionException e1) {
                                    JOptionPane.showMessageDialog(null,
                                            "Failed to connect to the database.\nCheck your MySQL settings and try again.",
                                            "Error - Database Connection Failure", JOptionPane.ERROR_MESSAGE);
                                    this.noProblems = false;
                                    break;
                                }
                            }
                        }
                    }

                    if (this.noProblems) {
                        JOptionPane.showMessageDialog(null,
                                "All needed treatments have been updated, please try generating the schedule again");
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Schedule creation failed.\nPlease try again.",
                                "Error", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (CloneNotSupportedException e1) {
                } catch (TimeConflictException e1) {
                    JOptionPane.showMessageDialog(null, "Time conflict unable to be solved!",
                            "Error - Time Conflict", JOptionPane.ERROR_MESSAGE);
                } catch (SQLConectionException e1) {
                    JOptionPane.showMessageDialog(null,
                            "Failed to connect to the database.\nCheck your MySQL settings and try again.",
                            "Error - Database Connection Failure", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Reattempts to create the schedule with the all available backups listed in
                // the text.
                try {
                    Schedule schedule = new Schedule(sqlData.getAnimals(), sqlData.getTreatments());

                    createTextFile(createScheduleString(schedule, confirmation));

                    JOptionPane.showMessageDialog(null,
                            "Schedule has been successfully made!\n Closing the program...");
                    frame.dispose();
                } catch (CloneNotSupportedException e1) {
                } catch (TimeConflictException e1) {

                }
            }

        } catch (TimeConflictException e) {
            e.printStackTrace();
        } catch (SQLConectionException e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to connect to the database.\nCheck your MySQL settings and try again.",
                    "Error - Database Connection Failure", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Something Unexepected has occured...");
            e.printStackTrace();
        }

    }

    /**
     * Generates the text that will be stored in the Schedule.txt file
     * 
     * @param schedule the schedule object that the schedule text will be generated
     *                 from.
     * @param backups  A hashmap relating hour number to their need for backups. 1
     *                 means a backup is needed.
     * @return A string that can be stored in a txt file.
     */
    private String createScheduleString(Schedule schedule, HashMap<Integer, Integer> backups) {
        this.scheduleString += "Schedule for " + schedule.getDate().toString() + "\n\n";
        // Loops through each hour, and if the time available has not changed then don't
        // store it into the string, otherwise, store the necessary information.
        schedule.getHourList().forEach(hour -> {
            if (hour.getTimeAvailable() < 60) {
                scheduleString += String.valueOf(hour.getHour()) + ":00";

                if (backups.containsKey(Integer.valueOf(hour.getHour()))) {
                    scheduleString += " [+ backup volunteer]\n";
                } else {
                    scheduleString += "\n";
                }

                // Loops through all tasks that are needed in that hour, and store its
                // description into the string
                for (int i = 0; i < hour.getTasks().size(); i++) {
                    scheduleString += "* " + hour.getTasks().get(i).getDescription() + "\n";
                }

                scheduleString += "\n";
            }
        });

        return scheduleString.trim();
    }

    /**
     * Creates a text file from the string returned from createScheduleString.
     * 
     * @param scheduleString string with hours and task descriptions listed.
     */
    private void createTextFile(String scheduleString) {
        try {
            File textFile = new File("Schedule.txt");

            // Creates the file if it doesnt exist, else, replace the file
            // and create the new file.
            if (!textFile.exists()) {
                textFile.createNewFile();
            } else {
                textFile.delete();
                textFile.createNewFile();
            }

            // Create the FileWriter object to write into a text file
            FileWriter textWriter = new FileWriter(textFile);
            textWriter.write(scheduleString);
            textWriter.close();
        } catch (IOException e) {

        }
    }

    /**
     * Validates user inputs for changing the start time of a treatment.
     * 
     * @param userInput value that will be validated only if it is between 0 and 23
     *                  inclusive.
     * @return true if valid input, false if invalid.
     */
    private boolean validateTreatmentTime(String userInput) {
        try {
            if (userInput == null) {
                return false;
            } else if (Integer.valueOf(userInput) >= Integer.valueOf(0)
                    && Integer.valueOf(userInput) <= Integer.valueOf(23)) {
                return true;
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException();
        }
    }

    /**
     * Implamentation of inherited abstract method.
     */
    public void mouseClicked(MouseEvent event) {
    }

    /**
     * Implamentation of inherited abstract method.
     */
    public void mouseEntered(MouseEvent event) {
    }

    /**
     * Implamentation of inherited abstract method.
     */
    public void mouseExited(MouseEvent event) {
    }

    /**
     * Implamentation of inherited abstract method.
     */
    public void mousePressed(MouseEvent event) {
    }

    /**
     * Implamentation of inherited abstract method.
     */
    public void mouseReleased(MouseEvent event) {
    }

    /**
     * Main method that creates a ScheduleMaker object which in turn runs the GUI.
     * 
     * @param args Unused.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new ScheduleMaker();
        });

    }

}