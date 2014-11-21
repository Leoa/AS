package com.sugardefynery.animeconvention.scheduler.Alerts;

import com.sugardefynery.animeconvention.scheduler.PreferenceConnector;
import com.sugardefynery.animeconvention.scheduler.R;
import com.sugardefynery.animeconvention.scheduler.TabBarExample;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AlertSettings extends Activity {

	Button btnSound;
	Button btnVibrate;
	Button btnBack;
	int vibrateState = 0;
	int soundState = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alertsettings);

		btnSound = (Button) findViewById(R.id.btnSound);
		btnVibrate = (Button) findViewById(R.id.btnVibrate);
		btnBack = (Button) findViewById(R.id.btnBack);

		
		vibrateState = PreferenceConnector.readInteger(AlertSettings.this,
				PreferenceConnector.VIBRATE_ON_OFF, 0);
		soundState = PreferenceConnector.readInteger(AlertSettings.this,
				PreferenceConnector.SOUND_ON_OFF, 0);

		if (vibrateState == 0) {

			btnVibrate.setText("Vibrate ON");

		} else {

			btnVibrate.setText("Vibrate OFF");

		}

		if (soundState == 0) {

			btnSound.setText("Sound ON");

		} else {

			btnSound.setText("Sound OFF");

		}

		System.out.println("sound state " + soundState + " vibrate state "
				+ vibrateState);

		btnVibrate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				vibrateState = PreferenceConnector.readInteger(
						AlertSettings.this, PreferenceConnector.VIBRATE_ON_OFF,
						0);

				// VIBRATE_ON_OFF
				switch (vibrateState) {
				case 0:

					PreferenceConnector.getEditor(AlertSettings.this)
							.remove(PreferenceConnector.VIBRATE_ON_OFF)
							.commit();
					vibrateState = 1;
					PreferenceConnector.writeInteger(AlertSettings.this,
							PreferenceConnector.VIBRATE_ON_OFF, vibrateState);
					btnVibrate.setText("Vibrate OFF");
					break;

				case 1:

					// SOUND_ON_OFF
					btnVibrate.setText("Vibrate ON");
					vibrateState = 0;
					PreferenceConnector.writeInteger(AlertSettings.this,
							PreferenceConnector.VIBRATE_ON_OFF, vibrateState);
					break;

				}

				

			}
		});

		btnSound.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				soundState = PreferenceConnector
						.readInteger(AlertSettings.this,
								PreferenceConnector.SOUND_ON_OFF, 0);

				// VIBRATE_ON_OFF
				switch (soundState) {
				case 0:

					PreferenceConnector.getEditor(AlertSettings.this)
							.remove(PreferenceConnector.SOUND_ON_OFF)
							.commit();
					soundState = 1;
					PreferenceConnector.writeInteger(AlertSettings.this,
							PreferenceConnector.SOUND_ON_OFF, soundState);
					btnSound.setText("Sound OFF");
					break;

				case 1:

					// SOUND_ON_OFF
					btnSound.setText("Sound ON");
					soundState = 0;
					PreferenceConnector.writeInteger(AlertSettings.this,
							PreferenceConnector.SOUND_ON_OFF, soundState);
					break;

				}

				

			}
		});


		btnBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

			
				 Intent intent = new
				 Intent(AlertSettings.this.getApplicationContext(),
				 TabBarExample.class);
				 startActivity(intent);

			}
		});

		
		
	}
}
