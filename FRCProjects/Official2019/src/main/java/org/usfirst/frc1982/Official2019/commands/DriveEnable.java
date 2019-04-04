package org.usfirst.frc1982.Official2019.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc1982.Official2019.Constants;
import org.usfirst.frc1982.Official2019.Robot;

public class DriveEnable extends Command {
    private double maxSpeed = 0.5;

    public DriveEnable() {
        requires(Robot.driveBase);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(Constants.flightStickController){
            double turnSlowing = 0.4;
            if(Robot.oi.stickButtonPressed(5)){
                double slowFactor = 0.3;
                Robot.driveBase.diffDrive.arcadeDrive(slowFactor*Robot.oi.getY(), slowFactor*Robot.oi.getX()*turnSlowing, false);
            }else{
                Robot.driveBase.diffDrive.arcadeDrive(Robot.oi.getY(), Robot.oi.getX()*turnSlowing, false);
            }
        }else{
            double leftStick = Robot.oi.getLeftStick();
            double rightStick= Robot.oi.getRightStick();

            if(Robot.oi.inputsClose()){
                double average = (leftStick+rightStick)/2;
                leftStick=average;
                rightStick=average;
            }
            // leftStick=Math.copySign(Math.min(Math.abs(leftStick), maxSpeed),leftStick);
            // rightStick=Math.copySign(Math.min(Math.abs(rightStick),maxSpeed),rightStick);

            leftStick*=maxSpeed;
            rightStick*=maxSpeed;

            System.out.println("left stick is: "+Robot.oi.controller.getRawAxis(1)+" and left motor is: "+leftStick);
            System.out.println("right stick is: "+Robot.oi.controller.getRawAxis(5)+" and right motor is: "+rightStick);

            Robot.driveBase.left.set(leftStick);
            Robot.driveBase.right.set(rightStick);
        }
    }

    protected boolean isFinished() {
        return false;
    }

    // Called when another command which requires one or more of the same subsystems is scheduled to run
    protected void interrupted() {
    }
}