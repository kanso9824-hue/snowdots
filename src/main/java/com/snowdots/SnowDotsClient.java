package com.snowdots;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.ThreadLocalRandom;

public class SnowDotsClient implements ClientModInitializer {
	private static KeyBinding menuKey;

	private LivingEntity target;
	private int targetTicks;
	private int tick;

	@Override
	public void onInitializeClient() {
		Config.load();

		KeyBinding.Category category = KeyBinding.Category.create(Identifier.of("snowdots", "main"));
		menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.snowdots.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_J, category));

		ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
	}

	private void onTick(MinecraftClient client) {
		while (menuKey.wasPressed()) {
			if (client.currentScreen == null) {
				client.setScreen(new MenuScreen());
			}
		}

		ClientWorld world = client.world;
		ClientPlayerEntity player = client.player;
		if (world == null || player == null) {
			target = null;
			return;
		}
		tick++;

		if (Config.targetDots) {
			updateTarget(client, player);
			if (target != null) {
				spawnDots(world, target);
			}
		} else {
			target = null;
		}

		if (Config.winter) {
			spawnSnow(world, player);
		}
	}

	/** Цель = существо, на которое смотришь. После потери прицела точки держатся ещё ~1.5 сек. */
	private void updateTarget(MinecraftClient client, ClientPlayerEntity player) {
		if (client.crosshairTarget instanceof EntityHitResult hit
				&& hit.getEntity() instanceof LivingEntity living
				&& living != player
				&& living.isAlive()) {
			target = living;
			targetTicks = 30;
		} else if (targetTicks > 0) {
			targetTicks--;
			if (target == null || !target.isAlive() || target.isRemoved()) {
				target = null;
				targetTicks = 0;
			}
		} else {
			target = null;
		}
	}

	/** Белые светящиеся точки, вращающиеся вокруг цели. Частицы рисуются с обычной глубиной — сквозь стены не видны. */
	private void spawnDots(ClientWorld world, LivingEntity e) {
		ThreadLocalRandom rnd = ThreadLocalRandom.current();
		double height = e.getHeight();
		double radius = e.getWidth() * 0.5 + 0.3;
		int count = 3;
		for (int i = 0; i < count; i++) {
			double angle = tick * 0.16 + i * (Math.PI * 2.0 / count);
			double yFactor = 0.5 + 0.45 * Math.sin(tick * 0.08 + i * 2.1);
			double x = e.getX() + Math.cos(angle) * radius + (rnd.nextDouble() - 0.5) * 0.1;
			double z = e.getZ() + Math.sin(angle) * radius + (rnd.nextDouble() - 0.5) * 0.1;
			double y = e.getY() + height * yFactor;
			// Хочешь другой вид точек — замени END_ROD на другой тип частиц (например GLOW или ELECTRIC_SPARK).
			world.addParticleClient(ParticleTypes.END_ROD, x, y, z, 0.0, 0.0, 0.0);
		}
	}

	/** Снегопад вокруг игрока. */
	private void spawnSnow(ClientWorld world, ClientPlayerEntity player) {
		ThreadLocalRandom rnd = ThreadLocalRandom.current();
		int perTick = switch (Config.snowLevel) {
			case 1 -> 4;
			case 3 -> 16;
			default -> 9;
		};
		for (int i = 0; i < perTick; i++) {
			double x = player.getX() + (rnd.nextDouble() * 2.0 - 1.0) * 14.0;
			double z = player.getZ() + (rnd.nextDouble() * 2.0 - 1.0) * 14.0;
			double y = player.getEyeY() + 1.0 + rnd.nextDouble() * 8.0;
			world.addParticleClient(ParticleTypes.SNOWFLAKE, x, y, z,
					(rnd.nextDouble() - 0.5) * 0.02, -0.02 - rnd.nextDouble() * 0.03, (rnd.nextDouble() - 0.5) * 0.02);
		}
	}
}
