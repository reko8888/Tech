package com.example.tech;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tech.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.tech.databinding.FragmentItemBinding;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Anuncio> mValues;


    public MyItemRecyclerViewAdapter(List<Anuncio> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mDescripcion.setText(mValues.get(position).getDescripcion());
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = mValues.get(position).getFecha().format(formateador);
        holder.mFecha.setText(formattedDate);
        holder.mTitulo.setText(mValues.get(position).getTitulo());

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

        public Anuncio mItem;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mDescripcion = binding.textDescripcion;
            mFecha = binding.textFecha;
            mTitulo = binding.textTitulo;
            mBtnImg = binding.botonEnviar;
        }

        @Override
        public String toString() {
            return super.toString() + " '" ;
        }
    }
}