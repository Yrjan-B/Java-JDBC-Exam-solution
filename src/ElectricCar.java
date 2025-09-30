public class ElectricCar extends Vehicles{

    private int batteryCapacity;
    private int chargeLevel;

    public ElectricCar(int vehicleID, String brand, String model, int yearModel, String registrationNumber, String chassisNumber, boolean driveable, int numberOfSellableWheels, int scrapYardID, int batteryCapacity, int chargeLevel) {
        super(vehicleID, brand, model, yearModel, registrationNumber, chassisNumber, driveable, numberOfSellableWheels, scrapYardID);
        this.batteryCapacity = batteryCapacity;
        this.chargeLevel = chargeLevel;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public int getChargeLevel() {
        return chargeLevel;
    }

    @Override
    public String toString() {
        return "ElectricCar{" +
                "batteryCapacity=" + batteryCapacity +
                ", chargeLevel=" + chargeLevel +
                "} " + super.toString();
    }
}
