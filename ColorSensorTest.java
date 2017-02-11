package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

@TeleOp(name = "Color sensor test", group = "Team code")
public class ColorSensorTest extends LinearOpMode{
	HardwareTeamRobot robot = new HardwareTeamRobot();

	@Override
	public void runOpMode() {
		robot.init(hardwareMap);

		float hsvValues[] = {0F,0F,0F};//An array to will hold the hue, saturation, and value.

		final float values[] = hsvValues;//Values is a reference to the hsvValues array.

		final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.RelativeLayout);//Color of the Robot Controller app to match the hue detected by the RGB sensor.

		//robot.beacon = hardwareMap.colorSensor.get("beacon sensor");//Load object from config

		//robot.beacon.enableLed(false);

		waitForStart();

		//While the op mode is active, loop and read the RGB data.
		while (opModeIsActive()){
			//Color.RGBToHSV(robot.beacon.red() * 8, robot.beacon.green() * 8, robot.beacon.blue() * 8, hsvValues);//Convert the RGB values to HSV values.

			relativeLayout.post(new Runnable(){//Change the background color to match the color detected by the RGB sensor.
				public void run(){
					relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
				}
			});

			telemetry.update();
		}
	}
}
