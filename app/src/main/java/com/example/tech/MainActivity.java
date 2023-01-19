package com.example.tech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import org.checkerframework.checker.units.qual.A;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import io.grpc.InternalMetadata;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerMenu;
    private NavigationView navView;
    private TextView textViewUsuarioCabe;
    public static List<Anuncio> listaAnuncios = new ArrayList<Anuncio>();
    private View cabeceraNav;
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciarComponentes();
        montarComponentes();
        cargarReciclerView();
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(listaAnuncios);
        myItemRecyclerViewAdapter.notifyDataSetChanged();
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
        cargarDB();

    }

    private void montarComponentes() {



    }

    private void cargarDB() {
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
                                Anuncio anuncio = new Anuncio(descripcion, fecha,titulo );
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

        iniciarDrawer();
        iniciarNavigation();
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
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;
        }
        drawerMenu.closeDrawer(GravityCompat.START);
        return true;
    }
}