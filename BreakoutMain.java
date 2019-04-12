import javax.swing.JFrame;

/**
 * 
 */

/**
 * @author bh
 *
 */
public class BreakoutMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		  JFrame frame = new JFrame ("break out");
	      frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	      frame.getContentPane().add (new BreakoutPanel1());
	      frame.pack();
	      frame.setVisible(true);
	}

}
