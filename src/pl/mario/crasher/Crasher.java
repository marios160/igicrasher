package pl.mario.crasher;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Crasher extends Activity {

	EditText ip;
	EditText port;
	Button crash;
	TextView status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crasher);
		ip = (EditText) findViewById(R.id.editText1);
		port = (EditText) findViewById(R.id.editText2);
		crash = (Button) findViewById(R.id.button1);
		status = (TextView) findViewById(R.id.textView1);

		/*runOnUiThread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						String message = Bum.getStatus();
						if (!message.isEmpty()) {
							status.setText(message);
							System.out.println("Server responsed");
						} else {
							status.setText("Not responded");
							System.out.println("Server not responsed");
						}

						Thread.sleep(5000);
					} catch (InterruptedException ex) {
						Logger.getLogger(Bum.class.getName()).log(Level.SEVERE, null, ex);
					}
				}

			}
		});*/
		synchronized (this) {

		new Thread(new Runnable() {
			public void run() {

				while (true) {
					try {
						String message = Bum.getStatuss();
						if (!message.isEmpty()) {
							setText(status,message);
							System.out.println("Server responsed");
						} else {
							setText(status,"Not responded");
							System.out.println("Server not responsed");
						}

						Thread.sleep(5000);
					} catch (InterruptedException ex) {
						Logger.getLogger(Bum.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}
		}).start();
		}

	}
	
	private void setText(final TextView text,final String value){
	    runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	            text.setText(value);
	        }
	    });
	}

	public void start(View v) {
		new Bum(this, ip.getText().toString(), port.getText().toString()).execute("a","s");

		
	}
	
	public void save(View v) {
		Bum b = new Bum(this, ip.getText().toString(), port.getText().toString());
		//b.crash();
	}

}
