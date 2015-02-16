package org.brandon.rika.ffr;

/**
 * Created by JJ on 2/15/2015.
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    static final String DATABASE_NAME = "FitFitRevolution";

    // Table names
    static final String TABLE_MOVES = "moves";
    static final String TABLE_MOVE_HISTORY = "movehistory";
    static final String TABLE_BODY_PART = "bodypart";
    static final String TABLE_WORKOUT = "workouts";
    static final String TABLE_EQUIPMENT = "equipment";

    // Table Moves Fields
    private static final String MOVES_ID = "moveid";
    private static final String MOVES_NAME = "name";
    private static final String MOVES_DESCRIPTION = "description";
    private static final String MOVES_EQUIPMENT_ID = "equipmentid";
    private static final String MOVES_BODY_PART_ID = "partid";
    private static final String MOVES_PRIORITY = "priority";
    private static final String MOVES_WEIGHT = "defaultweight";

    // Table Move_History Fields
    private static final String MH_MOVE_ID = "moveid";
    private static final String MH_WORKOUT_ID = "workoutid";
    private static final String MH_WEIGHT = "weight";
    private static final String MH_REPS = "reps";
    private static final String MH_PLACEMENT = "placement";

    // Table Body_Part Fields
    static final String BP_ID = "partid";
    static final String BP_NAME = "name";
    static final String BP_PRIORITY = "priority";

    // Table Workout Fields
    private static final String WORKOUT_ID = "workoutid";
    private static final String WORKOUT_DATETIME = "time";
    private static final String WORKOUT_SCORE = "score";

    // Table Equipment Fields
    private static final String EQUIPMENT_ID = "equipmentid";
    private static final String EQUIPMENT_NAME = "name";

    // other vars
    private static Context context;
    private static DatabaseHandler i_dbh;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static DatabaseHandler getInstance(Context c){
        if(i_dbh==null){
            i_dbh = new DatabaseHandler(c.getApplicationContext());
        }
        return i_dbh;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Moves Table
        db.execSQL("CREATE TABLE " + TABLE_MOVES + "("
                + MOVES_ID + " INTEGER PRIMARY KEY,"
                + MOVES_NAME + " TEXT,"
                + MOVES_DESCRIPTION + " TEXT,"
                + MOVES_EQUIPMENT_ID + " TEXT,"
                + MOVES_BODY_PART_ID + " INTEGER,"
                + MOVES_PRIORITY + " INTEGER,"
                + MOVES_WEIGHT + " INTEGER, "
                + "FOREIGN KEY(" + MOVES_BODY_PART_ID + ") REFERENCES " + TABLE_BODY_PART + "(" + BP_ID + "), "
                + "FOREIGN KEY(" + MOVES_EQUIPMENT_ID + ") REFERENCES " + TABLE_EQUIPMENT + "(" + EQUIPMENT_ID + "))");

        // Create Move_History Table
        db.execSQL("CREATE TABLE " + TABLE_MOVE_HISTORY + "("
                + MH_MOVE_ID + " INTEGER,"
                + MH_WORKOUT_ID + " INTEGER,"
                + MH_WEIGHT + " INTEGER,"
                + MH_REPS + " INTEGER,"
                + MH_PLACEMENT + " INTEGER, "
                + "FOREIGN KEY(" + MH_MOVE_ID + ") REFERENCES " + TABLE_MOVES + "(" + MOVES_ID + "), "
                + "FOREIGN KEY(" + MH_WORKOUT_ID + ") REFERENCES " + TABLE_WORKOUT + "(" + WORKOUT_ID + "))");

        // Create Body_Part Table
        db.execSQL("CREATE TABLE " + TABLE_BODY_PART + "("
                + BP_ID + " INTEGER PRIMARY KEY,"
                + BP_NAME + " TEXT,"
                + BP_PRIORITY + " INTEGER)");

        // Create Workout Table
        db.execSQL("CREATE TABLE " + TABLE_WORKOUT + "("
                + WORKOUT_ID + " INTEGER PRIMARY KEY,"
                + WORKOUT_DATETIME + " INTEGER,"
                + WORKOUT_SCORE + " INTEGER)");

        // Create Equipment Table
        db.execSQL("CREATE TABLE " + TABLE_EQUIPMENT + "("
                + EQUIPMENT_ID + " INTEGER PRIMARY KEY,"
                + EQUIPMENT_NAME + " TEXT)");

        FillBodyPartsTable(db);
        FillEquipmentTable(db);
        FillMovesTable(db);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_MOVES);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_MOVE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_BODY_PART);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_WORKOUT);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_EQUIPMENT);
        // Create tables again
        onCreate(db);
    }

    private void FillBodyPartsTable(SQLiteDatabase db) {
        String line;
        String csvDelimiter = ",";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.parts)));
            while ((line = br.readLine()) != null) {
                String[] input = line.split(csvDelimiter);
                ContentValues values = new ContentValues();
                values.put(BP_NAME, input[0]);
                values.put(BP_PRIORITY, 1);
                db.insert(TABLE_BODY_PART, null, values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void FillEquipmentTable(SQLiteDatabase db) {
        String line;
        String csvDelimiter = ",";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.equipment)));
            while ((line = br.readLine()) != null) {
                String[] input = line.split(csvDelimiter);
                ContentValues values = new ContentValues();
                values.put(EQUIPMENT_NAME, input[0]);
                db.insert(TABLE_EQUIPMENT, null, values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void FillMovesTable(SQLiteDatabase db) {
        String line;
        String csvDelimiter = ",";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.moves)));
            while ((line = br.readLine()) != null) {
                String[] input = line.split(csvDelimiter);
                ContentValues values = new ContentValues();
                values.put(MOVES_NAME, input[0]);
                values.put(MOVES_DESCRIPTION, input[1]);
                values.put(MOVES_EQUIPMENT_ID, input[2]);
                values.put(MOVES_BODY_PART_ID, input[3]);
                values.put(MOVES_PRIORITY, 1);
                values.put(MOVES_WEIGHT, input[4]);
                db.insert(TABLE_MOVES, null, values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Integer getBodyPart(SQLiteDatabase db) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BODY_PART, null);
        if (cursor.moveToFirst()) {
            int j = 0;
            do {
                for (int i = 0; i < cursor.getInt(2); i++) {
                    j=cursor.getInt(2);
                    list.add(cursor.getInt(0));
                }
                db.execSQL("UPDATE " + TABLE_BODY_PART + " SET " + BP_PRIORITY + " = " + (cursor.getInt(2) + 1) + " WHERE " + BP_ID + " = " + cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        Random random = new Random();
        int placement = random.nextInt(list.size());
        db.execSQL("UPDATE " + TABLE_BODY_PART + " SET " + BP_PRIORITY + " = 1 WHERE " + BP_ID + " = " + list.get(placement));
        return list.get(placement);
    }

    public String getBodyPartName(SQLiteDatabase db, Integer i) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BODY_PART + " WHERE " + BP_ID + "=" + i, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(1);
        } else return "Error";
    }

    public Integer getMove(SQLiteDatabase db) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MOVES, null);
        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i < cursor.getInt(2); i++) {
                    list.add(cursor.getInt(0));
                }
                db.execSQL("UPDATE " + TABLE_MOVES + " SET " + MOVES_PRIORITY + " = " + (cursor.getInt(2) + 2) + " WHERE " + MOVES_ID + " = " + cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        Random random = new Random();
        int placement = random.nextInt(list.size());
        db.execSQL("UPDATE " + TABLE_MOVES + " SET " + MOVES_PRIORITY + " = 0 WHERE " + MOVES_ID + " = " + list.get(placement));
        return list.get(placement);
    }
}