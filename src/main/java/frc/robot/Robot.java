// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Spark;
//import edu.wpi.first.wpilibj.TimedRobot;
//import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
//import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.PWMTalonSRX;
//import edu.wpi.first.wpilibj.PWMSparkMax;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  // Motor Controllers
  private final Spark m_leftMotor = new Spark(0);
  //private final Spark m_rightMotor = new Spark(1);
  private final PWMTalonSRX m_talon = new PWMTalonSRX(1);
  private final PWMVictorSPX m_victor = new PWMVictorSPX(2);
  //private final PWMSparkMax m_sparkMax = new PWMSparkMax(3);
  
  // Pneumatics
  private final DoubleSolenoid m_solenoid1 = new DoubleSolenoid(0,1);
  private final Compressor compressor = new Compressor(2);
  
  // Other Things? (Alex help me out)
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  //private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_leftMotor, m_rightMotor); 
  private final XboxController m_controller = new XboxController(0);
  private double speed = 0.5;
  
  boolean enabled = compressor.enabled();
  boolean pressureSwitch = compressor.getPressureSwitchValue();
  double current = compressor.getCompressorCurrent();
  

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    SmartDashboard.putNumber("Uptime", Timer.getFPGATimestamp());
    compressor.setClosedLoopControl(true);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here

        speed = 0.5;
        m_leftMotor.set(speed);
        m_talon.set(speed);
        m_victor.set(speed);
        //m_sparkMax.set(speed);
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() 
  {
    //m_robotDrive.arcadeDrive(m_controller.getY(GenericHID.Hand.kLeft), m_controller.getX(GenericHID.Hand.kLeft));
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() 
  {
    //setSafetyEnabled( False);
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() 
  {
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
      m_leftMotor.set(speed);
      m_talon.set(speed);
      m_victor.set(speed);
      //m_sparkMax.set(speed);
    }
    else if (m_controller.getBButton())
    {
      m_leftMotor.set(-speed);
      m_talon.set(-speed);
      m_victor.set(-speed);
      //m_sparkMax.set(-speed);
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
      m_talon.set(0);
      m_victor.set(0);
      //m_sparkMax.set(0);
    }
    //m_robotDrive.arcadeDrive(m_controller.getY(GenericHID.Hand.kLeft), m_controller.getX(GenericHID.Hand.kLeft));
  }

}
