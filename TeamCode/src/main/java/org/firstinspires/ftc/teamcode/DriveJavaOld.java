package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name = "JavaDrive")
public class DriveJavaOld extends LinearOpMode {

    private DcMotor right_drive_front;
    private DcMotor right_drive_back;
    private DcMotor left_drive_front;
    private DcMotor left_drive_back;

    private double powerFactor = 0;
    private int speedChangeValue = 1;
    private boolean speedChangedUp = false;
    private boolean speedChangedDown = false;

    private void initMotors() {

        right_drive_front = hardwareMap.get(DcMotor.class, "right_drive_front");
        right_drive_back = hardwareMap.get(DcMotor.class, "right_drive_back");
        left_drive_front = hardwareMap.get(DcMotor.class, "left_drive_front");
        left_drive_back = hardwareMap.get(DcMotor.class, "left_drive_back");
        // Put initialization blocks here
        right_drive_front.setDirection(DcMotorSimple.Direction.REVERSE);
        right_drive_back.setDirection(DcMotorSimple.Direction.REVERSE);
        powerFactor = 0.4;

    }

    @Override
    public void runOpMode() {
        initMotors();

        telemetry.addLine("Waiting for start");
        telemetry.update();

        /*
         * Wait for the user to press start on the Driver Station
         */
        waitForStart();
        telemetry.addLine("Driver mode initiated");
        telemetry.update();

        while (opModeIsActive()) {

            callSpeedChange();
            moveBot(-gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);

        }
    }

    /**
     * Describe this function...
     */

    void callSpeedChange()
    {

        if (this.gamepad1.right_trigger != 0 && !speedChangedUp)
        {
            speedChangedUp = true;
            speedChange(true);
        }
        else if (this.gamepad1.right_trigger == 0 && speedChangedUp)
        {
            speedChangedUp = false;
        }
        if (this.gamepad1.left_trigger != 0 && !speedChangedDown)
        {
            speedChangedDown = true;
            speedChange(false);
        }
        else if (this.gamepad1.left_trigger == 0 && speedChangedDown)
        {
            speedChangedDown = false;
        }

    }

    void speedChange(boolean faster)
    {

        if (faster && speedChangeValue < 4)
        {
            speedChangeValue++;
        }
        if (!faster && speedChangeValue > 0)
        {
            speedChangeValue--;
        }

        switch (speedChangeValue)
        {
            case 0:
                powerFactor = 0.2;
                break;
            case 1:
                powerFactor = 0.4;
                break;
            case 2:
                powerFactor = 0.6;
                break;
            case 3:
                powerFactor = 0.8;
                break;
            case 4:
                powerFactor = 1;
                break;
        }

        telemetry.addData("speed change", powerFactor);
        telemetry.update();

    }
   private void moveBot(float vertical, float pivot, float horizontal) {

        right_drive_front.setPower(powerFactor * (-pivot + (vertical - horizontal)));
        right_drive_back.setPower(powerFactor * (-pivot + vertical + horizontal));
        left_drive_front.setPower(powerFactor * (pivot + vertical + horizontal));
        left_drive_back.setPower(powerFactor * (pivot + (vertical - horizontal)));

        right_drive_front.setPower(0);
        right_drive_back.setPower(0);
        left_drive_front.setPower(0);
        left_drive_back.setPower(0);

    }
 }

