package topcoder.datascience;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GrafixMaskGraphic extends JPanel {
	String[] strShapes;
	GrafixMaskGraphic(String[] strShapes) {
        setBackground(Color.GRAY);	// X    Y
        setPreferredSize(new Dimension(600, 400));
        this.strShapes = strShapes;
	}

	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawLine(192, 350, 207, 350);
        final Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLUE);
        for (String strShape : strShapes) {
        	String[] strBroken = strShape.split(" ");
        	int[] box = new int[strBroken.length];
        	for (int i = 0; i < strBroken.length; i++) {
        		box[i] = Integer.parseInt(strBroken[i]);
        	}
        	System.out.println("> (" + box[1] + "," + box[0] + ") and (" + box[3] + "," + box[2]+")");
//            g.drawLine(box[1], box[0], box[1], box[2]);
//            g.drawLine(box[3], box[2], box[3], box[0]);
            g2.draw(new Rectangle2D.Float(box[1], box[0], box[3], box[2]));
//        	g.drawRect(box[1], box[0], box[3], box[2]);
        }
    }

	public static void main(String[] args) {
        String shapeAmount = JOptionPane.showInputDialog(null,
                "How many shapes?", "Random Shapes...", JOptionPane.PLAIN_MESSAGE);
        int amount = Integer.parseInt(shapeAmount);
        String[] strShapes = new String[amount];
        for (int i = 0; i < amount; i++) {
            strShapes[i] = JOptionPane.showInputDialog(null,
                    "Shape #" + i, "Shape Dimension in Y X Y X", JOptionPane.PLAIN_MESSAGE);
        }
        JFrame frame = new JFrame();
        frame.add(new GrafixMaskGraphic(strShapes));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}

}
