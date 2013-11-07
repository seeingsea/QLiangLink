package jk.haibian.qlianglink;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class CLinkPieceBuilder
{
	private Context context;

	public CLinkPieceBuilder(Context context)
	{
		this.context = context;
	}

	public CPiece[][] createPieces(int rowNum, int colNum, String iconSet, int iconNum)
	{
		// 加载图标资源
		List<Bitmap> bmpList = new ArrayList<Bitmap>();
		Resources res = context.getResources();

		for (int i = 1; i <= iconNum; i++) {
			String name = String.format("%s_%d", iconSet, i);
			int id = res.getIdentifier(name, "drawable", "jk.haibian.qlianglink");
			Bitmap bmp = BitmapFactory.decodeResource(res, id);
			if (bmp != null) {
				bmpList.add(bmp);			
			}
		}
		
		if (bmpList.size() < 4) {
			return null;
		}

		// 创建CPiece对象
		List<CPiece> pieceList = new ArrayList<CPiece>();
		for (int i = 0; i < (colNum - 2) * (rowNum - 2); i++) {
			int bmpIdx = (i / 2) % iconNum;
			CPiece piece = new CPiece();
			piece.setImage(bmpList.get(bmpIdx));
			piece.setImageId(bmpIdx);
			pieceList.add(piece);
		}

		// 乱序放入二维数组
		CPiece[][] pieces = new CPiece[colNum][rowNum];
		Collections.shuffle(pieceList);
		for (int i = 1; i < colNum - 1; i++) {
			for (int j = 1; j < rowNum - 1; j++) {
				int idx = (i-1) * (rowNum - 2) + (j-1);
//				Log.w("pieceBuilder", String.format("%d", idx));
				pieces[i][j] = pieceList.get(idx);
				pieces[i][j].setIndexX(i);
				pieces[i][j].setIndexY(j);
			}
		}

		return pieces;
	}

}
