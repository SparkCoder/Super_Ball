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
public class Audio_Player {

    private AudioInputStream bg;
    private Clip clip;
    public boolean loop = false;

    public Audio_Player() {
        try {
            //Get Sounds
            bg = AudioSystem.getAudioInputStream(getClass().getResource("/Resources/Audio/BG.wav"));
            clip = AudioSystem.getClip();
            clip.open(bg);
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
    }

    public void loop() {
        try {
            new Thread(new Runnable() {

                public void run() {
                    while (loop) {
                        if (!clip.isRunning()) {
                            clip.loop(Clip.LOOP_CONTINUOUSLY); // Start playing
                        }
                        try {
                            Thread.sleep(clip.getMicrosecondLength() / clip.getFrameLength());
                        } catch (Exception ex) {
                            System.out.println("Error: " + ex);
                        }
                    }
                }
            }).start();
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
    }

    public void startLoop() {
        loop = true;
        loop();
    }

    public void stopLoop() {
        loop = false;
        stop();
    }

    private void stop() {
        clip.stop();
        clip.setFramePosition(0);
    }
}
