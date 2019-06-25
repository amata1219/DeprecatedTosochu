package amata1219.tosochu;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import amata1219.tosochu.player.GamePlayer;

public class EventListener implements Listener {

	@EventHandler
	public void onTouch(EntityDamageByEntityEvent event){
		if(!isPlayer(event.getDamager()) || !isPlayer(event.getEntity()))
			return;

		PlayerManager manager = PlayerManager.getManager();
		GamePlayer damager = manager.toGamePlayer(((Player) event.getDamager()).getUniqueId());
		GamePlayer target = manager.toGamePlayer(((Player) event.getEntity()).getUniqueId());
		Game game = GameManager.getInstance();
		if(game.isHunter(damager) && game.isRunaway(target)){
			game.touch(damager, target);
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event){
		Game game = GameManager.getInstance();
		Player pl = event.getPlayer();
		GamePlayer player = PlayerManager.getManager().toGamePlayer(pl.getUniqueId());
		if(game.isHunter(player)){
			boolean find = false;
			for(Entity entity : pl.getNearbyEntities(25, 25, 25)){
				if(isPlayer(entity))
					find = true;
			}
			if(find)
				pl.setFoodLevel(20);
			else if(pl.getFoodLevel() > 4)
				pl.setFoodLevel(4);
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		PlayerManager.getManager().registerPlayer(event.getPlayer());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		PlayerManager.getManager().unregisterPlayer(event.getPlayer());
	}

	public boolean isPlayer(Entity entity){
		return entity instanceof Player;
	}

}
