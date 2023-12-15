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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import hanu.a2_2001040067.database.ProductManager;
import hanu.a2_2001040067.model.Product;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    CartActivity cartActivity;
    Context context;
    List<Product> productList;
    ProductManager productManager;

    public CartAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_cart, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        productManager = new ProductManager(context);
        Product product = productList.get(position);
        Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
        executor.execute(() -> {
            Bitmap bitmap = loadImg(product.getThumbnail());
            if (bitmap != null) {
                handler.post(() -> holder.thumbnail.setImageBitmap(bitmap));
            }
        });
        holder.name.setText(product.getName());
        holder.unitPrice.setText(String.valueOf(product.getUnitPrice()));
        holder.quantity.setText(String.valueOf(productManager.getQuantity(product.getName(), context)));
        int sumPrice = product.getUnitPrice() * productManager.getQuantity(product.getName(), context);
        holder.sumPrice.setText(String.valueOf(sumPrice));

//        int totalPrice = productManager.getTotalPrice(product.getName(), context);
//        holder.totalPrice.setText(String.valueOf(totalPrice));

        holder.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productManager.plusProduct(product.getName(), context);
                int sumPrice = product.getUnitPrice() * productManager.getQuantity(product.getName(), context);
                holder.sumPrice.setText(String.valueOf(sumPrice));
                holder.quantity.setText(String.valueOf(productManager.getQuantity(product.getName(), context)));

//                int totalPrice = cartActivity.getTotalPrice(productList);
//                holder.totalPrice.setText(String.valueOf(totalPrice));

            }
        });

        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productManager.minusProduct(product.getName(), context);
                int sumPrice = product.getUnitPrice() * productManager.getQuantity(product.getName(), context);
                holder.sumPrice.setText(String.valueOf(sumPrice));
                holder.quantity.setText(String.valueOf(productManager.getQuantity(product.getName(), context)));

//                int totalPrice = cartActivity.getTotalPrice(productList);
//                holder.totalPrice.setText(String.valueOf(totalPrice));

                if (productManager.getQuantity(product.getName(), context) == 0) {
                    remove(product.getName());
                    productManager.removeProduct(product, context);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView name, unitPrice, quantity, sumPrice, totalPrice;
       ImageButton plusBtn, minusBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plusBtn = itemView.findViewById(R.id.plusBtn);
            minusBtn = itemView.findViewById(R.id.minusBtn);
            thumbnail = itemView.findViewById(R.id.cart_product_image);
            name = itemView.findViewById(R.id.cart_product_name);
            unitPrice = itemView.findViewById(R.id.cart_product_price);
            quantity = itemView.findViewById(R.id.quantity);
            sumPrice = itemView.findViewById(R.id.sumPrice);
            totalPrice = itemView.findViewById(R.id.totalPrice);
        }
    }


    @Override
    public int getItemCount() {
        return productList.size();

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

    @SuppressLint("NotifyDataSetChanged")
    public void remove(String name) {
        productList.removeIf(product -> product.getName().equals(name));
        notifyDataSetChanged();
    }
}
