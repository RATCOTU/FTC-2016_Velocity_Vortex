package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.TouchSensor;

/*
Hardware configuration for FTC11368 robot: 2016-17 Velocity Vortex
*/

public class HardwareTeamRobot {
	//Drive Controller
	public DcMotor driveLeft;
	public DcMotor driveRight;
	//Ball Controller
	public DcMotor intake;
	public DcMotor shooter;
	//Beacon Controller
	public DcMotor beaconPusher;

	ColorSensor beacon;
	ColorSensor line;

	public HardwareMap hwMap;

	public HardwareTeamRobot() {
	}

	public void init(HardwareMap ahwMap) {
		hwMap = ahwMap;

		driveLeft = hwMap.dcMotor.get("drive left");
		driveRight = hwMap.dcMotor.get("drive right");

		intake = hwMap.dcMotor.get("intake");
		shooter = hwMap.dcMotor.get("shooter");

		beaconPusher = hwMap.dcMotor.get("beacon pusher");

		beacon = hwMap.colorSensor.get("beacon sensor");
		line = hwMap.colorSensor.get("line sensor");

		driveLeft.setPower(0.0);
		driveRight.setPower(0.0);
		intake.setPower(0.0);
		shooter.setPower(0.0);
		beaconPusher.setPower(0.0);

		/*
		beacon = hwMap.i2cDevice.get("beacon sensor");
		beacon.enableI2cWriteMode(new I2cAddr(0x3e), 0x03, 1);
		beaconReader = new I2cDeviceSynchImpl(beacon, new I2cAddr(0x3e), false);
		beaconReader.engage();

		line = hwMap.i2cDevice.get("line sensor");
		line.enableI2cWriteMode(new I2cAddr(0x3c), 0x03, 1);
		lineReader = new I2cDeviceSynchImpl(line, new I2cAddr(0x3c), false);
		lineReader.engage();
		*/
	}
}
