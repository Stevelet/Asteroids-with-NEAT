package original;

import added.Loopable;
import added.Vector;

import java.util.ArrayList;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Asteroid extends Loopable {
    private static final double TWO_PI = Math.PI * 2.0;
    Vector vel;
    @SuppressWarnings("UnusedAssignment")
    int size = 3; //3 = large 2 = medium and 1 = small
    double radius;
    ArrayList<Asteroid> chunks = new ArrayList<Asteroid>();//each asteroid contains 2 smaller asteroids which are released when shot
    boolean split = false;//whether the asteroid has been hit and split into to 2
    int sizeHit;

    //------------------------------------------------------------------------------------------------------------------------------------------
    //constructor
    Asteroid(double posX, double posY, double velX, double velY, int sizeNo) {
        pos = new Vector(posX, posY);
        size = sizeNo;
        vel = new Vector(velX, velY);

        height = ScreenValues.height;
        width = ScreenValues.width;

        switch(sizeNo) {//set the velocity and radius depending on size
            case 1:
                radius = 15;
                vel.normalize();
                vel.mult(1.25);
                break;
            case 2:
                radius = 30;
                vel.normalize();
                vel.mult(1);
                break;
            case 3:
                radius = 60;
                vel.normalize();
                vel.mult(0.75);
                break;
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    //draw the asteroid
    void show() {
        if (split) {//if split show the 2 chunks
            for (Asteroid a : chunks) {
                a.show();
            }
        } else {// if still whole
            noFill();
            stroke(255);
            polygon(pos.x, pos.y, radius, 12);//draw the dodecahedrons
        }
    }
    //--------------------------------------------------------------------------------------------------------------------------
    //draws a polygon
    //not gonna lie, I copied this from https://processing.org/examples/regularpolygon.html
    @SuppressWarnings("SameParameterValue")
    void polygon(double x, double y, double radius, int npoints) {
        double angle = TWO_PI / npoints;//set the angle between vertexes
        beginShape();
        for (double a = 0; a < TWO_PI; a += angle) {//draw each vertex of the polygon
            double sx = x + cos(a) * radius;//math
            double sy = y + sin(a) * radius;//math
            vertex(sx, sy);
        }
        endShape();
    }
    //------------------------------------------------------------------------------------------------------------------------------------------
    //adds the velocity to the position
    void move() {
        if (split) {//if split move the chunks
            for (Asteroid a : chunks) {
                a.move();
            }
        } else {//if not split
            pos.add(vel);//move it
            if (AsteroidGameNeat.isOut(pos)) {//if out of the playing area wrap (loop) it to the other side
                loopy();
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    //checks if a bullet hit the asteroid
    @SuppressWarnings("DuplicatedCode")
    boolean checkIfHit(Vector bulletPos) {
        if (split) {//if split check if the bullet hit one of the chunks
            for (Asteroid a : chunks) {
                if (a.checkIfHit(bulletPos)) {
                    return true;
                }
            }
        } else {
            if (pos.dist(bulletPos) < radius) {//if it did hit
                isHit();//boom
                return true;
            }
            if (pos.x< -50 +radius || pos.x > width+50 - radius || pos.y< -50 + radius || pos.y > height+50 -radius ) {//if ateroid is overlapping edge
                if (pos.x< -50 +radius || pos.x > width+50 - radius || pos.y< -50 + radius || pos.y > height+50 -radius ) {//if bullet is near the edge
                    Vector overlapPos = new Vector(pos.x, pos.y);
                    if (pos.x< -50 +radius) {
                        overlapPos.x += width+100;
                    }
                    if ( pos.x > width+50 - radius ) {
                        overlapPos.x -= width+100;
                    }

                    if ( pos.y< -50 + radius) {
                        overlapPos.y +=height + 100;
                    }

                    if (pos.y > height+50 -radius) {

                        overlapPos.y -= height + 100;
                    }
                    if (overlapPos.dist(bulletPos) < radius) {
                        isHit();//boom
                        return true;
                    }
                }
            }
        }
        return false;
    }
    //------------------------------------------------------------------------------------------------------------------------------------------
    //probs could have made these 3 functions into 1 but whatever
    //this one checks if the player hit the asteroid
    @SuppressWarnings("DuplicatedCode")
    boolean checkIfHitPlayer(Vector playerPos) {
        if (split) {//if split check if the player hit one of the chunks
            for (Asteroid a : chunks) {
                if (a.checkIfHitPlayer(playerPos)) {
                    return true;
                }
            }
        } else {
            if (pos.dist(playerPos) < radius + 15) {//if hit player
                isHit();//boom

                return true;
            }

            if (pos.x< -50 +radius || pos.x > width+50 - radius || pos.y< -50 + radius || pos.y > height+50 -radius ) {//if ateroid is overlapping edge
                if (pos.x< -50 +radius || pos.x > width+50 - radius || pos.y< -50 + radius || pos.y > height+50 -radius ) {//if bullet is near the edge
                    Vector overlapPos = new Vector(pos.x, pos.y);
                    if (pos.x< -50 +radius) {
                        overlapPos.x += width+100;
                    }
                    if ( pos.x > width+50 - radius ) {
                        overlapPos.x -= width+100;
                    }

                    if ( pos.y< -50 + radius) {
                        overlapPos.y +=height + 100;
                    }

                    if (pos.y > height+50 -radius) {

                        overlapPos.y -= height + 100;
                    }
                    if (overlapPos.dist(playerPos) < radius) {
                        isHit();//boom
                        return true;
                    }
                }
            }
        }
        return false;
    }




    //------------------------------------------------------------------------------------------------------------------------------------------
    //same as checkIfHit but it doesnt destroy the asteroid used by the look function
    @SuppressWarnings("DuplicatedCode")
    boolean lookForHit(Vector bulletPos) {
        if (split) {
            for (Asteroid a : chunks) {
                if (a.lookForHit(bulletPos)) {
                    return true;
                }
            }
        } else {
            if (pos.dist(bulletPos) < radius) {
                sizeHit = size;

                return true;
            }
            if (pos.x< -50 +radius || pos.x > width+50 - radius || pos.y< -50 + radius || pos.y > height+50 -radius ) {//if ateroid is overlapping edge
                if (pos.x< -50 +radius || pos.x > width+50 - radius || pos.y< -50 + radius || pos.y > height+50 -radius ) {//if bullet is near the edge
                    Vector overlapPos = new Vector(pos.x, pos.y);
                    if (pos.x< -50 +radius) {
                        overlapPos.x += width+100;
                    }
                    if ( pos.x > width+50 - radius ) {
                        overlapPos.x -= width+100;
                    }

                    if ( pos.y< -50 + radius) {
                        overlapPos.y +=height + 100;
                    }

                    if (pos.y > height+50 -radius) {

                        overlapPos.y -= height + 100;
                    }
                    return overlapPos.dist(bulletPos) < radius;
                }
            }
        }
        return false;
    }
    //------------------------------------------------------------------------------------------------------------------------------------------

    //destroys/splits asteroid
    void isHit() {
        split = true;
        if (size != 1) {//can't split the smallest asteroids
            //add 2 smaller asteroids to the chunks array with slightly different velocities
            Vector velocity = new Vector(vel.x, vel.y);
            velocity.rotate(-0.3);
            chunks.add(new Asteroid(pos.x, pos.y, velocity.x, velocity.y, size-1));
            velocity.rotate(0.5);
            chunks.add(new Asteroid(pos.x, pos.y, velocity.x, velocity.y, size-1));
        }
    }

    @SuppressWarnings("DuplicatedCode")
    Asteroid getAsteroid(Vector bulletPos) {

        if (split) {
            for (Asteroid a : chunks) {
                if (a.getAsteroid(bulletPos)!= null) {
                    return a.getAsteroid(bulletPos);
                }
            }
        } else {

            if (pos.dist(bulletPos) < radius) {
                return this;
            }
            if (pos.x< -50 +radius || pos.x > width+50 - radius || pos.y< -50 + radius || pos.y > height+50 -radius ) {//if ateroid is overlapping edge
                if (pos.x< -50 +radius || pos.x > width+50 - radius || pos.y< -50 + radius || pos.y > height+50 -radius ) {//if bullet is near the edge
                    Vector overlapPos = new Vector(pos.x, pos.y);
                    if (pos.x< -50 +radius) {
                        overlapPos.x += width+100;
                    }
                    if ( pos.x > width+50 - radius ) {
                        overlapPos.x -= width+100;
                    }

                    if ( pos.y< -50 + radius) {
                        overlapPos.y +=height + 100;
                    }

                    if (pos.y > height+50 -radius) {

                        overlapPos.y -= height + 100;
                    }
                    if (overlapPos.dist(bulletPos)< radius) {
                        return this;
                    }
                }
            }
        }
        return null;
    }
}
