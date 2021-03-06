package org.esialb.edison.blocks;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import org.eviline.core.Engine;
import org.eviline.core.Field;
import org.eviline.core.Shape;
import org.eviline.core.ShapeType;
import org.eviline.core.XYShapes;

import org.esialb.edison.sfo.OledDataBuffer;
import org.esialb.edison.sfo.OledImage;
import org.esialb.edison.sfo.OledRaster;
import org.esialb.edison.sfo.SFOled;

public class EngineDraw {
	protected OledImage image;
	protected Graphics2D g;
	
	
	protected Engine engine;
	
	public EngineDraw(Engine engine) {
		this.engine = engine;
		image = SFOled.createImage();
		g = image.createGraphics();
		g.setFont(Font.decode(Font.MONOSPACED).deriveFont(10f));
	}
	
	public void paint() {
		
		Field field = engine.getField();
		
		int bh = 3 * field.HEIGHT;
		int bw = 3 * field.WIDTH;
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 64, 48);
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, bh+1, bw+1);
		for(int y = 0; y < field.HEIGHT; y++) {
			for(int x = 0; x < field.WIDTH; x++) {
				boolean fill = false;
				if(field.masked(x, y))
					fill = true;
				else if(engine.getShape() != -1 && XYShapes.has(engine.getShape(), x, y))
					fill = true;
				if(fill) {
					g.fillRect(1+3*y, 1+3*(field.WIDTH - x - 1), 3, 3);
				} else if(engine.getGhost() != -1 && XYShapes.has(engine.getGhost(), x, y))
					g.drawRect(2+3*y, 2+3*(field.WIDTH - x - 1), 0, 0);

			}
		}
		
		ShapeType[] next = engine.getNext();
		if(next.length > 0 && next[0] != null)
			drawShapeType(next[0], 0, 36);
		
		ShapeType held = engine.getHold();
		if(held != null)
			drawShapeType(held, 58, 36);
		
		g.drawString("" + engine.getLines(), 12, 47);

		if(engine.isOver()) {
			int xo = 2;
			int yo = 9;
			int w = g.getFontMetrics().stringWidth("Game Over") + 2;
			int h = g.getFontMetrics().getHeight();
			g.setColor(Color.BLACK);
			g.fillRect(xo + 0, yo + 0, w + 2, h + 0);
			g.setColor(Color.WHITE);
			g.drawRect(xo + 0, yo + 0, w + 1, h - 1);
			g.drawString("Game Over", xo + 2, yo + h - g.getFontMetrics().getDescent());
		}
		
		image.paint();
	}
	
	private void drawShapeType(ShapeType type, int x, int y) {
		Shape shape = type.start();
		for(int ix = 0; ix < 4; ix++) {
			for(int iy = 0; iy < 4; iy++) {
				if(shape.has(3 - iy, ix))
					g.fillRect(x+3*ix, y + 3*iy, 3, 3);
			}
		}
	}
	
	public void clear() {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		image.paint();
	}
}
