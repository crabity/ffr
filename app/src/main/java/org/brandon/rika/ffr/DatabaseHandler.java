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
    private static final int DATABASE_VERSION = 11;

    // Database Name
    static final String DATABASE_NAME = "FitFitRevolution";

    // Table names
    static final String TABLE_MOVES = "moves";
    static final String TABLE_MOVE_HISTORY = "movehistory";
    static final String TABLE_BODY_PART = "bodypart";
    static final String TABLE_WORKOUT = "workouts";
    static final String TABLE_EQUIPMENT = "equipment";
    static final String TABLE_MOVE_EQUIP = "moveequip";
    static final String TABLE_WEIGHTS = "weights";

    // Table Moves Fields
    static final String MOVES_ID = "moveid";
    static final String MOVES_NAME = "name";
    static final String MOVES_DESCRIPTION = "description";
    static final String MOVES_BODY_PART_ID = "partid";
    static final String MOVES_PRIORITY = "priority";
    static final String MOVES_WEIGHT_ID = "moveweightid";

    // Table Move_History Fields
    static final String MH_MOVE_ID = "moveid";
    static final String MH_WORKOUT_ID = "workoutid";
    static final String MH_WEIGHT = "weight";
    static final String MH_REPS = "reps";
    static final String MH_PLACEMENT = "placement";

    // Table Body_Part Fields
    static final String BP_ID = "partid";
    static final String BP_NAME = "name";
    static final String BP_PRIORITY = "priority";

    // Table Workout Fields
    static final String WORKOUT_ID = "workoutid";
    static final String WORKOUT_DATETIME = "time";
    static final String WORKOUT_SCORE = "score";

    // Table Equipment Fields
    static final String EQUIPMENT_ID = "equipmentid";
    static final String EQUIPMENT_NAME = "name";

    // Table Move_Equip Fields
    static final String ME_MOVE_ID = "moveid";
    static final String ME_EQUIP_ID = "equipid";

    // Table Weights Fields
    static final String WEIGHTS_ID = "weightid";
    static final String WEIGHTS_NUM = "weightnum";

    // other vars
    private static Context context;
    private static DatabaseHandler i_dbh;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static DatabaseHandler getInstance(Context c) {
        if (i_dbh == null) {
            i_dbh = new DatabaseHandler(c.getApplicationContext());
        }
        return i_dbh;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Moves Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_MOVES + "("
                + MOVES_ID + " INTEGER PRIMARY KEY,"
                + MOVES_NAME + " TEXT,"
                + MOVES_DESCRIPTION + " TEXT,"
                + MOVES_BODY_PART_ID + " INTEGER,"
                + MOVES_PRIORITY + " INTEGER,"
                + MOVES_WEIGHT_ID + " INTEGER, "
                + "FOREIGN KEY(" + MOVES_BODY_PART_ID + ") REFERENCES " + TABLE_BODY_PART + "(" + BP_ID + "), "
                + "FOREIGN KEY(" + MOVES_WEIGHT_ID + ") REFERENCES " + TABLE_WEIGHTS + "(" + WEIGHTS_ID + "))");

        // Create Move_History Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_MOVE_HISTORY + "("
                + MH_MOVE_ID + " INTEGER,"
                + MH_WORKOUT_ID + " INTEGER,"
                + MH_WEIGHT + " INTEGER,"
                + MH_REPS + " INTEGER,"
                + MH_PLACEMENT + " INTEGER, "
                + "FOREIGN KEY(" + MH_MOVE_ID + ") REFERENCES " + TABLE_MOVES + "(" + MOVES_ID + "), "
                + "FOREIGN KEY(" + MH_WORKOUT_ID + ") REFERENCES " + TABLE_WORKOUT + "(" + WORKOUT_ID + "))");

        // Create Body_Part Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_BODY_PART + "("
                + BP_ID + " INTEGER PRIMARY KEY,"
                + BP_NAME + " TEXT,"
                + BP_PRIORITY + " INTEGER)");

        // Create Workout Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_WORKOUT + "("
                + WORKOUT_ID + " INTEGER PRIMARY KEY,"
                + WORKOUT_DATETIME + " INTEGER,"
                + WORKOUT_SCORE + " INTEGER)");

        // Create Equipment Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EQUIPMENT + "("
                + EQUIPMENT_ID + " INTEGER PRIMARY KEY,"
                + EQUIPMENT_NAME + " TEXT)");

        // Create Move_Equipment Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_MOVE_EQUIP + "("
                + ME_MOVE_ID + " INTEGER,"
                + ME_EQUIP_ID + " INTEGER, "
                + "FOREIGN KEY(" + ME_MOVE_ID + ") REFERENCES " + TABLE_MOVES + "(" + MOVES_ID + "), "
                + "FOREIGN KEY(" + ME_EQUIP_ID + ") REFERENCES " + TABLE_EQUIPMENT + "(" + EQUIPMENT_ID + "))");

        // Create Weights Table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_WEIGHTS + "("
                + WEIGHTS_ID + " INTEGER PRIMARY KEY,"
                + WEIGHTS_NUM + " REAL)");

        FillBodyPartsTable(db);
        FillEquipmentTable(db);
        FillMovesTable(db);
        FillMoveEquipTable(db);
        FillWeightsTable(db);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVES);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_MOVE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BODY_PART);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_WORKOUT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQUIPMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVE_EQUIP);
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

    private void FillMoveEquipTable(SQLiteDatabase db) {
        String line;
        String csvDelimiter = ",";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.moveequipment)));
            while ((line = br.readLine()) != null) {
                String[] input = line.split(csvDelimiter);
                ContentValues values = new ContentValues();
                values.put(ME_MOVE_ID, input[0]);
                values.put(ME_EQUIP_ID, input[1]);
                db.insert(TABLE_MOVE_EQUIP, null, values);
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
                values.put(MOVES_BODY_PART_ID, input[2]);
                values.put(MOVES_PRIORITY, 1);
                values.put(MOVES_WEIGHT_ID, input[3]);
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

    private void FillWeightsTable(SQLiteDatabase db) {
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.weights)));
            while ((line = br.readLine()) != null) {
                ContentValues values = new ContentValues();
                values.put(WEIGHTS_NUM, Float.parseFloat(line));
                db.insert(TABLE_WEIGHTS, null, values);
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
            do {
                for (int i = 0; i < cursor.getInt(2); i++) {
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

    public ArrayList<String> getEquipmentList(SQLiteDatabase db, ArrayList<Integer> moveList) {
        ArrayList<String> names = new ArrayList<String>();
        Cursor cursor = null;
        for (int i: moveList) {
            cursor = db.rawQuery("SELECT " + ME_EQUIP_ID + " FROM " + TABLE_MOVE_EQUIP + " WHERE " + ME_MOVE_ID + " = " + i, null);
            if (cursor.moveToFirst()) {
                boolean testForMatch = false;
                for (String test: names) {
                    if (test.equals(getEquipmentName(db, cursor.getInt(0)))) {
                        testForMatch = true;
                    }
                }
                if (!testForMatch) {
                    names.add(getEquipmentName(db, cursor.getInt(0)));
                }
            }
            cursor.close();
        }
        return names;
    }

    public String getEquipmentName(SQLiteDatabase db, Integer i) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EQUIPMENT + " WHERE " + EQUIPMENT_ID + " = " + i, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(1);
        } else return "Error";
    }

    public static Integer getLastRep(SQLiteDatabase db, Integer mID) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MOVE_HISTORY + " WHERE " + MH_MOVE_ID + " = " + mID + " ORDER BY " + MH_WORKOUT_ID + " DESC, " + MH_PLACEMENT + " DESC", null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(3);
        } else return 10;
    }

    public static Cursor getMove(SQLiteDatabase db, Integer mID) {
        return db.rawQuery("SELECT * FROM " + TABLE_MOVES + " WHERE " + MOVES_ID + " = " + mID, null);
    }

    public String getMoveName(SQLiteDatabase db, Integer i) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MOVES + " WHERE " + MOVES_ID + "=" + i, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(1);
        } else return "Error";
    }

    public Integer getNextWorkoutID(SQLiteDatabase db) {
        Integer value = 1;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WORKOUT + " ORDER BY " + WORKOUT_ID + " DESC", null);
        if (cursor.moveToFirst()) {
            value = cursor.getInt(0) + 1;
        }
        return value;
    }

    public Integer getRandomMoveID(SQLiteDatabase db, Integer partNum) {
        ArrayList<Integer> list = new ArrayList<Integer>(500);
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MOVES + " WHERE " + MOVES_BODY_PART_ID + " = " + partNum, null);
        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i < cursor.getInt(4); i++) {
                    list.add(cursor.getInt(0));
                }
                db.execSQL("UPDATE " + TABLE_MOVES + " SET " + MOVES_PRIORITY + " = " + (cursor.getInt(4) + 2) + " WHERE " + MOVES_ID + " = " + cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        Random random = new Random();
        int placement = random.nextInt(list.size());
        db.execSQL("UPDATE " + TABLE_MOVES + " SET " + MOVES_PRIORITY + " = 1 WHERE " + MOVES_ID + " = " + list.get(placement));
        return list.get(placement);
    }

    public static Integer getWeightNum(SQLiteDatabase db, Integer wID) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WEIGHTS + " WHERE " + WEIGHTS_ID + "=" + wID, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(1);
        } else return 0;
    }

    public static void insertWorkout(SQLiteDatabase db, Integer wID, Integer score) {
        db.execSQL("INSERT INTO " + TABLE_WORKOUT + "(" + WORKOUT_DATETIME + ", " + WORKOUT_SCORE + ") VALUES(DATETIME('now'), " + score + ")");
    }

    public static void insertWorkoutMove(SQLiteDatabase db, Integer mID, Integer wID, Integer reps, Integer weightID, Integer pos) {
        db.execSQL("INSERT INTO " + TABLE_MOVE_HISTORY + " VALUES(" + mID + ", " + wID + ", " + weightID + ", "+ reps + ", " + pos + ")");
    }
}