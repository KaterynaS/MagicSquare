package com.example.fifteen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //interface elements
    private Button newGameButton;
    private TextView movesTextView;

    //game variables
    //board
    private int size = 4; //size of a board
    private int numberOfMoves;
    private int[] arr; //row column
    private int indexOfEmpty;
    boolean isFinished;
    int[] arrayOfSixteen;

    private List<Button> cells;
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

        cells = new ArrayList<Button>();
        // or slightly better
        // cells = new ArrayList<Button>(BUTTON_IDS.length);
        for(int id : CELL_IDS) {
            Button cell = (Button)findViewById(id);
            cell.setOnClickListener(this); // maybe
            cells.add(cell);
        }

        shuffle();
    }

    public void moveTile(int indexOfPressedCell)
    {
        //i is an number of a cell pressed in an array of cells int[] CELL_IDS


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
        }

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
        for (int i = 0; i < arrayOfSixteen.length; i++)
        {
            if(i == arrayOfSixteen.length - 1)
            {
                arrayOfSixteen[i] = 0;
            }
            else
            {
                arrayOfSixteen[i] = arrayOfFifteen[i];
            }
        }

        renewGrid();
    }


    private void renewGrid()
    {
        for (int i = 0; i < arrayOfSixteen.length; i++) {

            if(arrayOfSixteen[i] == 0)
            {
                Button cell = cells.get(i);
                cell.setText("");
                cells.get(i).setBackgroundColor(getResources().getColor(R.color.buttonTextColor));
            }
            else
            {
                Button cell = cells.get(i);
                String tileName = "" + arrayOfSixteen[i];
                cell.setText(tileName);
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


        if(!isFinished)
        {
            for(int i = 0; i < CELL_IDS.length; i++)
            {
                if(v.getId() == CELL_IDS[i])
                {
                    moveTile(i);
                    if(isSolved())
                    {
                        Toast.makeText(MainActivity.this,
                                "VICTORY",
                                Toast.LENGTH_LONG).show();
                    }
                }
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
            case R.id.cell1633:
                //todo
                break;
            default:
                break;
        }
    }



    private boolean isSolved() {

        if (arrayOfSixteen[15] != 0) // if blank tile is not in the solved position ==> not solved
            return false;

        for (int i = arrayOfSixteen.length - 1; i >= 0; i--) {
            if (arrayOfSixteen[i] != i + 1)
                return false;
        }


        return true;
    }

}
