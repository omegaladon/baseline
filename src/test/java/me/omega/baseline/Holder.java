package me.omega.baseline;

public class Holder implements LoggedClass {
//    @Log(name = "holderValue")
    private double holderValue = 25;

    @Log(name = "swerveModule1")
    private SwerveModule swerveModule1 = new SwerveModule();
}
