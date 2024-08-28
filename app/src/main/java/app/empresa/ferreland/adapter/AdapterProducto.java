package app.empresa.ferreland.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import app.empresa.ferreland.retrofit.response.ResponseProducto;

import app.empresa.ferreland.R;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class AdapterProducto extends RecyclerView.Adapter<AdapterProducto.ViewHolder> {
    private final Context mContext;
    private final List<ResponseProducto> mValues;

    public AdapterProducto(Context mContext, List<ResponseProducto> mValues) {
        this.mContext = mContext;
        this.mValues = mValues;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mValues != null) {
            ResponseProducto data = mValues.get(position);

            holder.tvNombreProducto.setText(data.getNombre());
            holder.tvPrecioArticulo.setText("Bs. " + data.getPrecio().toString());
            holder.tvCodigoArticulo.setText(data.getCDigo());

            if (data.getRutaImagen().isEmpty()) {
                Glide.with(mContext).load(R.drawable.icon_no_image_article).into(holder.imgProducto);
            } else {
                Glide.with(mContext)
                        .load(data.getRutaImagen())
                        .placeholder(R.drawable.loading)
                        .centerCrop()
                        .into(holder.imgProducto);
            }

            if (data.getActivo()) {
                holder.tvActivoArticulo.setText(R.string.activo);
                holder.tvActivoArticulo.setTextColor(mContext.getResources().getColor(R.color.green));
            } else {
                holder.tvActivoArticulo.setText(R.string.inactivo);
                holder.tvActivoArticulo.setTextColor(mContext.getResources().getColor(R.color.yellow));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mValues == null) {
            return 0;
        } else {
            return mValues.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ShapeableImageView imgProducto;
        public TextView tvNombreProducto, tvActivoArticulo, tvPrecioArticulo, tvCodigoArticulo;

        public ViewHolder(View itemview) {
            super(itemview);
            imgProducto = itemview.findViewById(R.id.imgProducto);
            tvNombreProducto = itemview.findViewById(R.id.tvNombreProducto);
            tvPrecioArticulo = itemview.findViewById(R.id.tvPrecioProducto);
            tvActivoArticulo = itemview.findViewById(R.id.tvActivoProducto);
            tvCodigoArticulo = itemview.findViewById(R.id.tvCodigoProducto);
        }
    }
}