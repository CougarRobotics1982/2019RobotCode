package org.usfirst.frc1982.Official2019;

import org.usfirst.frc1982.Official2019.commands.*;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.*;
// import org.usfirst.frc1982.Official2019.subsystems.*;


public class OI {
    // public static final boolean isFlightStick = true;
    // public static final boolean usingOpBox = false;
    private final double inputExponent = 3;

    public Joystick controller;
    public Joystick opBox;

    public JoystickButton autoLineUp;
    public JoystickButton spinIntakeIn;
    public JoystickButton spinIntakeOut;
    public JoystickButton hatchGrabber;
    public JoystickButton maximumOverdrive;

    public JoystickButton opBoxCargoIn;
    public JoystickButton opBoxCargoOut;
    public JoystickButton opBoxHatchGrabber;

    
    public OI() {
        controller = new Joystick(0);
        if(Constants.usingOpBox)
            opBox = new Joystick(1);
        
        if(Constants.flightStickController){
            hatchGrabber = new JoystickButton(controller, 1);
            spinIntakeIn = new JoystickButton(controller, 2);
            spinIntakeOut = new JoystickButton(controller, 4);
            if(Constants.useVisionProcessing)
                autoLineUp = new JoystickButton(controller, 2);
        }else{
            hatchGrabber = new JoystickButton(controller, 5);
            spinIntakeIn = new JoystickButton(controller, 6);
            spinIntakeOut= new JoystickButton(controller, 4);
            if(Constants.useVisionProcessing)
                autoLineUp = new JoystickButton(controller, 1);
        }

        if(Constants.usingOpBox){
            opBoxCargoIn = new JoystickButton(opBox, 8);
            opBoxCargoOut = new JoystickButton(opBox, 6);
            opBoxHatchGrabber = new JoystickButton(opBox, 7);

            opBoxHatchGrabber.whenPressed(new ExtendHatchGrabber());
            opBoxHatchGrabber.whenReleased(new RetractHatchGrabber());
            opBoxCargoIn.whileHeld(new SpinIntakeIn());
            opBoxCargoOut.whileHeld(new SpinIntakeOut());
        }
        
        hatchGrabber.whenPressed(new ExtendHatchGrabber());
        hatchGrabber.whenReleased(new RetractHatchGrabber());
        spinIntakeIn.whileHeld(new SpinIntakeIn());
        spinIntakeOut.whileHeld(new SpinIntakeOut());
        if(Constants.useVisionProcessing)
            autoLineUp.whileHeld(new VisionLineUp());
        // maximumOverdrive.whenPressed(new ShiftSpeed());
        // maximumOverdrive.whenReleased(new ShiftTorque());
    }

    public double getX(){
        double initial = -controller.getTwist();
        return Math.copySign(Math.pow(initial, inputExponent), -initial);
    }

    public double getY(){
        double initial = controller.getY();
        return Math.copySign(Math.pow(initial, inputExponent), -initial);
    }

    public double getLeftStick(){
        double initial = controller.getRawAxis(1);
        return Math.copySign(Math.pow(initial, inputExponent), -initial);
    }
    public double getRightStick(){
        double initial = controller.getRawAxis(5);
        return Math.copySign(Math.pow(initial, inputExponent), -initial);
    }

    public boolean inputsClose(){
        return Math.abs(controller.getRawAxis(1)-controller.getRawAxis(5))<=0.1;
    }

    public boolean stickButtonPressed(int button){
        return controller.getRawButton(button);
    }

    public double getElevatorControlSlider(){
        if(Constants.usingOpBox){
            return opBox.getRawAxis(1)*0.5+0.5;
        }else{
            if(Constants.flightStickController)
                return -controller.getRawAxis(3)*0.5+0.5;
        }
        return 0.01;
    }
}