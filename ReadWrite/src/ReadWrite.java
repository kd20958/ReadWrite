/**
 * 
 * @author Kevin
 * 
 */
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ReadWrite extends JFrame {

	final static int NAME_SIZE = 32;
	final static int ID_SIZE = 32;
	final static int GPA_SIZE = 20;
	final static int RECORD_SIZE = (NAME_SIZE + ID_SIZE + GPA_SIZE);

	private RandomAccessFile raf;

	private JTextField jtfName = new JTextField(NAME_SIZE);
	private JTextField jtfID = new JTextField(ID_SIZE);
	private JTextField jtfGPA = new JTextField(GPA_SIZE);

	private JButton jbtAdd = new JButton("Add");
	private JButton jbtFirst = new JButton("First");
	private JButton jbtNext = new JButton("Next");
	private JButton jbtPrevious = new JButton("Previous");
	private JButton jbtLast = new JButton("Last");

	public ReadWrite() {
		try {
			raf = new RandomAccessFile("List.dat", "rw");
		} catch (IOException ex) {
			System.out.print("Error: " + ex);
			System.exit(1);
		}

		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(3, 1));
		p1.add(new JLabel("Name"));
		p1.add(new JLabel("ID"));
		p1.add(new JLabel("GPA"));

		JPanel p3 = new JPanel();
		p3.setLayout(new BorderLayout());
		p3.add(jtfGPA, BorderLayout.CENTER);

		JPanel p4 = new JPanel();
		p4.setLayout(new GridLayout(3, 1));
		p4.add(jtfName);
		p4.add(jtfID);
		p4.add(p3);

		JPanel jpAddress = new JPanel(new BorderLayout());
		jpAddress.add(p1, BorderLayout.WEST);
		jpAddress.add(p4, BorderLayout.CENTER);


		jpAddress.setBorder(new BevelBorder(BevelBorder.RAISED));

		JPanel jpButton = new JPanel();
		jpButton.add(jbtAdd);
		jpButton.add(jbtFirst);
		jpButton.add(jbtNext);
		jpButton.add(jbtPrevious);
		jpButton.add(jbtLast);

		add(jpAddress, BorderLayout.CENTER);
		add(jpButton, BorderLayout.SOUTH);

		jbtAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				writeAddress();
			}
		});
		jbtFirst.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (raf.length() > 0)
						readAddress(0);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		jbtNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					long currentPosition = raf.getFilePointer();
					if (currentPosition < raf.length())
						readAddress(currentPosition);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		jbtPrevious.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					long currentPosition = raf.getFilePointer();
					if (currentPosition - 2 * RECORD_SIZE > 0)
						readAddress(currentPosition - 2 * 2 * RECORD_SIZE);
					else
						readAddress(0);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		jbtLast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					long lastPosition = raf.length();
					if (lastPosition > 0)
						readAddress(lastPosition - 2 * RECORD_SIZE);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});

		try {
			if (raf.length() > 0)
				readAddress(0);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void writeAddress() {
		try {
			raf.seek(raf.length());
			FixedLengthStringIO.writeFixedLengthString(jtfName.getText(),
					NAME_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(jtfID.getText(),
					ID_SIZE, raf);
			FixedLengthStringIO.writeFixedLengthString(jtfGPA.getText(),
					GPA_SIZE, raf);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/** Read a record at the specified position */
	public void readAddress(long position) throws IOException {
		raf.seek(position);
		String name = FixedLengthStringIO.readFixedLengthString(NAME_SIZE, raf);
		String ID = FixedLengthStringIO.readFixedLengthString(ID_SIZE,
				raf);
		String gpa = FixedLengthStringIO.readFixedLengthString(GPA_SIZE, raf);

		jtfName.setText(name);
		jtfID.setText(ID);
		jtfGPA.setText(gpa);
	}

	public static void main(String[] args) {
		ReadWrite frame = new ReadWrite();
		frame.pack();
		frame.setTitle("Student List");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}