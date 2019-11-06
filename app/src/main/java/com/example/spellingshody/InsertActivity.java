package com.example.spellingshody;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.AdapterView.*;

public class InsertActivity extends AppCompatActivity {
    EditText wordText,idText;
    Spinner daysSpin,weeksSpin;
    MyDatabase db;
    GridView dataList;
    ArrayList getdata;
    String week,day,word;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        wordText=findViewById(R.id.wordText);
        idText=findViewById(R.id.idText);
        daysSpin=findViewById(R.id.daysSpin);
        weeksSpin=findViewById(R.id.weeksSpin);
        dataList=findViewById(R.id.dataList);
        db = new MyDatabase(this);
        weeksSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                week = weeksSpin.getSelectedItem().toString();
                showData();
            }
            //
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        daysSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                day=daysSpin.getSelectedItem().toString();
                showData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//
//        showData();
    }

    public void save(View view) {
        word = wordText.getText().toString();
        day = daysSpin.getSelectedItem().toString();
        week = weeksSpin.getSelectedItem().toString();

        if (wordText.getText().toString().equals("")) {
            Toast.makeText(this, "Please Enter Your word", Toast.LENGTH_SHORT).show();
        }
            else{


            Word word1 = new Word(word, day, week);
            boolean res = db.insertWOrd(word1);
            if (res)
                Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Error Not Saved", Toast.LENGTH_SHORT).show();

            wordText.setText("");
            showData();
        }
    }
    public void showData (){
    week = weeksSpin.getSelectedItem().toString();
    day=daysSpin.getSelectedItem().toString();
    getdata=db.getAllWords(week,day);
    ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,getdata);
    dataList.setAdapter(adapter);
    }

    public void delete(View view) {
        if (idText.getText().toString().equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you Sure you Want Delete All Words")
                    .setIcon(android.R.drawable.stat_notify_error)
                    .setTitle("Delete All Words")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                         db.deleteAllWords(week, day);
                            Toast.makeText(InsertActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(InsertActivity.this, "Cancled", Toast.LENGTH_SHORT).show();
                        }
                    }).show();
        }
        else {
            id= Integer.parseInt(idText.getText().toString());
            db.deleteWord(id);
            Toast.makeText(this, "Word NO. "+id+" Deleted", Toast.LENGTH_SHORT).show();
            idText.setText("");
        }
    }
}
