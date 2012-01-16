package main;

import java.util.Vector;
import main.entity.*;

public class EntityManager {
	public static Vector<Entity> entityTable = new Vector<Entity>(256, 16);
	
	public static Entity_player ENT_PLAYER;
	
	public static void init() {
		ENT_PLAYER = new Entity_player();
		entityTable.add(0, ENT_PLAYER);
		
		for(int i = 0; i < entityTable.size(); i++) {
			entityTable.get(i).init();
		}
	}
	
	public static int getEntIndex(Entity ent) {
		return entityTable.indexOf(ent);
	}
	
	public static Entity getEntByIndex(int i) {
		return entityTable.get(i);
	}
	
	public static Entity getEntByName(String name) {
		for(int i = 0; i < entityTable.size(); i++) {
			if(entityTable.get(i).getName().equals(name))
				return entityTable.get(i);
		}
		return null;
	}
	
	public static int getTableLength() {
		return entityTable.size();
	}
	
	public static Vector<Entity> getEntsByType(String entType) {
		Vector<Entity> outTable = new Vector<Entity>(1, 1);
		for(int i = 0; i < entityTable.size(); i++) {
			if(entityTable.get(i).getType().equals(entType))
				outTable.add(entityTable.get(i));				
		}
		return outTable;
	}
	
	public static void addEntToTable(Entity ent) {
		if(getEntIndex(ent) == -1) {
			entityTable.add(ent);
		}
	}
	
	public static void addEntsToTable(Vector<Entity> oldTable) {
		for(int i = 0; i < oldTable.size(); i++) {
			if(entityTable.indexOf(oldTable.get(i)) != -1)
				entityTable.add(oldTable.get(i));
		}
	}
	
	public static Entity_player getPlayer() {
		return (Entity_player)getEntByIndex(0);
	}
	
	public static void drawEntities() {
		for(int i = 0; i < entityTable.size(); i++) {
			entityTable.get(i).draw();
		}
	}
	
	
}
