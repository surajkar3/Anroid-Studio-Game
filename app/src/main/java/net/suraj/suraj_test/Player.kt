package net.suraj.suraj_test

import android.graphics.Point

class Player {
    var health = 100.0;
    var position: Point;

    constructor(position: Point) {
        this.position = position
    }

    fun damage(amount: Double){
        health -= amount;
    }

    fun isDead(): Boolean {
        return health <= 0.0;
    }

    fun moveTowardsTarget(targetPoint: Point){
        var xMod = 0;
        var yMod = 0;

        if(position.x < targetPoint.x){
            xMod = 1;
        }
        else if(position.x > targetPoint.x){
            xMod = -1;
        }

        if(position.y < targetPoint.y){
            yMod = 1;
        }
        else if(position.y > targetPoint.y){
            yMod = -1;
        }

        this.position = Point(this.position.x + xMod, this.position.y + yMod);
    }
}