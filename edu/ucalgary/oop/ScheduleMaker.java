package edu.ucalgary.oop;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import javax.swing.*;

import java.awt.event.*;
import java.util.*;
import java.awt.GridBagLayout;

public class ScheduleMaker implements ActionListener, MouseListener{
    private SqlConnector sqlData = new SqlConnector();

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
                // TODO: change this to a specific exception later
                throw new Exception();
            }
            JOptionPane.showMessageDialog(null, "Schedule has been successfully made!");
    	} catch (Exception e) {
    		JOptionPane.showMessageDialog(null, "Schedule is in need of backup, please confirm with the backups");
    	} catch (TimeConflictException e) {
            
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