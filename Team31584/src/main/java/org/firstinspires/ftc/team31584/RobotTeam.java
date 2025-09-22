package org.firstinspires.ftc.team31584;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

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
        Drivetrain.init(
                hardwareMap.get(DcMotor.class, "motor 0"),
                hardwareMap.get(DcMotor.class, "motor 1"),
                hardwareMap.get(DcMotor.class, "motor 2"),
                hardwareMap.get(DcMotor.class, "motor 3"),
                imu
        );

        // --- Setup Vision ---
        visionManager = new VisionManager(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            double powerV = gamepad1.left_stick_y;
            double powerH = gamepad1.left_stick_x;
            double powerR = gamepad1.right_stick_x;

            if (gamepad1.a) {
                imu.resetYaw();
            }
            if (gamepad1.b) {
                if(relativeX>0){
                    Drivetrain.drive(0.0f,-1.0f,0.0f);
                }
                if(relativeX<0){
                    Drivetrain.drive(0.0f,1.0f,0.0f);
                }
            }

            Drivetrain.driveFieldRelative(powerV, powerH, powerR);

            // --- AprilTag detections ---
            List<AprilTagDetection> detections1 = visionManager.getCam1Detections();
            if (!detections1.isEmpty()) {
                relativeX = detections1.get(0).ftcPose.x;
                relativeZ = detections1.get(0).ftcPose.z;

                for (AprilTagDetection tag : detections1) {
                    telemetry.addData("[Cam1] Tag ID", tag.id);
                    telemetry.addData("[Cam1] TAGPos X", tag.ftcPose.x);
                    telemetry.addData("[Cam1] TAGPos Y", tag.ftcPose.y);
                    telemetry.addData("[Cam1] TAGPos Z", tag.ftcPose.z);
                    telemetry.addData("[Cam1] ROBOTPos X", tag.robotPose.getPosition().x);
                    telemetry.addData("[Cam1] ROBOTPos Y", tag.robotPose.getPosition().y);
                    telemetry.addData("[Cam1] ROBOTPos Z", tag.robotPose.getPosition().z);
                }
            } else {
                relativeX = 0;
                relativeZ = 0;
                telemetry.addLine("[Cam1] No AprilTags detected");
            }

            List<AprilTagDetection> detections2 = visionManager.getCam2Detections();
            if (!detections2.isEmpty()) {
                for (AprilTagDetection tag : detections2) {
                    telemetry.addData("[Cam2] Tag ID", tag.id);
                    telemetry.addData("[Cam2] Pos X", tag.ftcPose.x);
                    telemetry.addData("[Cam2] Pos Y", tag.ftcPose.y);
                    telemetry.addData("[Cam2] Pos Z", tag.ftcPose.z);
                }
            } else {
                telemetry.addLine("[Cam2] No AprilTags detected");
            }

            // Telemetry
            telemetry.addData("Stick Y Gamepad", gamepad1.left_stick_y);
            telemetry.addData("Stick X Gamepad", gamepad1.left_stick_x);
            telemetry.addData("Forward Right Motor Power", Drivetrain.forwardRightMotor.getPower());
            telemetry.addData("Forward Left Motor Power", Drivetrain.forwardLeftMotor.getPower());
            telemetry.addData("Back Right Motor Power", Drivetrain.backRightMotor.getPower());
            telemetry.addData("Back Left Motor Power", Drivetrain.backLeftMotor.getPower());
            telemetry.addData("Horz Power", powerH);
            telemetry.addData("Vert Power", powerV);
            telemetry.addData("Rota Power", powerR);
            telemetry.update();
        }

        // Stop vision when done
        visionManager.close();
    }
}

