package com.gunziluv.input;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputHandler implements KeyListener , FocusListener , MouseListener , MouseMotionListener {

	public boolean[] key = new boolean[68836];
	public static int MouseX; //수평
	public static int MouseY; //수직
	public static int MouseDX; //drag X
	public static int MouseDY; //drag Y
	public static int MousePX; //Pressed X
	public static int MousePY; //Pressed Y
	public static int MouseButton;
	public static boolean dragged = false;

	
	@Override
	public void mouseDragged(MouseEvent e) {
		dragged = true;
		MouseDX = e.getX();
		MouseDY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		MouseX = e.getX();
		MouseY = e.getY();
		
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		MouseButton = e.getButton();
		MousePX = e.getX();
		MousePY = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		MouseButton = 0;
		dragged = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void focusGained(FocusEvent e) {
	}

	//키눌린채로 다른작업창으로넘어갈때. 즉 작업창의초점을 잃을때
	@Override
	public void focusLost(FocusEvent e) {
		for(int i = 0 ; i < key.length ; i++) {
			key[i] = false; //키눌림 전부 초기화
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	//키눌렷을때
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode > 0 && keyCode < key.length) {
			key[keyCode] = true;
		}
	}

	//키눌렸다가땠을때
	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode > 0 && keyCode < key.length) {
			key[keyCode] = false;
		}
	}

}
