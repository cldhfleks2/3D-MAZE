package com.gunziluv.graphics;
import java.util.Random;
import com.gunziluv.front.Game;

public class Screen extends Render {
	
	private Render test;
	private Render3D render;
	
	//실제로 화면에 그리는 로직
	public Screen(int width , int height) {
		super(width , height);
		Random random = new Random();
		render = new Render3D(width, height);
		test = new Render(256 , 256);
		for(int i = 0 ; i < 256*256 ; i++) {
			test.pixels[i] = random.nextInt() * (random.nextInt(5) / 4); //여기에 넣은값으로 화면을 그림. 
			//render 에서 if(alpha>0)를 없애고 test.pixels[i] = random.nextInt() * (random.nextInt(5) / 4);
			//를하면 투명성이생김
		}
		
	}
	public void render(Game game) {
		//화면에그려진 픽셀을 초기화.
		for(int i = 0 ; i < width*height ; i++) {
			pixels[i] = 0;
		}
		
		//매순간마다 초기화된 픽셀들위에 그림. 
		//i < 200 에서 200 를 i반복수 라고하자.
		
		/*
		 * for(int i = 0 ; i < 50 ; i ++) { //% 늘릴수록 느려지고 i반복수가 클수록 더많은그림자를 가짐. //여기서
		 * i반복수의값자체는 anim, anim2에 각각을곱한것과 같은 결과출력 //i < 200 , anim , anim2 와 같은 결과는 i <
		 * 100 , anim*2 , anim2*2 라는 소리임. 대신에 프로그램이 덜 지연됨. 렉이 덜 걸림. //자바에선 조건문,반복문을
		 * 사용할수록 느려짐. 그래서임 int anim = (int) (Math.sin(game.time + i * 2) % 1000.0 / 100)
		 * * 100; int anim2 = (int) (Math.cos(game.time + i * 2) % 1000.0 /100) * 100;
		 * 
		 * draw(test , ~~~~~) }
		 */

		render.floor(game);
		/*
		 * render.renderWall(0, 0.5, 1.5, 1.5, 0); 
		 * render.renderWall(0, 0, 1, 1.5, 0);
		 * render.renderWall(0, 0.5, 1, 1, 0); 
		 * render.renderWall(0.5, 0.5, 1, 1.5, 0);
		 */
		render.renderDistanceLimiter();
		


		
		draw(render , 0 , 0);
	}
}
