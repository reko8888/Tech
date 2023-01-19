package com.example.tech;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

 private static final String TAG = "IMPORTANTE";
    private static final int RC_SIGN_IN = 100;

 private EditText editTextEmail;
 private EditText editTextPassword;
 private TextView textViewOlvidoPw;
 private Button btnIniciarSesion;
 private Button btnIniciarSesionGoogle;
 private CheckBox checkBoxterminosCondiciones;
 private String providerSession;
 private FirebaseAuth mAuth;
 public static String email;
 public static  String displayName;
 private static String password;
 private LinearLayout linearLayoutTerminos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inicializarComponentes();
        montarComponentes();
    }


    private void initialSessionGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    private void iniciarSesion() {
         email = editTextEmail.getText().toString();
         password = editTextPassword.getText().toString();
         if(!email.isEmpty() && !password.isEmpty()){

             mAuth.signInWithEmailAndPassword(email,password)
                     .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if(task.isSuccessful()){
                                 entrarActitivity(email,providerSession);
                             }else {
                                 if(linearLayoutTerminos.getVisibility() == android.view.View.INVISIBLE){
                                     linearLayoutTerminos.setVisibility(android.view.View.VISIBLE);
                                 }else {
                                     if(checkBoxterminosCondiciones.isChecked()){
                                         registrarUsuario();
                                     }
                                 }

                             }
                         }
                     });



         }



    }

    private void entrarActitivity(String email, String provider) {
         this.email = email;
         providerSession = provider;

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void registrarUsuario() {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();

                            guardarCorreoBD("email",email);


                            entrarActitivity(email,"email");


                        } else {
                            Toast.makeText(LoginActivity.this, R.string.contraseñaIncorrecta, Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: REGISTRO FALLIDO ");
                        }
                    }
                });
    }

    private void guardarCorreoBD(String columna, String dato) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> userBaseDatos = new HashMap<>();
        userBaseDatos.put(columna, dato);
        db.collection("users").document(dato).set(userBaseDatos);
    }

   private void resetearContraseña(){
        String emailOlvido = editTextEmail.getText().toString();
        if(!emailOlvido.isEmpty()){
            mAuth.sendPasswordResetEmail(emailOlvido)
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, R.string.msjCorrectoEnvio, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(LoginActivity.this, R.string.msjErrorEnvío, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
    /*
    Inicializo los métodos
     */
    private void inicializarComponentes(){
        mAuth = FirebaseAuth.getInstance();
        textViewOlvidoPw = findViewById(R.id.textViewOlvidoContraseña);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnIniciarSesion = findViewById(R.id.btnViewIniciarSesion);
        btnIniciarSesionGoogle = findViewById(R.id.buttonGoogle);
        checkBoxterminosCondiciones = findViewById(R.id.checkboxTerminos);
        linearLayoutTerminos = findViewById(R.id.linearTerminos);
        linearLayoutTerminos.setVisibility(android.view.View.INVISIBLE);
    }
    /*
    Metodo dónde desarrollo los comportamientos de los componentes
     */
    private void montarComponentes(){

        btnIniciarSesion.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                iniciarSesion();
            }
        });

        btnIniciarSesionGoogle.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                initialSessionGoogle();
            }
        });
        textViewOlvidoPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetearContraseña();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if(account != null){
                displayName = account.getDisplayName();
                email = account.getEmail();
                AuthCredential credenciales = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credenciales)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                entrarActitivity(email,"Google");
                                guardarCorreoBD("correo",email);
                                guardarCorreoBD("nombre",displayName);
                                } else {

                                    Log.w(TAG, "Ha fallado la autentificación", task.getException());

                                }
                            }
                        });




            }


        } catch (ApiException e) {

            Log.w(TAG, "Ha fallado la conexion error= " + e.getStatusCode());

        }
    }
}