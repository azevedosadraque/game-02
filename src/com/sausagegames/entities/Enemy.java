package com.sausagegames.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.sausagegames.main.Game;
import com.sausagegames.sounds.Sound;
import com.sausagegames.world.Camera;
import com.sausagegames.world.World;

public class Enemy extends Entity {

	private double speed = 0.85;

	private int maskX = 8;
	private int maskY = 8;
	private int maskW = 10;
	private int maskH = 10;

	private int frames = 0;
	private int maxFrames = 20;
	private int index = 0;
	private int maxIndex = 2;

	private final double VISION = 100;
	private boolean canMove = false;

	public BufferedImage[] sprites;

	private int life = 25;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);

		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(112 + 16, 16, 16, 16);
	}

	public void tick() {

		seePlayer();

		if (isCollidingWithPlayer() == false) {
			if ((int) x < Game.player.getX() && World.isFree((int) (x + speed), (int) this.getY())
					&& !isColliding((int) (x + speed), (int) this.getY()) && canMove) {
				x += speed;
			} else if ((int) x > Game.player.getX() && World.isFree((int) (x - speed), (int) this.getY())
					&& !isColliding((int) (x - speed), (int) this.getY()) && canMove) {
				x -= speed;
			} else {
				if ((int) y < Game.player.getY() && World.isFree((int) (this.getX()), (int) (y + speed))
						&& !isColliding((int) (this.getX()), (int) (y + speed)) && canMove) {
					y += speed;
				}
			}

			if ((int) y < Game.player.getY() && World.isFree((int) (this.getX()), (int) (y + speed))
					&& !isColliding((int) (this.getX()), (int) (y + speed)) && canMove) {
				y += speed;
			} else if ((int) y > Game.player.getY() && World.isFree((int) (this.getX()), (int) (y - speed))
					&& !isColliding((int) (this.getX()), (int) (y - speed)) && canMove) {
				y -= speed;
			}
		} else {

			if (Game.rand.nextInt(100) < 15) {
				Player.life = ((Player.life -= 2) < 1) ? 0 : (Player.life -= 2);
				Game.player.isDamaged = true;
				if(Player.life == 0) {
					Sound.ouchDeathPlayer.play();
				}
			}
		}

		frames++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index == maxIndex) {
				index = 0;
			}
		}

		collidingBullet();

		if (life <= 20 && life > 15) {
			sprites[0] = Game.spritesheet.getSprite(96, 48, 16, 16);
			sprites[1] = Game.spritesheet.getSprite(112, 48, 16, 16);
		} else if (life <= 15 && life > 10) {
			sprites[0] = Game.spritesheet.getSprite(96, 48 + 16, 16, 16);
			sprites[1] = Game.spritesheet.getSprite(112, 48 + 16, 16, 16);
		} else if (life <= 10 && life > 5) {
			sprites[0] = Game.spritesheet.getSprite(96, 48 + 32, 16, 16);
			sprites[1] = Game.spritesheet.getSprite(112, 48 + 32, 16, 16);
		} else if (life < 1) {
			Sound.enemyDeath.play();
			Game.enemies.remove(this);
			Game.entities.remove(this);
			Game.enemiesDestroyed++;
			return;
		}
	}

	private void seePlayer() {

		var player = Game.player;

		if (player.getX() > x && player.getY() > y) {
			if ((x + VISION) > player.getX() && (y + VISION) > player.getY()) {
				canMove = true;
			}
		}
		
		if (player.getX() > x && player.getY() < y) {
			if ((x + VISION) > player.getX() && (y - VISION) < player.getY()) {
				canMove = true;
			}
		}
		
		if (player.getX() < x && player.getY() < y) {
			if ((x - VISION) < player.getX() && (y - VISION) < player.getY()) {
				canMove = true;
			}
		}
		
		if (player.getX() < x && player.getY() > y) {
			if ((x - VISION) < player.getX() && (y + VISION) > player.getY()) {
				canMove = true;
			}
		}

	}

	public void collidingBullet() {
		for (int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			if (Entity.isColliding(this, e)) {

				life -= 5;
				
				Sound.enemyImpact.play();
				
				Game.bullets.remove(e);
				return;
			}
		}
	}

	public boolean isCollidingWithPlayer() {
		var enemyCurrent = new Rectangle((int) this.getX() + maskX, (int) this.getY() + maskY, maskW, maskH);
		var player = new Rectangle((int) Game.player.getX(), (int) Game.player.getY(), 16, 16);

		return enemyCurrent.intersects(player);
	}

	public boolean isColliding(int xNext, int yNext) {
		var enemyCurrent = new Rectangle(xNext + maskX, yNext + maskY, maskW, maskH);

		for (int i = 0; i < Game.enemies.size(); i++) {
			var e = Game.enemies.get(i);
			if (e == this) {
				continue;
			}

			var targetEnemy = new Rectangle((int) e.getX() + maskX, (int) e.getY() + maskY, maskW, maskH);

			if (enemyCurrent.intersects(targetEnemy)) {
				return true;
			}

		}

		return false;
	}

	public void render(Graphics g) {
		g.drawImage(sprites[index], (int) this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
	}

	public boolean isCanMove() {
		return canMove;
	}

	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}

}
