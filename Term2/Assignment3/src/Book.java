import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class Book {
    public static final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    private final boolean isBorrowableBook;
    private final BookType bookType;
    private final int id;
    private boolean extendedDeadlineOnce;
    private LocalDateTime borrowedDate;
    private LocalDateTime deadline;
    private Member borrowedMember;

    public Book(int id, BookType bookType) {
        this.bookType = bookType;
        this.id = id;
        this.isBorrowableBook = bookType.isBorrowable;
    }

    public boolean borrowBook(Member member, LocalDateTime date) {
        if (isBorrowed())
            return false;

        borrowedMember = member;
        borrowedDate = date;
        deadline = date.plusDays(borrowedMember.getMemberType().borrowDays);
        return true;
    }

    public boolean readInLibrary(Member member, LocalDateTime date) {
        if (!isReadable(member))
            return false;

        borrowedMember = member;
        borrowedDate = date;
        return true;
    }

    public boolean returnBook() {
        if (!isBorrowed())
            return false;

        borrowedMember = null;
        borrowedDate = null;
        deadline = null;
        extendedDeadlineOnce = false;
        return true;
    }

    public BookType getBookType() {
        return bookType;
    }

    public int getId() {
        return id;
    }

    public boolean isBorrowableBook() {
        return isBorrowableBook;
    }

    public boolean isBorrowable() {
        return isBorrowableBook && !isBorrowed();
    }

    public boolean isReadable(Member member) {
        return member.canRead(this) && !isBorrowed();
    }

    public boolean isBorrowed() {
        return borrowedMember != null;
    }

    public boolean isBorrowedBy(Member member) {
        return borrowedMember == member;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public Member getBorrowedMember() {
        return borrowedMember;
    }

    public Object getBorrowedDate() {
        return borrowedDate;
    }

    public boolean extendDeadline(Member member) {
        if (!isBorrowedBy(member))
            return false;

        deadline = deadline.plusDays(member.getMemberType().borrowDays);
        extendedDeadlineOnce = true;
        return true;
    }

    public boolean didExtendDeadlineOnce() {
        return extendedDeadlineOnce;
    }
}
