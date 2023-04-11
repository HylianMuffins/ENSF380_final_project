# ENSF380_final_project

This is the ensf 380 final project repository completed by group 49.

In order to run this program you must follow the following steps:

1. Setting up MySQL

   - This program pulls all its data from a specificly named sql server, so if you don't have it setup properly the aplication will never progress pass the starting page.
   - Create an SQL server named 'localhost' using your root user
   - Run the sql file representing your database in a mysql shell to create the database within the server (see included treatments.sql file for example on the required structure, or just use it as a test)
     - On MacOS this can be done by running the following command in your terminal, and on windows it should be run through the command line (both OSs need mysql in the PATH or otherwise be navigated to the mysql bin folder)
       - `mysql -u root -p < <path to .sql file>`
     - If you are working in a MySQL shell simply run the command
       - `source <path to .sql file>;`
   - Now that the database exists, we need to create a way for the program to have access. This is done through a specific user named oop created with the following command in mysql:
     - `CREATE USER 'oop'@'localhost' IDENTIFIED BY 'password';`
   - This is how the program will connect to the database, but it will also need privileges to read and write data. You can grant the new user these permissions on the ewr database with the following commands:
     - `USE EWR`
     - `GRANT SELECT ON * TO 'oop'@'localhost';`
     - `GRANT UPDATE ON Treatments TO 'oop'@'localhost';`
   - The program should now have all the access it needs to run smoothly!

2. Compiling and running program

   - In order for the project to compile it needs access to the mysql-connector-java library.
   - You can compile the project including this library with the following command on MacOS:
     - `javac -cp .:lib/mysql-connector-java-8.0.23.jar edu/ucalgary/oop/*.java`
   - Or on Windows:

     - `javac -cp .;lib/mysql-connector-java-8.0.23.jar edu/ucalgary/oop/*.java`

   - Then to run the code use the following command on MacOS:
     - `java -cp .:lib/mysql-connector-java-8.0.23.jar edu.ucalgary.oop.ScheduleMaker`
   - Or on Windows:
     - `java -cp .;lib/mysql-connector-java-8.0.23.jar edu.ucalgary.oop.ScheduleMaker`

3. Using the program
   - The instructions and descriptions in the application are simple enough to understand, and anyone should be able to use the program without any struggles, but here are a few key points to be aware of:
     - If a time conflict arises that cannot be handled by a backup volunteer, and the database is updated, the schedule will have to rebuild itself from the new data. This means that none of the backups are garunteed to be needed in the same place. As a result, if a treatment is moved, previously confirmed backups will need to be re-confirmed after the database is updated.
     - After an update, the program will not automatically generate a new schedule, that is left up to the user. So after you move a task and are prompted to create a new schedule, you must press the "Create Schedule" button again, otherwise no schedule will be created.
     - Once a schedule is finalised, a text file will be generated in the same directory in which the code was run, containing information about which tasks should be performed in each hour, and where backup volunteers are required.
