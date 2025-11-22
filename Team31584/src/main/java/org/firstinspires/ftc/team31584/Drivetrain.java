package org.firstinspires.ftc.team31584;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Drivetrain {

    public static DcMotor forwardRightMotor;
    public static DcMotor backRightMotor;
    public static DcMotor forwardLeftMotor;
    public static DcMotor backLeftMotor;
    public static IMU imu;

    public static void init(DcMotor fr, DcMotor br, DcMotor fl, DcMotor bl, IMU imuin){
        forwardRightMotor = fr;
        backRightMotor = br;
        forwardLeftMotor = fl;
        backLeftMotor = bl;

        imu = imuin;

        forwardRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        forwardLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // This needs to be changed to match the orientation on your robot
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection =
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection =
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD;

        RevHubOrientationOnRobot orientationOnRobot = new
                RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu.initialize(new IMU.Parameters(orientationOnRobot));
    }

    public static void driveFieldRelative(double forward, double right, double rotate) {


        // First, convert direction being asked to drive to polar coordinates
        double theta = Math.atan2(forward, right);
        double r = Math.hypot(right, forward);

        // Second, rotate angle by the angle the robot is pointing
        theta = AngleUnit.normalizeRadians(theta +
                imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));

        // Third, convert back to cartesian
        double newForward = r * Math.sin(theta);
        double newRight = r * Math.cos(theta);

        // Finally, call the drive method with robot relative forward and right amounts
        drive(newForward, newRight, rotate);
    }


    public static void drive(double forward, double strafe, double rotate){


        forwardRightMotor.setPower(forward + strafe - rotate);
        forwardLeftMotor.setPower(forward - strafe - rotate);

        backRightMotor.setPower(forward - strafe +rotate);
        backLeftMotor.setPower((forward + strafe + rotate));

    }
}
