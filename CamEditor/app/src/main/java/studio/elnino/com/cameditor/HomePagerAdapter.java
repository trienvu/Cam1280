package studio.elnino.com.cameditor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HomePagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_ITEMS = 4;

    public HomePagerAdapter(FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
        case 0:
            return CameraFragment.newInstance();
        case 1:
            return EditFragment.newInstance();
        case 2:
            return FragmentMenuDiscovery.newInstance();
        case 3:
            return FragmentMenuDiscovery.newInstance();
        default:
            return FragmentMenuDiscovery.newInstance();
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}