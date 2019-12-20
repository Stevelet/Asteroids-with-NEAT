package original;

import added.Loopable;
import added.Vector;

public class Bullet extends Loopable {
    Vector vel;
    double speed = 10;
    boolean off = false;
    int lifespan = 60;
    //------------------------------------------------------------------------------------------------------------------------------------------

    Bullet(double x, double y, double r, double playerSpeed) {
        height = ScreenValues.height;
        width = ScreenValues.width;

        pos = new Vector(x, y);
        vel = Vector.fromAngle(r);
        vel.mult(speed + playerSpeed);//bullet speed = 10 + the speed of the player
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    //move the bullet 
    void move() {
        lifespan --;
        if (lifespan<0) {//if lifespan is up then destroy the bullet
            off = true;
        } else {
            pos.add(vel);
            if (AsteroidGameNeat.isOut(pos)) {//wrap bullet
                loopy();
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    //show a dot representing the bullet
    void show() {
        if (!off) {
            fill(255);
            ellipse(pos.x, pos.y, 3, 3);
        }
    }
}
