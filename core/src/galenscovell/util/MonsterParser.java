
/**
 * MONSTERPARSER CLASS
 * Deserializes monster JSON data.
 */

package galenscovell.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import galenscovell.entities.Monster;


public class MonsterParser {

    public MonsterParser() {
        Gson gson = new Gson();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/monsters.json"));
            Monster[] monsterList = gson.fromJson(reader, Monster[].class);
            reader.close();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public static void spawn() {

    }
}
