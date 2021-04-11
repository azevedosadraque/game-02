package com.sausagegames.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.sausagegames.entities.Player;
import com.sausagegames.main.Game;

public class UI {

	public BufferedImage[] sprites;

	public UI() {

		sprites = new BufferedImage[5];
		sprites[0] = Game.spritesheet.getSprite(0, 16, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(16, 16, 16, 16);
		sprites[2] = Game.spritesheet.getSprite(16, 32, 16, 16);
		sprites[3] = Game.spritesheet.getSprite(0, 32, 16, 16);
		sprites[4] = Game.spritesheet.getSprite(96, 32, 16, 16);
	}

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(18, 6, (int)((Player.life / Player.MAX_LIFE) * 50), 10);
		
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 8));
		g.drawString((int)Player.life + "/"  + (int)Player.MAX_LIFE, 25, 14);
		
		g.setColor(Color.black);
		g.setFont(new Font("arial", Font.BOLD, 12));
		g.drawString("X " + Player.ammo, 205, 150);
		g.drawImage(sprites[4], 193, 134, null);

		if (Player.life > 1) {
			g.drawImage(sprites[HeartSprite()], 0, 0, null);
		}
		
		
	}

	private int HeartSprite() {
		if (Player.life >= 75) {
			return 0;
		}

		if (Player.life >= 50 && Player.life < 75) {
			return 1;
		}

		if (Player.life >= 25 && Player.life < 50) {
			return 2;
		}
		return 3;
	}

}
