package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Touch sensor test", group = "Team code")
public class TouchSensorTest extends OpMode {

	HardwareTeamRobot robot = new HardwareTeamRobot();//Load hardware config

	@Override
	public void init() {
		robot.init(hardwareMap);//Initialize hardware config
	}

	@Override
	public void loop() {
		//telemetry.addData("Front status:", robot.front.isPressed());
		//telemetry.addData("Back status: ", robot.back.isPressed());
		telemetry.update();
	}
}
