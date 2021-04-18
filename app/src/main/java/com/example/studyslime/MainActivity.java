package com.example.studyslime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //Const variables
    private static final long START_TIME = 600000;

    //Initialize variable
    DrawerLayout drawerLayout;
    TextView testData_text, countDown_text;
    UserData userData;
    Button timerButton;
    CountDownTimer countDownTimer;

    //Database variables
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference testValueRef = databaseReference.child("TestValue");

    boolean timerRunning, timerFinished;
    long timeLeft = START_TIME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);
        testData_text = findViewById(R.id.testData_text);
        timerButton = findViewById(R.id.Timer_button);
        countDown_text = findViewById(R.id.countDown_text);
        timerFinished = true;

        userData = DataUtil.getUserData(this);

        //Initialize view
        //testData_text.setText(String.valueOf(userData.getTestData()));
        UpdateCountDownText();
    }


    //Test Method
    public void ClickButton(View view) {
        userData = DataUtil.getUserData(this);
        DataUtil.saveUserData(this, DataUtil.testData_name, userData.getTestData() + 1);
        testData_text.setText(String.valueOf(userData.getTestData()));
    }

    //Timer Methods
    public void ClickTimerButton(View view) {
        if (timerRunning && !timerFinished) {
            PauseTimer();
        }
        else if (!timerRunning && !timerFinished) {
            ResetTimer();
        }
        else if (!timerRunning && timerFinished) {
            StartTimer();
        }
    }

    public void StartTimer() {
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                UpdateCountDownText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                timerButton.setText("start");
                timerFinished = true;
            }
        }.start();

        timerRunning = true;
        timerFinished = false;
        timerButton.setText("pause");

    }

    public void PauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        timerButton.setText("reset");
    }

    public void ResetTimer() {
        timeLeft =  START_TIME;
        UpdateCountDownText();
        timerFinished = true;
        timerButton.setText("start");
    }

    public void UpdateCountDownText() {
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        countDown_text.setText(timeLeftFormatted);
    }

    //Menu Methods
    public void ClickMenu(View view) {
        //Open drawer
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        //Open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view) {
        //Close drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //Close drawer layout
        //Check condition
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //When drawer is open
            //Close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view) {
        //recreate activity
        recreate();
    }

    public void ClickCollections(View view) {
        //Redirect activity to collections
        redirectActivity(this, Collections.class);
    }

    public void ClickStatus(View view) {
        //Redirect activity to collections
        redirectActivity(this, Status.class);
    }

    public void ClickQuit(View view) {
        //Close app
        quit(this);
    }

    public static void quit(Activity activity) {
        //Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //Set title
        builder.setTitle("Quit");
        //Set message
        builder.setMessage("Are you sure you want to quit ?");
        //Positive yes button
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Finish activity
                activity.finishAffinity();
                //Exit app
                System.exit(0);
            }
        });
        //Negative no button
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Dismiss dialog
                dialog.dismiss();
            }
        });
        //Show dialog
        builder.show();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        //Initialize intent
        Intent intent = new Intent(activity, aClass);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity(intent);
    }

    //onStart Method
    @Override
    protected void onStart() {
        super.onStart();

        testValueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int value = snapshot.getValue(Integer.class);
                testData_text.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //onPause Method
    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        closeDrawer(drawerLayout);
    }
}