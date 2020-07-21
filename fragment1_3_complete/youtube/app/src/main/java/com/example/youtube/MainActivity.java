package com.example.youtube;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.os.Bundle;
import android.view.View;


import com.example.youtube.contact.Fragment1;
import com.example.youtube.gallery.Fragment2;
import com.example.youtube.booking.Fragment3;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends FragmentActivity {
    private static final int NUM_PAGES = 3;
    private FragmentStateAdapter pagerAdapter;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private  String email_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},1);

        email_key = getIntent().getStringExtra("email");

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new TapPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
        });

        tabLayout = findViewById(R.id.tab_layout);

        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position + 1) {
                            case 1 :
                                tab.setText("Contacts");
                                tab.setIcon(R.drawable.ic_phone);
                                break;
                            case 2 :
                                tab.setText("Gallery");
                                tab.setIcon(R.drawable.ic_gallery);
                                break;
                            case 3 :
                                tab.setText("Empty");
                                tab.setIcon(R.drawable.ic_music);
                                break;
                        }
                    }
                }).attach();

        tabLayout.addOnTabSelectedListener(new MyOnTabSelectedListener());
    } //onCreate


    private class TapPagerAdapter extends FragmentStateAdapter {
        public TapPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = new Fragment1(); // 코드를 조금 더 이쁘게 만들 수 있을 것 같은데
            switch (position + 1) {
                case 1 :
                     fragment = new Fragment1();
                    break;
                case 2 :
                    fragment = new Fragment2();
                    break;
                case 3 :
                    fragment = new Fragment3();
                    break;
            }
            return fragment;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    private class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f); // perfectly transparent

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view. setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else {
                // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }

        }
    }

    //각 탭이 선택되었을 경우 처리
    private class MyOnTabSelectedListener implements TabLayout.OnTabSelectedListener {
        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

        }
        @Override
        public void onTabUnselected(TabLayout.Tab tab)  {

        }
    }

    public String getEmail_key(){
        return email_key;
    }
}
