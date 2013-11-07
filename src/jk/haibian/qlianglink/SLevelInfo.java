package jk.haibian.qlianglink;

public class SLevelInfo
{
	public int	levelId;
	public int	enabled;
	public int	passed;
	public int	unlockCost;
	public int	maxTime;
	public int	score;
	public int	imageNum;
	public int  rowNum;
	public int  colNum;
	public String iconSet;
	
	public SLevelInfo()
	{
		levelId	= 0;
		enabled	= 0;
		passed	= 0;
		unlockCost = 0;
		maxTime	= 0;
		score		= 0;
		imageNum = 0;
		rowNum   = 0;
		colNum	= 0;
	}
}
