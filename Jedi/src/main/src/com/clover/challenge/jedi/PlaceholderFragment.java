package com.clover.challenge.jedi;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

	private static final int FONT_SIZE = 40;
	private static final int ANIM_FREQ = 3000;
	private Runnable switcher;
	private TextSwitcher ts;
	private String[] titles = { "Welcome!", "Select an action!",
			"Share anything!" };

	public PlaceholderFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		initTS();

	}

	@Override
	public void onResume() {
		super.onResume();

		switcher = new Runnable() {
			private int index = 0;

			public void run() {
				index = (index + 1) % titles.length;
				ts.setText(titles[index]);
				ts.postDelayed(this, ANIM_FREQ);
			}
		};

		ts.postDelayed(switcher, 0);
	}

	@Override
	public void onPause() {
		super.onPause();

		ts.removeCallbacks(switcher);
		switcher = null;
	}

	private void initTS() {
		
		if (ts != null)
			return;
		
		ts = (TextSwitcher) getActivity().findViewById(R.id.title);

		ts.setInAnimation(AnimationUtils.loadAnimation(getActivity(),
				android.R.anim.fade_in));
		ts.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),
				android.R.anim.fade_out));

		ts.setFactory(new ViewFactory() {
			// CALLED WHENEVER NEW CHILD VIEW IS NEEDED
			public View makeView() {

				TextView tv = new TextView(getActivity());
				tv.setLayoutParams(new TextSwitcher.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				tv.setTextSize(FONT_SIZE);
				tv.setTypeface(Typeface.create("sans-serif-thin",
						Typeface.NORMAL));
				tv.setGravity(Gravity.CENTER);
				tv.setTextColor(Color.BLACK);

				return tv;
			}
		});
	}
}
