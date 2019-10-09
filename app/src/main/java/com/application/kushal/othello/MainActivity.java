package com.application.kushal.othello;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout mainLayout;
    MyButton[][] grid;
    LinearLayout[] rowLayout;
    private static boolean gameOver;
    private static boolean blackTurn;
    private static boolean exit = false;
    int b_c;
    int w_c;

    private boolean ONE_PLAYER;
    private ArrayList<MyButton> buttonArrayList = new ArrayList<>();
    ProgressBar progressBar;

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
        ONE_PLAYER = false;
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
        progressBar = findViewById(R.id.progressBar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.newGame) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Do you really want to REPLAY ?")
                    .setMessage("All progress will be LOST !")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                            Toast.makeText(MainActivity.this, "Restarted", Toast.LENGTH_SHORT).show();
                            finish();
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
        if (id == R.id.autoPlay){
            Toast.makeText(this, "<auto play is true>", Toast.LENGTH_SHORT).show();
            item.setVisible(false);
            ONE_PLAYER = true;
        }
        return true;
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
                    buttonArrayList.add(grid[olc][ilc]);
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
            buttonArrayList.clear();
            b_c++;
        } else {
            button.setImageResource(R.drawable.white);
            button.setBlack(false);
            button.setClicked(true);
            w_c++;
        }
        //Flip
        if(blackTurn)
            checkBlack(button.getAtX(),button.getAtY());
        else
            checkWhite(button.getAtX(),button.getAtY());

        button.setClicked(true);
        blackCount.setText(b_c+"");
        whiteCount.setText(w_c+"");
        blackTurn = !blackTurn;
        updateBoard();
//=>        for auto play stuff
        if (ONE_PLAYER && !blackTurn){
            progressBar.setVisibility(View.VISIBLE);
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (buttonArrayList.size()!=0){
                        int r = new Random().nextInt(buttonArrayList.size());
                        buttonArrayList.get(r).performClick();

                        buttonArrayList.clear();
                    }
                }
            }, 500);
        }

        if (counter == 0) {
            blackTurn = !blackTurn;
            updateBoard();
            if (counter == 0) {
                gameOver = true;
                final String s;

                if(b_c>w_c)
                    s = "Black Won This Game";
                else if(w_c>b_c)
                    s = "White Won This Game";
                else
                    s = "It's A Draw";

                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.setContentView(R.layout.sucess_dialog);
                        dialog.setCancelable(false);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                        TextView title = dialog.findViewById(R.id.main_title);
                        title.setText(s);

                        dialog.findViewById(R.id.review).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) { dialog.dismiss(); }});
                        dialog.findViewById(R.id.replay).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this, MainActivity.class));
                                finish();
                                dialog.dismiss(); }
                        });

                    }
                },400);


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
