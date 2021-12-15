package net.suraj.suraj_test

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.random.Random

class CustomView @JvmOverloads constructor(context: Context,
                                           attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {


    // Called when the view should render its content.
    var ticks = 0;
    var isStarted: Boolean = false;


    fun spawnEnemy(width: Int, height: Int){
        var randX = width / 2;
        var randY = 100;
        enemies.add(Enemy(Point(randX, randY)));
    }

    fun gameLogic(width: Int, height: Int){
        for(e in enemies){
            e.moveTowardsPoint(player.position);
            if(dist(e.getPosition(), player.position) <= 20){
                player.damage(0.05);
            }
        }

        var deadEnemies: MutableList<Enemy> = mutableListOf();
        for(t in towers){
            var inRange : Iterable<Enemy> = enemies.filter { enemy ->  dist(enemy.getPosition(), t.position) < t.range && !enemy.isDead()};
            for(e in inRange){
                e.damage(0.15);
                if(e.isDead()){
                    deadEnemies.add(e);
                    playerMoney += 50;
                }
            }
        }
        enemies.removeAll(deadEnemies);

        player.moveTowardsTarget(playerTarget);


        if(Random.Default.nextInt(1024) == 4){
            spawnEnemy(width, height);
        }
    }

    fun dist(point1: Point, point2: Point): Double{
        var distCheck: Double =
            Math.sqrt(Math.pow((point1.x.toDouble() - point2.x.toDouble()), 2.0) + Math.pow((point1.y.toDouble() - point2.y.toDouble()),2.0));
        return distCheck;
    }

    fun getClosestTowerToEnemy(enemy: Enemy): Tower?{
        var distance = Double.MAX_VALUE;
        var tower: Tower? = null;
        for(t in towers){
            var distCheck = dist(t.position, enemy.getPosition());
            if(distCheck < distance){
                distance = distCheck;
                tower = t;
            }
        }
        return tower;
    }


    var enemies: MutableList<Enemy> = mutableListOf<Enemy>();
    var towers: MutableList<Tower> = mutableListOf<Tower>();
    var playerTarget: Point = Point(50,50);
    var player: Player = Player(playerTarget);

    var towerMode: Boolean = false;
    var enemyMode: Boolean = false;
    var moveMode: Boolean = false;
    fun moveMode(){
        moveMode = true;
        towerMode = false;
        enemyMode = false;
    }
    fun buyTower(){
        towerMode = true;
        enemyMode = false;
        moveMode = false;
    }

    fun placeEnemy(){
        towerMode = false;
        enemyMode = true;
        moveMode = false;
    }


    fun startCombat(){
        spawnEnemy(canvasWidth, canvasHeight);
        isStarted = true;
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event != null){
            var touchPoint: Point = Point(event.rawX.toInt(), event.rawY.toInt());
            if(towerMode){
                if(playerMoney >= 150){
                    towers.add(Tower(touchPoint, 150));
                    playerMoney -= 150;
                }
            }
            else if(enemyMode){
                enemies.add(Enemy(touchPoint));
            }
            else if(moveMode){
                this.playerTarget = touchPoint;
            }
        }

        Log.d("APP", event?.rawX.toString());
        return super.onTouchEvent(event)
    }

    var playerMoney = 150.0;


    fun drawRectangle(left: Int, top: Int, right: Int, bottom: Int, canvas: Canvas?, paint: Paint?) {
        var right = right
        var bottom = bottom
        right = left + right // width is the distance from left to right
        bottom = top + bottom // height is the distance from top to bottom
        canvas?.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint!!)
    }

    var canvasWidth: Int = 0;
    var canvasHeight: Int = 0;

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint = Paint().apply{
            color = Color.GRAY
        };
        val gameOver = Paint().apply{
            color = Color.RED
            textSize = 150.0f

        };
        val healthGood = Paint().apply{
            color = Color.GREEN
        };
        val textPaint = Paint().apply{
            color = Color.BLACK
            textSize = 50.0f
        };

        canvas?.drawRect(Rect(0,0,canvas.width,canvas.height), paint);
        //todo ^ Draw background

        var enemyPaint = Paint().apply {
            color = Color.RED
        };
        var towerPaint = Paint().apply {
            color = Color.BLUE
        };
        var playerPaint = Paint().apply {
            color = Color.YELLOW
        };

        for(t in towers){
            drawRectangle(t.position.x, t.position.y, 75, 75, canvas, towerPaint);
        }

        for(e in enemies){
            if(e.isDead()){
                Log.i("APP", "Dead enemy");
                continue;
            }
            drawRectangle(e.getPosition().x, e.getPosition().y, 25, 25, canvas, enemyPaint); // draw enemy
            drawRectangle(e.getPosition().x - 20, e.getPosition().y - 40, 65, 10, canvas, enemyPaint); //red part of their health bar
            drawRectangle(e.getPosition().x - 20, e.getPosition().y - 40, (65 * (e.health / 100.0)).toInt(), 10, canvas, healthGood); // green part
        }
        drawRectangle(player.position.x, player.position.y, 25, 25, canvas, playerPaint);

        canvas?.drawText("$" + playerMoney.toString(), 100.0f, 100.0f, textPaint);

        if(canvas != null){
            drawRectangle(0,0, canvas.width, 50, canvas, enemyPaint);
            drawRectangle(0,0, (canvas.width * (player.health / 100.0)).toInt(), 50, canvas, healthGood);
        }


        ticks++;
        if(canvas != null){
            canvasWidth = canvas.width;
            canvasHeight = canvas.height;
            if(playerTarget.x == 50 && playerTarget.y == 50){
                player.position = Point(canvas.width / 2, canvas.height - 250);
                playerTarget = player.position;
            }

            if(isStarted && !player.isDead()){
                gameLogic(canvas.width, canvas.height);
            }
            if(player.isDead()){
                canvas?.drawText("GAME OVER",
                    (0.0).toFloat(), (canvasHeight / 2.0).toFloat(), gameOver);
            }
        }
        // DRAW STUFF HERE

        invalidate(); //
    }
}