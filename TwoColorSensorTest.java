package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;

@TeleOp(name = "Two Color sensor test", group = "Team code")
public class TwoColorSensorTest extends OpMode{
	HardwareTeamRobot robot = new HardwareTeamRobot();//Load hardware config

	ColorSensor beacon;
	ColorSensor line;

	I2cAddr I2cAddrBeacon = I2cAddr.create8bit(0x3e);
	I2cAddr I2cAddrLine = I2cAddr.create8bit(0x3c);

	@Override
	public void init() {
		robot.init(hardwareMap);//Initialize hardware config

		beacon = robot.hwMap.colorSensor.get("beacon sensor");
		line = robot.hwMap.colorSensor.get("line sensor");

		beacon.setI2cAddress(I2cAddrBeacon);
		beacon.enableLed(false);


		line.setI2cAddress(I2cAddrLine);
		line.enableLed(true);

	}

	@Override
	public void loop() {
		telemetry.addData("Beacon: ", beacon.argb());
		telemetry.addData("Line: ", line.argb());
		telemetry.update();
	}
}
