import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class BookManager {
    private static ArrayList<Book> books = new ArrayList<Book>();
    private static ArrayList<Book> readingBooks = new ArrayList<Book>();
    private static ArrayList<Book> borrowedBooks = new ArrayList<Book>();
    private static ArrayList<Integer> existingIds = new ArrayList<Integer>();

    public static boolean doesBookExist(int id) {
        return existingIds.contains(id);
    }

    public static Book getBookById(int id) {
        if (!doesBookExist(id))
            return null;

        for (Book book : books)
            if (book.getId() == id)
                return book;

        return null;
    }

    public static void handleAddBook(String[] command) {
        if (command.length < 1 || "PH".contains(command[0]) == false || existingIds.size() >= 999999) {
            System.out.println(Messages.GeneralError);
            return;
        }

        int id = existingIds.size() == 0 ? 1 : existingIds.get(existingIds.size() - 1) + 1;
        BookType bookType;
        if (command[0].equals("H"))
            bookType = BookType.Handwritten;
        else
            bookType = BookType.Printed;

        books.add(new Book(id, bookType));
        existingIds.add(id);

        System.out.println(String.format("Created new book: %s [id: %d]", bookType.toString(), id));
    }

    public static void handleBorrowBook(String[] command) {
        OperationInfo op = getOperationInfoFromCommand(command);
        if (op == null) {
            System.out.println(Messages.CannotBorrowBook);
            return;
        }

        if (!op.member.canBorrow()) {
            System.out.println(Messages.BorrowingLimitExceeded);
            return;
        }

        if (!op.book.isBorrowable()) {
            System.out.println(Messages.CannotBorrowBook);
            return;
        }

        if (!op.book.borrowBook(op.member, op.date)) {
            System.out.println(Messages.CannotBorrowBook);
            return;
        }

        borrowedBooks.add(op.book);
        op.member.borrowBook();
        System.out.println(String.format("The book [%d] was borrowed by member [%d] at %s",
                op.bookId, op.memberId, op.date.toString().substring(0, 10)));
    }

    public static void handleReturnBook(String[] command) {
        OperationInfo op = getOperationInfoFromCommand(command);
        if (op == null) {
            System.out.println(Messages.CannotReturnBook);
            return;
        }

        if (!op.book.isBorrowedBy(op.member)) {
            System.out.println(Messages.CannotReturnBook);
            return;
        }

        LocalDateTime deadline = op.book.getDeadline();
        if (!op.book.returnBook()) {
            System.out.println(Messages.CannotReturnBook);
            return;
        }

        int fee = 0;
        if (deadline != null) { // book was borrowed
            borrowedBooks.remove(op.book);
            if (deadline.isBefore(op.date))
                fee = (int) ChronoUnit.DAYS.between(deadline, op.date);
        } else {
            readingBooks.remove(op.book);
        }
        op.member.returnBook();
        System.out.println(String.format("The book [%d] was returned by member [%d] at %s Fee: %d",
                op.bookId, op.memberId,
                op.date.toString().substring(0, 10), fee));
    }

    public static void handleExtendBook(String[] command) {
        OperationInfo op = getOperationInfoFromCommand(command);
        if (op == null) {
            System.out.println(Messages.CannotExtendDeadline);
            return;
        }

        if (op.book.getDeadline().isBefore(op.date)) {
            System.out.println(Messages.CannotExtendDeadline);
            return;
        }

        if (op.book.didExtendDeadlineOnce()) {
            System.out.println(Messages.CannotExtendDeadline);
            return;
        }

        if (!op.book.extendDeadline(op.member)) {
            System.out.println(Messages.CannotReturnBook);
            return;
        }

        System.out.println(String.format("The deadline of book [%d] was extended by member [%d] at %s",
                op.bookId, op.memberId, op.date.toString().substring(0, 10)));
        System.out.println(String.format("New deadline of book [%d] is %s",
                op.bookId, op.book.getDeadline().toString().substring(0, 10)));
    }

    public static void readInLibrary(String[] command) {
        OperationInfo op = getOperationInfoFromCommand(command);
        if (op == null) {
            System.out.println(Messages.CannotReadInLibrary);
            return;
        }

        if (op.book.getBookType() == BookType.Handwritten && op.member.getMemberType() == MemberType.Student) {
            System.out.println(Messages.CannotReadHandwritten);
            return;
        }

        if (!op.book.readInLibrary(op.member, op.date)) {
            System.out.println(Messages.CannotReadInLibrary);
            return;
        }

        readingBooks.add(op.book);
        System.out.println(String.format("The book [%d] was read in library by member [%d] at %s",
                op.bookId, op.memberId, op.date.toString().substring(0, 10)));
    }

    private static OperationInfo getOperationInfoFromCommand(String[] command) {
        if (command.length < 3)
            return null;

        int bookId;
        int memberId;
        LocalDateTime date;
        try {
            bookId = Integer.parseInt(command[0]);
            memberId = Integer.parseInt(command[1]);
            date = LocalDateTime.parse(command[2], Book.dateTimeFormatter);

            if (!doesBookExist(bookId) || !MemberManager.doesMemberExist(memberId))
                throw new Exception();
        } catch (Exception e) {
            return null;
        }

        // book and member are valid because we nullchecked them in the try-catch block
        Book book = getBookById(bookId);
        Member member = MemberManager.getMemberById(memberId);

        return new OperationInfo(book, member, date, bookId, memberId);
    }

    private static class OperationInfo {
        public Book book;
        public int bookId;
        public int memberId;
        public Member member;
        public LocalDateTime date;

        public OperationInfo(Book book, Member member, LocalDateTime date, int bookId, int memberId) {
            this.book = book;
            this.member = member;
            this.date = date;
            this.bookId = bookId;
            this.memberId = memberId;
        }
    }

    public static ArrayList<Book> getPrintedBooks() {
        return new ArrayList<>(
                books.stream().filter(b -> b.getBookType() == BookType.Printed).collect(Collectors.toList()));
    }

    public static ArrayList<Book> getHandwrittenBooks() {
        return new ArrayList<>(
                books.stream().filter(b -> b.getBookType() == BookType.Handwritten).collect(Collectors.toList()));
    }

    public static ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public static ArrayList<Book> getReadingBooks() {
        return readingBooks;
    }
}
