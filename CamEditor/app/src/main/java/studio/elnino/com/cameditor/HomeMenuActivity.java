package studio.elnino.com.cameditor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aviary.android.feather.sdk.AviaryIntent;
import com.aviary.android.feather.sdk.internal.Constants;
import com.aviary.android.feather.sdk.internal.headless.utils.MegaPixels;

import java.io.File;

public class HomeMenuActivity extends Activity implements Callback {
    private static final String TAG = "HomeMenuActivity";

    private static final String COLLAGE_APP_PACKAGE_NAME = "com.zentertain.photocollage2";
    private static final int ACTION_REQUEST_CAMERA = 0;
    private static final int ACTION_REQUEST_EDIT = 1;
    private static final int ACTION_REQUEST_GALLERY = 2;
    private static final int ACTION_REQUEST_FEATHER = 3;
    private static final int EXTERNAL_STORAGE_UNAVAILABLE = 1;
    /**
     * Folder name on the sdcard where the images will be saved *
     */
    public static final String SD_CARD_PATH = Environment
            .getExternalStorageDirectory().toString() + "/";
    public static final String FOLDER_NAME = "CamEditor";
    public static final String SAVE_FILE_PATH = SD_CARD_PATH
            + FOLDER_NAME;

    private TextView mTvCamera, mTvGallery, mTvEdit;
    private ImageView mIvShare, mIvDelete;
    private ViewPager mPgPhotos;

    private PhotosAdapter mPhotosAdapter;
    // Main Handler
    private static Handler mHandler;

    String mOutputFilePath;
    Uri mImageUri;
    File mGalleryFolder;
    File[] mFiles;

    private Dialog mConfirmDeleteDialog;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        mContext = this;
        findViews();
        initialize();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mHandler = new Handler(this);
    }

    private void findViews() {
        mPgPhotos = (ViewPager) findViewById(R.id.pgPhotos);
        mTvCamera = (TextView) findViewById(R.id.tvCamera);
        mTvGallery = (TextView) findViewById(R.id.tvGallery);
        mTvEdit = (TextView) findViewById(R.id.tvEdit);
        mIvDelete = (ImageView) findViewById(R.id.ivDelete);
        mIvShare = (ImageView) findViewById(R.id.ivShare);
    }

    public void initialize() {
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
        mTvEdit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                File file = mFiles[mPgPhotos.getCurrentItem()];
                mImageUri = Uri.parse(file.getAbsolutePath());
                if (mImageUri != null) {
                    startFeather(mImageUri);
                }
            }
        });

        mIvDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDeleteDialog(mContext);
            }
        });

        mIvShare.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sharePic();
            }
        });

        mGalleryFolder = createFolders();
        File targetDirector = new File(mGalleryFolder.getAbsolutePath());
        mFiles = targetDirector.listFiles();
        if (mFiles == null || mFiles.length == 0)
            return;

        mPhotosAdapter = new PhotosAdapter(this, mFiles);
        mPgPhotos.setAdapter(mPhotosAdapter);
    }

    private void refreshList() {
        File targetDirector = new File(mGalleryFolder.getAbsolutePath());
        mFiles = targetDirector.listFiles();
        if (mPhotosAdapter == null)
            mPhotosAdapter = new PhotosAdapter(this, mFiles);
        mPhotosAdapter.refreshData(mFiles);
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
                    startFeather(data.getData());
                }
                break;
            case ACTION_REQUEST_GALLERY:
                if (data != null) {
                    // user chose an image from the gallery
                    startFeather(data.getData());
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
//                    loadAsync(data.getData());
                    refreshList();
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
    @SuppressWarnings("unused")
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
     * Open another app.
     *
     * @param context     current Context, like Activity, App, or Service
     * @param packageName the full package name of the app to open
     * @return true if likely successful, false if unsuccessful
     */
    public static boolean openCollageApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                Log.d(TAG, "Null cmnr");
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("market://details?id=" + packageName)));
                return true;
                //throw new PackageManager.NameNotFoundException();
            }
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Once you've chosen an image you can start the feather activity
     *
     * @param uri
     */
    @SuppressWarnings("deprecation")
    private void startFeather(Uri uri) {
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

        File aviaryFolder = new File(baseDir, FOLDER_NAME);

        if (aviaryFolder.exists()) {
            return aviaryFolder;
        }
        if (aviaryFolder.mkdirs()) {
            return aviaryFolder;
        }

        return Environment.getExternalStorageDirectory();
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

    private void doDeleteFile() {

        File file = mFiles[mPgPhotos.getCurrentItem()];
        if (file.exists())
            file.delete();
        Log.d("XX", "Path: " + file.getAbsolutePath());
        //refresh list;
        refreshList();
    }

    public void showDeleteDialog(Context ctx) {
        mConfirmDeleteDialog = new Dialog(mContext, R.style.CustomWarningDialog);
        mConfirmDeleteDialog.setContentView(R.layout.custom_warning_dialog);
        mConfirmDeleteDialog.getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                getWindow().getWindowManager()
                        .getDefaultDisplay().getHeight());
        Button ok = (Button) mConfirmDeleteDialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDeleteFile();
                mConfirmDeleteDialog.dismiss();
            }
        });
        Button cancel = (Button) mConfirmDeleteDialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConfirmDeleteDialog.dismiss();
            }
        });
        mConfirmDeleteDialog.show();
    }

    private void sharePic() {
        File file = mFiles[mPgPhotos.getCurrentItem()];
        if (file.exists())
            CaptureLayoutUtil.shareToFacebook(mContext, Uri.parse(file.getAbsolutePath()));
    }
}
