package org.usfirst.frc1982.Official2019.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1982.Official2019.Robot;

public class ShiftSpeed extends Command {
    private boolean done = false;
    public ShiftSpeed(){  requires(Robot.driveBase);   }
    protected void initialize() {
        Robot.driveBase.shiftFast();;
        done=true;
    }
    protected boolean isFinished() {
        return done;
    }
}