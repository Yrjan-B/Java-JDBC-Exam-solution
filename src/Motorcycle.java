public class Motorcycle extends Vehicles{

    private boolean hasSideCar;
    private int engineCapacity;
    private boolean isModified;
    private int numberOfWheels;

    public Motorcycle(int vehicleID, String brand, String model, int yearModel, String registrationNumber, String chassisNumber, boolean driveable, int numberOfSellableWheels, int scrapYardID, boolean hasSideCar, int engineCapacity, boolean isModified, int numberOfWheels) {
        super(vehicleID, brand, model, yearModel, registrationNumber, chassisNumber, driveable, numberOfSellableWheels, scrapYardID);
        this.hasSideCar = hasSideCar;
        this.engineCapacity = engineCapacity;
        this.isModified = isModified;
        this.numberOfWheels = numberOfWheels;
    }

    public boolean isHasSideCar() {
        return hasSideCar;
    }

    public int getEngineCapacity() {
        return engineCapacity;
    }

    public boolean isModified() {
        return isModified;
    }

    public int getNumberOfWheels() {
        return numberOfWheels;
    }

    @Override
    public String toString() {
        return "Motorcycle{" +
                "hasSideCar=" + hasSideCar +
                ", engineCapacity=" + engineCapacity +
                ", isModified=" + isModified +
                ", numberOfWheels=" + numberOfWheels +
                "} " + super.toString();
    }
}
