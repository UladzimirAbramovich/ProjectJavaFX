package by.stormnet.projectjavafx;

public class WorkingTime <T> {

    private T startWorkingDay;
    private T endWorkingDay;
    private T startLunch;
    private T endLunch;
    private T interval;
    private T hardWorkingTime;

    public WorkingTime(T startWorkingDay, T endWorkingDay, T startLunch, T endLunch, T interval, T hardWorkingTime) {
        this.startWorkingDay = startWorkingDay;
        this.endWorkingDay = endWorkingDay;
        this.startLunch = startLunch;
        this.endLunch = endLunch;
        this.interval = interval;
        this.hardWorkingTime = hardWorkingTime;
    }

    public T getStartWorkingDay() {
        return startWorkingDay;
    }

    public void setStartWorkingDay(T startWorkingDay) {
        this.startWorkingDay = startWorkingDay;
    }

    public T getEndWorkingDay() {
        return endWorkingDay;
    }

    public void setEndWorkingDay(T endWorkingDay) {
        this.endWorkingDay = endWorkingDay;
    }

    public T getStartLunch() {
        return startLunch;
    }

    public void setStartLunch(T startLunch) {
        this.startLunch = startLunch;
    }

    public T getEndLunch() {
        return endLunch;
    }

    public void setEndLunch(T endLunch) {
        this.endLunch = endLunch;
    }

    public T getInterval() {
        return interval;
    }

    public void setInterval(T interval) {
        this.interval = interval;
    }

    public T getHardWorkingTime() {
        return hardWorkingTime;
    }

    public void setHardWorkingTime(T hardWorkingTime) {
        this.hardWorkingTime = hardWorkingTime;
    }
}
