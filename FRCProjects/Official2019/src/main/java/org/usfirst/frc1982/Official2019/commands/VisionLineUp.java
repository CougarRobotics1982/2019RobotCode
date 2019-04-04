package org.usfirst.frc1982.Official2019.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1982.Official2019.Robot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
// import edu.wpi.first.wpilibj.Joystick;
import org.usfirst.frc1982.Official2019.vision.*;
import org.usfirst.frc1982.Official2019.subsystems.*;

public class VisionLineUp extends Command {
    private final double tolerance    = 10; //pixels off of center it can be
    private final int    slowDist     = 95; //pixels between the centers of the targets
    private final double stopDist     = 2.6; //IR sensor value

    private final double minTurnSpeed = 0.05;
    private final double maxTurnSpeed = 0.2;
    private final double forwardSpeed = 0.3;
    private final double slowSpeed    = 0.05;

    private final int imgWidth = Robot.imgWidth;
    private final double range = (double)imgWidth/2; //range from middle of screen to edge
    private final double scale = (maxTurnSpeed-minTurnSpeed)/range; //scale to convert from pixels off (0-range) to motor speed (minTurnSpeed to )

    private boolean finished = false;
    private int consecutiveBlindnesses = 0;
    private DriveBase driveBase;
    private DifferentialDrive diffDrive;
    private TargetPair target;
    // private Joystick controller;

    public VisionLineUp() {
        requires(Robot.driveBase);
    }

    protected void initialize() {
        driveBase=Robot.driveBase;
        diffDrive = Robot.driveBase.diffDrive;
        // controller = Robot.oi.controller;
    }

    protected void execute() {
        if(Robot.hasTarget()){
            target = Robot.getTarget();
            consecutiveBlindnesses=0;
        }else{
            consecutiveBlindnesses++;
        }

        if(consecutiveBlindnesses<=2 && target!=null){
            double targWidth = target.getXDistance();

            boolean close = false;
            close = close || targWidth>slowDist;
            
            if(!close){
                double distFromCenter = target.getX()-(0.5*imgWidth);
                double sign = Math.copySign(1, distFromCenter); //1 or -1 for direction

                double turn = distFromCenter*scale+sign*minTurnSpeed;//should be 0 to 1

                if(Math.abs(distFromCenter)>tolerance){
                    diffDrive.arcadeDrive(turn, forwardSpeed);
                }else{
                    diffDrive.arcadeDrive(0, forwardSpeed);
                }
            }else{
                if(driveBase.irSensorValue()<stopDist)
                    diffDrive.arcadeDrive(0, slowSpeed);
                else
                    diffDrive.arcadeDrive(0, 0);
                finished=true;
            }
        }else{
            if(driveBase.irSensorValue()<stopDist)
                diffDrive.arcadeDrive(0, slowSpeed);
            else
                diffDrive.arcadeDrive(0, 0);
        }
    }

    protected boolean isFinished() {
        return finished;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
