package org.firstinspires.ftc.team31584;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

public class PoseConvert {

    private static TileQuard pretileQuard;
    private static HardwareMap hardwareMap;
    private static IMU imu;


    public static TileQuard getPretileQuard() {
        return pretileQuard;
    }

    public static void init(IMU imu2){
        imu = imu2;
    }

    public static TileQuard covertToTileQuard(Pose3D pose3D){
        double theta = pose3D.getOrientation().getYaw();
        double hypot = pose3D.getPosition().y;


        Position position = new Position(
                DistanceUnit.INCH,
                141-(12+hypot*Math.cos(theta+imu.getRobotYawPitchRollAngles().getYaw())),
                0.0,
                141-(12+hypot*Math.sin(theta+imu.getRobotYawPitchRollAngles().getYaw())),0);
        position.toUnit(DistanceUnit.INCH);

        pretileQuard = new TileQuard(position);

        return pretileQuard;
    }

    public static TileQuard covertToTileQuard(Position position){
        position.toUnit(DistanceUnit.INCH);

        pretileQuard = new TileQuard(position);

        return pretileQuard;
    }


    public static class TileQuard{

        //The Grid Pose X (Up/Down)
        public int x;
        //The Grid Pose Z (Left/Right)
        public int z;

        public TileQuard (Position position){


            int tileSizeInchines = 23;

            this.x = Math.toIntExact(Math.round(position.x / tileSizeInchines));
            this.z = Math.toIntExact(Math.round(position.y/tileSizeInchines));



        }
    }


}
