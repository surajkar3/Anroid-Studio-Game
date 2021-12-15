package net.suraj.suraj_test

import android.graphics.Point

class Enemy {
    var health = 100.0;
    var x: Double;
    var y: Double;
    constructor(position: Point) {
        this.x = position.x.toDouble();
        this.y = position.y.toDouble();
    }

    fun damage(amount: Double){
        health -= amount;
    }

    fun isDead(): Boolean {
        return health <= 0.0;
    }

    fun getPosition(): Point{
        return Point(x.toInt(), y.toInt());
    }

    fun moveTowardsPoint(targetPoint: Point){
        var xMod = 0.0;
        var yMod = 0.0;

        var speed = 0.7;

        if(x < targetPoint.x){
            xMod = speed;
        }
        else if(x > targetPoint.x){
            xMod = -speed;
        }

        if(y < targetPoint.y){
            yMod = speed;
        }
        else if(y > targetPoint.y){
            yMod = -speed;
        }

        this.x += xMod;
        this.y += yMod;
    }
}