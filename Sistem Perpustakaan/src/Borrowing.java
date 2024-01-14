public class Borrowing {
    String borrowingCode;
    String borrowerName;
    String bookCode;
    String borrowDate;
    int borrowDuration;
    String returnDate;
    boolean hasFine;

    Borrowing(String borrowingCode, String borrowerName, String bookCode, String borrowDate, int borrowDuration) {
        this.borrowingCode = borrowingCode;
        this.borrowerName = borrowerName;
        this.bookCode = bookCode;
        this.borrowDate = borrowDate;
        this.borrowDuration = borrowDuration;
        this.returnDate = "";
        this.hasFine = false;
    }
}