public class Member {
    private final int id;
    private final MemberType memberType;
    private final int maxBooks;
    private int borrowedBooksCount;

    public Member(int id, MemberType memberType) {
        this.id = id;
        this.memberType = memberType;
        this.maxBooks = memberType.maxBooks;
    }

    public int getId() {
        return id;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public void borrowBook() {
        borrowedBooksCount++;
    }

    public void returnBook() {
        borrowedBooksCount--;
    }

    public boolean canRead(Book book) {
        if (memberType == MemberType.Academic)
            return true;
        else
            return book.getBookType() == BookType.Printed;
    }

    public boolean canBorrow() {
        return borrowedBooksCount < maxBooks;
    }

}
