import java.util.*;
import java.awt.*;

//Class to keep track of all rows in TextArea as well as position within text
public class Text {
    private ArrayList<Row> rows = new ArrayList<Row>(); 
    private int row = 0; //row that user is on
    private int letter = 0; //letter in row that user is on
    private Spacer spacer;
    private FontMetrics fontMetrics;

    public Text(FontMetrics fontMetrics, Spacer spacer) {
        this.fontMetrics = fontMetrics;
        this.spacer = spacer;
        addRow(0);
    }

    public int getCurrentRowIndex() { return this.row; }

    public int getCurrentLetter() { return this.letter; }

    public Row getCurrentRow() { return rows.get(row); }

    public ArrayList<Row> getRows() { return this.rows;}

    public void addCharacter(Character c) {
        rows.get(row).addCharacter(c, letter);
        letter++;
    }

    public void deleteCharacter() {
        //if deleting while in leftmost position in row move any characters in the row to the
        //end of the row above and keep cursor in same location relative to the moved characters
        if(letter == 0 && row > 0) {
            int placeHolder = rows.get(row-1).getChars().size();
            moveChars(row,row-1,letter,placeHolder);
            deleteRow(row);
            row--;
            letter = placeHolder;
            return;
        }
        else if (letter > 0) { 
            rows.get(row).deleteCharacter(letter-1); 
            letter--;
        }
    }

    public void addRow(int loc) {
        //height of row is a multiple of heightDiff
        rows.add(loc, new Row(spacer.heightDiff*(loc+1),spacer.hMargin,this.fontMetrics));

        //readjust height of all rows below the one just added
        for(int i = loc+1; i < rows.size();i++) {
            rows.get(i).setHeight(spacer.heightDiff*(i+1));
        }
    }
    
    public void deleteRow(int row) {
        rows.remove(row);

        //readjust height of all rows below the one just removed
        for(int i = row; i < rows.size();i++) {
            rows.get(i).setHeight(spacer.heightDiff*(i+1));
        }
    }

    public void changeRow(int row) {
        //row out of bounds
        if(row < 0 || row >= rows.size()) {return;}
        else { 
            //move to specified row, keeping same letter offset unless there are less
            //chars than old row in which case simply move to the end of the new row
            this.row = row;
            int charsInRow = rows.get(this.row).getChars().size();
            this.letter = (this.letter > charsInRow) ? charsInRow : this.letter;
        }
    }

    public void changeLetter(int letter) {
        int charsInRow = rows.get(this.row).getChars().size();
        //if moving left past beginning of row, move up to end of row above
        if(letter < 0 && this.row > 0) {
            this.row--;
            this.letter = rows.get(this.row).getChars().size(); //size of previous (now current) row
        }
        //if moving right past end of row, move to the beginning of the row below
        else if (letter > charsInRow && this.row < rows.size()-1) {
            this.row++;
            this.letter = 0;
        }
        else if (letter >= 0 && letter <= charsInRow) {
            this.letter = letter;
        }
    }


    //move characters from indices s1 to e1 inclusive in row1 to index dest in row2
    public void moveChars(int row1, int row2, int s1, int e1, int dest) {
        Row chars1 = rows.get(row1);
        Row chars2 = rows.get(row2);
        for (int i = s1; i <= e1; ++i) {
            chars2.addCharacter(chars1.deleteCharacter(s1), dest+(i-s1));
        }
    }

    //move characters from index s1 to the end of row1 to index dest in row2
    public void moveChars(int row1, int row2, int s1, int dest) {
        int endOfRow = rows.get(row1).getChars().size()-1;
        moveChars(row1, row2, s1, endOfRow, dest);
    }
}