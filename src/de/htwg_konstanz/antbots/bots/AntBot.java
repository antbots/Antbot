package de.htwg_konstanz.antbots.bots;


import java.io.IOException;

import de.htwg_konstanz.antbots.common_java_package.controller.AttackManager;
import de.htwg_konstanz.antbots.common_java_package.controller.Bot;
import de.htwg_konstanz.antbots.common_java_package.controller.DefendOwnHillManager;
import de.htwg_konstanz.antbots.common_java_package.controller.EnemyHillManager;
import de.htwg_konstanz.antbots.common_java_package.controller.GameInformations;
import de.htwg_konstanz.antbots.common_java_package.controller.Logger;
import de.htwg_konstanz.antbots.common_java_package.controller.attack.AlphaBeta;
import de.htwg_konstanz.antbots.common_java_package.controller.attack.AttackInit;
import de.htwg_konstanz.antbots.common_java_package.controller.attack.MaxN;
import de.htwg_konstanz.antbots.common_java_package.controller.boarder.BuildBoarder;
import de.htwg_konstanz.antbots.common_java_package.controller.helper.BreadthFirstSearch;
import de.htwg_konstanz.antbots.common_java_package.controller.helper.Pathfinding;

/**
 * 
 * @author Benjamin
 */
public class AntBot extends Bot {

	private static GameInformations gameI;
	private static BreadthFirstSearch bsf;
	private static Logger logger = new Logger("AntBot.txt");
	private static Pathfinding pathfinding;
	private static int turn = 0;
	private static BuildBoarder boarder;
	private static AttackInit attack;
	private static AlphaBeta gameStrategy;
	private static AttackManager attackManager;
	private static EnemyHillManager enemyHillManager;
	private static DefendOwnHillManager defendOwnHillManager;
	

	public static void main(String[] args) throws IOException {
		new AntBot().readSystemInput();
	}

	private void init() {
		gameI = gameStateInforamtions();
		gameI.setLogger(logger);
		bsf = new BreadthFirstSearch(gameI);
		pathfinding = new Pathfinding(gameI);
		boarder = new BuildBoarder(gameI);
		attack = new AttackInit(gameI);
		gameStrategy = new AlphaBeta();
		attackManager = new AttackManager();
		enemyHillManager = new EnemyHillManager();
		defendOwnHillManager = new DefendOwnHillManager();
		
	}

	@Override
	public void doTurn() {
		logger.log("---------------------------------------------------------------------------------------");
		if (turn == 0) {
			init();
		}
		gameI.initNextOrder();
		
		logger.log("TURN " + turn);
		boarder.buildBoarder();
		
		enemyHillManager.antsToEnemyHill();
		defendOwnHillManager.defendAntsToOwnHill();
		
		attackManager.markOwnAntsAsDangered();
		attackManager.markAntsToAttack();
		
		
		gameI.getFoodManager().markAntsToCollectFood();
		
		gameI.getMyAnts().forEach(a -> {
			logger.log("Process Ant: " + a.getAntPosition());
			a.doLogic();
			a.move();
		});

		logger.log("---------------------------------------------------------------------------------------");
		turn++;
	}
	
	

	public static AttackManager getAttackManager() {
		return attackManager;
	}

	public static GameInformations getGameI() {
		return gameI;
	}

	public static BreadthFirstSearch getBsf() {
		return bsf;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static Pathfinding getPathfinding() {
		return pathfinding;
	}

	public static int getTurn() {
		return turn;
	}

	public static BuildBoarder getBoarder() {
		return boarder;
	}

	public static AttackInit getAttack() {
		return attack;
	}

	public static AlphaBeta getGameStrategy() {
		return gameStrategy;
	}
	
	public static EnemyHillManager getEnemyHillManager() {
		return enemyHillManager;
	}
	
	public static DefendOwnHillManager getDefendOwnHillManager(){
		return defendOwnHillManager;
	}

	
}
