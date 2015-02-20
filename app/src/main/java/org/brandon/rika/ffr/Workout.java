package org.brandon.rika.ffr;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class Workout extends ActionBarActivity {

    DataAPI api;
    Move i_move;
    Integer workoutID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        api = DataAPI.getInstance(this);
        i_move = new Move(api.getDB(), api.getMoveID(), 0, api.getMoveIDX());

        TextView workout_id = (TextView) findViewById(R.id.move_count);
        workout_id.setText("# " + api.getMoveIDX() + " of " + api.MOVE_COUNT);

        TextView workout_name = (TextView) findViewById(R.id.workout_name);
        workout_name.setText(i_move.name);

        TextView workout_desc = (TextView) findViewById(R.id.workout_desc);
        workout_desc.setText(i_move.description);

        EditText workout_rep = (EditText) findViewById(R.id.workout_rep);
        workout_rep.setText(i_move.reps + "");

        EditText workout_weight = (EditText) findViewById(R.id.workout_weight);
        workout_weight.setText(i_move.weight + "");

        workoutID = DatabaseHandler.getNextWorkoutID(api.getDB());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workout, menu);
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

    public void toNextPage(View v){
        Intent intent = null;
        if(api.isLastMove()) intent = new Intent(this, Summary.class);
        else {
            api.incrMoveIDX();
            i_move.submit();
            intent = new Intent(this, Workout.class);
        }

        startActivity(intent);
    }

    public void setRepsUp(View v){
        i_move.setRepsUp();
        EditText workout_rep = (EditText) findViewById(R.id.workout_rep);
        workout_rep.setText(i_move.reps + "");
    }

    public void setRepsDown(View v){
        i_move.setRepsDown();
        EditText workout_rep = (EditText) findViewById(R.id.workout_rep);
        workout_rep.setText(i_move.reps + "");
    }

    public void setWeightUp(View v){
        i_move.setWeightUp();
        EditText workout_rep = (EditText) findViewById(R.id.workout_weight);
        workout_rep.setText(i_move.weight + "");
    }

    public void setWeightDown(View v){
        i_move.setWeightDown();
        EditText workout_rep = (EditText) findViewById(R.id.workout_weight);
        workout_rep.setText(i_move.weight + "");
    }

}
