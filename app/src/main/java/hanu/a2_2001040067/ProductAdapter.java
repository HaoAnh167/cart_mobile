
package hanu.a2_2001040067;



import static hanu.a2_2001040067.Constants.executor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import hanu.a2_2001040067.database.ProductManager;
import hanu.a2_2001040067.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    Context context;
    List<Product> productList;
    List<Product> rsList;

    ProductManager productManager;

    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.rsList = new ArrayList<>();
        this.context = context;
    }


    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_main, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        productManager = new ProductManager(context);
        Product product = rsList.get(position);
//        Product rsProduct = rsList.get(position);
        Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
        executor.execute(() -> {
            Bitmap bitmap = loadImg(productList.get(position).getThumbnail());
            if (bitmap != null) {
                handler.post(() -> holder.thumbnail.setImageBitmap(bitmap));
            }
        });
        holder.name.setText(product.getName());
        holder.unitPrice.setText(String.valueOf(product.getUnitPrice()));
        holder.imbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Added successfully.", Toast.LENGTH_LONG).show();
                Product newProduct = new Product();
//                newProduct.setId(product.getId());
                newProduct.setThumbnail(product.getThumbnail());
                newProduct.setName(product.getName());
                newProduct.setCategory(product.getCategory());
                newProduct.setUnitPrice(product.getUnitPrice());
                if (productManager.checkIfProductExists(newProduct.getName(), context) == true) {
                    productManager.plusProduct(newProduct.getName(), context);
                } else {
                    productManager.saveProduct(newProduct, context);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView name, unitPrice;
        ImageButton imbAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imbAdd = itemView.findViewById(R.id.imbAdd);
            thumbnail = itemView.findViewById(R.id.product_image);
            name = itemView.findViewById(R.id.product_name);
            unitPrice = itemView.findViewById(R.id.product_price);
        }
    }

    @Override
    public int getItemCount() {
        return rsList.size();
    }

    public void removeItem(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
    }

    public Bitmap loadImg(String link){
        try {
            URL url = new URL(link);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            return bitmap;

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence charSequence) {
//                String stringSearch = charSequence.toString();
//                if (stringSearch.isEmpty()) {
//                    productList = productListOld;
//                } else {
//                    List<Product> list = new ArrayList<>();
//                    for (Product product : productListOld) {
//                        if (product.getName().toLowerCase().contains(stringSearch.toLowerCase())) {
//                            list.add(product);
//                        }
//                    }
//                    productList = list;
//                }
//
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = productList;
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                productList = (List<Product>) filterResults.values;
//                notifyDataSetChanged();
//            }
//        };
//    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String query) {
        rsList.clear();
        if (query.isEmpty()) {
            rsList.addAll(productList);
        } else {
            query = query.toLowerCase(Locale.getDefault());
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    rsList.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }
}
