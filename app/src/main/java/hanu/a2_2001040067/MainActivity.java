package hanu.a2_2001040067;

import static hanu.a2_2001040067.Constants.executor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import hanu.a2_2001040067.database.DBHelper;
import hanu.a2_2001040067.model.Product;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageButton imbCart;
    List<Product> productList;
    ProductAdapter productAdapter;

    EditText editTextSearch;
    ImageButton imbSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imbSearch = findViewById(R.id.imbSearch);
        editTextSearch = findViewById(R.id.editTextSearch);

        recyclerView = findViewById(R.id.rvProduct);
        imbCart = findViewById(R.id.imbCart);
        productList = new ArrayList<>();

        Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
        executor.execute(() -> {
            String url = "https://hanu-congnv.github.io/mpr-cart-api/products.json";
            String json =  loadJSon(url);
            handler.post(() -> {
               if (json == null) {
                   Toast.makeText(MainActivity.this, "Failed to load json", Toast.LENGTH_SHORT).show();
               } try {
                   JSONArray jsonArray = new JSONArray(json);
                   for (int i = 0; i<jsonArray.length(); i++) {
                       JSONObject product = jsonArray.getJSONObject(i);
                       Product root = new Product();
//                       root.setId(product.getInt("id"));
                       root.setThumbnail(product.getString("thumbnail"));
                       root.setName(product.getString("name"));
                       root.setCategory(product.getString("category"));
                       root.setUnitPrice(product.getInt("unitPrice"));
                       productList.add(root);
                   }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
               productAdapter = new ProductAdapter(productList,MainActivity.this);
               recyclerView.setAdapter(productAdapter);

               productAdapter.filter("");
               imbSearch.setOnClickListener(view -> {
                    String query = editTextSearch.getText().toString();
                    productAdapter.filter(query);
                });
            });
        });

        //navigate to cart activity
        imbCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }

    public String loadJSon(String link){

        try {
            // connect to url
            URL url = new URL(link);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.connect();

            // read json data
            Scanner sc = new Scanner(connection.getInputStream());
            StringBuilder json = new StringBuilder();
            String line;
            while (sc.hasNextLine()){
                line=  sc.nextLine();
                json.append(line);
            }
            return json.toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //    public Bitmap loadImg(String link){
//        try {
//            URL url = new URL(link);
//            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//
//            Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
//            return bitmap;
//
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

}