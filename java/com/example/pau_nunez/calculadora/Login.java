package com.example.pau_nunez.calculadora;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pau_nunez.calculadora.DataBase.DBHelper;
import com.example.pau_nunez.calculadora.DataBase.DataBaseLogin;

public class Login extends AppCompatActivity {

    DBHelper dbh;

    EditText textUsername;
    EditText textPassword;

    Button bCreate;
    Button bLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbh = new DBHelper(getApplicationContext());
        textUsername = (EditText) findViewById(R.id.textUsername);
        textPassword = (EditText) findViewById(R.id.textPassword);
        bCreate = (Button) findViewById(R.id.bCreate);
        bLogin = (Button) findViewById(R.id.bLogin);

        /* Botó CREATE ACCOUNT */
        bCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!textUsername.getText().toString().equals("")) {

                    if (dbh.existsUser(textUsername.getText().toString())) {
                        Toast.makeText(getApplicationContext(),"This user already exists!", Toast.LENGTH_LONG).show();
                    }

                    else {
                        dbh.insertUser(textUsername.getText().toString(),textPassword.getText().toString());
                        Toast.makeText(getApplicationContext(),"User created successfully!",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        /* Botó LOGIN */
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!textUsername.getText().toString().equals("")) {

                    if (dbh.correctUserAndPassword(textUsername.getText().toString(),textPassword.getText().toString())) {
                        Intent i = new Intent(getApplicationContext(),MainMenu.class);
                        i.putExtra("Username",textUsername.getText().toString());
                        startActivity(i);
                    }

                    else {
                        Toast.makeText(getApplicationContext(),"Incorrect Username/Password or user does not exist", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
}
