public class SudokuValidator{

    public static boolean[] validSection;

    public static class ColRow{
        int col;
        int row;

        ColRow(int col,int row){
            this.col = col;
            this.row = row;
        }
    }

    public static class checkRow extends ColRow implements Runnable{
        checkRow(int col, int row){
            super(col, row);
        }

        public void run(){
            if (col != 0){
               System.out.println("Invalid Column"); 
               return;
            }

            if(row > 8){
                System.out.println("Invalid Row");
                return;
            } 
            
            

        }
    }

}