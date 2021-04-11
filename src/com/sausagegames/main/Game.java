package com.sausagegames.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.sausagegames.entities.Ammo;
import com.sausagegames.entities.Bullet;
import com.sausagegames.entities.Enemy;
import com.sausagegames.entities.Entity;
import com.sausagegames.entities.LifePack;
import com.sausagegames.entities.Player;
import com.sausagegames.graficos.Spritesheet;
import com.sausagegames.graficos.UI;
import com.sausagegames.world.Camera;
import com.sausagegames.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	private final int SCALE = 3;

	private int CURRENT_LEVEL = 1;
	private int MAX_LEVEL = 2;
	private BufferedImage image;

	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<LifePack> lifePacks;
	public static List<Ammo> ammos;
	public static List<Bullet> bullets;
	
	public static Spritesheet spritesheet;
	public static World world;
	public static Player player;
	public static Random rand;
	
	public static int enemiesDestroyed = 0;
	public static int frameGun;
	public static int frameEnemy;

	public UI ui;

	public Game() {
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();

		carregarMundo("inicio");

	}

	private void carregarMundo(String level) {
		if(level != "inicio") {
			entities.clear();
			enemies.clear();
			lifePacks.clear();
			ammos.clear();
			bullets.clear();			
		}
		
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		lifePacks = new ArrayList<LifePack>();
		ammos = new ArrayList<Ammo>();
		bullets = new ArrayList<Bullet>();
		spritesheet = new Spritesheet("/spritesheet.png");
		ui = new UI();
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		
		if(level == "inicio") {
			//world = new World("/level1.png");	
			world = new World("/teste.png");	
		}else {
			world = new World(level);	
		}

		Player.life = 100;
		Player.ammo = 0;
		enemiesDestroyed = 0;
		
	}

	private void initFrame() {
		frame = new JFrame("Meu Jogo");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String args[]) {
		Game game = new Game();
		game.start();

	}

	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void tick() {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
		
		if(enemiesDestroyed > 40) {
			enemiesDestroyed = 0;
			CURRENT_LEVEL++;
			if(CURRENT_LEVEL > MAX_LEVEL) {
				CURRENT_LEVEL = 1;
			}
			String newWorld = "/level" + CURRENT_LEVEL + ".png";
			carregarMundo(newWorld);
		}

		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).tick();
		}
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = image.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		/*
		 * Graphics2D g2 = (Graphics2D) g; g2.setColor(new Color(0, 0, 0, 45));
		 */

		world.render(g);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}

		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}

		ui.render(g);

		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		bs.show();

	}

	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		frameGun = 0;
		frameEnemy = 0;
		while (isRunning) {

			if (Player.life == 0) {
				carregarMundo("/level1.png");
			}
			
			world.tick();

			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
				
				frameGun++;
				frameEnemy++;
			}

			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS 	" + frames);
				frames = 0;
				timer += 1000;
			}
			
			if(frameGun > 1000) {
				frameGun = 30;
			}
			
			if(frameEnemy > 250) {
				frameEnemy = 121;
			}
		}

		stop();

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_X) {
			player.shoot = true;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_X) {
			player.shoot = false;
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseShoot = true;
		
		player.mx = (e.getX() / 3);
		player.my = (e.getY() / 3);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		player.mouseShoot = false;
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		player.mx = (e.getX() / 3);
		player.my = (e.getY() / 3);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

}