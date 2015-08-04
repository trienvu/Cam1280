package studio.elnino.com.cameditor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.aviary.android.feather.sdk.AviaryIntent;
import com.aviary.android.feather.sdk.internal.Constants;
import com.aviary.android.feather.sdk.internal.headless.utils.MegaPixels;


public class CamActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);

        Intent intent = AviaryIntent.createCdsInitIntent(getBaseContext());
        startService(intent);
    }

    private void goEditPage() {
        Intent newIntent = new AviaryIntent.Builder(this)
                .setData(uri) // input image src
                .withOutput(Uri.parse("file://" + mOutputFilePath)) // output file
                .withOutputFormat(Bitmap.CompressFormat.JPEG) // output format
                .withOutputSize(MegaPixels.Mp5) // output size
                .withOutputQuality(90) // output quality
                .build();

        // start the activity
        startActivityForResult(newIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1: // this is the request code we used in this example
                    Uri mImageUri = data.getData(); // generated output file
                    Bundle extra = data.getExtras();
                    if (null != extra) {
                        // image has been changed?
                        boolean changed = extra.getBoolean(Constants.EXTRA_OUT_BITMAP_CHANGED);
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cam, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
