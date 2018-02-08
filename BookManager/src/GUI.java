import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class GUI extends JFrame {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private DefaultTableModel model = null;
	private JTable table = null;
	private String[][] datas;
	private String[] titles;
	private String name = "";
	private String author = "";
	private String number = "";
	private String price = "";

	private Statement stat;

	public GUI() {
		// Connect to the database
		DataBase db = new DataBase();
		db.getConn();
		stat = db.getStmt();

		JPanel main = new JPanel();
		main.setLayout(new BorderLayout(20, 20));
		// Top panel
		JPanel top = new JPanel();
		top.setLayout(new GridLayout(1, 6, 3, 0));
		JButton add = new JButton("Add");
		JButton delete = new JButton("Delete");
		JButton modify = new JButton("Modify");
		JButton refresh = new JButton("Refresh");
		JButton searchByName = new JButton("Search By Name");
		JButton searchByAuthor = new JButton("Search By Author");

		// Add listener for button
		AddButtonListener abl = new AddButtonListener();
		DeleteButtonListener dbl = new DeleteButtonListener();
		ModifyButtonListener mbl = new ModifyButtonListener();
		RefreshButtonListener rbl = new RefreshButtonListener();
		SearchByNameListener sbnl = new SearchByNameListener();
		SearchByAuthorListener sbal = new SearchByAuthorListener();

		add.addActionListener(abl);
		delete.addActionListener(dbl);
		modify.addActionListener(mbl);
		refresh.addActionListener(rbl);
		searchByName.addActionListener(sbnl);
		searchByAuthor.addActionListener(sbal);

		// Add button to the panel
		top.add(add);
		top.add(delete);
		top.add(modify);
		top.add(refresh);
		top.add(searchByName);
		top.add(searchByAuthor);
		main.add(top, BorderLayout.NORTH);

		// Button panel
		JPanel buttom = new JPanel();
		datas = new String[100][4];
		// datas[0][0]="aa";
		titles = new String[] { "Name", "Author", "Number", "Price" };
		model = new DefaultTableModel(datas, titles);
		table = new JTable(model) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}// 表格不允许被编辑
		};
		table.setDefaultRenderer(Object.class, new TableStyle());
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setPreferredScrollableViewportSize(new Dimension(880, 510));
		table.setGridColor(Color.black);

		buttom.add(table);
		buttom.add(new JScrollPane(table));

		// Show the table content
		try {
			stat.execute("SELECT name,author,number,price FROM book where 1");
			ResultSet rs = stat.getResultSet();
			int row = 1;
			while (row <= 100 && rs.next()) {
				datas[row - 1][0] = rs.getString(1);
				datas[row - 1][1] = rs.getString(2);
				datas[row - 1][2] = rs.getString(3);
				datas[row - 1][3] = rs.getString(4);
				row++;
			}
			model.setDataVector(datas, titles);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Sorry, Adding fails", "Adding failed",
					JOptionPane.WARNING_MESSAGE, null);
		}

		main.add(buttom, BorderLayout.SOUTH);

		add(main);

		setSize(900, 600);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Book Manager");
		setVisible(true);
	}

	class TableStyle extends DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public TableStyle() {
			setHorizontalAlignment(RIGHT);
		}
	}

	// Defination of the listener
	class AddButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JFrame addFrame = new JFrame("Add a new item");
			JLabel jl1 = new JLabel("   Name:");
			JTextField jtf1 = new JTextField(8);
			JLabel jl2 = new JLabel("   Author:");
			JTextField jtf2 = new JTextField(8);
			JLabel jl3 = new JLabel("   Number:");
			JTextField jtf3 = new JTextField(8);
			JLabel jl4 = new JLabel("   Price:");
			JTextField jtf4 = new JTextField(8);
			// Top Panel
			JPanel top = new JPanel(new GridLayout(4, 2));
			top.add(jl1);
			top.add(jtf1);
			top.add(jl2);
			top.add(jtf2);
			top.add(jl3);
			top.add(jtf3);
			top.add(jl4);
			top.add(jtf4);
			// Buttom Panel
			JPanel down = new JPanel(new FlowLayout());
			JButton OK = new JButton("Add");
			OK.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					// Action work
					name = jtf1.getText();
					author = jtf2.getText();
					number = jtf3.getText();
					price = jtf4.getText();
					boolean fails = false;

					try {
						// Set the name+author as key, if the key constrain happens, add the new number
						// to the orignal book
						fails = stat.execute("insert into book (name,author,number,price) values ('" + name + "','"
								+ author + "' ,'" + number + "','" + price + "') "
								+ "ON DUPLICATE KEY UPDATE number=number+" + number + ", price=price+" + price + ";");
						JOptionPane.showMessageDialog(null, "Successfully insert a new row", "Adding finished",
								JOptionPane.WARNING_MESSAGE, null);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "Sorry, Adding fails", "Adding failed",
								JOptionPane.WARNING_MESSAGE, null);
					}
					if (!fails) {
						// Refresh the table
						try {
							stat.execute("SELECT name,author,number,price FROM book where 1");
							ResultSet rs = stat.getResultSet();
							int row = 1;
							datas = new String[100][4];
							while (row <= 100 && rs.next()) {
								datas[row - 1][0] = rs.getString(1);
								datas[row - 1][1] = rs.getString(2);
								datas[row - 1][2] = rs.getString(3);
								datas[row - 1][3] = rs.getString(4);
								row++;
							}
							model.setDataVector(datas, titles);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null, "Sorry, Adding fails", "Adding failed",
									JOptionPane.WARNING_MESSAGE, null);
						}
					} else {
						JOptionPane.showMessageDialog(null, "Sorry, inserting fails", "Adding failed",
								JOptionPane.WARNING_MESSAGE, null);
					}

				}

			});
			JButton Cancel = new JButton("Cancel");
			Cancel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					addFrame.dispose();
				}
			});
			down.add(OK);
			down.add(Cancel);

			addFrame.setLayout(new GridLayout(2, 1, 0, 20));
			addFrame.add(top);
			addFrame.add(down);

			// Frame setting
			Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = (int) screensize.getWidth();
			int height = (int) screensize.getHeight();
			addFrame.setLocation(width / 2, height / 2);
			addFrame.setSize(300, 200);
			addFrame.setResizable(false);
			addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			addFrame.setVisible(true);

		}

	}

	class DeleteButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JFrame deleteFrame = new JFrame("Delete a item");
			JLabel jl1 = new JLabel("   Name:");
			JTextField jtf1 = new JTextField(8);
			JLabel jl2 = new JLabel("   Author:");
			JTextField jtf2 = new JTextField(8);
			// Top Panel
			JPanel top = new JPanel(new GridLayout(2, 2));
			top.add(jl1);
			top.add(jtf1);
			top.add(jl2);
			top.add(jtf2);
			// Buttom Panel
			JPanel down = new JPanel(new FlowLayout());
			JButton OK = new JButton("Delete");
			OK.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					name = jtf1.getText();
					author = jtf2.getText();
					boolean fails = false;

					try {
						// Set the name+author as key, if the key constrain happens, add the new number
						// to the orignal book
						if(name.equals("")||author.equals(""))
						{
							JOptionPane.showMessageDialog(null, "You shouldn't leave name or author with blank!", "Deletion failed",
									JOptionPane.WARNING_MESSAGE, null);
							return;
						}
						fails = stat.execute("delete from book WHERE name='" + name + "' AND author='" + author + "';");
						JOptionPane.showMessageDialog(null, "Successfully delete the row", "Deletion finished",
								JOptionPane.WARNING_MESSAGE, null);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "Sorry, deletion fails", "Deletion failed",
								JOptionPane.WARNING_MESSAGE, null);
					}
					if (!fails) {
						// Refresh the table
						try {
							stat.execute("SELECT name,author,number,price FROM book where 1");
							ResultSet rs = stat.getResultSet();
							int row = 1;
							datas = new String[100][4];
							while (row <= 100 && rs.next()) {
								datas[row - 1][0] = rs.getString(1);
								datas[row - 1][1] = rs.getString(2);
								datas[row - 1][2] = rs.getString(3);
								datas[row - 1][3] = rs.getString(4);
								row++;
							}
							model.setDataVector(datas, titles);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null, "Sorry, deletion fails", "Deletion failed",
									JOptionPane.WARNING_MESSAGE, null);
						}
					} else {
						JOptionPane.showMessageDialog(null, "Sorry, deletion fails", "Deletion failed",
								JOptionPane.WARNING_MESSAGE, null);
					}
				}

			});
			JButton Cancel = new JButton("Cancel");
			Cancel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub

				}

			});
			down.add(OK);
			down.add(Cancel);

			deleteFrame.setLayout(new GridLayout(2, 1, 0, 20));
			deleteFrame.add(top);
			deleteFrame.add(down);

			// Frame setting
			Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = (int) screensize.getWidth();
			int height = (int) screensize.getHeight();
			deleteFrame.setLocation(width / 2, height / 2);
			deleteFrame.setSize(300, 200);
			deleteFrame.setResizable(false);
			deleteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			deleteFrame.setVisible(true);
		}

	}

	class ModifyButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JFrame modFrame = new JFrame("Modify the item");
			JLabel jl1 = new JLabel("   Name:");
			JTextField jtf1 = new JTextField(8);
			JLabel jl2 = new JLabel("   Author:");
			JTextField jtf2 = new JTextField(8);
			JLabel jl3 = new JLabel("New Number:");
			JTextField jtf3 = new JTextField(8);
			JLabel jl4 = new JLabel("New Price:");
			JTextField jtf4 = new JTextField(8);
			// Top Panel
			JPanel top = new JPanel(new GridLayout(5, 2));
			top.add(jl1);
			top.add(jtf1);
			top.add(jl2);
			top.add(jtf2);
			top.add(jl3);
			top.add(jtf3);
			top.add(jl4);
			top.add(jtf4);
			// Buttom Panel
			JPanel down = new JPanel(new FlowLayout());
			JButton OK = new JButton("Modify");
			OK.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					name = jtf1.getText();
					author = jtf2.getText();
					number = jtf3.getText();
					price = jtf4.getText();
					// TODO Auto-generated method stub
					try {
						stat.execute("UPDATE book set number=" + number + " ,price=" + price + " WHERE name='" + name
								+ "' AND author='" + author + "'");
						boolean fails = false;
						if (!fails) {
							// Refresh the table
							try {
								stat.execute("SELECT name,author,number,price FROM book where 1");
								ResultSet rs = stat.getResultSet();
								int row = 1;
								datas = new String[100][4];
								while (row <= 100 && rs.next()) {
									datas[row - 1][0] = rs.getString(1);
									datas[row - 1][1] = rs.getString(2);
									datas[row - 1][2] = rs.getString(3);
									datas[row - 1][3] = rs.getString(4);
									row++;
								}
								model.setDataVector(datas, titles);
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "Sorry, Modification fails", "Modification failed",
										JOptionPane.WARNING_MESSAGE, null);
							}
							JOptionPane.showMessageDialog(null, "Successfully modify a  row", "Adding finished",
									JOptionPane.WARNING_MESSAGE, null);
						} else {

							JOptionPane.showMessageDialog(null, "Sorry, Modification fails", "Modification failed",
									JOptionPane.WARNING_MESSAGE, null);
						}

					}

					catch (SQLException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "Sorry, Modification fails", "Modification failed",
								JOptionPane.WARNING_MESSAGE, null);
					}

				}

			});
			JButton Cancel = new JButton("Cancel");
			Cancel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					modFrame.dispose();
				}

			});
			down.add(OK);
			down.add(Cancel);

			modFrame.setLayout(new GridLayout(2, 1, 0, 0));
			modFrame.add(top);
			modFrame.add(down);

			// Frame setting
			Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = (int) screensize.getWidth();
			int height = (int) screensize.getHeight();
			modFrame.setLocation(width / 2, height / 2);
			modFrame.setSize(300, 250);
			modFrame.setResizable(false);
			modFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			modFrame.setVisible(true);

		}

	}

	class RefreshButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				stat.execute("SELECT name,author,number,price FROM book where 1");
				ResultSet rs = stat.getResultSet();
				int row = 1;
				datas = new String[100][4];
				while (row <= 100 && rs.next()) {
					datas[row - 1][0] = rs.getString(1);
					datas[row - 1][1] = rs.getString(2);
					datas[row - 1][2] = rs.getString(3);
					datas[row - 1][3] = rs.getString(4);
					row++;
				}
				model.setDataVector(datas, titles);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Sorry, Refreshing fails", "Refreshing failed",
						JOptionPane.WARNING_MESSAGE, null);
			}
		}

	}

	class SearchByNameListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			JFrame searchFrame = new JFrame("Search a item by name");
			JLabel jl1 = new JLabel("   Name:");
			JTextField jtf1 = new JTextField(8);
			// Top Panel
			JPanel top = new JPanel(new GridLayout(1, 2));
			top.add(jl1);
			top.add(jtf1);
			// Buttom Panel
			JPanel down = new JPanel(new FlowLayout());
			JButton OK = new JButton("Search");
			OK.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					name = jtf1.getText();
					// Refresh the table
					try {
						if(name.equals(""))
						{
							JOptionPane.showMessageDialog(null, "You shouldn't leave name with blank!", "Searching failed",
									JOptionPane.WARNING_MESSAGE, null);
							return;
						}
						stat.execute("SELECT name,author,number,price FROM book where name='" + name + "'");
						ResultSet rs = stat.getResultSet();
						int row = 1;
						datas = new String[100][4];
						while (row <= 100 && rs.next()) {
							datas[row - 1][0] = rs.getString(1);
							datas[row - 1][1] = rs.getString(2);
							datas[row - 1][2] = rs.getString(3);
							datas[row - 1][3] = rs.getString(4);
							row++;
						}
						model.setDataVector(datas, titles);
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null, "Sorry, searching fails", "Searching failed",
								JOptionPane.WARNING_MESSAGE, null);
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "Sorry, Searching fails", "Searching failed",
								JOptionPane.WARNING_MESSAGE, null);
					}
					JOptionPane.showMessageDialog(null, "Successfully searching the result", "Searching finished",
							JOptionPane.WARNING_MESSAGE, null);
				}
			}

			);
			JButton Cancel = new JButton("Cancel");
			Cancel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					searchFrame.dispose();

				}

			});
			down.add(OK);
			down.add(Cancel);

			searchFrame.setLayout(new GridLayout(2, 1, 0, 20));
			searchFrame.add(top);
			searchFrame.add(down);

			// Frame setting
			Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = (int) screensize.getWidth();
			int height = (int) screensize.getHeight();
			searchFrame.setLocation(width / 2, height / 2);
			searchFrame.setSize(200, 150);
			searchFrame.setResizable(false);
			searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			searchFrame.setVisible(true);

		}

	}

	class SearchByAuthorListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			JFrame searchFrame = new JFrame("Search a item by name");
			JLabel jl1 = new JLabel("   Name:");
			JTextField jtf1 = new JTextField(8);
			// Top Panel
			JPanel top = new JPanel(new GridLayout(1, 2));
			top.add(jl1);
			top.add(jtf1);
			// Buttom Panel
			JPanel down = new JPanel(new FlowLayout());
			JButton OK = new JButton("Search");
			OK.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					author = jtf1.getText();

					// Refresh the table
					try {
						if(author.equals(""))
						{
							JOptionPane.showMessageDialog(null, "You shouldn't leave author with blank!", "Searching failed",
									JOptionPane.WARNING_MESSAGE, null);
							return;
						}
						stat.execute("SELECT name,author,number,price FROM book where author='" + author + "'");
						ResultSet rs = stat.getResultSet();
						int row = 1;
						datas = new String[100][4];
						while (row <= 100 && rs.next()) {
							datas[row - 1][0] = rs.getString(1);
							datas[row - 1][1] = rs.getString(2);
							datas[row - 1][2] = rs.getString(3);
							datas[row - 1][3] = rs.getString(4);
							row++;
						}
						model.setDataVector(datas, titles);
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null, "Sorry, searching fails", "Searching failed",
								JOptionPane.WARNING_MESSAGE, null);
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "Sorry, Searching fails", "Searching failed",
								JOptionPane.WARNING_MESSAGE, null);
					}
					JOptionPane.showMessageDialog(null, "Successfully searching the result", "Searching finished",
							JOptionPane.WARNING_MESSAGE, null);
				}
			}

			);
			JButton Cancel = new JButton("Cancel");
			Cancel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					searchFrame.dispose();

				}

			});
			down.add(OK);
			down.add(Cancel);

			searchFrame.setLayout(new GridLayout(2, 1, 0, 20));
			searchFrame.add(top);
			searchFrame.add(down);

			// Frame setting
			Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = (int) screensize.getWidth();
			int height = (int) screensize.getHeight();
			searchFrame.setLocation(width / 2, height / 2);
			searchFrame.setSize(200, 150);
			searchFrame.setResizable(false);
			searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			searchFrame.setVisible(true);

		}

	}
}