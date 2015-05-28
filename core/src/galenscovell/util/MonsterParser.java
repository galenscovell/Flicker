
/**
 * MONSTERPARSER CLASS
 * Deserializes monster JSON data.
 */

package galenscovell.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import galenscovell.entities.Monster;


public class MonsterParser {


    private List<Monster> createMonsterList() {
        List<Monster> monsterList = new ArrayList<Monster>();
        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
        try {
            BufferedReader reader = Gdx.files.internal("monsters.json").reader(1024);
            JsonArray mArray = parser.parse(reader).getAsJsonArray();
            for (JsonElement mElement : mArray) {
                Monster monster = gson.fromJson(mElement, Monster.class);
                monsterList.add(monster);
            }
            reader.close();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return monsterList;
    }

    public Monster spawn(int level) {
        List<Monster> monsterList = createMonsterList();
        Random random = new Random();
        boolean found = false;
        while (!found) {
            int selection = random.nextInt(monsterList.size());
            Monster selected = monsterList.get(selection);
            if (selected.level == level || selected.level == (level - 1)) {
                selected.setup();
                found = true;
                return selected;
            }
        }
        return null;
    }
}
