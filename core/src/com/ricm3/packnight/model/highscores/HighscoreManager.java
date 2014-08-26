package com.ricm3.packnight.model.highscores;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class HighscoreManager {
	 private ArrayList<Score> scores;
	 
	 private static final String HIGHSCORE_FILE = "scores.dat";
	 private final int max = 10;
	 ObjectOutputStream outputStream = null;
	 ObjectInputStream inputStream = null;

	 public HighscoreManager() {
	 
	        scores = new ArrayList<Score>();
	     }
	  public int getMax(){
		  return max;
	  }
	 
	 public ArrayList<Score> getScores() {
	        loadScoreFile();
	        sort();
	        return scores;
	    }


	 private void sort() {
	        HighscoreComparator comparator = new HighscoreComparator();
	        Collections.sort(scores, comparator);
	}


	@SuppressWarnings("unchecked")
	private void loadScoreFile() {
		 try {
	            inputStream = new ObjectInputStream(new FileInputStream(HIGHSCORE_FILE));
	            scores = (ArrayList<Score>) inputStream.readObject();
	        } catch (FileNotFoundException e) {
	            System.out.println("[Laad] FNF Error: " + e.getMessage());
	        } catch (IOException e) {
	            System.out.println("[Laad] IO Error: " + e.getMessage());
	        } catch (ClassNotFoundException e) {
	            System.out.println("[Laad] CNF Error: " + e.getMessage());
	        } finally {
	            try {
	                if (outputStream != null) {
	                    outputStream.flush();
	                    outputStream.close();
	                }
	            } catch (IOException e) {
	                System.out.println("[Laad] IO Error: " + e.getMessage());
	            }
	        }
	}
		
	
	public void addScore(String name, int score) {
        loadScoreFile();
        scores.add(new Score(name, score));
        updateScoreFile();
}


	private void updateScoreFile() {
		try {
            outputStream = new ObjectOutputStream(new FileOutputStream(HIGHSCORE_FILE));
            outputStream.writeObject(scores);
        } catch (FileNotFoundException e) {
            System.out.println("[Update] FNF Error: " + e.getMessage() + ",the program will try and make a new file");
        } catch (IOException e) {
            System.out.println("[Update] IO Error: " + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println("[Update] Error: " + e.getMessage());
            }
        }
		
	}
	
	
	public String getHighscoreString(ArrayList<Score> scores) {
        String highscoreString = "";

        int i = 0;
        int x = scores.size();
        if (x > max) {
            x = max;
        }
        while (i < x) {
            highscoreString += (i + 1) + ". " + scores.get(i).getName() + " " + scores.get(i).getScore() + "\n";
            i++;
        }
        return highscoreString;
	}
}
