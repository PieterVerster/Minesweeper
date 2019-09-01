import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.*;
import java.util.*;

public class Settings extends JFrame
{
	private static final int TRY_MIN = 1;
	private static final int TRY_MAX = 4;
	private static final int TRY_INIT = 1;
	
	private String[] screenSizeArray = {"450x600", "600x800", "768x1024", "800x600", "1152x864", "1280x720"};
	
	private GridBagConstraints constraints = new GridBagConstraints();
	private GridBagLayout formLayout = new GridBagLayout();
	private MouseHandler mouseHandler = new MouseHandler();
	private ReaderWriter readWrite = new ReaderWriter();
	
	private SliderListener sliderListener = new SliderListener();
	private ComboBoxListener comboBoxListener = new ComboBoxListener();
	
	private String filePath = "Settings\\settings.txt";
	
	//Create object of panel
	private JPanel settingsPanel = new JPanel();
	
	//Create Labels for controls
	private JLabel retryCounterLabel;
	private JLabel screenSizeLabel;
	
	//Create Controls
	private JSlider tryCounter;
	private JComboBox<String> screenSize;
	private JButton acceptButton;
	
	//Properties
	private int retries = 1;
	private int height = 0;
	private int width = 0;
	
	private ArrayList<String> settingsList = new ArrayList<String>();
	
	public Settings()
	{
		//set layout and title of the form
		settingsPanel.setLayout(formLayout);
		setTitle("Minesweeper - Settings");
		
		initializeControls();
		addActionListeners();
		setScreenSizeControls();
		setSliderControls();
		
		constraints.fill = GridBagConstraints.HORIZONTAL;		//Fill constraints horizontally
		
		//Set constraints and add controls
		setConstraints(1, 20, 0, 0);
		settingsPanel.add(retryCounterLabel, constraints);
		setConstraints(1, 20, 0, 1);
		settingsPanel.add(tryCounter, constraints);

		setConstraints(2, 20, 0, 2);
		settingsPanel.add(screenSizeLabel, constraints);
		setConstraints(2, 20, 0, 3);
		settingsPanel.add(screenSize, constraints);	

		setConstraints(1, 20, 0, 4);
		settingsPanel.add(acceptButton, constraints);
		
		add(settingsPanel);
	}
	
//===========================================================================================================================================================
	
	//initialize all controls
	public void initializeControls()
	{
		retryCounterLabel = new JLabel("Retry Modifier: ");
		screenSizeLabel = new JLabel("Screen Size: ");
		
		acceptButton = new JButton("Accept");
		tryCounter = new JSlider(JSlider.HORIZONTAL, TRY_MIN, TRY_MAX, TRY_INIT);
		screenSize = new JComboBox<>(screenSizeArray);
		
	} //end of initializeButtons()
	
//===========================================================================================================================================================
	
	//add action listeners to all controls
	public void addActionListeners()
	{
		acceptButton.addMouseListener(mouseHandler);
		tryCounter.addChangeListener(sliderListener);
		screenSize.addActionListener(comboBoxListener);
		
	} //end of initializeButtons()

//===========================================================================================================================================================
	
	//set constraints for controls
	public void setConstraints(int width, int padY, int x, int y)
	{
		constraints.gridwidth = width;	//set width for button
		constraints.ipady = padY;		//set height for button
		constraints.gridx = x;		//set new x position for button
		constraints.gridy = y;		//set new y position for button
	
	}	//end of setConstraints()
	
//===========================================================================================================================================================
	
	//set JComboBox for screen size
	public void setScreenSizeControls()
	{
		screenSize.setSelectedIndex(0);
		
	} //end of initializeButtons()

//===========================================================================================================================================================
	
	//set JSlider for bomb counter
	public void setSliderControls()
	{
		tryCounter.setMajorTickSpacing(1);
		tryCounter.setPaintLabels(true);
		tryCounter.setPaintTicks(true);
		tryCounter.setSnapToTicks(true);
		tryCounter.setPaintTrack(true);
	
	} //end of setSliderControls()

//===========================================================================================================================================================
	
	//accessor for retries
	public int getRetryCounter()
	{
		return retries;
	
	} //end of getRetryCounter()

//===========================================================================================================================================================
	
	//mutator for retries
	public void setRetryCounter(int value)
	{
		retries = value;
	
	} //end of setRetryCounter()
	
//===========================================================================================================================================================
	
	//accessor for screen height
	public int getScreenHeight()
	{
		return height;
	
	} //end of getScreenHeight()

//===========================================================================================================================================================
	
	//mutator for screen height
	public void setScreenHeight(int value)
	{
		height = value;
	
	} //end of setScreenHeight()
	
//===========================================================================================================================================================
	
	//accessor for screen width
	public int getScreenWidth()
	{
		return width;
	
	} //end of getScreenWidth()

//===========================================================================================================================================================
	
	//mutator for screen width
	public void setScreenWidth(int value)
	{
		width = value;
	
	} //end of setScreenWidth()
	
//===========================================================================================================================================================
	
	public class ComboBoxListener implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) 
		{
			@SuppressWarnings("unchecked")
			JComboBox<String> comboBox = (JComboBox<String>)e.getSource();
			String screenSizeArray = (String)comboBox.getSelectedItem();
			
			setScreenHeight(Integer.parseInt(screenSizeArray.substring(screenSizeArray.indexOf("x") + 1)));
			setScreenWidth(Integer.parseInt(screenSizeArray.substring(0, screenSizeArray.indexOf("x"))));
		}
    }
	
//===========================================================================================================================================================

	public class SliderListener implements ChangeListener
	{
		public void stateChanged(ChangeEvent e)
		{
			JSlider source = (JSlider)e.getSource();
			
			System.out.println("Testing value: " + source.getValue());
			
			int counter = source.getValue();
			setRetryCounter(counter);
		}
	}
	
//===========================================================================================================================================================

	//subclass for MouseHandler
	public class MouseHandler implements MouseListener
	{	
		public void mousePressed(MouseEvent me)
		{
			JButton button = (JButton) me.getSource();
			
			if (button == acceptButton)
			{
				System.out.println("Accept button");
				
				settingsList.add(String.valueOf(getRetryCounter()));
				settingsList.add(String.valueOf(getScreenWidth()));
				settingsList.add(String.valueOf(getScreenHeight()));
				
				readWrite.saveFile(filePath, settingsList);
				dispose();
			}
			
		} //end of mousePressed
		
//-----------------------------------------------------------------------------------------------------------------------------------------------------------
		
		public void mouseReleased(MouseEvent me)
		{
			//Empty
		
		} //end of mouseReleased
		
		public void mouseEntered(MouseEvent me)
		{
			//Empty
			
		} //end of mouseEntered
		
//-----------------------------------------------------------------------------------------------------------------------------------------------------------
		
		public void mouseExited(MouseEvent me)
		{
			//Empty
			
		} //end of mouseExited
		
//-----------------------------------------------------------------------------------------------------------------------------------------------------------
		
		public void mouseClicked(MouseEvent me)
		{
			//Empty
		
		} //end of mouseClicked
		
	} //end of subclass MouseHandler
	
//===========================================================================================================================================================

} //end of class Settings