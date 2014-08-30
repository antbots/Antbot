package de.htwg_konstanz.antbots.common_java_package.model;

import de.htwg_konstanz.antbots.common_java_package.controller.Ant;

public class Food {

	private Tile position;
	private boolean inDemand;
	private boolean onOffer;
	private boolean isAlive;
	
	public Food(Tile position) {
		this.position = position;
		this.inDemand = false;
		this.onOffer = true;
		isAlive = false;
	}

	public Tile getPosition() {
		return position;
	}

	public void setPosition(Tile position) {
		this.position = position;
	}

	public boolean isOnOffer() {
		return onOffer;
	}

	public void setOnOffer(boolean onOffer) {
		this.onOffer = onOffer;
	}

	public boolean isInDemand() {
		return inDemand;
	}

	public void setInDemand(boolean inDemand) {
		this.inDemand = inDemand;
	}

	public void setAlive(boolean b) {
		isAlive = b;
	}

	public boolean isAlive() {
		return isAlive;
	}
	
	@Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof Food) {
        	Food food = (Food)o;
            result = position.getRow() == food.getPosition().getRow() && position.getCol() == food.getPosition().getCol();
        }
        return result;
    }
	
	
}
