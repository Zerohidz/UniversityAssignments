import java.time.LocalDateTime;

public class SmartCamera extends Device {
    private double storageUsage; // mb/min
    private double totalStorageUsage; // mb
    private LocalDateTime runStartTime;

    public double getTotalStorageUsage() {
        return totalStorageUsage;
    }

    public void setTotalStorageUsage(double totalStorageUsage) {
        this.totalStorageUsage = totalStorageUsage;
    }

    public SmartCamera(String name) {
        super(name);
    }

    public double getStorageUsage() {
        return storageUsage;
    }

    public void setStorageUsage(double storageUsage) {
        this.storageUsage = storageUsage;
    }

    public boolean getIsStorageUsageValid(double storageUsage) {
        return storageUsage >= 0;
    }

    @Override
    public void switchOn() {
        super.switchOn();
        this.runStartTime = TimeManager.getCurrentTime();
    }

    @Override
    protected void update() {
        if (isSwitchedOn()) {
            if (this.runStartTime == null)
                return;
            double timeDifference = TimeManager.getSecondsPassed(this.runStartTime, TimeManager.getCurrentTime());
            this.totalStorageUsage += timeDifference * this.storageUsage;
            this.runStartTime = TimeManager.getCurrentTime();
        }
    }

    public static void handleAddSmartCamera(String commandString) {
        String[] parts = commandString.split("\t");

        if (parts.length < 4 || parts.length > 5) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        String name = parts[2];

        if (DeviceManager.findDeviceByName(name) != null) {
            System.out.println(Messages.DEVICE_ALREADY_EXISTS_MESSAGE);
            return;
        }

        boolean initialStatus = false;
        double storageUsage = 0;

        try {
            storageUsage = Double.parseDouble(parts[3]);
        } catch (Exception e) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        if (parts.length >= 5) {
            if (parts[4].equals("On"))
                initialStatus = true;
            else if (parts[4].equals("Off"))
                initialStatus = false;
            else {
                System.out.println(Messages.GENERAL_ERROR_MESSAGE);
                return;
            }
        }

        SmartCamera smartCamera = new SmartCamera(name);
        if (!smartCamera.getIsStorageUsageValid(storageUsage)) {
            System.out.println(Messages.STORAGE_USAGE_MESSAGE);
            return;
        }
        smartCamera.setStorageUsage(storageUsage);

        if (initialStatus)
            smartCamera.switchOn();
        else
            smartCamera.switchOff();
        DeviceManager.addDevice(smartCamera);
    }

    @Override
    public String getZReportLine() {
        update();
        String status = isSwitchedOn() ? "on" : "off";
        String storage = String.format("%.2f", getStorageUsage());
        return "Smart Camera " + getName() + " is " + status + " and used " + storage
                + " MB of storage so far (excluding current device), and its time to switch its status is "
                + getSwitchTimeString() + ".";
    }
}
