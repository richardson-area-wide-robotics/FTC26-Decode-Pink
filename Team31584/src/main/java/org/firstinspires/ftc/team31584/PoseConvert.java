package org.firstinspires.ftc.team31584;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

public class PoseConvert {

    private static TileCord pretileCoord;
    private static IMU imu;


    public static TileCord getPretileCoord() {
        return pretileCoord;
    }

    public static void init(IMU imu2){
        imu = imu2;
    }

    public static TileCord covertToTileCoord(Pose3D pose3D){
        double theta = pose3D.getOrientation().getYaw();
        double hypot = pose3D.getPosition().y;


        Position position = new Position(
                DistanceUnit.INCH,
                //dimension - (distance from tag to edge+distance to tag*trig(mesured angle to tag+angle to starting point)
                141-(12+hypot*Math.cos(theta+imu.getRobotYawPitchRollAngles().getYaw())),
                0.0,
                141-(12+hypot*Math.sin(theta+imu.getRobotYawPitchRollAngles().getYaw())),0);
        position.toUnit(DistanceUnit.INCH);

        pretileCoord = new TileCord(position);

        return pretileCoord;
    }




    public static class TileCord {

        //The Grid Pose X (Up/Down)
        public int x;
        //The Grid Pose Z (Left/Right)
        public int z;

        public TileCord(Position position){


            int tileSizeInches = 23;

            this.x = Math.toIntExact(Math.round(position.x / tileSizeInches));
            this.z = Math.toIntExact(Math.round(position.y/tileSizeInches));

        }

        @NonNull
        @Override
        public String toString(){
            return "TILECORD: "+this.x+","+this.z;
        }
    }


}
