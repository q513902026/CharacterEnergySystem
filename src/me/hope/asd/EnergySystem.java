package me.hope.asd;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.hope.asd.data.GamePlayer;
import me.hope.asd.util.FileUtil;

/**
 * EnergySystem插件主类
 * <hr>
 * <b>往MC中的每个玩家中添加能量系统和一些相关的事件<br>
 * 
 * @author HopeAsd
 * @see org.bukkit.plugin.java.JavaPlugin
 * @see java.lang.Runnable
 */
public class EnergySystem extends JavaPlugin implements Runnable {
	/**
	 * 在线玩家列表 存储玩家UUID和GamePlayer
	 */
	private static Map<UUID, GamePlayer> playLists = new HashMap<UUID, GamePlayer>();
	/**
	 * 插件唯一实例
	 */
	private static EnergySystem INSTANCE;
	/**
	 * 插件唯一Logger
	 */
	private static Logger log;
	/**
	 * 插件监听器列表
	 */
	private static List<Listener> pluginlistener = new ArrayList<Listener>();
	/**
	 * 插件玩家数据文件夹地址
	 */
	private static String playerdataDirs = null;

	/**
	 * 插件启动方法
	 */
	@Override
	public void onEnable() {

		EnergySystem.INSTANCE = this;
		playerdataDirs = getFile().getPath();
		System.out.println("插件" + this.getClass().getSimpleName() + "启动中，加载日志！");

		try {
			EnergySystem.log = this.getLogger();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (log == null) {
				log = Logger.getLogger("Minecraft");
			}
		}

		Bukkit.getServer().getScheduler().runTaskLater(this, this, 40L);
		this.saveDefaultConfig();
		log.info("注册监听器!");
		registeredListener();
		log.info("插件开启完成!");

	}

	/**
	 * 插件关闭方法
	 */
	@Override
	public void onDisable() {

		try {
			saveGamePlayer();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		unregisteredListener();
		EnergySystem.INSTANCE = null;
		log.info("插件关闭完成!");
		log = null;

	}

	/**
	 * 统一注册监听器
	 */
	private void registeredListener() {
		for (Listener listener : pluginlistener) {
			log.info("开始加载监听器<" + listener.getClass().getName() + ">");
			Bukkit.getServer().getPluginManager().registerEvents(listener, getInstance());
			log.info("加载监听器<" + listener.getClass().getName() + "> 完成!");
		}
	}

	/**
	 * 统一解注册监听器
	 */
	private void unregisteredListener() {
		for (Listener listener : pluginlistener) {
			log.info("开始卸载监听器<" + listener.getClass().getName() + ">");
			HandlerList.unregisterAll(listener);
			log.info("卸载监听器<" + listener.getClass().getName() + "> 完成!");
		}
		pluginlistener.clear();
	}

	/**
	 * 往 {@link #pluginlistener} 中添加监听器
	 * 
	 * @param listener
	 *            详见{@linkplain org.bukkit.event.Listener Listener}
	 */
	public void addListener(Listener listener) {
		if (pluginlistener.contains(listener)) {
			return;
		}
		pluginlistener.add(listener);
	}

	/**
	 * 获取唯一实例
	 * 
	 * @return {@link #INSTANCE}
	 */
	public static EnergySystem getInstance() {
		return INSTANCE;
	}

	/**
	 * 在插件加载一段时间后 整体初始化
	 */
	@Override
	public void run() {
		if (Bukkit.getServer().getPluginManager().isPluginEnabled(this))
			for (Player aPlayer : Bukkit.getServer().getOnlinePlayers()) {
				playLists.put(aPlayer.getUniqueId(), getGamePlayer(aPlayer));
			}
	}

	/**
	 * 从文件中获取游戏玩家类
	 * 
	 * @param aPlayer
	 *            详见{@linkplain org.bukkit.entity.Player Player}类
	 * @return 详见{@linkplain me.hope.asd.data.GamePlayer 游戏玩家}类
	 */
	private GamePlayer getGamePlayer(Player aPlayer) {

		String playerfilepath = getPlayerFilePath(aPlayer);
		log.info("加载 -->" + playerfilepath);
		GamePlayer aGamePlayer = null;
		try {
			aGamePlayer = FileUtil.readObject(playerfilepath);
		} catch (FileNotFoundException e) {
			aGamePlayer = new GamePlayer(aPlayer);
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			log.info("加载完毕 -->" + aGamePlayer);
		}
		return aGamePlayer;
	}

	/**
	 * 获取特定玩家数据存储地址
	 * 
	 * @see #getGamePlayer(Player)
	 * @param uuid
	 *            详见{@linkplain org.bukkit.entity.Player Player}类
	 * @return 玩家数据存储地址
	 */
	private String getPlayerFilePath(Player uuid) {
		return getPlayerFilePath(uuid.getUniqueId().toString());
	}

	private String getPlayerFilePath(String uuid) {
		return playerdataDirs + "\\" + uuid;
	}

	/**
	 * 存储所有玩家数据的方法
	 * 
	 * @throws FileNotFoundException
	 *             当文件不存在时 ,处理详见
	 *             {@linkplain me.hope.asd.util.FileUtil#writeObject(Object, java.io.File)
	 *             writeObject}方法
	 * @throws IOException
	 *             出现IO错误时，则打印错误
	 */
	private void saveGamePlayer() throws FileNotFoundException, IOException {
		for (Map.Entry<UUID, GamePlayer> playerData : playLists.entrySet()) {
			String playerfilepath = getPlayerFilePath(playerData.getKey().toString());
			FileUtil.writeObject(playerData.getValue(), playerfilepath);
		}
	}

	/**
	 * 测试方法main
	 * 
	 * @param args
	 *            传入的参数
	 */
	public static void main(String[] args) {
		System.out.println("请把" + EnergySystem.class.getName() + "放入Plugins文件夹");
	}
}
