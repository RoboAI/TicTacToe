package com.example.fuat.newtictactoe;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

public class SoundEffects implements GameController.ICellInteraction,
                                     GameController.IGameEndedCelebration,
                                     GameController.INewGameStarted,
                                     GameController.IGameIsQuitting{

    private SoundPool sounds;
    private int playerOid;
    private int playerXid;
    private int playerOwin;
    private int playerXwin;
    private int newGameId;
    private int undoId;

    private MediaPlayer mediaPlayer;

    private boolean bFirst = true;

    public SoundEffects(Context context){
        init();

        playerOid = sounds.load(context, R.raw.pop1, 3);
        playerXid = sounds.load(context, R.raw.pop2, 3);
        playerOwin = sounds.load(context, R.raw.player_win, 1);
        playerXwin = sounds.load(context, R.raw.player_win, 1);
        newGameId = sounds.load(context, R.raw.new_game, 0);
        undoId = sounds.load(context, R.raw.whoosh, 3);

        mediaPlayer = MediaPlayer.create(context, R.raw.ambient);
        mediaPlayer.setLooping(true);
    }

    private void init(){
        createSoundPool();
    }

    protected void createSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        } else {
            createOldSoundPool();
        }
    }

    public void releaseResources(){
        sounds.unload(playerOid);
        sounds.unload(playerXid);
        sounds.release();
        sounds = null;

        mediaPlayer.release();
        mediaPlayer = null;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void createNewSoundPool(){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        sounds = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .setMaxStreams(5)
                .build();
    }

    @SuppressWarnings("deprecation")
    protected void createOldSoundPool(){
        sounds = new SoundPool(5,AudioManager.STREAM_MUSIC,0);
    }

    public void playTurnSound(int player){

    }

    public void playCheeringSound(){

    }

    public void playGameEndedSound(GameResult gameResult){

    }

    public void pauseSounds(){
        mediaPlayer.pause();
    }

    public void continuePlaying(){
        mediaPlayer.start();
    }

    @Override
    public void displayGameEnded(GameResult gameResult) {
        if (gameResult.result == GameGlobals.GAME_ENDED_O_WON){
            sounds.play(playerOwin, 1.0f, 1.0f, 0, 0, 1);
        }else if(gameResult.result == GameGlobals.GAME_ENDED_X_WON){
            sounds.play(playerXwin, 1.0f, 1.0f, 0, 0, 1);
        }
    }

    @Override
    public void onNewGameStarted(GameState gameState) {
        if(bFirst) {
            mediaPlayer.setVolume(0.2f, 0.2f);
            mediaPlayer.start();
            bFirst = false;
        }else{
            sounds.play(newGameId, 1.0f, 1.0f, 0, 0, 1);
        }
    }

    @Override
    public void cellClicked(SingleMove singleMove) {
        if (singleMove.player == GameGlobals.PLAYER_O){
            sounds.play(playerOid, 1.0f, 1.0f, 0, 0, 1);
        }else if(singleMove.player == GameGlobals.PLAYER_X){
            sounds.play(playerXid, 1.0f, 1.0f, 0, 0, 1);
        }
    }

    @Override
    public void undoCell(SingleMove singleMove) {
        sounds.play(undoId, 1.0f, 1.0f, 0, 0, 1);
    }

    @Override
    public void onGameIsQuitting() {
    }
}
