package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public abstract class Robot extends LinearOpMode {

    protected DcMotorEx drive_rf;
    protected DcMotorEx drive_rb;
    protected DcMotorEx drive_lf;
    protected DcMotorEx drive_lb;

    protected DcMotorEx claw_arm;
    protected DcMotorEx claw;

    protected double powerFactor = 0;


    protected void initMotors() {
        drive_rf = hardwareMap.get(DcMotorEx.class, "drive_rf");
        drive_rb = hardwareMap.get(DcMotorEx.class, "drive_rb");
        drive_lf = hardwareMap.get(DcMotorEx.class, "drive_lf");
        drive_lb = hardwareMap.get(DcMotorEx.class, "drive_lb");

        drive_rf.setDirection(DcMotorSimple.Direction.REVERSE);
        drive_rb.setDirection(DcMotorSimple.Direction.REVERSE);

        claw_arm = hardwareMap.get(DcMotorEx.class, "claw_arm");
        claw = hardwareMap.get(DcMotorEx.class, "claw");
    }



    public abstract void runOpMode();




    private void clawGrab(boolean grab) {

    }

    private void clawTilt(int angle) {

    }

    private void clawLift(int height) {

    }


}
