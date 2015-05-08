
/**
 * MONSTERPARSER CLASS
 * Deserializes monster JSON data.
 */

package galenscovell.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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
            InputStreamReader reader = new InputStreamReader(new FileInputStream("data/monsters.json"));
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
            if (selected.getLevel() == level || selected.getLevel() == (level - 1)) {
                selected.setup();
                found = true;
                System.out.println(selected);
                return selected;
            }
        }
        return null;
    }
}
