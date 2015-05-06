
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import galenscovell.entities.Monster;


public class MonsterParser {
    private List<Monster> monsterList;


    public MonsterParser() {;
        this.monsterList = new ArrayList<Monster>();
        Gson gson = new Gson();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/monsters.json"));
            Monster[] monster = gson.fromJson(reader, Monster[].class);
            for (Monster m : monster) {
                monsterList.add(m);
            }
            reader.close();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public Monster spawn() {
        Random random = new Random();
        int selection = random.nextInt(monsterList.size());
        Monster selected = monsterList.get(selection);
        System.out.println(selected);
        return selected;
    }
}
