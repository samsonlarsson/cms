package ke.co.greencredit.nunua.GeoCoder;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.io.IOException;
import java.util.List;

import ke.co.greencredit.nunua.R;

/**
 * Created by eorenge on 1/5/16.
 */
public class GeoCoder extends Activity implements TextWatcher {
    EditText etGeo;
    List<Address> addressList;
    //AIzaSyCeN7solrXIV0yj7V9OYhxdQjFk6AUtjRk
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo);
        AutoCompleteTextView completeTextView = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);

//        etGeo = (EditText) findViewById(R.id.etLocation);
//        etGeo.addTextChangedListener(this);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        String toSearch = etGeo.getText().toString();
        if(toSearch != "" && toSearch.length() >= 3){
            Geocoder geocoder =new Geocoder(getApplicationContext());
            if(geocoder.isPresent()){
                try {
                    addressList = geocoder.getFromLocationName(toSearch + " , nairobi kenya",5);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                if(addressList == null){
                    Log.d("log message>","null address list");
                    return;

                }

                if(addressList.size() != 0){
                    Address a = addressList.get(0);
                    Log.d("log message>",a.getAddressLine(0));

                }else {
                    Log.d("log message", "No location was fouund");
                }

            }else{

                Log.d("log message", "Geo Coder absent");

            }
        }


    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
