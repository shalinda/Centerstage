package org.firstinspires.ftc.teamcode;

public class DriveJava extends Robot {



    private int speedChangeValue = 1;
    private boolean speedChangedUp = false;
    private boolean speedChangedDown = false;

    protected double powerFactor = 0.4;




    public void runOpMode() {

        initMotors();
        telemetry.addLine("Waiting for start");
        telemetry.update();

        waitForStart();
        telemetry.addLine("Driver mode initiated");
        telemetry.update();

        while (opModeIsActive()) {
            callSpeedChange();
            moveBot(-gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.left_stick_x);
        }
    }




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

        drive_rf.setPower(powerFactor * (-pivot + (vertical - horizontal)));
        drive_rb.setPower(powerFactor * (-pivot + vertical + horizontal));
        drive_lf.setPower(powerFactor * (pivot + vertical + horizontal));
        drive_lb.setPower(powerFactor * (pivot + (vertical - horizontal)));

        drive_rf.setPower(0);
        drive_rb.setPower(0);
        drive_lf.setPower(0);
        drive_lb.setPower(0);

    }
}
