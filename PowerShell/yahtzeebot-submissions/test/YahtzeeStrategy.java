// Sample strategy worked up in class 2022-35.

/*
Iterations: 1000000
Min Score: 25
Max Score: 797
Average Score: 155.31
Games>150: 33.70%
Games>200: 13.81%
*/

import java.util.Arrays;
import java.util.Map;

public class YahtzeeStrategy {
    Yahtzee game = new Yahtzee();
    //System.out.println( game );

    // The basic structure of a turn is that you must roll the dice,
    // choose to score the hand or to reroll some/all of the dice.
    //
    // If scoring you must provide the decision making on what box to score.
    //
    // If rerolling you must provide the decision making on which dice to
    // reroll.
    //
    // You may score or reroll a second time after the first reroll.
    //
    // After the second reroll you must score or scratch (score a 0).


    // Used enumMap instead of boolean[] so I can use enums as indexes.
    // Keep track of which boxes I've already filled.
    Map<Yahtzee.Boxes, Boolean> boxFilled;

    boolean[] keep; // flag array of dice to keep
    int[] roll;  // current turn's dice state

    // EXAMPLE GAME PLAY
    // YOU SHOULD HEAVILY EDIT THIS LOGIC TO SUIT YOUR STRATEGY

    // Track what pattern matches are in the roll.
    Map<Yahtzee.Boxes, Boolean> thisRollHas;

    public int play() {
        for (int turnNum = 1; turnNum <= 13; turnNum++) {
            //System.out.println( "Playing turn " + turnNum + ": ");
            boxFilled = game.getScoreState();
            keep = new boolean[5];
            roll = game.play();

            // CHANGE THIS STRAGEGY TO SUIT YOURSELF
            // THIS STRATEGY KEEPS YAHTZEES, LARGE STRAIGHTS AND THAT'S IT.
            // YOU SHOULD BE ABLE TO MODULARLIZE THIS WITH SOME THOUGHT

            //System.out.println( "Turn " + turnNum + " Roll 1: " + Arrays.toString( roll ) );
            thisRollHas = game.has();

            // does the roll have a yahtzee?
            if (thisRollHas.get(Yahtzee.Boxes.Y)) {
                // note that set score can fail if the pattern doesn't actually match up or
                // if the box is already filled.  Not a problem with yahtzee but maybe for
                // other paterns it is a problem.
                if (game.setScore(Yahtzee.Boxes.Y)) {
                    continue;
                }
            }

            // does the roll have a large straight?
            if (thisRollHas.get(Yahtzee.Boxes.LS)) {
                if (game.setScore(Yahtzee.Boxes.LS)) {
                    continue;
                }
            }

            // DO NOT SORT THE ROLL ARRAY - the order is significant!!
            // Since it is easier to reason with sorted arrays, we clone the
            // roll and work off a temporary copy.
            int[] tempRoll = roll.clone();
            Arrays.sort(tempRoll);

            if ( thisRollHas.get(Yahtzee.Boxes.SS) ) {
                if ( !boxFilled.get(Yahtzee.Boxes.SS ) ||
                        !boxFilled.get(Yahtzee.Boxes.C ) ) {
                    // keep starts off all false
                    boolean found = false;
                    for ( int i = 0; i<roll.length-1; i++ ) {
                        if (tempRoll[i] + 1 == tempRoll[i + 1]) {
                            keep[i] = true;
                        } else {
                            found = true;
                        }
                    }
                    if ( found ) keep[roll.length-1] = true;
                }

            }

            // If we have a 3 of a kind or 4 of a kind, roll for yahtzee
            // otherwise roll all 5 dice
            if (thisRollHas.get(Yahtzee.Boxes.FK) || thisRollHas.get(Yahtzee.Boxes.TK)) {
                // if there is a 3 or 4 of a kind, the middle die is always
                // part of the pattern, keep any die that matches it
                for (int i = 0; i < roll.length; i++)
                    if (roll[i] == tempRoll[2]) keep[i] = true;
            }


            // START ROLL 2
            roll = game.play(keep);
            //System.out.println( "Turn " + turnNum + " Roll 2: " + Arrays.toString( roll ) );
            thisRollHas = game.has();

            // does the roll have a yahtzee?
            if (thisRollHas.get(Yahtzee.Boxes.Y)) {
                // note that set score can fail if the pattern doesn't actually match up or
                // if the box is already filled.  Not a problem with yahtzee but maybe for
                // other paterns it is a problem.
                if (game.setScore(Yahtzee.Boxes.Y)) {
                    continue;
                }
            }

            // does the roll have a large straight?
            if (thisRollHas.get(Yahtzee.Boxes.LS)) {
                if (game.setScore(Yahtzee.Boxes.LS)) {
                    continue;
                }
            }

            // DO NOT SORT THE ROLL ARRAY - the order is significant!!
            // Since it is easier to reason with sorted arrays, we clone the
            // roll and work off a temporary copy.
            tempRoll = roll.clone();
            Arrays.sort(tempRoll);

            if ( thisRollHas.get(Yahtzee.Boxes.SS) ) {
                if ( !boxFilled.get(Yahtzee.Boxes.SS ) ||
                        !boxFilled.get(Yahtzee.Boxes.C ) ) {
                    // keep starts off all false
                    boolean found = false;
                    for ( int i = 0; i<roll.length-1; i++ ) {
                        if (tempRoll[i] + 1 == tempRoll[i + 1]) {
                            keep[i] = true;
                        } else {
                            found = true;
                        }
                    }
                    if ( found ) keep[roll.length-1] = true;
                }

            }


            // If we have a 3 of a kind or 4 of a kind, roll for yahtzee
            // otherwise roll all 5 dice
            if (thisRollHas.get(Yahtzee.Boxes.FK) || thisRollHas.get(Yahtzee.Boxes.TK)) {
                // if there is a 3 or 4 of a kind, the middle die is always
                // part of the pattern, keep any die that matches it
                for (int i = 0; i < roll.length; i++)
                    if (roll[i] == tempRoll[2]) keep[i] = true;
            }


            // START ROLL 3
            roll = game.play(keep);
            //System.out.println( "Turn " + turnNum + " Roll 3: " + Arrays.toString( roll ) );
            thisRollHas = game.has();

            // MUST SCORE SOMETHING!!
            if (thisRollHas.get(Yahtzee.Boxes.Y))
                if (game.setScore(Yahtzee.Boxes.Y)) {
                    continue;
                }

            if (thisRollHas.get(Yahtzee.Boxes.LS)) {
                if (game.setScore(Yahtzee.Boxes.LS)) {
                    continue;
                }
            }

            int diesum = 0;
            for (int i=0; i<roll.length; i++) diesum += roll[i];

            if ( thisRollHas.get(Yahtzee.Boxes.SS ) ) {
                if ( game.setScore(Yahtzee.Boxes.SS)) {
                    continue;
                }

                if ( diesum >= 20 ) {
                    if ( game.setScore(Yahtzee.Boxes.C) ) {
                        continue;
                    }
                }
            }

            if (thisRollHas.get(Yahtzee.Boxes.FK) && diesum >= 16 )
                if (game.setScore(Yahtzee.Boxes.FK)) {
                    continue;
                }

            // score it anywhere
            boolean scored = false;
            for (Yahtzee.Boxes b : Yahtzee.Boxes.values()) {
                switch (b) {
                    // yes, at this point I wish I hadn't used strings ...
                    // but I can set priority by rearranging things
                    case U1:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore(Yahtzee.Boxes.U1);
                        break;
                    case U2:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore(Yahtzee.Boxes.U2);
                        break;
                    case U3:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore(Yahtzee.Boxes.U3);
                        break;
                    case U4:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore(Yahtzee.Boxes.U4);
                        break;
                    case U5:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore(Yahtzee.Boxes.U5);
                        break;
                    case U6:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore(Yahtzee.Boxes.U6);
                        break;
                    case TK:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore(Yahtzee.Boxes.TK);
                        break;
                    case FH:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore(Yahtzee.Boxes.FH);
                        break;
                    case SS:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore(Yahtzee.Boxes.SS);
                        break;
                    case C:
                        if (!boxFilled.get(b) && thisRollHas.get(b)) scored = game.setScore(Yahtzee.Boxes.C);
                        break;
                }

                if (scored) {
                    break;
                }
            }

            boolean scratched = false;
            if (!scored) {
                // must scratch, let's do it stupidly
                for (Yahtzee.Boxes b : Yahtzee.Boxes.values()) {
                    scratched = game.scratchBox(b);

                    if (scratched) {
                        break;
                    }
                }
            }

            if (!scored && !scratched)
                System.err.println("Invalid game state, can't score, can't scratch.");

            //System.out.println( game );
        }
        return game.getGameScore();
    }

}
