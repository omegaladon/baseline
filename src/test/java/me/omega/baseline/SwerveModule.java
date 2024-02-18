package me.omega.baseline;

import java.util.concurrent.Executors;

public class SwerveModule implements LoggedClass {

    @Log(name = "angleMotor")
    private double angleMotor = 0;

    private double thing = 0;

    public SwerveModule() {
        Executors.newSingleThreadExecutor().submit(() -> {
            while (true) {
                angleMotor += 0.1;
                thing += 0.5;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Log(name = "thing")
    public double getThing() {
        return thing;
    }

}
