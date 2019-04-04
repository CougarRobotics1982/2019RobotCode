package org.usfirst.frc1982.Official2019.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1982.Official2019.Robot;

public class RetractHatchGrabber extends Command {
    private boolean done = false;
    public RetractHatchGrabber(){  requires(Robot.intake);   }
    protected void initialize() {
        Robot.intake.retractHatchGrabber();
        done=true;
    }
    protected boolean isFinished() {
        return done;
    }
}