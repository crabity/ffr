package org.brandon.rika.ffr;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class NewWorkout extends ActionBarActivity {

    DataAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);

        api = new DataAPI(this);

        ArrayList<String> array_body = api.getBodyParts(this);
        String[] string_body = array_body.toArray(new String[array_body.size()]);
        ListAdapter list_bodypart = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, string_body);
        ListView v_body = (ListView) findViewById(R.id.new_list_bodypart);
        v_body.setAdapter(list_bodypart);

        api.getMoves(this);

        ArrayList<String> array_equip = api.getEquipment(this);
        String[] string_equip = array_equip.toArray(new String[array_equip.size()]);
        ListAdapter list_equip = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, string_equip);
        ListView v_equip = (ListView) findViewById(R.id.new_list_equipment);
        v_equip.setAdapter(list_equip);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_workout, menu);
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

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }

    public void startWorkout(View v){
        Intent intent = new Intent(this, Workout.class);
        startActivity(intent);
    }

    public void rerollWorkout(View v){
        Intent intent = new Intent(this, NewWorkout.class);
        startActivity(intent);
    }
}
