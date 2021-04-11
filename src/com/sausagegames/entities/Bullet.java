package com.sausagegames.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.sausagegames.main.Game;
import com.sausagegames.world.Camera;
import com.sausagegames.world.World;

public class Bullet extends Entity {

	private double dx;
	private double dy;
	private double speed = 4;

	private int totalDistance = 40;
	private int distance = 0;
	
	private boolean wallUp = false;
	private boolean wallDown = false;
	private boolean wallRight = false;
	private boolean wallLeft = false;

	public Bullet(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}

	public void tick() {
		x += dx * speed;
		y += dy * speed;

		var isFree = collingWall();

		distance++;
		if (distance == totalDistance || isFree) {
			Game.bullets.remove(this);
		}
	}

	private boolean collingWall() {

		var p = Game.player;

		detectWall();

		if (p.mx > (Game.WIDTH / 2) && p.my > (Game.HEIGHT / 2)) {
			return !World.isFree((int) (x - 12), (int) (y - 12));
		}

		if (p.mx < (Game.WIDTH / 2) && p.my > (Game.HEIGHT / 2)) {
			return !World.isFree((int) (x), (int) (y - 12));
		}

		if (p.mx < (Game.WIDTH / 2) && p.my < (Game.HEIGHT / 2)) {
			return !World.isFree((int) (x), (int) (y));
		}

		if (p.mx > (Game.WIDTH / 2) && p.my < (Game.HEIGHT / 2)) {
			return !World.isFree((int) (x - 12), (int) (y));
		}
		
		return false;
	}

	private void detectWall() {
		var px = Game.player.getX();
		var py = Game.player.getY();
		
		wallUp = !World.isFree((int) (Game.player.getX()), (int) (Game.player.getY() - 1));
		wallDown = !World.isFree((int) (Game.player.getX()), (int) (Game.player.getY() + 1));
		wallRight = !World.isFree((int) (Game.player.getX() + 1), (int) (Game.player.getY()));
		wallLeft = !World.isFree((int) (Game.player.getX() - 1), (int) (Game.player.getY()));

	}

	public void render(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval((int) (this.getX() - Camera.x), (int) (this.getY() - Camera.y), 2, 2);
	}

}
