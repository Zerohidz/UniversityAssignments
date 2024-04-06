import java.util.ArrayList;

public class CommandManager {
    private static Command[] commands;

    /**
     * @param commandStrings The array of command strings
     *                       Initializes the commands array
     */
    public static void initialize(String[] commandStrings) {
        ArrayList<Command> commandsList = new ArrayList<Command>();
        for (int i = 0; i < commandStrings.length; i++) {
            if (commandStrings[i].isEmpty())
                continue;
            commandsList.add(new Command(commandStrings[i]));
        }
        commands = commandsList.toArray(new Command[commandsList.size()]);
    }

    /**
     * Executes the commands which are previously initialized
     */
    public static void executeCommands() {
        if (!commands[0].getIsSetInitialTimeCommand()) {
            System.out.println("COMMAND: " + commands[0].getCommandString());
            System.out.println(Messages.FIRST_COMMAND_ERROR);
            Main.terminateProgram(Messages.FIRST_COMMAND_ERROR);
            return;
        }

        for (Command command : commands) {
            command.execute();
        }

        // Run a ZReport command at the end
        new Command("ZReport").execute();
    }
}
