package jk.haibian.qlianglink;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;

public class CLinkLine
{
	// ����һ���������ڱ������ӵ�
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

	// �������Ӽ���
	public List<Point> getLinkPoints()
	{
		return points;
	}
}
