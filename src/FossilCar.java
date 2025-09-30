public class FossilCar extends Vehicles{

    private String fuelType;
    private int fuelAmount;

    public FossilCar(int vehicleID, String brand, String model, int yearModel, String registrationNumber, String chassisNumber, boolean driveable, int numberOfSellableWheels, int scrapYardID, String fuelType, int fuelAmount) {
        super(vehicleID, brand, model, yearModel, registrationNumber, chassisNumber, driveable, numberOfSellableWheels, scrapYardID);
        this.fuelType = fuelType;
        this.fuelAmount = fuelAmount;
    }

    public String getFuelType() {
        return fuelType;
    }

    public int getFuelAmount() {
        return fuelAmount;
    }

    @Override
    public String toString() {
        return "FossilCar{" +
                "fuelType='" + fuelType + '\'' +
                ", fuelAmount=" + fuelAmount +
                "} " + super.toString();
    }
}
