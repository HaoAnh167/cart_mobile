package hanu.a2_2001040067.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import hanu.a2_2001040067.model.Product;
public class ProductManager {
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;
    private DBHelper dbHelper;

    public ProductManager(Context context) {
        this.dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public int getQuantity(String name, Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase1 = dbHelper.getReadableDatabase();


        cursor = sqLiteDatabase.rawQuery("SELECT quantity FROM products WHERE name ='" + name + "'", null);
        int quantityIndex = cursor.getColumnIndex("quantity");
        int quantity = 0;
        if (cursor != null && cursor.moveToFirst()) {
            quantity = Integer.parseInt(cursor.getString(quantityIndex));
        }
        cursor.close();
        sqLiteDatabase1.close();

        return quantity;
    }

    public boolean saveProduct(Product product, Context context) {

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase16 = dbHelper.getWritableDatabase();

        String thumbnaill = product.getThumbnail();
        String namee = product.getName();
        String categoryy = product.getCategory();
        int unitPricee = product.getUnitPrice();
        int sumPrice = unitPricee;

        String query = "INSERT INTO products (thumbnail, name, category, unitPrice, quantity, sumPrice) VALUES (?, ?, ?, ?, ?, ?)";
        SQLiteStatement sqLiteStatement16 = sqLiteDatabase16.compileStatement(query);
        sqLiteStatement16.bindString(1, thumbnaill);
        sqLiteStatement16.bindString(2, namee);
        sqLiteStatement16.bindString(3, categoryy);
        sqLiteStatement16.bindString(4, String.valueOf(unitPricee));
        sqLiteStatement16.bindString(5, "1");
        sqLiteStatement16.bindString(6, String.valueOf(sumPrice));
        long id = sqLiteStatement16.executeInsert();

        sqLiteDatabase16.close();
        return id > 0;
    }

    public boolean checkIfProductExists(String productName, Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase1 = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM products WHERE name ='" + productName + "'" ;

        Cursor cursor = sqLiteDatabase1.rawQuery(query, null);

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        sqLiteDatabase1.close();

        return exists;
    }

    public void plusProduct (String name, Context context) {

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase1 = dbHelper.getReadableDatabase();
        SQLiteDatabase sqLiteDatabase2 = dbHelper.getWritableDatabase();

        cursor = sqLiteDatabase.rawQuery("SELECT unitPrice, quantity, totalPrice FROM products WHERE name ='" + name +"'" , null, null);
        int unitPriceIndex = cursor.getColumnIndex("unitPrice");
        int quantityIndex = cursor.getColumnIndex("quantity");

        while (cursor.moveToNext()) {
            int preQuantity = Integer.parseInt(cursor.getString(quantityIndex));
            double unitPrice = Double.parseDouble(cursor.getString(unitPriceIndex));
            String quantity = String.valueOf((preQuantity + 1));
            String sumPrice = String.valueOf((Integer.parseInt(quantity) * unitPrice));
            String increaseQuantity = "UPDATE products SET quantity =" + quantity +
                    ", sumPrice = " + sumPrice + " " +
//                    ", totalPrice = " + totalPrice + " " +
                    "WHERE name ='" + name + "'";

            sqLiteDatabase2.execSQL(increaseQuantity);
        }
        cursor.close();
        sqLiteDatabase2.close();
        sqLiteDatabase1.close();
    }

    public void minusProduct (String name, Context context) {

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase1 = dbHelper.getReadableDatabase();
        SQLiteDatabase sqLiteDatabase2 = dbHelper.getWritableDatabase();

        cursor = sqLiteDatabase.rawQuery("SELECT unitPrice, quantity, totalPrice FROM products WHERE name ='" + name +"'" , null, null);
        int unitPriceIndex = cursor.getColumnIndex("unitPrice");
        int quantityIndex = cursor.getColumnIndex("quantity");


        while (cursor.moveToNext()) {

            int preQuantity = Integer.parseInt(cursor.getString(quantityIndex));

            int unitPrice = Integer.parseInt(cursor.getString(unitPriceIndex));
            String quantityAfter = String.valueOf((preQuantity - 1));
            String sumPrice = String.valueOf((Integer.parseInt(quantityAfter) * unitPrice));

            String decreaseQuantity = "UPDATE products SET quantity =" + quantityAfter +
                    ", sumPrice = " + sumPrice + " " +
//                    ", totalPrice = " + totalPrice + " " +
                    "WHERE name ='" + name + "'";

            sqLiteDatabase2.execSQL(decreaseQuantity);
        }
        cursor.close();
        sqLiteDatabase2.close();
        sqLiteDatabase1.close();
    }



    public void removeProduct(Product product, Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase16 = dbHelper.getWritableDatabase();

        String productName = product.getName();

        sqLiteDatabase16.delete("products", "name = ?", new String[] { productName });
        sqLiteDatabase16.close();
    }
}
