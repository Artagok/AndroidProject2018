package com.example.pau_nunez.calculadora.DataBase;

/**
 * Created by pau_nunez on 16/02/18.
 */

public class DBPuntuacioRow {

    private String name;
    private String points;
    private String time;

    public DBPuntuacioRow (String name, String points, String time) {
        this.name = name;
        this.points = points;
        this.time = time;
    }

    public String getName() {
        return this.name;
    }

    public String getPoints() {
        return this.points;
    }

    public String getTime() {
        return this.time;
    }
}
