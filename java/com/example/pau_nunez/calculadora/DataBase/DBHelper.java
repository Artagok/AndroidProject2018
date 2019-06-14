package com.example.pau_nunez.calculadora.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by pau_nunez on 2/02/18.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PointsDatabase";
    private static DBHelper instance;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBHelper getInstance(Context context) {
        if (instance == null) instance = new DBHelper(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE_PUNTIACIONS = "CREATE TABLE " + DataBasePuntuacionsContract.TABLE_NAME + "(" +
                DataBasePuntuacionsContract.COLUMN_USERNAME + " text ," +
                DataBasePuntuacionsContract.COLUMN_POINTS + " text ," +
                DataBasePuntuacionsContract.COLUMN_TIMESTAMP + " text )";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_PUNTIACIONS);

        final String SQL_CREATE_TABLE_LOGIN = "CREATE TABLE " + DataBaseLogin.TALBE_NAME + "(" +
                DataBaseLogin.COLUMN_USERNAME + " text, " +
                DataBaseLogin.COLUMN_PASSWORD + " text )";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_LOGIN);
        /*
        sqLiteDatabase.execSQL("INSERT INTO " + DataBasePuntuacionsContract.TABLE_NAME + " VALUES ( 'ALVARO', 42, '1517571326')");
        sqLiteDatabase.execSQL("INSERT INTO " + DataBasePuntuacionsContract.TABLE_NAME + " VALUES ( 'PAU', 43, '1517571326')");
        sqLiteDatabase.execSQL("INSERT INTO " + DataBasePuntuacionsContract.TABLE_NAME + " VALUES ( 'NIL', 44, '1517571326')");
        */
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public ArrayList<String> getAllRegisteredUsers() {
        Cursor resultat;
        SQLiteDatabase db = getReadableDatabase();
        //Retorna un cursor amb tots els users a la DB
        resultat = db.rawQuery("SELECT DISTINCT " + DataBasePuntuacionsContract.COLUMN_USERNAME + " FROM " + DataBasePuntuacionsContract.TABLE_NAME,null);
        ArrayList<String> ret = new ArrayList<>();
        if (resultat.moveToFirst()) {
            ret.add(resultat.getString(resultat.getColumnIndex(DataBasePuntuacionsContract.COLUMN_USERNAME)));
            while (resultat.moveToNext()) {
                ret.add(resultat.getString(resultat.getColumnIndex(DataBasePuntuacionsContract.COLUMN_USERNAME)));

            }
        }
        db.close();
        return ret;
    }

    public ArrayList<DBPuntuacioRow> getPuntuacionsMemoryGame() {
        Cursor resultat;
        SQLiteDatabase db = getReadableDatabase();
        resultat = db.rawQuery("SELECT * FROM " + DataBasePuntuacionsContract.TABLE_NAME,null);
        ArrayList<DBPuntuacioRow> ret = new ArrayList<>();
        String nameAux, pointsAux, timeAux;
        if (resultat.moveToFirst()) {
            while(!resultat.isAfterLast()) {
                nameAux = resultat.getString(resultat.getColumnIndex(DataBasePuntuacionsContract.COLUMN_USERNAME));
                pointsAux = resultat.getString(resultat.getColumnIndex(DataBasePuntuacionsContract.COLUMN_POINTS));
                timeAux = resultat.getString(resultat.getColumnIndex(DataBasePuntuacionsContract.COLUMN_TIMESTAMP));
                DBPuntuacioRow aux = new DBPuntuacioRow(nameAux,pointsAux,timeAux);
                ret.add(aux);
                resultat.moveToNext();
            }
        }
        db.close();
        return ret;
    }

    public boolean existsUser(String user) {
        Cursor resultat;
        SQLiteDatabase db = getReadableDatabase();
        resultat = db.rawQuery("SELECT " + DataBaseLogin.COLUMN_USERNAME + " FROM " + DataBaseLogin.TALBE_NAME + " WHERE " + DataBaseLogin.COLUMN_USERNAME + " = '" + user + "'",null);
        boolean ret = resultat.moveToFirst();
        db.close();
        return ret;
    }

    public void insertUser(String user, String password) {
        String DBInsertUser = "INSERT INTO " + DataBaseLogin.TALBE_NAME + " VALUES ( '" + user + "', '" + password + "')";
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            db.execSQL(DBInsertUser);
        }
        db.close();
    }

    public boolean correctUserAndPassword(String user, String password) {
        Cursor resultat;
        SQLiteDatabase db = getReadableDatabase();
        resultat = db.rawQuery("SELECT * FROM " + DataBaseLogin.TALBE_NAME + " WHERE " + DataBaseLogin.COLUMN_USERNAME + " = '" + user + "'",null);
        if (resultat.moveToFirst()) {
            if (user.equals(resultat.getString(resultat.getColumnIndex(DataBaseLogin.COLUMN_USERNAME)))
                    && password.equals(resultat.getString(resultat.getColumnIndex(DataBaseLogin.COLUMN_PASSWORD)))) {
                db.close();
                return true;
            }

        }
        db.close();
        return false;
    }
}


