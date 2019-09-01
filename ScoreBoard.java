import javax.swing.*;
import java.awt.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.Object;
import java.util.*;

public class ScoreBoard extends JFrame
{
	private GridBagConstraints constraints = new GridBagConstraints();
	private GridBagLayout formLayout = new GridBagLayout();
	private MouseHandler mouseHandler = new MouseHandler();
	private ReaderWriter readWrite = new ReaderWriter();
	
	private String filePath = "Best Times\\bestTimes.txt";
	
	//Create object of panel
	private JPanel scorePanel = new JPanel();
	
	//Create references to all buttons
	private JButton acceptButton;
	private JButton resetEasyButton;
	private JButton resetMediumButton;
	private JButton resetHardButton;
	
	//Create references to all labels
	private JLabel easyHeading;
	private JLabel mediumHeading;
	private JLabel hardHeading;
	private JTextArea easyScores;
	private JTextArea mediumScores;
	private JTextArea hardScores;
	
	//Array List for the scores
	private ArrayList<String> scoreList = new ArrayList<String>();
	
	public ScoreBoard()
	{
		//set layout and title of the form
		scorePanel.setLayout(formLayout);
		setTitle("Minesweeper - ScoreBoard");
		
		initializeButtons();		//initialize buttons
		
		initializeLabels();			//initialize labels
		
		addMouseListeners();		//add mouse listeners
		
		constraints.fill = GridBagConstraints.HORIZONTAL;		//Fill constraints horizontally
		
		//Set constraints and add heading, scores and reset button for easy scores
		setConstraints(1, 20, 0, 0);
		scorePanel.add(easyHeading, constraints);
		setConstraints(2, 20, 0, 1);
		scorePanel.add(easyScores, constraints);	
		setConstraints(1, 20, 2, 1);
		scorePanel.add(resetEasyButton, constraints);
		
		//Set constraints and add heading, scores and reset button for medium scores
		setConstraints(1, 20, 0, 2);
		scorePanel.add(mediumHeading, constraints);
		setConstraints(2, 20, 0, 3);
		scorePanel.add(mediumScores, constraints);
		setConstraints(1, 20, 2, 3);
		scorePanel.add(resetMediumButton, constraints);
		
		//Set constraints and add heading, scores and reset button for hard scores
		setConstraints(1, 20, 0, 4);
		scorePanel.add(hardHeading, constraints);
		setConstraints(2, 20, 0, 5);
		scorePanel.add(hardScores, constraints);
		setConstraints(1, 20, 2, 5);
		scorePanel.add(resetHardButton, constraints);
		
		constraints.insets = new Insets(50, 0, 0, 0);		//set insets of button
		setConstraints(4, 20, 0, 6);
		scorePanel.add(acceptButton, constraints);
		
		add(scorePanel);
		
	}	//end of ScoreBoard()
	
//===========================================================================================================================================================
	
	//initialize all buttons
	public void initializeButtons()
	{
		acceptButton = new JButton("Accept");
		resetEasyButton = new JButton("Reset Easy Times");
		resetMediumButton = new JButton("Reset Medium Times");
		resetHardButton = new JButton("Reset Hard Times");
		
	} //end of initializeButtons()
	
//===========================================================================================================================================================
	
	//add mouse listeners to all buttons
	public void addMouseListeners()
	{
		acceptButton.addMouseListener(mouseHandler);
		resetEasyButton.addMouseListener(mouseHandler);
		resetMediumButton.addMouseListener(mouseHandler);
		resetHardButton.addMouseListener(mouseHandler);
	
	} //end of addMouseListeners()

//===========================================================================================================================================================
	
	//load best times from file
	public ArrayList<String> getScores(String difficulty)
	{
		ArrayList<String> scores = new ArrayList<String>();
		
		if (difficulty == "easy")
		{
			filePath = "Best Times\\easyTimes.txt";
		}
		else if (difficulty == "medium")
		{
			filePath = "Best Times\\mediumTimes.txt";
		}
		else if (difficulty == "hard")
		{
			filePath = "Best Times\\hardTimes.txt";
		}
		
		if (readWrite.loadFile(filePath).size() == 0)
		{
			scores.add("999");
			scores.add("999");
			scores.add("999");
			
			System.out.println("Default times used for " + difficulty + " difficulty");
		}
		else
		{
			scores = readWrite.loadFile(filePath);
		}
		
		return scores;
	
	}	//end of getScores()
	
//===========================================================================================================================================================
	
	//initialize all labels
	public void initializeLabels()
	{
		easyHeading = new JLabel("Easy: ");
		mediumHeading = new JLabel("Medium: ");
		hardHeading = new JLabel("Hard: ");
		
		easyScores = new JTextArea("");
		easyScores.setEditable(false);
		
		mediumScores = new JTextArea("");
		mediumScores.setEditable(false);
		
		hardScores = new JTextArea("");
		hardScores.setEditable(false);
		
		reloadScores();
		
	}	//end of initializeLabels()

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
	
	//delete file from saved times
	public void deleteFile(String filePath)
	{
		readWrite.deleteFile(filePath);
		revalidate();		//redo layout
		repaint();			//reprint layout
	
	}  //end of deleteFile()

//===========================================================================================================================================================

	//Get scores from file and reload them onto the text areas
	public void reloadScores()
	{
		String scores = "";
		
		scoreList = getScores("easy");
		
		for (int i = 0; i < scoreList.size(); i++)
		{
			scores += scoreList.get(i) + " seconds \n";
		}
		
		easyScores.setText(scores);
		
		scores = "";
		scoreList = getScores("medium");
		
		for (int i = 0; i < scoreList.size(); i++)
		{
			scores += scoreList.get(i) + " seconds \n";
		}
		
		mediumScores.setText(scores);
		
		scores = "";
		scoreList = getScores("hard");
		
		for (int i = 0; i < scoreList.size(); i++)
		{
			scores += scoreList.get(i) + " seconds \n";
		}
		
		hardScores.setText(scores);
	}
	
//===========================================================================================================================================================
	
	//subclass for MouseHandler
	public class MouseHandler implements MouseListener
	{
		GridBagConstraints constraints = new GridBagConstraints();
		
//-----------------------------------------------------------------------------------------------------------------------------------------------------------
		
		public void mousePressed(MouseEvent me)
		{
			JButton button = (JButton) me.getSource();
			
			if (button == acceptButton)
			{
				System.out.println("Accept button");
				dispose();
			}
			else
			{
				System.out.println("Reset buttons");
				
				if (button == resetEasyButton)
				{
					filePath = "Best Times\\easyTimes.txt";
				}
				else if (button == resetMediumButton)
				{
					filePath = "Best Times\\mediumTimes.txt";
				}
				else if (button == resetHardButton)
				{	
					filePath = "Best Times\\hardTimes.txt";
				}
				
				deleteFile(filePath);
				reloadScores();
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
}	//end of class ScoreBoard