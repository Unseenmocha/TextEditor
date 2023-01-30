import java.awt.*;
import java.util.*;

public class Row {
    private ArrayList<Character> chars = new ArrayList<Character>();
    private int length = 0;
    private int hMargin;
    private int height;
    private FontMetrics fontMetrics;

    Row(int height, int hMargin, FontMetrics fontMetrics) {
        this.height = height;
        this.hMargin = hMargin;
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

    //count out length of characters in this row from index 0 to letter-1
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

    public void draw(Graphics g, int[] origin) {
        int pos = 0;
        for (int i = 0; i<chars.size(); i++) {
            char ch = (char)chars.get(i);
            char[] tempArr = {ch};
            int charWidth = fontMetrics.charWidth(ch);
            g.drawChars(tempArr,0,1,origin[0]+hMargin+pos, origin[1]+height);
            pos += charWidth;
        }
    }
}

