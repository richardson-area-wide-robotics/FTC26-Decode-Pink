package org.firstinspires.ftc.team31584;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;

@TeleOp(name = "PinkBot2")
public class RobotTeam extends LinearOpMode {

    //TODO TODAY
    //* UnShoot method
    //* UnIntake method
    //* NAV
    double relativeX = 0;
    double relativeZ = 0;
    private VisionManager visionManager;
    private double intakeRot = 0;

    @Override
    public void runOpMode() {
        IMU imu = hardwareMap.get(IMU.class, "imu");
        Drivetrain.init(
                hardwareMap.get(DcMotor.class, "motor 0"),
                hardwareMap.get(DcMotor.class, "motor 1"),
                hardwareMap.get(DcMotor.class, "motor 2"),
                hardwareMap.get(DcMotor.class, "motor 3"),
                imu
        );
        Intake.init(
                hardwareMap.get(CRServo.class,"servo 0"),
                hardwareMap.get(DcMotor.class,"core 1"),
                hardwareMap.get(DcMotor.class,"core 0")
        );
        PoseConvert.init(hardwareMap.get(IMU.class, "imu"));

        Shooter.init(hardwareMap.get(DcMotor.class,"shooter"));

        // --- Setup Vision ---
        visionManager = new VisionManager(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            double powerV = gamepad1.left_stick_y;
            double powerH = gamepad1.left_stick_x;
            double powerR = gamepad1.right_stick_x;

            if (gamepad1.a) { // Reset Yaw
                imu.resetYaw();
            }
            if(gamepad1.x){ // Intake
                intakeRot = Intake.intake(1);
            } else if (gamepad1.b){
                intakeRot = Intake.intake(-1);
            } else {
                intakeRot = Intake.intake(0);
            }

            Shooter.shoot(gamepad1.right_trigger); // Shoot that thang

            Drivetrain.driveFieldRelative(powerV, powerH, powerR);

            // --- AprilTag detections ---
            List<AprilTagDetection> detections = visionManager.getAllDetections();
            if (!detections.isEmpty()) {
                telemetry.addData("[Tag] Obelisk ID", visionManager.getObeliskID());

                // Use first detection for relative positioning
                relativeX = detections.get(0).ftcPose.x;
                relativeZ = detections.get(0).ftcPose.z;

                for (AprilTagDetection tag : detections) {
                    telemetry.addData("[Tag] ID", tag.id);
                    if(tag.id==20||tag.id ==24){
                        PoseConvert.covertToTileCoord(tag.robotPose);

                    }else {
                        telemetry.addData("[Tag] Pos X", tag.ftcPose.x);
                        telemetry.addData("[Tag] Pos Y", tag.ftcPose.y);
                        telemetry.addData("[Tag] Pos Z", tag.ftcPose.z);
                    }
                }
            } else {
                relativeX = 0;
                relativeZ = 0;
                telemetry.addLine("[Vision] No AprilTags detected");
            }

            // --- Telemetry ---
            PoseConvert.TileCord coord = PoseConvert.getPretileCoord();

            if(coord != null){
                telemetry.addLine("CORD: ("+coord.x+","+coord.z+")");
            }

            telemetry.addData("Stick Y Gamepad", gamepad1.left_stick_y);
            telemetry.addData("Stick X Gamepad", gamepad1.left_stick_x);
            telemetry.addData("Forward Right Motor Power", Drivetrain.forwardRightMotor.getPower());
            telemetry.addData("Forward Left Motor Power", Drivetrain.forwardLeftMotor.getPower());
            telemetry.addData("Back Right Motor Power", Drivetrain.backRightMotor.getPower());
            telemetry.addData("Back Left Motor Power", Drivetrain.backLeftMotor.getPower());
            telemetry.addData("Horz Power", powerH);
            telemetry.addData("Vert Power", powerV);
            telemetry.addData("Roha Power", powerR);
            telemetry.addData("Intake Spin", intakeRot);
            telemetry.update();
        }

        // Stop vision when done
        visionManager.close();
    }
}
