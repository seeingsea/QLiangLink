package jk.haibian.qlianglink;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.adsmogo.adview.AdsMogoLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CImageBrowse extends Activity implements OnClickListener
{
	private AdsMogoLayout adsMogoLayout;
	private ImageLoader imageLoader;
	private ImageView imgView;
	private ImageButton btnZoomIn;
	private ImageButton btnZoomOut;
	private ImageButton btnZoomOrg;
	private ImageButton btnPre;
	private ImageButton btnNext;
	private SLevelInfo level;
	private int bmpIdx = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
											WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_image_browse);

		adsMogoLayout = (AdsMogoLayout)findViewById(R.id.adsMogoView);
		imgView = (ImageView)findViewById(R.id.imageView1);
		imgView.setOnTouchListener(new MulitPointTouchListener(imgView));
		btnZoomIn = (ImageButton)findViewById(R.id.imageButton1);
		btnZoomIn.setOnClickListener(this);
		btnZoomOut = (ImageButton)findViewById(R.id.imageButton2);
		btnZoomOut.setOnClickListener(this);
		btnZoomOrg = (ImageButton)findViewById(R.id.imageButton5);
		btnZoomOrg.setOnClickListener(this);
		btnPre = (ImageButton)findViewById(R.id.imageButton3);
		btnPre.setOnClickListener(this);
		btnNext = (ImageButton)findViewById(R.id.imageButton4);
		btnNext.setOnClickListener(this);

		Intent intent = getIntent();
		int id = intent.getIntExtra("LEVEL_ID", -1);
		level = CGameData.getInstance(null).getLevelInfo(id);

		imageLoader = ImageLoader.getInstance();
		showImage(bmpIdx);
	}

	private void showImage(int idx)
	{
		String bmpUri = String.format("http://yin-an.net/test/img/a%d%02d.jpg", level.levelId, idx);
		if (1 <= idx && idx <= level.imageNum) {
			imageLoader.displayImage(bmpUri, imgView);
		}
	}

	@Override
	protected void onDestroy()
	{
		if (adsMogoLayout != null) {
			adsMogoLayout.clearThread();
		}
		super.onDestroy();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
		case R.id.imageButton1:
			imageZoomIn();
			break;
		case R.id.imageButton2:
			imageZoomOut();
			break;
		case R.id.imageButton5:
			imageZoomOrg();
			break;
		case R.id.imageButton3:
			if (1 < bmpIdx) {
				showImage(--bmpIdx);
				imageZoomOrg();
			}
			break;
		case R.id.imageButton4:
			if (bmpIdx < level.imageNum) {
				showImage(++bmpIdx);
				imageZoomOrg();
			}
			break;
		}
	}

	private void imageZoomOrg()
	{
		imgView.setScaleType(ScaleType.FIT_CENTER);
//		Matrix matrix = new Matrix();
//		matrix.reset();
//		imgView.setImageMatrix(matrix);
	}

	private void imageZoomOut()
	{
		int vh = imgView.getHeight();
		int vw = imgView.getWidth();
		Matrix matrix = imgView.getImageMatrix();
		matrix.postScale(0.6667f, 0.6667f, vw/2, vh/2);
		imgView.setScaleType(ScaleType.MATRIX);
		imgView.setImageMatrix(matrix);
		imgView.postInvalidate();
	}

	private void imageZoomIn()
	{
		int vh = imgView.getHeight();
		int vw = imgView.getWidth();
		Matrix matrix = imgView.getImageMatrix();
		matrix.postScale(1.5f, 1.5f, vw/2, vh/2);
		imgView.setScaleType(ScaleType.MATRIX);
		imgView.setImageMatrix(matrix);
		imgView.postInvalidate();
	}

}

class MulitPointTouchListener implements OnTouchListener
{
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	public ImageView image;
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;

	public MulitPointTouchListener(ImageView image)
	{
		super();
		this.image = image;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		this.image.setScaleType(ScaleType.MATRIX);

		ImageView view = (ImageView)v;
		// dumpEvent(event);

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
//			Log.w("FLAG", "ACTION_DOWN");
			matrix.set(view.getImageMatrix());
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			mode = DRAG;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
//			Log.w("FLAG", "ACTION_POINTER_DOWN");
			oldDist = spacing(event);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
			break;
		case MotionEvent.ACTION_UP:
//			Log.w("FLAG", "ACTION_UP");
		case MotionEvent.ACTION_POINTER_UP:
//			Log.w("FLAG", "ACTION_POINTER_UP");
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:
//			Log.w("FLAG", "ACTION_MOVE");
			if (mode == DRAG) {
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
			}
			else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = newDist / oldDist;
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			}
			break;
		}

		view.setImageMatrix(matrix);
		return true;
	}

	private float spacing(MotionEvent event)
	{
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event)
	{
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
}
