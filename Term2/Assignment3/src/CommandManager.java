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
        for (Command command : commands) {
            command.execute();
        }
    }

    public static void handleGetTheHistory() {
        ArrayList<Member> students = MemberManager.getStudents();
        ArrayList<Member> academics = MemberManager.getAcademics();
        ArrayList<Book> printedBooks = BookManager.getPrintedBooks();
        ArrayList<Book> handwrittenBooks = BookManager.getHandwrittenBooks();
        ArrayList<Book> borrowedBooks = BookManager.getBorrowedBooks();
        ArrayList<Book> readingBooks = BookManager.getReadingBooks();
        
        System.out.println("History of library:");
        System.out.println();
        System.out.println("Number of students: " + students.size());
        for (Member student : students) {
            System.out.println(String.format("Student [id: %d]", student.getId()));
        }
        System.out.println();
        System.out.println("Number of academics: " + academics.size());
        for (Member academic : academics) {
            System.out.println(String.format("Academic [id: %d]", academic.getId()));
        }
        System.out.println();
        System.out.println("Number of printed books: " + printedBooks.size());
        for (Book printedBook : printedBooks) {
            System.out.println(String.format("Printed [id: %d]", printedBook.getId()));
        }
        System.out.println();
        System.out.println("Number of handwritten books: " + handwrittenBooks.size());
        for (Book handwrittenBook : handwrittenBooks) {
            System.out.println(String.format("Handwritten [id: %d]", handwrittenBook.getId()));
        }
        System.out.println();
        System.out.println("Number of borrowed books: " + borrowedBooks.size());
        for (Book borrowedBook : borrowedBooks) {
            System.out.println(String.format("The book [%d] was borrowed by member [%d] at %s", borrowedBook.getId(),
                    borrowedBook.getBorrowedMember().getId(),
                    borrowedBook.getBorrowedDate().toString().substring(0, 10)));
        }
        System.out.println();
        System.out.println("Number of books read in library: " + readingBooks.size());
        for (Book readingBook : readingBooks) {
            System.out.println(String.format("The book [%d] was read in library by member [%d] at %s",
                    readingBook.getId(), readingBook.getBorrowedMember().getId(),
                    readingBook.getBorrowedDate().toString().substring(0, 10)));
        }
    }
}
