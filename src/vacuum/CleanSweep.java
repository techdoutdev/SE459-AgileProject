package vacuum;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import floor.Tile;
import floor.Tile.Direction;
import general.DataValidationException;

import static floor.Tile.Direction.*;

public class CleanSweep {

	private static CleanSweep instance;

	private Tile currentTile;
	private ArrayList<Tile> visited;	// Visited tiles
	private ArrayList<Tile> unvisited;	// Tiles seen but not visited
	private Stack<Tile> visitHistory;

	private CleanSweep() {}

	public static CleanSweep getInstance() {
		if (instance == null) {
			instance = new CleanSweep();
			instance.visited = new ArrayList<>();  // TODO don't think we need this since visit is tracked in tile [Alex]
			instance.unvisited = new ArrayList<>(); // TODO don't think we need this since visit is tracked in tile [Alex]
			instance.visitHistory = new Stack<>();
		}
		return instance;
	}

	/**
	 * Move the Clean Sweep to a different tile in the north, south, east, west direction
	 * @param direction
	 * @throws DataValidationException
	 */
	public void move(Direction direction) throws DataValidationException {
		switch (direction) {
			case NORTH:
				visitHistory.add(currentTile); //moving away from the current tile so adding it to the visited stack before moving
				currentTile = currentTile.getAdjacent(NORTH);
				break;

			case SOUTH:
				visitHistory.add(currentTile); //moving away from the current tile so adding it to the visited stack before moving
				currentTile = currentTile.getAdjacent(SOUTH);
				break;

			case EAST:
				visitHistory.add(currentTile); //moving away from the current tile so adding it to the visited stack before moving
				currentTile = currentTile.getAdjacent(EAST);
				break;

			case WEST:
				visitHistory.add(currentTile); //moving away from the current tile so adding it to the visited stack before moving
				currentTile = currentTile.getAdjacent(WEST);
				break;

			default:
				throw new DataValidationException("ERROR: Invalid direction");
		}

		// Re-categorizes the current tile from unvisited to visited
		currentTile.visit();
		visited.add(currentTile); // TODO don't think we need this since visit is tracked in tile [Alex]
		unvisited.remove(currentTile); // TODO don't think we need this since visit is tracked in tile [Alex]

		// Marks the new tile's adjacent tiles to unvisited
		for (Tile tile : currentTile.getAdjacentTiles()) {
			if (!visited.contains(tile) && !unvisited.contains(tile)) {
				unvisited.add(tile);
			}
		}
	}

    /**
     * Moves CS to the tile it visited before the current tile.
     * @return true if move back success, fail if move back failed (CS has to previous tile)
     */
	public boolean moveBack() {
        if (!visitHistory.empty()) { //prevents popping and empty stack
            try {
                move(currentTile.getDirectionTo(visitHistory.pop()));
                visitHistory.pop(); //removes original tile (before move) from stack as move puts it on the stack
            } catch (DataValidationException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
	}

	public void runVacuum() {
		while (!visitHistory.empty()) {
			// call clean code here
			List<Tile> successorTiles = new ArrayList<>();
			for (Tile tile : currentTile.getAdjacentTiles()) {
				if (tile.getVisited() == 0){
					successorTiles.add(tile);
				}
			}
			if (!successorTiles.isEmpty()) {
				try {
					move(currentTile.getDirectionTo(successorTiles.get(0)));
				} catch (DataValidationException e) {
					e.printStackTrace();
				}
			} else {
				moveBack();
			}
		}
	}

	public void followPath(Direction[] path) throws DataValidationException {
		for (Direction d : path) {
			move(d);
			// TODO: Vacuum when necessary (?)
		}
	}

	public Tile getTile() {
		return currentTile;
	}

	public void setTile(Tile tile) {
		currentTile = tile;
		currentTile.visit();
	}
	
	/**
	 * Resets all relevant values of the CleanSweep singleton.
	 */
	public void reset() {
		currentTile = null;
		visited.clear();
		unvisited.clear();
		visitHistory.clear();
		
	}
}
