package com.SandY.stomanage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.SandY.stomanage.Administrator.AdministratorMainMenu;
import com.SandY.stomanage.HeadWarehouseTeam.HeadWarehouseTeamMainMenu;
import com.SandY.stomanage.dataObject.UserObj;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Login extends AppCompatActivity {

    EditText email, password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty()){
                    email.setError(getResources().getString(R.string.email_error));
                    email.requestFocus();
                    return;
                }
                if(password.getText().toString().isEmpty()){
                    password.setError(getResources().getString(R.string.password_error));
                    password.requestFocus();
                    return;
                }
                //TODO set error to less then 6 char password
                FirebaseAuth _auth = FirebaseAuth.getInstance();
                _auth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        password.setText("");
                        if(task.isSuccessful()){
                            String uid = task.getResult().getUser().getUid();
                            DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    UserObj user = snapshot.getValue(UserObj.class);
                                    if (user.getUserPerm().equals(GlobalConstants.Perm.מדריך.toString())){
                                        //TODO
                                        return;
                                    }
                                    if (user.getUserPerm().equals(GlobalConstants.Perm.מחסנאי.toString())){
                                        //TODO
                                        return;
                                    }
                                    if (user.getUserPerm().equals(GlobalConstants.Perm.מחסנאי_ראשי.toString())){
                                        Intent intent = new Intent(Login.this, HeadWarehouseTeamMainMenu.class);
                                        startActivity(intent);
                                        return;
                                    }
                                    if (user.getUserPerm().equals(GlobalConstants.Perm.מנהל.toString())){
                                        Intent intent = new Intent(Login.this, AdministratorMainMenu.class);
                                        startActivity(intent);
                                        return;
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    //TODO set error to DatabaseError
                                }
                            });
                        }
                        else{
                            //TODO set error to fail login
                        }
                    }
                });
            }
        });
    }
}