package org.firstinspires.ftc.team31584;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Drivetrain {

    final DcMotor forwardRightMotor;
    final DcMotor backRightMotor;
    final DcMotor forwardLeftMotor;
    final DcMotor backLeftMotor;
    private final IMU imu;

    /**
     * Constructor for the Drivetrain subsystem
     *
     * @param forwardRightMotor Front right motor
     * @param backRightMotor    Back right motor
     * @param forwardLeftMotor  Front left motor
     * @param backLeftMotor     Back left motor
     * @param imu               IMU sensor
     */
    public Drivetrain(DcMotor forwardRightMotor, DcMotor backRightMotor, DcMotor forwardLeftMotor, DcMotor backLeftMotor, IMU imu) {
        this.forwardRightMotor = forwardRightMotor;
        this.backRightMotor = backRightMotor;
        this.forwardLeftMotor = forwardLeftMotor;
        this.backLeftMotor = backLeftMotor;
        this.imu = imu;

        // Set up motors
        this.forwardRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.forwardLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Set up IMU orientation
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection =
                RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection =
                RevHubOrientationOnRobot.UsbFacingDirection.UP;

        RevHubOrientationOnRobot orientationOnRobot =
                new RevHubOrientationOnRobot(logoDirection, usbDirection);

        this.imu.initialize(new IMU.Parameters(orientationOnRobot));
    }

    /**
     * Drive the robot in field-relative mode
     *
     * @param forward Forward/backward motion (positive = forward)
     * @param right   Left/right strafe (positive = right)
     * @param rotate  Rotation (positive = clockwise)
     */
    public void driveFieldRelative(double forward, double right, double rotate) {
        double theta = Math.atan2(forward, right);
        double r = Math.hypot(right, forward);

        // Rotate by the robot’s current heading
        theta = AngleUnit.normalizeRadians(theta +
                imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));

        // Convert back to Cartesian coordinates
        double newForward = r * Math.sin(theta);
        double newRight = r * Math.cos(theta);

        // Drive using robot-relative coordinates
        drive(newForward, newRight, rotate);
    }

    /**
     * Drive using robot-relative coordinates
     *
     * @param forward Forward/backward motion (positive = forward)
     * @param strafe  Left/right motion (positive = right)
     * @param rotate  Rotation (positive = clockwise)
     */
    public void drive(double forward, double strafe, double rotate) {
        forwardRightMotor.setPower(forward + strafe - rotate);
        forwardLeftMotor.setPower(forward - strafe - rotate);
        backRightMotor.setPower(forward - strafe + rotate);
        backLeftMotor.setPower(forward + strafe + rotate);
    }

    /**
     * Stop all drivetrain motors
     */
    public void stop() {
        forwardRightMotor.setPower(0);
        forwardLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        backLeftMotor.setPower(0);
    }
}
