package jtable;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class TableTest extends JFrame {

	private static final long serialVersionUID = 1L;
	JTable table;
	File file = new File("src//Files//Archive.txt");

	TableTest() {
		setLayout(new FlowLayout());

		// Table creation and rows color renderer
		table = new JTable(new DefaultTableModel(new Object[] { "Text Data 1", "Text Data 2", "Double data" }, 0)) {

			private static final long serialVersionUID = 1L;

			public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int collIndex) {
				Component c = super.prepareRenderer(renderer, rowIndex, collIndex);
				if (rowIndex % 2 == 0 && !isCellSelected(rowIndex, collIndex)) {
					c.setBackground(Color.LIGHT_GRAY);
				} else {
					c.setBackground(getBackground());
				}
				return c;
			}
		};

		table.setPreferredScrollableViewportSize(new Dimension(500, 300));
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);

		// Add new row button
		JButton addRow = new JButton("Add Row");
		addRow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.addRow(new Object[] {});

				alignCenter();
				alignRight();

			}
		});

		// Delete button
		JButton deleteRow = new JButton("Delete Row");
		deleteRow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int viewIndex = table.getSelectedRow();
				if (viewIndex != -1) {
					int modelIndex = table.convertRowIndexToModel(viewIndex);
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					model.removeRow(modelIndex);
				}
			}
		});

		// Export button
		JButton Export = new JButton("Export to File");
		Export.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					saveTofile(file);
				} catch (IOException e1) {
					System.out.println("Exception thrown");
					e1.printStackTrace();
				}

			}
		});

		// Import button
		JButton Import = new JButton("Import from File");
		Import.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					loadFromFile(file, table);
				} catch (Exception e1) {
					System.out.println("Exception thrown");
				}

				alignCenter();
				alignRight();

			}
		});

		// Save to PDF button
		JButton savePDF = new JButton("Save to PDF");
		savePDF.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					int count = table.getRowCount();
					System.out.println(count);
					Document document = new Document();
					PdfWriter.getInstance(document, new FileOutputStream("e:/data.pdf"));
					document.open();
					PdfPTable tab = new PdfPTable(3);
					tab.addCell("Text Data 1");
					tab.addCell("Text Data 2");
					tab.addCell("Double data");
					for (int i = 0; i < count; i++) {
						Object obj1 = GetData(table, i, 0);
						Object obj2 = GetData(table, i, 1);
						Object obj3 = GetData(table, i, 2);

						String value1 = obj1.toString();
						String value2 = obj2.toString();
						String value3 = obj3.toString();

						tab.addCell(value1);
						tab.addCell(value2);
						tab.addCell(value3);
					}
					document.add(tab);
					document.close();
				} catch (Exception exception) {
				}
			}

		});

		// Print button
		JButton print = new JButton("Print");
		print.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					table.print();
				} catch (PrinterException e1) {
					System.out.println("Printing exception");
				}

			}
		});

		add(addRow);
		add(deleteRow);
		add(Export);
		add(Import);
		add(savePDF);
		add(print);
	}

	public Object GetData(JTable table, int row_index, int col_index) {
		return table.getModel().getValueAt(row_index, col_index);
	}

	// Write to file
	public void saveTofile(File file) throws IOException {
		BufferedWriter writer = null;
		StringBuffer tableData = new StringBuffer();
		for (int row = 0; row < table.getRowCount(); row++) {
			for (int column = 0; column < table.getColumnCount(); column++) {
				tableData.append(table.getValueAt(row, column)).append("\t");
			}
			tableData.append("\n");
		}
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(tableData.toString());
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	// Read from file
	public void loadFromFile(File file, JTable table) throws Exception {

		DefaultTableModel model = new DefaultTableModel
				(new String[] { "Text Data 1", "Text Data 2", "Double data" },0);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine();

		while (line != null) {
			String[] data = line.split("\\t");
			model.addRow(data);
			line = br.readLine();
		}

		table.setModel(model);
	}

	// Align text to center
	public void alignCenter() {
		DefaultTableCellRenderer center = new DefaultTableCellRenderer();
		center.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(1).setCellRenderer(center);

	}

	// Align text to right
	public void alignRight() {
		DefaultTableCellRenderer right = new DefaultTableCellRenderer();
		right.setHorizontalAlignment(JLabel.RIGHT);
		table.getColumnModel().getColumn(2).setCellRenderer(right);
	}

	public static void main(String args[]) {

		TableTest table = new TableTest();
		table.setTitle("Table");
		table.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		table.setSize(600, 450);
		table.setVisible(true);

	}

}
