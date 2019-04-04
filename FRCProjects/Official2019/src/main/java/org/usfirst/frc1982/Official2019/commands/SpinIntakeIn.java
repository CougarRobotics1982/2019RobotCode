package org.usfirst.frc1982.Official2019.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1982.Official2019.Robot;

public class SpinIntakeIn extends Command {
    public SpinIntakeIn() {
        requires(Robot.intake);
    }

    protected void initialize() {
        Robot.intake.spinInward();
    }

    
    protected void execute() {
        Robot.intake.spinInward();
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