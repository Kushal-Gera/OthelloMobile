package com.application.kushal.othello;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout mainLayout;
    MyButton[][] grid;
    LinearLayout[] rowLayout;
    private static boolean gameOver;
    private static boolean blackTurn;
    private static boolean exit = false;
    int b_c;
    int w_c;

    int counter;
    Button blackCount;
    Button whiteCount;
    ImageButton imageBlack;
    ImageButton imageWhite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameOver = false;
        blackTurn = true;
        b_c = w_c  = 2;
        Intent i = getIntent();
        mainLayout = findViewById(R.id.mainLayout);
        blackCount = findViewById(R.id.blackCount);
        whiteCount = findViewById(R.id.whiteCount);
        imageBlack = findViewById(R.id.image_black);
        imageWhite = findViewById(R.id.image_white);
        createGrid();
        updateBoard();
        blackCount.setText(b_c+"");
        whiteCount.setText(w_c+"");
        if(blackTurn)
        {
            imageWhite.setImageResource(R.drawable.white);
            imageBlack.setImageResource(R.drawable.black);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.newGame)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Do you really want to REPLAY ?")
                    .setMessage("All progress will be LOST !")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            restart();
                            Toast.makeText(MainActivity.this, "Restarted", Toast.LENGTH_SHORT).show();
                            Log.e("restart","yo");
                        }
                    })
                    .setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //do nothing
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return true;
    }

    public void restart() {
        Log.e("restrat","yo");
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++){
                grid[i][j].setBlack(false);
                grid[i][j].setClicked(false);
                grid[i][j].setCanClicked(false);
                grid[i][j].setBackgroundResource(R.drawable.rect);
                grid[i][j].setImageResource(R.drawable.tr);
                if ((i == 3 && j == 3) || (i == 4 && j == 4)) {
                    grid[i][j].setClicked(true);
                    grid[i][j].setBlack(false);
                    grid[i][j].setImageResource(R.drawable.white);
                }
                if ((i == 4 && j == 3) || (i == 3 && j == 4)) {
                    grid[i][j].setClicked(true);
                    grid[i][j].setImageResource(R.drawable.black);
                    grid[i][j].setBlack(true);
                }
            }

        }
        updateBoard();
        gameOver = false;
        blackTurn = true;
        b_c = w_c  = 2;
        if(blackTurn)
        {
            imageWhite.setImageResource(R.drawable.tr);
            imageBlack.setImageResource(R.drawable.black);
        }
        blackCount.setText(b_c+"");
        whiteCount.setText(w_c+"");

    }

    public void createGrid() {
        rowLayout = new LinearLayout[8];
        grid = new MyButton[8][8];
        for (int i = 0; i < 8; i++) {
            rowLayout[i] = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            rowLayout[i].setOrientation(LinearLayout.HORIZONTAL);
            rowLayout[i].setLayoutParams(params);
            mainLayout.addView(rowLayout[i]);
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                grid[i][j] = new MyButton(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
                grid[i][j].setLayoutParams(params);
                grid[i][j].setX(i);
                grid[i][j].setY(j);
                grid[i][j].setScaleType(ImageView.ScaleType.CENTER_CROP);
                grid[i][j].setBackgroundResource(R.drawable.rect);
                grid[i][j].setOnClickListener(this);
                if ((i == 3 && j == 3) || (i == 4 && j == 4)) {
                    grid[i][j].setClicked(true);
                    grid[i][j].setBlack(false);
                    grid[i][j].setImageResource(R.drawable.white);
                }
                if ((i == 4 && j == 3) || (i == 3 && j == 4)) {
                    grid[i][j].setClicked(true);
                    grid[i][j].setImageResource(R.drawable.black);
                    grid[i][j].setBlack(true);
                }
                rowLayout[i].addView(grid[i][j]);
            }
        }
    }

    public void updateBoard() {
        counter = 0;
        int valid = 0, i, j;
        boolean flag;
        for (int olc = 0; olc < 8; olc++) {
            for (int ilc = 0; ilc < 8; ilc++) {
                if (grid[olc][ilc].getClicked())
                    continue;
                if (blackTurn) {
                    i = olc + 1;
                    j = ilc + 1;
                    flag = false;
                    valid = 0;
                    // Diagonal Right-Down
                    while (i < 8 && j < 8 && grid[i][j].getClicked()) {
                        if (grid[i][j].getBlack() && valid == 0) {
                            break;
                        }
                        if (!grid[i][j].getBlack())
                            valid = 1;
                        if (valid == 1 && grid[i][j].getBlack()) {
                            flag = true;
                            break;
                        }
                        i++;
                        j++;
                    }
                    i = olc - 1;
                    j = ilc - 1;
                    valid = 0;
                    // Up Left
                    while (i >= 0 && j >= 0 && !flag && grid[i][j].getClicked()) {

                        if (grid[i][j].getBlack() && valid == 0) {
                            break;
                        }
                        if (!grid[i][j].getBlack())
                            valid = 1;
                        if (valid == 1 && grid[i][j].getBlack()) {
                            flag = true;
                            break;
                        }
                        i--;
                        j--;
                    }
                    i = olc - 1;
                    j = ilc + 1;
                    valid = 0;
                    //UP-Right
                    while (i >= 0 && j < 8 && !flag && grid[i][j].getClicked()) {

                        if (grid[i][j].getBlack() && valid == 0) {
                            break;
                        }
                        if (!grid[i][j].getBlack())
                            valid = 1;
                        if (valid == 1 && grid[i][j].getBlack()) {
                            flag = true;
                            break;
                        }
                        i--;
                        j++;
                    }
                    i = olc + 1;
                    j = ilc - 1;
                    valid = 0;
                    //Down- LEFT
                    while (i < 8 && j >= 0 && !flag && grid[i][j].getClicked()) {

                        if (grid[i][j].getBlack() && valid == 0) {
                            break;
                        }
                        if (!grid[i][j].getBlack())
                            valid = 1;
                        if (valid == 1 && grid[i][j].getBlack()) {
                            flag = true;
                            break;
                        }
                        i++;
                        j--;
                    }
                    i = olc - 1;
                    j = ilc;
                    valid = 0;
                    //UP
                    while (i >= 0 && !flag && grid[i][j].getClicked()) {
                        if (grid[i][j].getBlack() && valid == 0) {
                            break;
                        }
                        if (!grid[i][j].getBlack())
                            valid = 1;
                        if (valid == 1 && grid[i][j].getBlack()) {
                            flag = true;
                            break;
                        }
                        i--;
                    }
                    i = olc + 1;
                    j = ilc;
                    valid = 0;
                    //Down
                    while (i < 8 && !flag && grid[i][j].getClicked()) {

                        if (grid[i][j].getBlack() && valid == 0) {
                            break;
                        }
                        if (!grid[i][j].getBlack())
                            valid = 1;
                        if (valid == 1 && grid[i][j].getBlack()) {
                            flag = true;
                            break;
                        }
                        i++;
                    }
                    i = olc;
                    j = ilc - 1;
                    valid = 0;
                    //Left
                    while (j >= 0 && !flag && grid[i][j].getClicked()) {

                        if (grid[i][j].getBlack() && valid == 0) {
                            break;
                        }
                        if (!grid[i][j].getBlack())
                            valid = 1;
                        if (valid == 1 && grid[i][j].getBlack()) {
                            flag = true;
                            break;
                        }
                        j--;
                    }
                    i = olc;
                    j = ilc + 1;
                    valid = 0;
                    //Right
                    while (j < 8 && !flag && grid[i][j].getClicked()) {

                        if (grid[i][j].getBlack() && valid == 0) {
                            break;
                        }
                        if (!grid[i][j].getBlack())
                            valid = 1;
                        if (valid == 1 && grid[i][j].getBlack()) {
                            flag = true;
                            break;
                        }
                        j++;
                    }
                }//******************************************************************************
                else {
                    i = olc + 1;
                    j = ilc + 1;
                    flag = false;
                    // Diagonal Right-Down
                    while (i < 8 && j < 8 && grid[i][j].getClicked()) {
                        if (!grid[i][j].getBlack() && valid == 0) {
                            break;
                        }
                        if (grid[i][j].getBlack())
                            valid = 1;
                        if (valid == 1 && !grid[i][j].getBlack()) {
                            Log.d("White-Turn", " Diagonal Right Down" + " " + olc + " " + ilc + "");
                            flag = true;
                            break;
                        }
                        i++;
                        j++;
                    }
                    i = olc - 1;
                    j = ilc - 1;
                    valid = 0;
                    // Up Left
                    while (i >= 0 && j >= 0 && !flag && grid[i][j].getClicked()) {

                        if (!grid[i][j].getBlack() && valid == 0) {
                            break;
                        }
                        if (grid[i][j].getBlack())
                            valid = 1;
                        if (valid == 1 && !grid[i][j].getBlack()) {
                            Log.d("White-Turn", " Up Left" + " " + olc + " " + ilc + "");
                            flag = true;
                            break;
                        }
                        i--;
                        j--;
                    }
                    i = olc - 1;
                    j = ilc + 1;
                    valid = 0;
                    //UP-Right
                    while (i >= 0 && j < 8 && !flag && grid[i][j].getClicked()) {

                        if (!grid[i][j].getBlack() && valid == 0) {
                            break;
                        }
                        if (grid[i][j].getBlack())
                            valid = 1;
                        if (valid == 1 && !grid[i][j].getBlack()) {
                            Log.d("White-Turn", " Diagonal Up Right" + " " + olc + " " + ilc + "");
                            flag = true;
                            break;
                        }
                        i--;
                        j++;
                    }
                    i = olc + 1;
                    j = ilc - 1;
                    valid = 0;
                    //Down- LEFT
                    while (i < 8 && j >= 0 && !flag && grid[i][j].getClicked()) {

                        if (!grid[i][j].getBlack() && valid == 0) {
                            break;
                        }
                        if (grid[i][j].getBlack())
                            valid = 1;
                        if (valid == 1 && !grid[i][j].getBlack()) {
                            Log.d("White-Turn", " Diagonal Left Down" + " " + olc + " " + ilc + "");
                            flag = true;
                            break;
                        }
                        i++;
                        j--;
                    }
                    i = olc - 1;
                    j = ilc;
                    valid = 0;
                    //UP
                    while (i >= 0 && !flag && grid[i][j].getClicked()) {
                        if (!grid[i][j].getBlack() && valid == 0) {
                            break;
                        }
                        if (grid[i][j].getBlack())
                            valid = 1;
                        if (valid == 1 && !grid[i][j].getBlack()) {
                            flag = true;
                            Log.d("White-Turn", " Up" + " " + olc + " " + ilc + "");
                            break;
                        }
                        i--;
                    }
                    i = olc + 1;
                    j = ilc;
                    valid = 0;
                    //Down
                    while (i < 8 && !flag && grid[i][j].getClicked()) {

                        if (!grid[i][j].getBlack() && valid == 0) {
                            break;
                        }
                        if (grid[i][j].getBlack())
                            valid = 1;
                        if (valid == 1 && !grid[i][j].getBlack()) {
                            Log.d("White-Turn", " Down" + " " + olc + " " + ilc + "");
                            flag = true;
                            break;
                        }
                        i++;
                    }
                    i = olc;
                    j = ilc - 1;
                    valid = 0;
                    //Left
                    while (j >= 0 && !flag && grid[i][j].getClicked()) {

                        if (!grid[i][j].getBlack() && valid == 0) {
                            break;
                        }
                        if (grid[i][j].getBlack())
                            valid = 1;
                        if (valid == 1 && !grid[i][j].getBlack()) {
                            Log.d("White-Turn", " Left" + " " + olc + " " + ilc + "");
                            flag = true;
                            break;
                        }
                        j--;
                    }
                    i = olc;
                    j = ilc + 1;
                    valid = 0;
                    //Right
                    while (j < 8 && !flag && grid[i][j].getClicked()) {

                        if (!grid[i][j].getBlack() && valid == 0) {
                            break;
                        }
                        if (grid[i][j].getBlack())
                            valid = 1;
                        if (valid == 1 && !grid[i][j].getBlack()) {
                            Log.d("White-Turn", " Right" + " " + olc + " " + ilc + "");
                            flag = true;
                            break;
                        }
                        j++;
                    }
                }
                if (flag) {
                    grid[olc][ilc].setCanClicked(true);
                    grid[olc][ilc].setImageResource(R.drawable.greyy);
                    counter++;
                } else {
                    grid[olc][ilc].setCanClicked(false);
                    grid[olc][ilc].setImageResource(R.drawable.tr);
                    grid[olc][ilc].setBackgroundResource(R.drawable.rect);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        MyButton button = (MyButton) view;
        if (button.getClicked() || gameOver || !button.isCanClicked())
            return;

        if (blackTurn) {
            button.setBlack(true);
            button.setClicked(true);
            button.setImageResource(R.drawable.black);
            b_c++;
        } else {
            button.setImageResource(R.drawable.white);
            button.setBlack(false);
            button.setClicked(true);
            w_c++;
        }
        //Flip
        if(blackTurn){
            checkBlack(button.getAtX(),button.getAtY());
        }
        else
            checkWhite(button.getAtX(),button.getAtY());
        button.setClicked(true);
        blackCount.setText(b_c+"");
        whiteCount.setText(w_c+"");
        blackTurn = !blackTurn;
        updateBoard();
        if (counter == 0) {
            blackTurn = !blackTurn;
            updateBoard();
            if (counter == 0)
            {
                gameOver = true;
                String s;
                if(b_c>w_c){
                    Toast.makeText(this, "Black Won", Toast.LENGTH_LONG).show();    s = "Black";
                }
                else if(w_c>b_c){
                    Toast.makeText(this, "White Won", Toast.LENGTH_LONG).show();    s = "White";
                }
                else{
                    Toast.makeText(this, "Draw", Toast.LENGTH_LONG).show(); s = "Tie\nNo one";
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(s + " Won the Game")
                        .setMessage("Game Ends Here !")
                        .setCancelable(false)
                        .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //restart the game
                                restart();
                            }
                        })
                        .setNegativeButton("Review Game", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
            else
                Toast.makeText(this, "PLEASE PASS", Toast.LENGTH_LONG).show();
        }
        if(blackTurn)
        {
            imageWhite.setImageResource(R.drawable.tr);
            imageBlack.setImageResource(R.drawable.black);
        }
        else
        {
            imageWhite.setImageResource(R.drawable.white);
            imageBlack.setImageResource(R.drawable.tr);
        }
    }

    public void checkBlack(int row,int col){

        int i,j,count;
        //UP
        i = row-1;
        j = col;
        count = 0;
        while(i>=0&&grid[i][j].getClicked()&&!grid[i][j].getBlack()) {
            count++;
            i--;
        }
        if((i>=0&&i<8&&j>=0&&j<8&&!grid[i][j].getClicked())||!(i>=0&&i<8&&j>=0&&j<8))
        {
            count = 0;
        }
        b_c+=count;
        w_c-=count;
        while(count>0)
        {
            grid[row-count][col].setImageResource(R.drawable.black);
            grid[row-count][col].setBlack(true);
            count--;
        }
        //Down
        i = row+1;
        j = col;
        count = 0;
        while(i<8&&grid[i][j].getClicked()&&!grid[i][j].getBlack()) {
            count++;
            i++;
        }
        if((i>=0&&i<8&&j>=0&&j<8&&!grid[i][j].getClicked())||!(i>=0&&i<8&&j>=0&&j<8))
        {
            count = 0;
        }
        b_c+=count;
        w_c-=count;
        while(count>0)
        {
            grid[row+count][col].setImageResource(R.drawable.black);
            grid[row+count][col].setBlack(true);
            count--;
        }
        //Left
        i = row;
        j = col-1;
        count = 0;
        while(j>=0&&grid[i][j].getClicked()&&!grid[i][j].getBlack())
        {
            j--;
            count++;
        }
        if((i>=0&&i<8&&j>=0&&j<8&&!grid[i][j].getClicked())||!(i>=0&&i<8&&j>=0&&j<8))
        {
            count = 0;
        }
        b_c+=count;
        w_c-=count;
        while(count>0)
        {
            grid[row][col-count].setImageResource(R.drawable.black);
            grid[row][col-count].setBlack(true);
            count--;
        }
        //Right
        i = row;
        j = col+1;
        count = 0;
        while(j<8&&grid[i][j].getClicked()&&!grid[i][j].getBlack())
        {
            j++;
            count++;
        }
        if((i>=0&&i<8&&j>=0&&j<8&&!grid[i][j].getClicked())||!(i>=0&&i<8&&j>=0&&j<8))
        {
            count = 0;
        }
        b_c+=count;
        w_c-=count;
        while(count>0)
        {
            grid[row][col+count].setImageResource(R.drawable.black);
            grid[row][col+count].setBlack(true);
            count--;
        }
        //Up_Right
        i = row-1;
        j = col+1;
        count = 0;
        while(j<8&&i>=0&&grid[i][j].getClicked()&&!grid[i][j].getBlack())
        {
            j++;
            i--;
            count++;
        }
        if((i>=0&&i<8&&j>=0&&j<8&&!grid[i][j].getClicked())||!(i>=0&&i<8&&j>=0&&j<8))
        {
            count = 0;
        }
        b_c+=count;
        w_c-=count;
        while(count>0)
        {
            grid[row-count][col+count].setImageResource(R.drawable.black);
            grid[row-count][col+count].setBlack(true);
            count--;
        }
        //up_left
        i = row-1;
        j = col-1;
        count = 0;
        while(j>=0&&i>=0&&grid[i][j].getClicked()&&!grid[i][j].getBlack())
        {
            j--;
            i--;
            count++;
        }
        if((i>=0&&i<8&&j>=0&&j<8&&!grid[i][j].getClicked())||!(i>=0&&i<8&&j>=0&&j<8))
        {
            count = 0;
        }
        b_c+=count;
        w_c-=count;
        while(count>0)
        {
            grid[row-count][col-count].setImageResource(R.drawable.black);
            grid[row-count][col-count].setBlack(true);
            count--;
        }
        //down_left
        i = row+1;
        j = col-1;
        count = 0;
        while(j>=0&&i<8&&grid[i][j].getClicked()&&!grid[i][j].getBlack())
        {
            j--;
            i++;
            count++;
        }
        if((i>=0&&i<8&&j>=0&&j<8&&!grid[i][j].getClicked())||!(i>=0&&i<8&&j>=0&&j<8))
        {
            count = 0;
        }
        b_c+=count;
        w_c-=count;
        while(count>0)
        {
            grid[row+count][col-count].setImageResource(R.drawable.black);
            grid[row+count][col-count].setBlack(true);
            count--;
        }
        //down_right
        i = row+1;
        j = col+1;
        count = 0;
        while(j<8&&i<8&&grid[i][j].getClicked()&&!grid[i][j].getBlack())
        {
            j++;
            i++;
            count++;
        }
        if((i>=0&&i<8&&j>=0&&j<8&&!grid[i][j].getClicked())||!(i>=0&&i<8&&j>=0&&j<8))
        {
            count = 0;
        }
        b_c+=count;
        w_c-=count;
        while(count>0)
        {
            grid[row+count][col+count].setImageResource(R.drawable.black);
            grid[row+count][col+count].setBlack(true);
            count--;
        }
    }

    public void checkWhite(int row, int col){
        int i,j,count;
        //UP
        i = row-1;
        j = col;
        count = 0;
        while(i>=0&&grid[i][j].getClicked()&&grid[i][j].getBlack()) {
            count++;
            i--;
        }
        if((i>=0&&i<8&&j>=0&&j<8&&!grid[i][j].getClicked())||!(i>=0&&i<8&&j>=0&&j<8))
        {
            count = 0;
        }
        b_c-=count;
        w_c+=count;
        while(count>0)
        {
            grid[row-count][col].setImageResource(R.drawable.white);
            grid[row-count][col].setBlack(false);
            count--;
        }
        //Down
        i = row+1;
        j = col;
        count = 0;
        while(i<8&&grid[i][j].getClicked()&&grid[i][j].getBlack()) {
            count++;
            i++;
        }
        if((i>=0&&i<8&&j>=0&&j<8&&!grid[i][j].getClicked())||!(i>=0&&i<8&&j>=0&&j<8))
        {
            count = 0;
        }
        b_c-=count;
        w_c+=count;
        while(count>0)
        {
            grid[row+count][col].setImageResource(R.drawable.white);
            grid[row+count][col].setBlack(false);
            count--;
        }
        //Left
        i = row;
        j = col-1;
        count = 0;
        while(j>=0&&grid[i][j].getClicked()&&grid[i][j].getBlack())
        {
            j--;
            count++;
        }
        if((i>=0&&i<8&&j>=0&&j<8&&!grid[i][j].getClicked())||!(i>=0&&i<8&&j>=0&&j<8))
        {
            count = 0;
        }
        b_c-=count;
        w_c+=count;
        while(count>0)
        {
            grid[row][col-count].setImageResource(R.drawable.white);
            grid[row][col-count].setBlack(false);
            count--;
        }
        //Right
        i = row;
        j = col+1;
        count = 0;
        while(j<8&&grid[i][j].getClicked()&&grid[i][j].getBlack())
        {
            j++;
            count++;
        }
        if((i>=0&&i<8&&j>=0&&j<8&&!grid[i][j].getClicked())||!(i>=0&&i<8&&j>=0&&j<8))
        {
            count = 0;
        }
        b_c-=count;
        w_c+=count;
        while(count>0)
        {
            grid[row][col+count].setImageResource(R.drawable.white);
            grid[row][col+count].setBlack(false);
            count--;
        }
        //Up_Right
        i = row-1;
        j = col+1;
        count = 0;
        while(j<8&&i>=0&&grid[i][j].getClicked()&&grid[i][j].getBlack())
        {
            j++;
            i--;
            count++;
        }
        if((i>=0&&i<8&&j>=0&&j<8&&!grid[i][j].getClicked())||!(i>=0&&i<8&&j>=0&&j<8))
        {
            count = 0;
        }
        b_c-=count;
        w_c+=count;
        while(count>0)
        {
            grid[row-count][col+count].setImageResource(R.drawable.white);
            grid[row-count][col+count].setBlack(false);
            count--;
        }
        //up_left
        i = row-1;
        j = col-1;
        count = 0;
        while(j>=0&&i>=0&&grid[i][j].getClicked()&&grid[i][j].getBlack())
        {
            j--;
            i--;
            count++;
        }
        if((i>=0&&i<8&&j>=0&&j<8&&!grid[i][j].getClicked())||!(i>=0&&i<8&&j>=0&&j<8))
        {
            count = 0;
        }
        b_c-=count;
        w_c+=count;
        while(count>0)
        {
            grid[row-count][col-count].setImageResource(R.drawable.white);
            grid[row-count][col-count].setBlack(false);
            count--;
        }
        //down_left
        i = row+1;
        j = col-1;
        count = 0;
        while(j>=0&&i<8&&grid[i][j].getClicked()&&grid[i][j].getBlack())
        {
            j--;
            i++;
            count++;
        }
        if((i>=0&&i<8&&j>=0&&j<8&&!grid[i][j].getClicked())||!(i>=0&&i<8&&j>=0&&j<8))
        {
            count = 0;
        }
        b_c-=count;
        w_c+=count;
        while(count>0)
        {
            grid[row+count][col-count].setImageResource(R.drawable.white);
            grid[row+count][col-count].setBlack(false);
            count--;
        }
        //down_right
        i = row+1;
        j = col+1;
        count = 0;
        while(j<8&&i<8&&grid[i][j].getClicked()&&grid[i][j].getBlack())
        {
            j++;
            i++;
            count++;
        }
        if((i>=0&&i<8&&j>=0&&j<8&&!grid[i][j].getClicked())||!(i>=0&&i<8&&j>=0&&j<8))
        {
            count = 0;
        }
        b_c-=count;
        w_c+=count;
        while(count>0)
        {
            grid[row+count][col+count].setImageResource(R.drawable.white);
            grid[row+count][col+count].setBlack(false);
            count--;
        }
    }

    @Override
    public void onBackPressed() {

        if (exit){
            super.onBackPressed();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you really want to QUIT ?")
                .setMessage("All progress will be LOST !")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        exit = true;
                        onBackPressed();
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        exit = false;
                        //do nothing
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

        exit = false;

    }



}
