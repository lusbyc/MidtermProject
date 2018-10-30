import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

public class LibraryApp {
	public static void main(String[] args) {

		// initiate scanner
		Scanner scan = new Scanner(System.in);

		// initialize variables
		String directoryFolder = "library";
		String fileName = "library.txt";

		// createDirectory();
		// createFile(directoryFolder, fileName);

		// writeToFile(directoryFolder, fileName);
		ArrayList<Book> books = new ArrayList<>();

		// writeToFile(directoryFolder, fileName);

		System.out.println("Welcome to the Grand Circus Library!");
		System.out.println("What would you like to do today!?");
		int userChoice = 0;

		do {
			System.out.println("1 - See list of books");
			System.out.println("2 - Search by the author");
			System.out.println("3 - Search by the title");
			System.out.println("4 - Return a book");
			System.out.println("5 - Donate a book");
			System.out.println("6 - Exit program");

			userChoice = LabValidator.getInt(scan, "\nEnter menu number:  ", 1, 6);
			if (userChoice == 1) {
				searchByList(scan, readFromFile(directoryFolder, fileName));
			} else if (userChoice == 2) {
				searchForAuthorOfBook(scan, readFromFile(directoryFolder, fileName));
			} else if (userChoice == 3) {
				SearchForTitleOfBook(scan, readFromFile(directoryFolder, fileName));
			} else if (userChoice == 4) {
				returnBook(scan, readFromFile(directoryFolder, fileName));
			} else if (userChoice == 5) {
				donateBook(scan, readFromFile(directoryFolder, fileName));
			}
		} while (!(userChoice == 6));
		System.out.println("\nThank you for visiting the Grand Circus Library. Goodbye!");
	}

	public static void donateBook(Scanner scan, ArrayList<Book> bookArrayList) {
		String directoryFolder = "library";
		String fileName = "library.txt";

		String userDonateAuthor = LabValidator.getString(scan, "Enter the author's name: ");
		String userDonateTitle = LabValidator.getString(scan, "Enter the title of the book: ");
		System.out.println(
				"Thank you for your donation! This book has been added to the Grand Circus library.\nWhat would you like to do now?\n");
		Path filePath = Paths.get(directoryFolder, fileName);
		File file = filePath.toFile();
		try {
			String status = "ON SHELF";
			String date = "N/A";
			PrintWriter outW = new PrintWriter(new FileOutputStream(file, true));
			outW.println(userDonateAuthor + "," + userDonateTitle + "," + status + "," + date);
			outW.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void returnBook(Scanner scan, ArrayList<Book> bookArrayList) {
		String userSelection = LabValidator.getString(scan, "Please enter the name of title or author.");
		int available = 0;
		for (Book book : bookArrayList) {
			if (((book.getAuthor().contains(userSelection.toUpperCase())
					|| (book.getTitle().contains(userSelection.toUpperCase()))))
					&& (book.getStatus().contains("CHECKED OUT"))) {
				available = available + 1;
			}
			if (available == 1) {
				System.out.println("We found the following in our database: " + book.getTitle() + " by "
						+ book.getAuthor() + " and its status is " + book.getStatus() + ".");
				writeOldStatusToFile(bookArrayList, book);
				writeOldDueDateToFile(bookArrayList, book);
				System.out.println("Thank you for returning this book!\n");
				System.out.println("What else would you like to do? ");
				break;
			}
		}
		if (available < 1) {
			System.out.println("Sorry that book is checked out or is not apart of our inventory.");
			System.out.println("\nWhat else would you like to do?");
		}
	}

	public static void writeOldStatusToFile(ArrayList<Book> books, Book book) {
		String directoryFolder = "library";
		String fileName = "library.txt";
		Path filePath = Paths.get(directoryFolder, fileName);
		File file = filePath.toFile();
		book.setStatus("ON SHELF");

		try {
			// the true parameter for the file output stream constructor appends data
			// to the end of the file. False rewrites over the entire file
			PrintWriter outW = new PrintWriter(new FileOutputStream(file));
			for (Book b : books) {
				outW.println(b.getTitle() + "," + b.getAuthor() + "," + b.getStatus() + "," + b.getDueDate());
			}
			outW.close(); // mandatory: this needs to be closed when done or may not write all info
		} catch (FileNotFoundException e) {
			System.out.println("The file was not found.");
		}

	}

	public static void writeOldDueDateToFile(ArrayList<Book> books, Book book) {
		String directoryFolder = "library";
		String fileName = "library.txt";
		Path filePath = Paths.get(directoryFolder, fileName);
		File file = filePath.toFile();
		book.setDueDate("N/A");

		try {
			// the true parameter for the file output stream constructor appends data
			// to the end of the file. False rewrites over the entire file
			PrintWriter outW = new PrintWriter(new FileOutputStream(file));
			for (Book b : books) {
				outW.println(b.getTitle() + "," + b.getAuthor() + "," + b.getStatus() + "," + b.getDueDate());
			}
			outW.close(); // mandatory: this needs to be closed when done or may not write all info
		} catch (FileNotFoundException e) {
			System.out.println("The file was not found.");
		}

	}

	public static void searchForAuthorOfBook(Scanner scan, ArrayList<Book> bookArrayList) {
		LocalDate today = LocalDate.now();
		String isItOnShelf = "ON SHELF";
		String userSelection = LabValidator.getString(scan, "Please enter the name of the author: ");
		int available = 0;
		for (Book book : bookArrayList) {
			if ((book.getAuthor().contains(userSelection.toUpperCase()) && (book.getStatus().contains(isItOnShelf)))) {
				System.out.println("We found the following in our database: " + book.getTitle() + " by "
						+ book.getAuthor() + ". This book is currently " + book.getStatus());
				available = available + 1;
			}
			if (available == 1) {
				String userCheckout = LabValidator.getString(scan,
						"Would you like to checkout this book? (Yes/Y or No/N)");
				if (userCheckout.equalsIgnoreCase("yes") || userCheckout.equalsIgnoreCase("y")) {
					System.out.println("Congrats! You have checked out " + book.getTitle() + " by " + book.getAuthor()
							+ " for 2 weeks.");
					System.out.println("Please return this book by: " + today.plus(14, ChronoUnit.DAYS));
					writeNewStatusToFile(bookArrayList, book);
					writeNewDueDateToFile(bookArrayList, book);
					System.out.println("\nWhat else would you like to do?");
					break;
				} else {
					System.out.println("\nWhat else would you like to do?");
					break;
				}
			}
		}
		if (available < 1) {
			System.out.println("Sorry that book is checked out or is not apart of our inventory.");
			System.out.println("\nWhat else would you like to do?");
		}

	}

	public static void searchByList(Scanner scan, ArrayList<Book> books) {
		String directoryFolder = "library";
		String fileName = "library.txt";

		books = readFromFile(directoryFolder, fileName);
		int counter = 1;
		System.out.printf("%-28s %-20s %-20s %-1s", "\n\tTITLE", "  AUTHOR", "  STATUS", " RETURN DATE\n"); // prints
																											// header
																											// for table
		System.out.printf("%-28s %-20s %-20s %-1s", "\t-----", " ------", " ------", "------------\n");
		for (Book book : books) {
			System.out.println(counter++ + ".\t" + book);
		}
		String user1st = LabValidator.getString(scan, "\nWould you like to check out a book? Y/N  ");
		while (user1st.equalsIgnoreCase("y")) {
			int userChoiceForBookNumber = LabValidator.getInt(scan,
					"Please select the number that corresponds to the book you'd like to check out:  ", 1, 14);
			for (int i = 1; i < books.size() + 1; i++) {
				if (userChoiceForBookNumber == i && books.get(i - 1).getStatus().equals("ON SHELF")) {
					writeNewStatusToFile(books, books.get(i - 1));
					writeNewDueDateToFile(books, books.get(i - 1));
					System.out.println(books.get(i - 1).getTitle() + " is now " + books.get(i - 1).getStatus());
					System.out.println("This book is due back in " + books.get(i - 1).getDueDate() + ".\n");
					System.out.println("Would you like to check out another book? Y/N  ");
					user1st = scan.next();
				} else if (userChoiceForBookNumber == i && books.get(i - 1).getStatus().equals("CHECKED OUT")) {
					System.out.println(
							"We're sorry but that title is already checked out. It is due back to the library in "
									+ books.get(i - 1).getDueDate() + ". Please make another selection.\n");
					if (user1st.equalsIgnoreCase("N")) {
						System.out.println("\nWhat would you like to do? ");
					}
				}
			}
		}
		System.out.println("\nWhat would you like to do next? ");
	}

	public static void SearchForTitleOfBook(Scanner scan, ArrayList<Book> bookArrayList) {
		LocalDate today = LocalDate.now();
		String isItOnShelf = "ON SHELF";
		String userSelection = LabValidator.getString(scan, "Please enter the name of the title: ");
		int available = 0;
		for (Book book : bookArrayList) {
			if ((book.getTitle().contains(userSelection.toUpperCase()) && (book.getStatus().contains(isItOnShelf)))) {
				System.out.println("We found the following in our database: " + book.getTitle() + " by "
						+ book.getAuthor() + ". This book is currently " + book.getStatus());
				available = available + 1;
			}
			if (available == 1) {
				String userCheckout = LabValidator.getString(scan,
						"Would you like to checkout this book? (Yes/Y or No/N) ");
				if (userCheckout.equalsIgnoreCase("yes") || userCheckout.equalsIgnoreCase("y")) {
					System.out.println("Congrats! You have checked out " + book.getTitle() + " by " + book.getAuthor()
							+ " for 2 weeks.");
					System.out.println("Please return this book by: " + today.plus(14, ChronoUnit.DAYS));
					writeNewStatusToFile(bookArrayList, book);
					writeNewDueDateToFile(bookArrayList, book);
					System.out.println("\nWhat else would you like to do?");
					break;
				} else {
					System.out.println("\nWhat else would you like to do?");
					break;
				}
			}
		}
		if (available < 1) {
			System.out.println("Sorry that book is checked out or is not apart of our inventory.");
			System.out.println("\nWhat else would you like to do?");
		}

	}

	public static void createFile(String directoryFolder, String fileName) {
		Path filePath = Paths.get(directoryFolder, fileName);
		if (Files.notExists(filePath)) {
			try {
				Files.createFile(filePath);
				System.out.println("File was created successfully");
			} catch (IOException e) {
				System.out.println("Something went wrong with the file creation");
				e.printStackTrace();
			}
		} else {
		}
	}

	public static void createDirectory() {
		String dirPath = "library";

		Path folder = Paths.get(dirPath);

		if (Files.notExists(folder)) {

			try {// this is an example of a checked exception
				Files.createDirectories(folder);
			} catch (IOException e) {
				System.out.println("Something went wrong with folder creation");
				e.printStackTrace();
			}

		}
	}

	public static void writeToFile(String directoryFolder, String fileName) {
		Path filePath = Paths.get(directoryFolder, fileName);
		File file = filePath.toFile();

		try {
			// the true parameter for the FileOutputStream() constructor
			// appends data to the end of the file
			// false rewrites over the file
			PrintWriter outW = new PrintWriter(new FileOutputStream(file, true));
			outW.close(); // mandatory: this needs to be closed when you are done or it may not write all
							// of your stuff
			// to the file
		} catch (FileNotFoundException e) {
			System.out.println("The file was not found");
			e.printStackTrace();
		}
	}

	public static void writeNewStatusToFile(ArrayList<Book> books, Book book) {
		String directoryFolder = "library";
		String fileName = "library.txt";
		Path filePath = Paths.get(directoryFolder, fileName);
		File file = filePath.toFile();
		book.setStatus("CHECKED OUT");

		try {
			// the true parameter for the file output stream constructor appends data
			// to the end of the file. False rewrites over the entire file
			PrintWriter outW = new PrintWriter(new FileOutputStream(file));
			for (Book b : books) {
				outW.println(b.getTitle() + "," + b.getAuthor() + "," + b.getStatus() + "," + b.getDueDate());
			}
			outW.close(); // mandatory: this needs to be closed when done or may not write all info
		} catch (FileNotFoundException e) {
			System.out.println("The file was not found.");
		}

	}

	public static void writeNewDueDateToFile(ArrayList<Book> books, Book book) {
		String directoryFolder = "library";
		String fileName = "library.txt";
		Path filePath = Paths.get(directoryFolder, fileName);
		File file = filePath.toFile();
		book.setDueDate("2 Weeks");

		try {
			// the true parameter for the file output stream constructor appends data
			// to the end of the file. False rewrites over the entire file
			PrintWriter outW = new PrintWriter(new FileOutputStream(file));
			for (Book b : books) {
				outW.println(b.getTitle() + "," + b.getAuthor() + "," + b.getStatus() + "," + b.getDueDate());
			}
			outW.close(); // mandatory: this needs to be closed when done or may not write all info
		} catch (FileNotFoundException e) {
			System.out.println("The file was not found.");
		}

	}

	public static ArrayList<Book> readFromFile(String diretoryFolder, String fileName) {
		Path filePath = Paths.get(diretoryFolder, fileName);
		File file = filePath.toFile();
		ArrayList<Book> bookList = new ArrayList<>();

		try {
			FileReader fr = new FileReader(file);
			BufferedReader reader = new BufferedReader(fr);

			String line = reader.readLine();
			String[] bookLine = new String[4];

			while (line != null) {
				bookLine = line.split(",");

				Book a = new Book();
				a.setTitle(bookLine[0].toUpperCase()); // added the toUpperCase to ensure that our search method will
														// recognize any case entered by user
				a.setAuthor(bookLine[1].toUpperCase());
				a.setStatus(bookLine[2].toUpperCase());
				a.setDueDate(bookLine[3].toUpperCase());
				bookList.add(a);
				line = reader.readLine();

			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Contact customer service");
			e.printStackTrace();
		}
		return bookList;
	}

}