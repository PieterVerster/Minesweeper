public class MSCell
{
	private boolean revealed;
	private boolean bomb;
	private int value;
	private boolean flagged;
	
	public MSCell()
	{
		revealed = false;
		bomb = false;
		setValue(0);
		flagged = false;
	}
	
	public void setRevealed()
	{
		if(!revealed)		//if not revealed
		{
			revealed = true;	//reveal
		}
		else		//if revealed
		{
			revealed = false;	//unreveal
		}
	
	} //end of setRevealed()
	
	public boolean isRevealed()
	{
		return revealed;
	
	} //end of isRevealed()
	
	public void setFlagged()
	{
		if(flagged)		//if flagged
		{
			flagged = false;	//unflag
		}
		else	//if not flagged
		{
			flagged = true;		//flag
		}
	
	} //end of setFlagged()
	
	public boolean isFlagged()
	{
		return flagged;
	
	} //end of isFlagged()
	
	public void setBomb()
	{
		bomb = true;
	
	} //end of setBomb()
	
	public boolean isBomb()
	{
		return bomb;
	
	} //end of isBomb()
	
	public void setValue(int v)
	{
		value = v;
	
	} //end of setValue()
	
	public int getValue()
	{
		return value;
	
	} //end of getValue()
	
	public String toString()
	{
		if (isFlagged())	//if flagged
		{
			return "";	//return empty string (as icon will be added)
		}
		else	//else if not flagged
		{
			if (isBomb())	//and is a bomb
			{
				return "";	//return empty string (as icon will be added)
			}
			else	//else if not bomb
			{			
				if (isRevealed())	//and is revealed
				{
					if (getValue() > 0)		//and value is more than zero
					{
						return getValue() + "";	//show value
					}
					else	//else if zero or less
					{
						return " ";	//return blank space
					}
				}
				else	//else if not revealed
				{
					return " ";	//return blank space
				
				} //end of if-else (isRevealed)
			
			} //end of if-else (isBomb)
		
		} //end of if-else (isFlagged)
	
	} //end of toString()

} //end of class MSCell