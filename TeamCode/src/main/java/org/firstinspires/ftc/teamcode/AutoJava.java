package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;


@Autonomous(name = "AutoJava", group = "Auto")
public class AutoJava extends Robot {

    private PixelDetection pixelDetection;
    private volatile PixelDetection.BackdropPosition pos;

    private OpenCvCamera camera;
    private final String webcamName = "Webcam 1";

    protected double powerFactor = 1;
    private double startingPF = 0;



    private void initCamera() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, webcamName), cameraMonitorViewId);
        pixelDetection = new PixelDetection();
        camera.setPipeline(pixelDetection);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(320,240, OpenCvCameraRotation.SIDEWAYS_LEFT);
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("Camera error code:", errorCode);
                telemetry.update();
            }
        });
    }





    public void runOpMode() {

        initMotors();
        initCamera();

        // TODO: add the ability to find *what* color is most common in whichever area, useful for debugging
        while (!isStarted()) {
            telemetry.addData("White percentage of LCR mats:", pixelDetection.getLeftWhitePercent() + " "
                    + pixelDetection.getCenterWhitePercent() + " " + pixelDetection.getRightWhitePercent());
            telemetry.addData("ROTATION1: ", pixelDetection.getPosition());
            telemetry.update();
            pos = pixelDetection.getPosition();
        }
        camera.closeCameraDevice();

        telemetry.addLine("Waiting for start");
        telemetry.update();
        waitForStart();

        // doesnt technically need while loop but its easier to read & locate ig
        while (opModeIsActive()) {

            // TODO

        }

    }


}
