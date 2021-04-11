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

	public Bullet(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}

	public void tick() {
		x += dx * speed;
		y += dy * speed;
		
		distance++;
		if(distance == totalDistance || !World.isFree((int)x, (int)y)) {
			Game.bullets.remove(this);
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval((int)(this.getX() - Camera.x), (int)(this.getY() - Camera.y), 2, 2);
	}
	
}
