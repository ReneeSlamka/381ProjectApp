package com.example.ece381;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class TheGame extends Activity {
	
	//strings to be sent to middleman and view sequence page
	String msg = "";
	String message = "";
	
	//Brandon new code
	String mManResponse_copy = "abcd";
	
	public final static String EXTRA_MESSAGE = "final String";
	
	
	//Brandon's code, for making sound
	/*private Button yellow, blue, red, green;
	private SoundPool spool;
	private int soundID;*/
	
	//Brandon's code for speech input
	protected static final int REQUEST_OK = 1;
	private ImageButton speechButton;
	private TextView speechText;
	
	TextView LobbyLeader; 
	
	//New code
	Intent sendSeqIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//NEW PRECAUTION CODE 
		msg = "";
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_the_game);
		
		//LobbyLeader = (TextView)findViewById(R.id.lobbyleader);
		//LobbyLeader.setText(getIntent().getExtras().getString("LobbyLeader"));
		
		//Brandon's code for speech input--------------------------------
		/*speechText = (TextView) findViewById(R.id.speechToText1);
        speechButton = (ImageButton) findViewById(R.id.button2);
        speechButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                try {
                    startActivityForResult(intent, REQUEST_OK);
                    speechText.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast error = Toast.makeText(getApplicationContext(),
                            "Opps! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    error.show();
                }
            }
        });*/
        //End of Brandon's code for speech input-----------------------------
		

		// code previously in MainActivity that sets up a timer
		// task that reads the input queue from middleman
		// every 500 ms 3000ms after it's started
		TCPReadTimerTask tcp_task = new TCPReadTimerTask();
		Timer tcp_timer = new Timer();
		tcp_timer.schedule(tcp_task, 3000, 500);
		
		//Code to protect against null value in intent to ViewSequence
		//REMEMBER YOU DO THIS
		message = "X";
		sendSeqIntent = new Intent(this, TheGame_ViewMoves.class);
		sendSeqIntent.putExtra(EXTRA_MESSAGE, message);
		
		
		//COME BACK TO THIS AFTER
		
		//****commented out sound because crashes app atm****
		 /*this.setVolumeControlStream(AudioManager.STREAM_MUSIC); spool = new
		 SoundPool(10, AudioManager.STREAM_MUSIC, 0); soundID =
		 spool.load(this, R.raw.coloured_button, 1);
		
		 yellow = (Button)findViewById(R.id.topleftButton);
		 yellow.setOnClickListener(new View.OnClickListener() { public void
		 onClick(View v) { btnSound(); } });
		 
		 blue = (Button)findViewById(R.id.topRightButton);
		 blue.setOnClickListener(new View.OnClickListener() { public void
		 onClick(View v) { btnSound(); } });
		 
		 red = (Button)findViewById(R.id.bottomLeftButton);
		 red.setOnClickListener(new View.OnClickListener() { public void
		 onClick(View v) { btnSound(); } });
		 
		 green = (Button)findViewById(R.id.bottomRightButton);
		 green.setOnClickListener(new View.OnClickListener() { public void
		 onClick(View v) { btnSound(); } });*/
		 
	}

	
	// premade functions --> leave these as defaults for now--------------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.the_game, menu);
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
	// end of premade functions ------------------------------------------------------
	
	
	//function that asks user to confirm they want to leave if 
	//press back button
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Rage Quitting?")
	        .setMessage("Are you sure you want to leave this lobby?")
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            finish();    
	        }

	    })
	    .setNegativeButton("No", null)
	    .show();
	}
	
	public void clickedYellow(View view) {
		msg = msg.concat("Y");
		Log.i("SendAttempt", msg);
	}

	public void clickedBlue(View view) {
		msg = msg.concat("B");
		Log.i("SendAttempt", msg);
	}
 
	public void clickedGreen(View view) {
		msg = msg.concat("G");
		Log.i("SendAttempt", msg);
	}

	public void clickedRed(View view) {
		msg = msg.concat("R");
		Log.i("SendAttempt", msg);
	}

	public void gotoViewSequence(View view) {
		if(message.equals("X")){
			//do not go
		} else {
		Log.i("Sent to ViewPage", message);
		//Intent sendSeqIntent = new Intent(this, TheGame_ViewMoves.class);
		//sendSeqIntent.putExtra(EXTRA_MESSAGE, message);
		startActivity(sendSeqIntent);
		}

	}

	/*public void btnSound() {
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		float volume = (float) audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		spool.play(soundID, volume, volume, 1, 0, 1f);
	};*/
	// -----------end of buton click operations---------------------------------------------------
	
	//
	public void sendAttempt(View view) {
		
		MyApplication app = (MyApplication) getApplication();
		
		// Create an array of bytes. First byte will be the
		// message length, and the next ones will be the message
		byte buf[] = new byte[msg.length()];
		// buf[0] = (byte) msg.length();
		System.arraycopy(msg.getBytes(), 0, buf, 0, msg.length());
		// Now send through the output stream of the socket
		OutputStream out;
		try {
			out = app.sock.getOutputStream();
			try {
				out.write(buf, 0, msg.length());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		msg = "";
	}

	// code copied from MainActivity of second middleman tutorial project
	//receives MM response
	public class TCPReadTimerTask extends TimerTask {
		public void run() {
			MyApplication app = (MyApplication) getApplication();
			if (app.sock != null && app.sock.isConnected()
					&& !app.sock.isClosed()) {
				try {
					InputStream in = app.sock.getInputStream();
					// See if any bytes are available from the Middleman
					int bytes_avail = in.available();
					if (bytes_avail > 0) {
						// If so, read them in and create a string
						byte buf[] = new byte[bytes_avail];
						in.read(buf);
						final String mManResponse = new String(buf, 0,
								bytes_avail, "US-ASCII");
						//get mManResponse for ViewMoves page
						if ((mManResponse.endsWith("wrong")) || (mManResponse.endsWith("You Won")) || 
								(mManResponse.endsWith("Your Turn"))){
							//ignore this message, not the sequence
						} else { 
							message = mManResponse; 
							Log.i("Current message outside runUI", message);
						}
						
						//code to update GUI using GUI thread
						runOnUiThread(new Runnable() {
							public void run() {
								Context gameplay_context = getApplicationContext();
								CharSequence gameResult;
								int duration = Toast.LENGTH_LONG;
								// String gameResult;
								Log.i("SendAttempt", mManResponse);
								//REFACTOR
								if ((mManResponse.endsWith("wrong")) || (mManResponse.endsWith("You Won")) || 
								(mManResponse.endsWith("Your Turn"))){
									//ignore this message, not the sequence
								} else { 
									message = mManResponse; 
									Log.i("CurrentMessage inside runUI", message);
								}
								if (mManResponse.endsWith("wrong")) {
									gameResult = "You Lose, Try Again!";
								} else {
									gameResult = mManResponse;
											
								}
								//should maybe create intent with message in onCreate just in case
								if(sendSeqIntent.getExtras() != null){
									String clearExtra = sendSeqIntent.getStringExtra(EXTRA_MESSAGE);
									sendSeqIntent.removeExtra(clearExtra);
									sendSeqIntent.putExtra(EXTRA_MESSAGE, message);
								} else{ //IF IS NULL (MAY NEED TO REMOVE)
									sendSeqIntent.putExtra(EXTRA_MESSAGE, message);
								}
								
								//Brandon's timer code
								//end of timer code
								
								//show MM response on screen in toast for debugging
								Toast showResult = Toast.makeText(
										gameplay_context, gameResult, duration);
								showResult.show();
								
							}
						});
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	} 
	
	/*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case REQUEST_OK: {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> text = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                speechText.setText(text.get(0));
                
                if (text.get(0).equals("yellow")){
                	msg = msg.concat("Y");
                } else if ((text.get(0).equals("blue")) || (text.get(0).equals("hello"))){
                	msg = msg.concat("B");
                } else if ((text.get(0).equals("green")) || (text.get(0).equals("screen"))){
                	msg = msg.concat("G");
                } else if (text.get(0).equals("red")){ //equals "R"?
                	msg = msg.concat("R");
                }
                Log.i("Voice Message", message);
            }
            break;
        }
        }
    }*/
}