package com.example.ece381;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.ece381.TheGame.TCPReadTimerTask;


import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.util.Log;

public class TheGame_ViewMoves extends Activity{ 
	
	private AnimationDrawable tlAnim;
	private AnimationDrawable trAnim;
	private AnimationDrawable blAnim;
	private AnimationDrawable brAnim;
	private List<AnimationDrawable> animationList = new ArrayList<AnimationDrawable>();
	private Timer animationTimer;
	private int viewOnceFlag = 0;
	private Timer getUpdatesTimer;
	private Timer countDownTimer;
	private String latestMMReply;
	private int turnTrigger;
	private int countDown;
	private int counter;
	//private int check;
	private ImageView topLeftView;
	private ImageView topRightView; 
	private ImageView bottomLeftView;
	private ImageView bottomRightView;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_the_game__view_moves);
		
		Intent intent = getIntent();
		final String sequence = intent.getStringExtra(TheGame.EXTRA_MESSAGE);
		Log.i("ReceivedSequence", sequence);
		
		//timer stuff for notifications from MiddleMan
		getUpdatesTimer = new Timer();
		turnTrigger = 0;
		latestMMReply = "";
		//using TimerTask class from TheGame that receives MiddleMan messages 
		//TCPCheckforUpdates getUpdates  = new TCPCheckforUpdates();
		//getUpdatesTimer.schedule(getUpdates, 3000, 500);
		
		//flag that ensures can only view animation ONCE
		viewOnceFlag = 0;
		countDown = 0;
		counter = 0;
		
		//set up imageview animation for each button
		topLeftView = (ImageView)findViewById(R.id.imageViewTL);
		topRightView = (ImageView)findViewById(R.id.imageViewTR);
		bottomLeftView = (ImageView)findViewById(R.id.imageViewBL);
		bottomRightView = (ImageView)findViewById(R.id.imageViewBR);
		
		topLeftView.setBackgroundResource(R.drawable.tl_view_animation);
		topRightView.setBackgroundResource(R.drawable.tr_view_animation);
		bottomLeftView.setBackgroundResource(R.drawable.bl_view_animation);
		bottomRightView.setBackgroundResource(R.drawable.br_view_animation);
		
		tlAnim = (AnimationDrawable) topLeftView.getBackground();
		trAnim = (AnimationDrawable) topRightView.getBackground();
		blAnim = (AnimationDrawable) bottomLeftView.getBackground();
		brAnim = (AnimationDrawable) bottomRightView.getBackground();
		
		
		//build list of button animations to play in animation timer task
		if (sequence.length() > 0){
			for (int i = 0; i < (sequence.length()); i++){
				if (sequence.charAt(i) == 'Y'){
					animationList.add(tlAnim);
				}else if (sequence.charAt(i) == 'B') {
					animationList.add(trAnim);
				}else if (sequence.charAt(i) == 'R') {
					animationList.add(blAnim);	
				}else if (sequence.charAt(i) == 'G') {//char is 'G'
					animationList.add(brAnim);
				}else {
					//don't add anything, stray string slipped through thegame filter
				}
			}	
		}
		countDown = 3000 + (800)*(sequence.length()) + (500)*((sequence.length())-1) + 3000; //added extra 3 seconds as precaution
		Log.i("TotalTime", Integer.toString(countDown));
		counter = 0;
		//countDownTimer = new Timer(); 
        MyTimerTask switchingActTask = new MyTimerTask(); 
        Log.i("CountDownCount", Integer.toString(countDown));
        //countDownTimer.schedule(switchingActTask, countDown, 3000);//1000);
		//Log.i("AnimationSequence", animationList.toString());
		//create show sequence button that activates sequence animation on click

	}
	
	public class MyTimerTask extends TimerTask {
		
		public void run() {
		     
				/*runOnUiThread(new Runnable() { 
					public void run() {
				
					}  
				});	*/
					/*Log.i("CountDownCount", Integer.toString(counter));
			        Intent switchpage = new Intent(TheGame_ViewMoves.this, TheGame.class);
			        TheGame_ViewMoves.this.startActivity(switchpage);
			        countDownTimer.cancel();*/
			        
			}
	}
	
	public class AnimationTimerTask extends TimerTask{

		int animCount = 0;
		
		@Override
		public void run() {
			Log.i("AnimationCount", Integer.toString(animCount));
			runOnUiThread(new Runnable() {
				public void run() {
					Log.i("Animation", animationList.get(animCount).getCurrent().toString());
					animationList.get(animCount).start();
				}
			});
			while(animationList.get(animCount).getCurrent() !=  animationList.get(animCount).getFrame(2)){
				//wait until reaches last frame to stop it
			}
			//lose about 200 seconds on last frame of animation here, but shouldn't matter
			animationList.get(animCount).stop();
			Log.i("AnimationCountConfirmPrev", Integer.toString(animCount));
			if (animCount < (animationList.size() - 1)){
				animCount = animCount + 1;
			}	
			else{
				Intent switchToTheGame = new Intent(TheGame_ViewMoves.this, TheGame.class);
				startActivity(switchToTheGame);
				animationTimer.cancel();
			}
		}
	}
	
	// code copied from MainActivity of second middleman tutorial project	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus){
			if ((animationList.size() != 0)){
				//timer for running animation
				animationTimer = new Timer();
				AnimationTimerTask animTask = new AnimationTimerTask();
				animationTimer.schedule(animTask, 2000, 1500); //was (1500)5000 and 4000
				
				//countdown timer for switching activity when anim done
				/*Log.i("CountDownCount", Integer.toString(countDown));
				countDownTimer = new Timer(); 
		        MyTimerTask switchingActTask = new MyTimerTask(); 
		        countDownTimer.schedule(switchingActTask, countDown, 3000);*/
				
			} //else do nothing
			
		}
	}
	
	public void goToTheGame(View view){
		Intent intent = new Intent(this, TheGame.class);
    	startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.the_game__view_moves, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
