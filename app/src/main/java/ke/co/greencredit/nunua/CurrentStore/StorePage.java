package ke.co.greencredit.nunua.CurrentStore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ke.co.greencredit.nunua.Home.Main;
import ke.co.greencredit.nunua.R;
import ke.co.greencredit.nunua.StoreNavigation.StoreDepartment;

public class StorePage extends AppCompatActivity {
    public GridView productsGridView;
    public ImageView imgBack;
    public LinearLayout curDept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_page);

        productsGridView = (GridView) findViewById(R.id.products_grid);
        setPopularItems();

        imgBack = (ImageView) findViewById(R.id.back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StorePage.this, Main.class);
                startActivity(intent);
            }
        });

        curDept = (LinearLayout) findViewById(R.id.curDeptContainer);
        curDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StorePage.this, StoreDepartment.class);
                startActivity(intent);
            }
        });
    }

    public void setPopularItems() {
        ProductsAdapter productsAdapter = new ProductsAdapter(getApplicationContext());
        productsGridView.setAdapter(productsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_store_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
