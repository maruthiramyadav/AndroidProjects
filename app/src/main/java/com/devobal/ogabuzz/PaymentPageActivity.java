package com.devobal.ogabuzz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class PaymentPageActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView aboutusback;
    TextView paypalpayment,cardpayment;
    EditText name,cardnumber,cvv;
    Spinner monthspinner,dayspinner;
    ArrayList<String> month;
    ArrayList<String> day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        aboutusback=(ImageView)findViewById(R.id.aboutusback);
        paypalpayment=(TextView) findViewById(R.id.paypalpayment);
        cardpayment=(TextView) findViewById(R.id.cardpayment);
        name=(EditText)findViewById(R.id.name);
        cardnumber=(EditText)findViewById(R.id.cardnumber);
        cvv=(EditText)findViewById(R.id.cvv);
        monthspinner=(Spinner)findViewById(R.id.monthspinner);
        dayspinner=(Spinner)findViewById(R.id.dayspinner);

//month array
        month=new ArrayList<String>();
        for (int i=1;i<=12;i++){
            if (i==1){
                month.add("Month");
                month.add(String.valueOf(i));
            }else {
                month.add(String.valueOf(i));
            }
        }

//day array
        day=new ArrayList<String>();
        for (int i=1;i<=30;i++){
            if (i==1){
                day.add("Day");
                day.add(String.valueOf(i));
            }else {
                day.add(String.valueOf(i));
            }
        }

        ArrayAdapter monthadapter = new ArrayAdapter(PaymentPageActivity.this,
                R.layout.textview,month);
        monthspinner.setAdapter(monthadapter);

        ArrayAdapter dayadapter = new ArrayAdapter(PaymentPageActivity.this,
                R.layout.textview,day);
        dayspinner.setAdapter(dayadapter);

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(monthspinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(300);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(dayspinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(300);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

        aboutusback.setOnClickListener(this);
        paypalpayment.setOnClickListener(this);
        cardpayment.setOnClickListener(this);

        cardpayment.performClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.paypalpayment:
                paypalpayment.setBackgroundColor(getResources().getColor(R.color.colorlightaccent));
                paypalpayment.setTextColor(getResources().getColor(R.color.colorWhite));
                cardpayment.setBackgroundColor(getResources().getColor(R.color.colorborder));
                cardpayment.setTextColor(getResources().getColor(R.color.colorBlack));
                break;
            case R.id.cardpayment:
                cardpayment.setBackgroundColor(getResources().getColor(R.color.colorlightaccent));
                cardpayment.setTextColor(getResources().getColor(R.color.colorWhite));
                paypalpayment.setBackgroundColor(getResources().getColor(R.color.colorborder));
                paypalpayment.setTextColor(getResources().getColor(R.color.colorBlack));
                break;
            case R.id.aboutusback:
                finish();
                break;
        }
    }
}
