package run;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import data.Perceptron;
import data.WorldModel;
import geometry.Topology2D;
import graphic.Viewer2D;
import math2D.Point2D;
import topology.MeshTopology;

public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	public MainWindow() {
		super("Machine Learning");
		
		MeshTopology<Point2D> t1 = new MeshTopology<Point2D>();
		MeshTopology<Point2D> t2 = new MeshTopology<Point2D>();
		
		ArrayList<double[]> inputs = new ArrayList<double[]>();
		ArrayList<Boolean> expectedOutput = new ArrayList<Boolean>();
		
		// generate data
		for (int i = 0; i < 400; i++) {
			double x = -10 + Math.random() * 20;
			double y = -10 + Math.random() * 20;
			inputs.add(new double[] {x,y});
			
			boolean b = (0.5 * x - y >= 0);
			expectedOutput.add(b);
			
			if (b)
				t1.addPosition(new Point2D(x,y));
			else
				t2.addPosition(new Point2D(x,y));
		}
		
		Perceptron p = new Perceptron();
		p.training(inputs, expectedOutput);
		
		p.print_weights();
		
		WorldModel world = new WorldModel();
		
		Topology2D<Point2D> t11 = new Topology2D<Point2D>(t1);
		t11.setColor(Color.red);
		world.addDrawable(t11);
		
		Topology2D<Point2D> t12 = new Topology2D<Point2D>(t2);
		t12.setColor(Color.blue);
		world.addDrawable(t12);
		
		Viewer2D viewer = new Viewer2D(world, 640, 480);
		
		JPanel panneau = new JPanel(new BorderLayout());
		panneau.setLayout(new BorderLayout());
		panneau.add(viewer, BorderLayout.CENTER);
		
		setContentPane(panneau);
		
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final MainWindow fen = new MainWindow();
				fen.addKeyListener(new KeyListener() {
					public void keyTyped(KeyEvent ev) {}
					
					public void keyReleased(KeyEvent ev) {}
					
					public void keyPressed(KeyEvent ev) {
						if (ev.getKeyCode() == KeyEvent.VK_ESCAPE) {
							fen.dispose();
							System.exit(0);
						}
					}
				});
				fen.setFocusable(true);
				fen.setVisible(true);
			}
		});
	}
}
