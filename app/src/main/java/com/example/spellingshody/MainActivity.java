package com.example.spellingshody;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.UserDictionary;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.daimajia.androidanimations.library.Techniques;
//import com.daimajia.androidanimations.library.YoYo;
//import com.example.spellingshody.R;
//import com.github.nisrulz.sensey.Sensey;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static android.icu.text.MessagePattern.ArgType.SELECT;

public class MainActivity extends AppCompatActivity {
    TextView resultText,mailText,helpText,insertText;
    EditText answerText;
    Spinner weeksSpin,daysSpin;
    ImageView miceImage;
    Button checkButton;
    TextToSpeech mic;
    int i , wrongs;
    String x,week,day,add;
    List<String>days=Arrays.asList("Saturday","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday");
    List<String>weeks=Arrays.asList("Week1","Week2","Week3","Week4");
    ArrayList<String> words ;
    MyDatabase db;
//    SharedPreferences saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultText = findViewById(R.id.resultText);
        answerText = findViewById(R.id.ansewrText);
        insertText = findViewById(R.id.insertText);
        weeksSpin = findViewById(R.id.weeksSpin);
        daysSpin = findViewById(R.id.daysSpin);
        mailText = findViewById(R.id.mailText);
        helpText = findViewById(R.id.helpText);
        miceImage = findViewById(R.id.micImg);
        checkButton = findViewById(R.id.checkButton);
        db = new MyDatabase(this);

        //fill spinner weeks & days
        ArrayAdapter adapterW = new ArrayAdapter(this,android.R.layout.simple_list_item_1,weeks);
        weeksSpin.setAdapter(adapterW);
        ArrayAdapter adapterD = new ArrayAdapter(this,android.R.layout.simple_list_item_1,days);
        daysSpin.setAdapter(adapterD);

        // lister for spinner weeks & days
        weeksSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                get();
                miceImage.setEnabled(true);
                Toast.makeText(MainActivity.this, words.size()+"", Toast.LENGTH_SHORT).show();
                if (words.size()==0){
                    miceImage.setEnabled(false);
                    Toast.makeText(MainActivity.this, "List is Empty Please Change Week Or Day", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        daysSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                get();
                miceImage.setEnabled(true);
                Toast.makeText(MainActivity.this, words.size()+"", Toast.LENGTH_SHORT).show();
                if (words.size()==0){
                    miceImage.setEnabled(false);
                    Toast.makeText(MainActivity.this, "List is Empty Please Change Week Or Day", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        get();
//        Toast.makeText(this, words.size()+"", Toast.LENGTH_SHORT).show();

        //mic speech rate
        mic = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                mic.setLanguage(Locale.ENGLISH);
                mic.setPitch(0.7f);
                mic.setSpeechRate(0.7f);
            }
        });

//        miceImage.setEnabled(false);
////        saved=getPreferences(MODE_PRIVATE);
//
//                if (status == TextToSpeech.SUCCESS){
////                 if (result == TextToSpeech.LANG_MISSING_DATA
////                       || result == TextToSpeech.LANG_NOT_SUPPORTED){
////                     Log.e("Mic","Languge is Not Supported");
////                 } else {
////                     miceImage.setEnabled(true);
////                 }
////                }else{
////                    Log.e("Mic","Initialization Failed");
////                }
//            }
//        });
////        Sensey.getInstance().init(this);

    }

    //mic speaker
    public void speak(View view) {
            mic.speak("" + words.get(i), TextToSpeech.QUEUE_FLUSH, null);
            x = words.get(i);
            i++;
            answerText.setText("");
            helpText.setText("");
            resultText.setText("");
            if (answerText.getText().toString().equalsIgnoreCase("") || resultText.getText().toString().equals("Wrong")) {
                i--;
            }
            if (i >= words.size() - 1) {
                i = 0;
                Collections.shuffle(words);
            }
    }

    //check if word wright or wrong
    public void check(View view) {
        if (answerText.getText().toString().equalsIgnoreCase(String.valueOf(x))) {
            Toast.makeText(this, "Next Sound", Toast.LENGTH_SHORT).show();
            resultText.setText("Bravo!!!");
            YoYo.with(Techniques.Shake).duration(200).repeat(2).playOn(miceImage);
//            resultText.setTextColor(0x11B81F);
            miceImage.setEnabled(true);
            i++;
        }
        else if (!answerText.getText().toString().equalsIgnoreCase(x)&&!answerText.getText().toString().equalsIgnoreCase("")) {
            YoYo.with(Techniques.Shake).duration(200).repeat(2).playOn(checkButton);
            YoYo.with(Techniques.Shake).duration(200).repeat(2).playOn(answerText);
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            resultText.setText("Wrong");
            wrongs++;
            if (wrongs==3){
                helpText.setText(x);
                wrongs=0;
            }
        }else if (answerText.getText().toString().equalsIgnoreCase(""))
            Toast.makeText(this, "Please Enter Your Answer", Toast.LENGTH_SHORT).show();
        if (i>= words.size()&&words.size()==1)
            i--;
//        miceImage.setEnabled(false);
    }

    //send mail suggestions for app
    public void mail(View view) {
        Intent mail = new Intent(Intent.ACTION_SEND);
        mail.setType("text/plain");
        mail.putExtra(Intent.EXTRA_EMAIL,"mohamedelgezery@gmail.com");
        mail.putExtra(Intent.EXTRA_SUBJECT,"Suggestions For App");
        mail.putExtra(Intent.EXTRA_TEXT,"Sent form spelling APp");
        startActivity(Intent.createChooser(mail,"Send Email"));
    }

    //enter to insert activity
    public void insert(View view) {
        Intent intent = new Intent(this,InsertActivity.class);
        startActivity(intent);
    }



    public void get(){
        week = weeksSpin.getSelectedItem().toString();
        day = daysSpin.getSelectedItem().toString();
        words = db.getWords(week,day);
    }

}

