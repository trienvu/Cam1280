package studio.elnino.com.cameditor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
public class CameraFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final CameraFragment newInstance() {
        CameraFragment f = new CameraFragment();
        Bundle bdl = new Bundle();
        bdl.putSerializable(EXTRA_MESSAGE, "");
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_gallery_layout, null,
                false);

        return v;

    }
}
