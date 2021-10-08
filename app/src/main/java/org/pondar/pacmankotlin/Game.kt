package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.TextView
import java.lang.Math.random
import java.lang.Math.sqrt
import java.util.ArrayList
import kotlin.math.pow


/**
 *
 * This class should contain all your game logic
 */

class Game(private var context: Context,view: TextView) {

        private var pointsView: TextView = view
        private var points : Int = 0
        //bitmap of the pacman
        var pacBitmap: Bitmap
        var coinBitmap: Bitmap

        var pacx: Int = 0
        var pacy: Int = 0

        //did we initialize the coins?
        var coinsInitialized = false

        //the list of goldcoins - initially empty
        var coins = ArrayList<GoldCoin>()

        //a reference to the gameview
        private lateinit var gameView: GameView

        private var h: Int = 0
        private var w: Int = 0 //height and width of screen


    //The init code is called when we create a new Game class.
    //it's a good place to initialize our images.
    init {
        var pacBitmapFactory = BitmapFactory.decodeResource(context.resources, R.drawable.pacman)
        pacBitmap = Bitmap.createScaledBitmap(pacBitmapFactory,50,50,false);

        var coinBitmapFactory = BitmapFactory.decodeResource(context.resources, R.drawable.coin)
        coinBitmap = Bitmap.createScaledBitmap(coinBitmapFactory,50,50,false);
    }


    fun setGameView(view: GameView) {
        this.gameView = view
    }

    //TODO initialize goldcoins also here
    fun initializeGoldcoins()
    {
        //DO Stuff to initialize the array list with some coins.
        var coin1 = GoldCoin(100, 100, false)
        var coin2 = GoldCoin(200, 200, false)
        var coin3 = GoldCoin(300, 300, false)
        var coin4 = GoldCoin(400, 400, false)
        var coin5 = GoldCoin(500, 500, false)
        coins.add(coin1)
        coins.add(coin2)
        coins.add(coin3)
        coins.add(coin4)
        coins.add(coin5)

        coinsInitialized = true
    }


    fun newGame() {
        pacx = 50
        pacy = 400 //just some starting coordinates - you can change this.

        //reset the points
        coinsInitialized = false
        points = 0
        pointsView.text = "${context.resources.getString(R.string.points)} $points"

        initializeGoldcoins()

        gameView.invalidate() //redraw screen

    }
    fun setSize(h: Int, w: Int) {
        this.h = h
        this.w = w
    }

    fun moveRight(pixels: Int) {
        //still within our boundaries?
        // right boundary
        if (pacx + pixels + pacBitmap.width < w)
            pacx += pixels
        gameView.invalidate()//redraw everything
    }

    fun moveLeft(pixels: Int){
        // left boundary
        // if location + pixels added + size of pacman > 0 + size of pacman
        if(pacx - pixels + pacBitmap.width > 0 + pacBitmap.width)
            pacx -= pixels
        gameView.invalidate()//redraw everything
    }

    fun moveUp(pixels: Int){
        // Upper boundary
        if (pacy - pixels + pacBitmap.height > 0 + pacBitmap.height)
            pacy -= pixels
        gameView.invalidate()//redraw everything
    }

    fun moveDown(pixels: Int){
        // bottom boundary
        if (pacy + pixels + pacBitmap.height < h)
            pacy += pixels
        gameView.invalidate()//redraw everything
    }



    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    fun doCollisionCheck() {
        fun distance(x1:Int,y1:Int,x2:Int,y2:Int) : Float {
            // calculate distance and return it
            var dx : Double = (x2-x1).toDouble()
            var dy : Double = (y2-y1).toDouble()
            return sqrt(Math.pow(dx, 2.0) + Math.pow(dy, 2.0)).toFloat()
        }

        var pacCenter = pacBitmap.height/2
        var coinCenter = coinBitmap.height/2

        for(coin in coins){
            if (distance(pacx + pacCenter,pacy + pacCenter,coin.coinx + coinCenter,coin.coiny + coinCenter) < 20){
                coin.isTaken = true
            }
        }

        if(coins.filter{it.isTaken}.isNullOrEmpty()){
            //running = false
        }
    }

}