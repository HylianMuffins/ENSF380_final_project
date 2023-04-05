package edu.ucalgary.oop;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.GridBagLayout;
import java.io.*;

public class ScheduleMaker implements ActionListener, MouseListener{
    private SqlConnector sqlData = new SqlConnector();
    private String scheduleString = "";

    public ScheduleMaker() {
        setupGUI();
    }
    
    private void setupGUI(){
    	// Creates the frame called "Schedule Builder" to contain 
    	//the necessary components, also setting the size and location when created.
    	JFrame frame = new JFrame("Schedule Builder");
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
    
    public void actionPerformed(ActionEvent event){
    	// Check if schedule is made successfully, if it is print the message success message
    	// else, print the confirm backup
    	try {
    		Schedule schedule = new Schedule(sqlData.getAnimals(), sqlData.getTreatments());
            ArrayList<String> confirmBackupMessage = new ArrayList<String>();

            for (Hour hour: schedule.getHourList()) {
                if (hour.getBackupBoolean()) {
                    confirmBackupMessage.add("A backup is needed at " + hour.getHour());
                }
            }

            if (confirmBackupMessage.size() != 0) {
                throw new UnconfirmedBackupsException();
            }

            createTextFile(createScheduleString(schedule));
            
            JOptionPane.showMessageDialog(null, "Schedule has been successfully made!");
    	} catch (UnconfirmedBackupsException e) {
    		JOptionPane.showMessageDialog(null, "Schedule is in need of backup, please confirm with the backups");
    	} catch (TimeConflictException e) {
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Something Unexepected has occured...");
            //e.printStackTrace();
        }

    }
    
    private String createScheduleString(Schedule schedule) {
        this.scheduleString += "Schedule for " + schedule.getDate().toString() + "\n\n";
        // Loops through each hour, and if the time available has not changed then don't
        // store it into the string, otherwise, store the necessary information.
        schedule.getHourList().forEach(hour -> {
            if (hour.getTimeAvailable() < 60) {
                scheduleString += String.valueOf(hour.getHour()) + ":00\n";

                // Loops through all tasks that are needed in that hour, and store its description
                // into the string
                for (int i = 0; i < hour.getTasks().size(); i++) {
                    scheduleString += "* " + hour.getTasks().get(i).getDescription() + "\n"; 
                }

                scheduleString += "\n";
            }
        });
        
        return scheduleString;
    }

    private void createTextFile(String schedule) {
        try {
            File textFile = new File("Schedule.txt");
            
            // Creates the file if it doesnt exist, else, replace the file
            // and create the new file.
            if(!textFile.exists()) {
                textFile.createNewFile();
            } else {
                textFile.delete();
                textFile.createNewFile();
            }

            // Create the FileWriter object to write into a text file
            FileWriter textWriter = new FileWriter(textFile);
            textWriter.write(schedule);
            textWriter.close();
        } catch (IOException e) {

        }
    }

    public void mouseClicked(MouseEvent event){
    }
    
    public void mouseEntered(MouseEvent event){
        
    }

    public void mouseExited(MouseEvent event){
        
    }

    public void mousePressed(MouseEvent event){
    }

    public void mouseReleased(MouseEvent event){
        
    }

    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new ScheduleMaker();        
        });
       
    }
        
}