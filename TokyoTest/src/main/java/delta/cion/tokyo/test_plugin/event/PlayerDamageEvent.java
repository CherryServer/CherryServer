package delta.cion.tokyo.test_plugin.event;

import delta.cion.tokyo.api.event.DeltaEvent;
import delta.cion.tokyo.test_plugin.pvp.CountDamage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.entity.EntityDamageEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerDamageEvent {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlayerDamageEvent.class);

	public static DeltaEvent<EntityDamageEvent> playerDamageEvent() {
		return new DeltaEvent<>(EntityDamageEvent.class, event -> {
			LOGGER.debug("playerDamageEvent registered.");
			Entity entity = event.getDamage().getAttacker();
			if (!(entity instanceof Player player)) return;

			Damage damage = event.getDamage();
			player.sendMessage("Damage count: ["+damage.getAmount()+"].");
			player.sendMessage("Damage type: ["+damage.getType()+"].");
			event.setCancelled(true);
		});
	}

	public static DeltaEvent<EntityAttackEvent> entityAttackEvent() {
		return new DeltaEvent<>(EntityAttackEvent.class, event -> {
			LOGGER.debug("entityAttackEvent registered.");
			Entity target = event.getTarget();
			if (!(target instanceof LivingEntity livingTarget)) {
				LOGGER.debug("Target {} [{}] is not LivingEntity", target, target.getEntityType().key());
				return;
			}

			RegistryKey<@NotNull DamageType> damageTypeRegistryKey = null;

			Entity attacker = event.getEntity();
			float damageCount = 0f;

			if (attacker instanceof Player player) {
				damageTypeRegistryKey = DamageType.PLAYER_ATTACK;
				ItemStack item = player.getItemInMainHand();
				damageCount = CountDamage.countDamage(item, attacker);
			} else if (!(attacker instanceof LivingEntity)) {
				damageTypeRegistryKey = DamageType.MOB_ATTACK;
				damageCount = 1f;
			}
			else {
				damageTypeRegistryKey = DamageType.MOB_ATTACK;
				damageCount = 1f;
			}

			Damage damage = new Damage(damageTypeRegistryKey, target, attacker, target.getPosition(), damageCount);
			livingTarget.damage(damage);
		});
	}
}
