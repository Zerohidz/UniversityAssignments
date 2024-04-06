public class SmartColorLamp extends SmartLamp {
    public static enum Mode {
        COLOR,
        WHITE
    }

    private Mode mode = Mode.WHITE;
    private int colorCode; // 0x000000 - 0xFFFFFF

    public SmartColorLamp(String name) {
        super(name);
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }

    public boolean getIsColorCodeValid(int colorCode) {
        return colorCode >= 0 && colorCode <= 0xFFFFFF;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public static void handleAddSmartColorLamp(String commandString) {
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
        int color = 0;
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
                if (parts[4].startsWith("0x")) {
                    color = Integer.parseInt(parts[4].substring(2), 16);
                } else {
                    kelvin = Integer.parseInt(parts[4]);
                }
                brightness = Integer.parseInt(parts[5]);
            } catch (Exception e) {
                System.out.println(Messages.GENERAL_ERROR_MESSAGE);
                return;
            }
        }

        SmartColorLamp smartColorLamp = new SmartColorLamp(name);
        if (!smartColorLamp.getIsBrightnessValid(brightness)) {
            System.out.println(Messages.BRIGHTNESS_MESSAGE);
            return;
        }

        if (color != 0) {
            if (!smartColorLamp.getIsColorCodeValid(color)) {
                System.out.println(Messages.COLOR_CODE_MESSAGE);
                return;
            }
            smartColorLamp.setMode(Mode.COLOR);
            smartColorLamp.setColorCode(color);
        } else {
            if (!smartColorLamp.getIsKelvinValid(kelvin)) {
                System.out.println(Messages.KELVIN_MESSAGE);
                return;
            }
            smartColorLamp.setKelvin(kelvin);
        }
        if (initialStatus)
            smartColorLamp.switchOn();
        else
            smartColorLamp.switchOff();
        smartColorLamp.setBrightness(brightness);

        DeviceManager.addDevice(smartColorLamp);
    }

    @Override
    public String getZReportLine() {
        update();
        String status = isSwitchedOn() ? "on" : "off";
        String colorCode = String.format("0x%06X", getColorCode());
        String kelvin = String.format("%dK", getKelvin());
        String brightness = String.format("%d%%", getBrightness());
        return String.format(
                "Smart Color Lamp %s is %s and its color value is %s with %s brightness, and its time to switch its status is %s.",
                getName(), status, getMode() == Mode.COLOR ? colorCode : kelvin, brightness, getSwitchTimeString());
    }

    public static void handleSetColorCode(String commandString) {
        // SetColorCode <TAB> <NAME> <TAB> <COLOR_CODE>
        String[] parts = commandString.split("\t");
        if (parts.length != 3) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        String name = parts[1];
        String colorCodeString = parts[2];

        if (!colorCodeString.startsWith("0x")) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        int colorCode;
        try {
            colorCode = Integer.parseInt(colorCodeString.substring(2), 16);
        } catch (Exception e) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        Device device = DeviceManager.findDeviceByName(name);
        if (device == null) {
            System.out.println(Messages.NOT_SUCH_A_DEVICE_MESSAGE);
            return;
        }

        if (!(device instanceof SmartColorLamp)) {
            System.out.println("ERROR: This device is not a smart color lamp!");
            return;
        }

        SmartColorLamp smartColorLamp = (SmartColorLamp) device;
        if (!smartColorLamp.getIsColorCodeValid(colorCode)) {
            System.out.println(Messages.COLOR_CODE_MESSAGE);
            return;
        }
        smartColorLamp.setColorCode(colorCode);

    }

    public static void handleSetWhite(String commandString) {
        // SetWhite <TAB> <NAME> <TAB> <KELVIN> <TAB> <BRIGHTNESS>
        String[] parts = commandString.split("\t");
        if (parts.length != 4) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        String name = parts[1];
        int kelvin;
        int brightness;

        Device device = DeviceManager.findDeviceByName(name);
        if (device == null) {
            System.out.println(Messages.NOT_SUCH_A_DEVICE_MESSAGE);
            return;
        }

        if (!(device instanceof SmartColorLamp)) {
            System.out.println("ERROR: This device is not a smart color lamp!");
            return;
        }

        SmartColorLamp smartColorLamp = (SmartColorLamp) device;
        try {
            kelvin = Integer.parseInt(parts[2]);
            brightness = Integer.parseInt(parts[3]);
        } catch (Exception e) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        if (!smartColorLamp.getIsKelvinValid(kelvin)) {
            System.out.println(Messages.KELVIN_MESSAGE);
            return;
        } else if (!smartColorLamp.getIsBrightnessValid(brightness)) {
            System.out.println(Messages.BRIGHTNESS_MESSAGE);
            return;
        }

        smartColorLamp.setKelvin(kelvin);
        smartColorLamp.setBrightness(brightness);
        smartColorLamp.setMode(Mode.WHITE);

    }

    public static void handleSetColor(String commandString) {
        // SetColor <TAB> <NAME> <TAB> <COLOR_CODE> <TAB> <BRIGHTNESS>
        String[] parts = commandString.split("\t");
        if (parts.length != 4) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        String name = parts[1];
        int colorCode;
        int brightness;

        Device device = DeviceManager.findDeviceByName(name);
        if (device == null) {
            System.out.println(Messages.NOT_SUCH_A_DEVICE_MESSAGE);
            return;
        }

        if (!(device instanceof SmartColorLamp)) {
            System.out.println("ERROR: This device is not a smart color lamp!");
            return;
        }

        if (!parts[2].startsWith("0x")) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        SmartColorLamp smartColorLamp = (SmartColorLamp) device;
        try {
            colorCode = Integer.parseInt(parts[2].substring(2), 16);
            brightness = Integer.parseInt(parts[3]);
        } catch (Exception e) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        if (!smartColorLamp.getIsColorCodeValid(colorCode)) {
            System.out.println(Messages.COLOR_CODE_MESSAGE);
            return;
        } else if (!smartColorLamp.getIsBrightnessValid(brightness)) {
            System.out.println(Messages.BRIGHTNESS_MESSAGE);
            return;
        }

        smartColorLamp.setColorCode(colorCode);
        smartColorLamp.setBrightness(brightness);
        smartColorLamp.setMode(Mode.COLOR);

    }

}
