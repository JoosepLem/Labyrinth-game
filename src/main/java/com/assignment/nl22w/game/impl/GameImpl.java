package com.assignment.nl22w.game.impl;

import com.assignment.nl22w.game.Game;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

@Component
@Slf4j
public class GameImpl implements Game {

	private static final char WALL = '1';
	private static final char START = 'X';
	private static final char SPACE = ' ';
	private char[][] labyrinth;
	private int rows;
	private int cols;

	/*public GameImpl(String filename) {
		readLabyrinthFromFile(filename);
	}*/
	public GameImpl(){
		readLabyrinthFromFile("src/main/resources/map1.txt");
	}

	private void readLabyrinthFromFile(String filename) {
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
			}
			String labyrinthString = sb.toString();
			String[] lines = labyrinthString.split(System.lineSeparator());
			rows = lines.length;
			cols = lines[0].length();
			labyrinth = new char[rows][cols];
			for (int i = 0; i < rows; i++) {
				labyrinth[i] = lines[i].toCharArray();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int solve() {
		int startRow = -1;
		int startCol = -1;
		boolean foundStart = false;

		// Find the starting position
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (labyrinth[i][j] == START) {
					startRow = i;
					startCol = j;
					foundStart = true;
					break;
				}
			}
			if (foundStart) {
				break;
			}
		}

		if (!foundStart) {
			return 0;
		}

		// Perform breadth-first search to find the shortest path
		boolean[][] visited = new boolean[rows][cols];
		Queue<GameImpl.Point> queue = new ArrayDeque<>();
		queue.offer(new GameImpl.Point(startRow, startCol));
		visited[startRow][startCol] = true;

		int[][] distances = new int[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				distances[i][j] = Integer.MAX_VALUE;
			}
		}
		distances[startRow][startCol] = 0;

		int[] dx = {0, 0, 1, -1};
		int[] dy = {1, -1, 0, 0};

		while (!queue.isEmpty()) {
			GameImpl.Point current = queue.poll();
			int currentRow = current.row;
			int currentCol = current.col;

			for (int i = 0; i < 4; i++) {
				int newRow = currentRow + dx[i];
				int newCol = currentCol + dy[i];

				if (isValidPosition(newRow, newCol) && !visited[newRow][newCol] && labyrinth[newRow][newCol] != WALL) {
					queue.offer(new GameImpl.Point(newRow, newCol));
					visited[newRow][newCol] = true;
					distances[newRow][newCol] = distances[currentRow][currentCol] + 1;
				}
			}
		}

		// Find the exit position with the minimum distance
		int minDistance = Integer.MAX_VALUE;

		for (int i = 0; i < rows; i++) {
			if (distances[i][0] < minDistance) {
				minDistance = distances[i][0];
			}
			if (distances[i][cols - 1] < minDistance) {
				minDistance = distances[i][cols - 1];
			}
		}

		for (int j = 0; j < cols; j++) {
			if (distances[0][j] < minDistance) {
				minDistance = distances[0][j];
			}
			if (distances[rows - 1][j] < minDistance) {
				minDistance = distances[rows - 1][j];
			}
		}

		if (minDistance == Integer.MAX_VALUE) {
			return 0;
		}
		return minDistance;
	}

	private boolean isValidPosition(int row, int col) {
		return row >= 0 && row < rows && col >= 0 && col < cols;
	}

	private static class Point {
		int row;
		int col;

		Point(int row, int col) {
			this.row = row;
			this.col = col;
		}
	}
	@Override
	public int escapeFromTheWoods(Resource resource) throws IOException {
		//TODO start your journey here
		String filename = "src/main/resources/map1.txt";  // Replace with the actual filename
		GameImpl solver = new GameImpl();
		return solver.solve();
	}

}
