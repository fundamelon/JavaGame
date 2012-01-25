package main.entity;

import java.util.ArrayList;
import java.util.Vector;
import main.entity.*;

public class EntityManager {
	public static ArrayList<Entity> mobile_ents = new ArrayList<Entity>();
	public static ArrayList<Entity> item_ents = new ArrayList<Entity>(256);
	public static ArrayList<Entity> env_ents = new ArrayList<Entity>(256);
	public static ArrayList<Entity> tile_ents = new ArrayList<Entity>();
	
	public static Entity_player ENT_PLAYER;
	
	public static void init() {
		ENT_PLAYER = new Entity_player();
		ENT_PLAYER.init(0, 0, false);
		mobile_ents.add(0, ENT_PLAYER);
		
	}
	
	public static int getIndex(Entity ent, String setname) {
		return getSet(setname).indexOf(ent);
	}
	
	public static Entity getByIndex(int i, String setname) {
		return getSet(setname).get(i);
	}
	
	public static Entity getByName(String name, String setname) {
		ArrayList<Entity> ent_list = getSet(setname);
		for(int i = 0; i < ent_list.size(); i++) {
			if(ent_list.get(i).getName().equals(name))
				return ent_list.get(i);
		}
		return null;
	}
	
	public static int getTableLength(String setname) {
		return getSet(setname).size();
	}
	
	public static ArrayList<Entity> getByType(String entType, String setname) {
		ArrayList<Entity> ent_list = getSet(setname);
		ArrayList<Entity> outTable = new ArrayList<Entity>();
		for(int i = 0; i < ent_list.size(); i++) {
			if(ent_list.get(i).getType().equals(entType))
				outTable.add(ent_list.get(i));				
		}
		return outTable;
	}
	
	public static void addToTable(Entity ent, String setname) {
		if(getIndex(ent, setname) == -1) {
			getSet(setname).add(ent);
		}
	}
	
	public static void addToTable(Vector<Entity> oldTable, String setname) {
		ArrayList<Entity> ent_list = getSet(setname);
		for(int i = 0; i < oldTable.size(); i++) {
			if(ent_list.indexOf(oldTable.get(i)) != -1)
				ent_list.add(oldTable.get(i));
		}
	}
	
	public static Entity_player getPlayer() {
		return (Entity_player)getByIndex(0, "mobile");
	}
	
	private static ArrayList<Entity> getSet(String name) {
		ArrayList<Entity> table;
		switch(name) {
		case "mobile":
			table = mobile_ents;
			break;
		case "item":
			table = item_ents;
			break;
		case "environment":
			table = env_ents;
			break;
		case "tile":
			table = tile_ents;
			break;
		default:
			table = mobile_ents;
		}
		return table;
	}
	
	public static void draw(String setname) {
		ArrayList<Entity> ent_list = getSet(setname);
		for(int i = 0; i < ent_list.size(); i++) {
			ent_list.get(i).draw();
		}
	}
	
	
}
