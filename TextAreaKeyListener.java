import java.awt.event.*;

//custom keyListener for the TextArea component
public class TextAreaKeyListener implements KeyListener {
    TextArea textArea;
    Text text;

    TextAreaKeyListener(TextArea textArea) {
        super();
        this.textArea = textArea;
        this.text = textArea.getText();
    }

    public void keyReleased(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        int row = text.getCurrentRowIndex();
        int letter = text.getCurrentLetter();
        switch(code) {
            case KeyEvent.VK_DELETE, KeyEvent.VK_BACK_SPACE:
                text.deleteCharacter();
                break;
            case KeyEvent.VK_ENTER:
                text.addRow(row+1);
                text.moveChars(row,row+1,letter,0);
                text.changeRow(row+1);
                text.changeLetter(0);
                break;
            case KeyEvent.VK_LEFT:
                text.changeLetter(letter-1);
                break;
            case KeyEvent.VK_RIGHT:
                text.changeLetter(letter+1);
                break;
            case KeyEvent.VK_UP:
                text.changeRow(row-1);
                break;
            case KeyEvent.VK_DOWN:
                text.changeRow(row+1);
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
            text.addCharacter(c);
        }  
        textArea.repaint();
    }
}


