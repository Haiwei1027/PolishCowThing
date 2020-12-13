
import java.awt.Color;
import java.awt.Canvas;
import java.awt.Graphics;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import java.util.Random;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.awt.Image;

public class Main{
	
	static myFrame frame;
	static myCanvas canvas;
	static Random random;
	static Vector2 winSize = new Vector2(800,450);
	static int fps = 60;
	
	public static void main(String[] args) {
		random = new Random();
		canvas = new myCanvas();
		frame = new myFrame(winSize);
		
		frame.add(canvas);
		playSound("polishcow.wav");
	}
	
	static class myFrame extends JFrame{
		public myFrame(Vector2 size) {
			super.setSize((int)size.x,(int)size.y);
			super.setTitle("Applet Thing (No longer applet thing because idk)");
			super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			super.setResizable(false);
			super.setVisible(true);
		}
	}
	public static synchronized void playSound(String url) {
		  new Thread(new Runnable() {
		  // The wrapper thread is unnecessary, unless it blocks on the
		  // Clip finishing; see comments.
			@Override
		    public void run() {
		      try {
		        Clip clip = AudioSystem.getClip();
		        if(Main.class.getResourceAsStream(url)==null) {
		        	System.out.println("bad");
		        }
		        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
		                Main.class.getResourceAsStream("rsc/"+url));
		        
		        clip.open(inputStream);
		        clip.start(); 
		        
		      } catch (Exception e) {
		        System.err.println(e.getMessage());
		        e.printStackTrace();
		      }
		    }
		  }).start();
		}
	
	static class myCanvas extends Canvas implements Runnable, KeyListener{
		private static int[] keys_held = new int[10];
		public int key_down;
		public int key_up;
		public int key_typed;
		public double delta_time;
		
		static double prev_flash = System.nanoTime() * Math.pow(10, -9);
		static Color bg_color = new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256));
		static Vector2 smile_pos = winSize.div(2);
		static Vector2 smile_velo = new Vector2(2);
		
		public myCanvas() {
			super.addKeyListener(this);
			Thread thread = new Thread(this);
			thread.start();
		}
		@Override
		public void run() {
			long prev_time = System.nanoTime();
			while (true){
				delta_time = (System.nanoTime()-prev_time)*Math.pow(10, -9);
				prev_time = System.nanoTime();
				double start = System.nanoTime() * Math.pow(10, -6);
				repaint();
				double end = System.nanoTime() * Math.pow(10, -6);
				try {
					Thread.sleep(1000/fps-(long)(end-start));
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		@Override
		public void paint(Graphics g) {
			g.setColor(bg_color);
			if (System.nanoTime() * Math.pow(10, -9) - prev_flash > 0.5) {
				bg_color = new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256));
				prev_flash = System.nanoTime() * Math.pow(10, -9);
			}
			g.fillRect(0, 0, (int)winSize.x, (int)winSize.y);
			Image img = createImage(100,100);
			try {
			img = ImageIO.read(new File("rsc/face.png"));
			}catch(IOException e) {
				e.printStackTrace();
			}
			int s = 64;
			g.drawImage(img,(int)smile_pos.x-s/2,(int)smile_pos.y-s/2,s,s,this);
			smile_pos = smile_pos.add(smile_velo);
			if (smile_pos.x > winSize.x-s || smile_pos.x < s/2) {smile_velo.x = -smile_velo.x;}
			if (smile_pos.y > winSize.y-s || smile_pos.y < s/2) {smile_velo.y = -smile_velo.y;}
			g.setColor(Color.black);
			char[] ahh = "Ahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh".toCharArray();
			g.drawChars(ahh, 0, ahh.length, 30, 30);
			char[] keys = new char[10];
			int length = 0;
			for(int i=0;i<10;i++) {
				int keyCode = keys_held[i];
				keys[i] = KeyEvent.getKeyText(keyCode).charAt(0);
			}
			g.drawChars(keys, 0, 10, 30, 50);
		}
		
		public boolean isKeyHeld(int keyCode) {
			for (int key : keys_held) {
				if (keyCode == key) {
					return true;
				}else if (key == 0) {
					return false;
				}
			}
			return false;
		}
		
		@Override
		public void keyTyped(KeyEvent e) {
			key_typed = e.getKeyCode();
		}
		@Override
		public void keyPressed(KeyEvent e) {
			key_down = e.getKeyCode();
			int i=0;
			for (int key : keys_held) {
				if (key == key_down) {
					return;
				}
				else if (key == 0) {
					keys_held[i] = key_down;
					return;
				}
				i++;
			}
			System.arraycopy(keys_held, 0, keys_held, 1, 9);
			keys_held[0] = key_down;
			
		}
		@Override
		public void keyReleased(KeyEvent e) {
			key_up = e.getKeyCode();
			int i=0;
			for (int key : keys_held) {
				if (key == key_up) {
					keys_held[i] = 0;
				}
				i++;
			}
			
		}
	}
}
