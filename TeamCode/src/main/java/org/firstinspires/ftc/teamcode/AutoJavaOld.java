package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.openftc.easyopencv.OpenCvCamera;

import java.util.ArrayList;

;

@Autonomous(name = "AutoJava", group = "Auto")
public class AutoJavaOld extends LinearOpMode {
    private DcMotorEx right_drive1;
    private DcMotorEx right_drive2;
    private DcMotorEx left_drive1;
    private DcMotorEx left_drive2;
    private ArrayList<String> movements = new ArrayList<>();
//    private volatile SleeveDetection.ParkingPosition pos;

    PixelDetection pixelDetection;
    OpenCvCamera camera;
    String webcamName = "Webcam 1";


    double powerFactor = 1;
    double startingPF = 0;
    boolean startPressed = false;
    boolean clawClosed = false;
    //we may need some additional variables here ^^

    private void initMotors() {
        right_drive1 = hardwareMap.get(DcMotorEx.class, "right_drive1");
        right_drive2 = hardwareMap.get(DcMotorEx.class, "right_drive2");
        left_drive1 = hardwareMap.get(DcMotorEx.class, "left_drive1");
        left_drive2 = hardwareMap.get(DcMotorEx.class, "left_drive2");

        right_drive1.setDirection(DcMotorSimple.Direction.REVERSE);
        right_drive2.setDirection(DcMotorSimple.Direction.REVERSE);
        // stop and reset encoder goes in init motors don't change
        // claw things here
        powerFactor = 1;
        startingPF = powerFactor;
    }


    @Override
    public void runOpMode()
    {
        initMotors();


        while (!isStarted()) {
/*
            telemetry.addData("YCM: ", sleeveDetection.getYelPercent() + " " +
                    sleeveDetection.getCyaPercent() + " " + sleeveDetection.getMagPercent());
            telemetry.addData("ROTATION1: ", sleeveDetection.getPosition());
            telemetry.update();
            pos = sleeveDetection.getPosition();*/

        }


        telemetry.addLine("Waiting for start");
        telemetry.update();
        waitForStart();

        startPressed = true;
        boolean stop = false;
        camera.closeCameraDevice();
        while (opModeIsActive())
        {
            if (!stop) {


                stop = true;

            }

        }


    }


    private void moveBot(int distIN, float vertical, float pivot, float horizontal)
    {


    }




}
