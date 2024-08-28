package app.empresa.ferreland.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import app.empresa.ferreland.R;

import com.google.android.material.imageview.ShapeableImageView;

import app.empresa.ferreland.retrofit.response.ResponseUsuario;

import java.util.List;

public class AdapterUsuario extends RecyclerView.Adapter<AdapterUsuario.ViewHolder> implements View.OnClickListener {

    public final Context mContext;
    private final List<ResponseUsuario> mValues;
    public AdapterUsuario(Context mContext, List<ResponseUsuario> mValues) {
        this.mContext = mContext;
        this.mValues = mValues;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mValues != null) {
            ResponseUsuario data = mValues.get(position);
            Glide.with(mContext).load(R.drawable.icon_usuario).into(holder.imgUsuario);
            holder.tvNombreUsuario.setText(data.getNombres());
            holder.tvCorreoUsuario.setText(data.getCorreo());
            holder.tvRolUsuario.setText(data.getRol() != null ? data.getRol().getNombre() : "null");

            holder.tvRolUsuario.setTextColor(mContext.getResources().getColor(R.color.blue));
            if (data.getActivo() != null) {
                if (data.getActivo()) {
                    holder.tvActivoUsuario.setText(R.string.activo);
                    holder.tvActivoUsuario.setTextColor(mContext.getResources().getColor(R.color.green));
                } else {
                    holder.tvActivoUsuario.setText(R.string.inactivo);
                    holder.tvActivoUsuario.setTextColor(mContext.getResources().getColor(R.color.red));
                }
            } else {
                holder.tvActivoUsuario.setText("null");
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

    @Override
    public void onClick(View v) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ShapeableImageView imgUsuario;
        public TextView tvNombreUsuario, tvCorreoUsuario, tvActivoUsuario, tvRolUsuario;

        ViewHolder(View itemview) {
            super(itemview);
            imgUsuario = itemview.findViewById(R.id.imgUsuario);
            tvNombreUsuario = itemview.findViewById(R.id.tvNombreUsuario);
            tvCorreoUsuario = itemview.findViewById(R.id.tvCorreoUsuario);
            tvActivoUsuario = itemview.findViewById(R.id.tvActivoUsuario);
            tvRolUsuario = itemview.findViewById(R.id.tvRoleUsuario);


//            rlF = itemview.findViewById(R.id.rlCategory);
//            rlB = itemview.findViewById(R.id.rl_background_category);
        }
    }

}
