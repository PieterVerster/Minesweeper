//SECTION 1 (line 124): CREATION OF HEADER AND MINEFIELD (METHOD CALLS AS FOUND IN THE CONSTRUCTOR)
//SECTION 2 (line 297): BACKGROUND LOGIC OF HEADER AND MINEFIELD (COUNT FLAGGED BOMBS, CHECK ZERO CELLS, SORT TIMES, REPLACE BUTTON WITH LABEL)
//SECTION 3 (line 433): IMAGES
//SECTION 4 (line 489): GETS AND SETS (GET: FILE PATH, READ TIMES FROM FILE |||| SET: CONSTRAINTS, IMAGE, WRITE TIMES TO FILE)
//SECTION 5 (line 600): ANCILLARY AND FEATURES (DISPLAY MESSAGE, CHANGE NUMBER COLOURS, CREATE TIMER, START TIMER, STOP TIMER, RESET ALL [BOARD])
//SECTION 6 (line 749): SUB-CLASSES (MOUSE HANDLER, TIME LISTENER, FRAME LISTENER)
	
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
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;
import java.io.File;
import java.awt.Color;
import javax.swing.Timer;

public class Minefield extends JFrame
{	
	//Variables for the rows, cols and bombs
	private int rows = 10;
	private int cols = 10;
	private int bombs = rows * cols / 8;
	
	private int bombsFlagged = 0;
	private int cellsRevealed = 0;
	private int cellsFlagged = 0;
	
	//create object of header button
	JButton headerButton = new JButton();
	
	//create objects for panels
	private JPanel headerPanel = new JPanel();
	private JPanel gridPanel = new JPanel();
	
	//create 2d arrays of the buttons and MSCell class
	private JButton [][] grid = new JButton[rows + 2][cols + 2];
	private MSCell[][] field = new MSCell[rows + 2][cols + 2];
	
	//create objects of the constraints, layouts and MouseHandler
	private GridBagConstraints constraints = new GridBagConstraints();
	private GridBagConstraints headConstraints = new GridBagConstraints();
	private GridBagLayout formLayout = new GridBagLayout();
	private MouseHandler mouseHandler = new MouseHandler();
	
	//create colours for numbers and timer
	private final Color PURPLE = new Color(102, 0, 153);
	private final Color BROWN = new Color(102, 51, 0);
	private final Color GOLD = new Color(255, 204, 51);
	private final Color VERY_DARK_RED = new Color(153, 0, 0);
	private final Color DARK_GREY = new Color(102, 102, 102);
	private final Color ORANGE = new Color(255, 102, 0);
	private final Color DARK_GREEN = new Color(0, 153, 0);
	
	//create controls for the header
	private int time = 0;
	private Timer timer = null;
	private JLabel timeLabel;
	private JLabel bestTimeLabel;
	private boolean timerStarted = false;
	private JLabel retryCounterLabel;
	
	//Create ArrayList for best times
	private ArrayList<String> bestTimesList = new ArrayList<String>();
	
	//create bombCounter
	private JLabel bombLabel;
	
	//create ReaderWriter object
	private ReaderWriter readerWriter = new ReaderWriter();
	
	//create modifiers to store difficulty and frame size (default = 800)
	String difficulty = "";
	int frameWidth = 800;
	int frameHeight = 800;
	int retries = 0;
	int initialRetryCounter = 0;
	
	public Minefield()
	{
		//Empty default constructor, not being used	
	}
	
	public Minefield(int r, int c, int bombModifier, String diff, int w, int h, int retry)
	{
		//parameters that will be adjustable in the settings
		rows = r;
		cols = c;
		bombs = rows * cols / bombModifier;
		difficulty = diff;
		frameHeight = h;
		frameWidth = w;
		retries = retry;
		initialRetryCounter = retries;
		
		//add WindowListener to detect when frame is being closed (used to stop timer when Minesweeper window is closed)
		this.addWindowListener(new FrameListener());
		
		//create new 2d array objects with  new rows and cols
		grid = new JButton[rows + 2][cols + 2];
		field = new MSCell[rows + 2][cols + 2];
		
		//set layout and title of the form
		formLayout = new GridBagLayout();
		gridPanel.setLayout(formLayout);
		setTitle("Minesweeper");
		
		constraints.fill = GridBagConstraints.BOTH;			//fill the constraints
		
		createHeader();					//create header for Minefield
		
		constraints.weightx = 0.5;		//set weightx for constraints
		constraints.weighty = 0.5;		//set weighty for constraints
		
		initialiseField();				//Create new objects of all cells
		plantBombs();					//plant bombs in random cells
		countBombs();					//Add counters to each cell if they are near bombs
		setBoard();						//Set properties to each cell
		
	} //end of Minefield()

//===========================================================================================================================================================
	
	//SECTION 1: CREATION OF HEADER AND MINEFIELD (METHOD CALLS AS FOUND IN THE CONSTRUCTOR)
	
//===========================================================================================================================================================	
	
	//Create header for the Minesweeper (Potentially with a timer, reset button and best time)
	public void createHeader()
	{
		GridBagLayout headLayout = new GridBagLayout();		//Create new layout object for header
		
		readTimesFromFile();								//read best times from file for the difficulty chosen, store as ArrayList<String> bestTimesList
		
		if (bestTimesList.size() > 0)						//if bestTimesList has more than one item (or is not null)
		{
			bestTimeLabel = new JLabel("BEST = " + bestTimesList.get(0));	//display best time in bestTimeLabel
		}
		else												//if bestTimesList is empty (or null)
		{
			bestTimeLabel = new JLabel("BEST = N/A");		//display "Not Available"
		}
		
		timeLabel = new JLabel("TIME = 0");								//initialise timeLabel
		bombLabel = new JLabel("BOMBS = " + bombs);						//initialise bombLabel and display amount of bombs
		retryCounterLabel = new JLabel("RETRY COUNTER: " + retries);	//initialise retryCounterLabel and display amount of retries
		
		headerPanel.setLayout(headLayout);					//Set headLayout to panel
		
		headConstraints.gridheight = 1;						//set height for grid
		
		setConstraints(0, 0, 0, 0.5, 0.5, headConstraints);	//set constraints for timeLabel (padY, xPos, yPos, weightx, weighty, GridBagConstraints)
		headerPanel.add(timeLabel, headConstraints);		//add to header panel
		
		setConstraints(10, 0, 1, 0.5, 0.5, headConstraints);//set constraints for bombLabel (padY, xPos, yPos, weightx, weighty, GridBagConstraints)
		headerPanel.add(bombLabel, headConstraints);		//add to header panel
		
		headerButton.setSize(new Dimension(80, 80));		//button size of the header
		
		//Get image from randomly chosen
		ImageIcon headerIcon = chooseIcon(headerButton, "Images\\Header");
		
		headerButton.addMouseListener(mouseHandler);		//add mouse listener
		headerButton.setIcon(headerIcon);					//Set header icon
		
		headConstraints.gridheight = 2;						//set height for grid
		
		setConstraints(0, 1, 0, 0.5, 0.5, headConstraints);	//set constraints for headerButton (padY, xPos, yPos, weightx, weighty, GridBagConstraints)
		headerPanel.add(headerButton, headConstraints);		//add to header panel
		
		headConstraints.gridheight = 1;						//set height for grid
		
		setConstraints(0, 2, 0, 0.5, 0.5, headConstraints);	//set constraints for bestTimeLabel (padY, xPos, yPos, weightx, weighty, GridBagConstraints)
		headerPanel.add(bestTimeLabel, headConstraints);	//add to header panel
		
		setConstraints(0, 2, 1, 0.5, 0.5, headConstraints);	//set constraints for retryCounterLabel (padY, xPos, yPos, weightx, weighty, GridBagConstraints)
		headerPanel.add(retryCounterLabel, headConstraints);	//add to header panel
		
		add(headerPanel, BorderLayout.NORTH);				//add panel to frame
		
		createTimer();										//create timer
		
	} //end of createHeader()
	
//===========================================================================================================================================================
	
	//Initialise each MSCell in the 2d array
	public void initialiseField()
	{
		for (int i = 0; i < rows + 2; i++)
		{
			for (int j = 0; j < cols + 2; j++)
			{
				field[i][j] = new MSCell();
			
			} //end of nested for loop
		} //end of for loop
	} //end of initialiseField()
		
//===========================================================================================================================================================
	
	//Plant bombs on the field
	public void plantBombs()
	{
		ThreadLocalRandom randomNumbers = ThreadLocalRandom.current();
		int bombRow;
		int bombCol;
		int bombCount = 0;
		
		//In the event that there are more bombs than cells
		if (bombs > rows * cols)
		{
			bombCount = rows * cols;	//have bombCount be the same as cell count
		}
		else
		{
			bombCount = bombs;			//else keep bombCount equal to bombs
		}
		
		for (int i = 0; i < bombCount; i++)
		{
			bombRow = randomNumbers.nextInt(rows + 1);		//random row 
			bombCol = randomNumbers.nextInt(cols + 1);		//random col 
			
			if (field[bombRow][bombCol].isBomb() || bombRow == 0 || bombCol == 0)		//If random cell is already bomb, or border is selected
			{
				i--;																	//redo the current bomb
			}
			else																		//otherwise
			{
				field[bombRow][bombCol].setBomb();										//set bomb in field
				System.out.println("BOMB " + (i + 1) + ": " + bombRow + "" + bombCol);
			}
		}
		
	} //end of plantBombs()
	
//===========================================================================================================================================================
	
	//Add counters to each square to show how many bombs are nearby
	public void countBombs()
	{
		int countBombs = 0;
		
		for (int i = 1; i < rows + 1; i++)
		{
			for (int j = 1; j < cols + 1; j++)
			{
				countBombs = 0;
				
				if (!field[i][j].isBomb())
				{
					for (int x = -1; x <= 1; x++)
					{
						for (int y = -1; y <= 1; y++)
						{	
							if (field[i + x][j + y].isBomb())
							{
								countBombs++;		//count bombs around button
							}
						}
					}
					
					field[i][j].setValue(countBombs);		//set value of MSCell 
				}
			}
		}
		
	} //end of countBombs()
	
//===========================================================================================================================================================
	
	//Set properties to each cell
	public void setBoard()
	{
		//Run through array of buttons
		for (int i = 0; i < rows + 2; i++)
		{
			for (int j = 0; j < cols + 2; j++)
			{
				constraints.gridx = i;						//set new x position for each button
				constraints.gridy = j;						//set new y position for each button
			
				if (i == 0 || i == rows + 1|| j == 0 || j == cols + 1)		//if on the border
				{	
					field[i][j].setValue(-100);
					grid[i][j] = new JButton("+");		//Create new object of each button in array
				}
				else
				{
					grid[i][j] = new JButton(field[i][j].toString());		//Create new object of each button in array
					grid[i][j].addMouseListener(mouseHandler);			//add MouseListener to each button
				}
				
				gridPanel.add(grid[i][j], constraints);		//add the new button to the position on the form
			}
		}
		
		add(gridPanel, BorderLayout.CENTER);										//add panel to the frame
	
	} //end of setBoard()
	
//===========================================================================================================================================================


	//SECTION 2: BACKGROUND LOGIC OF HEADER AND MINEFIELD (COUNT FLAGGED BOMBS, CHECK ZERO CELLS, SORT TIMES, REPLACE BUTTON WITH LABEL)

//===========================================================================================================================================================
	
	//Count the flagged cells and bombs and display in the cmd
	public void countFlaggedBombsAndCells(int rows, int cols)
	{
		if (field[rows][cols].isFlagged())
		{
			cellsFlagged++;
			System.out.println("Cells flagged plus: " + cellsFlagged);
		}
		else
		{
			if (cellsFlagged > 0)
			{
				cellsFlagged--;
				System.out.println("Cells flagged minus: " + cellsFlagged);
			}
		}
		
		if (field[rows][cols].isFlagged() && field[rows][cols].isBomb())		//If cell is flagged and is a bomb
		{
			bombsFlagged++;														//count the cell as a flagged bomb
			System.out.println("Bombs flagged plus: " + bombsFlagged);
		}
		else if (field[rows][cols].isBomb())									//otherwise, if cell is unflagged, and is a bomb
		{
			if (bombsFlagged > 0)												//and if a bomb cell has been flagged before
			{
				bombsFlagged--;													//count the cell as unflagged
				System.out.println("Bombs flagged minus: " + bombsFlagged);
			
			} //end of nested if
		} //end of if-else
			
	} //end of countFlaggedBombsAndCells
	
//===========================================================================================================================================================
	
	//Check surrounding cells for any zero cells
	public void checkZeroCells(int row, int col)
	{	
		for (int x = -1; x <= 1; x++)		//for cells from one above, to one below
		{
			for (int y = -1; y <= 1; y++)		//for cells from one left to one right
			{
				MSCell currentCell = field[row + x][col + y];
				
				constraints.gridx = (row + x);		//set x position
				constraints.gridy = (col + y);		//set y position
				
				if (currentCell.getValue() > -1 && !currentCell.isRevealed())		//if cell has zero value and cell is not yet revealed
				{	
					replaceButtonWithLabel(row + x, col + y, constraints);			//replace button with label
					
					if (currentCell.getValue() == 0)		//if current cell has value of zero
					{	
						checkZeroCells(row + x, col + y);	//check for more zero cells
					
					}//end of nested if
				} //end of if
			} //end of nested for loop
		} //end of for loop
	} //end of checkZeroCells()
	
//===========================================================================================================================================================
	
	//sort times in bestTimesList from best to worst, only storing best 3 values
	public void sortTimes()
	{
		String numberOnly = "";			//String for using only number value
		int min = 999;					//set minimum default to 999
		int middle = 999;				//set middle value default to 999
		int max = 999;					//set maximum value default to 999
		
		if (bestTimesList.size() > 0)							//if bestTimesList is not empty (min stored items is 0)
		{
			numberOnly = bestTimesList.get(0);					//set numberOnly to the best time in bestTimesList
			numberOnly = numberOnly.replace(" seconds", "");	//remove the string part to keep only the int part of the string
			min = Integer.parseInt(numberOnly);					//set minimum to best time found
		}
		
		if (bestTimesList.size() > 1)							//if bestTimesList has more than one value saved
		{
			numberOnly = bestTimesList.get(1);					//set numberOnly to middle value in bestTimesList
			numberOnly = numberOnly.replace(" seconds", "");	//remove the string part to keep only the int part of the string
			middle = Integer.parseInt(numberOnly);				//set middle to middle time found
		}
		
		if (bestTimesList.size() > 2)							//if bestTimesList has more than two values saved (max stored items is 3)
		{
			numberOnly = bestTimesList.get(2);					//set numberOnly to last value in bestTimesList
			numberOnly = numberOnly.replace(" seconds", "");	//remove the string part to keep only the int part of the string
			max = Integer.parseInt(numberOnly);					//set maximum to last time found
		}
		
		if (time < min)			//if new time is faster than best time
		{
			middle = min;		//move fastest time down to middle time
			min = time;			//set new time as best time
		}
		else if (time < middle)	//if new time is faster than middle time
		{
			max = middle;		//move middle time down to slowest time
			middle = time;		//set new time as middle time
		}
		else if (time < max)	//if new time is faster than slowest time
		{
			max = time;			//replace slowest time with new time
		}
		
		System.out.println("MIN: " + min);
		System.out.println("MID: " + middle);
		System.out.println("MAX: " + max);
		
		bestTimesList.clear();		//clear best times list, in order to set new changes
		
		bestTimesList.add(String.valueOf(min));		//add new fastest time
		bestTimesList.add(String.valueOf(middle));	//add new middle time
		bestTimesList.add(String.valueOf(max));		//add new slowest time
	
	} //end of sortTimes()

//===========================================================================================================================================================

	//replace button with a label
	public void replaceButtonWithLabel(int row, int col, GridBagConstraints constr)
	{
		if (field[row][col].isFlagged())	//if a cell is being revealed autonomously
		{
			field[row][col].setFlagged();	//unflag to enable revealing
		}
		
		field[row][col].setRevealed();		//reveal cell
		cellsRevealed++;					//increment cells revealed
		
		JLabel label = new JLabel(field[row][col].toString());	//create new label with MSCell display
		
		label.setHorizontalAlignment(SwingConstants.CENTER);	//set text to center
		changeNumberColours(label);								//set colour to number, depending on its value
		
		gridPanel.remove(grid[row][col]);						//remove button
		gridPanel.add(label, constr);							//add label
	
	} //end of replaceButtonWithLabel()
	
//===========================================================================================================================================================
	
	
	//SECTION 3: IMAGES (chooseIcon)

//===========================================================================================================================================================
	
	//Return random Image Icon from an array of icon paths
	public ImageIcon chooseIcon(JButton button, String filePath)
	{
		button.setMargin(new Insets(0, 0, 0, 0));		//Set margin around button
		String[] fileImages = new String[30];			//create String array for multiple images
		int filesStored = 0;							//counter for amount of files stored
		
		for (int i = 0; i < fileImages.length; i++)
		{
			File f = new File(filePath + "\\img" + (i + 1) + ".jpg");	//create new file from img i in file
			
			if (f.exists())									//if file exists
			{
				fileImages[i] = "img" + (i + 1) + ".jpg";	//add to fileImages array
				filesStored++;								//increment counter for amount of images stored
			}
		}
		
		int random = new Random().nextInt(filesStored);								//choose random number for images in array
		
		System.out.println("Image file used: " + fileImages[random]);
		ImageIcon imageIcon = new ImageIcon(filePath + "\\" + fileImages[random]);	//create new Icon from random image stored
		Image image = imageIcon.getImage();											//get the icon image
		
		if (filePath.contains("Header"))												//if image is for header
		{
			image = image.getScaledInstance(button.getWidth(), button.getHeight(), java.awt.Image.SCALE_SMOOTH);		//scale image
		}
		else																			//if image is for minefield
		{
			button.setMargin(new Insets(-40, -40, -40, -40));						//Set margin around button
			
			if (filePath.contains("Bombs"))													//if filePath is for bombs
			{
				System.out.println("Bomb");
			}
			
			image = image.getScaledInstance(button.getWidth(), button.getHeight(), java.awt.Image.SCALE_SMOOTH);		//scale image
		}
		
		imageIcon = new ImageIcon(image);		//set image as new image icon
		
		return imageIcon;
	
	} //end of chooseIcon()
	
//===========================================================================================================================================================


	//SECTION 4: GETS AND SETS (GET: FILE PATH, READ TIMES FROM FILE |||| SET: CONSTRAINTS, IMAGE, WRITE TIMES TO FILE)

//===========================================================================================================================================================

	//get file path of saved times
	public String getFilePath()
	{
		String filePath = "";
		
		if (difficulty == "easy")	//if difficulty is easy, get easy filePath
		{
			filePath = "Best Times\\easyTimes.txt";
		}
		else if (difficulty == "medium")	//if difficulty is medium, get medium filePath
		{
			filePath = "Best Times\\mediumTimes.txt";
		}
		else if (difficulty == "hard")		//if difficulty is hard, get hard filePath
		{
			filePath = "Best Times\\hardTimes.txt";
		}
		
		return filePath;
	
	} //end of getFilePath()

//===========================================================================================================================================================


	//set constraints as needed
	public void setConstraints(int padY, int x, int y, double wx, double wy, GridBagConstraints constr)
	{
		constr.ipady = padY;	//set width for button
		constr.gridx = x;		//set new x position for button
		constr.gridy = y;		//set new y position for button
		constr.weightx = wx;	//set weightx for button
		constr.weighty = wy;	//set weighty for button
	
	} //end of setConstraints()
	
//===========================================================================================================================================================

	//set image on button of type
	public void setImage(JButton button, MSCell cell, String type)
	{
		ImageIcon buttonIcon = null;
		
		if (!cell.isFlagged())										//if cell is not flagged
		{
			if (type == "bomb")										//if button is a bomb
			{
				buttonIcon = chooseIcon(button, "Images\\Bombs");	//create new icon from bombs images
				
				button.setIcon(buttonIcon);							//set icon to button
			}			
			else													//if button is not a bomb
			{
				button.setIcon(null); 								//remove icon from button
			}
		}
		else														//if cell is flagged, get image icon...
		{
			if (type == "flag")										//if button is a flag
			{
				buttonIcon = chooseIcon(button, "Images\\Flag");	//create new button from flag images
			}
			
			button.setIcon(buttonIcon);								//set icon to button
		}
	
	} //end of setImage()
	
//===========================================================================================================================================================
	
	//write best times to file
	public void writeTimesToFile(ArrayList<String> timeList)
	{	
		String filePath = getFilePath();	//get file path depending on difficulty level
		
		if (filePath != "")
		{
			readerWriter.saveFile(filePath, timeList);		//save times to file from bestTimesList
		}
		else
		{
			System.out.println("File path does not exist. Times could not be written to the file.");
		}
	
	} //end of writeTimesToFile()
	
//===========================================================================================================================================================
	
	//read best times from file
	public void readTimesFromFile()
	{
		String filePath = getFilePath();	//get file path depending on difficulty level
		
		if (filePath != "")	//if file path exists
		{
			bestTimesList = readerWriter.loadFile(filePath);	//load times from file to bestTimesList
		}
		else
		{
			System.out.println("File path does not exist. Times could not be read from the file.");
		}
	
	} //end fo readTimesFromFile()
	
//===========================================================================================================================================================

	
	//SECTION 5: ANCILLARY AND FEATURES (DISPLAY MESSAGE, CHANGE NUMBER COLOURS, CREATE TIMER, START TIMER, STOP TIMER, RESET RETRY COUNTER, RESET ALL [BOARD])
	
//===========================================================================================================================================================

	
	//display messages (only for win and lose at the moment)
	public void displayMessage(String condition, JButton button)
	{
		JFrame msgFrame = new JFrame("Message");
		int answer = -1;
		
		if (condition.equals("win"))						//if the conditions are to win
		{	
			if (cellsRevealed == (rows * cols) - bombs)		//if the cells revealed is equal to the empty cells
			{
				stopTimer();						//stop the timer
				readTimesFromFile();				//read all times from file
				sortTimes();						//sort times from fastest to slowest
				writeTimesToFile(bestTimesList);	//save times to file
				answer = JOptionPane.showConfirmDialog(msgFrame, "YOU WIN! Restart?", "You revealed all fields!", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);	//you win the game
			}
			else if (bombsFlagged == bombs && cellsFlagged == bombs)				//if all bombs have been flagged and all flags are bombs
			{
				stopTimer();						//stop the timer
				readTimesFromFile();				//read all times from file
				sortTimes();						//sort times from fastest to slowest
				writeTimesToFile(bestTimesList);	//save times to file				
				answer = JOptionPane.showConfirmDialog(msgFrame, "YOU WIN! Restart?", "You flagged all bombs!", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);	//you win the game			
			}
			
			if (answer == JOptionPane.YES_OPTION)
			{
				resetRetryCounter();
				resetAll();		//reset minefield
			}
			else
			{
				//DO NOTHING
			}
		}
		else if (condition.equals("lose"))		//else if the conditions are to lose
		{
			stopTimer();
			
			if(retries > 0)
			{
				retries--;
				
				retryCounterLabel.setText("RETRY COUNTER: " + retries);
				stopTimer();
				answer = JOptionPane.showConfirmDialog(msgFrame, "YOU LOSE! Restart?", "You clicked on a bomb!", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);		//you lose the game
			}
			else
			{
				retries--;
				JOptionPane.showMessageDialog(msgFrame, "YOU LOSE!", "Game Over", JOptionPane.WARNING_MESSAGE);		//you lose the game
			}
				
			if (answer == JOptionPane.YES_OPTION || retries < 0 || retries == initialRetryCounter)
			{
				resetRetryCounter();		//reset retry counter
				resetAll();		//reset minefield
			}
			else
			{
				startTimer();			//start timer
				button.setIcon(null);
			}
		}
		else
		{
			//Do Nothing	
		} //end of if-elseIf-else
		
	} //end of displayMessage()
	
//===========================================================================================================================================================
	
	//change colour of number in cell, depending on its value
	public void changeNumberColours(JLabel label)
	{
		if (label.getText().equals("1"))	//if value is 1
		{
			label.setForeground(Color.BLUE);	//set to blue
		}
		else if (label.getText().equals("2"))	//if value is 2
		{
			label.setForeground(Color.ORANGE);	//set to orange
		}
		else if (label.getText().equals("3"))	//if value is 3
		{
			label.setForeground(Color.RED);		//set to red
		}
		else if (label.getText().equals("4"))	//if value is 4
		{
			label.setForeground(PURPLE);		//set to purple
		}
		else if (label.getText().equals("5"))	//if value is 5
		{
			label.setForeground(BROWN);			//set to brown
		}
		else if (label.getText().equals("6"))	//if value is 6
		{
			label.setForeground(GOLD);			//set to gold
		}
		else if (label.getText().equals("7"))	//if value is 7
		{
			label.setForeground(VERY_DARK_RED);	//set to very dark red
		}
		else if (label.getText().equals("8"))	//if value is 8 (max value)
		{
			label.setForeground(DARK_GREY);		//set to dark grey
		}
		
	} //end of changeNumberColours()
	
//===========================================================================================================================================================
	
	//create timer
	public void createTimer()
	{
		time = 0;	//set time to 0
		timer = new Timer(1000, new TimeListener());	//create new timer object with 1 second speed and TimeListener
	
	} //end of createTimer()
	
//===========================================================================================================================================================
	
	//Start the timer
	public void startTimer()
	{
		timerStarted = true;	//set timerStarted to true
		timer.start();			//start the timer
	
	} //end of startTimer()
	
//===========================================================================================================================================================
	
	//Stop the timer
	public void stopTimer()
	{
		timerStarted = false;	//set timerStarted to false
		timer.stop();			//stop the timer
	
	} //end of stopTimer()
	
//===========================================================================================================================================================
	
	//reset retry counter
	public void resetRetryCounter()
	{
		retries = initialRetryCounter;
	}
	
	//remove frame and display new frame with new properties
	public void resetAll()
	{
		retries = initialRetryCounter;
		stopTimer();	//stop the timer
		dispose();	//dispose of the current frame
		Minefield minefield = new Minefield(rows, cols, (rows*cols/bombs), difficulty, frameWidth, frameHeight, retries);	//create new instance of the same frame
		minefield.setSize(frameWidth, frameHeight);
		minefield.setLocationRelativeTo(null);
		minefield.setVisible(true);
	
	} //end of resetAll()
	
//===========================================================================================================================================================

	
	//SECTION 6: SUB-CLASSES (MOUSE HANDLER, TIME LISTENER, FRAME LISTENER)

//===========================================================================================================================================================

	//subclass for MouseHandler
	public class MouseHandler implements MouseListener
	{
		GridBagConstraints constraints = new GridBagConstraints();
		
//-----------------------------------------------------------------------------------------------------------------------------------------------------------
		
		public void mousePressed(MouseEvent me)
		{
			JButton button = (JButton) me.getSource();
			
			if (button == headerButton)		//if header button is clicked
			{
				resetAll();					//reset the board
			}
			
			//Run through array of buttons
			for (int i = 1; i < rows + 1; i++)
			{
				for (int j = 1; j < cols + 1; j++)
				{
					if (button == grid[i][j])		//if button is found
					{
						constraints.gridx = i;		//set x position
						constraints.gridy = j;		//set y position
						
						if (me.getButton() == MouseEvent.BUTTON1)		//left click
						{
							if (!timerStarted)		//if timer has not started
							{
								startTimer();		//start timer
								System.out.println("if not started works: " + timerStarted);	//show that timer has started
							}
							
							if (!field[i][j].isFlagged())		//if cell is not flagged
							{
								if (field[i][j].isBomb())		//if cell is a bomb
								{
									setImage(grid[i][j], field[i][j], "bomb");		//set bomb image when clicked
									displayMessage("lose", grid[i][j]);				//display lose message
								}
								else if (field[i][j].getValue() == 0)				//else if cell has zero value
								{
									checkZeroCells(i, j);							//check surrounding cells for zero values until not found
								}						
								else if (field[i][j].getValue() > 0)				//else if cell has positive value
								{
									replaceButtonWithLabel(i, j, constraints);		//replace button with label
								}
							}
						}
						else if (me.getButton() == MouseEvent.BUTTON3)		//right click
						{		
							field[i][j].setFlagged();						//Set cell to flagged
							
							grid[i][j].setText(field[i][j].toString());		//if flagged, print toString to show flagged, if necessary for testing toString
							
							setImage(grid[i][j], field[i][j], "flag");		//set flag image to show flagged

							countFlaggedBombsAndCells(i, j);						//Count flagged bombs and cells
						}
						
						revalidate();							//redo layout
						repaint();								//reprint layout
						displayMessage("win", grid[i][j]);		//check win and display win message
						
					} //end of if (button = grid[i][j])
				} //end of nested for
			} //end of for
		} //end of mousePressed
		
//-----------------------------------------------------------------------------------------------------------------------------------------------------------
		
		public void mouseReleased(MouseEvent me)
		{
			//Empty
		
		} //end of mouseReleased
		
		public void mouseEntered(MouseEvent me)
		{
			JButton button = (JButton) me.getSource();
			
			//Run through array of buttons
			for (int i = 1; i < rows + 1; i++)
			{
				for (int j = 1; j < cols + 1; j++)
				{
					if (button == grid[i][j])						//if button is found
					{
						grid[i][j].setBackground(Color.GREEN);		//change backround to show active cell
						
						revalidate();								//redo layout
						repaint();									//reprint layout
					
					} //end of if (button == grid[i][j])
				} //end of nested for loop
			} //end of for loop
		} //end of mouseEntered
		
//-----------------------------------------------------------------------------------------------------------------------------------------------------------
		
		public void mouseExited(MouseEvent me)
		{
			JButton button = (JButton) me.getSource();
			
			//Run through array of buttons
			for (int i = 1; i < rows + 1; i++)
			{
				for (int j = 1; j < cols + 1; j++)
				{
					if (button == grid[i][j])		//if button is found
					{
						grid[i][j].setBackground(null);		//reset backround to inactive cell
						
						revalidate();		//redo layout
						repaint();			//reprint layout
					
					} //end of if (button == grid[i][j])
				} //end of nested for loop
			} //end of for loop
		} //end of mouseExited
		
//-----------------------------------------------------------------------------------------------------------------------------------------------------------
		
		public void mouseClicked(MouseEvent me)
		{
			//Empty
		
		} //end of mouseClicked
		
	} //end of subclass MouseHandler
	
//===========================================================================================================================================================
	
	//subclass for TimeListener
	public class TimeListener implements ActionListener
	{	
		public void actionPerformed(ActionEvent e)
		{	
			time++;		//increment time with one second
			
			if (bestTimesList.size() > 0)		//if there is a best times file
			{
				if (time < Integer.parseInt(bestTimesList.get(0).replace(" seconds", "")))	//if time better than the best time on file
				{
					timeLabel.setForeground(DARK_GREEN);	//display as dark green
				}
				else if (time < Integer.parseInt(bestTimesList.get(2).replace(" seconds", "")))	//else if time is better than worst time on file
				{
					timeLabel.setForeground(ORANGE);	//display as orange
				}
				else	//if time won't make it on the top 3
				{
					timeLabel.setForeground(Color.RED);		//display as red
				}
			}
			else	//if best times list does not exist on file
			{
				timeLabel.setForeground(DARK_GREEN);	//display only dark green
			}
			
			timeLabel.setText("TIME: " + time);		//set text to time label
			System.out.println("Time is: " + time);
		}
	} //end of subclass TimeListener
	
//===========================================================================================================================================================
	
	//FrameListener when frame is being closed
	public class FrameListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			timer.stop();
		}
	}

}	//end of class Minefield