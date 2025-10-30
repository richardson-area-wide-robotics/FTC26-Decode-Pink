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

    double relativeX = 0;
    double relativeZ = 0;
    private VisionManager visionManager;

    @Override
    public void runOpMode() {
        IMU imu = hardwareMap.get(IMU.class, "imu");
        final Drivetrain DRIVETRAIN = new Drivetrain(
                hardwareMap.get(DcMotor.class, "motor 0"),
                hardwareMap.get(DcMotor.class, "motor 1"),
                hardwareMap.get(DcMotor.class, "motor 2"),
                hardwareMap.get(DcMotor.class, "motor 3"),
                imu
        );
        final Intake INTAKE = new Intake(hardwareMap.get(CRServo.class,"servo 0"),
                hardwareMap.get(DcMotor.class,"core 1"),
                hardwareMap.get(DcMotor.class,"core 0"));

        PoseConvert.init(hardwareMap.get(IMU.class, "imu"));

        final Shooter SHOOTER = new Shooter(hardwareMap.get(DcMotor.class,"shooter"));

        // --- Setup Vision ---
        visionManager = new VisionManager(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            double powerV = gamepad1.left_stick_x;
            double powerH = gamepad1.left_stick_y;
            double powerR = gamepad1.right_stick_x;

            if (gamepad1.a) { // Reset Yaw
                imu.resetYaw();
            }
            if(gamepad1.x){ // Intake
                INTAKE.intake(1);
            } else if (gamepad1.b){
                INTAKE.intake(-1);
            } else {
                 INTAKE.intake(0);
            }

            // Shoot that thang
            if (gamepad1.right_trigger > 0.1) {
                SHOOTER.shoot(gamepad1.right_trigger); // Forward shoot
            } else if (gamepad1.left_trigger > 0.1) {
                SHOOTER.shoot(-gamepad1.left_trigger); // Reverse
            }

            DRIVETRAIN.driveFieldRelative(powerV, powerH, powerR);

            // --- AprilTag detections ---
            List<AprilTagDetection> detections = visionManager.getAllDetections();
            if (!detections.isEmpty()) {

                if(visionManager.getObeliskID() != -1){
                    telemetry.addData("[Tag] Obelisk ID", visionManager.getObeliskID());
                }

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
            telemetry.addData("Forward Right Motor Power", DRIVETRAIN.forwardRightMotor.getPower());
            telemetry.addData("Forward Left Motor Power", DRIVETRAIN.forwardLeftMotor.getPower());
            telemetry.addData("Back Right Motor Power", DRIVETRAIN.backRightMotor.getPower());
            telemetry.addData("Back Left Motor Power", DRIVETRAIN.backLeftMotor.getPower());
            telemetry.addData("Horz Power", powerH);
            telemetry.addData("Vert Power", powerV);
            telemetry.addData("Roha Power", powerR);
            telemetry.addData("Intake Spin", INTAKE.getMekSpeed());
            telemetry.addData("Shooter Spin", SHOOTER.getMekSpeed());
            telemetry.update();
        }

        // Stop vision when done
        visionManager.close();
    }
}
