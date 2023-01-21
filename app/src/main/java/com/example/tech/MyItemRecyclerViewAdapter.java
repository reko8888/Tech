package com.example.tech;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tech.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.tech.databinding.FragmentItemBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Anuncio> mValues;

;

    public MyItemRecyclerViewAdapter(List<Anuncio> items) {
        mValues = items;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String descripcion = mValues.get(position).getDescripcion();
        holder.mItem = mValues.get(position);
        holder.mDescripcion.setText(descripcion);
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = mValues.get(position).getFecha().format(formateador);
        holder.mFecha.setText(formattedDate);
        holder.mTitulo.setText(mValues.get(position).getTitulo());
        String emailAnunciante = mValues.get(position).getEmailAnuncio();

        if(emailAnunciante != null && emailAnunciante.equals(LoginActivity.email)){
            holder.mBtnImgCerrar.setVisibility(View.VISIBLE);
            holder.mBtnImgCerrar.setEnabled(true);
        }else{
            holder.mBtnImgCerrar.setVisibility(View.INVISIBLE);
            holder.mBtnImgCerrar.setEnabled(false);
        }
       holder.mBtnImgCerrar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               FirebaseFirestore db = FirebaseFirestore.getInstance();
               // Creo la referencia de la coleccion que quiero borrar
               CollectionReference collectionReference = db.collection("anuncios");

                // Crear una consulta para tener el documento que cumpla con el criterio
               Query query = collectionReference.whereEqualTo("descripcion", descripcion);

                // Ejecuto la consulta
               query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {

                           for (QueryDocumentSnapshot document : task.getResult()) {

                               document.getReference().delete();

                           }
                       } else {
                           Log.w("TAG", "Error al obtener documentos", task.getException());
                       }
                       Intent i = new Intent(v.getContext(), MainActivity.class);
                       v.getContext().startActivity(i);
                   }
               });


           }
       });
        holder.mBtnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mDescripcion;
        public final TextView mFecha;
        public final TextView mTitulo;
        public final ImageButton mBtnImg;
        public final ImageButton mBtnImgCerrar;

        public Anuncio mItem;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mDescripcion = binding.textDescripcion;
            mFecha = binding.textFecha;
            mTitulo = binding.textTitulo;
            mBtnImg = binding.botonEnviar;
            mBtnImgCerrar = binding.imagbtCerrarAnuncio;
        }

        @Override
        public String toString() {
            return super.toString() + " '" ;
        }
    }
}