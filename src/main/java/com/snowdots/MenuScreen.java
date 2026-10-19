package com.snowdots;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/** Меню на кнопку J: включение/выключение точек, зимы и сила снегопада. */
public class MenuScreen extends Screen {

	public MenuScreen() {
		super(Text.literal("Snow Dots"));
	}

	private static Text onOff(String label, boolean value) {
		return Text.literal(label + ": ")
				.append(Text.literal(value ? "ВКЛ" : "ВЫКЛ").formatted(value ? Formatting.GREEN : Formatting.RED));
	}

	private static Text levelText() {
		String s = switch (Config.snowLevel) {
			case 1 -> "слабый";
			case 3 -> "сильный";
			default -> "средний";
		};
		return Text.literal("Снегопад: " + s);
	}

	@Override
	protected void init() {
		int w = 200;
		int h = 20;
		int x = this.width / 2 - w / 2;
		int y = this.height / 2 - 62;

		// Заголовок (неактивная кнопка, чтобы не лезть в API отрисовки)
		ButtonWidget header = ButtonWidget.builder(Text.literal("❄ Snow Dots ❄"), b -> {
		}).dimensions(x, y, w, h).build();
		header.active = false;
		addDrawableChild(header);

		addDrawableChild(ButtonWidget.builder(onOff("Точки на цели", Config.targetDots), b -> {
			Config.targetDots = !Config.targetDots;
			Config.save();
			b.setMessage(onOff("Точки на цели", Config.targetDots));
		}).dimensions(x, y + 30, w, h).build());

		addDrawableChild(ButtonWidget.builder(onOff("Зима", Config.winter), b -> {
			Config.winter = !Config.winter;
			Config.save();
			b.setMessage(onOff("Зима", Config.winter));
		}).dimensions(x, y + 56, w, h).build());

		addDrawableChild(ButtonWidget.builder(levelText(), b -> {
			Config.snowLevel = Config.snowLevel % 3 + 1;
			Config.save();
			b.setMessage(levelText());
		}).dimensions(x, y + 82, w, h).build());

		addDrawableChild(ButtonWidget.builder(Text.literal("Готово"), b -> close())
				.dimensions(x, y + 116, w, h).build());
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
}
