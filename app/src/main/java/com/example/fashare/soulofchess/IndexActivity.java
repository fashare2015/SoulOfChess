package com.example.fashare.soulofchess;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class IndexActivity extends ActivityGroup {
	// ----------------- ViewPager ---------------------
	private ViewPager viewPager;
	private List<View> views;
	private int currIndex = 0;
	private View view1, view2, view3;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.index);

		//load_data();

		initViewPager();
	}

	private void load_data() {

	}

	@SuppressWarnings("deprecation")
	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.vPager);
		views = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater();
		
		LocalActivityManager am= getLocalActivityManager();
		view1 = am.startActivity("Rank", new Intent(IndexActivity.this, RankActivity.class)).getDecorView();
		view2 = am.startActivity("AIBrowser", new Intent(IndexActivity.this, AIBrowserActivity.class)).getDecorView();
		view3 = am.startActivity("PatternBrowser", new Intent(IndexActivity.this, PatternBrowserActivity.class)).getDecorView();

		views.add(view1);
		views.add(view2);
		views.add(view3);

		viewPager.setAdapter(new MyViewPagerAdapter(views));
		viewPager.setCurrentItem(1);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	public class MyViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			
			container.addView(mListViews.get(position), 0);
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageSelected(int arg0) {
		}
	}

}


