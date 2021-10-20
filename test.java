// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Compressor;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private final Spark m_leftMotor = new Spark(0);
  //private final Spark m_rightMotor = new Spark(1);
  //private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_leftMotor, m_rightMotor); 
  private final XboxController m_controller = new XboxController(0);
  private double speed = 0.5;
  private final DoubleSolenoid m_solenoid1 = new DoubleSolenoid(1,2);
  private final Compressor compressor = new Compressor(3);

  boolean enabled = compressor.enabled();
  boolean pressureSwitch = compressor.getPressureSwitchValue();
  double current = compressor.getCompressorCurrent();
    if (enabled)
    {
      compressor.setClosedLoopControl(true);
    }
    if (m_controller.getBumperPressed(GenericHID.Hand.kLeft))
    {
      m_solenoid1.set(DoubleSolenoid.Value.kForward);
    }
    else if (m_controller.getBumperReleased(GenericHID.Hand.kLeft))
    {
      m_solenoid1.set(DoubleSolenoid.Value.kReverse);
    }
    else
    {
      m_solenoid1.set(DoubleSolenoid.Value.kOff);
    }
    if (m_controller.getXButtonPressed() && speed>0)
    {
      speed = speed - 0.1;
    }
    else if (m_controller.getYButtonPressed() && speed<1)
    {
      speed = speed + 0.1;
    }
    if (m_controller.getAButton())
    {
      m_leftMotor.set(speed );
    }
    else if (m_controller.getBButton())
    {
      m_leftMotor.set(-speed);
    }
    else if (m_controller.getBumper(GenericHID.Hand.kRight))
    {
      speed = 0.5;
    }
    //else if (m_controller.getBumper(GenericHID.Hand.kLeft))
    //{
    //  m_leftMotor.set(0.5);
   //   public static void delay( 5.0 );
    //}
    else
    {
      m_leftMotor.set(0);
    }
    //m_robotDrive.arcadeDrive(m_controller.getY(GenericHID.Hand.kLeft), m_controller.getX(GenericHID.Hand.kLeft));
