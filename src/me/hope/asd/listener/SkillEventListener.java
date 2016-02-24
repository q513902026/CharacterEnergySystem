package me.hope.asd.listener;

import java.util.logging.Logger;

import org.bukkit.event.Listener;

import me.hope.asd.EnergySystem;

public class SkillEventListener implements Listener {
	private Logger log;
	
	private EnergySystem eSystem;
	
	public SkillEventListener()
	{
		this.log = EnergySystem.getInstance().getLogger();
		this.eSystem = EnergySystem.getInstance();
		log.info("技能监视加载！");
		eSystem.addListener(this);
	}
	
	public static void main(String[] args)
	{
		
	}

}
