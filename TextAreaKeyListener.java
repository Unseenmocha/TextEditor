import java.awt.event.*;

public class TextAreaKeyListener implements KeyListener {
    TextArea textArea;

    TextAreaKeyListener(TextArea textArea) {
        super();
        this.textArea = textArea;
    }

    public void keyReleased(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        int row = textArea.getCurrentRowIndex();
        int letter = textArea.getLetter();
        switch(code) {
            case KeyEvent.VK_DELETE, KeyEvent.VK_BACK_SPACE:
                textArea.deleteCharacter();
                break;
            case KeyEvent.VK_ENTER:
                textArea.addRow(row+1);
                textArea.moveChars(row,row+1,letter,0);
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


