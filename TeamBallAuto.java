package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="11368 Ball Auto", group="Team code")
public class TeamBallAuto extends LinearOpMode {
	HardwareTeamRobot robot = new HardwareTeamRobot();

	private ElapsedTime runtime = new ElapsedTime();//Timer

	static final double COUNTS_PER_MOTOR_REV = 7;//Counts per revolution of andymark hall effect encoder
	static final double DRIVE_GEAR_REDUCTION = 40;//Andymark 40:1 neverest
	static final double DIAMETER = 2.559;//Diameter of of drive sprocket
	static final double PULSES_PER_INCH = ((DIAMETER * 3.1415) / COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION);//How many times the encoder pulses pre inch
	static final double DRIVE_SPEED = 0.6;//Motor speed for normal movement
	static final double TURN_SPEED = 0.5;//Motor speed for turning
	static final double SEEK_SPEED = 0.3;//Motor speed for line seeking(not implemented)

	@Override
	public void runOpMode() throws InterruptedException {
		telemetry.addData("Status: ", "Resetting Encoders");
		telemetry.update();

		robot.init(hardwareMap);//Initialize hardwaremap to access hardware components

		/*
		robot.lineUp.enableLed(false);
		robot.beacon.enableLed(false);
		*/

		robot.driveLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);//Reset encoder
		robot.driveRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		idle();//Set to idle

		robot.driveLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);//Set to normal movement mode
		robot.driveRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		telemetry.addData("Encoders reset ", "Starting at %7d :%7d", robot.driveLeft.getCurrentPosition(), robot.driveRight.getCurrentPosition());
		telemetry.update();//Prints start position

		waitForStart();

		driveToEncoder(DRIVE_SPEED, 200, -200, 5);//Drive forward to center vortex
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