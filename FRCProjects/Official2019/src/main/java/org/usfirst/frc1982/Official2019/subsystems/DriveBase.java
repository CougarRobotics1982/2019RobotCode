package org.usfirst.frc1982.Official2019.subsystems;

import org.usfirst.frc1982.Official2019.commands.*;
import org.usfirst.frc1982.Official2019.*;
// import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
// import edu.wpi.first.wpilibj.PIDOutput;
// import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;


public class DriveBase extends Subsystem {
    private DoubleSolenoid gearboxShifter;
    private AnalogInput irSensor;

    private CANSparkMax left1;
    private CANSparkMax left2;
    private CANSparkMax left3;
    private CANSparkMax right4;
    private CANSparkMax right5;
    private CANSparkMax right6;
    public SpeedControllerGroup left;
    public SpeedControllerGroup right;

    public DifferentialDrive diffDrive;

    public DriveBase() {
        gearboxShifter = new DoubleSolenoid(0, 0, 1);
        // addChild("gearboxShifter",gearboxShifter);
        
        irSensor = new AnalogInput(0);
        // addChild("irSensor",irSensor);
        
        left1 = new CANSparkMax(1,MotorType.kBrushless);
        left2 = new CANSparkMax(2,MotorType.kBrushless);
        left3 = new CANSparkMax(3,MotorType.kBrushless);
        right4= new CANSparkMax(4,MotorType.kBrushless);
        right5= new CANSparkMax(5,MotorType.kBrushless);
        right6= new CANSparkMax(6,MotorType.kBrushless);

        left3.setInverted(true);
        right6.setInverted(true);

        left = new SpeedControllerGroup(left1, left2, left3);
        right = new SpeedControllerGroup(right4, right5, right6);

        right.setInverted(true);

        shiftTorque();

        if(Constants.flightStickController){
            diffDrive=new DifferentialDrive(left, right);
            diffDrive.setRightSideInverted(false);
        }
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new DriveEnable());
    }
    public void periodic() {}

    public void shiftFast(){
        gearboxShifter.set(Value.kForward);
    }
    public void shiftTorque(){
        gearboxShifter.set(Value.kReverse);
    }

    public double irSensorValue(){
        return irSensor.getVoltage();
    }
}