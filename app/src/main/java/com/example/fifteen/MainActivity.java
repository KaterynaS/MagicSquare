package com.example.fifteen;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.plattysoft.leonids.ParticleSystem;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //interface elements
    private Button newGameButton;
    private TextView movesTextView;

    //game variables
    //board
    private int size = 4; //size of a board
    private int numberOfMoves;
    private int indexOfEmpty;
    int[] arrayOfSixteen;

    private ArrayList<TextView> cells;
    private static final int[] CELL_IDS = {
            R.id.cell1600, R.id.cell1601, R.id.cell1602, R.id.cell1603,
            R.id.cell1610, R.id.cell1611, R.id.cell1612, R.id.cell1613,
            R.id.cell1620, R.id.cell1621, R.id.cell1622, R.id.cell1623,
            R.id.cell1630, R.id.cell1631, R.id.cell1632, R.id.cell1633
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movesTextView = findViewById(R.id.moves);

        newGameButton = findViewById(R.id.bnew_game);
        newGameButton.setOnClickListener(this);

        cells = new ArrayList<TextView>();

        for(int id : CELL_IDS) {
            TextView cell = (TextView) findViewById(id);
            cell.setOnClickListener(this); // maybe
            cells.add(cell);
        }
        shuffle();
    }


    public void moveTile(int indexOfPressedCell)
    {
    //check if the adjacent tile is empty
        //get coordinates of a pressed tile
        int[] cellPressed = getCoordinates(indexOfPressedCell);
        int[] emptyCell = getCoordinates(indexOfEmpty);

        //compare with coordinates of an empty tile
        if((cellPressed[0] == emptyCell[0] &&
                Math.abs(cellPressed[1] - emptyCell[1]) == 1) ||
            (cellPressed[1] == emptyCell[1] &&
                   Math.abs(cellPressed[0] - emptyCell[0]) == 1))
        {

            //swap elements in arrayOfSixteen
            arrayOfSixteen[indexOfEmpty] = arrayOfSixteen[indexOfPressedCell];
            arrayOfSixteen[indexOfPressedCell] = 0;
            numberOfMoves++;
            movesTextView.setText("Moves: " + numberOfMoves);
            renewGrid();

            if(isSolved())
            {
                showAnimation();
                showVictoryDialog();
            }
        }
    }

    public void showAnimation()
    {
        ParticleSystem ps =
                new ParticleSystem(this, 1000, R.drawable.ic_flower, 500);
        ps.setSpeedRange(0.2f, 0.5f);
        ps.emit(movesTextView, 100);
                //.oneShot(anchorView, numParticles);
    }

    public int[] getCoordinates(int numberInOneDArray)
    {
        int row = numberInOneDArray / size;
        int column = numberInOneDArray % size;

        int[] coordinatesInGrid = {row, column};

        return coordinatesInGrid;
    }


    private void shuffle()
    {
        //shuffle tiles randomly
        //an array int[]{1, ..., 15} should be shuffled (until solvable)
        int[] arrayOfFifteen = RandomizeArray(1,15);;
        while (!isSolvable(arrayOfFifteen))
        {
            arrayOfFifteen = RandomizeArray(1,15);
        }

        //in a new arrayOfSixteen add 0 to the and
        arrayOfSixteen = new int[16];
        arrayOfSixteen[15] = 0;
        for (int i = 0; i < arrayOfFifteen.length; i++)
        {
                arrayOfSixteen[i] = arrayOfFifteen[i];
        }
        renewGrid();
    }


    private void renewGrid()
    {
        for (int i = 0; i < arrayOfSixteen.length; i++)
        {
            TextView cell = cells.get(i);
            if(arrayOfSixteen[i] == 0)
            {
                cell.setText("");
                cells.get(i).setBackgroundColor(getResources().getColor(R.color.buttonTextColor));
            }
            else
            {
                //String tileName = "" + arrayOfSixteen[i];
                cell.setText("" + arrayOfSixteen[i]);
                cells.get(i).setBackgroundColor(getResources().getColor(R.color.buttonBackgroundColor));
            }
        }
        indexOfEmpty = indexOfEmptyTile(arrayOfSixteen);
    }

    public static int[] RandomizeArray(int a, int b){
        Random rgen = new Random();  // Random number generator
        int size = b-a+1;
        int[] array = new int[size];

        for(int i=0; i< size; i++){
            array[i] = a+i;
        }

        for (int i=0; i<array.length; i++) {
            int randomPosition = rgen.nextInt(array.length);
            int temp = array[i];
            array[i] = array[randomPosition];
            array[randomPosition] = temp;
        }

        for(int s: array)
            System.out.println(s);

        return array;
    }


    private boolean isSolvable(int[] randomArray)
    {
        int countInversions = 0;

        for (int i = 0; i < randomArray.length; i++) {
            for (int j = 0; j < i; j++) {
                if (randomArray[j] > randomArray[i])
                    countInversions++;
            }
        }

        return countInversions % 2 == 0;
    }


    public static int indexOfEmptyTile(int[] a)
    {
        for (int i = 0; i < a.length; i++)
            if (a[i] == 0)
                return i;

        return -1;
    }

    @Override
    public void onClick(View v) {

        for(int i = 0; i < CELL_IDS.length; i++)
        {
            if(v.getId() == CELL_IDS[i])
            {
                moveTile(i);
            }
        }

        switch (v.getId())
        {
            case R.id.bnew_game:
                shuffle();
                numberOfMoves = 0;
                //movesTextView= findViewById(R.id.moves);
                movesTextView.setText("Moves: " + numberOfMoves);
                break;
            default:
                break;
        }
    }


    public void showVictoryDialog()
    {
        String message = "You solved it with " + numberOfMoves + " moves.\n" +
                "Play new game or quit?";
        //stop, greet and offer new game

//        AlertDialog.Builder alertDialogBuilder =
//                new AlertDialog.Builder
//                (new ContextThemeWrapper(this, R.style.AlertDialogCustom));

        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Play",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        shuffle();
                    }
                });
        alertDialogBuilder.setNegativeButton("Quit",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.setTitle("Victory!");
        alert.show();
    }

    private boolean isSolved()
    {
        boolean solved = true;

        if (arrayOfSixteen[15] != 0)
        {
            solved = false;
        }

        for (int i = 0; i < arrayOfSixteen.length - 1; i++)
        {
            if(arrayOfSixteen[i] != i + 1)
            {
                solved = false;
            }
        }

        return solved;
    }

}