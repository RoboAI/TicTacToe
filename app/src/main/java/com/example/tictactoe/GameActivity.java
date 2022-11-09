package com.example.tictactoe;

import android.content.Intent;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import static com.example.tictactoe.GameGlobals.DEFAULT_AI_DIFFICULTY;
import static com.example.tictactoe.GameGlobals.DEFAULT_GRID_SIZE;
import static com.example.tictactoe.GameGlobals.DEFAULT_WIN_COMBO;
import static com.example.tictactoe.SettingsActivity.TAG_AI_DIFFICULTY;
import static com.example.tictactoe.SettingsActivity.TAG_GRID_SIZE;
import static com.example.tictactoe.SettingsActivity.TAG_WIN_COMBO;

public class GameActivity extends AppCompatActivity implements
        GameView.IMainViewInflated,
        GameController.IUndoCountChanged{

    public static String TAG = "ABCD";
    public static final int SETTINGS_REQUEST = 1;

    private int adjustedGridSize = DEFAULT_GRID_SIZE;
    private int adjustedWinCombo = DEFAULT_WIN_COMBO;
    private int adjustedAI = DEFAULT_AI_DIFFICULTY;

    //TODO: these should go into the GameView class
    private PlayerTurnFrame playerTurnFrame;
    private WinningComboSizeView winningComboSizeView;
    private TictactoeGrid gameGrid;
    private PlayerTurnDots playerTurnDots;
    private BackgroundAux backgroundAux;
    private SoundEffects sounds;

    private GameView gameView;
    private GameModel gameModel;
    private GameController gameController;
    private GameSession gameSession;
    private TictactoeAI gameAI;

    private int colour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        colour = ContextCompat.getColor(getApplicationContext(), R.color.colorCellBlank);

        Utilities.requestFullScreen(this);
        Utilities.requestPortrait(this);

        ActionBar bar = getSupportActionBar();
        if(bar != null)
            bar.hide();

        setContentView(R.layout.activity_game);

        if (getIntent().getExtras() != null)
        {
            Bundle bundle = this.getIntent().getExtras();

            adjustedGridSize = bundle.getInt(TAG_GRID_SIZE);
            adjustedWinCombo = bundle.getInt(TAG_WIN_COMBO);
            adjustedAI = bundle.getInt(TAG_AI_DIFFICULTY);
        }
        else{
            adjustedGridSize = DEFAULT_GRID_SIZE;
            adjustedWinCombo = DEFAULT_WIN_COMBO;
            adjustedAI = DEFAULT_AI_DIFFICULTY;
        }

        sounds = new SoundEffects(this);
        gameModel = new GameModel();
        gameController = new GameController();
        gameSession = new GameSession();
        gameAI = new TictactoeAI();

        gameView = findViewById(R.id.gameContainer);
        playerTurnFrame = findViewById(R.id.playerTurnFrame);
        playerTurnDots = findViewById(R.id.playerTurnDots);
        winningComboSizeView = findViewById(R.id.textViewWinComboCount);
        gameGrid = findViewById(R.id.gameGrid);
        backgroundAux = findViewById(R.id.viewBackgroundAux);

        gameView.setViewInflatedCallback(this);

        //model-controller bind
        gameController.setModel(gameModel);

        //view-controller bind
        gameGrid.setController(gameController);

        //new game started
        gameController.addInewGameStarted(gameGrid);
        gameController.addInewGameStarted(sounds);
        gameController.addInewGameStarted(gameSession);
        gameController.addInewGameStarted(gameView);
        gameController.addInewGameStarted(gameAI);

        //touch/cell inputs
        gameController.addIcellInteraction(gameGrid);
        gameController.addIcellInteraction(sounds);
        gameController.addIcellInteraction(gameSession);

        //game ended
        gameController.addIgameEndedCelebration(sounds);
        gameController.addIgameEndedCelebration(gameGrid);
        gameController.addIgameEndedCelebration(playerTurnDots);

        //display for number-of-match-in-a-row
        gameController.addIwinComboCount(winningComboSizeView);

        //player turn changed
        gameController.addIplayerTurn(playerTurnFrame);
        gameController.addIplayerTurn(playerTurnDots);

        //undo button pressed
        gameController.addIundoCountChanged(this);

        //game is quitting
        gameController.addIgameIsQuitting(sounds);

        //set AI-Active state based on ai-toggle-button state
        gameController.setAiActive(((ToggleButton)findViewById(R.id.buttonAISwitch)).isChecked());

        SeekBar sb = findViewById(R.id.musicVolume);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //sounds.mediaPlayer.setVolume((float)progress / 100, (float)progress / 100);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar sb2 = findViewById(R.id.restartVolume);
        sb2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
              //  sounds.sounds.setVolume(sounds.newGameId, (float)progress/100, (float)progress/100);
                setTitle(String.valueOf(progress));

                colour = colour & 0x00FFFFFF;//70
                colour = colour | (progress << 24);
                gameGrid.setCellBlankColour(colour);
                /*
                colour = colour & 0xFF00FFFF;//40
                colour = colour | (progress << 16);
                gameGrid.setCellBlankColour(colour);
                 */
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


       // playerTurnDots.setLowDPImode(0);
        //winningComboSizeView.setLowDPImode(0);
    }

    public void onClick_UndoMove(View view){
        gameController.onClick_Undo();
    }

    public void onClick_ToggleAI(View view){
        gameController.setAiActive(((ToggleButton)view).isChecked());
    }

    public void onClick_Settings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        Bundle bundle = new Bundle();

        bundle.putInt(SettingsActivity.TAG_GRID_SIZE, adjustedGridSize);
        bundle.putInt(SettingsActivity.TAG_WIN_COMBO, adjustedWinCombo);
        bundle.putInt(SettingsActivity.TAG_AI_DIFFICULTY, adjustedAI);

        intent.putExtras(bundle);
        startActivityForResult(intent, SETTINGS_REQUEST);
    }

    public void onClick_Reset(View view){
        gameController.startNewGame();
    }

    public void onClick_Back(View view){
        gameController.exitGame();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        gameController.resumeGame();
        sounds.continuePlaying();
    }

    @Override
    protected void onPause() {
        super.onPause();

        sounds.pauseSounds();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        sounds.releaseResources();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SETTINGS_REQUEST){
            if(resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();

                int gridSize = bundle.getInt(SettingsActivity.TAG_GRID_SIZE);
                int winCombo = bundle.getInt(SettingsActivity.TAG_WIN_COMBO);
                int ai = bundle.getInt(SettingsActivity.TAG_AI_DIFFICULTY);

                if(adjustedGridSize != gridSize ||
                        adjustedWinCombo != winCombo) {

                    //TODO: tell user that game progress will be lost
                    adjustedGridSize = gridSize;
                    adjustedWinCombo = winCombo;
                    adjustedAI = ai;

                    gameController.startNewGame(adjustedGridSize, adjustedWinCombo, adjustedAI);
                }
                else if(adjustedAI != ai){
                    adjustedAI = ai;
                    gameController.setAiLevel(adjustedAI);
                }
            }
        }
    }

    @Override
    public void onLayoutChanged(Rect rcArea) {
        gameGrid.gridAreaChanged(rcArea);
    }

    @Override
    public void onUndoCountChanged(int count) {
        toast(String.valueOf(count));
    }

    public void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
