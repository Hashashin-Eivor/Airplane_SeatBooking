/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.w20374581_planemanagement;

/**
 *
 * @author iftekharzaman
 * 
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class W20374581_PlaneManagement {
    private static final Scanner scanner = new Scanner(System.in);
    private static final int[][] seats = new int[4][14];
    private static final Ticket[] ticketList = new Ticket[52];

    public static void main(String[] args) {
        System.out.println("Welcome to the Plane Management application");
        while (true) {
            System.out.println("**************************************");
            System.out.println("*             Menu Options           *");
            System.out.println("**************************************");
            System.out.println("1. Buy a seat");
            System.out.println("2. Cancel a seat");
            System.out.println("3. Find the first available seat");
            System.out.println("4. Show seating plan");
            System.out.println("5. Plane ticket information and total sales");
            System.out.println("6. Search ticket");
            System.out.println("0. Quit");

            System.out.print("Select an option: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    buy_seat();
                    break;
                case 2:
                    cancel_seat();
                    break;
                case 3:
                    find_first_available();
                    break;
                case 4:
                    show_seating_plan();
                    break;
                case 5:
                    print_tickets_info();
                    break;
                case 6:
                    search_ticket();
                    break;
                case 0:
                    System.out.println("Exiting application...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void buy_seat() {
        System.out.print("Enter row letter (A-D): ");
        char rowChar = Character.toUpperCase(scanner.next().charAt(0));

        // Validating row input
        if (rowChar < 'A' || rowChar > 'D') {
            System.out.println("Invalid row selection. Please enter a valid row (A-D).");
            return;
        }

        int rowCapacity = (rowChar == 'B' || rowChar == 'C') ? 12 : 14;

        System.out.print("Enter seat number: ");
        int seatNumber = scanner.nextInt();

        // Validating seat input
        if (seatNumber < 1 || seatNumber > rowCapacity) {
            System.out.println("Invalid seat number. Please enter a valid seat number (1-" + rowCapacity + ").");
            return;
        }

        // Converting row letter to index
        int rowIndex = rowChar - 'A';

        // Check if the seat is available
        if (seats[rowIndex][seatNumber - 1] == 0) {
            // Seat is available, recording it as sold
            seats[rowIndex][seatNumber - 1] = 1;

            // Ticket creation and storage
            Person person = getPersonInfo();
            double price = calculateTicketPrice(seatNumber);
            Ticket ticket = new Ticket(rowChar, seatNumber, price, person);
            addTicket(ticket);
            ticket.save();

            System.out.println("Seat " + rowChar + seatNumber + " has been successfully purchased.");
        } else {
            // Seat is already sold
            System.out.println("Sorry, the seat " + rowChar + seatNumber + " is not available.");
        }
    }

    private static void cancel_seat() {
        System.out.print("Enter row letter (A-D): ");
        char rowChar = Character.toUpperCase(scanner.next().charAt(0));

        // Validating row input
        if (rowChar < 'A' || rowChar > 'D') {
            System.out.println("Invalid row selection. Please enter a valid row (A-D).");
            return;
        }

        int rowCapacity = (rowChar == 'B' || rowChar == 'C') ? 12 : 14;

        System.out.print("Enter seat number: ");
        int seatNumber = scanner.nextInt();

        // Validating seat input
        if (seatNumber < 1 || seatNumber > rowCapacity) {
            System.out.println("Invalid seat number. Please enter a valid seat number (1-" + rowCapacity + ").");
            return;
        }

        // Converting row letter to index
        int rowIndex = rowChar - 'A';

        // Checking if the seat is already available
        if (seats[rowIndex][seatNumber - 1] == 0) {
            System.out.println("The seat " + rowChar + seatNumber + " is already available.");
        } else {
            // Seat is sold, make it available
            seats[rowIndex][seatNumber - 1] = 0;

            // Ticket cancellation
            Ticket ticketToRemove = findTicket(rowChar, seatNumber);
            if (ticketToRemove != null) {
                removeTicket(ticketToRemove);
                ticketToRemove.remove();
                System.out.println("Seat " + rowChar + seatNumber + " has been successfully cancelled.");
            } else {
                System.out.println("Ticket not found for seat " + rowChar + seatNumber);
            }
        }
    }

    private static Person getPersonInfo() {
        System.out.print("Enter name: ");
        String name = scanner.next();

        System.out.print("Enter surname: ");
        String surname = scanner.next();

        System.out.print("Enter email: ");
        String email = scanner.next();

        return new Person(name, surname, email);
    }

    private static Ticket findTicket(char rowChar, int seatNumber) {
        for (Ticket ticket : ticketList) {
            if (ticket != null && ticket.getRow() == rowChar && ticket.getSeat() == seatNumber) {
                return ticket;
            }
        }
        return null;
    }

    private static void addTicket(Ticket ticket) {
        for (int i = 0; i < ticketList.length; i++) {
            if (ticketList[i] == null) {
                ticketList[i] = ticket;
                break;
            }
        }
    }

    private static void removeTicket(Ticket ticket) {
        for (int i = 0; i < ticketList.length; i++) {
            if (ticketList[i] == ticket) {
                ticketList[i] = null;
                break;
            }
        }
    }

    private static void find_first_available() {
        for (char rowChar = 'A'; rowChar <= 'D'; rowChar++) {
            int rowIndex = rowChar - 'A';
            int rowCapacity = (rowChar == 'B' || rowChar == 'C') ? 12 : 14;

            for (int seatNumber = 1; seatNumber <= rowCapacity; seatNumber++) {
                if (seats[rowIndex][seatNumber - 1] == 0) {
                    System.out.println("First available seat: " + rowChar + seatNumber);
                    return;  // Stop searching after finding the first available seat
                }
            }
        }

        // If no available seat is found
        System.out.println("Sorry, No available seats at the moment.");
    }

    private static void show_seating_plan() {
        System.out.println("Seating Plan:");

        for (char rowChar = 'A'; rowChar <= 'D'; rowChar++) {
            int rowIndex = rowChar - 'A';
            int rowCapacity = (rowChar == 'B' || rowChar == 'C') ? 12 : 14;

            // Display row label
            System.out.print(rowChar + " ");

            // Display seats based on the row's capacity
            for (int seatNumber = 1; seatNumber <= rowCapacity; seatNumber++) {
                char seatStatus = (seats[rowIndex][seatNumber - 1] == 0) ? 'O' : 'X';
                System.out.print(" " + seatStatus + " ");   // 0 for the available seats and X for the sold seats.
            }

            System.out.println();  // Moving to the next line for the next row
        }
    }

    private static double calculateTicketPrice(int seatNumber) {
        if (seatNumber >= 1 && seatNumber <= 5) {
            return 200.0;
        } else if (seatNumber >= 6 && seatNumber <= 9) {
            return 150.0;
        } else {
            return 180.0;
        }
    }

    private static void print_tickets_info() {
        boolean isEmpty = true;
        double totalSales = 0.0;
        System.out.println("\nTickets Sold Information:");
        for (Ticket ticket : ticketList) {
            if (ticket != null) {
                ticket.print_tickets_info(); // Printing each ticket's details
                totalSales += ticket.getPrice(); // Sum up the total sales
                isEmpty = false;
            }
        }
        if (isEmpty) {
            System.out.println("No tickets have been sold.");
        } else {
            System.out.printf("Total sales: £%.2f\n", totalSales);
        }
    }

    public static class Person {
        private String name;
        private String surname;
        private String email;

        public Person(String name, String surname, String email) {
            this.name = name;
            this.surname = surname;
            this.email = email;
        }

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class Ticket {
        private char row;
        private int seat;
        private double price;
        private Person person;

        public Ticket(char row, int seat, double price, Person person) {
            this.row = row;
            this.seat = seat;
            this.price = price;
            this.person = person;
        }

        public void save() {
            String fileName = row + Integer.toString(seat) + ".txt";

            try (FileWriter fileWriter = new FileWriter(fileName)) {
                fileWriter.write("Ticket Information:\n");
                fileWriter.write("Row: " + row + "\n");
                fileWriter.write("Seat: " + seat + "\n");
                fileWriter.write("Price: $" + price + "\n");
                fileWriter.write("Person Information:\n");
                fileWriter.write("Name: " + person.getName() + "\n");
                fileWriter.write("Surname: " + person.getSurname() + "\n");
                fileWriter.write("Email: " + person.getEmail() + "\n");

                System.out.println("Ticket information saved to file: " + fileName);
            } catch (IOException e) {
                System.out.println("Error saving ticket information to file: " + fileName);
                e.printStackTrace();
            }
        }

        public void remove() {
            String fileName = row + Integer.toString(seat) + ".txt";
            File file = new File(fileName);
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("Ticket information file deleted: " + fileName);
                } else {
                    System.out.println("Failed to delete ticket information file: " + fileName);
                }
            } else {
                System.out.println("Ticket information file does not exist: " + fileName);
            }
        }

        // Getters and setters
        public char getRow() {
            return row;
        }

        public void setRow(char row) {
            this.row = row;
        }

        public int getSeat() {
            return seat;
        }

        public void setSeat(int seat) {
            this.seat = seat;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public Person getPerson() {
            return person;
        }

        public void setPerson(Person person) {
            this.person = person;
        }

        // Method to print information from Ticket (including Person)
        public void print_tickets_info() {
            System.out.println("Ticket Information:");
            System.out.println("Row: " + row);
            System.out.println("Seat: " + seat);
            System.out.println("Price: $" + price);
            System.out.println("Person Information:");
            System.out.println("Name: " + person.getName());
            System.out.println("Surname: " + person.getSurname());
            System.out.println("Email: " + person.getEmail());
        }
    }

    private static void search_ticket() {
        System.out.print("Enter row letter (A-D): ");
        char rowChar = Character.toUpperCase(scanner.next().charAt(0));

        // Validating row input
        if (rowChar < 'A' || rowChar > 'D') {
            System.out.println("Invalid row selection. Please enter a valid row (A-D).");
            return;
        }

        int rowCapacity = (rowChar == 'B' || rowChar == 'C') ? 12 : 14;

        System.out.print("Enter seat number: ");
        int seatNumber = scanner.nextInt();

        // Validating seat input
        if (seatNumber < 1 || seatNumber > rowCapacity) {
            System.out.println("Invalid seat number. Please enter a valid seat number (1-" + rowCapacity + ").");
            return;
        }

        // Attempting to find the ticket
        Ticket ticket = findTicket(rowChar, seatNumber);
        if (ticket != null) {
            // If ticket is found, print ticket and person information
            ticket.print_tickets_info();
        } else {
            System.out.println("This seat is available.");
        }
    }
}

                                // *****The End***** //