package org.firstinspires.ftc.team31584;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

@Autonomous(name = "Auton")
public class Auton extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        IMU imu = hardwareMap.get(IMU.class, "imu");
        Drivetrain.init(
                hardwareMap.get(DcMotor.class, "frontRight"),
                hardwareMap.get(DcMotor.class, "backRight"),
                hardwareMap.get(DcMotor.class, "frontLeft"),
                hardwareMap.get(DcMotor.class, "backLeft"),
                imu
        );
        Intake.init(
                hardwareMap.get(DcMotor.class,"intakeCore"),
                hardwareMap.get(DcMotor.class,"intakeMotor")
        );
        PoseConvert.init(hardwareMap.get(IMU.class, "imu"));

        Shooter.init(hardwareMap.get(DcMotor.class,"shooter"));

        telemetry.addLine("Ready to run autonomous");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {

            runFor(1600, () -> Drivetrain.drive(0.0f, -9.0f, 0.0f));
            runFor(20,  () -> Drivetrain.drive(0.0f, 0.0f, 0.0f));

            runFor(5000,  () -> Shooter.shoot(1));


            intake();






            telemetry.addLine("Finished autonomous actions");
            telemetry.update();
        }
    }

    public void intake(){
        try {
            runFor(4000,  () -> Intake.intake(-1));
            runFor(4000,  () -> Intake.kick(-1));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
