import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//# Klasse som koble databasen og java programmet, metoder som snakker direkte med databasen skjer her.
public class ScrapyardDBService {

    //#SQL Konstanter, for å legge inn i databasen:
    private static final String INSERT_SCRAPYARD_VALUES_SQL = "INSERT INTO Scrapyard (ScrapyardID, Name, Address, PhoneNumber) VALUES (?, ?, ?, ?)";

    private static final String INSERT_ELECTRICCAR_VALUES_SQL = "INSERT INTO ElectricCar (VehicleID, brand, model, yearModel, registrationNumber, chassisNumber, driveable, numberOfSellableWheels, scrapYardID, batteryCapacity, chargeLevel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_FOSSILCAR_VALUES_SQL = "INSERT INTO FossilCar (VehicleID, brand, model, yearModel, registrationNumber, chassisNumber, driveable, numberOfSellableWheels, scrapYardID, fuelType, fuelAmount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_MOTORCYCLE_VALUES_SQL = "INSERT INTO Motorcycle (VehicleID, brand, model, yearModel, registrationNumber, chassisNumber, driveable, numberOfSellableWheels, scrapYardID, hasSideCar, engineCapacity, isModified, numberOfWheels) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    //#SQL Konstanter for å hente ut fra databasen:
    private static final String SHOW_ALL_ELECTRICCARS_SQL = "SELECT * FROM ElectricCar";
    private static final String SHOW_ALL_FOSSILCAR_SQL = "SELECT * FROM FossilCar";
    private static final String SHOW_ALL_MOTORCYCLE_SQL = "SELECT * FROM Motorcycle";
    private static final String SHOW_AMOUNT_OF_FUEL_SQL = "Select sum(FuelAmount) from FossilCar";

    private static final String SHOW_DRIVABLE_ELECTRICCAR_SQL = "SELECT * FROM ElectricCar WHERE driveable = true";
    private static final String SHOW_DRIVABLE_FOSSILCAR_SQL = "SELECT * FROM FossilCar WHERE driveable = true";
    private static final String SHOW_DRIVABLE_MOTORCYCLE_SQL = "SELECT * FROM Motorcycle WHERE driveable = true";

    private static final String SHOW_ALL_ELECTRICCARS_BY_YEAR_SQL = "SELECT * FROM ElectricCar where yearModel > ?";
    private static final String SHOW_ALL_FOSSILCARS_BY_YEAR_SQL = "SELECT * FROM FossilCar where yearModel > ?";
    private static final String SHOW_ALL_MOTORCYCLES_BY_YEAR_SQL = "SELECT * FROM Motorcycle where yearModel > ?";

    private final MysqlDataSource scrapyardDB;

    //#Metoder for JDBC
    //Metode for å koble databsen til java.
    public ScrapyardDBService() {
        scrapyardDB = new MysqlDataSource();
        scrapyardDB.setServerName(PropertiesProvider.PROPERTIES.getProperty("host"));
        scrapyardDB.setPortNumber(Integer.parseInt(PropertiesProvider.PROPERTIES.getProperty("port")));
        scrapyardDB.setDatabaseName(PropertiesProvider.PROPERTIES.getProperty("db_name"));
        scrapyardDB.setUser(PropertiesProvider.PROPERTIES.getProperty("username"));
        scrapyardDB.setPassword(PropertiesProvider.PROPERTIES.getProperty("pwd"));
    }

    //#Metode for å lese inn info om scrapyard til databasen. Det er ikke nødvendig å returnere noe i denne metoden.
    public void readScrapyardToDatabase() throws SQLException{ //Kaster en SQL exception her for den vil jeg hondtere i main.
        File file = new File("vehicles.txt");
        try (Scanner fileInput = new Scanner(file);
            Connection con = scrapyardDB.getConnection();
            PreparedStatement statement = con.prepareStatement(INSERT_SCRAPYARD_VALUES_SQL);
        ) {
           fileInput.nextLine(); //Skipper første linjen da det kunn forteller hvor mange scrapyards som finnes.

           while (fileInput.hasNextLine()) { //Så lenge filen har flere linjer å lese kjør kodne i blokken.
               String check = fileInput.nextLine();

               if (check.equals("26")) {
                   break;
               } /* Denne skjekker linjer til første linje av vehicles (26) for å så avslutte loopen. Kunne istedet
                   også ha gjort dette med en for loop eks: for (int i = 0; i < 3; i++).*/

               int scrapyardID = Integer.parseInt(check); //Hver gang det er en int som leses pareser jeg den. Må parse check siden det er første linjen som leses.
               String name = fileInput.nextLine();
               String adress = fileInput.nextLine();
               String phoneNumber = fileInput.nextLine();
               fileInput.nextLine(); //Leser "---"

               // Setter verdier for de ? på spørringen og executer til slutt.
               statement.setInt(1, scrapyardID);
               statement.setString(2, name);
               statement.setString(3, adress);
               statement.setString(4, phoneNumber);
               statement.executeUpdate();
           }
        } catch (FileNotFoundException e) {
            System.out.println("Couldnt read from file");
            throw new RuntimeException(e);
        }
    }

    //#Metode for å lese vehicles inn i databasen, trenger kunn en metode da jeg benytter arv.

    //Metoden kan være en void da det ikke er nødevndig å returnere noe, men jeg velger likevel å returnere en liste
    //Slik at hvis jeg skal ha en liste av alle kjøretøy senere så blir det enklere å hente.
    public void readVehiclesToDatabase() throws SQLException{
        File file = new File("vehicles.txt"); //Leser filen
        try (Scanner fileInput = new Scanner(file);
             Connection con = scrapyardDB.getConnection();
             //Tre ulike statements til hver subklasse.
             PreparedStatement electricCarStatement = con.prepareStatement(INSERT_ELECTRICCAR_VALUES_SQL);
             PreparedStatement fossilCarStatement = con.prepareStatement(INSERT_FOSSILCAR_VALUES_SQL);
             PreparedStatement motorcycleStatement = con.prepareStatement(INSERT_MOTORCYCLE_VALUES_SQL);

        ) {
            //While loop som hopper over Scrapyards
            while (fileInput.hasNextLine()) {
                String check = fileInput.nextLine();

                if (check.equals("26")) {
                    break;
                }
            }

            //Loop som faktisk leser kjøretøy
            while (fileInput.hasNextLine()) {
                //"Arvelige/Globale variabler" først.
                int vehicleID = Integer.parseInt(fileInput.nextLine());
                int scrapYardID = Integer.parseInt(fileInput.nextLine());

                String vehicleCheck = fileInput.nextLine(); //Leser linjen og smaneligner inn i switchcasen.

                String brand = fileInput.nextLine();
                String model = fileInput.nextLine();
                int yearModel = Integer.parseInt(fileInput.nextLine());
                String registrationNumber = fileInput.nextLine();
                String chassisNumber = fileInput.nextLine();
                boolean driveable = Boolean.parseBoolean(fileInput.nextLine());
                int numberOfSellableWheels = Integer.parseInt(fileInput.nextLine());

                switch (vehicleCheck) { //Switch for å skjekke hvilken type kjøretøy det er for å gi de spesifiske variabler.
                    case "ElectricCar" -> {
                        int batteryCapacity = Integer.parseInt(fileInput.nextLine());
                        int chargeLevel = Integer.parseInt(fileInput.nextLine());
                        fileInput.nextLine(); //Leser "---"

                        //Henter riktig spørring og setter inn riktig verdier spesifikke for typen av kjøretøy.
                        electricCarStatement.setInt(1, vehicleID);
                        electricCarStatement.setString(2, brand);
                        electricCarStatement.setString(3, model);
                        electricCarStatement.setInt(4, yearModel);
                        electricCarStatement.setString(5, registrationNumber);
                        electricCarStatement.setString(6, chassisNumber);
                        electricCarStatement.setBoolean(7, driveable);
                        electricCarStatement.setInt(8, numberOfSellableWheels);
                        electricCarStatement.setInt(9, scrapYardID);
                        electricCarStatement.setInt(10, batteryCapacity);
                        electricCarStatement.setInt(11, chargeLevel);
                        electricCarStatement.executeUpdate();
                    }

                    case "FossilCar" -> {
                        String fuelType = fileInput.nextLine();
                        int fuelAmount = Integer.parseInt(fileInput.nextLine());
                        fileInput.nextLine(); //Leser "---";


                        fossilCarStatement.setInt(1, vehicleID);
                        fossilCarStatement.setString(2, brand);
                        fossilCarStatement.setString(3, model);
                        fossilCarStatement.setInt(4, yearModel);
                        fossilCarStatement.setString(5, registrationNumber);
                        fossilCarStatement.setString(6, chassisNumber);
                        fossilCarStatement.setBoolean(7, driveable);
                        fossilCarStatement.setInt(8, numberOfSellableWheels);
                        fossilCarStatement.setInt(9, scrapYardID);
                        fossilCarStatement.setString(10, fuelType);
                        fossilCarStatement.setInt(11, fuelAmount);
                        fossilCarStatement.executeUpdate();
                    }

                    case "Motorcycle" -> {
                        boolean hasSideCar = Boolean.parseBoolean(fileInput.nextLine());
                        int engineCapacity = Integer.parseInt(fileInput.nextLine());
                        boolean isModified = Boolean.parseBoolean(fileInput.nextLine());
                        int numberOfWheels = Integer.parseInt(fileInput.nextLine());
                        fileInput.nextLine(); //Leser "---"

                        motorcycleStatement.setInt(1, vehicleID);
                        motorcycleStatement.setString(2, brand);
                        motorcycleStatement.setString(3, model);
                        motorcycleStatement.setInt(4, yearModel);
                        motorcycleStatement.setString(5, registrationNumber);
                        motorcycleStatement.setString(6, chassisNumber);
                        motorcycleStatement.setBoolean(7, driveable);
                        motorcycleStatement.setInt(8, numberOfSellableWheels);
                        motorcycleStatement.setInt(9, scrapYardID);
                        motorcycleStatement.setBoolean(10, hasSideCar);
                        motorcycleStatement.setInt(11, engineCapacity);
                        motorcycleStatement.setBoolean(12, isModified);
                        motorcycleStatement.setInt(13, numberOfWheels);
                        motorcycleStatement.executeUpdate();

                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not read file or file was not found");
            throw new RuntimeException(e);
        }
    }

    //#Metoder
    // Denne skal returnere en liste med vehicle objekter dermed bruker jeg List istdenfor void.
    public List<Vehicles> getAllVehicles() throws SQLException{
        List<Vehicles> vehicles = new ArrayList<>();
        try (Connection con = scrapyardDB.getConnection();
             PreparedStatement electricCarStatement = con.prepareStatement(SHOW_ALL_ELECTRICCARS_SQL);
             PreparedStatement fossilCarStatement = con.prepareStatement(SHOW_ALL_FOSSILCAR_SQL);
             PreparedStatement motorcycleCarStatement = con.prepareStatement(SHOW_ALL_MOTORCYCLE_SQL);

             // Utfører spørring og lagrer resultatene for de tre ulike kjøretøy
             ResultSet re = electricCarStatement.executeQuery();
             ResultSet rf = fossilCarStatement.executeQuery();
             ResultSet rm = motorcycleCarStatement.executeQuery();
        ) {
            // Itererer gjennom alle electriccars i resultSettet.
            while (re.next()) {
                Vehicles electricCar = new ElectricCar(
                        re.getInt("vehicleID"),
                        re.getString("brand"),
                        re.getString("model"),
                        re.getInt("yearModel"),
                        re.getString("registrationNumber"),
                        re.getString("chassisNumber"),
                        re.getBoolean("driveable"),
                        re.getInt("numberOfSellableWheels"),
                        re.getInt("scrapYardID"),
                        re.getInt("batteryCapacity"),
                        re.getInt("chargeLevel")
                );
                vehicles.add(electricCar);
            }

            // Itererer gjennom alle fossilcars i resultSettet.
            while (rf.next()) {
                Vehicles fossilCar = new FossilCar(
                        rf.getInt("vehicleID"),
                        rf.getString("brand"),
                        rf.getString("model"),
                        rf.getInt("yearModel"),
                        rf.getString("registrationNumber"),
                        rf.getString("chassisNumber"),
                        rf.getBoolean("driveable"),
                        rf.getInt("numberOfSellableWheels"),
                        rf.getInt("scrapYardID"),
                        rf.getString("fuelType"),
                        rf.getInt("fuelAmount")
                );
                vehicles.add(fossilCar);
            }

            // Itererer gjennom alle motorcycles i resultSettet.
            while (rm.next()) {
                Vehicles motorCycle = new Motorcycle(
                        rm.getInt("vehicleID"),
                        rm.getString("brand"),
                        rm.getString("model"),
                        rm.getInt("yearModel"),
                        rm.getString("registrationNumber"),
                        rm.getString("chassisNumber"),
                        rm.getBoolean("driveable"),
                        rm.getInt("numberOfSellableWheels"),
                        rm.getInt("scrapYardID"),
                        rm.getBoolean("hasSideCar"),
                        rm.getInt("engineCapacity"),
                        rm.getBoolean("isModified"),
                        rm.getInt("numberOfWheels")
                );
                vehicles.add(motorCycle);
            }
        }

        return vehicles;
    }

    //#Metode for å hente inn totale mengeden av fuel i alle kjøretøy
    public int getTotalAmountOfFuel() {
        int totalAmountOfFuel = 0; //Lagrer verdien på fuel.
        try (Connection con = scrapyardDB.getConnection();
             PreparedStatement fuelStatement = con.prepareStatement(SHOW_AMOUNT_OF_FUEL_SQL);
             ResultSet rf = fuelStatement.executeQuery();
        ){
            while (rf.next()) {
                totalAmountOfFuel = rf.getInt(1); //Henter inn første kolonne fra Reultsette som vil være (SUM)
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return totalAmountOfFuel;
    }

    //#Metode for å få alle kjørbare kjøretøy nesten helt likens som metode getAllVehicles men SQL spørringene er annerledes.
    public List<Vehicles> getAllDriveableVehicles() throws SQLException{
        List<Vehicles> vehicles = new ArrayList<>();
        try (Connection con = scrapyardDB.getConnection();
             PreparedStatement electricCarStatement = con.prepareStatement(SHOW_DRIVABLE_ELECTRICCAR_SQL);
             PreparedStatement fossilCarStatement = con.prepareStatement(SHOW_DRIVABLE_FOSSILCAR_SQL);
             PreparedStatement motorcycleCarStatement = con.prepareStatement(SHOW_DRIVABLE_MOTORCYCLE_SQL);

             // Utfører spørring og lagrer resultatene for de tre ulike kjøretøy
             ResultSet re = electricCarStatement.executeQuery();
             ResultSet rf = fossilCarStatement.executeQuery();
             ResultSet rm = motorcycleCarStatement.executeQuery();
        ) {
            // Itererer gjennom alle electriccars i resultSettet.
            while (re.next()) {
                Vehicles electricCar = new ElectricCar(
                        re.getInt("vehicleID"),
                        re.getString("brand"),
                        re.getString("model"),
                        re.getInt("yearModel"),
                        re.getString("registrationNumber"),
                        re.getString("chassisNumber"),
                        re.getBoolean("driveable"),
                        re.getInt("numberOfSellableWheels"),
                        re.getInt("scrapYardID"),
                        re.getInt("batteryCapacity"),
                        re.getInt("chargeLevel")
                );
                vehicles.add(electricCar);
            }

            // Itererer gjennom alle fossilcars i resultSettet.
            while (rf.next()) {
                Vehicles fossilCar = new FossilCar(
                        rf.getInt("vehicleID"),
                        rf.getString("brand"),
                        rf.getString("model"),
                        rf.getInt("yearModel"),
                        rf.getString("registrationNumber"),
                        rf.getString("chassisNumber"),
                        rf.getBoolean("driveable"),
                        rf.getInt("numberOfSellableWheels"),
                        rf.getInt("scrapYardID"),
                        rf.getString("fuelType"),
                        rf.getInt("fuelAmount")
                );
                vehicles.add(fossilCar);
            }

            // Itererer gjennom alle motorcycles i resultSettet.
            while (rm.next()) {
                Vehicles motorCycle = new Motorcycle(
                        rm.getInt("vehicleID"),
                        rm.getString("brand"),
                        rm.getString("model"),
                        rm.getInt("yearModel"),
                        rm.getString("registrationNumber"),
                        rm.getString("chassisNumber"),
                        rm.getBoolean("driveable"),
                        rm.getInt("numberOfSellableWheels"),
                        rm.getInt("scrapYardID"),
                        rm.getBoolean("hasSideCar"),
                        rm.getInt("engineCapacity"),
                        rm.getBoolean("isModified"),
                        rm.getInt("numberOfWheels")
                );
                vehicles.add(motorCycle);
            }
        }

        return vehicles;
    }

    public List<Vehicles> getVehiclesByYear(int year) {
        List<Vehicles> vehicles = new ArrayList<>();
        try (Connection con = scrapyardDB.getConnection();
             PreparedStatement electricCarStatement = con.prepareStatement(SHOW_ALL_ELECTRICCARS_BY_YEAR_SQL);
             PreparedStatement fossilCarStatement = con.prepareStatement(SHOW_ALL_FOSSILCARS_BY_YEAR_SQL);
             PreparedStatement motorcycleCarStatement = con.prepareStatement(SHOW_ALL_MOTORCYCLES_BY_YEAR_SQL);
        ) {
            //Setter inn ? verdien av spørringen som verdien av parameteren, da det i prigram blir en user input
            electricCarStatement.setInt(1, year);
            fossilCarStatement.setInt(1, year);
            motorcycleCarStatement.setInt(1, year);

            try (ResultSet re = electricCarStatement.executeQuery();
                 ResultSet rf = fossilCarStatement.executeQuery();
                 ResultSet rm = motorcycleCarStatement.executeQuery();
                 ) {

                // Itererer gjennom alle electriccars i resultSettet.
                while (re.next()) {
                    Vehicles electricCar = new ElectricCar(
                            re.getInt("vehicleID"),
                            re.getString("brand"),
                            re.getString("model"),
                            re.getInt("yearModel"),
                            re.getString("registrationNumber"),
                            re.getString("chassisNumber"),
                            re.getBoolean("driveable"),
                            re.getInt("numberOfSellableWheels"),
                            re.getInt("scrapYardID"),
                            re.getInt("batteryCapacity"),
                            re.getInt("chargeLevel")
                    );
                    vehicles.add(electricCar);
                }

                // Itererer gjennom alle fossilcars i resultSettet.
                while (rf.next()) {
                    Vehicles fossilCar = new FossilCar(
                            rf.getInt("vehicleID"),
                            rf.getString("brand"),
                            rf.getString("model"),
                            rf.getInt("yearModel"),
                            rf.getString("registrationNumber"),
                            rf.getString("chassisNumber"),
                            rf.getBoolean("driveable"),
                            rf.getInt("numberOfSellableWheels"),
                            rf.getInt("scrapYardID"),
                            rf.getString("fuelType"),
                            rf.getInt("fuelAmount")
                    );
                    vehicles.add(fossilCar);
                }

                // Itererer gjennom alle motorcycles i resultSettet.
                while (rm.next()) {
                    Vehicles motorCycle = new Motorcycle(
                            rm.getInt("vehicleID"),
                            rm.getString("brand"),
                            rm.getString("model"),
                            rm.getInt("yearModel"),
                            rm.getString("registrationNumber"),
                            rm.getString("chassisNumber"),
                            rm.getBoolean("driveable"),
                            rm.getInt("numberOfSellableWheels"),
                            rm.getInt("scrapYardID"),
                            rm.getBoolean("hasSideCar"),
                            rm.getInt("engineCapacity"),
                            rm.getBoolean("isModified"),
                            rm.getInt("numberOfWheels")
                    );
                    vehicles.add(motorCycle);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vehicles;
    }
}
