package org.brandon.rika.ffr;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class Summary extends ActionBarActivity {

    DataAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        api = DataAPI.getInstance(this);

        ArrayList<String> array_moves = api.getMoveList();
        System.out.println("array_moves.size() = " + array_moves.size());
        String[] string_moves = array_moves.toArray(new String[array_moves.size()]);
        ListAdapter list_moves = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, string_moves);
        ListView v_body = (ListView) findViewById(R.id.list_moves);
        v_body.setAdapter(list_moves);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_summary, menu);
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
