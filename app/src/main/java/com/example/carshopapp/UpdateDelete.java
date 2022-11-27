package com.example.carshopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateDelete extends DBActivity {

    protected EditText editPartName, editPartNumber, editQuantity;
    protected Button btnUpdate, btnDelete;
    protected String ID;

    protected void BackToMain(){
        finishActivity(200);
        Intent i = new Intent(UpdateDelete.this,
                MainActivity.class);
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        editPartName=findViewById(R.id.editPartName);
        editPartNumber=findViewById(R.id.editPartNumber);
        editQuantity=findViewById(R.id.editQuantity);
        btnUpdate=findViewById(R.id.btnUpdate);
        btnDelete=findViewById(R.id.btnDelete);
        Bundle b = getIntent().getExtras();
        if(b!=null){
            ID=b.getString("ID");
            editPartName.setText(b.getString("PartName"));
            editPartNumber.setText(b.getString("PartNumber"));
            editQuantity.setText(b.getString("Quantity"));
        }

        btnDelete.setOnClickListener(view->{
            try{
                ExecSQL(
                        "DELETE FROM CARPARTS " +
                                "WHERE ID = ?",
                        new Object[]{ID},
                        ()->Toast.makeText(getApplicationContext(),
                                "Deletion was successful",
                                Toast.LENGTH_LONG
                        ).show()

                );
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                                e.getLocalizedMessage(), Toast.LENGTH_LONG)
                        .show();
            }finally {
                BackToMain();
            }

        });

        btnUpdate.setOnClickListener(view->{
            try{
                ExecSQL(
                        "UPDATE CARPARTS SET " +
                                "PartName = ?, " +
                                "PartNumber = ?, " +
                                "Quantity = ? " +
                                "WHERE ID = ? ",
                        new Object[]{
                                editPartName.getText().toString(),
                                editPartNumber.getText().toString(),
                                editQuantity.getText().toString(),
                                ID
                        },
                        ()->Toast.makeText(getApplicationContext(),
                                "Update was successful",
                                Toast.LENGTH_LONG
                        ).show()

                );
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                                e.getLocalizedMessage(), Toast.LENGTH_LONG)
                        .show();
            }finally {
                BackToMain();
            }

        });




    }
}
