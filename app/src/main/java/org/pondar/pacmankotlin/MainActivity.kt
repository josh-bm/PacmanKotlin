package org.pondar.pacmankotlin

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import org.pondar.pacmankotlin.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var myTimer: Timer = Timer()
    var counter : Int = 0


    //reference to the game class.
    private lateinit var game: Game
    private lateinit var binding : ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //makes sure it always runs in portrait mode - will cost a warning
        //but this is want we want!
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        Log.d("onCreate","Oncreate called")

        binding.startButton.setOnClickListener(this)
        binding.stopButton.setOnClickListener(this)


        binding.moveUp.setOnClickListener(this)
        binding.moveDown.setOnClickListener(this)
        binding.moveLeft.setOnClickListener(this)
        binding.moveRight.setOnClickListener(this)


        myTimer.schedule(object : TimerTask() {
            override fun run() {
                timerMethod()
            }

        }, 0, 200) //0 indicates we start now, 200
        //is the number of miliseconds between each call

        game = Game(this,binding.pointsView)

        //intialize the game view class and game class
        game.setGameView(binding.gameView)
        binding.gameView.setGame(game)
        game.newGame()
    }

    override fun onStop() {
        super.onStop()
        //just to make sure if the app is killed, that we stop the timer.
        myTimer.cancel()
    }

    private fun timerMethod() {
        //This method is called directly by the timer
        //and runs in the same thread as the timer - i.e the background

        // we could do updates here TO GAME LOGIC,
        // but not updates TO ACTUAL UI

        //We call the method that will work with the UI
        //through the runOnUiThread method.

        this.runOnUiThread(timerTick)
        //timerTick.run() //try doing this instead of the above...will crash the app!

    }

    private val timerTick = Runnable {
        //This method runs in the same thread as the UI.
        // so we can draw
        if (game.running) {
            counter++
            //update the counter - notice this is NOT seconds in this example
            //you need TWO counters - one for the timer count down that will
            // run every second and one for the pacman which need to run
            //faster than every second
            binding.textView.text = getString(R.string.timerValue,counter)

            if (game.direction == game.RIGHT)
            { // move right
                game.moveRight(50)
                //move the pacman - you
                //should call a method on your game class to move
                //the pacman instead of this - you have already made that
            }
            else if (game.direction == game.LEFT)
            {
                //move pacman left.
                game.moveLeft(50)
            }

            else if (game.direction == game.UP)
            {
                //move pacman left.
                game.moveUp(50)
            }

            else if (game.direction == game.DOWN)
            {
                //move pacman left.
                game.moveDown(50)
            }

            game.enemyMovement(50)

        }
    }

    //if anything is pressed - we do the checks here
    override fun onClick(v: View) {
        if (v.id == R.id.startButton) {
            game.running = true
        } else if (v.id == R.id.stopButton) {
            game.running = false
        } else if (v.id == R.id.action_newGame){
            game.running = false
            counter = 0
            binding.textView.text = getString(R.string.timerValue,counter)

            game.newGame() //you should call the newGame method instead of this


        } else if (v.id == R.id.moveUp){
            game.direction = game.UP

        } else if (v.id == R.id.moveDown){
            game.direction = game.DOWN

        } else if (v.id == R.id.moveLeft){
            game.direction = game.LEFT

        } else if (v.id == R.id.moveRight){
            game.direction = game.RIGHT

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_settings) {
            Toast.makeText(this, "settings clicked", Toast.LENGTH_LONG).show()
            return true
        } else if (id == R.id.action_newGame) {
            Toast.makeText(this, "New Game clicked", Toast.LENGTH_LONG).show()
            game.newGame()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
