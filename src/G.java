import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class G extends Applet implements Runnable {
	private static final int width = 400;
	private static final int height = 300;
	private static final int scale = 2;
	

	public void start() {
		new Thread(this).start();
	}

	public void run() {
		setSize(width * scale, height * scale); // For AppletViewer, remove later.
		
		BufferedImage bkg = new BufferedImage(width * scale, height * scale, BufferedImage.TYPE_INT_RGB);
		int[] pixels = ((DataBufferInt) bkg.getRaster().getDataBuffer()).getData();
		
		double shade = 0;
		
		for(int y = 0; y < height * scale; y++) {
			for(int x = 0; x < width * scale; x++) {
				int is = (int) shade;
				pixels[y * (width * scale) + x] = is << 16 | is << 8 | is;
			}
			shade += (150.0 / (height * scale));
		}

		// Set up the graphics stuff, double-buffering.
		BufferedImage screen = new BufferedImage(width * scale, height * scale, BufferedImage.TYPE_INT_RGB);
		Graphics g = screen.getGraphics();
		Graphics appletGraphics = getGraphics();

		// Some variables to use for the fps.
		int tick = 0, fps = 0, acc = 0;
		long lastTime = System.nanoTime();

		// Game loop.
		while (true) {
			long now = System.nanoTime();
			acc += now - lastTime;
			tick++;
			if (acc >= 1000000000L) {
				acc -= 1000000000L;
				fps = tick;
				tick = 0;
			}

			// Update
			// TODO add some update logic here.

			lastTime = now;

			g.setColor(Color.black);
			g.fillRect(0, 0, 800, 600);
			
			// render
			g.drawImage(bkg, 0, 0, this);
			
			g.setColor(Color.white);
			g.drawString("FPS " + String.valueOf(fps), 20, 30);

			// Draw the entire results on the screen.
			appletGraphics.drawImage(screen, 0, 0, this);

			do {
				Thread.yield();
			} while (System.nanoTime() - lastTime < 0);

			if (!isActive()) {
				return;
			}
		}
	}

	public boolean handleEvent(Event e) {
		switch (e.id) {
			case Event.KEY_PRESS:
			case Event.KEY_ACTION:
				// key pressed
				break;
			case Event.KEY_RELEASE:
				// key released
				break;
			case Event.MOUSE_DOWN:
				// mouse button pressed
				break;
			case Event.MOUSE_UP:
				// mouse button released
				break;
			case Event.MOUSE_MOVE:
				break;
			case Event.MOUSE_DRAG:
				break;
		}
		return false;
	}
}