package studio.elnino.com.cameditor;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class CamActivity extends Activity implements AdapterView.OnItemClickListener {

    List<GridViewItem> gridItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setGridAdapter(HomeMenuActivity.SD_CARD_PATH);
    }


    /**
     * This will create our GridViewItems and set the adapter
     *
     * @param path
     *            The directory in which to search for images
     */
    private void setGridAdapter(String path) {
        // Create a new grid adapter
        gridItems = createGridItems(path);
        MyGridAdapter adapter = new MyGridAdapter(this, gridItems);

        // Set the grid adapter
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(adapter);

        // Set the onClickListener
        gridView.setOnItemClickListener(this);
    }


    /**
     * Go through the specified directory, and create items to display in our
     * GridView
     */
    private List<GridViewItem> createGridItems(String directoryPath) {
        List<GridViewItem> items = new ArrayList<GridViewItem>();

        // List all the items within the folder.
        File[] files = new File(directoryPath).listFiles(new ImageFileFilter());
        for (File file : files) {

            // Add the directories containing images or sub-directories
            if (file.isDirectory()
                    && file.listFiles(new ImageFileFilter()).length > 0) {

                items.add(new GridViewItem(file.getAbsolutePath(), true, null));
            }
            // Add the images
            else {
                Bitmap image = BitmapHelper.decodeBitmapFromFile(file.getAbsolutePath(),
                        50,
                        50);
                items.add(new GridViewItem(file.getAbsolutePath(), false, image));
            }
        }

        return items;
    }


    /**
     * Checks the file to see if it has a compatible extension.
     */
    private boolean isImageFile(String filePath) {
        if (filePath.endsWith(".jpg") || filePath.endsWith(".png"))
        // Add other formats as desired
        {
            return true;
        }
        return false;
    }


    @Override
    public void
    onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (gridItems.get(position).isDirectory()) {
            setGridAdapter(gridItems.get(position).getPath());
        }
        else {
            // Display the image
        }

    }

    /**
     * This can be used to filter files.
     */
    private class ImageFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }
            else if (isImageFile(file.getAbsolutePath())) {
                return true;
            }
            return false;
        }
    }

}