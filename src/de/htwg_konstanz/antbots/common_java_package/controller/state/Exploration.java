package de.htwg_konstanz.antbots.common_java_package.controller.state;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.OverlayLayout;

import de.htwg_konstanz.antbots.bots.AntBot;
import de.htwg_konstanz.antbots.common_java_package.controller.Ant;
import de.htwg_konstanz.antbots.common_java_package.model.Aim;
import de.htwg_konstanz.antbots.common_java_package.model.Configuration;
import de.htwg_konstanz.antbots.common_java_package.model.Ilk;
import de.htwg_konstanz.antbots.common_java_package.model.Tile;
import de.htwg_konstanz.antbots.visualizer.OverlayDrawer;
import de.htwg_konstanz.antbots.visualizer.OverlayDrawer.SubTile;

public class Exploration  implements State{
	
	private Ant ant;
	private StateName stateName;
	private Tile destination;

	public Exploration(Ant a) {
		this.ant = a;
		stateName = StateName.Exploration;
	}

	@Override
	public void changeState() {
		if(AntBot.getAttackManager().getMarkedAnts().containsKey(ant)){
			ant.setState(new Attack(ant));
			return;
		}
		if(AntBot.getEnemyHillManager().getAntsToHill().containsKey(ant)) {
			ant.setState(new AttackEnemyHill(ant));
			return;
		}
		if(AntBot.getGameI().getFoodManager().getMarkedAnts().containsKey(ant) && !ant.isDanger()){
			ant.setState(new CollectFood(ant));
			return;
		}
		if(!ant.isDanger() && !AntBot.getGameI().getFoodManager().getMarkedAnts().containsKey(ant) && AntBot.getGameI().getExplorerAnts() < Configuration.EXPLORERANTSLIMIT){
			return;
		}
	}

	@Override
	public void execute() {
		if(destination == null || destination.getType() != Ilk.UNKNOWN)  {
			int radius = (int) Math.sqrt(AntBot.getGameI().getViewRadius2()) + 2;

			Tile antTile = ant.getAntPosition();

			// get the tiles in viewradius+2
			Set<Tile> visibleTiles = AntBot.getGameI().getTilesInRadius(antTile, radius);


			List<Tile> route = null;
			Tile target = null;
			List<Tile> targets = null;

			// get the route of the closest of the highest exploration tiles.
			while (route == null) {
				// get the tile with die highest exploreValue
				targets = AntBot.getGameI().getTilesToExplore(visibleTiles);
				
//				for(Tile t : targets) {
//					OverlayDrawer.setFillColor(Color.LIGHT_GRAY);
//					OverlayDrawer.drawTileSubtile(t.getRow(), t.getCol(),
//							SubTile.BM);
//				}

				
				target = targets.iterator().next();
				destination = target;
				// get the route to the target
				route = AntBot.getPathfinding().aStar(antTile, target);
				
				
				// target is not rachable -> remove it form visitable
				visibleTiles.remove(target);
			}
			
			// draw
					for (Tile rTile : route) {
						OverlayDrawer.setFillColor(Color.WHITE);
						OverlayDrawer.drawTileSubtile(rTile.getRow(), rTile.getCol(),
								SubTile.MM);
					}
					route.remove(0);
			ant.setRoute(route);
			AntBot.getLogger().log("Route is set: " + ant.getRoute());
		} else {
			List<Tile> route = null;
			route = AntBot.getPathfinding().aStar(ant.getAntPosition(), destination);
			
//			try {
//				route.remove(0);
//			} catch (Exception e) {
//				AntBot.debug().log("DEBUGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
//				AntBot.debug().log(destination + " typ " + destination.getType());
//			}
			
			ant.setRoute(route);
			for (Tile rTile : route) {
				OverlayDrawer.setFillColor(Color.WHITE);
				OverlayDrawer.drawTileSubtile(rTile.getRow(), rTile.getCol(),
						SubTile.MM);
			}
		}
		
		

	}
	
	@Override
	public String toString() {
		return "Exploration State";
	}
	
	public StateName getStateName() {
		return stateName;
	}

	@Override
	public void stateEnter() {
		AntBot.getGameI().increaseExplorerAnts();
		AntBot.getLogger().log(ant.getState().toString());
	}

	@Override
	public void stateExit() {
		AntBot.getGameI().decreaseExplorerAnts();
	}

}
