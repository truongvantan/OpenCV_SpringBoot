package com.tvt.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;

public class ImageLabelWithCircles extends JLabel {
	private static final long serialVersionUID = 1L;

	private Integer xMaxTemp = null;
	private Integer yMaxTemp = null;
	private Integer xMinTemp = null;
	private Integer yMinTemp = null;

	private final int circleRadius = 6; // pixels

	public ImageLabelWithCircles() {
		super();
		setOpaque(true);
	}

	// Allow setting the circle positions dynamically
	public void setTemperaturePoints(Integer xMax, Integer yMax, Integer xMin, Integer yMin) {
		this.xMaxTemp = xMax;
		this.yMaxTemp = yMax;
		this.xMinTemp = xMin;
		this.yMinTemp = yMin;
		repaint(); // redraw the label
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g); // draw the image first
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (xMaxTemp != null && yMaxTemp != null) {
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(2f));
			g2.drawOval(xMaxTemp - circleRadius, yMaxTemp - circleRadius, circleRadius * 2, circleRadius * 2);
		}

		if (xMinTemp != null && yMinTemp != null) {
			g2.setColor(Color.YELLOW);
			g2.setStroke(new BasicStroke(2f));
			g2.drawOval(xMinTemp - circleRadius, yMinTemp - circleRadius, circleRadius * 2, circleRadius * 2);
		}

		g2.dispose();
	}

}