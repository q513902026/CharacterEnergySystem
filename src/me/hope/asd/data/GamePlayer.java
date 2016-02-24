package me.hope.asd.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.hope.asd.EnergySystem;
import me.hope.asd.event.PlayerUseSkillEvent;

/**
 * 技能系统中的玩家
 * 
 * @author HopeAsd
 *
 */
public class GamePlayer implements Serializable {

	/**
	 * 序列化标示UID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 玩家识别码
	 */
	private UUID playerUUID;
	/**
	 * 能量上限
	 */
	private final int energy_limit;
	/**
	 * 当前能量值
	 */
	private int energy_current;
	/**
	 * 能量每秒恢复值
	 */
	private int energy_rate;
	/**
	 * 一秒钟进行几次Tick
	 */
	private static final long TICK_PERSENCOND = 20L;
	/**
	 * 技能锁 防止同时调用 作为GCD使用
	 */
	private boolean skillLock = false;
	/**
	 * 技能锁解锁时间
	 */
	private long skillGCD = 20L;

	public GamePlayer(Player aPlayer) {
		this.playerUUID = aPlayer.getUniqueId();
		this.energy_current = 0;
		this.energy_limit = 100;
		this.energy_rate = 10;
	}

	private GamePlayer() {
		this.playerUUID = UUID.randomUUID();
		this.energy_current = 0;
		this.energy_limit = 100;
		this.energy_rate = 10;
	}

	public GamePlayer(Player aPlayer, int energy_current, int energy_limit, int energy_rate) {
		this.playerUUID = aPlayer.getUniqueId();
		this.energy_current = energy_current;
		this.energy_limit = energy_limit;
		this.energy_rate = energy_rate;
	}

	public GamePlayer(String playerUUID, int energy_current, int energy_limit, int energy_rate) {
		this.playerUUID = UUID.fromString(playerUUID);
		this.energy_current = energy_current;
		this.energy_limit = energy_limit;
		this.energy_rate = energy_rate;
	}

	public final int getEnergyLimit() {
		return energy_limit;
	}

	public int getEnergyCurrent() {
		return energy_current;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(playerUUID);
	}

	public int getTickEnergyRate() {
		return (int) (energy_rate / TICK_PERSENCOND);
	}

	private void setEnergy(int amount) {
		energy_current = amount;
	}

	private boolean reduceEnergy(int amount) {
		if (amount < 0) {
			return false;
		}
		if (getEnergyCurrent() < amount) {
			return false;
		}
		setEnergy(getEnergyCurrent() - amount);

		return true;
	}

	/**
	 * 发送事件->根据事件反馈进行技能锁->根据事件反馈进行技能效果->事件结束
	 * 
	 * @param energy
	 *            技能消耗
	 * @param skill
	 *            技能类型
	 * @param attackedEntity
	 *            释放目标的实体
	 * @param skillID
	 *            技能id
	 */
	public void useSkill(int energy, SkillType skill, Entity attackedEntity, int skillID) {
		// 发送事件
		PlayerUseSkillEvent skillUseEvent = new PlayerUseSkillEvent(this, energy, skillID, skill, attackedEntity);
		Bukkit.getServer().getPluginManager().callEvent(skillUseEvent);
		// 根据事件反馈进行技能锁
		if (!skillUseEvent.isCancelled()) {

			if (reduceEnergy(skillUseEvent.getEnergy())) {
				setSkillLock(true);
				Bukkit.getScheduler().runTaskLater(EnergySystem.getInstance(), new Runnable() {

					@Override
					public void run() {
						setSkillLock(false);

					}
				}, skillGCD);
			}
		}
		// TODO 根据事件反馈进行技能效果
		// TODO 事件收尾
	}

	public boolean isSkillLock() {
		return skillLock;
	}

	private boolean setSkillLock(boolean bool) {
		skillLock = bool;
		return skillLock;
	}

	/**
	 * 能量恢复计算 加入结构锁
	 */
	private void addEnergy() {
		if (isSkillLock()) {
			return;
		}
		int energy_temp = getEnergyCurrent() + getTickEnergyRate();
		if (energy_temp > energy_limit) {
			energy_current = energy_limit;
		} else {
			energy_current = energy_temp;
		}
	}

	/**
	 * 每tick执行的操作
	 */
	public void tick() {
		if (!skillLock) {
			addEnergy();
		}
		// do something
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{").append("playerUUID:" + playerUUID + ",")
				.append("energy_current:" + energy_current + ",").append("energy_limit:" + energy_limit + ",")
				.append("energy_rate:" + energy_rate + ",");
		sb.append("}");
		return sb.toString();
	}

	public static void main(String[] args) {
		GamePlayer game1 = new GamePlayer(), game2 = new GamePlayer("8cd7d696-2129-40ba-9495-29fb2aa80f41", 0, 100, 10);
		System.out.println(game1);
		System.out.println(game2);
		String file = "F:\\code\\workspace\\CharacterEnergySystem\\src\\my.out";
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(game1);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		ObjectInputStream oin = null;
		try {
			oin = new ObjectInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		GamePlayer game3 = null;
		try {
			game3 = (GamePlayer) oin.readObject();// 由Object对象向下转型为GamePlayer对象
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(game3);
	}
}
