/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import javax.sound.sampled.*;

/**
 *
 * @author Dinesan
 */
public class fx_Player implements Runnable {

    private AudioInputStream fxs;
    private Clip fx;

    public fx_Player(String path) {
        try {
            //Get Sounds
            fxs = AudioSystem.getAudioInputStream(getClass().getResource(path));
            fx = AudioSystem.getClip();
            fx.open(fxs);
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
    }

    public void Playfx() {
        try {
            if (fx.isRunning()) {
                fx.stop();
            }
            fx.setFramePosition(0);
            fx.start();
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
    }

    public void run() {
        while (true) {
        }
    }
}
