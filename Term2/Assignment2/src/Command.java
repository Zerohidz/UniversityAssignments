public class Command {
    private String commandString;

    public Command(String commandString) {
        this.commandString = commandString;
    }

    /**
     * Executes the command by deciding which method to call
     */
    public void execute() {
        if (commandString.isEmpty())
            return;
        System.out.println("COMMAND: " + commandString);

        if (commandString.startsWith("SetInitialTime")) {
            TimeManager.handleSetInitialTime(commandString);
        } else if (commandString.startsWith("SetTime")) {
            TimeManager.handleSetTime(commandString);
        } else if (commandString.startsWith("SetSwitchTime")) {
            DeviceManager.handleSetSwitchTime(commandString);
        } else if (commandString.startsWith("SkipMinutes")) {
            TimeManager.handleSkipMinutes(commandString);
        } else if (commandString.startsWith("Nop")) {
            DeviceManager.handleNop();
        } else if (commandString.startsWith("Add\tSmartPlug")) {
            SmartPlug.handleAddSmartPlug(commandString);
        } else if (commandString.startsWith("Add\tSmartCamera")) {
            SmartCamera.handleAddSmartCamera(commandString);
        } else if (commandString.startsWith("Add\tSmartLamp")) {
            SmartLamp.handleAddSmartLamp(commandString);
        } else if (commandString.startsWith("Add\tSmartColorLamp")) {
            SmartColorLamp.handleAddSmartColorLamp(commandString);
        } else if (commandString.startsWith("Remove")) {
            DeviceManager.handleRemove(commandString);
        } else if (commandString.startsWith("ZReport")) {
            DeviceManager.handleZReport();
        } else if (commandString.startsWith("Switch")) {
            DeviceManager.handleSwitch(commandString);
        } else if (commandString.startsWith("ChangeName")) {
            DeviceManager.handleChangeName(commandString);
        } else if (commandString.startsWith("PlugIn")) {
            SmartPlug.handlePlugIn(commandString);
        } else if (commandString.startsWith("PlugOut")) {
            SmartPlug.handlePlugOut(commandString);
        } else if (commandString.startsWith("SetKelvin")) {
            SmartLamp.handleSetKelvin(commandString);
        } else if (commandString.startsWith("SetBrightness")) {
            SmartLamp.handleSetBrightness(commandString);
        } else if (commandString.startsWith("SetColorCode")) {
            SmartColorLamp.handleSetColorCode(commandString);
        } else if (commandString.startsWith("SetWhite")) {
            SmartColorLamp.handleSetWhite(commandString);
        } else if (commandString.startsWith("SetColor")) {
            SmartColorLamp.handleSetColor(commandString);
        } else {
            System.out.println(Messages.GENERAL_ERROR_MESSAGE);
        }
    }

    public boolean getIsSetInitialTimeCommand() {
        return commandString.startsWith("SetInitialTime");
    }

    public String getCommandString() {
        return this.commandString;
    }
}
