package com.sausagegames.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.sausagegames.main.Game;
import com.sausagegames.world.Camera;

public class Entity {
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(6*16, 0, 16, 16 );
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(7*16, 0, 16, 16 );
	public static BufferedImage AMMO_EN = Game.spritesheet.getSprite(6*16, 16, 16, 16 );
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(7*16, 16, 16, 16 );
	public static BufferedImage WEAPON_EN_LEFT = Game.spritesheet.getSprite(8*16, 0, 16, 16 );

	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	protected BufferedImage sprite;
	
	private static int maskX;
	private static int maskY;
	private int mWidth;
	private int mHeight;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskX = 0;
		this.maskY = 0;
		this.mWidth = width;
		this.mHeight = height;
	}
	
	public void setMask(int maskX, int maskY, int mWidth, int mHeight) {
		this.maskX = maskX;
		this.maskY = maskY;
		this.mWidth = mWidth;
		this.mHeight = mHeight;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void render(Graphics g) {
		g.drawImage(sprite, (int)this.getX() - Camera.x, (int)this.getY() - Camera.y,  null);
	}

	public void tick() {
		
	}
	
	public static boolean isColliding(Entity e1, Entity e2) {
		var e1Mask = new Rectangle((int)e1.getX() + Entity.maskX, (int)e1.getY() + maskX, e1.mWidth, e1.mHeight );
		var e2Mask = new Rectangle((int)e2.getX() + Entity.maskX, (int)e2.getY() + maskY, e2.mWidth, e2.mHeight );
		
		return e1Mask.intersects(e2Mask);
	}
	
}
