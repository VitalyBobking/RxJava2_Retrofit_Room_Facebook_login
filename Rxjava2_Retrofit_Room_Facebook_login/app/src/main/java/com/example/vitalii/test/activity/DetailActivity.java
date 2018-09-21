package com.example.vitalii.test.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.vitalii.test.R;
import com.example.vitalii.test.model.Person;

public class DetailActivity extends AppCompatActivity {
    
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Person person = getIntent().getParcelableExtra(ScreenTwoActivity.PERSON_EXTRA);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(person.getName());
        }

        Log.e("person", String.valueOf(person));
        TextView tvName = findViewById(R.id.tvName);
        TextView tvEmail = findViewById(R.id.tvEmail);
        TextView tvPhone = findViewById(R.id.tvPhone);
        TextView tv_website = findViewById(R.id.tv_website);
        TextView tv_company = findViewById(R.id.tv_company);
        TextView tv_city = findViewById(R.id.tv_city);
        TextView tv_street = findViewById(R.id.tv_street);
        TextView tv_suit = findViewById(R.id.tv_suit);
        TextView tv_zipCode = findViewById(R.id.tv_zipCode);
        TextView tv_catchPhrase = findViewById(R.id.tv_catchPhrase);
        TextView tv_bs = findViewById(R.id.tv_bs);

        tvName.setText("Name: "+person.getName());
        tvEmail.setText("Email: "+person.getEmail());
        tvPhone.setText("Phone: "+person.getPhone());
        tv_website.setText("Website: "+person.getWebsite());
        tv_company.setText("Company: "+person.getCompany().getName());
        tv_city.setText("City: "+person.getAddress().getCity());
        tv_street.setText("Street: "+person.getAddress().getStreet());
        tv_suit.setText("Suit: "+person.getAddress().getSuite());
        tv_zipCode.setText("Zip code: "+person.getAddress().getZipcode());
        tv_catchPhrase.setText("Catch phrase: "+person.getCompany().getCatchPhrase());
        tv_bs.setText("BS: "+person.getCompany().getBs());

        findViewById(R.id.btnOpenMap).setOnClickListener(v ->  {
                getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.rlContent, MapFragment.newInstance(Double.parseDouble(person.getAddress().getGeo().getLat()),
                        Double.parseDouble(person.getAddress().getGeo().getLng()), person.getName()), MapFragment.class.getName())
                .disallowAddToBackStack()
                .commit();
            });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
