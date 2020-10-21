package com.sarthak.memorableplaces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static  ArrayList<String> places = new ArrayList<>();
    static ArrayList<LatLng> location = new ArrayList<>();
     static ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView =(ListView)findViewById(R.id.listView);
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.sarthak.memorableplaces", Context.MODE_PRIVATE);
    ArrayList<String> latitude=new ArrayList<>();
    ArrayList<String> longitude =new ArrayList<>();
    places.clear();
    latitude.clear();
    longitude.clear();
    location.clear();


        try {
            places=(ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places",ObjectSerializer.serialize(new ArrayList<String>())));
            latitude=(ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("latitudes",ObjectSerializer.serialize(new ArrayList<String>())));
            longitude=(ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("longitudes",ObjectSerializer.serialize(new ArrayList<String>())));

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(places.size()>0&&latitude.size()>0&&longitude.size()>0)
        {
            if(places.size()==latitude.size()&&latitude.size()==longitude.size())
            {
                for(int i=0;i<latitude.size();i++){

                    location.add(new LatLng(Double.parseDouble(latitude.get(i)),Double.parseDouble(longitude.get(i))));


                }
            }
        }else {


            places.add("Add a new place...");
            location.add(new LatLng(0, 0));
        }

         arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,places);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  Toast.makeText(MainActivity.this, "Hahaha", Toast.LENGTH_SHORT).show();
                Intent intent =  new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("Place NUmber",position);
                startActivity(intent);

            }
        });
    }
}
