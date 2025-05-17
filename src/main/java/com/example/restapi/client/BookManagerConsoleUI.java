package com.example.restapi.client;

import com.example.restapi.model.Book;

import java.util.List;
import java.util.Scanner;

public class BookManagerConsoleUI {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java BookManagerConsoleUI <hostname> <port>");
            System.exit(0);
        }

        String hostname = args[0];
        String port = args[1];

        BookManager bookManager = new BookManager(hostname, port);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Register Book");
            System.out.println("2. List All Books");
            System.out.println("3. Delete Book");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

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
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
