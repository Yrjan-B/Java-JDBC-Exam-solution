import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MenuProgram {
    private final ScrapyardDBService scrapyardDB;

    public MenuProgram() {
        scrapyardDB = new ScrapyardDBService();
    }

    //#Metoder
    //Metode for funksjon og presentasjon av meny
    public void runProgram() {
        System.out.println("You will now get 6 options:");
        Scanner input = new Scanner(System.in);
        int choice = 0;

        while (choice !=6) { //While loop som gir meny valg
            menuProvider();
            choice = Integer.parseInt(input.nextLine());

            switch (choice) {
                case 1 -> addFileToDataBase();
                case 2 -> showAllVehicles();
                case 3 -> showTotalAmountOfFuel();
                case 4 -> showDriveableVehicles();
                case 5 -> showVehiclesOlderThan();
                case 6 -> System.out.println("Bye bye.");
            }
        }
    }

    //#Ekstra metode for å kunne legge til filen til databasen.
    private void addFileToDataBase() {
        ScrapyardDBService dbService = new ScrapyardDBService();
        try {
            System.out.println("Reading information of Scrapyards to database");
            dbService.readScrapyardToDatabase();
            System.out.println("Read without problems, and added");
            System.out.println("------------");

        } catch (SQLException e) {
            System.out.println("Scrapyard information already exists in database.");
            System.out.println("------------");
        }

        try {
            System.out.println("Trying to read vehicles to database.");
            dbService.readVehiclesToDatabase();
            System.out.println("Read without problems, and added");
            System.out.println("------------");

        } catch (SQLException e) {
            System.out.println("Vehicle information already exists in database.");
            System.out.println("------------");
        }
    }

    private void showAllVehicles() {
        System.out.println("Getting all vehicles: ");
        try {
            List<Vehicles> vehicles = scrapyardDB.getAllVehicles(); //Trenger ikke å lage ny liste henter bare inn getAllVehicles som returnerer en liste.
            System.out.println("This is the amount of vehicles in the list: " + vehicles.size());
            for (Vehicles vehicle : vehicles) {
                System.out.println(vehicle);                        //Looper gjennom hver vehicle objekt i listen og printer de ut.
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void showTotalAmountOfFuel() {
        System.out.println("Getting total amount of fuel in fossil fuel cars: ");
        int totalAmountOfFuel = scrapyardDB.getTotalAmountOfFuel(); //Forteller totalAmountOfFuel at verdien den skal ha er det samme som blir returnert av metoden getTotalAmountOfFuel().
        System.out.printf("Total fuel is: %d. \n", totalAmountOfFuel);
        System.out.println("-------");
    }

    private void showDriveableVehicles() {
        System.out.println("Getting all driveable vehicles: ");
        try {
            List<Vehicles> vehicles = scrapyardDB.getAllDriveableVehicles(); //Trenger ikke å lage ny liste henter bare inn getAllVehicles som returnerer en liste.
            System.out.println("This is the amount of driveable vehicles in the list: " + vehicles.size());
            for (Vehicles vehicle : vehicles) {
                System.out.println(vehicle);                                 //Looper gjennom hver vehicle objekt i listen og printer de ut.
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void showVehiclesOlderThan() {
        System.out.println("Please input a year with this format: example(2010)");
        Scanner input = new Scanner(System.in);             //Lager en ny scanner
        int userInput = Integer.parseInt(input.nextLine()); //Gir den verdien av brukeren sin input
        List<Vehicles> vehicles = scrapyardDB.getVehiclesByYear(userInput); //Henter metoden fra ScrapyardDBService og sier at parameteren sin verdi skal være brukeren sin unput i tall verdi.
        System.out.println("Amount of vehicles found older than inputted age is: " + vehicles.size());
        for (Vehicles vehicle : vehicles) {
            System.out.println(vehicle);
        }

    }

    //#Metode kunn for presentasjon av strenger altså menyen sin tekst.
    private void menuProvider() {
        System.out.println("1: Add file to dataBase");
        System.out.println("2: See info of all vehicles");
        System.out.println("3: See information on the total amount of fuel in the fossil-fuel cars.");
        System.out.println("4: See information about all driveable vehicles");
        //5: Bli ekstra funksjon.
        System.out.println("5: See information of alle vehicles with a model older than year: ");
        System.out.println("6: Quit");
    }
}
