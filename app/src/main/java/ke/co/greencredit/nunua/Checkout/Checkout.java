package ke.co.greencredit.nunua.Checkout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ke.co.greencredit.nunua.General.AppConstants;
import ke.co.greencredit.nunua.R;

public class Checkout extends AppCompatActivity {
    Toolbar toolbar;
    TextView stepIndicator;
    Button btnCash;
    Button btnMpesa;
    TextView tvSubTotal;
    SharedPreferences settings;
    AppConstants appConstants = new AppConstants();
    int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        stepIndicator = (TextView) findViewById(R.id.txtStep);
        stepIndicator.setText("Checkout");

        restoreActionBar();
        settings = getSharedPreferences(appConstants.PREFS_NAME,MODE_PRIVATE);
        price = settings.getInt("cartprice", 0);

        settings.edit().putString("cartat", "checkout").apply();

        tvSubTotal = (TextView)findViewById(R.id.subtotal);
        tvSubTotal.setText("Ksh. "+price+".00");
        btnCash = (Button) findViewById(R.id.btnProceedCash);
        btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Checkout.this, CashOnDelivery.class);
                startActivity(intent);
            }
        });

        btnMpesa = (Button) findViewById(R.id.btnProceedMpesa);
        btnMpesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Service unavailable.",Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(Checkout.this, PayMpesa.class);
//                startActivity(intent);
            }
        });

    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.mipmap.left);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_checkout, menu);
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
