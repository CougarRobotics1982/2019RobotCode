package org.usfirst.frc1982.Official2019.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1982.Official2019.Robot;

public class ExtendHatchGrabber extends Command {
    private boolean done = false;
    public ExtendHatchGrabber(){  requires(Robot.intake);   }
    protected void initialize() {
        Robot.intake.extendHatchGrabber();
        done=true;
    }
    protected boolean isFinished() {
        return done;
    }
}