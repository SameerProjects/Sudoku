import java.awt.Font;
import javax.swing.*;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.SimpleAttributeSet;

public class gui{
    public static void main(String args[]){

        int[][] puzzle = 
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
    
    
        JLabel verdictLabel = new JLabel("Verdict", SwingConstants.CENTER);
        verdictLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        verdictLabel.setFont(new Font("Monospaced", Font.PLAIN, 13));
        verdictLabel.setText("This isn't a valid sudoku solution");


       JFrame frame = new JFrame("Sudoku Validator");
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setSize(300,300);
       frame.setVisible(true);
       frame.setResizable(false);
       frame.add(textPane);
       frame.add(verdictLabel);

 

    }
}