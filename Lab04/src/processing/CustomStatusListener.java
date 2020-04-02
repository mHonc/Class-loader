package processing;

public class CustomStatusListener implements StatusListener {
    private Status status;

    public CustomStatusListener(String taskName, int progress) {
        this.status = new Status(taskName, progress);
    }

    @Override
    public void statusChanged(Status s) {
        status = s;
    }

    public String getInfo(){
        return status.getTaskName() + " , progress: " + status.getProgress() + "%";
    }
}
