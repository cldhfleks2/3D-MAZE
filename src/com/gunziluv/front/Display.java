package com.gunziluv.front;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.gunziluv.front.gui.Launcher;
import com.gunziluv.graphics.Screen;
import com.gunziluv.input.Controller;
import com.gunziluv.input.InputHandler;

public class Display extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	public static int width = 800;
	public static int height = 600;
	static final String TITLE= "3D PROGRAMMING 0.02";
	
	private Thread thread;
	private Screen screen;
	private Game game;
	private BufferedImage img;
	private boolean rundning = false;
	private int[] pixels;
	private InputHandler input;
	private int newX = 0;
	private int oldX = 0;
	private int fps;
	public static int selection = 0;
	
	public static int MouseSpeed;
	
	static Launcher launcher;
	
	public Display() {
		Dimension size = new Dimension(getGameWidth() , getGameHeight());
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		
		screen = new Screen(getGameWidth(), getGameHeight());
		game = new Game();
		img = new BufferedImage(getGameWidth(), getGameHeight(), BufferedImage.TYPE_INT_RGB); //어떤이미지를
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData(); //배열로 리턴하나봄
		
		input = new InputHandler();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
	}
	
	public static Launcher getLauncherInstance() {
		if(launcher == null) {
			launcher = new Launcher(0);
		}
		return launcher;
	}
	
	public static int getGameWidth() {
		return width;
	}
	
	public static int getGameHeight() {
		return height;
	}
	
	
	public synchronized void start() {
		if(rundning) return;
		rundning = true;
		thread = new Thread(this, "game");
		thread.start();
	}
	
	public synchronized void stop() {
		if(!rundning) return;
		rundning = false;
		try {
			
			thread.join();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	@Override
	public void run() {
		int frames = 0;
		double unprocessedSeconds = 0;
		long previousTime = System.nanoTime();
		double secondsPerTick = 1 / 60.0;
		int tickCount = 0;
		boolean ticked = false;
		
		
		while(rundning) {
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			unprocessedSeconds += passedTime / 1000000000.0;
			//launcher.updateFrame();
			
			//requestFocus();//해야되나
	
			while(unprocessedSeconds > secondsPerTick) {
				tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				if(tickCount % 60 == 0) { //1초마다 frame을 초기화하며 fps기록
					System.out.println(frames + "fps");
					fps = frames;
					previousTime += 1000;
					frames = 0;
				}
				if(ticked) {
					render(); //하나의장면을 그림.
					frames++;
				}
				//render(); //이 장면을 그리는것이 반복되어 fps로 되겠지.
			}
			
			
			
			
		}
		
		
	}
	
	private void tick() {
		game.tick(input.key);
		
		newX = InputHandler.MouseX;
		if(newX > oldX) {
			Controller.turnRight = true;
		}
		if(newX < oldX) {
			Controller.turnLeft = true;
			
		}
		if(newX == oldX) {
			Controller.turnLeft = false;
			Controller.turnRight = false;
		}
		
		MouseSpeed = Math.abs(newX - oldX); //마우스 감도변경시마다 적용
		oldX = newX;
	}
	
	
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3); //3D 게임을 만들기위해 2D면 2개필요?
			return;
		}
		
		screen.render(game);
		
		for(int i = 0 ; i < getGameWidth() * getGameHeight() ; i ++) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, getGameWidth(), getGameHeight(), null); //WIDTH , HEIGHT 에 각각*20 하면 픽셀이 20배크게 그려짐.
		g.setFont(new Font("Verdana", 2, 50));
		g.setColor(Color.cyan);
		g.drawString(fps + " fps", 20, 50);
		g.dispose();
		bs.show();
		
	}
	
	
	public static void main(String[] args) {
		getLauncherInstance();
	}

}
