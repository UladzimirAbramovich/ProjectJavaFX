package by.stormnet.projectjavafx;

public class Record<D,T> {
    private String worker;
    private D date;
    private T time;
    private String department;
    private String event;

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public D getDate() {
        return date;
    }

    public void setDate(D date) {
        this.date = date;
    }

    public T getTime() {
        return time;
    }

    public void setTime(T time) {
        this.time = time;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return worker + " " + date + " " + time + " " + department + " " + event;
    }
}
