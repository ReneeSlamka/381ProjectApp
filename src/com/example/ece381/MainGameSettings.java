package com.example.ece381;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainGameSettings extends Activity {
	
	Spinner spinner;
	String[] paths = { "4", "3", "2", "1" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_game_settings);
		Intent intent = getIntent();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainGameSettings.this, android.R.layout.simple_spinner_item, paths);
		spinner = (Spinner) findViewById(R.id.player_spinner);
		spinner.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_game_settings, menu);
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
	
	public void goToGameplay(View view) {
    	
    	Intent intent = new Intent(this, TheGame.class);
    	// pass the content of your EditText as parameter to the intent you use to start the other activity
    	EditText leadername = (EditText)findViewById(R.id.PlayerField);
        String leader = leadername.getText().toString();
        if (leader.toString().trim().length() == 0) {
            Toast.makeText(this, "You did not enter your name!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        EditText lobbyname = (EditText)findViewById(R.id.editText1);
        String LobbyName = lobbyname.getText().toString();
        if (LobbyName.toString().trim().length() == 0) {
            Toast.makeText(this, "You did not enter a lobby name!", Toast.LENGTH_SHORT).show();
            return;
        }
        //this info not going anywhere
        //TODO: use prof code to send lobby name to middleman
        intent.putExtra("LobbyLeader",leader);
        intent.putExtra("LobbyName",LobbyName);
    	startActivity(intent);
    	
    }
	
	
}
