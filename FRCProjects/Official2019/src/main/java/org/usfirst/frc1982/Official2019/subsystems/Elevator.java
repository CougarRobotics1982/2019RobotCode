package org.usfirst.frc1982.Official2019.subsystems;

import org.usfirst.frc1982.Official2019.commands.*;
import edu.wpi.first.wpilibj.command.Subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


public class Elevator extends Subsystem {
    public WPI_TalonSRX elevatorMotor;

    public Elevator() {
        elevatorMotor = new WPI_TalonSRX(7);
        elevatorMotor.setInverted(true);
        zeroEncoder();
    }

    public double getEncoder(){
        return -elevatorMotor.getSelectedSensorPosition();
    }

    public void zeroEncoder(){
        elevatorMotor.setSelectedSensorPosition(0);
    }

    public void initDefaultCommand() {
        setDefaultCommand(new ElevatorManualControl());
    }

    public void periodic() {
    }
}