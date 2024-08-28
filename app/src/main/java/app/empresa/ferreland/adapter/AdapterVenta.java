package app.empresa.ferreland.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.empresa.ferreland.R;
import app.empresa.ferreland.retrofit.response.ResponseVenta;

import java.util.List;

public class AdapterVenta extends RecyclerView.Adapter<AdapterVenta.ViewHolder> {

    public final Context mContext;
    private final List<ResponseVenta> mValues;
//    public List<ResponseUsuario> exampleListFull;

    public AdapterVenta(Context mContext, List<ResponseVenta> mValues) {
        this.mContext = mContext;
        this.mValues = mValues;
//        exampleListFull = new ArrayList<>(mValues);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venta, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mValues != null) {
            ResponseVenta data = mValues.get(position);

            holder.tvIdTransaccionVenta.setText(data.getIdTransaccion());
            holder.tvIdTransaccionVenta.setTextColor(mContext.getResources().getColor(R.color.purple));
            holder.tvMontoTotalVenta.setText("Bs. " + data.getMontoTotal().toString());
            holder.tvMontoTotalVenta.setTextColor(mContext.getResources().getColor(R.color.green));
            holder.tvFechaVentaVenta.setText(data.getFechaVenta());

            if (data.getConfirmarReserva() != null) {
                if (data.getConfirmarReserva()) {
                    holder.tvConfirmarReservaVenta.setText(R.string.activo);
                    holder.tvConfirmarReservaVenta.setTextColor(mContext.getResources().getColor(R.color.green));
                } else {
                    holder.tvConfirmarReservaVenta.setText(R.string.inactivo);
                    holder.tvConfirmarReservaVenta.setTextColor(mContext.getResources().getColor(R.color.yellow));
                }
            } else {
                holder.tvConfirmarReservaVenta.setText("null");
            }

        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdTransaccionVenta, tvMontoTotalVenta, tvFechaVentaVenta, tvConfirmarReservaVenta;

        public RelativeLayout rlF, rlB;

        public ViewHolder(View itemview) {
            super(itemview);
            tvIdTransaccionVenta = itemview.findViewById(R.id.tvIdTransaccionVenta);
            tvMontoTotalVenta = itemview.findViewById(R.id.tvMontoTotalVenta);
            tvFechaVentaVenta = itemview.findViewById(R.id.tvFechaVentaVenta);
            tvConfirmarReservaVenta = itemview.findViewById(R.id.tvConfirmarReservaVenta);
        }
    }
}
