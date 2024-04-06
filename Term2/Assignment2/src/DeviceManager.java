import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DeviceManager {
    private static List<Device> devices = new ArrayList<Device>();

    public static void sortDevices() {
        devices = devices.stream()
                .sorted(Comparator.comparing(Device::getSwitchTime, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    public static void addDevice(Device device) {
        devices.add(device);
        sortDevices();
    }

    public static void handleSetSwitchTime(String input) {
        String[] parts = input.split("\t");
        if (parts.length != 3) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        String name = parts[1];
        LocalDateTime switchTime;
        try {
            switchTime = LocalDateTime.parse(parts[2].replace("_", "T"));
        } catch (Exception e) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        Device device = findDeviceByName(name);
        if (device == null) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        // buraya hiç girmiyor çok garip
        device.setSwitchTime(switchTime);
        sortDevices();
    }

    public static Device findDeviceByName(String name) {
        for (Device device : devices) {
            if (device.getName().equals(name)) {
                return device;
            }
        }
        return null;
    }

    public static boolean removeDeviceByName(String name) {
        for (int i = 0; i < devices.size(); i++) {
            if (devices.get(i).getName().equals(name)) {
                devices.remove(i);
                return true;
            }
        }
        return false;
    }

    public static void handleNop() {
        if (devices.isEmpty() || devices.get(0).getSwitchTime() == null) {
            System.out.println(Messages.NOTHING_TO_SWITCH_MESSAGE);
            return;
        }

        Device firstDevice = devices.get(0);
        TimeManager.setCurrentTime(firstDevice.getSwitchTime());
        firstDevice.switchInverse();
        firstDevice.setSwitchTime(null);
        sortDevices();
    }

    public static void nopUntil(LocalDateTime newTime) {
        while (!devices.isEmpty() && devices.get(0).getSwitchTime() != null
                && devices.get(0).getSwitchTime().isBefore(newTime)) {
            handleNop();
        }
    }

    public static void handleRemove(String commandString) {
        String[] parts = commandString.split("\t");
        if (parts.length != 2) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        String name = parts[1];
        Device device = findDeviceByName(name);
        if (device != null) {
            removeDeviceByName(name);
            System.out.println("SUCCESS: Information about removed smart device is as follows:");
            System.out.println(device.getZReportLine());
        } else {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }
    }

    public static void handleZReport() {
        System.out.println("Time is:\t" + TimeManager.getCurrentTimeString());
        for (Device device : devices) {
            System.out.println(device.getZReportLine());
        }
    }

    public static void handleSwitch(String commandString) {
        String[] parts = commandString.split("\t");
        if (parts.length != 3) {
            System.out.println("Invalid Switch command: " + commandString);
            return;
        }

        String name = parts[1];
        String status = parts[2];

        Device device = findDeviceByName(name);
        if (device == null) {
            System.out.println(Messages.NOT_SUCH_A_DEVICE_MESSAGE);
            return;
        }

        if (status.equals("On")) {
            if (device.isSwitchedOn()) {
                System.out.println("ERROR: This device is already switched on!");
            } else {
                device.switchOn();
                System.out.println("SUCCESS: Switched the device on!");
            }
        } else if (status.equals("Off")) {
            if (!device.isSwitchedOn()) {
                System.out.println("ERROR: This device is already switched off!");
            } else {
                device.switchOff();
                System.out.println("SUCCESS: Switched the device off!");
            }
        } else {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
        }
    }

    public static void handleChangeName(String commandString) {
        // ChangeName <TAB> <OLD_NAME> <TAB> <NEW_NAME>
        String[] parts = commandString.split("\t");
        if (parts.length != 3) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        String oldName = parts[1];
        String newName = parts[2];

        if (oldName.equals(newName)) {
            System.out.println("ERROR: Both of the names are the same, nothing changed!");
            return;
        }

        Device device = findDeviceByName(oldName);
        if (device == null) {
            System.out.println(Messages.NOT_SUCH_A_DEVICE_MESSAGE);
            return;
        }

        if (findDeviceByName(newName) != null) {
            System.out.println(Messages.NAME_ALREADY_EXISTS_MESSAGE);
            return;
        }

        device.setName(newName);
    }

}