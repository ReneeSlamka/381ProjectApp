package com.example.ece381;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class JoinGame_IPName extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_game__ipname);
		Intent intent = getIntent();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.join_game__ipname, menu);
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
	
	public void continueToGame(View view) {
		Intent intent = new Intent(this, TheGame.class);
    	// pass the content of your EditText as parameter to the intent you use to start the other activity
    	EditText player2name = (EditText)findViewById(R.id.PlayerField);
        String player2 = player2name.getText().toString();
        if (player2.toString().trim().length() == 0) {
            Toast.makeText(this, "You did not enter your name!", Toast.LENGTH_SHORT).show();
            return;
        }
        intent.putExtra("Player2",player2);
    	startActivity(intent);
	}
	
}
