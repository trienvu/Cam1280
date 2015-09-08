package studio.elnino.com.cameditor;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.view.View;

/**
 * @see http 
 *      ://stackoverflow.com/questions/22533773/android-how-to-share-image-with
 *      -text-on-facebook-via-intent
 * @see http 
 *      ://stackoverflow.com/questions/5168145/android-post-picture-to-facebook
 *      -wall
 * @author anh
 * 
 */
public class CaptureLayoutUtil {

	public static final Bitmap captureLayout(final View v) {
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache(true);
		Bitmap bm = v.getDrawingCache();
		v.setDrawingCacheEnabled(false);
		return bm;
	}

	public static final Bitmap captureLayoutGoodQuality(final View v) {
		Bitmap bitmap = null; 
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache(true);

		try {
			bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
					Bitmap.Config.ARGB_8888); 
			Canvas canvas = new Canvas();
			canvas.setBitmap(bitmap);
			v.draw(canvas);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		v.setDrawingCacheEnabled(false);
		return bitmap;
	}

	public static final Uri getImageUri(Context inContext, Bitmap inImage) {
//		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//		inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(),
				inImage, "Title", null);
		return Uri.parse(path);
	}

	public static final byte[] getImageByteArray(Bitmap bi) {
		byte[] data;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bi.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		data = baos.toByteArray();

		return data;
	}

	public static final void viewImageFromUri(Context ctx, Uri u) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(u, "image/*");
		ctx.startActivity(intent);
	}

	public static final void shareToFacebook(Context ctx, Uri imageUri) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND);
		intent.setType("image/*");

		intent.putExtra(Intent.EXTRA_TEXT, "https://translate.google.com.vn/?");
		intent.putExtra(Intent.EXTRA_TITLE, "");
		intent.putExtra(Intent.EXTRA_SUBJECT, "");
		intent.putExtra(Intent.EXTRA_STREAM, imageUri);

		Intent openInChooser = new Intent(intent);
		openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intent);
		ctx.startActivity(openInChooser);
	}

}
