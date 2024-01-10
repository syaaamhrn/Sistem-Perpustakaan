public class Book {
    String bookCode;
    String title;
    String author;
    String publisher;
    int publicationYear;
    String condition;
    int stock;

    Book(String bookCode, String title, String author, String publisher, int publicationYear, String condition, int stock) {
        this.bookCode = bookCode;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.condition = condition;
        this.stock = stock;
    }
}
