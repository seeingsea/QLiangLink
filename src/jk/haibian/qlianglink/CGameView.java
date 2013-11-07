package jk.haibian.qlianglink;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CGameView extends View
{
	private SLevelInfo levelInfo;
	private CLinkGraph linkGraph;
	private int cellWidth = 60;
	private int cellHeight = 60;
	private CPiece selPiece;
	private Bitmap selEffect;
	private Paint paint;
	private CLinkLine linkLine;
	private Point startP, endP;
	private SoundPool sound;
	private int sid;

	public CGameView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		Log.d("CGameView", "constructor");
		this.paint = new Paint();
		// this.paint.setAntiAlias(true);
		// this.paint.setARGB(255, 255, 0, 0);
		this.paint.setStyle(Paint.Style.STROKE);
		this.paint.setStrokeWidth(9);
	}

	public boolean initGraph(SLevelInfo level, SoundPool soundPlay, int sid_erease)
	{
		Log.d("CGameView", "initGraph");
		levelInfo = level;
		sound = soundPlay;
		sid = sid_erease;
		
		// 使用位图平铺作为连接线条
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
		this.paint.setShader(new BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
		// 选中效果图
		this.selEffect = BitmapFactory.decodeResource(getResources(), R.drawable.selected);
		// 连连看的图块
		linkGraph = new CLinkGraph(getContext());
		int iconNum = level.rowNum <= 10 ? 12 : 14;
		if (level.levelId == 5 || level.levelId == 6) {
			iconNum = 12;
		}
		boolean bRetn = linkGraph.initial(level.rowNum, level.colNum, level.iconSet, iconNum);
		return bRetn;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		// Log.d("CGameView", "onDraw");
		if (levelInfo != null && linkGraph != null) {
			cellWidth = getWidth() / levelInfo.colNum;
			cellHeight = cellWidth;

			CPiece[][] pieces = linkGraph.getPieces();
			if (pieces != null) {
				drawPieces(canvas, pieces);
			}

			if (selPiece != null) {
				drawSelFrame(canvas);
			}

			if (linkLine != null) {
				drawLine(canvas, linkLine);
				linkGraph.erasePiece(startP, endP);
				linkLine = null;
			}
		}
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	}

	private void drawPieces(Canvas canvas, CPiece[][] pieces)
	{
		Rect src = new Rect(0, 0, cellHeight, cellHeight);
		Rect dst = new Rect(0, 0, cellHeight, cellWidth);
		// 遍历pieces二维数组
		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces[i].length; j++) {
				// 如果二维数组中该元素不为空（即有方块），将这个方块的图片画出来
				if (pieces[i][j] != null) {
					// 得到这个Piece对象
					CPiece piece = pieces[i][j];
					src.right = piece.getWidth();
					src.bottom = piece.getHeight();
					dst.left = i * cellWidth;
					dst.top = j * cellHeight;
					dst.right = dst.left + cellWidth;
					dst.bottom = dst.top + cellHeight;
					// 根据方块左上角X、Y座标绘制方块
					canvas.drawBitmap(piece.getImage(), src, dst, null);
				}
			}
		}
	}

	protected void drawSelFrame(Canvas canvas)
	{
		int col = selPiece.getIndexX();
		int row = selPiece.getIndexY();
		Rect src = new Rect(0, 0, selEffect.getWidth(), selEffect.getHeight());
		Rect dst = new Rect(0, 0, cellWidth, cellHeight);
		dst.offset(col * cellWidth, row * cellHeight);
		canvas.drawBitmap(selEffect, src, dst, null);
	}

	private void drawLine(Canvas canvas, CLinkLine line)
	{
		int cellRadius = cellWidth / 2;
		// 获取LinkInfo中封装的所有连接点
		List<Point> points = linkLine.getLinkPoints();
		// 依次遍历linkInfo中的每个连接点
		for (int i = 0; i < points.size() - 1; i++) {
			// 获取当前连接点与下一个连接点
			Point currentPoint = points.get(i);
			Point nextPoint = points.get(i + 1);
			// 绘制连线
			canvas.drawLine(	currentPoint.x * cellWidth + cellRadius, currentPoint.y * cellHeight + cellRadius,
									nextPoint.x * cellWidth + cellRadius, nextPoint.y * cellHeight + cellRadius,
									this.paint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// Log.d("CGameView", "onTouchEvent");
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			int x = (int)(event.getX() / cellWidth);
			int y = (int)(event.getY() / cellHeight);
			CPiece pickPiece = linkGraph.pickPiece(x, y);

			if (pickPiece != null) {
				if (selPiece == null) {
					selPiece = pickPiece;
				}
				else {
					Point pnt1 = selPiece.getIndexXY();
					Point pnt2 = pickPiece.getIndexXY();
					linkLine = linkGraph.link(pnt1, pnt2);
					if (linkLine != null) {
						//linkGraph.erasePiece(pnt1, pnt2);
						startP = pnt1;
						endP = pnt2;
						selPiece = null;
						sound.play(sid, 0.4f, 0.4f, 0, 0, 1);
					}
					else {
						selPiece = pickPiece;
					}
				}
			}
			return true;
		}
		else if (event.getAction() == MotionEvent.ACTION_UP) {
			postInvalidate();
		}
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}

	public boolean isCompleted()
	{
		return linkGraph.isCompleted();
	}

}
