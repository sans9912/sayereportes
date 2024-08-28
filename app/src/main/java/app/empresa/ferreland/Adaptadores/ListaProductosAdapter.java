package app.empresa.ferreland.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import app.empresa.ferreland.R;
import app.empresa.ferreland.entidades.Productos;
import app.empresa.ferreland.ui.VerProducto;

public class ListaProductosAdapter extends RecyclerView.Adapter<ListaProductosAdapter.ProductoViewHolder> {
    Context context;
    ArrayList<Productos> listaProductos;
    ArrayList<Productos> listaOriginal;
    public ListaProductosAdapter(ArrayList<Productos> listaProductos){
        this.listaProductos=listaProductos;
        listaOriginal=new ArrayList<>();
        listaOriginal.addAll(listaProductos);
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_producto,null,false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        holder.viewnombre.setText(listaProductos.get(position).getNombre());
        holder.viewstock.setText(listaProductos.get(position).getStock()+" ");
        holder.viewprecio.setText(listaProductos.get(position).getPrecio()+" Bs.");
        String u=listaProductos.get(position).getRutaimagen();
        if(u!="" && u!=null){
            Picasso.get().load(u).into(holder.viewImage);
        }
        else{
            holder.viewImage.setImageResource(R.drawable.herramientas);
        }

    }
    public void filtrado(String txtBuscar){
        int longitud=txtBuscar.length();
        if(longitud==0){
            listaProductos.clear();
            listaProductos.addAll(listaOriginal);
        }else {
            List<Productos> collecion=listaProductos.stream().filter(i ->i.getNombre().toLowerCase().contains(txtBuscar.toLowerCase())).collect(Collectors.toList());
            listaProductos.clear();
            listaProductos.addAll(collecion);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public class ProductoViewHolder extends RecyclerView.ViewHolder {

        TextView viewnombre,viewstock,viewprecio;
        ImageView viewImage;
        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);

            viewnombre=itemView.findViewById(R.id.viewnombre);
            viewstock=itemView.findViewById(R.id.viewstock);
            viewprecio=itemView.findViewById(R.id.viewprecio);
            viewImage=itemView.findViewById(R.id.viewImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context=v.getContext();
                    Intent intent=new Intent(context, VerProducto.class);
                    intent.putExtra("ID", listaProductos.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
