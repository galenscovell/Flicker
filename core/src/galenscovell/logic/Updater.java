
/**
 * UPDATER CLASS
 * Handles game logic: interactions, movements, behaviors and HUD updates.
 */

package galenscovell.logic;

import galenscovell.entities.Player;
import galenscovell.util.Constants;


public class Updater {
    private Player player;


    public Updater(Player player) {
        this.player = player;
    }

    public void update(float delta, int[] input) {
        player.move(input[0] * 48, input[1] * 48);
    }

}
