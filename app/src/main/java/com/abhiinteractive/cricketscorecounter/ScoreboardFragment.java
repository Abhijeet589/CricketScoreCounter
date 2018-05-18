package com.abhiinteractive.cricketscorecounter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreboardFragment extends Fragment {

    TextView totalRuns, totalBalls, totalWickets, totalFours, totalSixes, totalExtras;
    Button backToMatch;
    AdView mAdView;

    public ScoreboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_scoreboard, container, false);

        mAdView = v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("C50478B9352C968A158C206EB2D1DA63").build();
        mAdView.loadAd(adRequest);

        totalRuns = v.findViewById(R.id.total_runs);
        totalBalls = v.findViewById(R.id.total_balls);
        totalWickets = v.findViewById(R.id.total_wickets);
        totalFours = v.findViewById(R.id.total_fours);
        totalSixes = v.findViewById(R.id.total_sixes);
        totalExtras = v.findViewById(R.id.total_extras);

        backToMatch = v.findViewById(R.id.back_to_match_button);
        backToMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MainFragment mainFragment = new MainFragment();
                FragmentManager fm = getFragmentManager();
                //FragmentTransaction ft = fm.beginTransaction();
                //ft.replace(R.id.fragment_container, mainFragment);
                fm.popBackStack();
                //ft.commit();
            }
        });

        int[] teamData = Data.getIntData(getActivity(), /*11,*/ "teamScores");
        totalRuns.setText("Total runs: " + teamData[0]);
        totalBalls.setText("Total balls: " + teamData[1]);
        totalWickets.setText("Total wickets: " + teamData[2]);
        totalFours.setText("Total fours: " + teamData[6]);
        totalSixes.setText("Total sixes: " + teamData[7]);
        totalExtras.setText("Total extras: " + teamData[8]);

        LinearLayout scores = v.findViewById(R.id.scores);
        String[] playerData = Data.getStringData(getActivity(), /*11,*/ "playerData");
        int[] runsData = Data.getIntData(getActivity(), /*11,*/ "runsData");
        int[] ballsData = Data.getIntData(getActivity(), /*11,*/ "ballsData");
        int[] foursData = Data.getIntData(getActivity(), /*11,*/ "foursData");
        int[] sixesData = Data.getIntData(getActivity(), /*11,*/ "sixesData");
        for(int i=0; i<11; i++){
            if(playerData[i]!=null) {

                LinearLayout ll = new LinearLayout(getActivity());
                ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                TextView no = new TextView(getActivity());
                no.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                no.setText("" + (i+1));
                no.setTextColor(getResources().getColor(R.color.blueDark));
                no.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                ll.addView(no);

                TextView tv = new TextView(getActivity());
                tv.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 4));
                tv.setText("" + playerData[i]);
                tv.setTextColor(getResources().getColor(R.color.blueDark));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                ll.addView(tv);

                TextView runsTextView = new TextView(getActivity());
                runsTextView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2));
                runsTextView.setTextColor(getResources().getColor(R.color.blueDark));
                runsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                runsTextView.setText("" + runsData[i]);
                ll.addView(runsTextView);

                TextView ballsTextView = new TextView(getActivity());
                ballsTextView.setTextColor(getResources().getColor(R.color.blueDark));
                ballsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                ballsTextView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2));
                ballsTextView.setText("" + ballsData[i]);
                ll.addView(ballsTextView);

                TextView foursTextView = new TextView(getActivity());
                foursTextView.setTextColor(getResources().getColor(R.color.blueDark));
                foursTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                foursTextView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2));
                foursTextView.setText("" + foursData[i]);
                ll.addView(foursTextView);

                TextView sixesTextView = new TextView(getActivity());
                sixesTextView.setTextColor(getResources().getColor(R.color.blueDark));
                sixesTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                sixesTextView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2));
                sixesTextView.setText("" + sixesData[i]);
                ll.addView(sixesTextView);

                scores.addView(ll);
            }
        }

        return v;
    }

}
