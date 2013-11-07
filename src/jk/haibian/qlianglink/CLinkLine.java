package jk.haibian.qlianglink;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;

public class CLinkLine
{
	// 创建一个集合用于保存连接点
	private List<Point> points = new ArrayList<Point>();

	public CLinkLine()
	{
		
	}
	
	public void addPoint(Point p)
	{
		points.add(p);
	}

	public void clear()
	{
		points.clear();
	}

	// 返回连接集合
	public List<Point> getLinkPoints()
	{
		return points;
	}
}
