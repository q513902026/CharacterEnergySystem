package me.hope.asd.event;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import me.hope.asd.data.GamePlayer;
import me.hope.asd.data.SkillType;

public class PlayerUseSkillEvent extends PlayerSkillEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	private int energy;
	private Entity attackedEntity;

	private boolean cancelled =false;

	/**
	 * @ @param
	 *       gamePlayer 使用技能的玩家
	 * @param energy
	 *            技能消耗
	 * @param skillID
	 *            技能ID
	 * @param skill
	 *            技能类型
	 * @param attackedEntity
	 *            技能面向的对象实体
	 * 
	 */
	public PlayerUseSkillEvent(GamePlayer gamePlayer, int energy, int skillID, SkillType skill, Entity attackedEntity) {
		super(gamePlayer, skillID, skill);
		this.energy = energy;
		this.attackedEntity = attackedEntity;
	}

	public Entity getAttackedEntity() {
		return attackedEntity;
	}

	public int getEnergy() {
		return Integer.valueOf(energy);
	}

	public void setAmount(int energy) {
		this.energy = energy;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

}
