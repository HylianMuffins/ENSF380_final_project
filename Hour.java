public class Hour {
    private int timeAvailable = 60;
    private final int HOUR;
    private boolean backup = false;
    private Task[] tasks;

    public Hour(int hour) throws IllegalArgumentException{
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException();
        }
        this.HOUR = hour;
    }

    public int getHour() { return this.HOUR; }
    public Task getTasks() {return this.tasks; }
    
    public void setBackup(boolean backup) { this.backup = backup; }

    public void setTimeAvailable(int remainingTime) throws IllegalArgumentException {
        if (remainingTime > timeAvailable) {
            throw new IllegalArgumentException();
        }

        this.timeAvailable = remainingTime;
    }
}