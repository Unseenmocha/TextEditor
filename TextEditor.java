import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TextEditor {
    private Frame frame = new Frame("Text Editor");;
    private TextArea textArea;

    public TextEditor() {
        frame.setSize(400,400);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        frame.setVisible(true);
        textArea = new TextArea(frame.getGraphics().getFontMetrics(frame.getFont()));
        frame.add(textArea);
    }
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        editor.showTextEditor();
    }

    private void showTextEditor() {
        textArea.setFocusable(true);
        textArea.requestFocus();
        textArea.setVisible(true);
        frame.setVisible(true);
    }   
}

class TextArea extends Component {
    private ArrayList<Row> text = new ArrayList<Row>(); 
    private int lOffset = 5;
    private int rOffset = 5;
    private int vRowSep = 5;
    private int letter = 0;
    private int row = 0;
    private int cursorWidth = 2;
    private int cursorHeight;
    private int heightDiff;
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
        text.add(loc, new Row(heightDiff*(loc+1),lOffset,this.fontMetrics));
        for(int i = loc+1; i < text.size();i++) {
            text.get(i).setHeight(heightDiff*(i+1));
        }
    }

    //move all characters from index loc in row1 to end of row2
    //made solely to allow user to push text to new lines when pressing enter/return
    public void shiftChars(int row1, int row2, int loc) {
        Row chars1 = text.get(row1);
        Row chars2 = text.get(row2);
        int numCharsToMove = chars1.getChars().size();
        for(int i = loc; i < numCharsToMove; i++) {
            chars2.addCharacter(chars1.deleteCharacter(loc),chars2.getChars().size());
        }
    }

    public void deleteRow(int row) {
        text.remove(row);
        for(int i = row; i < text.size();i++) {
            text.get(i).setHeight(heightDiff*(i+1));
        }
    }

    public void moveRow(int row) {
        if(row<0||row>=text.size()) {return;}
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
    
    public int getRow() { return this.row; }

    public int getLetter() { return this.letter; }

    private void drawCursor(Graphics g) {
        int x = text.get(row).getOffset(letter)+lOffset;
        int y = (heightDiff)*(this.row)+vRowSep;
        g.fillRect(x,y,cursorWidth,cursorHeight);
    }
 
    public void paint(Graphics g) {
        for(Row row : text) {
            row.draw(g);
        }
        g.setColor(Color.RED);
        drawCursor(g);
        g.setColor(Color.BLACK);
    }
    public void deleteCharacter() {
        if (letter > 0) { 
            text.get(row).deleteCharacter(letter-1); 
        }
        if(letter == 0 && row > 0) {
            int placeHolder = text.get(row-1).getChars().size();
            shiftChars(row,row-1,letter);
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

class Row {
    private ArrayList<Character> chars = new ArrayList<Character>();
    private int length = 0;
    private int lOffset;
    private int height;
    private FontMetrics fontMetrics;

    Row(int height, int lOffset, FontMetrics fontMetrics) {
        this.height = height;
        this.lOffset = lOffset;
        this.fontMetrics = fontMetrics;
    }

    public ArrayList<Character> getChars() { return chars; }

    public int getScreenLength() { return length; }

    public void addCharacter(Character ch, int loc) {
        length += fontMetrics.charWidth((char)ch);
        chars.add(loc, ch);
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getOffset(int letter) {
        int offset = 0;
        for (int i=0; i<letter; ++i) {
            offset+= fontMetrics.charWidth((char) chars.get(i));
        }
        return offset;
    }

    public Character deleteCharacter(int loc) {
        char ch = (char) chars.get(loc);
        length -= fontMetrics.charWidth(ch);
        return chars.remove(loc);
    }

    public void draw(Graphics g) {
        int pos = 0;
        for (int i = 0; i<chars.size(); i++) {
            char ch = (char)chars.get(i);
            char[] tempArr = {ch};
            g.drawChars(tempArr,0,1,lOffset+pos, height);
            pos += fontMetrics.charWidth(ch);
        }
    }
}

class TextAreaKeyListener implements KeyListener {
    TextArea textArea;

    TextAreaKeyListener(TextArea textArea) {
        super();
        this.textArea = textArea;
    }

    public void keyReleased(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        int row = textArea.getRow();
        int letter = textArea.getLetter();
        switch(code) {
            case KeyEvent.VK_DELETE, KeyEvent.VK_BACK_SPACE:
                textArea.deleteCharacter();
                break;
            case KeyEvent.VK_ENTER:
                textArea.addRow(row+1);
                textArea.shiftChars(row,row+1,letter);
                textArea.moveRow(row+1);
                textArea.moveLetter(0);
                break;
            case KeyEvent.VK_LEFT:
                textArea.moveLetter(letter-1);
                break;
            case KeyEvent.VK_RIGHT:
                textArea.moveLetter(letter+1);
                break;
            case KeyEvent.VK_UP:
                textArea.moveRow(row-1);
                break;
            case KeyEvent.VK_DOWN:
                textArea.moveRow(row+1);
                break;
            
        }
        textArea.repaint();
    }

    public void keyTyped(KeyEvent e) {
        Character c = e.getKeyChar();
        if (c != KeyEvent.CHAR_UNDEFINED && 
            c != KeyEvent.VK_BACK_SPACE &&
            c != KeyEvent.VK_ENTER) 
        {

            textArea.addCharacter(c);
        }  
        textArea.repaint();
    }
}

