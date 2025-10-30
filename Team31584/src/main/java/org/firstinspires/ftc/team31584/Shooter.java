package org.firstinspires.ftc.team31584;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.team31584.abs.EasyPowerTrackingSubsystem;

public class Shooter extends EasyPowerTrackingSubsystem {

    private final DcMotor shooterMotor;

    /**
     * Constructor for the Shooter subsystem
     *
     * @param shooterMotor The motor that controls the shooter wheel
     */
    public Shooter(DcMotor shooterMotor) {
        this.shooterMotor = shooterMotor;
        this.shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Floor the engine
     *
     * @param power Power to apply (positive = shoot outward)
     */
    public void shoot(double power) {
        this.setMekSpeed(power);
        shooterMotor.setPower(-power);
    }

    /**
     * Stop the shooter motor.
     */
    public void stop() {
        this.setMekSpeed(0);
        shooterMotor.setPower(0);
    }
}
