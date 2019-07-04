package amata1219.tosochu.game;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import amata1219.tosochu.Tosochu;

public class TosoGameListener implements Listener {

	@EventHandler
	public void onRejoin(PlayerJoinEvent event){

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){

	}

	@EventHandler
	public void onMove(PlayerMoveEvent event){
		Player player = event.getPlayer();

		GameAPI game = Tosochu.getPlugin().gameLoader.getGame();
		if(game == null)
			return;

		if(game.isHunter(player)){
			if(game.isFindRunaway(player)){
				game.setLockOnRunaway(player, true);
			}else{
				if(game.isLockOnRunaway(player))
					game.setLockOnRunaway(player, false);
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getAction() != Action.PHYSICAL)
			return;

		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if(block == null)
			return;

		GameAPI game = Tosochu.getPlugin().gameLoader.getGame();
		if(game == null)
			return;

		if(game.isDropout(player))
			game.tryRespawn(player);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event){
		if(event.getCause() != DamageCause.FALL)
			return;

		Entity entity = event.getEntity();
		if(!(entity instanceof Player))
			return;

		Player player = (Player) entity;
		GameAPI game = Tosochu.getPlugin().gameLoader.getGame();
		if(game == null)
			return;

		if(game.isRunaway(player))
			game.fall(player);
	}

	@EventHandler
	public void onRunawayCatch(EntityDamageByEntityEvent event){
		Entity entity = event.getEntity();
		Entity damager = event.getDamager();
		if(!(entity instanceof Player) || !(damager instanceof Player))
			return;

		Player runaway = (Player) entity;
		Player hunter = (Player) damager;

		GameAPI game = Tosochu.getPlugin().gameLoader.getGame();
		if(game == null)
			return;

		if(game.isHunter(hunter) && game.isRunaway(runaway))
			game.touchedByHunter(runaway);
	}

}
