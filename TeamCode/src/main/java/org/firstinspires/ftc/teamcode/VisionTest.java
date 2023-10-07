package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous(name = "Signal Sleeve Test")
public class VisionTest extends LinearOpMode {

    PixelDetection pixelDetection;
    OpenCvCamera camera;

    // Name of the Webcam to be set in the config
    String webcamName = "Webcam 1";


    @Override
    public void runOpMode() {

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


        while (!isStarted()) {
            telemetry.addData("Yel percent of LCR mats:", pixelDetection.getLeftYelPercent() + " "
                + pixelDetection.getCenterYelPercent() + " " + pixelDetection.getRightYelPercent());
            telemetry.addData("ROTATION1: ", pixelDetection.getPosition());
            telemetry.update();
        }

        waitForStart();
    }
}