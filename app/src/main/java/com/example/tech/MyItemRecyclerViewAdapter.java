package com.example.tech;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tech.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.tech.databinding.FragmentItemBinding;

import java.time.format.DateTimeFormatter;
import java.util.List;

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

    public interface OnButtonClickListener {
        void onButtonClick(int position, String descripcion);
    }
    private OnButtonClickListener listener;

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.listener = listener;
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
               if (listener != null) {
                   listener.onButtonClick(position, descripcion);
               }

           }
       });
        holder.mBtnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                Intent chooser = null;
                i.setAction(Intent.ACTION_SEND);
                i.setData(Uri.parse("mailto: "));
                String[] para = {emailAnunciante};

                i.putExtra(Intent.EXTRA_EMAIL, para);
                i.putExtra(Intent.EXTRA_SUBJECT, "Referencia al anuncio: "+ mValues.get(position).getTitulo() );

                chooser = i.createChooser(i,"enviar email");
                v.getContext().startActivity(i);

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