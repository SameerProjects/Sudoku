import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

// Java Sudoku solution validator

public class SudokuChecker {
	
	// puzzle to be checked
	public static final int[][] puzzle = 
		{
			{6, 2, 4, 5, 3, 9, 1, 8, 7},
			{5, 1, 9, 7, 2, 8, 6, 3, 4},
			{8, 3, 7, 6, 1, 4, 2, 9, 5},
			{1, 4, 3, 8, 6, 5, 7, 2, 9},
			{9, 5, 8, 2, 4, 7, 3, 6, 1},
			{7, 6, 2, 3, 9, 1, 4, 5, 8},
			{3, 7, 1, 9, 5, 6, 8, 4, 2},
			{4, 9, 6, 1, 8, 2, 5, 7, 3},
			{2, 8, 5, 4, 7, 3, 9, 1, 6}
		};
	
	// variable to keep track of puzzle validation
	public static boolean[] isValid;
	
	public static class ColunmRow {
		int row;
		int col;
		ColunmRow(int row, int column) {
			this.row = row;
			this.col = column;
		}
	}
	// to determine if only numbers 1-9 are in the 3x3 matrix
		public static class validMatrix extends ColunmRow implements Runnable {
			validMatrix(int row, int column) {
				super(row, column); 
			}

			public void run() {
			
				if (row > 6 || row % 3 != 0 || col > 6 || col % 3 != 0) {
					System.out.println("Row or column is invalid");
					return;
				}
				
				// determine if only numbers 1-9  appear one time
				boolean[] validList = new boolean[9];			
				for (int i = row; i < row + 3; i++) {
					for (int j = col; j < col + 3; j++) {
						int num = puzzle[i][j];
						if (num < 1 || num > 9 || validList[num - 1]) {
							return;
						} else {
							validList[num - 1] = true;		
						}
					}
				}
		
				isValid[row + col/3] = true; 		
			}
			
		}
	

	
	// to determine if only numbers 1-9 are in the row 
	public static class ValidRow extends ColunmRow implements Runnable {		
		ValidRow(int row, int column) {
			super(row, column); 
		}

	
		public void run() {
			if (col != 0 || row > 8) {
				System.out.println("Row or column is invalid");				
				return;
			}
			
			// determine if only numbers 1-9  appear one time
			boolean[] validList = new boolean[9];
			int i;
			for (i = 0; i < 9; i++) {
				
				
				int num = puzzle[row][i];
				if (num < 1 || num > 9 || validList[num - 1]) {
					return;
				} else if (!validList[num - 1]) {
					validList[num - 1] = true;
				}
			}

			isValid[9 + row] = true;
		}

	}
	
	// Now do the same thing for column
	public static class ValidColumn extends ColunmRow implements Runnable {
		ValidColumn(int row, int column) {
			super(row, column); 
		}


		public void run() {
			if (row != 0 || col > 8) {
				System.out.println("Row or column is invalid");				
				return;
			}
			
			// // determine if only numbers 1-9  appear one time
			boolean[] validList = new boolean[9];
			int i;
			for (i = 0; i < 9; i++) {
				
				int num = puzzle[i][col];
				if (num < 1 || num > 9 || validList[num - 1]) {
					return;
				} else if (!validList[num - 1]) {
					validList[num - 1] = true;
				}
			}
			
			isValid[18 + col] = true;			
		}		
	}
	
	
//	Define the threads and make sure everything works
	public static void main(String[] args) {
		
		// define number of threads
		final int total_threads = 27;
		
		isValid = new boolean[total_threads];		
		Thread[] threads = new Thread[total_threads];
		int index = 0;
		// Now create all threads using a nested for loop
		for (int i = 0; i < 9; i++) {
			
			for (int j = 0; j < 9; j++) {						
				if (i%3 == 0 && j%3 == 0) {
					threads[index++] = new Thread(new validMatrix(i, j));				
				}
				if (i == 0) {					
					threads[index++] = new Thread(new ValidColumn(i, j));
				}
				if (j == 0) {
					threads[index++] = new Thread(new ValidRow(i, j));					
				}
			}
		}
		
		// start the threads
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
		
		
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		String sudoku ="";

        for(int i = 0; i < puzzle.length;i++){
            for(int j = 0; j < puzzle[i].length;j++){
                sudoku += puzzle[i][j] + " " ;
            }
            
            sudoku += "\n";
        }

		JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setFont(new Font("Monospaced", Font.PLAIN, 18));
        textPane.setSize(300,225);
        textPane.setText(sudoku);
        StyledDocument documentStyle = textPane.getStyledDocument();
        SimpleAttributeSet centerAttribute = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_CENTER);
        documentStyle.setParagraphAttributes(0, documentStyle.getLength(), centerAttribute, false);
    
		String verdict = "";

		// make sure the entries are not equal to 0 in the array
		for (int i = 0; i < isValid.length; i++) {
			if (!isValid[i]) {
				verdict = "Invalid sudoku solution.";
				return;
			}
		}
		verdict = "Valid solution!";
		
        JLabel verdictLabel = new JLabel("Verdict", SwingConstants.CENTER);
        verdictLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        verdictLabel.setFont(new Font("Monospaced", Font.PLAIN, 13));
        verdictLabel.setText(verdict);
		
		
		JFrame frame = new JFrame("Sudoku Validator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300,300);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.add(textPane);
		frame.add(verdictLabel);
		
		
	}
}