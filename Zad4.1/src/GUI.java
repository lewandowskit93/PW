import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;


public class GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JProgressBar progressBar;
	protected JLabel time;
	protected JLabel time_left;
	protected JLabel finished;
	protected JLabel left;
	protected JLabel generated;
	protected JLabel to_generate;
	protected JPanel panel;
	public GUI()
	{
		super();
		this.setSize(400, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		progressBar = new JProgressBar(0,100);
		panel.add(progressBar);
		time = new JLabel();
		panel.add(time);
		time_left = new JLabel();
		panel.add(time_left);
		finished = new JLabel();
		panel.add(finished);
		left = new JLabel();
		panel.add(left);
		generated = new JLabel();
		panel.add(generated);
		to_generate = new JLabel();
		panel.add(to_generate);
		this.add(panel);
	}
	
	public void updateInfo(double finished, double left, long time, long time_left,
			int generated, int nr_of_permutations) {
		this.finished.setText("Finished: "+finished+"%");
		this.left.setText("Left: "+left+"%");
		this.time.setText("Time: "+time+" [ms]");
		this.time_left.setText("Time Left: "+time_left+" [ms]");
		this.generated.setText("Generated: "+generated);
		this.to_generate.setText("To generate: "+nr_of_permutations);
		progressBar.setValue((int)(finished));
	}

}
