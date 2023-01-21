package com.example.tech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Navegacion
    private DrawerLayout drawerMenu;
    private NavigationView navView;
    private TextView textViewUsuarioCabe;
    private View cabeceraNav;
    //Main
    private ConstraintLayout constraintLayoutMain;
    public static List<Anuncio> listaAnuncios = new ArrayList<Anuncio>();
    private RecyclerView recyclerView;
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    private com.google.android.material.floatingactionbutton.FloatingActionButton floatingActionButton;

    //PoPUP crear anuncio
    private CardView cvVentanaCrear;
    private ImageButton ibCerrarPoPup;
    private EditText etTituloCrear;
    private EditText etDescripcionCrear;
    private ImageButton ibGuardar;
    private ImageButton ibLimpiar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniciarComponentes();
        ocultarPoPup();
        myItemRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void ocultarPoPup() {
    cvVentanaCrear.setTranslationX(400f);
    cvVentanaCrear.setVisibility(View.INVISIBLE);
    constraintLayoutMain.setEnabled(true);
    drawerMenu.setEnabled(true);
    floatingActionButton.setEnabled(true);
    }
    public void mostrarCrear(){
        constraintLayoutMain.setEnabled(false);
        drawerMenu.setEnabled(false);
        floatingActionButton.setEnabled(false);
        cvVentanaCrear.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(cvVentanaCrear,"translationX",0f)
                .setDuration(300L)
                .start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        listaAnuncios.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void cargarReciclerView() {
        listaAnuncios.clear();
        cargarAnuncios();

    }





    private void cargarAnuncios() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("anuncios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String titulo = document.getString("titulo");
                                String descripcion = document.getString("descripcion");
                                Date fecha = Objects.requireNonNull(document.getTimestamp("fecha")).toDate();

                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                String formattedDate = formatter.format(fecha);
                                LocalDate localDate = LocalDate.parse(formattedDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                                String emailAnunciante = document.getString("emailUser");
                                Anuncio anuncio;
                                if(emailAnunciante != null){
                                    anuncio = new Anuncio(descripcion,localDate ,titulo, emailAnunciante );
                                }else {
                                    anuncio = new Anuncio(descripcion,localDate ,titulo, "" );
                                }

                                listaAnuncios.add(anuncio);
                                myItemRecyclerViewAdapter.notifyDataSetChanged();
                                Log.d("dsd", document.getId() + " => " + document.getData()+ anuncio.getFecha().toString());
                                Log.d("Firestore", "Anuncio cargado: " + anuncio);
                            }

                                            } else {
                                                Log.e("Firestore", "Error al cargar anuncios", task.getException());
                                            }
                                        }
                                    });

    }

    private void iniciarComponentes() {
        //PopUP Crear
        cvVentanaCrear =findViewById(R.id.cardViewLayoutCrear);
        etTituloCrear = findViewById(R.id.etTitulo);
        etDescripcionCrear = findViewById(R.id.etDescipcion);
        ibCerrarPoPup = findViewById(R.id.btCerrarCrear);
        ibLimpiar = findViewById(R.id.botonLimpiar);
        ibGuardar = findViewById(R.id.botonCrear);

        //
        constraintLayoutMain = findViewById(R.id.constraintMain);
        recyclerView = findViewById(R.id.myrecicler);
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(listaAnuncios);
        iniciarNavigation();
        iniciarDrawer();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myItemRecyclerViewAdapter);
        cargarReciclerView();
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCrear();
            }
        });
        ibCerrarPoPup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ocultarPoPup();

            }
        });
        ibLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etTituloCrear.setText("");
                etDescripcionCrear.setText("");
            }
        });
        ibGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarAnuncio();
            }
        });
    }

    private void guardarAnuncio() {
        LocalDate hoy = LocalDate.now();
        Instant instant = hoy.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date fecha = Date.from(instant);

        String descripcion = etDescripcionCrear.getText().toString();
        String titulo = etTituloCrear.getText().toString();

        if(!descripcion.isEmpty() && !titulo.isEmpty()){

            Map<String, Object> anuncio = new HashMap<>();
            anuncio.put("descripcion", descripcion);
            anuncio.put("fecha", new Timestamp(fecha));
            anuncio.put("titulo",titulo);
            anuncio.put("emailUser", LoginActivity.email);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
             db.collection("anuncios")
                    .add(anuncio)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            anuncio.put("idReferencia",documentReference.getId());
                            Log.d("TAG", "DocumentSnapshot written with ID: " + documentReference.getId());
                            Toast.makeText(MainActivity.this, R.string.anuncioGuardado , Toast.LENGTH_SHORT).show();
                            ocultarPoPup();
                            myItemRecyclerViewAdapter.notifyDataSetChanged();
                            restartActivity();
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error adding document", e);
                        }
                    });

        }else{
            Toast.makeText(this, R.string.errorGuardar, Toast.LENGTH_LONG).show();
        }

    }


    private void iniciarNavigation() {

        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        cabeceraNav = LayoutInflater.from(this).inflate(R.layout.nav_menu_cabecera,navView,false);
        navView.removeHeaderView(cabeceraNav);
        navView.addHeaderView(cabeceraNav);
        textViewUsuarioCabe = cabeceraNav.findViewById(R.id.nav_cabecera_user);

        //Si tenemos nombre introducimos el nombre en la siguiente pantalla sino el email.
        if(LoginActivity.displayName != null) {
            textViewUsuarioCabe.setText(LoginActivity.displayName);

        }else{
            textViewUsuarioCabe.setText(LoginActivity.email);
        }
    }

    private void iniciarDrawer() {
        drawerMenu = findViewById(R.id.drawer_main);
        ActionBarDrawerToggle despleglable = new ActionBarDrawerToggle(this, drawerMenu, R.string.apertura, R.string.cierre);
        drawerMenu.addDrawerListener(despleglable);
        despleglable.syncState();

    }
    private void cerrarSesion(){
        LoginActivity.email = "";
        LoginActivity.displayName = "";
        textViewUsuarioCabe.setText("");
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
  
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_usuario: cerrarSesion();
            break;
            case R.id.nav_item_main:
                restartActivity();
                break;
        }
        drawerMenu.closeDrawer(GravityCompat.START);
        return true;
    }

    private void restartActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}