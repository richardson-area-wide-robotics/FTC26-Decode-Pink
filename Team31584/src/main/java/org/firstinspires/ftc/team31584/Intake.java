package org.firstinspires.ftc.team31584;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.team31584.abs.EasyPowerTrackingSubsystem;

public class Intake extends EasyPowerTrackingSubsystem {

    private final CRServo servo0;
    private final DcMotor feederMotor1;
    private final DcMotor feederMotor2;

    /**
     * Constructor for the Intake subsystem
     *
     * @param servo0       The intake servo
     * @param feederMotor1 The first feeder motor
     * @param feederMotor2 The second feeder motor
     */
    public Intake(CRServo servo0, DcMotor feederMotor1, DcMotor feederMotor2) {
        this.servo0 = servo0;
        this.feederMotor1 = feederMotor1;
        this.feederMotor2 = feederMotor2;

        feederMotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        feederMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Intake a ball
     *
     * @param power Power to set the intake motors to (Positive = into robot)
     * @return The current servo power (for telemetry/debug)
     */
    public double intake(double power) {
        this.setMekSpeed(power);

        servo0.setPower(-power);
        feederMotor1.setPower(-power);
        feederMotor2.setPower(-power);
        return servo0.getPower();
    }

    /**
     * Stop the intake
     */
    public void stop() {
        this.setMekSpeed(0);
        servo0.setPower(0);
        feederMotor1.setPower(0);
        feederMotor2.setPower(0);
    }
}
