package galenscovell.util;

import galenscovell.entities.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * PLAYER PARSER
 * Deserializes player class JSON data.
 *
 * @author Galen Scovell
 */

public class PlayerParser {
    public Player pullClassStats(String type) {
        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
        Player returned = null;
        try {
            BufferedReader reader = Gdx.files.internal("playerClasses.json").reader(1024);
            JsonArray cArray = parser.parse(reader).getAsJsonArray();
            for (JsonElement cElement : cArray) {
                Player player = gson.fromJson(cElement, Player.class);
                if (player.getClassType().equals(type)) {
                    player.setup();
                    returned = player;
                }
            }
            reader.close();
        } catch (IOException ie) {
            ie.printStackTrace();
        } finally {
            return returned;
        }
    }
}
