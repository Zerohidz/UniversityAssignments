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

        if (commandString.startsWith("addBook"))
            BookManager.handleAddBook(commandString.substring("addBook".length()).trim().split("\t"));
        else if (commandString.startsWith("addMember"))
            MemberManager.handleAddMember(commandString.substring("addMember".length()).trim().split("\t"));
        else if (commandString.startsWith("borrowBook"))
            BookManager.handleBorrowBook(commandString.substring("borrowBook".length()).trim().split("\t"));
        else if (commandString.startsWith("returnBook"))
            BookManager.handleReturnBook(commandString.substring("returnBook".length()).trim().split("\t"));
        else if (commandString.startsWith("extendBook"))
            BookManager.handleExtendBook(commandString.substring("extendBook".length()).trim().split("\t"));
        else if (commandString.startsWith("readInLibrary"))
            BookManager.readInLibrary(commandString.substring("readInLibrary".length()).trim().split("\t"));
        else if (commandString.startsWith("getTheHistory"))
            CommandManager.handleGetTheHistory();
    }

    public String getCommandString() {
        return commandString;
    }
}
