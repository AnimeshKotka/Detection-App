package animesh.com.mark32;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTabHost;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;

public class MainActivity extends AppCompatActivity {

    FragmentPagerAdapter fragmentPagerAdapter;
    PagerTabStrip tabStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        fragmentPagerAdapter = new MyPageAdapter(getSupportFragmentManager());
        vpPager.setAdapter(fragmentPagerAdapter);
        tabStrip = (PagerTabStrip)findViewById(R.id.pager_header);

        tabStrip.setTextSize(TypedValue.COMPLEX_UNIT_PX,100);
        tabStrip.setBackgroundColor(Color.parseColor("#6ab04c"));



    }


    public static class MyPageAdapter extends FragmentPagerAdapter{

        private static int NUM_ITEMS = 3;
        public MyPageAdapter(FragmentManager fm){
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return FirstFragment.newInstance();
                case 1:
                    return SceondFragment.newInstance();
                case 2:
                    return ThridFragment.newInstance(3,"Object Detection");
                default:
                    return null;
            }


        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }
}
