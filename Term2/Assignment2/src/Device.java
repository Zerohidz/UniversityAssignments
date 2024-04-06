import java.time.LocalDateTime;

public abstract class Device {
    private String name;
    private boolean switchedOn;
    private LocalDateTime switchTime;

    public Device(String name) {
        this.name = name;
    }

    public abstract String getZReportLine();

    public LocalDateTime getSwitchTime() {
        return switchTime;
    }

    public String getSwitchTimeString() {
        if (this.switchTime != null)
            return switchTime.format(TimeManager.formatter);
        else
            return "null";
    }

    public void setSwitchTime(LocalDateTime switchTime) {
        this.switchTime = switchTime;
    }

    public boolean isSwitchedOn() {
        return switchedOn;
    }

    public void switchOn() {
        this.switchedOn = true;
    }
    
    public void switchOff() {
        update();
        this.switchedOn = false;
    }

    /**
     * Reverses the switch state, if it is on, it will be off, if it is off, it will be on
     */
    public void switchInverse() {
        update();
        this.switchedOn = !this.switchedOn;
    }

    protected void update() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}