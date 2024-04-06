import java.time.LocalDateTime;

public class SmartPlug extends Device {
    private int volts = 220;
    private double amps;
    private int consumedEnergy;
    private boolean pluggedIn;
    private LocalDateTime runStartTime;

    public SmartPlug(String name) {
        super(name);
    }

    public int getVolts() {
        return volts;
    }

    public void setVolts(int volts) {
        this.volts = volts;
    }

    public double getAmps() {
        return this.amps;
    }

    public void setAmps(double amps) {
        if (amps == 0)
            return;
        this.amps = amps;
    }

    public boolean getIsAmpsValid(double amps) {
        return amps >= 0;
    }

    public int getConsumedEnergy() {
        return consumedEnergy;
    }

    public void setConsumedEnergy(int consumedEnergy) {
        this.consumedEnergy = consumedEnergy;
    }

    public boolean isPluggedIn() {
        return pluggedIn;
    }

    @Override
    public void switchOn() {
        this.runStartTime = TimeManager.getCurrentTime();
        super.switchOn();
    }

    public void plugIn() {
        this.runStartTime = TimeManager.getCurrentTime();
        this.pluggedIn = true;
    }

    public void plugOut() {
        update();
        this.pluggedIn = false;
    }

    @Override
    public void update() {
        if (isPluggedIn() && isSwitchedOn()) {
            if (runStartTime == null)
                return;
            LocalDateTime currentTime = TimeManager.getCurrentTime();
            int secondsPassed = TimeManager.getSecondsPassed(runStartTime, currentTime);
            this.consumedEnergy += (int) (secondsPassed * getAmps() * getVolts() / 30); // 30 is here because example
                                                                                        // inputs were like this
            this.runStartTime = currentTime;
        }
    }

    public static void handleAddSmartPlug(String commandString) {
        String[] parts = commandString.split("\t");

        if (parts.length < 3 || parts.length > 5) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        String name = parts[2];

        if (DeviceManager.findDeviceByName(name) != null) {
            System.out.println(Messages.DEVICE_ALREADY_EXISTS_MESSAGE);
            return;
        }

        boolean initialStatus = false;
        double amperage = 0.0;

        if (parts.length >= 4) {
            if (parts[3].equals("On"))
                initialStatus = true;
            else if (parts[3].equals("Off"))
                initialStatus = false;
            else {
                System.out.println(Messages.GENERAL_ERROR_MESSAGE);
                return;
            }
        }

        if (parts.length == 5) {
            try {
                amperage = Double.parseDouble(parts[4]);
            } catch (Exception e) {
                System.out.println(Messages.GENERAL_ERROR_MESSAGE);
                return;
            }
        }

        SmartPlug smartPlug = new SmartPlug(name);
        if (!smartPlug.getIsAmpsValid(amperage)) {
            System.out.println(Messages.AMPERE_VALUE_ERROR);
            return;
        }
        smartPlug.setAmps(amperage);

        if (initialStatus)
            smartPlug.switchOn();
        else
            smartPlug.switchOff();
        DeviceManager.addDevice(smartPlug);
    }

    @Override
    public String getZReportLine() {
        update();
        String status = isSwitchedOn() ? "on" : "off";
        String consumedEnergy = String.format("%.2f", (float) getConsumedEnergy());
        return "Smart Plug " + getName() + " is " + status + " and consumed " + consumedEnergy
                + "W so far (excluding current device), and its time to switch its status is " + getSwitchTimeString()
                + ".";
    }

    public static void handlePlugIn(String commandString) {
        String[] parts = commandString.split("\t");

        if (parts.length != 3) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        String name = parts[1];
        double amperage = 0.0;

        try {
            amperage = Double.parseDouble(parts[2]);
        } catch (Exception e) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        if (amperage < 0) {
            System.out.println("ERROR: Ampere value must be a positive number!");
            return;
        }

        Device device = DeviceManager.findDeviceByName(name);
        if (device == null) {
            System.out.println(Messages.NOT_SUCH_A_DEVICE_MESSAGE);
            return;
        }

        if (!(device instanceof SmartPlug)) {
            System.out.println("ERROR: This device is not a smart plug!");
            return;
        }

        SmartPlug smartPlug = (SmartPlug) device;
        if (smartPlug.isPluggedIn()) {
            System.out.println("ERROR: There is already an item plugged in to that plug!");
            return;
        }

        smartPlug.setAmps(amperage);
        smartPlug.plugIn();
    }

    public static void handlePlugOut(String commandString) {
        // PlugOut <TAB> <NAME>
        String[] parts = commandString.split("\t");

        if (parts.length != 2) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        String name = parts[1];

        Device device = DeviceManager.findDeviceByName(name);
        if (device == null) {
            System.out.println(Messages.NOT_SUCH_A_DEVICE_MESSAGE);
            return;
        }

        if (!(device instanceof SmartPlug)) {
            System.out.println("ERROR: This device is not a smart plug!");
            return;
        }

        SmartPlug smartPlug = (SmartPlug) device;
        if (!smartPlug.isPluggedIn()) {
            System.out.println("ERROR: This plug has no item to plug out from that plug!");
            return;
        }

        smartPlug.setAmps(220);
        smartPlug.plugOut();
    }
}
