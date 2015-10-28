package ch.sebastianhaeni.edgewars.graphics.text;

public class TextObject {

    public String text;
    public float x;
    public float y;
    public float[] color;

    public TextObject(String txt, float x, float y) {
        text = txt;
        this.x = x;
        this.y = y;
        color = new float[]{1f, 1f, 1f, 1.0f};
    }

}
