package org.firstinspires.ftc.team31584;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.team31584.abs.EasyPowerTrackingSubsystem;

public class Shooter extends EasyPowerTrackingSubsystem {

    private final DcMotorEx shooterMotor;
    private final double CURRENT_LIMIT_AMPS = 200.0; // desired current limit

    /**
     * Constructor for the Shooter subsystem
     *
     * @param shooterMotor The motor that controls the shooter wheel
     */
    public Shooter(DcMotor shooterMotor) {
        this.shooterMotor = (DcMotorEx) shooterMotor;
        this.shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Set current alert
        this.shooterMotor.setCurrentAlert(CURRENT_LIMIT_AMPS, CurrentUnit.AMPS);
        this.shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    /**
     * Floor the engine
     *
     * @param power Power to apply (positive = shoot outward)
     */
    public void shoot(double power) {
        // Stop motor if overcurrent
        if (!shooterMotor.isOverCurrent()) {
            this.setMekSpeed(power);
            shooterMotor.setPower(-power);
        }
    }
    public double getCurrent() {
        return shooterMotor.getCurrent(CurrentUnit.AMPS);
    }
}
