import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class LibrarySystem {
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser;
    private static ArrayList<Book> books = new ArrayList<>();
    private static ArrayList<Borrowing> borrowings = new ArrayList<>();

    public static void main(String[] args) {
        showWelcomeMessage();
        showUserTypeMenu();
    }

    private static void showWelcomeMessage() {
        System.out.println("Selamat datang di Perpustakaan XYZ!");
    }

    private static void showUserTypeMenu() {
        while (true) {
            System.out.println("1. Admin");
            System.out.println("2. User");
            System.out.println("3. Exit");
            System.out.print("Pilih opsi: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    loginUser("Admin");
                    break;
                case 2:
                    loginUser("User");
                    break;
                case 3:
                    System.out.println("Terima kasih telah menggunakan Perpustakaan XYZ. Sampai jumpa!");
                    System.exit(0);
                default:
                    System.out.println("Opsi tidak valid. Silakan coba lagi.");
            }
        }
    }

    private static void loginUser(String userType) {
        System.out.println("\n=== Login " + userType + " ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (userType.equals("Admin")) {
            currentUser = new User(username, password);
            showAdminMenu();
        } else if (userType.equals("User")) {
            currentUser = new User(username, password);
            showUserMenu();
        }
    }

    private static void addBook() {
        System.out.println("\n=== Tambah Data Buku ===");
        System.out.print("Kode Buku: ");
        String bookCode = scanner.nextLine();
        System.out.print("Judul Buku: ");
        String title = scanner.nextLine();
        System.out.print("Penulis: ");
        String author = scanner.nextLine();
        System.out.print("Penerbit: ");
        String publisher = scanner.nextLine();
        System.out.print("Tahun Terbit: ");
        int publicationYear = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Kondisi Buku (Baru/Lama): ");
        String condition = scanner.nextLine();
        System.out.print("Stok: ");
        int stock = scanner.nextInt();

        Book newBook = new Book(bookCode, title, author, publisher, publicationYear, condition, stock);
        books.add(newBook);

        System.out.println("Data buku berhasil ditambahkan!");
    }

    private static void removeBook() {
        System.out.println("\n=== Hapus Data Buku ===");
        System.out.print("Masukkan Kode Buku yang akan dihapus: ");
        String bookCode = scanner.nextLine();

        boolean found = false;
        for (Book book : books) {
            if (book.bookCode.equals(bookCode)) {
                books.remove(book);
                found = true;
                break;
            }
        }

        if (found) {
            System.out.println("Data buku berhasil dihapus!");
        } else {
            System.out.println("Buku dengan kode " + bookCode + " tidak ditemukan.");
        }
    }

    private static void showBookList() {
        System.out.println("\n=== Daftar Buku ===");
        System.out.printf("%-15s %-30s %-20s %-20s %-10s %-10s %-5s\n",
                "Kode Buku", "Judul", "Penulis", "Penerbit", "Tahun", "Kondisi", "Stok");

        for (Book book : books) {
            System.out.printf("%-15s %-30s %-20s %-20s %-10d %-10s %-5d\n",
                    book.bookCode, book.title, book.author, book.publisher,
                    book.publicationYear, book.condition, book.stock);
        }
    }

    private static void viewUserBorrowingHistory() {
        System.out.println("\n=== Histori Peminjaman User ===");
        System.out.print("Masukkan username user: ");
        String username = scanner.nextLine();

        boolean userFound = false;
        for (Borrowing borrowing : borrowings) {
            if (borrowing.borrowerName.equals(username)) {
                System.out.println("\nHistori Peminjaman untuk User " + username + ":");
                System.out.printf("%-15s %-20s %-15s %-15s %-15s %-10s\n",
                        "Kode Peminjaman", "Kode Buku", "Tanggal Pinjam", "Waktu Pinjam", "Tanggal Kembali", "Denda");
                System.out.printf("%-15s %-20s %-15s %-15s %-15s %-10s\n",
                        borrowing.borrowingCode, borrowing.bookCode, borrowing.borrowDate,
                        borrowing.borrowDuration + " hari", borrowing.returnDate,
                        borrowing.hasFine ? "Rp 5.000" : "Tidak ada denda");
                userFound = true;
                break;
            }
        }

        if (!userFound) {
            System.out.println("User dengan username " + username + " tidak memiliki histori peminjaman.");
        }
    }

    private static void borrowBook() {
        System.out.println("\n=== Peminjaman Buku ===");
        System.out.print("Masukkan Kode Peminjaman: ");
        String borrowingCode = scanner.nextLine();
        System.out.print("Masukkan Kode Buku yang akan dipinjam: ");
        String bookCode = scanner.nextLine();
        System.out.print("Masukkan Tanggal Peminjaman (dd-mm-yyyy): ");
        String borrowDate = scanner.nextLine();
        System.out.print("Masukkan berapa hari buku akan dipinjam: ");
        int borrowDuration = scanner.nextInt();
        scanner.nextLine(); // consume newline

        for (Book book : books) {
            if (book.bookCode.equals(bookCode) && book.stock > 0) {
                String borrowerName = currentUser.username;

                Borrowing newBorrowing = new Borrowing(borrowingCode, borrowerName, bookCode, borrowDate, borrowDuration);
                borrowings.add(newBorrowing);

                book.stock--;
                System.out.println("Buku berhasil dipinjam!");
                return;
            }
        }
        System.out.println("Buku dengan kode " + bookCode + " tidak tersedia atau stok habis.");
    }

    private static void returnBook() {
        System.out.println("\n=== Pengembalian Buku ===");
        System.out.print("Masukkan Kode Peminjaman: ");
        String borrowingCode = scanner.nextLine();

        boolean borrowingFound = false;
        for (Borrowing borrowing : borrowings) {
            if (borrowing.borrowingCode.equals(borrowingCode)) {
                borrowingFound = true;

                System.out.print("Masukkan Tanggal Pengembalian (dd-mm-yyyy): ");
                String returnDate = scanner.nextLine();

                borrowing.returnDate = returnDate;

                int borrowedDays = borrowing.borrowDuration; // Menggunakan durasi peminjaman langsung

                String[] dateParts = returnDate.split("-");
                int returnedDays = Integer.parseInt(dateParts[0]); // Mengambil hari dari tanggal pengembalian

                for (Book book : books) {
                    if (book.bookCode.equals(borrowing.bookCode)) {
                        book.stock++; // Tambah stok saat pengembalian
                        break;
                    }
                }
                System.out.println("Buku berhasil dikembalikan!");
                break;
            }
        }

        if (!borrowingFound) {
            System.out.println("Peminjaman dengan kode " + borrowingCode + " tidak ditemukan.");
        }
    }

    private static void searchBook() {
        System.out.println("\n=== Pencarian Buku ===");
        System.out.println("1. Berdasarkan Judul");
        System.out.println("2. Berdasarkan Penulis");
        System.out.println("3. Berdasarkan Tahun Terbit");
        System.out.print("Pilih kriteria pencarian: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                System.out.print("Masukkan judul buku: ");
                String title = scanner.nextLine();
                searchBooksByTitle(title);
                break;
            case 2:
                System.out.print("Masukkan nama penulis: ");
                String author = scanner.nextLine();
                searchBooksByAuthor(author);
                break;
            case 3:
                System.out.print("Masukkan tahun terbit: ");
                int publicationYear = scanner.nextInt();
                scanner.nextLine(); // consume newline
                searchBooksByPublicationYear(publicationYear);
                break;
            default:
                System.out.println("Opsi tidak valid.");
        }
    }

    private static void searchBooksByTitle(String title) {
        System.out.println("\n=== Pencarian Berdasarkan Judul: " + title + " ===");
        System.out.printf("%-15s %-30s %-20s %-20s %-10s %-10s %-5s\n",
                "Kode Buku", "Judul", "Penulis", "Penerbit", "Tahun", "Kondisi", "Stok");

        for (Book book : books) {
            if (book.title.toLowerCase().contains(title.toLowerCase())) {
                System.out.printf("%-15s %-30s %-20s %-20s %-10d %-10s %-5d\n",
                        book.bookCode, book.title, book.author, book.publisher,
                        book.publicationYear, book.condition, book.stock);
            }
        }
    }

    private static void searchBooksByAuthor(String author) {
        System.out.println("\n=== Pencarian Berdasarkan Penulis: " + author + " ===");
        System.out.printf("%-15s %-30s %-20s %-20s %-10s %-10s %-5s\n",
                "Kode Buku", "Judul", "Penulis", "Penerbit", "Tahun", "Kondisi", "Stok");

        for (Book book : books) {
            if (book.author.toLowerCase().contains(author.toLowerCase())) {
                System.out.printf("%-15s %-30s %-20s %-20s %-10d %-10s %-5d\n",
                        book.bookCode, book.title, book.author, book.publisher,
                        book.publicationYear, book.condition, book.stock);
            }
        }
    }

    private static void searchBooksByPublicationYear(int publicationYear) {
        System.out.println("\n=== Pencarian Berdasarkan Tahun Terbit: " + publicationYear + " ===");
        System.out.printf("%-15s %-30s %-20s %-20s %-10s %-10s %-5s\n",
                "Kode Buku", "Judul", "Penulis", "Penerbit", "Tahun", "Kondisi", "Stok");

        for (Book book : books) {
            if (book.publicationYear == publicationYear) {
                System.out.printf("%-15s %-30s %-20s %-20s %-10d %-10s %-5d\n",
                        book.bookCode, book.title, book.author, book.publisher,
                        book.publicationYear, book.condition, book.stock);
            }
        }
    }

    private static void showAdminMenu() {
        while (true) {
            System.out.println("\n=== Menu Admin ===");
            System.out.println("1. Tambah Data Buku");
            System.out.println("2. Hapus Data Buku");
            System.out.println("3. Tampilkan Daftar Buku");
            System.out.println("4. Lihat Histori Peminjaman User");
            System.out.println("5. Logout");
            System.out.print("Pilih opsi: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    removeBook();
                    break;
                case 3:
                    showBookList();
                    break;
                case 4:
                    viewUserBorrowingHistory();
                    break;
                case 5:
                    currentUser = null;
                    System.out.println("Logout berhasil!");
                    showUserTypeMenu();
                default:
                    System.out.println("Opsi tidak valid. Silakan coba lagi.");
            }
        }
    }

    private static void showUserMenu() {
        while (true) {
            System.out.println("\n=== Menu User ===");
            System.out.println("1. Peminjaman Buku");
            System.out.println("2. Pengembalian Buku");
            System.out.println("3. Tampilkan Daftar Buku");
            System.out.println("4. Pencarian Buku");
            System.out.println("5. Logout");
            System.out.print("Pilih opsi: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    borrowBook();
                    break;
                case 2:
                    returnBook();
                    break;
                case 3:
                    showBookList();
                    break;
                case 4:
                    searchBook();
                    break;
                case 5:
                    currentUser = null;
                    System.out.println("Logout berhasil!");
                    showUserTypeMenu();
                default:
                    System.out.println("Opsi tidak valid. Silakan coba lagi.");
            }
        }
    }

}