//JPG Verster, last edit at 23 October 2018

import javax.swing.*;
import java.awt.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class MainMenu extends JFrame
{
	private GridBagConstraints constraints = new GridBagConstraints();
	private GridBagLayout formLayout = new GridBagLayout();
	private MouseHandler mouseHandler = new MouseHandler();
	private Settings settings = new Settings();
	
	//Create object of panel
	private JPanel menuPanel = new JPanel();
	
	//Create references to all controls
	private JButton easyButton;
	private JButton mediumButton;
	private JButton hardButton;
	private JButton highScoresButton;
	private JButton settingsButton;
	
	//Create ArrayList for settings
	private ArrayList<String> settingsList = new ArrayList<String>();
	
	//create ReaderWriter object
	private ReaderWriter readerWriter = new ReaderWriter();
	
	//File path for settings
	private String filePath = "Settings\\settings.txt";
	
	//Settings
	int height = 800;
	int width = 800;
	int retries = 0;
	
	public MainMenu()
	{
		//set layout and title of the form
		menuPanel.setLayout(formLayout);
		setTitle("Minesweeper - Main Menu");
		
		initializeButtons();	//initialize buttons
		
		addMouseListeners();	//add mouse listeners
		
		constraints.fill = GridBagConstraints.HORIZONTAL;	//Fill constraints horizontally
		constraints.insets = new Insets(10, 0, 0, 0);		//set insets for all buttons
		
		setConstraints(2, 20, 0, 0);						//set constraints for easy button
		menuPanel.add(easyButton, constraints);				//add easy button to menuPanel
		
		setConstraints(2, 20, 0, 1);						//set constraints for medium button
		menuPanel.add(mediumButton, constraints);			//add medium button to menuPanel
		
		setConstraints(2, 20, 0, 2);						//set constraints for hard button
		menuPanel.add(hardButton, constraints);				//add hard button button to menuPanel
		
		setConstraints(2, 20, 0, 4);						//set constraints for high scores button
		menuPanel.add(highScoresButton, constraints);		//add high scores button to menuPanel
		
		setConstraints(2, 20, 0, 5);						//set constraints for settings button
		menuPanel.add(settingsButton, constraints);			//add settings button to menuPanel
		
		add(menuPanel);		//add panel to frame
	
	} //end of constructor MainMenu()
	
//===========================================================================================================================================================
	
	//Initialize all buttons
	public void initializeButtons()
	{
		easyButton = new JButton("Easy");
		mediumButton = new JButton("Medium");
		hardButton = new JButton("Hard");
		highScoresButton = new JButton("High Scores");
		settingsButton = new JButton("Settings");
	
	} //end of initializeButtons()
	
//===========================================================================================================================================================
	
	//add mouse listeners to all buttons
	public void addMouseListeners()
	{
		easyButton.addMouseListener(mouseHandler);
		mediumButton.addMouseListener(mouseHandler);
		hardButton.addMouseListener(mouseHandler);
		highScoresButton.addMouseListener(mouseHandler);
		settingsButton.addMouseListener(mouseHandler);
	
	} //end of addMouseListeners()
	
//===========================================================================================================================================================
	
	//set constraints when needed
	public void setConstraints(int width, int padY, int x, int y)
	{
		constraints.gridwidth = width;	//set width for button
		constraints.ipady = padY;		//set height for button
		constraints.gridx = x;			//set new x position for button
		constraints.gridy = y;			//set new y position for button
	
	} //end of setConstraints()
	
//===========================================================================================================================================================

//subclass for MouseHandler
	public class MouseHandler implements MouseListener
	{
		GridBagConstraints constraints = new GridBagConstraints();
		
//-----------------------------------------------------------------------------------------------------------------------------------------------------------
		
		//when mouse is pressed
		public void mousePressed(MouseEvent me)
		{
			JButton button = (JButton) me.getSource();
			Minefield minefield = null;
			
			settingsList = readerWriter.loadFile(filePath);
			
			width = Integer.parseInt(settingsList.get(1));
			height = Integer.parseInt(settingsList.get(2));
			
			if (button == easyButton)	//if easy button is pressed
			{
				retries = 7;
				
				minefield = new Minefield(10, 10, 8, "easy", width, height, retries + (3 * Integer.parseInt(settingsList.get(0))));
				minefield.setSize(width, height);
				minefield.setLocationRelativeTo(null);
				minefield.setVisible(true);
			}
			else if (button == mediumButton)
			{
				retries = 3;
				
				minefield = new Minefield(10, 10, 6, "medium", width, height, retries + (2 * Integer.parseInt(settingsList.get(0))));
				minefield.setSize(width, height);
				minefield.setLocationRelativeTo(null);
				minefield.setVisible(true);
			}
			else if (button == hardButton)
			{
				retries = 0;
				
				minefield = new Minefield(10, 10, 4, "hard", width, height, retries + Integer.parseInt(settingsList.get(0)));
				minefield.setSize(width, height);
				minefield.setLocationRelativeTo(null);
				minefield.setVisible(true);
			}
			else if (button == highScoresButton)
			{
				width = 500;
				height = 600;
				
				ScoreBoard score = new ScoreBoard();
				score.setSize(width, height);
				score.setLocationRelativeTo(null);
				score.setVisible(true);
			}
			else if (button == settingsButton)
			{
				width = 300;
				height = 300;
				
				Settings settings = new Settings();
				settings.setSize(width, height);
				settings.setLocationRelativeTo(null);
				settings.setVisible(true);
			}
		
		} //end of mousePressed
		
//-----------------------------------------------------------------------------------------------------------------------------------------------------------
		
		public void mouseReleased(MouseEvent me)
		{
			//Empty
		
		} //end of mouseReleased
		
		public void mouseEntered(MouseEvent me)
		{
			JButton button = (JButton) me.getSource();
			
			if (button == easyButton)
			{
				easyButton.setBackground(Color.green);
			}
			else if (button == mediumButton)
			{
				mediumButton.setBackground(Color.green);
			}
			else if (button ==  hardButton)
			{
				hardButton.setBackground(Color.green);
			}
			else if (button == highScoresButton)
			{
				highScoresButton.setBackground(Color.green);
			}
			else if (button == settingsButton)
			{
				settingsButton.setBackground(Color.green);
			}
			
			revalidate();		//redo layout
			repaint();			//reprint layout
			
		} //end of mouseEntered
		
//-----------------------------------------------------------------------------------------------------------------------------------------------------------
		
		public void mouseExited(MouseEvent me)
		{
			JButton button = (JButton) me.getSource();
			
			if (button == easyButton)
			{
				easyButton.setBackground(null);
			}
			else if (button == mediumButton)
			{
				mediumButton.setBackground(null);
			}
			else if (button ==  hardButton)
			{
				hardButton.setBackground(null);
			}
			else if (button == highScoresButton)
			{
				highScoresButton.setBackground(null);
			}
			else if (button == settingsButton)
			{
				settingsButton.setBackground(null);
			}
			
			revalidate();		//redo layout
			repaint();			//reprint layout
			
		} //end of mouseExited
		
//-----------------------------------------------------------------------------------------------------------------------------------------------------------
		
		public void mouseClicked(MouseEvent me)
		{
			//Empty
		
		} //end of mouseClicked
		
	} //end of subclass MouseHandler
	
//===========================================================================================================================================================
}