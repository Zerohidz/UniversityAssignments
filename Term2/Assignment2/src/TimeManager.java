import java.time.LocalDateTime;
import java.time.temporal.*;
import java.time.format.DateTimeFormatter;

public class TimeManager {
    public static LocalDateTime initialTime;
    public static LocalDateTime currentTime;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");

    public static LocalDateTime getInitialTime() {
        return initialTime;
    }

    public static void setInitialTime(LocalDateTime initialTime) {
        TimeManager.initialTime = initialTime;
    }

    public static LocalDateTime getCurrentTime() {
        return currentTime;
    }

    public static String getCurrentTimeString() {
        if (currentTime != null)
            return currentTime.format(formatter);
        else
            return "null";
    }

    public static void setCurrentTime(LocalDateTime currentTime) {
        TimeManager.currentTime = currentTime;
    }

    public static boolean handleSetInitialTime(String commandString) {
        if (initialTime != null) {
            System.out.println("ERROR: Initial time already set!");
            return false;
        }

        String[] parts = commandString.split("\t");
        if (parts.length != 2) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return false;
        }

        try {
            initialTime = LocalDateTime.parse(parts[1].replace("_", "T"));
            currentTime = LocalDateTime.from(initialTime);
            System.out.println("SUCCESS: Time has been set to " + parts[1] + "!");
            return true;
        } catch (Exception e) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return false;
        }
    }

    public static void handleSetTime(String commandString) {
        String[] parts = commandString.split("\t");
        if (parts.length != 2) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }
        try {
            LocalDateTime newTime = LocalDateTime.parse(parts[1].replace("_", "T"));
            DeviceManager.nopUntil(newTime);
            setCurrentTime(newTime);
        } catch (Exception e) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }
    }

    public static void handleSkipMinutes(String commandString) {
        String[] parts = commandString.split("\t");
        if (parts.length != 2) {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
            return;
        }

        String skipTime = parts[1];

        try {
            int minutes = Integer.parseInt(skipTime);
            LocalDateTime newTime = currentTime.plusMinutes(minutes);
            DeviceManager.nopUntil(newTime);
            setCurrentTime(newTime);
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Invalid minutes value in SkipMinutes command: " + parts[1]);
            return;
        }
    }

    public static int getSecondsPassed(LocalDateTime runStartTime, LocalDateTime currentTime2) {
        return (int) ChronoUnit.MINUTES.between(runStartTime, currentTime2);
    }

}
