package org.usfirst.frc1982.Official2019.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1982.Official2019.Robot;

public class SpinIntakeOut extends Command {
    public SpinIntakeOut() {
        requires(Robot.intake);
    }

    protected void initialize() {
        Robot.intake.spinOutward();
    }

    
    protected void execute() {
        Robot.intake.spinOutward();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
        Robot.intake.stopSpinning();
    }

    protected void interrupted() {
        Robot.intake.stopSpinning();
    }
}