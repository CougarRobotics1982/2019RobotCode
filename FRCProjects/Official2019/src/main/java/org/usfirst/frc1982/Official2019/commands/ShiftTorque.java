package org.usfirst.frc1982.Official2019.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1982.Official2019.Robot;

public class ShiftTorque extends Command {
    private boolean done = false;
    public ShiftTorque(){  requires(Robot.driveBase);   }
    protected void initialize() {
        Robot.driveBase.shiftTorque();;
        done=true;
    }
    protected boolean isFinished() {
        return done;
    }
}