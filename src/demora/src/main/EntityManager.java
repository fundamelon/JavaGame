package main;

import java.util.Vector;
import main.entity.*;

public class EntityManager {
	public enum ent_flags{
		SPAWN,
		DAMAGE,
		KILL,
		
		ITEM_GIVE,
		ITEM_TAKE,
		ITEM_USE,
		
		AI_THINK,
		AI_RESET,
		AI_SET_TARGET,
		AI_MOVETO
	}
	public static Vector<Entity> entityTable = new Vector<Entity>(256, 16);
	
	public static int getEntID(Object ent) {
		return entityTable.indexOf(ent);
	}
	
	public static Entity getEntByID(int id) {
		return entityTable.get(id);
	}
	
	public static Entity getEntByName(String name) {
		for(int i = 0; i < entityTable.size(); i++) {
			if(entityTable.get(i).getName().equals(name))
				return entityTable.get(i);
		}
		return null;
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
		if(getEntID(ent) == -1) {
			entityTable.add(ent);
		}
	}
	
	public static void addEntsToTable(Vector<Entity> oldTable) {
		for(int i = 0; i < oldTable.size(); i++) {
			if(entityTable.indexOf(oldTable.get(i)) != -1)
				entityTable.add(oldTable.get(i));
		}
	}
	
	
}
