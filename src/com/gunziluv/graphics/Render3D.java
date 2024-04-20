package com.gunziluv.graphics;

import com.gunziluv.front.Game;
import com.gunziluv.front.level.Block;
import com.gunziluv.front.level.Level;
import com.gunziluv.input.Controller;

public class Render3D extends Render{
	
	public double[] zBuffer;
	public double[] zBufferWall;
	private double renderDistance = 5000;
	private double forward, right, cosine, sine, up, walking;
	

	public Render3D(int width, int height) {
		super(width, height);
		zBuffer = new double[width*height];
		zBufferWall = new double[width];
	}
	
	
	public void floor(Game game) {
		for(int x = 0 ; x < width ; x++) {
			zBufferWall[x] = 0;
		}
		
		double floorPosition = 8; //바닥높이 
		double ceilingPosition = 8; //천장높이 값이높으면 천장을 없앨수 있음.
		forward = game.controls.z;
		right = game.controls.x;
		up = game.controls.y;
		walking = 0;
		double rotation = game.controls.rotation;
		
		cosine =  Math.cos(rotation);
		sine = Math.sin(rotation);

		for(int y = 0 ; y < height ; y++) {
			double ceiling = (y - height / 2.0) / height;			
			
			double z = (floorPosition + up) / ceiling;
			
			if(Controller.walk) {
				walking = Math.sin(game.time / 6.0) * 0.5;
				z = (floorPosition + up + walking) / ceiling;
			}
			if(Controller.crouchWalk && Controller.walk) {
				walking = Math.sin(game.time / 6.0) * 0.25;
				z = (floorPosition + up + walking) / ceiling;
			}
			if(Controller.runWalk && Controller.walk) {
				walking = Math.sin(game.time / 6.0) * 0.8;
				z = (floorPosition + up + walking) / ceiling;
			}
			
			
			
			if(ceiling < 0) {
				z = (ceilingPosition - up) / -ceiling; // 위아래 같은방향으로 움직이게.
				if(Controller.walk) z = (ceilingPosition - up - walking) / -ceiling;
			}
			//이부분이없으면 바닥과 천장의 움직이는 방향이 대칭이됨.

			
			//바닥과천장을 렌더링하는부분
			for(int x = 0 ; x < width ; x++) {
				double depth = (x - width / 2.0) / height;
				depth *= z;
				//sine 과 cosine을 합쳐야 원으로 회전가능
				double xx = depth * cosine + z * sine; 
				double yy = z * cosine - depth * sine;
				int xPix = (int) (xx + right);
				int yPix = (int) (yy + forward);
				zBuffer[x + y*width] = z; // z는 깊이값 깊이를 저장
//				pixels[x + y * width] = ((xpix & 15) * 16) | ((ypix & 15) * 16) << 8; // xpix와 ypix값이 0~15 로 조절됨. & 15에의해서.. 원리는모르겟음
				pixels[x + y * width] = Texture.floor.pixels[(xPix & 7) + (yPix & 7) * 16]; //현재텍스쳐의 width가 16이므로 마지막에16곱
				
				if(z > 500)
					pixels[x + y * width] = 0;
	
			}
		}
		
		//블럭생성//////////////////
		Level level = game.level;
		int size = 20; //생성되는공간의 사이즈.
		for(int xBlock = -size; xBlock <= size ; xBlock++) {
			for(int zBlock = -size; zBlock <= size ; zBlock++) {
				Block block = level.create(xBlock, zBlock);
				Block east = level.create(xBlock + 1, zBlock);
				Block south = level.create(xBlock, zBlock + 1);
				
				if(block.solid) {
					if(!east.solid) {
						renderWall(xBlock + 1, xBlock + 1, zBlock, zBlock+1, 0.5);
					}
					if(!south.solid) {
						renderWall(xBlock + 1, xBlock, zBlock + 1, zBlock+1, 0.5);
					}
				}else {
					if(east.solid) {
						renderWall(xBlock + 1, xBlock + 1, zBlock + 1 , zBlock, 0.5);
					}
					if(south.solid) {
						renderWall(xBlock, xBlock + 1, zBlock + 1 , zBlock + 1, 0.5);
					}

				}
			}
		}
		
		for(int xBlock = -size; xBlock <= size ; xBlock++) {
			for(int zBlock = -size; zBlock <= size ; zBlock++) {
				Block block = level.create(xBlock, zBlock);
				
				for(int s = 0; s < block.sprites.size(); s++) {
					Sprite sprite = block.sprites.get(s);
					renderSprite(xBlock + sprite.x, sprite.y, zBlock + sprite.z);
				}
			}
		}

			
	}
	
	public void renderSprite(double x, double y, double z) {
		double upCorrect = -0.125;
		double rightCorrect = 0.0625;
		double forwardCorrect = 0.0625;
		double walkCorrect = 0.0625;
		
		double xc = ((x / 2) - (right * rightCorrect)) * 2;
		double yc = ((y / 2) - (up * upCorrect)) + (walking * walkCorrect) * 2;
		double zc = ((z / 2) - (forward * forwardCorrect)) * 2;
		
		double rotX = xc * cosine - zc * sine;
		double rotY = yc;
		double rotZ = zc * cosine + xc * sine;
		
		double xCentre = 400.0;
		double yCentre = 300.0;
		
		double xPixel = (rotX / rotZ * height + xCentre);
		double yPixel = (rotY / rotZ * height + yCentre);
		
		double xPixelL = xPixel - 32 / rotZ;
		double xPixelR = xPixel + 32 / rotZ;
		
		double yPixelL = yPixel - 32 / rotZ;
		double yPixelR = yPixel + 32 / rotZ;
		
		int xpl = (int) xPixelL;
		int xpr = (int) xPixelR;
		int ypl = (int) yPixelL;
		int ypr = (int) yPixelR;
		
		if(xpl < 0) xpl = 0;
		if(xpr > width) xpr = width;
		if(ypl < 0) ypl = 0;
		if(ypr > height) ypr = height;
		
		rotZ *= 8;
		
		for(int yp = ypl; yp < ypr; yp++) {
			double pixelRotationY = (yp - yPixelR) / (yPixelL - yPixelR);
			int yTexture = (int) pixelRotationY * 8;
			//int yTexture = (int) (8 * pixelRotationY); //블럭에 붙일 텍스쳐
			for(int xp = xpl; xp < xpr; xp++) {
				double pixelRotationX = (xp - xPixelR) / (xPixelL - xPixelR);
				int xTexture = (int) pixelRotationX * 8;
				if(zBuffer[xp + yp * width] > rotZ) {
					pixels[xp + yp * width] = xTexture * 32 + yTexture * 32 * 256;//Texture.floor.pixels[(xTexture & 7) + (yTexture & 7) * 16];
					zBuffer[xp + yp * width] = rotZ;
				}
			}
		}

	}
	
	public void renderWall(double xLeft, double xRight, double zDistanceLeft, double zDistanceRight, double yHeight) {
		double upCorrect = 0.0625;
		double rightCorrect = 0.0625;
		double forwardCorrect = 0.0625;
		double walkCorrect = -0.0625;
		
		double xcLeft = ((xLeft / 2) - (right * rightCorrect)) * 2;
		double zcLeft = ((zDistanceLeft / 2) - (forward * forwardCorrect)) * 2;
		
		double rotLeftSideX = xcLeft * cosine - zcLeft * sine;
		double yCornerTL = ((-yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
		double yCornerBL = ((+0.5 - yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
		double rotLeftSideZ = zcLeft * cosine + xcLeft * sine;
		//////////////////////////////////////////////////////
		//위 Left                                       아래 Right///
		/////////////////////////////////////////////////////////////
		double xcRight = ((xRight / 2) - (right * rightCorrect)) * 2;
		double zcRight = ((zDistanceRight / 2) - (forward * forwardCorrect)) * 2;
		
		double rotRightSideX = xcRight * cosine - zcRight * sine;
		double yCornerTR = ((-yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
		double yCornerBR = ((+0.5 - yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
		double rotRightSideZ = zcRight * cosine + xcRight * sine;
		
		double tex30 = 0;
		double tex40 = 8;
		double clip = 0.5;
		
		//클리핑소스///////////////////
		//cohen sutherland 알고리즘사용////////////////////////////
		if(rotLeftSideZ < clip && rotRightSideZ < clip) return;
		
		if(rotLeftSideZ < clip) {
			double clip0 = (clip - rotLeftSideZ) / (rotRightSideZ - rotLeftSideZ);
			rotLeftSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
			rotLeftSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * clip0;
			tex30 = tex30 + (tex40 - tex30) * clip0;
		}
		
		if(rotRightSideZ < clip) {
			double clip0 = (clip - rotLeftSideZ) / (rotRightSideZ - rotLeftSideZ);
			rotRightSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
			rotRightSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * clip0;
			tex40 = tex30 + (tex40 - tex30) * clip0;
		}///////////////////////////////////////////
		
		double xPixelLeft = (rotLeftSideX / rotLeftSideZ * height + width / 2);
		double xPixelRight = (rotRightSideX / rotRightSideZ * height + width / 2);
		
		if(xPixelLeft >= xPixelRight) return;
		int xPixelLeftInt = (int) (xPixelLeft);
		int xPixelRightInt = (int) (xPixelRight);
		
		if(xPixelLeftInt < 0) xPixelLeftInt = 0;
		if(xPixelRightInt > width) xPixelRightInt = width;
		//////////////////////////////////////////////////
		
		double yPixelLeftTop = (yCornerTL / rotLeftSideZ * height + height / 2.0);
		double yPixelLeftBottom = (yCornerBL / rotLeftSideZ * height + height / 2.0);
		double yPixelRightTop = (yCornerTR / rotRightSideZ * height + height / 2.0);
		double yPixelRightBottom = (yCornerBR / rotRightSideZ * height + height / 2.0);
		
		double tex1 = 1 / rotLeftSideZ;
		double tex2 = 1 / rotRightSideZ;
		double tex3 = tex30 / rotLeftSideZ;
		double tex4 = tex40 / rotRightSideZ - tex3;
		
		for(int x = xPixelLeftInt; x < xPixelRightInt; x++) {
			double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);
			
			//블럭뒤의 블럭을 가려줌//////////////////////////////////////
			double zWall = (tex1 + (tex2 - tex1) * pixelRotation);
			
			if(zBufferWall[x] > zWall) continue;
			
			zBufferWall[x] = zWall;
			///////////////////////

			int xTexture = (int) ((tex3 + tex4 * pixelRotation) / zWall);//블럭에 붙일 텍스쳐
			
			double yPixelTop = yPixelLeftTop + (yPixelRightTop - yPixelLeftTop) * pixelRotation;
			double yPixelBottom = yPixelLeftBottom + (yPixelRightBottom - yPixelLeftBottom) * pixelRotation;
			
			int yPixelTopInt = (int) (yPixelTop);
			int yPixelBottomInt = (int) (yPixelBottom);
			
			if(yPixelTopInt < 0) yPixelTopInt = 0;
			if(yPixelBottomInt > height) yPixelBottomInt = height;
			
			for(int y = yPixelTopInt; y < yPixelBottomInt; y++) {
				double pixelRotationY = (y - yPixelTop) / (yPixelBottom - yPixelTop);
				int yTexture = (int) (8 * pixelRotationY); //블럭에 붙일 텍스쳐
				//pixels[x + y * width] = xTexture * 100 + yTexture * 100 * 256;
				//텍스쳐의 x 의 시작점이 8번째픽셀부터~ 16픽셀까지 y는 그대로 0번째부터끝까지
				pixels[x + y * width] = Texture.floor.pixels[((xTexture & 7) + 8) + (yTexture & 7) * 16];
				zBuffer[x + y * width] = 1 / (tex1 + (tex2 - tex1) * pixelRotation) * 8; 
			}
		}
		
	}

	//밝기조절로 렌더링깊이또한 조절.
	public void renderDistanceLimiter() {
		for(int i = 0 ; i < width * height ; i++) {
			int colour = pixels[i];
			int brightness = (int) (renderDistance / zBuffer[i]);
			
			if(brightness < 0) brightness = 0; //최소
			if(brightness > 255) brightness = 255; //최대
			
			int r = (colour >> 16) & 0xff;
			int g = (colour >> 8) & 0xff;
			int b = (colour) & 0xff;
			
			r = r * brightness / 255;
			g = g * brightness / 255;
			b = b * brightness / 255;
			
			pixels[i] = r << 16 | g << 8 | b;
			
		}
	}
	
}
