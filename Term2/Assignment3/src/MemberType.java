public enum MemberType {
    Student(7, 2),
    Academic(14, 4);

    public int borrowDays;
    public int maxBooks;

    private MemberType(int borrowDays, int maxBooks) {
        this.borrowDays = borrowDays;
        this.maxBooks = maxBooks;
    }
}
