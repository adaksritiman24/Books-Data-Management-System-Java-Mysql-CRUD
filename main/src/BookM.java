//Storing Book Data(id, title, author, year of publish) in MySQL Database
//Gui - Java Swing
//Mysql Database Connector - jdbc (jar file : mysql.jar)
//primary key - id
// 
/*

table description 'book':
+--------+-------------+------+-----+---------+-------+
| Field  | Type        | Null | Key | Default | Extra |
+--------+-------------+------+-----+---------+-------+
| id     | int         | NO   | PRI | NULL    |       |
| title  | varchar(50) | YES  |     | NULL    |       |
| author | varchar(50) | YES  |     | NULL    |       |
| yop    | varchar(50) | YES  |     | NULL    |       |
+--------+-------------+------+-----+---------+-------+

*/

import java.awt.EventQueue;
//Importing Swing classes and objcets
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.border.SoftBevelBorder;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingConstants;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JSeparator;
import java.awt.Window.Type;
//sql secific classes for connecting to mysql
import java.sql.*;
public class BookM {
	//declaring private attributes
	private JFrame frame;
	private JTextField id;
	private JTextField title;
	private JTextField author;
	private JTextField yop;
	private JTable table;
	//model for table
	private DefaultTableModel model;
	// row ->(implementin a single row in the table) array of objects
	private final Object[] row = new Object[4];
	// this variable is for updating a row
	private int test=0;
	private int old_id =0;
	Connection conn = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BookM window = new BookM();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BookM() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {//this bloc is for connecting to the Mysql database: (note that, the server over here is local)
			
			System.out.println("Connecting to the database......");
			Class.forName("com.mysql.cj.jdbc.Driver");//setting the name path for the jdbc driver
			//connecting to our database : bookdb
			conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/bookdb","root","###########");
			System.out.println("Successfully connected to database!");
			
		}
		catch(Exception e) {// Connection to database failed
			System.out.println("Exception occured in connecting to the database :(\n"+ e.getMessage());
		}
		// main window of the application
		frame = new JFrame();
		//giving a window title
		frame.setTitle("Books Data Management (CRUD)");
		frame.setBounds(100, 100, 830, 547);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		// panel over the main frame:  this will contain all the items 
		JPanel panel = new JPanel();
		panel.setBackground(new Color(169, 169, 169));
		panel.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
		panel.setBounds(0, 0, 816, 510);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		//label for ID
		JLabel lblNewLabel = new JLabel("ID:");
		lblNewLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 17));
		lblNewLabel.setBounds(24, 119, 92, 36);
		panel.add(lblNewLabel);
		
		//Label for title
		JLabel lblNewLabel_1 = new JLabel("Title:");
		lblNewLabel_1.setFont(new Font("Trebuchet MS", Font.PLAIN, 17));
		lblNewLabel_1.setBounds(24, 165, 92, 36);
		panel.add(lblNewLabel_1);
		
		//label for Author
		JLabel lblNewLabel_2 = new JLabel("Author:");
		lblNewLabel_2.setFont(new Font("Trebuchet MS", Font.PLAIN, 17));
		lblNewLabel_2.setBounds(24, 211, 92, 36);
		panel.add(lblNewLabel_2);
		
		//label for Year of Publish
		JLabel lblNewLabel_3 = new JLabel("Year Of \r\nPublish:");
		lblNewLabel_3.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblNewLabel_3.setBounds(24, 257, 122, 36);
		panel.add(lblNewLabel_3);
		

		//declaring the text fields for user inputs
		//id text field
		id = new JTextField();
		id.setBackground(new Color(255, 250, 250));
		id.setBounds(126, 120, 163, 36);
		panel.add(id);
		id.setColumns(10);
		
		//title text field
		title = new JTextField();
		title.setBounds(126, 165, 163, 36);
		panel.add(title);
		title.setColumns(10);
		
		//author text field
		author = new JTextField();
		author.setBounds(126, 211, 163, 36);
		panel.add(author);
		author.setColumns(10);
		
		//Year of publish text field
		yop = new JTextField();
		yop.setBounds(126, 257, 163, 36);
		panel.add(yop);
		yop.setColumns(10);
		
		// this pannel will contain the buttons
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(169, 169, 169));
		panel_1.setBorder(new SoftBevelBorder(BevelBorder.RAISED, new Color(128, 128, 128), null, null, null));
		panel_1.setBounds(24, 315, 264, 165);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		//this panel will contain the table
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel_2.setBackground(new Color(169, 169, 169));
		panel_2.setBounds(304, 119, 491, 361);
		panel.add(panel_2);
		panel_2.setLayout(null);		
		
		//this scrollpane will be added to the table
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setToolTipText("");
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setBounds(10, 48, 471, 303);
		panel_2.add(scrollPane);		
		
		//the table which will show the data from the database
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setBackground(new Color(255, 222, 173));
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(0, 0, 0), null, null, null));
		model = new DefaultTableModel();
		Object[] column = {"ID","Title","Author","Year of Publish"};// columns for the table
		model.setColumnIdentifiers(column);//add the columns to the default table model
		table.setModel(model);// adding the default table model to the table
		table.setFont(new Font("Trebuchet MS", Font.PLAIN, 10));	//adding font 
		fetchAndShow();	// this will fetch table: book from the database: bookdb, and show the contents in the model , whenever the application is run
		// insert button
		JButton btnNewButton = new JButton("INSERT");
		btnNewButton.setForeground(new Color(0, 0, 0));
		btnNewButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		btnNewButton.setBackground(new Color(211, 211, 211));
		btnNewButton.addActionListener(new ActionListener() { // adding actionlistener for the insert button
			public void actionPerformed(ActionEvent arg0) {
				//if any of the field is kept null, data will not be stored
				if(id.getText().equals("")||title.getText().equals("")||author.getText().equals("")||yop.getText().equals("")){
					JOptionPane.showMessageDialog(null,"Please fill all the details");// display window 
				}
				else{
					// to insert into database and simultaneously making the change in table
					insertIntoDatabase(id.getText(),title.getText(),author.getText(),yop.getText());
				}
			}
		});
		btnNewButton.setBounds(20, 23, 106, 47);
		panel_1.add(btnNewButton);// add button to panel
		
		// button for delete operations
		JButton btnNewButton_1 = new JButton("DELETE");
		btnNewButton_1.addActionListener(new ActionListener() {// adding actionlistner
			public void actionPerformed(ActionEvent arg0) {
				int i = table.getSelectedRow(); // get the row number that is selected by user in the table

				if(i>=0) { // if any row is selected
					//deleteFromDatabase(Integer.parseInt(model.getValueAt(i,0).toString()));
					test=Integer.parseInt(model.getValueAt(i,0).toString()); // convert row id to integer type

					//delete from database and simultaneously make the change in the table displayed
					deleteFromDatabase(i, test);
					
				}
				else {// if no row is selected
					JOptionPane.showMessageDialog(null,"Select Row");	
				}
				
			}
		});

		btnNewButton_1.setForeground(new Color(0, 0, 0));
		btnNewButton_1.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		btnNewButton_1.setBackground(new Color(211, 211, 211));
		btnNewButton_1.setBounds(136, 23, 106, 47);
		panel_1.add(btnNewButton_1);
		
		//button for updating a row in the database
		JButton btnNewButton_2 = new JButton("UPDATE");
		btnNewButton_2.addActionListener(new ActionListener() {// adding actionlistner
			public void actionPerformed(ActionEvent arg0) {

				int i = table.getSelectedRow(); // get the row number of the selected row
				if(i>=0) {// row is selected
					// id any of the field is kept empty the the update is failed
					if(id.getText().equals("")||title.getText().equals("")||author.getText().equals("")||yop.getText().equals("")){
						JOptionPane.showMessageDialog(null, "Enter all the details");// enter all details window message
					}
					else{// no field is empty

						old_id = Integer.parseInt(model.getValueAt(i,0).toString());// convert the id in the row to integer
						test = Integer.parseInt(id.getText()); // get the new id from the id text field (not required)
						// update in database and simultaneously make the changed in the Jtable
						updateInDatabase(i,old_id,test, title.getText(), author.getText(),yop.getText());
					}
					
				}
				else {// no row is selected
					JOptionPane.showMessageDialog(null,"Select A Row");	
				}
			}
		});
		btnNewButton_2.setForeground(new Color(0, 0, 0));
		btnNewButton_2.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		btnNewButton_2.setBackground(new Color(211, 211, 211));
		btnNewButton_2.setBounds(20, 95, 106, 47);
		panel_1.add(btnNewButton_2);
		
		//Button for clearing the text fields
		JButton btnNewButton_3 = new JButton("CLEAR");
		btnNewButton_3.addActionListener(new ActionListener() {// adding actionlistner to the button
			public void actionPerformed(ActionEvent arg0) {
				//clear the text fields without performing any database operation
				clearFields();
			}
		});
		btnNewButton_3.setForeground(new Color(0, 0, 0));
		btnNewButton_3.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		btnNewButton_3.setBackground(new Color(211, 211, 211));
		btnNewButton_3.setBounds(136, 94, 106, 48);
		panel_1.add(btnNewButton_3); //add the button to the panel
	
	
		
		// label: heading for the table
		JLabel lblNewLabel_4 = new JLabel("DATABASE TABLE");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4.setForeground(new Color(0, 0, 139));
		lblNewLabel_4.setFont(new Font("Ebrima", Font.BOLD, 19));
		lblNewLabel_4.setBounds(10, 10, 471, 38);
		panel_2.add(lblNewLabel_4);
		
		// seperate the Main heading with the content part of the applcation (enhancing looks)
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(105, 105, 105));
		separator.setBackground(new Color(105, 105, 105));
		separator.setBounds(26, 107, 769, 2);
		panel.add(separator);
		
		//enhancing looks
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(new Color(47, 79, 79));
		separator_1.setBackground(new Color(47, 79, 79));
		separator_1.setBounds(24, 303, 264, 2);
		panel.add(separator_1);
		
		//panel for putting the main heading of the app (enhancing looks)
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(169, 169, 169));
		panel_3.setBorder(new BevelBorder(BevelBorder.RAISED, new Color(255, 0, 0), null, null, null));
		panel_3.setBounds(24, 20, 771, 77);
		panel.add(panel_3);
		panel_3.setLayout(null);
		
		// label: main heading of the application
		JLabel lblNewLabel_5 = new JLabel("Books Data Management");
		lblNewLabel_5.setFont(new Font("Verdana", Font.BOLD, 32));
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5.setBounds(10, 10, 751, 57);
		panel_3.add(lblNewLabel_5); // add label to the heading panel
	}



	public void clearFields() { //clear all the fields: set string to ""
		id.setText("");
		title.setText("");
		author.setText("");
		yop.setText("");
	}




	// fetch all data from the table 'book' and show them in the rows of the jtable
	public void fetchAndShow(){
		try{
			//creating simple statement
			Statement stmt = conn.createStatement();
			// mysql query for fetching all rows from a table
			String query = "select * from book";
			// return the rows in the resultset object
			ResultSet rs = stmt.executeQuery(query);
			//get each and every row using the next() method of the resultset object
			while(rs.next()){
				row[0] = rs.getInt("id");// read corresponding column data
				row[1] = rs.getString("title");
				row[2] = rs.getString("author");
				row[3] = rs.getString("yop");
				//System.out.println(row[0]);
				model.addRow(row);// add row to the Jtable
			}
		}
		catch(Exception e){ // if failed to execute the mysql query
			JOptionPane.showMessageDialog(null,"Failed to Fetch data"+ e.getMessage());
		}
	}




	//insert into the table a new row
	void insertIntoDatabase(String id, String title, String author, String yop){
		try{
			int id_int = Integer.parseInt(id); // convert the string to int 
			//query for insertion
			String query = "insert into book (id, title, author, yop) values (?,?,?,?);";
			PreparedStatement stmt = conn.prepareStatement(query);// prepare the statement
			stmt.setInt(1,id_int);
			stmt.setString(2,title);
			stmt.setString(3,author);
			stmt.setString(4,yop);	
			stmt.executeUpdate();	// execute the query
			// create new row for Jtable
			row[0]=id_int;
			row[1]=title;
			row[2]=author;
			row[3]=yop;
			model.addRow(row); // append the new row to Jtable
			JOptionPane.showMessageDialog(null,"Successfully Added!");
			clearFields(); // clear all fields after successfull operation
		}
		catch(Exception e){ // error in mysql query
			JOptionPane.showMessageDialog(null,"Failed to add data! ID must be unique");
		}

	}



	//delete a row from the database table
	public void deleteFromDatabase(int i,int id){
		try{
			String query = "delete from book where id=?"; // query: delete the row by id
			PreparedStatement stmt = conn.prepareStatement(query); // prepare for execution
			stmt.setInt(1,id);
			stmt.executeUpdate(); //execute query

			model.removeRow(i); // remove the row from the Jtable also
			JOptionPane.showMessageDialog(null,"Deletion Successfull");
		}
		catch(Exception e){ // error in implementing the mysql query
			JOptionPane.showMessageDialog(null, "Failed to Delete!");
		}
	}



	//update row 
	public void updateInDatabase(int i,int old_id, int id, String title, String author, String yop){
		try{
			String query = "update book set title=?, author=?, yop=?, id=? where id=?";// query for upadating the full row, by the id that is present in the Jtable
			PreparedStatement stmt = conn.prepareStatement(query); // prepare statement

			stmt.setString(1,title);
			stmt.setString(2,author);
			stmt.setString(3,yop);
			stmt.setInt(4, id);
			stmt.setInt(5,old_id);

			stmt.executeUpdate();	// execute query

			// fixing the changes done to that particular row in the Jtable
			model.setValueAt(id,i,0);
			model.setValueAt(title,i,1);
			model.setValueAt(author,i,2);	
			model.setValueAt(yop,i,3);

							
			JOptionPane.showMessageDialog(null,"Details Updated");	
			clearFields(); // clear text fields after successfull updation

		}
		catch(Exception e){ // if the query execution fails
			JOptionPane.showMessageDialog(null, "Update Failed!");
			System.out.println(e.getMessage());
		}
	}
}
//end of program
