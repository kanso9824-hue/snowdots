package com.snowdots;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Простые настройки, хранятся в config/snowdots.properties. */
public final class Config {
	public static boolean targetDots = true;
	public static boolean winter = true;
	/** 1 = слабый, 2 = средний, 3 = сильный снегопад */
	public static int snowLevel = 2;

	private static Path file() {
		return FabricLoader.getInstance().getConfigDir().resolve("snowdots.properties");
	}

	public static void load() {
		Path f = file();
		if (!Files.exists(f)) return;
		try (var in = Files.newInputStream(f)) {
			Properties p = new Properties();
			p.load(in);
			targetDots = Boolean.parseBoolean(p.getProperty("targetDots", "true"));
			winter = Boolean.parseBoolean(p.getProperty("winter", "true"));
			try {
				snowLevel = Math.max(1, Math.min(3, Integer.parseInt(p.getProperty("snowLevel", "2"))));
			} catch (NumberFormatException e) {
				snowLevel = 2;
			}
		} catch (IOException ignored) {
		}
	}

	public static void save() {
		Properties p = new Properties();
		p.setProperty("targetDots", Boolean.toString(targetDots));
		p.setProperty("winter", Boolean.toString(winter));
		p.setProperty("snowLevel", Integer.toString(snowLevel));
		try (var out = Files.newOutputStream(file())) {
			p.store(out, "Snow Dots");
		} catch (IOException ignored) {
		}
	}

	private Config() {
	}
}
