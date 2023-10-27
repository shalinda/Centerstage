package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public abstract class Robot extends LinearOpMode {

    protected DcMotorEx rf_drive;
    protected DcMotorEx rb_drive;
    protected DcMotorEx lf_drive;
    protected DcMotorEx lb_drive;

    protected DcMotorEx arm;
    protected Servo claw_arm;
    protected Servo claw;

    protected double powerFactor = 0;


    protected void initMotors() {
        rf_drive = hardwareMap.get(DcMotorEx.class, "rf_drive");
        rb_drive = hardwareMap.get(DcMotorEx.class, "rb_drive");
        lf_drive = hardwareMap.get(DcMotorEx.class, "lf_drive");
        lb_drive = hardwareMap.get(DcMotorEx.class, "lb_drive");

        rf_drive.setDirection(DcMotorSimple.Direction.REVERSE);
        rb_drive.setDirection(DcMotorSimple.Direction.REVERSE);

        arm = hardwareMap.get(DcMotorEx.class, "arm");
        claw_arm = hardwareMap.get(Servo.class, "claw_arm");
        claw = hardwareMap.get(Servo.class, "claw");

        arm.setDirection(DcMotor.Direction.FORWARD);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        claw.setPosition(0.0);
    }



    public abstract void runOpMode();




    private void clawGrab(boolean grab) {

    }

    private void clawTilt(int angle) {

    }

    private void clawLift(int height) {

    }


}
