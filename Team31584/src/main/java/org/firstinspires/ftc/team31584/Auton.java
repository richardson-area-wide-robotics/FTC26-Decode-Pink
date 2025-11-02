package org.firstinspires.ftc.team31584;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

@Autonomous(name = "Auton")
public class Auton extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize IMU
        IMU imu = hardwareMap.get(IMU.class, "imu");

        // Initialize drivetrain motors
        Drivetrain.init(
                hardwareMap.get(DcMotor.class, "motor 0"),
                hardwareMap.get(DcMotor.class, "motor 1"),
                hardwareMap.get(DcMotor.class, "motor 2"),
                hardwareMap.get(DcMotor.class, "motor 3"),
                imu
        );

        telemetry.addLine("Ready to run autonomous");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            runFor(500, () -> Drivetrain.drive(0.0f, -1.0f, 0.0f));
            Drivetrain.drive(0, 0, 0); // stop


            telemetry.addLine("Finished autonomous actions");
            telemetry.update();
        }
    }

    /**
     * Runs a given action for a specified duration (in milliseconds)
     */
    private void runFor(long millis, Runnable action) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        while (opModeIsActive() && System.currentTimeMillis() - startTime < millis) {
            action.run();
            idle(); // Let the system breathe
        }
    }
}
