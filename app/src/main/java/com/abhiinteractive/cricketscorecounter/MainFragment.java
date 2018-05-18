package com.abhiinteractive.cricketscorecounter;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    Button goToScoreboard;
    int i = 0;
    String[] playerNames = new String[11];
    int[] individualRuns = {0,0,0,0,0,0,0,0,0,0,0};
    int[] individualBalls = {0,0,0,0,0,0,0,0,0,0,0};
    int[] individualFours = {0,0,0,0,0,0,0,0,0,0,0};
    int[] individualSixes = {0,0,0,0,0,0,0,0,0,0,0};
    boolean alreadyLoaded = false;
    boolean conti = false;
    int teamRuns;
    int teamBalls;
    int teamWickets;
    int crease = 0;
    int runnerUp = 1;
    int totalOvers = 999;

    final int DOT_BALL = 0, ONE_RUN = 1, TWO_RUN = 2, THREE_RUN = 3, FOUR_RUN = 4, FIVE_RUN = 5, SIX_RUN = 6, WIDE_BALL = 7, NO_BALL = 8, WICKET = 9;
    //int lastBall = -1;
    //int secondLastBall = -1;
    int[] prevBalls = new int[10];

    LinearLayout matchEnded;
    TableLayout buttonsLayout;
    Button viewScoreboard;

    boolean pausedThenResumed=false;
    int c = 1;
    int visibleEle = 0;
    int NO_OF_ELE = 3;
    int fours, sixes, extras;
    TextView foursTV, sixesTV, extrasTV, rrTV;
    float rr;
    boolean isDefaultName;

    CountDownTimer cd;

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    TextView p1name, p1runs, p1balls, p1fours, p1sixes;
    TextView p2name, p2runs, p2balls, p2fours, p2sixes;

    TextView runsTextView, ballsTextView, wicketsTextView;
    Button oneRun, twoRun, threeRun, fourRun, sixRun, dotBall, wideBall, noBall, outBtn;
    ImageView undoBtn;
    LinearLayout lastBalls, runRate, foursAndSixes;
    LinearLayout[] eleArray;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        mAdView = v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("C50478B9352C968A158C206EB2D1DA63").build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-6229326724546843/6575905778");
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("C50478B9352C968A158C206EB2D1DA63").build());

        runsTextView = v.findViewById(R.id.runs);
        ballsTextView = v.findViewById(R.id.balls);
        wicketsTextView = v.findViewById(R.id.wickets);

        oneRun = v.findViewById(R.id.one_run);
        twoRun = v.findViewById(R.id.two_runs);
        threeRun = v.findViewById(R.id.three_runs);
        fourRun = v.findViewById(R.id.four_runs);
        sixRun = v.findViewById(R.id.six_runs);
        dotBall = v.findViewById(R.id.dot_ball);
        wideBall = v.findViewById(R.id.wide_ball);
        noBall = v.findViewById(R.id.no_ball);
        outBtn = v.findViewById(R.id.out_btn);

        undoBtn = v.findViewById(R.id.undo_btn);

        matchEnded = v.findViewById(R.id.match_ended_view);
        buttonsLayout = v.findViewById(R.id.buttons_layout);
        viewScoreboard = v.findViewById(R.id.view_scoreboard_btn);
        goToScoreboard = v.findViewById(R.id.go_to_scoreboard_btn);

        lastBalls = v.findViewById(R.id.last_balls);
        runRate = v.findViewById(R.id.run_rate_ll);
        foursAndSixes = v.findViewById(R.id.fours_and_sixes);
        eleArray = new LinearLayout[]{runRate, lastBalls, foursAndSixes};
        foursTV = v.findViewById(R.id.fours_tv);
        sixesTV = v.findViewById(R.id.sixes_tv);
        extrasTV = v.findViewById(R.id.extras_tv);
        rrTV = v.findViewById(R.id.run_rate_tv);

        p1name = v.findViewById(R.id.p1name);
        p1runs = v.findViewById(R.id.p1runs);
        p1balls = v.findViewById(R.id.p1balls);
        p1fours = v.findViewById(R.id.p1fours);
        p1sixes = v.findViewById(R.id.p1sixes);
        p2name = v.findViewById(R.id.p2name);
        p2runs = v.findViewById(R.id.p2runs);
        p2balls = v.findViewById(R.id.p2balls);
        p2fours = v.findViewById(R.id.p2fours);
        p2sixes = v.findViewById(R.id.p2sixes);

        conti = getActivity().getIntent().getExtras().getBoolean("continue", false);

        for(int i=0; i<10; i++){
            prevBalls[i] = -1;
        }

        changeEle();

        /*if (!alreadyLoaded && !conti) {

        }*/

        if (conti && !alreadyLoaded) {
            alreadyLoaded = true;
            int[] teamData = Data.getIntData(getActivity(), /*11,*/ "teamScores");
            teamRuns = teamData[0];
            teamBalls = teamData[1];
            teamWickets = teamData[2];
            i = teamData[3];
            crease = teamData[4];
            runnerUp = teamData[5];
            fours = teamData[6];
            sixes = teamData[7];
            extras = teamData[8];
            totalOvers = teamData[9];
            for(int i=10; i<20; i++){
                prevBalls[i-10] = teamData[i];
            }
            Log.i("Check", "On load: " + teamRuns + " " + teamBalls + " " + teamWickets + " " + i + " " + crease + " " + runnerUp);
            if(teamWickets==10){
                disableButtons();
            }
            playerNames = Data.getStringData(getActivity(), /*11,*/ "playerData");
            individualRuns = Data.getIntData(getActivity(), /*11,*/ "runsData");
            individualBalls = Data.getIntData(getActivity(), /*11,*/ "ballsData");
            individualFours = Data.getIntData(getActivity(), /*11,*/ "foursData");
            individualSixes = Data.getIntData(getActivity(), /*11,*/ "sixesData");
            updatePlayersData();
        }
        else if(!alreadyLoaded){
            alreadyLoaded = true;
            getPrefs();
            OutputStreamWriter writer = null;
            try {
                writer = new OutputStreamWriter
                        (getActivity().openFileOutput("playerData", Context.MODE_PRIVATE));
                writer.write("");
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
            try {
                writer = new OutputStreamWriter
                        (getActivity().openFileOutput("runsData", Context.MODE_PRIVATE));
                writer.write("0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n0");
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            try {
                writer = new OutputStreamWriter
                        (getActivity().openFileOutput("ballsData", Context.MODE_PRIVATE));
                writer.write("");
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
            try {
                writer = new OutputStreamWriter
                        (getActivity().openFileOutput("foursData", Context.MODE_PRIVATE));
                writer.write("");
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
            try {
                writer = new OutputStreamWriter
                        (getActivity().openFileOutput("sixesData", Context.MODE_PRIVATE));
                writer.write("");
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        goToScoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScoreboardFragment scoreboardFragment = new ScoreboardFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, scoreboardFragment);
                ft.addToBackStack("");
                ft.commit();
            }
        });

        viewScoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScoreboardFragment scoreboardFragment = new ScoreboardFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, scoreboardFragment);
                ft.addToBackStack("");
                ft.commit();
            }
        });

        outBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamBalls++;
                teamWickets++;
                individualBalls[crease] += 1;
                updateTeamScores();
                savePlayersData();
                crease = i;
                getNextPlayerName();
                for(int i=9; i>0; i--){
                    prevBalls[i] = prevBalls[i-1];
                }
                prevBalls[0] = WICKET;
                //secondLastBall = lastBall;
                //lastBall = WICKET;
                if(i==11){
                    disableButtons();
                }
                disableUndo();
            }
        });

        dotBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamBalls++;
                individualBalls[crease] += 1;
                updateTeamScores();
                //savePlayersData();
                for(int i=9; i>0; i--){
                    prevBalls[i] = prevBalls[i-1];
                }
                prevBalls[0] = DOT_BALL;
                updatePlayersData();
                //secondLastBall = lastBall;
                //lastBall = DOT_BALL;
                enableUndo();
            }
        });

        oneRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamRuns++;
                teamBalls++;
                individualRuns[crease] += 1;
                individualBalls[crease] += 1;
                updateTeamScores();
                //savePlayersData();
                swapCrease();
                for(int i=9; i>0; i--){
                    prevBalls[i] = prevBalls[i-1];
                }
                prevBalls[0] = ONE_RUN;
                updatePlayersData();
                //secondLastBall = lastBall;
                //lastBall = ONE_RUN;
                enableUndo();
            }
        });

        twoRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamRuns += 2;
                teamBalls++;
                individualRuns[crease] += 2;
                individualBalls[crease] += 1;
                updateTeamScores();
                //savePlayersData();
                for(int i=9; i>0; i--){
                    prevBalls[i] = prevBalls[i-1];
                }
                prevBalls[0] = TWO_RUN;
                updatePlayersData();
                //secondLastBall = lastBall;
                //lastBall = TWO_RUN;
                enableUndo();
            }
        });

        threeRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamRuns += 3;
                teamBalls++;
                individualRuns[crease] += 3;
                individualBalls[crease] += 1;
                updateTeamScores();
                //savePlayersData();
                swapCrease();
                for(int i=9; i>0; i--){
                    prevBalls[i] = prevBalls[i-1];
                }
                prevBalls[0] = THREE_RUN;
                updatePlayersData();
                //secondLastBall = lastBall;
                //lastBall = THREE_RUN;
                enableUndo();
            }
        });

        fourRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamRuns += 4;
                teamBalls++;
                fours++;
                individualRuns[crease] += 4;
                individualBalls[crease] += 1;
                individualFours[crease] += 1;
                updateTeamScores();
                //savePlayersData();
                for(int i=9; i>0; i--){
                    prevBalls[i] = prevBalls[i-1];
                }
                prevBalls[0] = FOUR_RUN;
                updatePlayersData();
                //secondLastBall = lastBall;
                //lastBall = FOUR_RUN;
                enableUndo();
            }
        });

        sixRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamRuns += 6;
                teamBalls++;
                sixes++;
                individualRuns[crease] += 6;
                individualBalls[crease] += 1;
                individualSixes[crease] += 1;
                updateTeamScores();
                //savePlayersData();
                for(int i=9; i>0; i--){
                    prevBalls[i] = prevBalls[i-1];
                }
                prevBalls[0] = SIX_RUN;
                updatePlayersData();
                //secondLastBall = lastBall;
                //lastBall = SIX_RUN;
                enableUndo();
            }
        });

        wideBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamRuns++;
                extras++;
                updateTeamScores();
                //savePlayersData();
                for(int i=9; i>0; i--){
                    prevBalls[i] = prevBalls[i-1];
                }
                prevBalls[0] = WIDE_BALL;
                updatePlayersData();
                //secondLastBall = lastBall;
                //lastBall = WIDE_BALL;
                enableUndo();
            }
        });

        noBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamRuns++;
                extras++;
                updateTeamScores();
                //savePlayersData();

                for(int i=9; i>0; i--){
                    prevBalls[i] = prevBalls[i-1];
                }
                prevBalls[0] = NO_BALL;
                updatePlayersData();
                individualBalls[crease] -= 1;
                teamBalls--;                //secondLastBall = lastBall;
                //lastBall = NO_BALL;
                enableUndo();
            }
        });

        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(matchEnded.getVisibility()==View.VISIBLE){
                    matchEnded.setVisibility(View.GONE);
                    buttonsLayout.setVisibility(View.VISIBLE);
                    goToScoreboard.setVisibility(View.VISIBLE);
                }
                undoLastTurn();
            }
        });

        return v;
    }

    public void undoLastTurn(){

        if(prevBalls[0]==NO_BALL) {
            teamBalls++;
        }
        if ((teamBalls) % 6 == 0 && ((teamBalls) / 6) == --c) {
            swapCrease();
        }
        if(prevBalls[0]==NO_BALL){
            teamBalls--;
        }
        if(prevBalls[1]==NO_BALL && (prevBalls[0]==ONE_RUN || prevBalls[0]==THREE_RUN)){
            swapCrease();
        }

        switch (/*lastBall*/prevBalls[0]) {
            case DOT_BALL:
                teamBalls--;
                individualBalls[crease] -= 1;
                for(int i=1; i<9; i++){
                    prevBalls[i-1] = prevBalls[i];
                }
                prevBalls[9] = -1;
                updateTeamScores();
                updatePlayersData();
                //lastBall = secondLastBall;
                //secondLastBall = -1;
                break;
            case ONE_RUN:
                swapCrease();
                teamRuns--;
                teamBalls--;
                individualRuns[crease] -= 1;
                individualBalls[crease] -= 1;
                for(int i=1; i<10; i++){
                    prevBalls[i-1] = prevBalls[i];
                }
                prevBalls[9] = -1;
                updateTeamScores();
                updatePlayersData();
                //lastBall = secondLastBall;
                //secondLastBall = -1;
                break;
            case TWO_RUN:
                teamRuns -= 2;
                teamBalls--;
                individualRuns[crease] -= 2;
                individualBalls[crease] -= 1;
                for(int i=1; i<10; i++){
                    prevBalls[i-1] = prevBalls[i];
                }
                prevBalls[9] = -1;
                updateTeamScores();
                updatePlayersData();
                //lastBall = secondLastBall;
                //secondLastBall = -1;
                break;
            case THREE_RUN:
                swapCrease();
                teamRuns -= 3;
                teamBalls--;
                individualRuns[crease] -= 3;
                individualBalls[crease] -= 1;
                for(int i=1; i<10; i++){
                    prevBalls[i-1] = prevBalls[i];
                }
                prevBalls[9] = -1;
                //lastBall = secondLastBall;
                //secondLastBall = -1;
                updateTeamScores();
                updatePlayersData();
                break;
            case FOUR_RUN:
                teamRuns -= 4;
                teamBalls--;
                fours--;
                individualRuns[crease] -= 4;
                individualBalls[crease] -= 1;
                individualFours[crease] -= 1;
                for(int i=1; i<10; i++){
                    prevBalls[i-1] = prevBalls[i];
                }
                prevBalls[9] = -1;
                updateTeamScores();
                updatePlayersData();
                //lastBall = secondLastBall;
                //secondLastBall = -1;
                break;
            case SIX_RUN:
                teamRuns -= 6;
                teamBalls--;
                sixes--;
                individualRuns[crease] -= 6;
                individualBalls[crease] -= 1;
                individualSixes[crease] -= 1;
                for(int i=1; i<10; i++){
                    prevBalls[i-1] = prevBalls[i];
                }
                prevBalls[9] = -1;
                updateTeamScores();
                updatePlayersData();
                //lastBall = secondLastBall;
                //secondLastBall = -1;
                break;
            case WIDE_BALL:
                teamRuns--;
                extras--;
                for(int i=1; i<10; i++){
                    prevBalls[i-1] = prevBalls[i];
                }
                prevBalls[9] = -1;
                updateTeamScores();
                updatePlayersData();
                //lastBall = secondLastBall;
                //secondLastBall = -1;
                break;
            case NO_BALL:
                teamRuns--;
                extras--;
                individualBalls[crease] += 1;
                teamBalls++;
                for(int i=1; i<10; i++){
                    prevBalls[i-1] = prevBalls[i];
                }
                prevBalls[9] = -1;
                updateTeamScores();
                updatePlayersData();
                //lastBall = secondLastBall;
                //secondLastBall = -1;
                break;
            case WICKET:
                break;
        }

        if(/*lastBall==-1*/prevBalls[0]==-1 || /*lastBall==WICKET*/prevBalls[0]==WICKET){
            disableUndo();
        }

    }

    private void enableUndo(){
        undoBtn.setEnabled(true);
        undoBtn.setVisibility(View.VISIBLE);
    }

    private void disableUndo(){
        undoBtn.setEnabled(false);
        undoBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(pausedThenResumed) {
            int[] teamData = Data.getIntData(getActivity(), /*11,*/ "teamScores");
            for (int i = 10; i < 20; i++) {
                prevBalls[i - 10] = teamData[i];
            }
            updateTeamScores();
            updatePlayersData();
            pausedThenResumed = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        alreadyLoaded = false;
    }

    private void getPrefs(){
        final Dialog prefsDialog = new Dialog(getActivity());
        prefsDialog.setContentView(R.layout.prefs_dialog);
        prefsDialog.setCanceledOnTouchOutside(false);

        Spinner spinner = (Spinner) prefsDialog.findViewById(R.id.spinner_overs);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.overs, R.layout.spinner_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(parent.getSelectedItemPosition()){
                    case 0: totalOvers = 999; break;
                    case 1: totalOvers = 1; break;
                    case 2: totalOvers = 5; break;
                    case 3: totalOvers = 10; break;
                    case 4: totalOvers = 20; break;
                    case 6: totalOvers = 50; break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                totalOvers = 999;
            }
        });

        final CheckBox playerDefNames = prefsDialog.findViewById(R.id.checkbox_default_player_names);

        Button ok = (Button) prefsDialog.findViewById(R.id.ok_button);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefsDialog.dismiss();
                isDefaultName = playerDefNames.isChecked();
                getNextPlayerName();
            }
        });

        prefsDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                totalOvers = 999;
                isDefaultName = playerDefNames.isChecked();
                getNextPlayerName();
            }
        });

        prefsDialog.show();
    }

    public void getNextPlayerName() {
        if (i < 11) {
            final Dialog nameDialog = new Dialog(getActivity());
            nameDialog.setContentView(R.layout.new_player_name_dialog);
            nameDialog.setCanceledOnTouchOutside(false);

            TextView nameTV = nameDialog.findViewById(R.id.name_text_view);
            if (i == 0)
                nameTV.setText("Enter strikers's name: ");
            else if (i == 1)
                nameTV.setText("Enter non striker's name");
            else
                nameTV.setText("Enter next batsman's name: ");

            final EditText nameET = (EditText) nameDialog.findViewById(R.id.name_edit_text);
            nameET.setHint("Player " + (i + 1));

            Button ok = (Button) nameDialog.findViewById(R.id.ok_button);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nameET.setSelectAllOnFocus(true);
                    i++;
                    if (!nameET.getText().toString().equals(""))
                        playerNames[i-1] = nameET.getText().toString();
                    else
                        playerNames[i-1] = "Player " + (i);
                    Data.putStringData(getActivity(), i, "playerData", playerNames);
                    updatePlayersData();
                    nameDialog.dismiss();
                    if (i == 1)
                        getNextPlayerName();
                }
            });

            nameDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    i++;
                    playerNames[i-1] = "Player " + (i);
                    Data.putStringData(getActivity(), i, "playerData", playerNames);
                    updatePlayersData();
                    if (i == 1)
                        getNextPlayerName();
                }
            });
            if(isDefaultName){
                i++;
                playerNames[i-1] = "Player " + (i);
                Data.putStringData(getActivity(), i, "playerData", playerNames);
                updatePlayersData();
                if (i == 1)
                    getNextPlayerName();
            }
            else
                nameDialog.show();
        }
    }

    public void swapCrease(){
        int temp = runnerUp;
        runnerUp = crease;
        crease = temp;
    }

    public void updateTeamScores(){
        StringBuilder sb = new StringBuilder();
        if(/*secondLastBall==NO_BALL*/prevBalls[1]==NO_BALL){
            teamBalls++;
        }
        sb.append((int)teamBalls/6);
        sb.append(".");
        sb.append((int)teamBalls%6);
        if(/*secondLastBall==NO_BALL*/prevBalls[1]==NO_BALL){
            teamBalls--;
        }


        if((teamBalls%6==0) && (teamBalls/6)==c){
            swapCrease();
            c++;
        }

        runsTextView.setText(""+teamRuns);
        ballsTextView.setText(""+sb.toString());
        wicketsTextView.setText(""+teamWickets);

        try {
            float b = (float)teamBalls/6;
            rr = (float)teamRuns / b;
        }catch (ArithmeticException e){
            rr = 0;
        }

        foursTV.setText("Fours: " + fours);
        sixesTV.setText("Sixes: " + sixes);
        extrasTV.setText("Extras: " + extras);
        rrTV.setText("Run rate: " + String.format("%.2f",rr));

        if(teamBalls==(totalOvers*6)){
            Toast.makeText(getContext(), "Match Ended", Toast.LENGTH_SHORT);
            matchEnded.setVisibility(View.VISIBLE);
            buttonsLayout.setVisibility(View.GONE);
            goToScoreboard.setVisibility(View.GONE);
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("Interstitial", "The interstitial wasn't loaded yet.");
            }
        }
    }

    public void updatePlayersData(){
        Log.i("Check", "On updation: " + teamRuns + " " + teamBalls + " " + teamWickets + " " + i + " " + crease + " " + runnerUp);
        if(crease>10){
            crease=10;
        }
        if(/*secondLastBall==NO_BALL*/prevBalls[1]==NO_BALL){
            individualBalls[crease]++;
        }
        p1name.setText(playerNames[crease]);
        p1runs.setText("" + individualRuns[crease]);
        p1balls.setText("" + individualBalls[crease]);
        p1fours.setText("" + individualFours[crease]);
        p1sixes.setText("" + individualSixes[crease]);
        p2name.setText(playerNames[runnerUp]);
        p2runs.setText("" + individualRuns[runnerUp]);
        p2balls.setText("" + individualBalls[runnerUp]);
        p2fours.setText("" + individualFours[runnerUp]);
        p2sixes.setText("" + individualSixes[runnerUp]);
        if(/*secondLastBall==NO_BALL*/prevBalls[1]==NO_BALL){
            individualBalls[crease]--;
        }

        lastBalls.removeAllViews();
        TextView tv1 = new TextView(getContext());
        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tv1.setLayoutParams(p1);
        tv1.setGravity(Gravity.CENTER);
        tv1.setText("Last balls: ");
        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        lastBalls.addView(tv1);
        for(int i=0; i<10; i++){
            TextView tv = new TextView(getContext());
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            tv.setLayoutParams(p);
            tv.setGravity(Gravity.CENTER);
            tv.setText(getBallsText(i) + "");
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            lastBalls.addView(tv);
        }

        savePlayersData();
    }


    private String getBallsText(int i){
        switch (prevBalls[i]){
            case DOT_BALL: return "0 ";
            case ONE_RUN: return "1 ";
            case TWO_RUN: return "2 ";
            case THREE_RUN: return "3 ";
            case FOUR_RUN: return "4 ";
            case SIX_RUN: return "6 ";
            case WIDE_BALL: return "WD ";
            case NO_BALL: return "NB ";
            case WICKET: return "W ";
        }
        return "";
    }

    public void savePlayersData(){
        int[] data = {teamRuns, teamBalls, teamWickets, i, crease, runnerUp, fours, sixes, extras, totalOvers};
        int[] teamData = new int[20];
        for(int i=0; i<10; i++){
            teamData[i] = data[i];
        }
        for(int i=10; i<20; i++){
            teamData[i] = prevBalls[i-10];
        }
        Log.i("Check", "On save: " + teamRuns + " " + teamBalls + " " + teamWickets + " " + i + " " + crease + " " + runnerUp);
        Data.putIntData(getActivity(), 20, "teamScores", teamData);
        Data.putIntData(getActivity(), i, "runsData", individualRuns);
        Data.putIntData(getActivity(), i, "ballsData", individualBalls);
        Data.putIntData(getActivity(), i, "foursData", individualFours);
        Data.putIntData(getActivity(), i, "sixesData", individualSixes);
    }

    public void disableButtons(){
        outBtn.setEnabled(false);
        oneRun.setEnabled(false);
        twoRun.setEnabled(false);
        threeRun.setEnabled(false);
        fourRun.setEnabled(false);
        sixRun.setEnabled(false);
        wideBall.setEnabled(false);
        noBall.setEnabled(false);
        dotBall.setEnabled(false);
    }

    public void changeEle(){
        visibleEle++;
        for(int i=0; i<NO_OF_ELE; i++){
            if(i==(visibleEle%3)) {
                eleArray[i].setVisibility(View.VISIBLE);
                eleArray[i].startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_from_left));
            }
            else {
                if(eleArray[i].getVisibility()==View.VISIBLE) {
                    eleArray[i].startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_to_left));
                    eleArray[i].setVisibility(View.GONE);
                }
            }
        }
        cd = new CountDownTimer(3000, 3000) {

            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                    changeEle();
            }

        }.start();
    }

    @Override
    public void onPause() {
        cd.cancel();
        pausedThenResumed = true;
        super.onPause();
    }


}
