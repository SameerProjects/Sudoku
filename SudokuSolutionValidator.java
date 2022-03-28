import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class SudokuSolutionValidator {
    
    public static boolean[] validArr = new boolean[27]; //This array will be used in tandem with sudokuThreads to see if a section of the board has an invalid number
    
    public static int[][] board =  //Sample sudoku board to be validated
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

    public static void main(String[] args) throws InterruptedException{	

		Thread[] sudokuThreads = new Thread[27]; //Thread array that will hold 27 thread. 9 for rows, columns and boxes each.
		int threadCounter = 0;

		for (int row = 0; row < 9; row++) {   //This nested loop will instantiate the threads with all the check classes described below  that will determine if the section is valid
			for (int col = 0; col < 9; col++) {
                
                if (col == 0) {
                    sudokuThreads[threadCounter++] = new Thread(new checkRow(row));					
                }
                
                if (row == 0) {					
                    sudokuThreads[threadCounter++] = new Thread(new checkCol(col));
				}
                
                if (row % 3 == 0 && col % 3 == 0) {
                    sudokuThreads[threadCounter++] = new Thread(new checkBox(row, col));				
                }
			}
		}
		
		for (int i = 0; i < sudokuThreads.length; i++) { // Start the threads
			sudokuThreads[i].start();
		}
		
		for (int i = 0; i < sudokuThreads.length; i++) { // Make sure the threads are all done
				sudokuThreads[i].join();
		}

        String sudokuNumbers =""; // String that will copy the board so we can display it to the GUI

        for(int i = 0; i < board.length;i++){ //Loop is copying the board onto String
            for(int j = 0; j < board[i].length;j++){
                sudokuNumbers += board[i][j] + " " ;
            }

            sudokuNumbers += "\n";
        }

        JTextPane textPane = new JTextPane(); //Utilizing and styling a textpane to display the board. The board is centered and has specific fonts.
        textPane.setEditable(false);
        textPane.setFont(new Font("Monospaced", Font.PLAIN, 18));
        textPane.setSize(300,225);
        textPane.setText(sudokuNumbers);
        StyledDocument documentStyle = textPane.getStyledDocument();
        SimpleAttributeSet centerAttribute = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_CENTER);
        documentStyle.setParagraphAttributes(0, documentStyle.getLength(), centerAttribute, false);


        JLabel verdictLabel = new JLabel("Verdict", SwingConstants.CENTER); // JLabel to notify whether the sudoku is valid or not.
        verdictLabel.setVerticalAlignment(SwingConstants.BOTTOM);           // Positioned at the bottom center and has consistent font with the board.
        verdictLabel.setFont(new Font("Monospaced", Font.PLAIN, 13));

        boolean cond = true; // This condition will be used to determine if the board is valid or not

        for (int i = 0; i < validArr.length; i++) {
            if (validArr[i] == false) { // If a false is present in validArr, then the board is not valid
                cond = false;
            }
        }

        if (cond) {
            verdictLabel.setText("Congrats! A valid sudoku :)");
        }
        else{
            verdictLabel.setText("This is not a valid sudoku :(");
        }


        JFrame frame = new JFrame("Sudoku Validator"); //Adding all of our components to the JFrame so that it can be displayed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,300);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.add(textPane);
        frame.add(verdictLabel);
		
	}
    
    
    public static class checkRow implements Runnable{
        int row;

        checkRow(int row){  //constructor so that the program can choose what row to validate
            this.row = row;
        }
        
        @Override
        public void run() {
            
            ArrayList<Integer> referenceArr = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9)); // All the valid numbers that can be placed in sudoku.
            Set<Integer> set = new HashSet<Integer>(); // Use a set to make sure a number doesn't repeat.
            boolean[] tempArr = new boolean[9]; // Temporary array I used for debugging purposes to make sure the row was correctly validating.
            
            for(int i = 0; i < 9; i++){
                if(referenceArr.contains(board[row][i]) && !set.contains(board[row][i])){
                    tempArr[i] = true;
                }
                else{
                    return;
                }
                set.add(board[row][i]);
            }
            
            validArr[row] = true; // If the code reaches this part, validArr at one of the first 9 indexes should be updated to true implying that the row is valid.
                                  // Repeat this method for all 9 rows of the board.
            
        }
    }
    
    public static class checkCol implements Runnable{
        int col;
        
        checkCol(int col){ //constructor so that the program can choose what column to validate
            this.col = col;
        }
        
        @Override
        public void run() {
            
            ArrayList<Integer> referenceArr = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9)); // All the valid numbers that can be placed in sudoku.
            Set<Integer> set = new HashSet<Integer>(); // Use a set to make sure a number doesn't repeat.
            boolean[] tempArr = new boolean[9]; // Temporary array I used for debugging purposes to make sure the row was correctly validating.
            
            for(int i = 0; i < 9; i++){
                if(referenceArr.contains(board[i][col]) && !set.contains(board[i][col])){
                    tempArr[i] = true;
                }
                else{
                    return;
                }
                set.add(board[i][col]);
            }
            
            validArr[col + 9] = true; // If the code reaches this part, validArr at indexes 9 - 17 should be updated to true implying that the column is valid.
                                      // Repeat this method for all 9 columns of the board.
        }
    }
    
    public static class checkBox implements Runnable{
        int row;
        int col;

        checkBox(int row,int col){ //constructor so that the program can choose what box to validate
            this.row = row;
            this.col = col;
        }
        
        @Override
        public void run() {
            
            ArrayList<Integer> referenceArr = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9)); // All the valid numbers that can be placed in sudoku.
            Set<Integer> set = new HashSet<Integer>(); // Use a set to make sure a number doesn't repeat.
            boolean[] tempArr = new boolean[9]; // Temporary array I used for debugging purposes to make sure the box was correctly validating.
            
            for (int i = row; i < row + 3; i++){
                for (int j = col; j < col + 3; j++){
                    if(referenceArr.contains(board[i][j]) && !set.contains(board[i][j])) {
                        tempArr[board[i][j] - 1] = true;
                    }
                    else{
                        return;
                    }
                    set.add(board[i][j]);
                }
            }
            
            
            validArr[(row + col/3) + 18] = true; // If the code reaches this part, validArr at indexes 18 - 26 should be updated to true implying that the box is valid.
                                                 // Repeat this method for all 9 boxes of the board.
        }
    }

}