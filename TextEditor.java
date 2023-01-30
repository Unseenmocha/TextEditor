import java.awt.*;
import java.awt.event.*;

//main class to set up window
public class TextEditor {
    private Frame frame = new Frame("Text Editor");
    private TextArea textArea;

    public TextEditor() {
        frame.setSize(400,400);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        frame.setVisible(true);
        FontMetrics fontMetrics = frame.getGraphics().getFontMetrics(frame.getFont());
        textArea = new TextArea(fontMetrics, 10, 5);
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