package com.ttt.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Winner {
	
	public static String chooseWinner(ArrayList<Integer> playerSymbolsPositions, ArrayList<Integer> computerSymbolsPositions) {
		
		String resultMessage = "";
		
		List firsTWinCase = Arrays.asList(1, 2, 3);
		List secondWinCase = Arrays.asList(4, 5, 6);
		List thirdWinCase = Arrays.asList(7, 8, 9);
		List fourthWinCase = Arrays.asList(1, 4, 7);
		List fifthWinCase = Arrays.asList(2, 5, 8);
		List sixthWinCase = Arrays.asList(3, 6, 9);
		List sevenththWinCase = Arrays.asList(1, 5, 9);
		List heightthWinCase = Arrays.asList(7, 5, 3);
		
		List<List> winningConditions = new ArrayList<List>();
		winningConditions.add(firsTWinCase);
		winningConditions.add(secondWinCase);
		winningConditions.add(thirdWinCase);
		winningConditions.add(fourthWinCase);
		winningConditions.add(fifthWinCase);
		winningConditions.add(sixthWinCase);
		winningConditions.add(sevenththWinCase);
		winningConditions.add(heightthWinCase);
		
		for (List winner : winningConditions) {
			if (playerSymbolsPositions.containsAll(winner)) {
				resultMessage = "You won yaatik alf saha bri!";
			}else if (computerSymbolsPositions.contains(winner)) {
				resultMessage = "Rakkez aad amoun";
			}
			else if(computerSymbolsPositions.size() + playerSymbolsPositions.size() == 9) {
				resultMessage = "jebtouha gadgad";
			}
		}
		return resultMessage;
	}
}
