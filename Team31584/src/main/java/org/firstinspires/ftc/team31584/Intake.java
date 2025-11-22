package org.firstinspires.ftc.team31584;

import com.qualcomm.robotcore.hardware.DcMotor;


public class Intake {

    public static DcMotor feederMotor1;
    public static DcMotor feederMotor2;



    public static void init(DcMotor ft1, DcMotor ft2){
        feederMotor1 = ft1;
        feederMotor2 = ft2;
        feederMotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        feederMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    /**
     * Intake a ball
     *
     * @param power Power to set the intake motors to, Positive = Into robot
     */
    public static double intake(double power){

        feederMotor1.setPower(-power);
        feederMotor2.setPower((power));

        return feederMotor2.getPower();
    }



}
