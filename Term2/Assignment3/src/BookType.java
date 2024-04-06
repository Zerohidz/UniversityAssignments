public enum BookType {
    Printed(true),
    Handwritten(false);

    public boolean isBorrowable;

    private BookType(boolean isBorrowable) {
        this.isBorrowable = isBorrowable;
    }
}
