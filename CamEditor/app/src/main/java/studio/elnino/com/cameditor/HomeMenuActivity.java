package studio.elnino.com.cameditor;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeMenuActivity extends FragmentActivity implements Callback {
    private static final String TAG = "HomeMenuActivity";
    private Context mContext;
    public ViewPager pager;
    HomePagerAdapter pageAdapter;
    private LinearLayout menuHome;
    public static final int PAGE_HOME = 0;
    public static final int PAGE_LIFE = 1;
    public static final int PAGE_DISCOVERY = 2;
    public static final int PAGE_ME = 3;
    public static final int PAGE_FILE = 4;
    public static final String KEY_INDEX_OF_PAGE_SELECTED = "selected_page_index";
    public static int selectedPage = PAGE_HOME;

    private TextView tvMenuHome, tvMenuLife, tvMenuDiscovery, tvMenuMe;
    // Main Handler
    private static Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        mContext = this;

        initializeUI();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mHandler = new Handler(this);
    }

    public void initializeUI() {
        pager = (ViewPager) findViewById(R.id.viewpager);
        menuHome = (LinearLayout) findViewById(R.id.menuHome);


        tvMenuHome = (TextView) findViewById(R.id.tvMenuHome);
        tvMenuLife = (TextView) findViewById(R.id.tvMenuLife);
        tvMenuDiscovery = (TextView) findViewById(R.id.tvMenuDiscovery);
        tvMenuMe = (TextView) findViewById(R.id.tvMenuMe);
        pageAdapter = new HomePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pageAdapter);
        // pager.setOffscreenPageLimit(1);

        pager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int pos) {

                switch (pos) {
                case PAGE_HOME:
//                    tvMenuHome.setCompoundDrawablesWithIntrinsicBounds(0,
//                            R.drawable.ic_home_nomal, 0, 0);
//                    tvMenuDiscovery.setCompoundDrawablesWithIntrinsicBounds(0,
//                            R.drawable.ic_discovery_active, 0, 0);
//                    tvMenuLife.setCompoundDrawablesWithIntrinsicBounds(0,
//                            R.drawable.ic_life_active, 0, 0);
//                    tvMenuMe.setCompoundDrawablesWithIntrinsicBounds(0,
//                            R.drawable.ic_me_active, 0, 0);
//                    tvMenuHome.setTextColor(getResources().getColor(
//                            R.color.text_active_color));
//                    tvMenuLife.setTextColor(getResources().getColor(
//                            R.color.text_color));
//                    tvMenuDiscovery.setTextColor(getResources().getColor(
//                            R.color.text_color));
//                    tvMenuMe.setTextColor(getResources().getColor(
//                            R.color.text_color));
                    break;
                case PAGE_LIFE:
//                    tvMenuHome.setCompoundDrawablesWithIntrinsicBounds(0,
//                            R.drawable.ic_home_active, 0, 0);
//                    tvMenuLife.setCompoundDrawablesWithIntrinsicBounds(0,
//                            R.drawable.ic_life_nomal, 0, 0);
//                    tvMenuDiscovery.setCompoundDrawablesWithIntrinsicBounds(0,
//                            R.drawable.ic_discovery_active, 0, 0);
//                    tvMenuMe.setCompoundDrawablesWithIntrinsicBounds(0,
//                            R.drawable.ic_me_active, 0, 0);
//                    tvMenuLife.setTextColor(getResources().getColor(
//                            R.color.text_active_color));
//                    tvMenuDiscovery.setTextColor(getResources().getColor(
//                            R.color.text_color));
//                    tvMenuMe.setTextColor(getResources().getColor(
//                            R.color.text_color));
//                    tvMenuHome.setTextColor(getResources().getColor(
//                            R.color.text_color));
                    break;
                case PAGE_DISCOVERY:
//                    tvMenuHome.setCompoundDrawablesWithIntrinsicBounds(0,
//                            R.drawable.ic_home_active, 0, 0);
//                    tvMenuLife.setCompoundDrawablesWithIntrinsicBounds(0,
//                            R.drawable.ic_life_active, 0, 0);
//                    tvMenuDiscovery.setCompoundDrawablesWithIntrinsicBounds(0,
//                            R.drawable.ic_discovery_nomal, 0, 0);
//                    tvMenuMe.setCompoundDrawablesWithIntrinsicBounds(0,
//                            R.drawable.ic_me_active, 0, 0);
//                    tvMenuDiscovery.setTextColor(getResources().getColor(
//                            R.color.text_active_color));
//                    tvMenuLife.setTextColor(getResources().getColor(
//                            R.color.text_color));
//                    tvMenuMe.setTextColor(getResources().getColor(
//                            R.color.text_color));
//                    tvMenuHome.setTextColor(getResources().getColor(
//                            R.color.text_color));
                    break;
                case PAGE_ME:
//                    tvMenuHome.setCompoundDrawablesWithIntrinsicBounds(0,
//                            R.drawable.ic_home_active, 0, 0);
//                    tvMenuLife.setCompoundDrawablesWithIntrinsicBounds(0,
//                            R.drawable.ic_life_active, 0, 0);
//                    tvMenuDiscovery.setCompoundDrawablesWithIntrinsicBounds(0,
//                            R.drawable.ic_discovery_active, 0, 0);
//                    tvMenuMe.setCompoundDrawablesWithIntrinsicBounds(0,
//                            R.drawable.ic_me_nomal, 0, 0);
//                    tvMenuMe.setTextColor(getResources().getColor(
//                            R.color.text_active_color));
//                    tvMenuLife.setTextColor(getResources().getColor(
//                            R.color.text_color));
//                    tvMenuDiscovery.setTextColor(getResources().getColor(
//                            R.color.text_color));
//                    tvMenuHome.setTextColor(getResources().getColor(
//                            R.color.text_color));
                    break;

                default:
                    break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        tvMenuHome.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pager.setCurrentItem(0, false);
            }
        });
        tvMenuLife.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1, false);

            }
        });
        tvMenuDiscovery.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pager.setCurrentItem(2, false);

            }
        });
        tvMenuMe.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pager.setCurrentItem(3, false);

            }
        });

        // mFragmentMenuHome.setCheckAddScreen(false);
        pager.setCurrentItem(selectedPage, false);
    }



    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
//            if (event.getAction() == KeyEvent.ACTION_DOWN)
//                if (pager.getCurrentItem() != 0) {
//                    if (pager.getCurrentItem() == 4) {
//                        showHideMenuHome(false);
//                        pager.setCurrentItem(3, false);
//                    } else {
//                        pager.setCurrentItem(0, false);
//                    }
//                } else {
//                    // if (mFragmentMenuHome.isCheckAddScreen()) {
//                    // mFragmentMenuHome.backPressed();
//                    // mFragmentMenuHome.setCheckAddScreen(false);
//                    // } else {
//                    finish();
//                    // }
//                }

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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null)
            mHandler.removeCallbacksAndMessages(null);
    }


}
