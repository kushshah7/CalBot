package info.androidhive.firebase;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.speech.RecognizerIntent;
import android.widget.TextView;
import android.widget.Toast;

import android.speech.tts.TextToSpeech;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Locale;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {
    protected static final int RESULT_SPEECH = 1;

    private Button buttonLogout;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth firebaseAuth;
    private Button btnSpeak;
    private TextView txtText;
    private String txt;
    private Logic data;
    private TextView  calGoal;
    private TextView calEat;
    private TextView txtReply;

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        progressBar = (ProgressBar) findViewById(R.id.progressBar3);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(this);

        data = new Logic();
        txtText = (TextView) findViewById(R.id.txtText);
        txtReply = (TextView) findViewById(R.id.txtReply);
        calGoal = (TextView) findViewById(R.id.calGoal);
        calGoal.setText(data.up.getDaily());
        calEat = (TextView) findViewById(R.id.calEat);
        calEat.setText(data.up.getCurrent());
        txt = txtText.getText().toString();
        //Log.i("Calbot DEBUGG", txt);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.CANADA);
                }
            }
        });

        btnSpeak = (Button) findViewById(R.id.btnSpeak);
        btnSpeak.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    //txtText.setText("");

                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Opps! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    txtText.setText(text.get(0));
                    txt = txtText.getText().toString();
                    //Log.i("Calbot DEBUGG", txt);
                    this.data.change(txt);
                    calEat.setText(this.data.up.getCurrent());
                    txtReply.setText(this.data.cd.getReply());
                    this.progressBar.setProgress(Math.round(this.data.up.getPercent()));
                    tts.speak(txtReply.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                }
                break;
            }

        }
    }

    @Override
    public void onClick(View view) {
        if(view == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}