public class Hour {
    private int timeAvailable = 60;
    private final int HOUR;
    private boolean backup = false;
    private Task[] tasks;

    public Hour(int hour) {
        this.HOUR = hour;
    }

    public int getHour() { return this.HOUR; }
    public Task getTasks() {return this.tasks; }
    
    public void setBackup(boolean backup) { this.backup = backup; }
    public void setTimeAvailable(int remainingTime) {this.timeAvailable = remainingTime;}
}