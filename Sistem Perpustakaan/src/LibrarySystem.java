import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

public class LibrarySystem {
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser;
    private static ArrayList<Book> books = new ArrayList<>();
    private static ArrayList<Borrowing> borrowings = new ArrayList<>();

    public static void main(String[] args) {
        menu();
    }
    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        ArrayList<User> users = User.loadUserData();
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("- PERPUSTAKAAN NUSANTARA -");
            System.out.println("1. Registrasi");
            System.out.println("2. Login");
            System.out.println("3. Logout");
            System.out.print("Pilih menu: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Membuang karakter newline dari buffer
            System.out.println();

            switch (choice) {
                case 1:
                    // Registrasi
                    System.out.println("Registrasi:");
                    System.out.print("Email: ");
                    String email = scanner.nextLine();

                    boolean emailExists = users.stream().anyMatch(u -> u.getEmail().equals(email));
                    if (emailExists) {
                        System.out.println("Email telah digunakan! Silahkan gunakan email lain.");
                        break;
                    }

                    System.out.print("Nama Lengkap: ");
                    String fullName = scanner.nextLine();
                    System.out.print("Tanggal Lahir: ");
                    String birthDate = scanner.nextLine();
                    System.out.print("Alamat: ");
                    String address = scanner.nextLine();
                    System.out.print("Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Password: ");
                    String password = scanner.nextLine();

                    System.out.print("Apakah Anda ingin mendaftar sebagai admin? (ya/tidak): ");
                    String isAdminInput = scanner.nextLine();
                    boolean isAdmin = isAdminInput.equalsIgnoreCase("ya");
                    System.out.println();

                    // Membuat objek User baru
                    User newUser = new User(email, fullName, birthDate, address, username, password, isAdmin);
                    users.add(newUser);
                    User.saveUserData(users);
                    break;
                case 2:
                    // Login
                    System.out.println("\nLogin:");
                    System.out.print("Email/Username: ");
                    String loginInput = scanner.nextLine();
                    System.out.print("Password: ");
                    String passwordInput = scanner.nextLine();

                    // Memeriksa keberhasilan login
                    boolean isLoggedIn = false;
                    for (User user : users) {
                        if ((user.getEmail().equals(loginInput) || user.getUsername().equals(loginInput)) && user.getPassword().equals(passwordInput)) {
                            isLoggedIn = true;
                            System.out.println("\nLogin berhasil!");
                            // Loading animation
                            System.out.print("\t Loading Please Wait");
                            try {
                                for (int i = 0; i < 3; i++) {
                                    Thread.sleep(1000);
                                    System.out.print(".");
                                    Thread.sleep(1000);
                                    System.out.print(".");
                                    Thread.sleep(1000);
                                    System.out.print("\b");
                                }
                                System.out.println();
                                // Clear screen (for simplicity, assuming it runs in a terminal)
                                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                                System.out.println("\t Loading completed\n");
                                System.out.flush();
                            } catch (Exception e) {
                                System.out.println("An error occurred: " + e.getMessage());
                            }

                            // Text display
                            String[] text = {"", "", ""};
                            for (String word : text) {
                                System.out.print(word + " ");
                                try {
                                    Thread.sleep(100); // Menunggu 0.1 detik
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (user.isAdmin()) {
                                showAdminMenu();
                            } else {
                                showUserMenu();
                            }
                            break;
                        }
                    }

                    if (!isLoggedIn) {
                        System.out.println("\nLogin gagal! Silahkan coba lagi.");

                        // Meminta pengguna untuk menginputkan kembali
                        boolean loginAttempt = false;
                        while (!loginAttempt) {
                            System.out.print("Email/Username: ");
                            String retryLoginInput = scanner.nextLine();
                            System.out.print("Password: ");
                            String retryPasswordInput = scanner.nextLine();

                            for (User user : users) {
                                if ((user.getEmail().equals(retryLoginInput) || user.getUsername().equals(retryLoginInput)) && user.getPassword().equals(retryPasswordInput)) {
                                    loginAttempt = true;
                                    System.out.println("\nLogin berhasil!");
                                    if (user.isAdmin()) {
                                        showAdminMenu();
                                    } else {
                                        showUserMenu();
                                    }
                                    break;
                                }
                            }

                            if (!loginAttempt) {
                                System.out.println("\nLogin gagal! Silahkan coba lagi.");
                            }
                        }
                    }
                    break;
                case 3:
                    System.out.println("Terimakasih dan sampai jumpa lagi!");
                    System.out.println();
                    System.out.print("-INFORMASI LEBIH LANJUT-");
                    System.out.println("WHATSAPP    : 082187048678");
                    System.out.println("INSTAGRAM   : @perpustakaannusantara_official");
                    System.out.println("TWITTER     : @perpustakaan_nasional");
                    System.out.println("TIKTOK      : @perpustakaannasional");

                    User.saveUserData(users);
                    isRunning = false;
                    System.exit(0);
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void addBook() {
        System.out.println("\n=== Tambah Data Buku ===");
        System.out.print("Kode Buku: ");
        String bookCode = scanner.nextLine();

        // Check if the book code already exists
        boolean isBookCodeExists = books.stream().anyMatch(b -> b.bookCode.equals(bookCode));
        if (isBookCodeExists) {
            System.out.println("Kode buku telah ada! Silahkan inputkan kode buku baru.");
            return;
        }

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
        System.out.println();
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
            System.out.println();
        } else {
            System.out.println("Buku dengan kode " + bookCode + " tidak ditemukan.");
        }
    }

    private static void showBookList() {
        System.out.println("\n=== Daftar Buku ===");
        System.out.printf("%-15s %-30s %-30s %-30s %-15s %-10s %-5s\n",
                "Kode Buku", "Judul", "Penulis", "Penerbit", "Tahun", "Kondisi", "Stok");

        for (Book book : books) {
            System.out.printf("%-15s %-30s %-30s %-30s %-15d %-10s %-5d\n",
                    book.bookCode, book.title, book.author, book.publisher,
                    book.publicationYear, book.condition, book.stock);
            System.out.println();
        }
    }

    private static void viewUserBorrowingHistory() {
        System.out.println("\n=== Histori Peminjaman ===");
        System.out.print("Masukkan Nama Peminjam: ");
        String borrowerName = scanner.nextLine();

        boolean userFound = false;
        for (Borrowing borrowing : borrowings) {
            if (borrowing.borrowerName.equals(borrowerName)) {
                System.out.println("\nHistori Peminjaman untuk User " + borrowerName + ":");
                System.out.printf("%-15s %-20s %-15s %-15s %-15s\n",
                        "Kode Peminjaman", "Kode Buku", "Tanggal Pinjam", "Waktu Pinjam", "Tanggal Kembali");
                System.out.printf("%-15s %-20s %-15s %-15s %-15s\n",
                        borrowing.borrowingCode, borrowing.bookCode, borrowing.borrowDate,
                        borrowing.borrowDuration + " hari", borrowing.returnDate);
                System.out.println();
                userFound = true;
                break;
            }
        }

        if (!userFound) {
            System.out.println("Pengguna bernama " + borrowerName + " tidak memiliki histori peminjaman.");
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
                System.out.print("Masukkan Nama Peminjam: ");
                String borrowerName = scanner.nextLine();

                Borrowing newBorrowing = new Borrowing(borrowingCode, borrowerName, bookCode, borrowDate, borrowDuration);
                borrowings.add(newBorrowing);

                book.stock--;
                System.out.println("Buku berhasil dipinjam!");
                System.out.println();
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
                System.out.println();
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
                System.out.println();
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
                System.out.println();
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
                System.out.println();
            }
        }
    }

    private static void showAdminMenu() {
        while (true) {
            System.out.println("Selamat datang Admin!");
            System.out.println("\n=== Menu Admin ===");
            System.out.println("1. Tambah Data Buku");
            System.out.println("2. Hapus Data Buku");
            System.out.println("3. Tampilkan Daftar Buku");
            System.out.println("4. Lihat Histori Peminjaman");
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
                    menu();
                default:
                    System.out.println("Opsi tidak valid. Silakan coba lagi.");
            }
        }
    }

    private static void showUserMenu() {
        while (true) {
            System.out.println("Selamat datang di Perpustakaan Nusantara!");
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
                    menu();
                default:
                    System.out.println("Opsi tidak valid. Silakan coba lagi.");
            }
        }
    }
}