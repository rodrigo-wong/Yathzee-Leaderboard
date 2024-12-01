/*
* Starter code for the COMP 10185 Yahtzee Strategy Assignment
*
* The code achieves the following results:
*       Iterations: 5000000
*       Min Score: 22
*       Max Score: 793
*       Average Score: 138.28
*       Games>150: 22.59%
*       Games>200: 9.51%
*/
import java.util.Arrays;
import java.util.Map;

public class YahtzeeStrategy {
    final boolean _DEBUG_ = false;

    public void debugWrite( String str ) {
        if ( _DEBUG_ )
            System.out.println( str );
    }


    Yahtzee game = new Yahtzee();

    Map<Yahtzee.Boxes, Boolean> boxFilled;
    boolean[] keep; // flag array of dice to keep
    int[] roll;  // current turn's dice state

    // Track what pattern matches are in the roll.
    Map<Yahtzee.Boxes, Boolean> thisRollHas;

    public int play() {
        debugWrite( game.toString() );
        for (int turnNum = 1; turnNum <= 13; turnNum++) {
            debugWrite( "Playing turn " + turnNum + ": ");
            boxFilled = game.getScoreState();
            keep = new boolean[5];
            roll = game.play();

            debugWrite( "Turn " + turnNum + " Roll 1: " + Arrays.toString( roll ) );
            thisRollHas = game.has();

            // does the roll have a yahtzee?
            if (thisRollHas.get(Yahtzee.Boxes.Y)) {
                // note that set score can fail if the pattern doesn't actually match up or
                // if the box is already filled.  Not a problem with yahtzee but maybe for
                // other patterns it is a problem.
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

            // If we have a 3 of a kind or 4 of a kind, roll for yahtzee
            // otherwise roll all 5 dice

            if ( thisRollHas.get(Yahtzee.Boxes.SS )) {
                // the logic on this can be done backwards - a converse proposition
            
                keep = new boolean[]{true,true,true,true,true};
                // assume we want to keep ALL the dice, then find the one that doesn't fit
                // - once we find that one, we're done.
            
                // this works because we have already confirmed that there are 4
                // consecutive die faces from 5 dice.
                for ( int i=0; i<roll.length-1; i++ ) {
                    // if we get to the last die we'll go out of bounds using i+1, but we will also
                    // miss the "wrong" die if we don't test it
                    if ( i == roll.length-2 || ( tempRoll[i] != (tempRoll[i+1]+1) && tempRoll[i] != (tempRoll[i+1]-1) ) ) {
                        // note that in the case of a pair that is part of the sequence it
                        // will flag the first of the repeated digit, but that's ok, it's only
                        // us that have a preference to keep the order in a particular way
                        // -- our dice are not really an array, they're really a "set",
                        // mathematically speaking.
                        if ( i == roll.length - 2 )
                            keep[i+1] = false;
                        else
                            keep[i]=false;
            
                        break;
                    }
                }
            }
            
            
            if (thisRollHas.get(Yahtzee.Boxes.FK) || thisRollHas.get(Yahtzee.Boxes.TK)) {
                // if there is a 3 or 4 of a kind, the middle die is always
                // part of the pattern, keep any die that matches it
                for (int i = 0; i < roll.length; i++)
                    if (roll[i] == tempRoll[2]) keep[i] = true;
            }


            // START ROLL 2
            roll = game.play(keep);
            debugWrite( "Turn " + turnNum + " Roll 2: " + Arrays.toString( roll ) );
            thisRollHas = game.has();

            // NOTE THIS IS THE SAME AS ABOVE, WHICH IS SILLY!!!
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
            debugWrite( "Turn " + turnNum + " Roll 3: " + Arrays.toString( roll ) );
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

            if (thisRollHas.get(Yahtzee.Boxes.SS)) {
                if ( game.setScore(Yahtzee.Boxes.SS) )
                    continue;
            }

            if (thisRollHas.get(Yahtzee.Boxes.FK))
                if (game.setScore(Yahtzee.Boxes.FK)) {
                    continue;
                }

            // score it anywhere
            boolean scored = false;
            for (Yahtzee.Boxes b : Yahtzee.Boxes.values()) {
                switch (b) {
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

            debugWrite( game.toString() );
        }
        return game.getGameScore() >= 0 ? game.getGameScore() : 0;
    }

}
