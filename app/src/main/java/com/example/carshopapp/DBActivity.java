package com.example.carshopapp;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Telephony;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public abstract class DBActivity extends AppCompatActivity {
    protected void ExecSQL(String SQL, Object[] args, OnQuerySuccess success)
            throws Exception {
        SQLiteDatabase db;
        db = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath() + "/carshop.db",
                null
        );
        Toast.makeText(getApplicationContext(), getFilesDir().getPath() + "/carshop.db",
                Toast.LENGTH_LONG).show();
        Log.d("DIRLOCATION", getFilesDir().getPath() + "/carshop.db");
        if (args == null) {
            db.execSQL(SQL);
        } else {
            db.execSQL(SQL, args);
        }
        db.close();
        if (success != null)
            success.OnSuccess();
    }

    protected void InitDB() throws Exception {
        ExecSQL(
                "CREATE TABLE if not exists CARPARTS( " +
                        "ID integer PRIMARY KEY AUTOINCREMENT, " +
                        "PartName text not null, " +
                        "PartNumber text not null, " +
                        "Quantity text not null, " +
                        "unique(PartNumber) " +
                        "); ",
                null,
                () -> Toast.makeText(getApplicationContext(),
                        "Table has been created successfuly",
                        Toast.LENGTH_LONG
                ).show()
        );



    }
    protected void SelectSQL(String SelectQ, String[] args, OnSelectSuccess
            success)
            throws Exception
    {
        SQLiteDatabase db;
        db = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath() + "/carshop.db",
                null
        );
        Toast.makeText(getApplicationContext(), getFilesDir().getPath() + "/carshop.db",
                Toast.LENGTH_LONG).show();
        Log.d("DIRLOCATION", getFilesDir().getPath() + "/carshop.db");
        Cursor cursor = db.rawQuery(SelectQ, args);
        while (cursor.moveToNext()){
            @SuppressLint("Range")
            String ID= cursor.getString(cursor.getColumnIndex("ID"));
            @SuppressLint("Range")
            String PartName = cursor.getString(cursor.getColumnIndex("PartName"));
            @SuppressLint("Range")
            String PartNumber = cursor.getString(cursor.getColumnIndex("PartNumber"));
            @SuppressLint("Range")
            String Quantity = cursor.getString(cursor.getColumnIndex("Quantity"));
            success.OnElementSelected(ID, PartName, PartNumber, Quantity);
        }
        db.close();
    }

    protected interface OnQuerySuccess {
        public void OnSuccess();
    }
    protected interface OnSelectSuccess{
        public void
        OnElementSelected(String ID, String PartName, String PartNumber, String Quantity);

    }

}

