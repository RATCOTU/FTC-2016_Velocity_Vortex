package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;


	@TeleOp(name = "11368 Tele-op", group = "Team code")
	public class TeamRobotTeleop extends OpMode{

		HardwareTeamRobot robot = new HardwareTeamRobot();//Load hardware config

	public static final double THRESHOLD = 0.1;//Set joystick threshold

	@Override
	public void init(){
		robot.init(hardwareMap);//Initialize hardware config
		telemetry.addData("Status: ", "initiated");
		telemetry.update();
	}

	@Override
	public void loop(){
		//telemetry.addData("Status: ", "running");
		//telemetry.update();
		double left;//Variables for joystick input
		double right;

		if(THRESHOLD < Math.abs(gamepad1.left_stick_y)){//Set deadzone for joystick
			left = -scale(gamepad1.left_stick_y);//Invert and scale
			robot.driveLeft.setPower(left);
		}
		else{
			robot.driveLeft.setPower(0);
		}

		if(THRESHOLD < Math.abs(gamepad1.right_stick_y)){//Set deadzone for joystick
			right = scale(gamepad1.right_stick_y);//Scale
			robot.driveRight.setPower(right);
		}
		else {
			robot.driveRight.setPower(0);
		}


		if(gamepad1.right_bumper){//Shooter out
			robot.shooter.setPower(-1.0);
		}
		else if(gamepad1.right_trigger > 0){//Shooter in
			robot.shooter.setPower(1.0);
		}
		else{//Shooter stop
			robot.shooter.setPower(0);
		}

		if(gamepad1.left_bumper){//Intake in
			robot.intake.setPower(1.0);
		}
		else if(gamepad1.left_trigger > 0){//Intake out
			robot.intake.setPower(-1.0);
		}
		else{//Intake out
			robot.intake.setPower(0);
		}

		if(gamepad1.dpad_left){//Beacon pusher moves to left
			robot.beaconPusher.setPower(1.0);
		}
		else if(gamepad1.dpad_right){//Beacon pusher moves to right
			robot.beaconPusher.setPower(-1.0);
		}
		else{//Beacon pusher stops
			robot.beaconPusher.setPower(0.0);
		}
		/*
		telemetry.addData("Beacon blue", robot.beacon.blue());
		telemetry.addData("Beacon red", robot.beacon.red());
		telemetry.addData("Line brightness", robot.beacon.alpha());
		*/
	}


	public double scale(double in){//Grants more fidelity at lower speed while not reducing max speed
		double out = in * in * in;
		return out;
	}
}
