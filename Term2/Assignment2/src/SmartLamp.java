public class SmartLamp extends Device {
    private int kelvin = 4000; // 2000 - 6500
    private int brightness = 100; // %0 - %100

    public SmartLamp(String name) {
        super(name);
    }

    public int getKelvin() {
        return kelvin;
    }

    public void setKelvin(int kelvin) {
        this.kelvin = kelvin;
    }

    public boolean getIsKelvinValid(int kelvin) {
        return kelvin >= 2000 && kelvin <= 6500;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public boolean getIsBrightnessValid(int brightness) {
        return brightness >= 0 && brightness <= 100;
    }

    public static void handleAddSmartLamp(String commandString) {
        String[] parts = commandString.split("\t");
        if (parts.length < 3 || parts.length > 6) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        String name = parts[2];

        if (DeviceManager.findDeviceByName(name) != null) {
            System.out.println(Messages.DEVICE_ALREADY_EXISTS_MESSAGE);
            return;
        }

        boolean initialStatus = false;
        int kelvin = 4000;
        int brightness = 100;

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

        if (parts.length >= 6) {
            try {
                kelvin = Integer.parseInt(parts[4]);
                brightness = Integer.parseInt(parts[5]);
            } catch (Exception e) {
                System.out.println(Messages.GENERAL_ERROR_MESSAGE);
                return;
            }
        }

        SmartLamp smartLamp = new SmartLamp(name);
        if (!smartLamp.getIsBrightnessValid(brightness)) {
            System.out.println(Messages.BRIGHTNESS_MESSAGE);
            return;
        } else if (!smartLamp.getIsKelvinValid(kelvin)) {
            System.out.println(Messages.KELVIN_MESSAGE);
            return;
        }

        if (initialStatus)
            smartLamp.switchOn();
        else
            smartLamp.switchOff();
        smartLamp.setKelvin(kelvin);
        smartLamp.setBrightness(brightness);
        DeviceManager.addDevice(smartLamp);
    }

    @Override
    public String getZReportLine() {
        update();
        String status = this.isSwitchedOn() ? "on" : "off";
        return String.format(
                "Smart Lamp %s is %s and its kelvin value is %dK with %d%% brightness, and its time to switch its status is %s.",
                this.getName(), status, this.getKelvin(), this.getBrightness(), getSwitchTimeString());
    }

    public static void handleSetKelvin(String commandString) {
        // SetKelvin <TAB> <NAME> <TAB> <KELVIN>
        String[] parts = commandString.split("\t");
        if (parts.length != 3) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        String name = parts[1];
        int kelvin = 0;
        try {
            kelvin = Integer.parseInt(parts[2]);
        } catch (Exception e) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        Device device = DeviceManager.findDeviceByName(name);
        if (device == null) {
            System.out.println(Messages.NOT_SUCH_A_DEVICE_MESSAGE);
            return;
        }

        if (!(device instanceof SmartLamp)) {
            System.out.println("ERROR: This device is not a smart lamp!");
            return;
        }

        SmartLamp smartLamp = (SmartLamp) device;
        if (!smartLamp.getIsKelvinValid(kelvin)) {
            System.out.println(Messages.KELVIN_MESSAGE);
            return;
        }
        smartLamp.setKelvin(kelvin);
    }

    public static void handleSetBrightness(String commandString) {
        // SetBrightness <TAB> <NAME> <TAB> <BRIGHTNESS>
        String[] parts = commandString.split("\t");
        if (parts.length != 3) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        String name = parts[1];
        int brightness = 0;
        try {
            brightness = Integer.parseInt(parts[2]);
        } catch (Exception e) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        Device device = DeviceManager.findDeviceByName(name);
        if (device == null) {
            System.out.println(Messages.NOT_SUCH_A_DEVICE_MESSAGE);
            return;
        }

        if (!(device instanceof SmartLamp)) {
            System.out.println("ERROR: This device is not a smart lamp!");
            return;
        }

        SmartLamp smartLamp = (SmartLamp) device;
        if (!smartLamp.getIsBrightnessValid(brightness)) {
            System.out.println(Messages.BRIGHTNESS_MESSAGE);
            return;
        }
        smartLamp.setBrightness(brightness);
    }
}
