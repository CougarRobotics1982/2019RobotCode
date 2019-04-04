package org.usfirst.frc1982.Official2019.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc1982.Official2019.*;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class ElevatorManualControl extends Command {
    private final double maxEncoderPosition = 20000;
    private final double stationaryPower = 0.25;

    private final double slowRange = 1000;
    private final double stopRange = 100;
    private final double fastSetPointSpeed = 1;
    private final double slowSetPointSpeed = 0.3;

    private WPI_TalonSRX motor;
    private ElevatorControlType controlType;

    private boolean reachedTarget;
    private double setPoint;

    public enum ElevatorControlType{None,Free,Basic,Setpoint,Combo,Slider}

    public ElevatorManualControl(){ requires(Robot.elevator); }
    protected void initialize() {
        motor = Robot.elevator.elevatorMotor;
        motor.setNeutralMode(NeutralMode.Brake);
        setControlType(Constants.defaultElevatorControl);
    }

    protected void execute() {
        switch(controlType){
            case Basic:
                basicControl();
                break;  
            case Slider:
                reachedTarget = false;
                grabSetPointFromOpBox();
                setPointControl();
                break;
            case Setpoint:
                setPointControl();
                break;
            case Combo:
                comboControl();
                break;
            default:
                motor.set(0);
        }
    }

    public void setControlType(ElevatorControlType t){
        controlType = t;
        if(t==ElevatorControlType.Free){
            motor.setNeutralMode(NeutralMode.Coast);
        }else{
            motor.setNeutralMode(NeutralMode.Brake);
        }
    }

    protected boolean isFinished() {return false;}

    public void setSetpoint(double point){
        reachedTarget = false;
        setPoint = point;
    }

    public void grabSetPointFromOpBox(){
        setPoint = Robot.oi.getElevatorControlSlider()*maxEncoderPosition;
    }

    private void basicControl(){
        double speed = 0;
        if(Constants.flightStickController){
            double povAngle = Robot.oi.controller.getPOV();
            if(povAngle == 0){//up
                speed = 1;
            }else if(povAngle == 180){//down
                speed = -0.6;
            }
        }else{
            double lTrigger = Robot.oi.controller.getRawAxis(2)-0.5;
            double rTrigger = Robot.oi.controller.getRawAxis(3)-0.5;
            speed = (lTrigger-rTrigger);
        }
        
        double ratio = 1-stationaryPower;
        double output = stationaryPower+speed*ratio;
        if(Robot.elevator.getEncoder()>100)
            motor.set(output);
        else
            motor.set(speed);
    }

    public void setPointControl(){
        System.out.println("reached target: "+reachedTarget);
        if(!reachedTarget){
            double position = Robot.elevator.getEncoder();
            double error = Math.abs(setPoint-position);

            System.out.println("setpoint: "+setPoint+" and position = "+position);
            if(error>stopRange){
                double speed;

                if(error<=slowRange)
                    speed = slowSetPointSpeed;
                else
                    speed = fastSetPointSpeed;

                speed*= 1-stationaryPower;
                if(position<setPoint){
                    motor.set(stationaryPower+speed);
                }else{
                    motor.set(stationaryPower-speed);
                }
            }else{
                reachedTarget=true;
                motor.set(stationaryPower);
            }
        }else{
            motor.set(stationaryPower);
        }
    }

    public void comboControl(){
        if(reachedTarget){
            basicControl();
        }else{
            setPointControl();
        }
    }
}