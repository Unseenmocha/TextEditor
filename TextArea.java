import java.awt.*;
import java.util.*;

public class TextArea extends Component {
    private ArrayList<Row> text = new ArrayList<Row>(); 
    private int hMargin = 10;
    private int vRowSep = 5;
    private int letter = 0;
    private int row = 0;
    private int[] origin = {0,0};
    private int cursorWidth = 2;
    private int cursorX;
    private int cursorY;
    private int cursorHeight;
    private int heightDiff;
    private int width;
    private int height;
    private FontMetrics fontMetrics;

    public TextArea(FontMetrics fontMetrics) {
        super();
        this.fontMetrics = fontMetrics;
        this.addKeyListener(new TextAreaKeyListener(this));
        cursorHeight = fontMetrics.getMaxAscent();
        heightDiff = fontMetrics.getMaxAscent()+vRowSep;
        addRow(0);
    }

    public void addRow(int loc) {
        text.add(loc, new Row(heightDiff*(loc+1),hMargin,this.fontMetrics));
        for(int i = loc+1; i < text.size();i++) {
            text.get(i).setHeight(heightDiff*(i+1));
        }
    }
    
    public void moveChars(int row1, int row2, int s1, int e1, int dest) {
        Row chars1 = text.get(row1);
        Row chars2 = text.get(row2);
        for (int i = s1; i <= e1; ++i) {
            chars2.addCharacter(chars1.deleteCharacter(s1), dest+(i-s1));
        }
    }

    public void moveChars(int row1, int row2, int s1, int dest) {
        int endOfRow = text.get(row1).getChars().size()-1;
        moveChars(row1, row2, s1, endOfRow, dest);
    }

    public void deleteRow(int row) {
        text.remove(row);
        for(int i = row; i < text.size();i++) {
            text.get(i).setHeight(heightDiff*(i+1));
        }
    }

    public void moveRow(int row) {
        if(row < 0 || row >= text.size()) {return;}
        else { 
            this.row = row;
            int charsInRow = text.get(this.row).getChars().size();
            this.letter = (this.letter > charsInRow) ? charsInRow : this.letter;
        }
    }

    public void moveLetter(int letter) {
        int charsInRow = text.get(this.row).getChars().size();
        if(letter < 0 && this.row > 0) {
            this.row--;
            this.letter = text.get(this.row).getChars().size(); //size of previous (now current) row
        }
        else if (letter > charsInRow && this.row < text.size()-1) {
            this.row++;
            this.letter = 0;
        }
        else if (letter >= 0 && letter <= charsInRow) {
            this.letter = letter;
        }
    }
    
    public int getCurrentRowIndex() { return this.row; }

    public Row getRow(int rowIndex) {
        return text.get(rowIndex >= text.size()? 0:rowIndex);
    }

    public int getLetter() { return this.letter; }

    private void updateSize(Graphics g) {
        Rectangle clipBounds = g.getClipBounds();
        this.height = clipBounds.height;
        if (this.width != clipBounds.width) {
            this.width = clipBounds.width;
        }
    }

    private void updateCursorLocation() {
        cursorX = text.get(row).getOffset(letter)+hMargin;
        cursorY = (heightDiff)*(this.row)+vRowSep;
    }

    private void drawCursor(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(origin[0]+cursorX,origin[1]+cursorY,cursorWidth,cursorHeight);
        g.setColor(Color.BLACK);
    }

    private void adjustScreen() {
        int cursorAbsX = cursorX + origin[0];
        int cursorAbsY = cursorY + origin[1];
        if (cursorAbsX > width-hMargin) {
            origin[0] -= cursorAbsX - (width-hMargin);
        }
        else if (cursorAbsX < hMargin) {
            origin[0] += (hMargin-cursorAbsX);
        }
        if (cursorAbsY > height-vRowSep-cursorHeight) {
            origin[1] -= heightDiff;
        }
        else if (cursorAbsY < vRowSep) {
            origin[1] += heightDiff;
        }
    }
 
    public void paint(Graphics g) {
        updateSize(g);
        updateCursorLocation();
        adjustScreen();
        drawCursor(g);
        for(Row row : text) {
            row.draw(g, origin);
        }
    }

    public void deleteCharacter() {
        if (letter > 0) { 
            text.get(row).deleteCharacter(letter-1); 
        }
        if(letter == 0 && row > 0) {
            int placeHolder = text.get(row-1).getChars().size();
            moveChars(row,row-1,letter,placeHolder);
            deleteRow(row);
            row--;
            letter = placeHolder;
            return;
        }
        letter -= letter > 0 ? 1 : 0;
    }

    public void addCharacter(Character c) {
        text.get(row).addCharacter(c, letter);
        letter++;
    }
}
