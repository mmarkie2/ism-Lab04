package pollub.ism.lab04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Array;
import java.text.MessageFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String TAG = "mmarkie2";

    Button[][] buttons;
    int currentTurnSign;
    boolean isGameEnded;
    int signsCounter;
    Button playAgainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    void init() {
        currentTurnSign = R.string.x_sign;

        buttons = new Button[3][3];
        buttons[0][0] = findViewById(R.id.button0_0);
        buttons[0][1] = findViewById(R.id.button0_1);
        buttons[0][2] = findViewById(R.id.button0_2);

        buttons[1][0] = findViewById(R.id.button1_0);
        buttons[1][1] = findViewById(R.id.button1_1);
        buttons[1][2] = findViewById(R.id.button1_2);

        buttons[2][0] = findViewById(R.id.button2_0);
        buttons[2][1] = findViewById(R.id.button2_1);
        buttons[2][2] = findViewById(R.id.button2_2);

        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setText(" ");
            }
        }

        isGameEnded = false;
        signsCounter = 0;
        playAgainButton = findViewById(R.id.playAgain);
    }

    public void onPlayAgain(View view) {
        init();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText(" ");
            }
        }
    }

    int[] getCords(String buttonName) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getResources().getResourceEntryName(buttons[i][j].getId()).equals(buttonName)) {
                    int ret[] = {i, j};
                    return ret;
                }

            }
        }
        return null;
    }

    public void onButtonClick(View view) {

        if (isGameEnded) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.tableLayout), "Game ended!", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }

        String buttonName = view.getResources().getResourceEntryName(view.getId());
        Log.i(TAG, MessageFormat.format("clicked {0}", buttonName));


        int cords[] = getCords(buttonName);
        if (cords != null) {
            int x = cords[0];
            int y = cords[1];

            Log.i(TAG, MessageFormat.format("cords {0} {1}", x, y));

            if (isCordOccupied(x, y)) {
                Log.i(TAG, "this place is occupied");
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.tableLayout), "this place is occupied", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {

                buttons[x][y].setText(currentTurnSign);
                signsCounter++;
                Log.i(TAG, MessageFormat.format("signsCounter: {0}", signsCounter));

                changeCurrentSign();


                String winnerSign = checkForWinner();
                if (!winnerSign.equals(" ")) {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.tableLayout), "Winner is: " + winnerSign, Snackbar.LENGTH_LONG);
                    snackbar.show();
                    isGameEnded = true;
                } else if (signsCounter == 9) {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.tableLayout), "No winner", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    isGameEnded = true;
                }
                if (isGameEnded) {

                }


            }

        } else {
            Log.i(TAG, "incorrect button name");
        }

    }

    void changeCurrentSign() {
        if (currentTurnSign == R.string.x_sign) {
            currentTurnSign = R.string.o_sign;
        } else {
            currentTurnSign = R.string.x_sign;
        }

    }

    boolean isCordOccupied(int x, int y) {
        return !buttons[x][y].getText().equals(" ");
    }

    //checks if 3 signs were placed in one line, else returns " "
    String getSignOf3Cells(String a, String b, String c) {
        if (a.equals(b) && b.equals(c)) {
            return a;
        } else {
            return " ";
        }
    }

    String checkForWinner() {
        String sign;
        for (int i = 0; i < 3; i++) {
            //row
            sign = getSignOf3Cells(buttons[i][0].getText().toString(), buttons[i][1].getText().toString(), buttons[i][2].getText().toString());
            if (!sign.equals(" ")) {
                return sign;
            }
            //column
            sign = getSignOf3Cells(buttons[0][i].getText().toString(), buttons[1][i].getText().toString(), buttons[2][i].getText().toString());
            if (!sign.equals(" ")) {
                return sign;
            }

        }
        //diagonal1
        sign = getSignOf3Cells(buttons[0][0].getText().toString(), buttons[1][1].getText().toString(), buttons[2][2].getText().toString());
        if (!sign.equals(" ")) {
            return sign;
        }
        //diagonal2
        sign = getSignOf3Cells(buttons[0][2].getText().toString(), buttons[1][1].getText().toString(), buttons[2][0].getText().toString());
        if (!sign.equals(" ")) {
            return sign;
        }

        return " ";
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        String[][] signs = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                signs[i][j] = buttons[i][j].getText().toString();
            }
        }
        outState.putSerializable("signs", signs);


        outState.putInt("currentTurnSign", currentTurnSign);
        outState.putBoolean("isGameEnded", isGameEnded);
        outState.putInt("signsCounter", signsCounter);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String[][] signs = (String[][]) savedInstanceState.getSerializable("signs");

        if (signs != null) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    buttons[i][j].setText(signs[i][j]);
                }
            }
        }

        currentTurnSign = savedInstanceState.getInt("currentTurnSign");
        isGameEnded = savedInstanceState.getBoolean("isGameEnded");
        signsCounter = savedInstanceState.getInt("signsCounter");
    }

}