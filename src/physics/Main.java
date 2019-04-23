package physics;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author Abhishek.D
 */
class Platform {

    public int x,  y;
    public int px,  py;
    public int w,  h;
    public int ch;
    public int id;
    private Color tc,  col;

    public Platform(int xi, int yi, int wi, int hi, int c) {
        x = xi;
        y = yi;
        w = wi;
        h = hi;
        ch = c;
    }

    public void draw(Graphics g) {
        tc = g.getColor();

        if (ch == 1) {
            col = Color.RED;
        } else if (ch == 2) {
            col = Color.BLUE;
        } else {
            col = Color.GREEN;
        }

        g.setColor(col);
        g.fill3DRect(x, y, w, h, true);
        g.setColor(tc);
    }

    public void clean(Graphics g) {
        g.clearRect(px, py, w, h);
    }
}

class Ball {

    public boolean og = false;
    public int sp,  usp;
    public int rad;
    public int x,  y,  dx,  dy,  px,  py;
    private Color tc,  col;
    private int bi;

    public Ball(int r, int s, int us, Color c) {
        rad = r;
        sp = s;
        usp = us;
        col = c;
    }

    public void draw(Graphics g) {
        tc = g.getColor();
        g.setColor(col);
        for (bi = 0; bi <= (rad * 2); bi += 10) {
            g.drawOval(x - (bi / 2), y - rad, bi, rad * 2);
            g.drawOval(x - rad, y - (bi / 2), rad * 2, bi);
        }
        g.setColor(tc);
    }

    public void clean(Graphics g) {
        g.clearRect(px - (rad * 4), py - (rad * 4), (rad * 8), (rad * 8));
    }
}

class Cont extends Canvas implements Runnable, KeyListener {

    public boolean ctrl,  menu;
    private boolean kicb,  pog,  plp,  started,  begin,  kup,  kdn,  pme,  dead,  pupg;
    private int Frame_Rate;
    private int keyLog[],  kip,  kir,  kic,  kicc,  kl;
    private int pi,  pip,  pipi,  pl;
    private int w,  h,  sbase;
    private int score,  pscore,  dscore,  dsscore,  cse,  ese;
    private int lives;
    private int rand,  rand2,  rand3,  randseed;
    private int Pspeed,  Threshold,  corr;
    private int counter,  icounter,  iPspeed,  dPspeed,  cPspeed;
    private int Fric,  airFric,  loss;
    private int g = 2;
    private int sx,  sy,  sdx,  sax,  sal;
    private int pm;
    private int id,  pid,  idl;
    private Image play_b,  exit_b,  death,  logo,  credits,  go;
    private Audio_Player plr;
    private fx_Player extra,  jump,  land,  speed,  die;
    private Random r;
    private Ball b;
    private Platform p[];

    public Cont() {
        this.setBackground(Color.BLACK);
        this.addKeyListener(this);

        //Initialize local variables
        kl = 4;
        pl = 6;
        Fric = 4;
        airFric = 2;
        Pspeed = 2;
        loss = 2;
        Threshold = 80;
        score = 0;
        pscore = 0;
        dscore = 10;
        dsscore = 40;
        cse = 0;
        pm = 0;
        pme = false;
        ese = 50;
        id = 0;
        pid = -1;
        idl = 1000;
        lives = 5;
        dead = false;
        menu = true;
        kup = kdn = false;
        Frame_Rate = 25;
        icounter = Frame_Rate * 10;
        cPspeed = Pspeed;
        iPspeed = Pspeed + 10;
        dPspeed = 1;
        ctrl = true;
        pupg = false;
        started = false;
        begin = false;
        kicb = false;

        try {
            //Get Images
            play_b = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Pics/play_b.png"));
            exit_b = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Pics/exit_b.png"));
            logo = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Pics/Super_Ball.png"));
            credits = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Pics/Abhishek_Arts.png"));
            death = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Pics/DEATH.png"));
            go = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Pics/Game_Over.png"));
        } catch (Exception e) {
            System.out.println("Error : " + e);
        }


        keyLog = new int[4];
        r = new Random();

        for (kic = 0; kic < kl; kic++) {
            keyLog[kic] = 0;
        }

        plr = new Audio_Player();
        die = new fx_Player("/Resources/Audio/die.wav");
        extra = new fx_Player("/Resources/Audio/extra.wav");
        jump = new fx_Player("/Resources/Audio/jump.wav");
        land = new fx_Player("/Resources/Audio/land.wav");
        speed = new fx_Player("/Resources/Audio/speed.wav");
        b = new Ball(20, 30, 30, Color.BLUE);
        p = new Platform[6];
    }

    @Override
    public void paint(Graphics g) {
        g.setFont(new Font("SansSerif", Font.BOLD, 18));

        if (!menu) {
            //Main Game
            this.setBackground(new Color(80, 173, 250));

            g.drawImage(death, 0, h - Threshold + 20, this);

            if (ese > 0) {
                g.drawString("PRESS ESC FOR MENU", (w / 2) - (9 * g.getFontMetrics().charWidth('S')), sbase / 4);
                ese--;
            }

            if ((score - pscore) == dsscore) {
                cse = 50;
            }

            if (counter > 0) {
                g.drawString("SPEED UP", ((w * 3) / 4) - (4 * g.getFontMetrics().charWidth('S')), sbase / 2);
            }

            if (cse > 0) {
                g.drawString("EXTRA SCORE", (w / 4) - (5 * g.getFontMetrics().charWidth('S')), sbase / 2);
                cse--;
            }

            g.drawLine(0, sbase, w, sbase);
            g.drawString("Lives : " + lives + ", Score : " + score, (w / 2) - (((("Lives : " + lives + ", Score : " + score).length() - 4) / 2) * g.getFontMetrics().charWidth('S')), sbase / 2);
            b.draw(g);

            for (pi = 0; pi < pl; pi++) {
                p[pi].draw(g);
            }

            invalidate();
        } else {
            //Menu Section
            if (started) {
                if (!dead) {
                    //The Menu
                    this.setBackground(Color.BLACK);
                    g.setColor(Color.RED);
                    g.drawImage(logo, (w / 2) - (logo.getWidth(this) / 2), (h / 4) - (logo.getHeight(this) / 2), this);

                    g.drawImage(play_b, (w / 2) - (play_b.getWidth(this) / 2), ((h * 4) / 8) - (play_b.getHeight(this) / 2) - 20, this);
                    g.drawImage(exit_b, (w / 2) - (exit_b.getWidth(this) / 2), ((h * 5) / 8) - (exit_b.getHeight(this) / 2) - 20, this);

                    if (pm == 0) {
                        g.drawRect((w / 2) - (play_b.getWidth(this) / 2) - 20, ((h * 4) / 8) - (play_b.getHeight(this) / 2) - 22, play_b.getWidth(this) + 40, play_b.getHeight(this) + 4);
                    } else if (pm == 1) {
                        g.drawRect((w / 2) - (exit_b.getWidth(this) / 2) - 20, ((h * 5) / 8) - (exit_b.getHeight(this) / 2) - 22, exit_b.getWidth(this) + 40, exit_b.getHeight(this) + 4);
                    }
                    invalidate();
                    g.setColor(Color.BLACK);
                } else {
                    g.setFont(new Font("SansSerif", Font.BOLD, 68));
                    g.drawString("Your Score : " + score, (w / 2) - (("Your Score:" + score).length() / 2) * g.getFontMetrics().charWidth('S'), (h / 2) - go.getHeight(this) + 40);
                    g.drawImage(go, (w / 2) - (go.getWidth(this) / 2), (h / 2) - (go.getHeight(this) / 2), this);
                }
            } else {
                //Show Off a bit
                this.setBackground(Color.BLACK);
                sx = sy = -20;
                sal = 0;
                try {
                    while (sal <= (credits.getWidth(this) / 2) + 30) {
                        g.drawImage(credits, (w / 2) - (credits.getWidth(this) / 2), (h / 2) - (credits.getHeight(this) / 2), this);
                        g.clearRect((w / 2) - (credits.getWidth(this) / 2) - 30, (h / 2) - (credits.getHeight(this) / 2) - 30, (credits.getWidth(this) / 2) + 30 - sal, credits.getHeight(this) + 60);
                        g.clearRect((w / 2) + sal, (h / 2) - (credits.getHeight(this) / 2) - 30, (credits.getWidth(this) / 2) + 30 - sal, credits.getHeight(this) + 60);
                        sal += 8;
                        Thread.sleep(1000 / 25);
                    }

                    Thread.sleep(1000);

                    while (sal >= -10) {
                        g.drawImage(credits, (w / 2) - (credits.getWidth(this) / 2), (h / 2) - (credits.getHeight(this) / 2), this);
                        g.clearRect((w / 2) - (credits.getWidth(this) / 2) - 30, (h / 2) - (credits.getHeight(this) / 2) - 30, (credits.getWidth(this) / 2) + 30 - sal, credits.getHeight(this) + 60);
                        g.clearRect((w / 2) + sal, (h / 2) - (credits.getHeight(this) / 2) - 30, (credits.getWidth(this) / 2) + 30 - sal, credits.getHeight(this) + 60);
                        sal -= 8;
                        Thread.sleep(1000 / 25);
                    }

                    Thread.sleep(250);

                    while (sy <= (h / 4)) {
                        g.clearRect((w / 2) - (logo.getWidth(this) / 2) - 40, sy - (logo.getHeight(this) / 2) - 44, logo.getWidth(this) + 80, logo.getHeight(this) + 80);
                        g.drawImage(logo, (w / 2) - (logo.getWidth(this) / 2), sy - (logo.getHeight(this) / 2), this);
                        Thread.sleep(1000 / 25);
                        sy += 10;
                    }

                    Thread.sleep(500);
                    sdx = 4;
                    sax = 4;

                    while (sx <= (w / 2)) {
                        g.clearRect(sx - (play_b.getWidth(this) / 2) - 40 - sdx - sax, ((h * 4) / 8) - (play_b.getHeight(this) / 2) - 20 - 40, play_b.getWidth(this) + 80, play_b.getHeight(this) + 80);
                        g.clearRect((w - sx) - (exit_b.getWidth(this) / 2) - 40 + sdx + sax, ((h * 5) / 8) - (exit_b.getHeight(this) / 2) - 20 - 40, exit_b.getWidth(this) + 80, exit_b.getHeight(this) + 80);
                        g.drawImage(play_b, sx - (play_b.getWidth(this) / 2), ((h * 4) / 8) - (play_b.getHeight(this) / 2) - 20, this);
                        g.drawImage(exit_b, (w - sx) - (exit_b.getWidth(this) / 2), ((h * 5) / 8) - (exit_b.getHeight(this) / 2) - 20, this);
                        Thread.sleep(1000 / 25);
                        sx += sdx;
                        sdx += sax;
                    }

                    started = true;
                } catch (Exception e) {
                    System.out.println("Error : " + e);
                }
            }
        }
    }

    @Override
    public void update(Graphics g) {
        if (!menu) {
            //Optimised Cleaning during Game
            if (pme) {
                pme = false;
                g.clearRect(0, 0, this.getWidth(), this.getHeight());
            }
            b.clean(g);

            for (pi = 0; pi < pl; pi++) {
                p[pi].clean(g);
            }

            g.clearRect(0, 0, w, sbase - 2);
        } else {
            //Total Clean during Menu Session
            //if (!dead) {
            g.clearRect(0, 0, this.getWidth(), this.getHeight());
        //}
        }
        paint(g);
    }

    public void run() {
        //Wait Until Screen width and Height are loaded
        while (w == 0 || h == 0) {
            w = this.getWidth();
            h = this.getHeight();
        }

        //Scale Images
        play_b = play_b.getScaledInstance(w / 6, h / 8, Image.SCALE_FAST);
        exit_b = exit_b.getScaledInstance(w / 6, h / 8, Image.SCALE_FAST);
        death = death.getScaledInstance(w, Threshold - 20, Image.SCALE_SMOOTH);
        logo = logo.getScaledInstance(w / 3, h / 4, Image.SCALE_SMOOTH);
        credits = credits.getScaledInstance(w / 2, h / 3, Image.SCALE_SMOOTH);
        go = go.getScaledInstance(w / 2, h / 3, Image.SCALE_SMOOTH);

        //Initalize Platforms
        for (pi = 0; pi < pl; pi++) {
            p[pi] = new Platform((((pi % 3) * 2 * w) / 5), ((pi + 1) * h) / (pl + 1), (w / 5), 20, 0);
            p[pi].id = id;
            id++;
        }

        //Initialze Ball position
        b.x = w / 2;
        b.y = h / 2;

        while (ctrl) {
            if (menu) {
                //Menu Loop
                Frame_Rate = 10;
                try {
                    for (kic = 0; kic < kl; kic++) {
                        if (keyLog[kic] == 0) {
                            continue;
                        }

                        if (keyLog[kic] == KeyEvent.VK_DOWN) {
                            if (!kdn) {
                                pm++;
                                if (pm > 1) {
                                    pm = 0;
                                }
                                kdn = true;
                            }
                        }

                        if (keyLog[kic] == KeyEvent.VK_UP) {
                            if (!kup) {
                                pm--;
                                if (pm < 0) {
                                    pm = 1;
                                }
                                kup = true;
                            }
                        }

                        if (keyLog[kic] == KeyEvent.VK_ENTER) {
                            if (dead) {
                                dead = false;
                                Thread.sleep(500);
                            } else {
                                if (pm == 0) {
                                    pme = true;
                                    menu = false;
                                    dead = false;
                                    if (!begin) {
                                        lives = 5;
                                        score = 0;
                                        counter = 0;
                                        cse = 0;
                                        begin = true;
                                    }
                                    plr.loop = true;
                                    plr.startLoop();
                                    Thread.sleep(1000);
                                } else {
                                    System.exit(0);
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Error: " + ex);
                }
            } else {
                //Game Loop
                Frame_Rate = 25;
                try {
                    //Get Current State
                    w = this.getWidth();
                    h = this.getHeight();
                    sbase = h / 8;

                    b.usp = h / 16;

                    //Update Ball's Position
                    b.y += b.dy;
                    b.x += b.dx;
                    b.px = b.x;
                    b.py = b.y;

                    //Function for Speed Power Up
                    if (counter > 0) {
                        if (counter == icounter) {
                            speed.Playfx();
                            System.out.println("Speed UP");
                            System.out.println("speed now = " + Pspeed);
                            System.out.println();
                        }

                        counter--;
                        if ((counter % Frame_Rate) == 0) {
                            Pspeed -= dPspeed;
                            System.out.println("speed now = " + Pspeed);
                            System.out.println();
                        }

                    }

                    //Respawning Platforms
                    for (pip = 0; pip < pl; pip++) {
                        p[pip].px = p[pip].x;
                        p[pip].py = p[pip].y;

                        p[pip].y -= Pspeed;
                        if ((score > 0) && ((score % 100) == 0) && pupg) {
                            Pspeed += 1;
                            cPspeed += 1;
                            pupg = false;
                        }

                        if (plp && (p[pip].y <= (sbase + (b.rad * 2))) && (p[pip].y >= (sbase + (b.rad * 2) - Pspeed)) && (p[pip].id == pid)) {
                            b.y = p[pip].y + p[pip].h + b.rad;
                        }

                        if (p[pip].y < sbase) {

                            rand = r.nextInt(3) * 2;

                            randseed =
                                    r.nextInt(20);
                            if (randseed == 0) {
                                rand2 = 1;
                            } else if (randseed == 5) {
                                rand2 = 2;
                            } else {
                                rand2 = 0;
                            }

                            //Make Sure Platforms are not too close
                            corr = 0;
                            for (pipi = 0; pipi < pl; pipi++) {
                                if ((((h - Threshold) - (p[pipi].y + p[pipi].h)) < ((b.rad * 5) / 2)) && (((h - Threshold) - (p[pipi].y + p[pipi].h)) >= (-1 * ((p[pipi].y + p[pipi].h) + (b.rad * 2)))) && (p[pipi].x == ((rand * w) / 5))) {
                                    corr = ((b.rad * 5) / 2) - ((h - Threshold) - (p[pipi].y + p[pipi].h));
                                    System.out.println("Correction = " + corr);
                                    break;
                                }
                            }

                            p[pip].x = ((rand * w) / 5);
                            p[pip].y = h - Threshold + corr;
                            p[pip].ch = rand2;
                            p[pip].id = id;
                            id++;
                            if (id > idl) {
                                id = 0;
                            }
                        }
                    }

                    //Make Ball Move with the platform if landed on it
                    if (plp) {
                        b.y -= Pspeed;
                    }

                    //Check Keyboard State
                    for (kic = 0; kic < kl; kic++) {
                        if (keyLog[kic] == 0) {
                            continue;
                        }

                        if (keyLog[kic] == KeyEvent.VK_LEFT) {
                            b.dx = -b.sp;
                        }

                        if (keyLog[kic] == KeyEvent.VK_RIGHT) {
                            b.dx = b.sp;
                        }

                        if (keyLog[kic] == KeyEvent.VK_UP || keyLog[kic] == KeyEvent.VK_SPACE) {
                            if (b.og) {
                                jump.Playfx();
                                b.dy = -b.usp;
                            }

                        }
                    }

                    //Prevent Ball from moving out of the screen
                    if (((b.x + b.dx) < 0) || ((b.x + b.dx) > w)) {
                        b.dx = 0;
                    }

                    //Prevent Ball from moving into the Score Section
                    if ((b.y + b.dy) < (sbase + b.rad)) {
                        b.y = (sbase + b.rad);
                        b.dy = 0;
                    }

                    //Add Friction
                    if (b.og) {
                        //If landed add ground friction
                        if (b.dx < 0) {
                            b.dx += Fric;
                            if (b.dx > 0) {
                                b.dx = 0;
                            }

                        } else if (b.dx > 0) {
                            b.dx -= Fric;
                            if (b.dx < 0) {
                                b.dx = 0;
                            }
                        }
                    } else {
                        //If flying add air friction
                        if (b.dx < 0) {
                            b.dx += airFric;
                            if (b.dx > 0) {
                                b.dx = 0;
                            }

                        } else if (b.dx > 0) {
                            b.dx -= airFric;
                            if (b.dx < 0) {
                                b.dx = 0;
                            }

                        }
                    }

                    //Handles Platform - Ball Interactions
                    for (pip = 0; pip < pl; pip++) {
                        //Handles Ball landing or bouncing from platform
                        if (((b.y + b.dy) >= (p[pip].y - b.rad)) && (b.y <= p[pip].y) && (b.x >= (p[pip].x - (b.rad / 2))) && (b.x <= (p[pip].x + p[pip].w + (b.rad / 2)))) {
                            plp = b.og = true;

                            if (b.dy != 0) {
                                //Add Bounce
                                land.Playfx();
                                b.dy /= (-1 * loss);

                                if (b.dy == 0) {
                                    b.y = p[pip].y - b.rad;
                                }

                            }

                            //Give score only if the platform landed is not same
                            if (p[pip].id != pid) {
                                pid = p[pip].id;
                                pscore = score;

                                if (p[pip].ch == 1) {
                                    extra.Playfx();
                                    score += dsscore;
                                    pupg = true;
                                } else {
                                    if (p[pip].ch == 2) {
                                        counter = icounter;
                                        Pspeed = cPspeed + iPspeed;
                                    }

                                    score += dscore;
                                    pupg = true;
                                }

                            }
                            break;
                        } else {
                            //Tells game that ball is in air
                            plp = b.og = false;
                        }

                        //Handles Ball's interaction when hitting it from below
                        if (((b.y + b.dy) <= (p[pip].y + p[pip].h + b.rad)) && (b.y >= (p[pip].y + p[pip].h + b.rad)) && (b.x >= (p[pip].x - (b.rad / 2))) && (b.x <= (p[pip].x + p[pip].w + (b.rad / 2)))) {
                            b.y = (p[pip].y + p[pip].h + b.rad);
                            b.dy = 0;
                            break;

                        }
                    }

                    //Handles Ball when falling into the death zone
                    if ((b.y + b.dy) >= (h - b.rad) && (!b.og)) {
                        b.og = true;
                        if (b.dy != 0) {
                            rand3 = r.nextInt(pl);
                            b.y = p[rand3].y - b.rad;
                            b.x = p[rand3].x + (p[rand3].w / 2);
                            b.dy = 0;
                        }
                        lives--;
                        die.Playfx();
                        if (lives < 0) {
                            menu = true;
                            dead = true;
                            Pspeed = 2;
                            begin = false;
                            plr.stopLoop();
                            //Initalize Platforms
                            for (pi = 0; pi < pl; pi++) {
                                p[pi] = new Platform((((pi % 3) * 2 * w) / 5), ((pi + 1) * h) / (pl + 1), (w / 5), 20, 0);
                                p[pi].id = id;
                                id++;
                            }
                        }
                    } else {
                        if (!plp) {
                            b.og = false;
                        }

                    }

                    if (!b.og) {
                        b.dy += g;
                    }

                    if (pog != b.og) {
                        System.out.println("On Ground : " + b.og);
                        System.out.println("On Platform : " + plp);
                        System.out.println();
                        pog = b.og;
                    }
                } catch (Exception ex) {
                    System.out.println("Error: " + ex);
                }
            }
            try {
                this.repaint();
                Thread.sleep(1000 / Frame_Rate);
            } catch (Exception ex) {
                System.out.println("Error: " + ex);
            }
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        //Handles and logs Key Presses
        for (kip = 0; kip < kl; kip++) {
            if (keyLog[kip] == 0) {
                kicb = false;
                for (kicc = 0; kicc < kl; kicc++) {
                    if (keyLog[kicc] == e.getKeyCode()) {
                        kicb = true;
                        break;

                    }
                }
                if (!kicb) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        System.out.println("left press");
                    }

                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        System.out.println("right press");
                    }

                    if (e.getKeyCode() == KeyEvent.VK_UP) {
                        System.out.println("up press");
                    }

                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        System.out.println("down press");
                    }
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        System.out.println("enter press");
                    }
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        System.out.println("space press");
                    }
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        System.out.println("esc press");
                    }

                    keyLog[kip] = e.getKeyCode();
                }

                break;
            }

        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            menu = true;
            plr.stopLoop();
        }

    }

    public void keyReleased(KeyEvent e) {
        //Handles and logs Key Releases
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            System.out.println("left release");
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            System.out.println("right release");
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            System.out.println("up release");
            if (menu) {
                if (kup) {
                    kup = false;
                }
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            System.out.println("down release");
            if (menu) {
                if (kdn) {
                    kdn = false;
                }
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            System.out.println("enter release");
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            System.out.println("space release");
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("esc release");
        }

        for (kir = 0; kir < kl; kir++) {
            if (keyLog[kir] == e.getKeyCode()) {
                keyLog[kir] = 0;
                break;

            }
        }
    }
}

class Physics {

    JFrame win;
    Cont con;
    Container c;
    GridLayout gl;
    Thread timer;

    public Physics() {
        //Initialises and sets the working window
        win = new JFrame("Super Ball");
        win.setUndecorated(true);
        win.setSize(700, 500);
        win.getGraphicsConfiguration().getDevice().setFullScreenWindow(win);
        win.setResizable(false);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setVisible(true);

        gl = new GridLayout(1, 1);
        con = new Cont();
        c = new Container();
        c.setLayout(gl);
        c.add(con);

        win.setContentPane(c);
        c.setSize(win.getWidth(), win.getHeight());
        con.setSize(win.getWidth(), win.getHeight());

        timer = new Thread(con);
        timer.start();
    }
}

public class Main {

    public static void main(String[] args) {
        //Start the program
        Physics phy = new Physics();
    }
}