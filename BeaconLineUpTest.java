package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Bacon line up test", group="Team code")
public class BeaconLineUpTest extends LinearOpMode{

	HardwareTeamRobot robot = new HardwareTeamRobot();

	private ElapsedTime runtime = new ElapsedTime();

	static final double COUNTS_PER_MOTOR_REV = 7;//Counts per revolution of andymark hall effect encoder
	static final double DRIVE_GEAR_REDUCTION = 40;//Andymark 40:1 neverest
	static final double DIAMETER = 3;//Diameter of of drive sprocket
	static final double PULSES_PER_INCH = ((DIAMETER * 3.1415) / COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION);//How many times the encoder pulses pre inch
	static final double DRIVE_SPEED = 1.0;//Motor speed for normal movement
	static final double TURN_SPEED = 0.5;//Motor speed for turning
	static final double SEEK_SPEED = 0.3;//Motor speed for line seeking(not implemented)

	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);//Initialize hardwaremap to access hardware components

		robot.driveLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);//Reset encoder
		robot.driveRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

		idle();//Set to idle
		robot.driveLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);//Set to normal movement mode
		robot.driveRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		waitForStart();

		driveToEncoder(DRIVE_SPEED, -10, 10, 1);
		sleep(1000);
	}

	public void driveToEncoder(double speed, double lDistance, double rDistance, double timeout) {
		int newLeftTarget;//Target length in inches
		int newRightTarget;

		if (opModeIsActive()) {
			newLeftTarget = robot.driveLeft.getCurrentPosition() + (int) (lDistance * PULSES_PER_INCH);//Convert target length to target encoders count
			newRightTarget = robot.driveRight.getCurrentPosition() + (int) (rDistance * PULSES_PER_INCH);

			robot.driveLeft.setTargetPosition(newLeftTarget);//Set motor targets
			robot.driveRight.setTargetPosition(newRightTarget);

			robot.driveLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);//Set motors to run to target
			robot.driveRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

			runtime.reset();//Reset clock

			robot.driveLeft.setPower(Math.abs(speed));//Set run speed
			robot.driveRight.setPower(Math.abs(speed));

			while (opModeIsActive() && (runtime.seconds() < timeout) && (robot.driveLeft.isBusy() && robot.driveRight.isBusy())) {
				//While robot is running and hasn't reached timeout get stuck in loop
				telemetry.addData("Target", "Running to %7d :%7d", newLeftTarget, newRightTarget);
				telemetry.addData("Current", "l:r at %7d :%7d", robot.driveLeft.getCurrentPosition(), robot.driveRight.getCurrentPosition());
				telemetry.update();
			}

			robot.driveLeft.setPower(0);//Stop motors after complete
			robot.driveRight.setPower(0);

			robot.driveLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);//Set motor to normal runmode
			robot.driveRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}
	}
}
