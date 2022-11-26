package me.omega;

public class LoggedObject {

    private final double baseline;
    private final double allowedDeviation;

    protected LoggedObject(double baseline, double allowedDeviation) {
        this.baseline = baseline;
        this.allowedDeviation = allowedDeviation;
    }

    public double getBaseline() {
        return baseline;
    }

    public double getAllowedDeviation() {
        return allowedDeviation;
    }

}
