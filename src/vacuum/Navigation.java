package vacuum;

import floor.Tile;
import general.DataValidationException;

import java.util.ArrayList;
import java.util.List;

public class Navigation {

	/*
	 * 1. Come up with a "path" for general movement + vacuuming.
	 * 	- Simple
	 * 	- Check for nearby unvisited tiles
	 *	- Don't worry about battery usage or floor type
	 * 	- Less structured
	 * 	- wander() (?)
	 * 
	 * 2. Come up with a path to return to a charging station.
	 *	- Shortest (or most efficient) path
	 *	- Don't check for nearby tiles
	 *	- More structured
	 *
	 */

	private static Navigation instance;

	private Navigation() {}

	public static Navigation getInstance() {
		if (instance == null) {
			instance = new Navigation();
		}

		return instance;
	}

	CleanSweep cs = CleanSweep.getInstance();

	public void traverseWholeFloor() {
		List<Tile> successorTiles = new ArrayList<>();
        //TODO remove debug code

		do {
			// call clean code here
			successorTiles.clear();
            System.err.println(cs.getTile().getAdjacentTiles());
			for (Tile tile : cs.getTile().getAdjacentTiles()) {
				if (tile.getVisited() == 0){
                    System.err.println("Adding Successor tile" + cs.getTile().getDirectionTo(tile));
					successorTiles.add(tile);
				}
			}
			if (!successorTiles.isEmpty()) {
				try {
                    System.err.println("CS moving to " + cs.getTile().getDirectionTo(successorTiles.get(0)));
					cs.move(cs.getTile().getDirectionTo(successorTiles.get(0))); //picks direction (first added) - random?
				} catch (DataValidationException e) {
					e.printStackTrace();
				}
			} else {
                System.err.println("CS moving back");
				cs.moveBack();
			}
		} while (!cs.isVisitHistoryEmpty());
	}


	// TODO: Come up with an algorithm to find closest tile with dirt
	public static char[] calculatePath(Tile t1, Tile t2) {
		return null;
	}

	// TODO: Come up with an algorithm.
	public static char[] getEfficientPath(Tile t1, Tile t2) {
		return null;

	}


}
