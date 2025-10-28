package org.firstinspires.ftc.team31584;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Shooter {

    private static DcMotor shooterMotor;

    public static void init(DcMotor motor){
        shooterMotor = motor;
    }
    public static void shoot(double power){
        shooterMotor.setPower(-power);
    }

}
