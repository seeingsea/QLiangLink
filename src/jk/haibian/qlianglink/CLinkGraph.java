package jk.haibian.qlianglink;

import android.content.Context;
import android.graphics.Point;

public class CLinkGraph
{
	private Context context;
	private CPiece[][] linkPieces;
	private int pieceNum;

	public CLinkGraph(Context context)
	{
		this.context = context;
	}

	public boolean initial(int rowNum, int colNum, String iconSet, int iconNum)
	{
		CLinkPieceBuilder builder = new CLinkPieceBuilder(context);
		linkPieces = builder.createPieces(rowNum, colNum, iconSet, iconNum);
		pieceNum = (rowNum-2) * (colNum-2);

		return (linkPieces != null);
	}

	public CPiece[][] getPieces()
	{
		return linkPieces;
	}

	public CPiece pickPiece(int x, int y)
	{
		if (0 < x && x < linkPieces.length) {
			if (0 < y && y < linkPieces[x].length) {
				return linkPieces[x][y];
			}
		}
		return null;
	}

	public void erasePiece(CPiece selPiece, CPiece pickPiece)
	{
		for (int i = 0; i < linkPieces.length; i++) {
			for (int j = 0; j < linkPieces[i].length; j++) {
				if (linkPieces[i][j].equals(selPiece) || linkPieces[i][j].equals(pickPiece)) {
					linkPieces[i][j] = null;
				}
			}
		}
	}

	public void erasePiece(Point pnt1, Point pnt2)
	{
		if (linkPieces[pnt1.x][pnt1.y] != null) {
			linkPieces[pnt1.x][pnt1.y] = null;
			pieceNum--;
		}
		if (linkPieces[pnt2.x][pnt2.y] != null) {
			linkPieces[pnt2.x][pnt2.y] = null;
			pieceNum--;
		}
	}

	public CLinkLine link(Point pa, Point pb)
	{
		CPiece piece1 = pickPiece(pa.x, pa.y);
		CPiece piece2 = pickPiece(pb.x, pb.y);
		if (!piece1.isSameImage(piece2)) {
			return null;
		}

		CLinkLine linkLine = new CLinkLine();

		if (directLink(pa, pb, linkLine)) {
			return linkLine;
		}
		else if (oneCornerLink(pa, pb, linkLine)) {
			return linkLine;
		}
		else if (towCornerLink(pa, pb, linkLine)) {
			return linkLine;
		}
		return null;
	}

	private boolean directLink(Point pa, Point pb, CLinkLine line)
	{
		if (pa.x == pb.x && pa.y != pb.y) {
			int dist = pa.y - pb.y;
			if (Math.abs(dist) > 1) {
				int y1 = pa.y < pb.y ? pa.y : pb.y;
				int y2 = pa.y > pb.y ? pa.y : pb.y;
				for (int j = y1 + 1; j < y2; j++) {
					if (linkPieces[pa.x][j] != null) {
						return false;
					}
				}
			}
			line.addPoint(pa);
			line.addPoint(pb);
			return true;
		}
		else if (pa.y == pb.y && pa.x != pb.x) {
			int dist = pa.x - pb.x;
			if (Math.abs(dist) > 1) {
				int x1 = pa.x < pb.x ? pa.x : pb.x;
				int x2 = pa.x > pb.x ? pa.x : pb.x;
				for (int i = x1 + 1; i < x2; i++) {
					if (linkPieces[i][pa.y] != null) {
						return false;
					}
				}
			}
			line.addPoint(pa);
			line.addPoint(pb);
			return true;
		}
		return false;
	}

	private boolean oneCornerLink(Point pa, Point pb, CLinkLine line)
	{
		Point pc = new Point(pa.x, pb.y);
		Point pd = new Point(pb.x, pa.y);

		if (linkPieces[pc.x][pc.y] == null) {
			if (directLink(pa, pc, line) && directLink(pc, pb, line)) {
				return true;
			}
			else {
				line.clear();
			}
		}
		if (linkPieces[pd.x][pd.y] == null) {
			if (directLink(pa, pd, line) && directLink(pd, pb, line)) {
				return true;
			}
			else {
				line.clear();
			}
		}
		return false;
	}

	private boolean towCornerLink(Point pa, Point pb, CLinkLine line)
	{
		for (int i = pa.x + 1; i < linkPieces.length; i++) {
			Point p2 = new Point(i, pa.y);
			if (linkPieces[p2.x][p2.y] == null) {
				if (directLink(pa, p2, line) && oneCornerLink(p2, pb, line)) {
					return true;
				}
				else {
					line.clear();
				}
			}
		}
		for (int i = pa.x - 1; i >= 0; i--) {
			Point p2 = new Point(i, pa.y);
			if (linkPieces[p2.x][p2.y] == null) {
				if (directLink(pa, p2, line) && oneCornerLink(p2, pb, line)) {
					return true;
				}
				else {
					line.clear();
				}
			}
		}
		for (int j = pa.y + 1; j < linkPieces[0].length; j++) {
			Point p2 = new Point(pa.x, j);
			if (linkPieces[p2.x][p2.y] == null) {
				if (directLink(pa, p2, line) && oneCornerLink(p2, pb, line)) {
					return true;
				}
				else {
					line.clear();
				}
			}
		}
		for (int j = pa.y - 1; j >= 0; j--) {
			Point p2 = new Point(pa.x, j);
			if (linkPieces[p2.x][p2.y] == null) {
				if (directLink(pa, p2, line) && oneCornerLink(p2, pb, line)) {
					return true;
				}
				else {
					line.clear();
				}
			}
		}
		return false;
	}

	public boolean isCompleted()
	{
		return (pieceNum==0);
	}

}
