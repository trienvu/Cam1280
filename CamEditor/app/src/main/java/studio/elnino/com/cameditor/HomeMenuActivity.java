package studio.elnino.com.cameditor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aviary.android.feather.sdk.AviaryIntent;
import com.aviary.android.feather.sdk.internal.Constants;
import com.aviary.android.feather.sdk.internal.headless.utils.MegaPixels;
import com.aviary.android.feather.sdk.internal.utils.DecodeUtils;
import com.aviary.android.feather.sdk.internal.utils.ImageInfo;

import java.io.File;

public class HomeMenuActivity extends FragmentActivity implements Callback {
    private static final String TAG = "HomeMenuActivity";
    private static final int ACTION_REQUEST_CAMERA = 0;
    private static final int ACTION_REQUEST_EDIT = 1;
    private static final int ACTION_REQUEST_GALLERY = 2;
    private static final int ACTION_REQUEST_FEATHER = 3;
    private static final int EXTERNAL_STORAGE_UNAVAILABLE = 1;
    /**
     * Folder name on the sdcard where the images will be saved *
     */
    private static final String FOLDER_NAME = "aviary-sample";
    private LinearLayout menuHome;
    public static final int PAGE_HOME = 0;
    public static final int PAGE_LIFE = 1;
    public static final int PAGE_DISCOVERY = 2;
    public static final int PAGE_ME = 3;
    public static final int PAGE_FILE = 4;
    public static final String KEY_INDEX_OF_PAGE_SELECTED = "selected_page_index";
    public static int selectedPage = PAGE_HOME;

    private TextView mTvCamera, mTvGallery, mTvEditPhoto, mTvCollage;
    private ImageView mIvPhoto;

    // Main Handler
    private static Handler mHandler;

    String mOutputFilePath;
    Uri mImageUri;
    int imageWidth, imageHeight;
    File mGalleryFolder;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        mContext = this;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        imageWidth = (int) ((float) metrics.widthPixels / 1.5);
        imageHeight = (int) ((float) metrics.heightPixels / 1.5);

        findViews();
        initialize();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mHandler = new Handler(this);
    }

    private void findViews() {
        menuHome = (LinearLayout) findViewById(R.id.menuHome);
        mTvCamera = (TextView) findViewById(R.id.tvCamera);
        mTvGallery = (TextView) findViewById(R.id.tvGallery);
        mTvEditPhoto = (TextView) findViewById(R.id.tvEditPhoto);
        mTvCollage = (TextView) findViewById(R.id.tvCollage);
        mIvPhoto = (ImageView) findViewById(R.id.ivPhoto);
    }

    public void initialize() {
        mGalleryFolder = createFolders();
        mTvCamera.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        mTvGallery.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });
        mTvEditPhoto.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mImageUri != null) {
                    startFeather(mImageUri);
                }
            }
        });
        mTvCollage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
        mIvPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = pickRandomImage();
                if (uri != null) {
                    Log.d(TAG, "image uri: " + uri);
                    loadAsync(uri);
                }
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return true;
    }

    private void sendMessage(int msg) {
        Message m = new Message();
        m.what = msg;
        mHandler.sendMessage(m);
    }

    @Override
    public boolean handleMessage(Message msg) {
        // TODO Auto-generated method stub
        switch (msg.what) {
            case 0:

                break;
            case 1:

                break;
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTION_REQUEST_CAMERA:
                if (data != null) {
//                    Bitmap bp = (Bitmap) data.getExtras().get("data");
//                    mIvPhoto.setImageBitmap(bp);
//                }
                    // user chose an image from the gallery
                    loadAsync(data.getData());
                }
                break;
            case ACTION_REQUEST_GALLERY:
                if (data != null) {
                    // user chose an image from the gallery
                    loadAsync(data.getData());
                }
                break;
            case ACTION_REQUEST_FEATHER:

                boolean changed = true;

                if (null != data) {
                    Bundle extra = data.getExtras();
                    if (null != extra) {
                        // image was changed by the user?
                        changed = extra
                                .getBoolean(Constants.EXTRA_OUT_BITMAP_CHANGED);
                    }
                }

                if (!changed) {
                    Log.w(
                            TAG,
                            "User did not modify the image, but just clicked on 'Done' button");
                } else {
                    // send a notification to the media scanner
                    updateMedia(mOutputFilePath);

                    // update the preview with the result
                    loadAsync(data.getData());
                    mOutputFilePath = null;
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null)
            mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * Start the activity to capture
     */
    private void openCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, ACTION_REQUEST_CAMERA);
    }

    /**
     * Start the activity to pick an image from the user gallery
     */
    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        Intent chooser = Intent.createChooser(intent, "Choose a Picture");
        startActivityForResult(chooser, ACTION_REQUEST_GALLERY);
    }
    /**
     * Pick a random image from the user gallery
     *
     * @return
     */
    @SuppressWarnings ("unused")
    private Uri pickRandomImage() {
        Cursor c = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA},
                MediaStore.Images.ImageColumns.SIZE + ">?", new String[]{"90000"}, MediaStore.Images.ImageColumns._ID);
        Uri uri = null;

        if (c != null) {
            int total = c.getCount();
            int position = (int) (Math.random() * total);
            if (total > 0) {
                if (c.moveToPosition(position)) {
                    String data = c.getString(
                            c.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                    long id = c.getLong(
                            c.getColumnIndex(MediaStore.Images.ImageColumns._ID));
                    uri = Uri.parse(data);
                }
            }
            c.close();
        }
        return uri;
    }


    /**
     * Once you've chosen an image you can start the feather activity
     *
     * @param uri
     */
    @SuppressWarnings("deprecation")
    private void startFeather(Uri uri) {
        Log.d(TAG, "uri: " + uri);

        // first check the external storage availability
        if (!isExternalStorageAvailable()) {
            showDialog(EXTERNAL_STORAGE_UNAVAILABLE);
            return;
        }

        // create a temporary file where to store the resulting image
        File file = getNextFileName();

        if (null != file) {
            mOutputFilePath = file.getAbsolutePath();
        } else {
            new AlertDialog.Builder(mContext)
                    .setTitle(android.R.string.dialog_alert_title)
                    .setMessage("Failed to create a new File").show();
            return;
        }

        // Create the intent needed to start feather
        Intent newIntent = new AviaryIntent.Builder(mContext).setData(uri)
                .withOutput(Uri.parse("file://" + mOutputFilePath))
                .withOutputFormat(Bitmap.CompressFormat.JPEG)
                .withOutputSize(MegaPixels.Mp5).withNoExitConfirmation(true)
                .saveWithNoChanges(true).withPreviewSize(1024)
                .build();

        // ..and start feather
        startActivityForResult(newIntent, ACTION_REQUEST_FEATHER);

    }

    /**
     * Return a new image file. Name is based on the current time. Parent folder
     * will be the one created with createFolders
     *
     * @return
     * @see #createFolders()
     */
    private File getNextFileName() {
        if (mGalleryFolder != null) {
            if (mGalleryFolder.exists()) {
                File file = new File(
                        mGalleryFolder, "aviary_"
                        + System.currentTimeMillis() + ".jpg");
                return file;
            }
        }
        return null;
    }

    /**
     * Check the external storage status
     *
     * @return
     */
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Load the incoming Image
     *
     * @param uri
     */
    private void loadAsync(final Uri uri) {
        Drawable toRecycle = mIvPhoto.getDrawable();
        if (toRecycle != null && toRecycle instanceof BitmapDrawable) {
            if (((BitmapDrawable) mIvPhoto.getDrawable()).getBitmap() != null) {
                ((BitmapDrawable) mIvPhoto.getDrawable()).getBitmap().recycle();
            }
        }
        mIvPhoto.setImageDrawable(null);
        mImageUri = null;

        DownloadAsync task = new DownloadAsync();
        task.execute(uri);
    }

    private File createFolders() {
        File baseDir;

        if (android.os.Build.VERSION.SDK_INT < 8) {
            baseDir = Environment.getExternalStorageDirectory();
        } else {
            baseDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        }

        if (baseDir == null) {
            return Environment.getExternalStorageDirectory();
        }

        Log.d(TAG, "Pictures folder: " + baseDir.getAbsolutePath());
        File aviaryFolder = new File(baseDir, FOLDER_NAME);

        if (aviaryFolder.exists()) {
            return aviaryFolder;
        }
        if (aviaryFolder.mkdirs()) {
            return aviaryFolder;
        }

        return Environment.getExternalStorageDirectory();
    }

    class DownloadAsync extends AsyncTask<Uri, Void, Bitmap> implements
            DialogInterface.OnCancelListener {
        ProgressDialog mProgress;
        private Uri mUri;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgress = new ProgressDialog(mContext);
            mProgress.setIndeterminate(true);
            mProgress.setCancelable(true);
            mProgress.setMessage("Loading image...");
            mProgress.setOnCancelListener(this);
            mProgress.show();
        }

        @Override
        protected Bitmap doInBackground(Uri... params) {
            mUri = params[0];

            Bitmap bitmap = null;

//            while (mImageContainer.getWidth() < 1) {
//                try {
//                    Thread.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            final int w = mImageContainer.getWidth();
//            Log.d(TAG, "width: " + w);
            ImageInfo info = new ImageInfo();
            bitmap = DecodeUtils.decode(
                    mContext, mUri, imageWidth,
                    imageHeight, info);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if (mProgress.getWindow() != null) {
                mProgress.dismiss();
            }

            if (result != null) {
                setImageURI(mUri, result);
            } else {
                Toast.makeText(
                        mContext,
                        "Failed to load image " + mUri, Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            Log.i(TAG, "onProgressCancel");
            this.cancel(true);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.i(TAG, "onCancelled");
        }
    }

    /**
     * Given an Uri load the bitmap into the current ImageView and resize it to
     * fit the image container size
     *
     * @param uri
     */
    @SuppressWarnings("deprecation")
    private boolean setImageURI(final Uri uri, final Bitmap bitmap) {
        mIvPhoto.setImageBitmap(bitmap);
        mIvPhoto.setBackgroundDrawable(null);

        mTvEditPhoto.setEnabled(true);
        mImageUri = uri;

        return true;
    }

    /**
     * We need to notify the MediaScanner when a new file is created. In this
     * way all the gallery applications will be notified too.
     *
     * @param filepath
     */
    private void updateMedia(String filepath) {
        MediaScannerConnection.scanFile(
                mContext,
                new String[]{filepath}, null, null);
    }

}
