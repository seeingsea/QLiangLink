package jk.haibian.qlianglink;

import android.graphics.Bitmap;
import android.graphics.Point;

public class CPiece
{
	private Bitmap image;
	private int imageId;
	// 该对象在Piece[][]数组中第一维的索引值
	private int indexX;
	// 该对象在Piece[][]数组中第二维的索引值
	private int indexY;

	public CPiece()
	{
	}

	public Bitmap getImage()
	{
		return image;
	}

	public void setImage(Bitmap image)
	{
		this.image = image;
	}

	public int getImageId()
	{
		return imageId;
	}

	public void setImageId(int imageId)
	{
		this.imageId = imageId;
	}

	public int getWidth()
	{
		if (image != null) {
			return image.getWidth();
		}
		return 0;
	}

	public int getHeight()
	{
		if (image != null) {
			return image.getHeight();
		}
		return 0;
	}

	public int getIndexX()
	{
		return indexX;
	}

	public void setIndexX(int indexX)
	{
		this.indexX = indexX;
	}

	public int getIndexY()
	{
		return indexY;
	}

	public void setIndexY(int indexY)
	{
		this.indexY = indexY;
	}

	public boolean isSameImage(CPiece other)
	{
		return this.imageId == other.imageId;
	}

	public Point getIndexXY()
	{
		Point pnt = new Point(indexX, indexY);
		return pnt;
	}
}
