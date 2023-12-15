package hanu.a2_2001040067;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import hanu.a2_2001040067.database.DBHelper;
import hanu.a2_2001040067.database.ProductManager;
import hanu.a2_2001040067.model.Product;

public class CartActivity extends AppCompatActivity {
    CartAdapter cartAdapter;
    RecyclerView rvCartProduct;
    List<Product> productList;
    TextView totalPrice;
    ProductManager productManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_main);

        productManager = new ProductManager(CartActivity.this);
        rvCartProduct = findViewById(R.id.rvCartProduct);

        productList = loadDatabase();
        rvCartProduct.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        cartAdapter = new CartAdapter(productList, CartActivity.this);
        rvCartProduct.setAdapter(cartAdapter);
        totalPrice = findViewById(R.id.totalPrice);
        totalPrice.setText(String.valueOf(getTotalPrice(productList)));

    }

    public List<Product> loadDatabase() {
        productList = new ArrayList<>();
        DBHelper dbHelper = new DBHelper(CartActivity.this);
        SQLiteDatabase sqLiteDatabase1 = dbHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase1.rawQuery("SELECT * FROM products", null);
        int thumbnailIndex = cursor.getColumnIndex("thumbnail");
        int nameIndex = cursor.getColumnIndex("name");
        int categoryIndex = cursor.getColumnIndex("category");
        int unitPriceIndex = cursor.getColumnIndex("unitPrice");

        while(cursor.moveToNext()) {
            String thumbnail = cursor.getString(thumbnailIndex);
            String name = cursor.getString(nameIndex);
            String category = cursor.getString(categoryIndex);
            int unitPrice = cursor.getInt(unitPriceIndex);
            Product product = new Product(thumbnail, name, category, unitPrice);
            productList.add(product);
        }

        cursor.close();
        sqLiteDatabase1.close();
        return productList;
    }
    public int getTotalPrice(List<Product> productList) {
        int totalPrice = 0;
        for (int i = 0; i<productList.size(); i++) {
            Product product = productList.get(i);
            int sumPrice = product.getUnitPrice() * productManager.getQuantity(product.getName(), CartActivity.this);
            totalPrice += sumPrice;
        }
        return totalPrice;
    }
}
