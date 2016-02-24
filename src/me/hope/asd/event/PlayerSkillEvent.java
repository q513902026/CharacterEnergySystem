package me.hope.asd.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.hope.asd.data.GamePlayer;
import me.hope.asd.data.SkillType;

public class PlayerSkillEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	
	private SkillType skillType;
	private GamePlayer gamePlayer;
	private int skillID;
	/**
	 * 
	 * @param gamePlayer 使用技能的玩家
	 * @param skillType 技能的类型
	 */
	public PlayerSkillEvent(GamePlayer gamePlayer,int skillID,SkillType skillType) {
		this.skillID = skillID;
		this.gamePlayer = gamePlayer;
		this.skillType =skillType;
	}
	public final GamePlayer getGamePlayer() {
		return gamePlayer;
	}

	public final SkillType getType() {
		return skillType;
	}
	public final int getSkillID() {
		return skillID;
	}
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}


}
