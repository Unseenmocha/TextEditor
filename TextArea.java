import java.awt.*;

public class TextArea extends Component {
    private Text text;
    private int[] origin = {0,0};
    private Cursor cursor;
    private int width;
    private int height;
    private Spacer spacer;

    public TextArea(FontMetrics fontMetrics, int hMargin, int vRowSep) {
        super();
        this.spacer = new Spacer(hMargin, vRowSep, fontMetrics);
        this.text = new Text(fontMetrics, spacer);
        this.addKeyListener(new TextAreaKeyListener(this));
        this.cursor = new Cursor(2, fontMetrics.getMaxAscent(), spacer);
        text.addRow(0);
    }

    public Text getText() { return this.text;}

    private void updateSize(Graphics g) {
        Rectangle clipBounds = g.getClipBounds();
        this.height = clipBounds.height;
        if (this.width != clipBounds.width) {
            this.width = clipBounds.width;
        }
    }

    //moves origin based on the cursor location such that the user can see all the text
    //they input
    private void adjustScreen() {
        int cursorAbsX = cursor.x + origin[0];
        int cursorAbsY = cursor.y + origin[1];
        if (cursorAbsX > width-spacer.hMargin) {
            origin[0] -= cursorAbsX - (width-spacer.hMargin);
        }
        else if (cursorAbsX < spacer.hMargin) {
            origin[0] += (spacer.hMargin-cursorAbsX);
        }
        if (cursorAbsY > height-spacer.vRowSep-cursor.height) {
            origin[1] -= spacer.heightDiff;
        }
        else if (cursorAbsY < spacer.vRowSep) {
            origin[1] += spacer.heightDiff;
        }
    }
 
    public void paint(Graphics g) {
        updateSize(g);
        cursor.updateCursorLocation(text);
        adjustScreen();
        cursor.drawCursor(g, origin);
        for(Row row : text.getRows()) {
            row.draw(g, origin);
        }
    }
}

class Cursor {
    public int width;
    public int height;
    public int x;
    public int y;
    private Spacer spacer;

    public Cursor(int width, int height, Spacer spacer) {
        this.width = width;
        this.height = height;
        this.spacer = spacer;
    }

    public void updateCursorLocation(Text text) {
        this.x = text.getCurrentRow().getOffset(text.getCurrentLetter())+spacer.hMargin;
        this.y = (spacer.heightDiff)*(text.getCurrentRowIndex())+spacer.vRowSep;
    }

    public void drawCursor(Graphics g, int[] origin) {
        g.setColor(Color.RED);
        g.fillRect(origin[0]+x,origin[1]+y,width,height);
        g.setColor(Color.BLACK);
    }

}

//object to hold values regarding spacing
class Spacer {
    public int hMargin; //margins on the sides of the text area where the cursor will not go past
    public int vRowSep; //space in between rows
    public int heightDiff; //distance between rows including the height of the row

    public Spacer(int hMargin, int vRowSep, FontMetrics fontMetrics) {
        this.hMargin = hMargin;
        this.vRowSep = vRowSep;
        this.heightDiff = fontMetrics.getMaxAscent()+vRowSep;
    }
}