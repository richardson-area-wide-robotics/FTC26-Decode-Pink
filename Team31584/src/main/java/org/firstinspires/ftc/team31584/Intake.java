package org.firstinspires.ftc.team31584;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;


// TODO Finish when we know exactly how the intakes gonna work
public class Intake {

    public static CRServo servo0;
    public static DcMotor feederMotor;


    public static void init(CRServo s0, DcMotor ft){
        servo0 = s0;
        feederMotor = ft;
        feederMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public static double rotate(double power){

        servo0.setPower(-power);
        feederMotor.setPower(-power);

        return servo0.getPower();
    }



}
