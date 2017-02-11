package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="11368 Blue Beacon Auto", group="Team code")
public class TeamBlueAuto extends LinearOpMode {
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
		telemetry.addData("Status: ", "Resetting Encoders");
		telemetry.update();

		robot.init(hardwareMap);//Initialize hardwaremap to access hardware components

		double idle = robot.line.argb();

		robot.driveLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);//Reset encoder
		robot.driveRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

		idle();//Set to idle
		robot.driveLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);//Set to normal movement mode
		robot.driveRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		telemetry.addData("Encoders reset ", "Starting at %7d :%7d", robot.driveLeft.getCurrentPosition(), robot.driveRight.getCurrentPosition());
		telemetry.update();//Prints start position

		waitForStart();

		//driveToEncoder(DRIVE_SPEED, -207.5, 207.5, 5);//Drive to wall: workaround distance * 2.5
		driveToColorSensor(DRIVE_SPEED, 5, idle);
		sleep(5000);
		driveToEncoder(TURN_SPEED, -11, -11, 5);//Turn 45 degrees
		beaconButton();//Push Bhutan
		sleep(5000);
		//driveToEncoder(DRIVE_SPEED, -127.5, 127.5, 5);//Drive to next beacon
		driveToColorSensor(DRIVE_SPEED, 2, idle);
		beaconButton();//Push button
		sleep(5000);
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
				/*
				telemetry.addData("Target", "Running to %7d :%7d", newLeftTarget, newRightTarget);
				telemetry.addData("Current", "l:r at %7d :%7d", robot.driveLeft.getCurrentPosition(), robot.driveRight.getCurrentPosition());
				telemetry.update();
				*/
			}

			robot.driveLeft.setPower(0);//Stop motors after complete
			robot.driveRight.setPower(0);

			robot.driveLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);//Set motor to normal runmode
			robot.driveRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		}
	}


	public void driveToColorSensor(double speed, double timeout, double idle){
		if(opModeIsActive()){
			runtime.reset();

			robot.driveLeft.setPower(-Math.abs(speed));//Set run speed
			robot.driveRight.setPower(Math.abs(speed));

			while (opModeIsActive() && (runtime.seconds() < timeout) && robot.line.argb() == 0) {
				//While robot is running, hasn't reached timeout, and hasn't detected a marking get stuck in loop
				telemetry.addData("Current", "Color %7d", robot.line.alpha());
				telemetry.update();
			}

			robot.driveLeft.setPower(0);//Stop motors after complete
			robot.driveRight.setPower(0);
		}
	}

	public void beaconButton() throws InterruptedException {
		if (robot.beacon.blue() > 0) {//If blue then push button
			telemetry.addData("Found correct color on first try ", "");
			telemetry.update();
			robot.beaconPusher.setPower(1.0);
			sleep(500);
			robot.beaconPusher.setPower(-1.0);
			sleep(500);
		} else if(robot.beacon.red() > 0){//If red then move forward and push button
			telemetry.addData("Didn't find correct color on first try ", "");
			telemetry.update();
			driveToEncoder(DRIVE_SPEED, -10, 10, 1);
			sleep(1000);
			robot.beaconPusher.setPower(1.0);
			sleep(500);
			robot.beaconPusher.setPower(-1.0);
			sleep(500);
		}
		robot.beaconPusher.setPower(0.0);//Stop
	}
}