package com.example.fuat.newtictactoe;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.fuat.newtictactoe.GameGlobals.AI_HARD;
import static com.example.fuat.newtictactoe.GameGlobals.AI_VERY_HARD;
import static com.example.fuat.newtictactoe.GameGlobals.AI_MEDIUM;

public class SettingsActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final String TAG_GRID_SIZE = "TAG_GRID_SIZE";
    public static final String TAG_WIN_COMBO = "TAG_WIN_COMBO";
    public static final String TAG_AI_DIFFICULTY = "TAG_AI_DIFFICULTY";
    public static final String TAG_MEDIAPLAYER = "TAG_MEDIAPLAYER";
    public static final String TAG_SOUNDPOOL = "TAG_SOUNDPOOL";

    public static final int SIZE_OFFSET_GRID = 3;
    public static final int SIZE_OFFSET_WINCOMBO = 3;

    private TextView textGridSize;
    private SeekBar seekSize;

    private TextView textWinCombo;
    private SeekBar seekWinCombo;

    private TextView textDifficulty;
    private SeekBar seekDifficulty;

    private int adjustedSize;
    private int adjustedWinCombo;
    private int adjustedDifficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utilities.requestFullScreen(this);
        Utilities.requestPortrait(this);

        ActionBar bar = getSupportActionBar();
        if(bar != null)
            bar.hide();

        setContentView(R.layout.activity_settings);

        setupControls();
        showDefaultValues();
    }

    protected void showDefaultValues(){
        if (SettingsActivity.this.getIntent().getExtras() != null)
        {
            Bundle bundle = this.getIntent().getExtras();

            adjustedSize = bundle.getInt(TAG_GRID_SIZE);
            setGridSizeText(adjustedSize);
            seekSize.setProgress(adjustedSize - SIZE_OFFSET_GRID);

            adjustedWinCombo = bundle.getInt(TAG_WIN_COMBO);
            setWinComboText(adjustedWinCombo);
            seekWinCombo.setProgress(adjustedWinCombo - SIZE_OFFSET_WINCOMBO);

            adjustedDifficulty = bundle.getInt(TAG_AI_DIFFICULTY);
            setDifficultyText(adjustedDifficulty);
            seekDifficulty.setProgress(adjustedDifficulty);

            //sp = bundle.get
        }
    }

    protected void setupControls(){
        textGridSize = findViewById(R.id.textViewGridSize);
        seekSize = findViewById(R.id.seekBar);
        seekSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                adjustedSize = progress + SIZE_OFFSET_GRID;
                setGridSizeText(adjustedSize);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        textWinCombo = findViewById(R.id.textViewWinCombo);
        seekWinCombo = findViewById(R.id.seekBarWinCombo);
        seekWinCombo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                adjustedWinCombo = progress + SIZE_OFFSET_WINCOMBO;
                setWinComboText(adjustedWinCombo);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        textDifficulty = findViewById(R.id.textViewDifficulty);
        seekDifficulty = findViewById(R.id.seekBarDifficulty);
        seekDifficulty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                adjustedDifficulty = progress;
                setDifficultyText(adjustedDifficulty);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    protected void setGridSizeText(int size){
        String text = String.valueOf(size);
        textGridSize.setText(text + " x " + text);
    }

    protected void setWinComboText(int size){
        String text = String.valueOf(size);
        textWinCombo.setText(text);
    }

    protected void setDifficultyText(int difficulty){
        String text = "error";

        if(adjustedDifficulty == AI_MEDIUM)
            text = "Medium";

        else if(adjustedDifficulty == AI_HARD)
            text = "Hard";

        else if(adjustedDifficulty == AI_VERY_HARD)
            text = "Impossible";

        textDifficulty.setText(text);
    }

    public void onClick_Back(View view){
        if(adjustedWinCombo <= adjustedSize) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();

            bundle.putInt(SettingsActivity.TAG_GRID_SIZE, adjustedSize);
            bundle.putInt(SettingsActivity.TAG_WIN_COMBO, adjustedWinCombo);
            bundle.putInt(SettingsActivity.TAG_AI_DIFFICULTY, adjustedDifficulty);

            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }
        else{
            toast("Winning Boxes must be less than Grid Size");
        }
    }

    public void toast(String msg){
        Toast.makeText(this,  msg, Toast.LENGTH_SHORT).show();
    }
}
