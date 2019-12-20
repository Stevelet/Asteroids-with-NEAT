package added;

public class Loopable extends Drawable {
    public Vector pos;

    //TODO update

    //------------------------------------------------------------------------------------------------------------------------------------------
    //if out moves it to the other side of the screen

    protected void loopy() {
        if (pos.y < -50) {
            pos.y = height + 50;
        } else
        if (pos.y > height + 50) {
            pos.y = -50;
        }
        if (pos.x< -50) {
            pos.x = width +50;
        } else  if (pos.x > width + 50) {
            pos.x = -50;
        }
    }
}
