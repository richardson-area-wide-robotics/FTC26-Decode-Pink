package org.firstinspires.ftc.team31584;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


// TODO Finish when we know exactly how the intakes gonna work
public class Intake {

    public static Servo servo1;
    public static Servo servo2;
    public static Servo servo3;
    public static DcMotor coreHex;
    public static DcMotor uphex1;
    public static DcMotor uphex2;
    public static DcMotor uphex3;

    public static void init(DcMotor hex1, DcMotor hex2, DcMotor hex3, DcMotor cHex, Servo s1, Servo s2, Servo s3){
        servo1 = s1;
        servo2 = s2;
        servo3 = s3;
        coreHex = cHex;
        uphex1 = hex1;
        uphex2 = hex2;
        uphex3 = hex3;

        uphex1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        uphex2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        uphex3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        coreHex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public static void init(Servo s1, Servo s2){
        servo1 = s1;
        servo2 = s2;
    }

    private static boolean isOpen = false;

    public static void servo() {
        if (isOpen) {
            // Close
            servo1.setPosition(0.0);
            servo2.setPosition(1.0);
        } else {
            // Open (invert)
            servo1.setPosition(1.0);
            servo2.setPosition(0.0);
        }
        isOpen = !isOpen; // flip the state
    }
}
