package com.ricm3.packnight.model.highscores;

import java.util.Comparator;

public class HighscoreComparator implements Comparator<Score> {

	@Override
	public int compare(Score o1, Score o2) {
		int sc1 = o1.getScore();
        int sc2 = o2.getScore();

        if (sc1 > sc2){
            return -1;
        }else if (sc1 < sc2){
            return +1;
        }else{
            return 0;
        }
    
	}

}
