//# Abstarct klasse som kjøretøy skal arve fra
public abstract class Vehicles {

    //Instans variabler som subklassene MÅ arve.
    private int vehicleID;
    private String brand;
    private String model;
    private int yearModel;
    private String registrationNumber;
    private String chassisNumber;
    private boolean driveable;
    private int numberOfSellableWheels;
    private int scrapYardID;

    //Konstruktør slik at man ikke kan lage Vehicle obejekter yten instans variablene.
    public Vehicles(int vehicleID, String brand, String model, int yearModel, String registrationNumber, String chassisNumber, boolean driveable, int numberOfSellableWheels, int scrapYardID) {
        this.vehicleID = vehicleID;
        this.brand = brand;
        this.model = model;
        this.yearModel = yearModel;
        this.registrationNumber = registrationNumber;
        this.chassisNumber = chassisNumber;
        this.driveable = driveable;
        this.numberOfSellableWheels = numberOfSellableWheels;
        this.scrapYardID = scrapYardID;
    }

    //# Gettere, ikke sikkert disse blir nødvendige men greit å in case.
    public int getVehicleID() {
        return vehicleID;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYearModel() {
        return yearModel;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public boolean isDriveable() {
        return driveable;
    }

    public int getNumberOfSellableWheels() {
        return numberOfSellableWheels;
    }

    public int getScrapYardID() {
        return scrapYardID;
    }

    @Override
    public String toString() {
        return "Vehicles{" +
                "vehicle=" + vehicleID +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", yearModel=" + yearModel +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", chassisNumber='" + chassisNumber + '\'' +
                ", drivable=" + driveable +
                ", numberOfSellableWheels=" + numberOfSellableWheels +
                ", scrapYardID=" + scrapYardID +
                '}';
    }
}
