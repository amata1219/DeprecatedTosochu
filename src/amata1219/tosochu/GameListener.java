package amata1219.tosochu;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import amata1219.tosochu.game.Game;
import amata1219.tosochu.game.scoreboard.StatesDisplayer;

public class GameListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		if(!shouldRun(null))
			return;

		Game.game.join(event.getPlayer());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();

		if(!shouldRun(player))
			return;

		Game.game.quit(player);
	}

	@EventHandler
	public void onRespawn(PlayerInteractEvent event){
		Player player = event.getPlayer();

		if(!shouldRun(player))
			return;

		Block block = event.getClickedBlock();
		if(block == null)
			return;

		if(block.getType() != Material.STONE_PLATE)
			return;

		Game.game.respawn(player);
	}

	@EventHandler
	public void onRunawayCatch(EntityDamageByEntityEvent event){
		Entity entity = event.getEntity();
		Entity damager = event.getDamager();
		if(!shouldRun(entity))
			return;

		event.setCancelled(true);

		if(!isPlayer(entity) || !isPlayer(damager))
			return;

		Game game = Game.game;
		Player hunter = (Player) entity;
		Player runaway = (Player) damager;
		if(!game.isHunter(hunter) || !game.isRunaway(runaway))
			return;

		game.becomeDropout(runaway);

		game.broadcast(runaway.getName() + "が確保されました");
	}

	@EventHandler
	public void onRunawayDamage(EntityDamageEvent event){
		Entity entity = event.getEntity();
		if(!shouldRun(entity))
			return;

		if(!isPlayer(entity))
			return;

		if(event.getCause() != DamageCause.FALL)
			return;

		Game game = Game.game;
		Player runaway = (Player) entity;
		if(game.isRunaway(runaway))
			game.fall(runaway);
	}

	@EventHandler
	public void onHeld(PlayerItemHeldEvent event){
		Player player = event.getPlayer();
		if(!shouldRun(player))
			return;

		Inventory inventory = player.getInventory();
		ItemStack item = inventory.getItem(event.getNewSlot());
		StatesDisplayer displayer = Game.game.getDisplayer(player);
		if(displayer != null)
			displayer.setDisplay(item == null ? false : item.getType() == Material.BOOK);
	}

	private boolean shouldRun(Entity entity){
		return Game.isInGame() && (entity == null || Game.game.world.equals(entity.getWorld()));
	}

	private boolean isPlayer(Entity entity){
		return entity instanceof Player;
	}

}
