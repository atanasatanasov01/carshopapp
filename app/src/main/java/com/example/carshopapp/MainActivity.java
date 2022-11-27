package com.example.carshopapp;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends DBActivity {
    protected EditText editPartName, editPartNumber, editQuantity;
    protected Button btnInsert;
    protected ListView listView;

    protected void FillListView() throws Exception{
        final ArrayList<String> listResults
                = new ArrayList<>();
        SelectSQL(
                "SELECT * FROM CARPARTS " +
                        "ORDER BY ID ",
                null,
                (ID, PartName, PartNumber, Quantity)->
                        listResults.add(ID+"\t"+PartName+"\t"+PartNumber+"\t"+Quantity+"\n")

        );
        listView.clearChoices();
        ArrayAdapter<String> arrayAdapter=
                new ArrayAdapter<>(
                        getApplicationContext(),
                        R.layout.activity_list_view,
                        R.id.textView,
                        listResults
                );
        listView.setAdapter(arrayAdapter);

    }
    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        try {
            FillListView();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editPartName = findViewById(R.id.editPartName);
        editPartNumber = findViewById(R.id.editPartNumber);
        editQuantity = findViewById(R.id.editQuantity);
        btnInsert = findViewById(R.id.btnInsert);
        listView = findViewById(R.id.listView);

        try {
            InitDB();
            FillListView();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("",e.getLocalizedMessage().toString());
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected="";
                TextView clickedText = view.findViewById(R.id.textView);
                selected=clickedText.getText().toString();
                String [] elements = selected.split("\t");
                elements[3]=elements[3].trim();
                Intent intent= new Intent(MainActivity.this, UpdateDelete.class);
                Bundle b= new Bundle();
                b.putString("ID", elements[0]);
                b.putString("PartName", elements[1]);
                b.putString("PartNumber", elements[2]);
                b.putString("Quantity", elements[3]);
                intent.putExtras(b);
                startActivityForResult(intent, 200, b);



            }
        });

        btnInsert.setOnClickListener(view->{
            try{
                ExecSQL(
                        "INSERT INTO CARPARTS(PartName, PartNumber, Quantity ) " +
                                "VALUES(?, ?, ?) ",
                        new Object[]{
                                editPartName.getText().toString(),
                                editPartNumber.getText().toString(),
                                editQuantity.getText().toString()
                        },
                        ()->Toast.makeText(getApplicationContext(), "Insert was successful",
                                Toast.LENGTH_LONG).show()
                );
                FillListView();

            }catch (Exception e){
                e.printStackTrace();
                Log.d("",e.getLocalizedMessage().toString());
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }


        });


    }
}