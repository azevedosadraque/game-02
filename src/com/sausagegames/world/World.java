package com.sausagegames.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sausagegames.entities.*;
import com.sausagegames.main.Game;

public class World {

	public static Tile[] tiles;
	public static int WIDTH;
	public static int HEIGHT;
	public static final int TILE_SIZE = 16;

	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for (int xx = 0; xx < map.getWidth(); xx++) {
				for (int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];

					tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					if (pixelAtual == 0xFF000000) {
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					} else if (pixelAtual == 0xFFFFFFFF) {
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
					} else if (pixelAtual == 0xFF0C20FF) {
						Game.player.setX((int) (xx * 16));
						Game.player.setY((int) (yy * 16));
					} else if (pixelAtual == 0xFFFF0000) {
						var en = new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY_EN);

						Game.entities.add(en);
						Game.enemies.add(en);

					} else if (pixelAtual == 0xFF51FF57) {
						Game.entities.add(new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON_EN));
					} else if (pixelAtual == 0xFFFFF15B) {
						var en = new Ammo(xx * 16, yy * 16, 16, 16, Entity.AMMO_EN);

						Game.entities.add(en);
						Game.ammos.add(en);

					} else if (pixelAtual == 0xFFD559FF) {

						var lifePack = new LifePack(xx * 16, yy * 16, 16, 16, Entity.LIFEPACK_EN);

						Game.entities.add(lifePack);
						Game.lifePacks.add(lifePack);
					}

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isFree(int xNext, int yNext) {
		int x1 = xNext / TILE_SIZE;
		int y1 = yNext / TILE_SIZE;

		int x2 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = yNext / TILE_SIZE;

		int x3 = xNext / TILE_SIZE;
		int y3 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

		int x4 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

		return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));

	}

	public void render(Graphics g) {

		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;

		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);

		for (int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {

				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}

				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
		;
	}

	public void tick() {
		while (Game.enemies.size() < 10 && Game.frameEnemy > 60) {
			Game.frameEnemy = 0;
			//GenerateEnemys(Game.rand.nextInt(4) + 1);
		}
	}

	private void GenerateEnemys(int i) {

		Enemy en;

		switch (i) {
		case 1:
			en = new Enemy(16, 16, 16, 16, Entity.ENEMY_EN);
			break;
		case 2:
			en = new Enemy(16, 280, 16, 16, Entity.ENEMY_EN);
			break;
		case 3:
			en = new Enemy(280, 280, 16, 16, Entity.ENEMY_EN);
			break;
		case 4:
			en = new Enemy(280, 16, 16, 16, Entity.ENEMY_EN);
			break;
		default:
			en = new Enemy(16, 16, 16, 16, Entity.ENEMY_EN);
		}
		
		en.setCanMove(true);
		Game.entities.add(en);
		Game.enemies.add(en);
	}
}
