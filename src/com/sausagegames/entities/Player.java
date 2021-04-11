package com.sausagegames.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.sausagegames.main.Game;
import com.sausagegames.world.Camera;
import com.sausagegames.world.World;

public class Player extends Entity {

	public boolean right;
	public boolean left;
	public boolean up;
	public boolean down;

	public int right_dir = 1;
	public int left_dir = 0;
	public int up_dir = 2;
	public int dir = right_dir;

	public static double life = 0;
	public static int MAX_LIFE = 100;

	public double speed = 1;

	private int frames = 0;
	private int maxFrames = 5;
	private int index = 0;
	private int maxIndex = 4;
	private boolean moved = false;

	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] upPlayerGun;

	private BufferedImage playerDamage;

	public boolean isDamaged = false;
	public static int ammo = 0;
	public boolean hasGun = false;
	public boolean shoot = false;
	public boolean mouseShoot = false;
	public int mx;
	public int my;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		upPlayer = new BufferedImage[4];
		upPlayerGun = new BufferedImage[4];
		playerDamage = Game.spritesheet.getSprite(0, 48, 16, 16);

		for (int i = 0; i < 4; i++) {

			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 0, 16, 16);
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 16, 16, 16);
			upPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 32, 16, 16);
			upPlayerGun[i] = Game.spritesheet.getSprite(32 + (i * 16), 48, 16, 16);

		}
	}

	public void tick() {
		moved = false;
		if (right && World.isFree((int) (x + speed), (int) y)) {
			moved = true;
			setX(getX() + speed);
			dir = right_dir;
		} else if (left && World.isFree((int) (x - speed), (int) y)) {
			moved = true;
			setX(getX() - speed);
			dir = left_dir;
		}

		if (up && World.isFree((int) x, (int) (y - speed))) {
			moved = true;
			setY(getY() - speed);
			dir = up_dir;
		} else if (down && World.isFree((int) x, (int) (y + speed))) {
			moved = true;
			setY(getY() + speed);
			dir = right_dir;
		}

		if (moved) {
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if (index == maxIndex) {
					index = 0;
				}
			}
		}

		this.checkCollisionLifePack();
		this.checkCollisionAmmo();
		this.checkCollisionGun();

		if (shoot && hasGun && ammo > 0) {
			if (Game.frameGun > 0) {
				 
				Game.frameGun = 0;

				ammo++;

				int dx;
				int px = 0;
				int py = 7;

				if (dir == right_dir) {
					px = 13;
					dx = 1;
				} else {
					px = 1;
					dx = -1;
				}
				var bullet = new Bullet((int) (this.getX() + px), (int) (this.getY() + py), 3, 3, null, dx, 0);
				Game.bullets.add(bullet);
			}

		} else {
			shoot = false;
		}
		
		if (mouseShoot && hasGun && ammo > 0) {
			if (Game.frameGun > 4) {
				
				//ammo--;
				 
				Game.frameGun = 0;

				double dx = 0;
				double dy = 0;
				var px = 0;
				
				var py = 7;
				double angle  = 0;
				
				if (dir == right_dir) {
					angle = Math.atan2(my - (this.getY() + 7 - Camera.y),mx - (this.getX() + 15 - Camera.x));
					px = 15;
				} else {
					angle = Math.atan2(my - (this.getY() + 7 - Camera.y),mx - (this.getX() + 0 - Camera.x));
					px = 0;
				}
				
				dx = Math.cos(angle);
				dy = Math.sin(angle);
				
				var bullet = new Bullet((int) (this.getX() + px), (int) (this.getY() + py), 3, 3, null, dx, dy);
				Game.bullets.add(bullet);
			}

		} else {
			mouseShoot = false;
		}


		Camera.x = Camera.clamp(((int) (this.getX() - Game.WIDTH / 2)), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(((int) (this.getY() - Game.HEIGHT / 2)), 0, World.HEIGHT * 16 - Game.HEIGHT);
	}

	public void render(Graphics g) {
		if (!isDamaged) {
			if (right_dir == dir) {
				g.drawImage(rightPlayer[index], (int) this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
				if (hasGun) {
					g.drawImage(Entity.WEAPON_EN, ((int) this.getX() - Camera.x) + 2,
							((int) this.getY() - Camera.y) - 1, null);
				}
			} else if (left_dir == dir) {
				g.drawImage(leftPlayer[index], (int) this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
				if (hasGun) {
					g.drawImage(Entity.WEAPON_EN_LEFT, ((int) this.getX() - Camera.x) - 2,
							((int) this.getY() - Camera.y) - 1, null);
				}
			} else if (up_dir == dir) {
				if (hasGun) {
					g.drawImage(upPlayerGun[index], ((int) this.getX() - Camera.x) - 2,
							((int) this.getY() - Camera.y) - 1, null);
				} else {
					g.drawImage(upPlayer[index], (int) this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
				}
			}
		} else {
			g.drawImage(playerDamage, (int) this.getX() - Camera.x, (int) this.getY() - Camera.y, null);
			isDamaged = false;
		}
	}

	public void checkCollisionAmmo() {
		for (int i = 0; i < Game.ammos.size(); i++) {
			var atual = Game.ammos.get(i);
			if (Entity.isColliding(this, atual)) {
				ammo += 100;
				Game.ammos.remove(atual);
				Game.entities.remove(atual);
			}
		}
	}

	public void checkCollisionGun() {
		for (int i = 0; i < Game.entities.size(); i++) {
			var atual = Game.entities.get(i);
			if (atual instanceof Weapon) {
				if (Entity.isColliding(this, atual)) {
					hasGun = true;
					Game.entities.remove(atual);
				}
			}
		}
	}

	public void checkCollisionLifePack() {
		for (int i = 0; i < Game.lifePacks.size(); i++) {
			var atual = Game.lifePacks.get(i);
			if (Entity.isColliding(this, atual)) {
				life = (life + 25) > 99 ? 100 : life + 25;
				Game.lifePacks.remove(atual);
				Game.entities.remove(atual);
			}

		}
	}

}
