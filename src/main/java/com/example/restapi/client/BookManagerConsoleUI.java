package com.example.restapi.client;

import com.example.restapi.model.Book;

import java.util.List;
import java.util.Scanner;

public class BookManagerConsoleUI {

    private BookManager bookManager;

    public static void main(String[] args) {
        new BookManagerConsoleUI().start(args);
    }

    public BookManagerConsoleUI() {}

    public BookManagerConsoleUI(BookManager bookManager) {
        this.bookManager = bookManager;
    }

    public void start(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java BookManagerConsoleUI <hostname> <port>");
        } else {
            run(args[0], args[1]);
        }
    }

    public void run(String hostname, String port) {
        if (bookManager == null) {
            this.bookManager = new BookManager(hostname, port);
        }

        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            System.out.println("1. Register Book");
            System.out.println("2. List All Books");
            System.out.println("3. Delete Book");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter book title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter book author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter book ISBN: ");
                    String isbn = scanner.nextLine();
                    Book book = new Book(title, author, isbn);
                    bookManager.registerBook(book);
                    break;
                case 2:
                    List<Book> books = bookManager.getAllBooks();
                    for (Book b : books) {
                        System.out.println("ID: " + b.getId());
                        System.out.println("Title: " + b.getTitle());
                        System.out.println("Author: " + b.getAuthor());
                        System.out.println("ISBN: " + b.getIsbn());
                        System.out.println("---------------------------");
                    }
                    break;
                case 3:
                    System.out.print("Enter book ID to delete: ");
                    String bookId = scanner.nextLine();
                    bookManager.deleteBook(bookId);
                    break;
                case 4:
                    continuar = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}


