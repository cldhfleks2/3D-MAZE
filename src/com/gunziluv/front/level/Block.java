package com.gunziluv.front.level;

import java.util.ArrayList;
import java.util.List;

import com.gunziluv.graphics.Sprite;

public class Block {
	
	public boolean solid = false;
	
	public static Block solidWall = new SolidBlock();

	public List<Sprite> sprites = new ArrayList<>();
	
	public void addSprite(Sprite sprite) {
		sprites.add(sprite);
	}
	
	
}
