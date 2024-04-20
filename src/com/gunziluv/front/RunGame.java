package com.gunziluv.front;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class RunGame {
	
	public RunGame() {
		BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB); //커서를 나타내기위해
		Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0,0), "blank"); // 마우스커서를 생성
		Display game = new Display();
		JFrame frame = new JFrame();
		frame.add(game);
		frame.setSize(Display.getGameWidth(), Display.getGameHeight());
		//frame.getContentPane().setCursor(blank); //커서를 숨김.
		frame.setTitle(Display.TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //윈도우창종료시 프로세스도 함께종료
		frame.setLocationRelativeTo(null);
		frame.setResizable(false); //창크기변경불가
		frame.setVisible(true); //보이게
		
		game.start();
		stopMenuThread();
	}
	
	private void stopMenuThread() {
		Display.getLauncherInstance().stopMenu();
	}

}
