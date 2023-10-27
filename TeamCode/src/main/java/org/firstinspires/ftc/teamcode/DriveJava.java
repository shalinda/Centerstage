package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "DriveJava")
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
            telemetry.update();

            arm.setPower(gamepad2.left_stick_y);
            claw_arm.setPosition(gamepad2.right_stick_y);

            if (gamepad2.left_trigger > 0) claw.setPosition(0.08); //grab claw
            if (gamepad2.right_trigger > 0) claw.setPosition(0.00); //drop

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

        rf_drive.setPower(powerFactor * (-pivot + (vertical - horizontal)));
        rb_drive.setPower(powerFactor * (-pivot + vertical + horizontal));
        lf_drive.setPower(powerFactor * (pivot + vertical + horizontal));
        lb_drive.setPower(powerFactor * (pivot + (vertical - horizontal)));

        rf_drive.setPower(0);
        rb_drive.setPower(0);
        lf_drive.setPower(0);
        lb_drive.setPower(0);

    }
}
