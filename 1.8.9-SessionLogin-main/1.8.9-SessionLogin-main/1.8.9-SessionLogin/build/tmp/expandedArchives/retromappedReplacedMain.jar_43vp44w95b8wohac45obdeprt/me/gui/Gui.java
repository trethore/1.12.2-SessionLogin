package me.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.UUID;

import org.lwjgl.input.Keyboard;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.realmsclient.gui.ChatFormatting;

import me.main.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.util.Session;

public class Gui extends GuiScreen {
	private GuiScreen previousScreen;
	private GuiTextField sessionField;
	private ScaledResolution sr;

	public Gui(GuiScreen previousScreen) {
		this.previousScreen = previousScreen;
	}

	@Override
	public void func_73866_w_() {
		Keyboard.enableRepeatEvents(true);
		sr = new ScaledResolution(field_146297_k);

		sessionField = new GuiTextField(1, field_146297_k.field_71466_p, sr.func_78326_a() / 2 - 100, sr.func_78328_b() / 2,
				200, 20);
		sessionField.func_146203_f(32767);
		sessionField.func_146195_b(true);
		field_146292_n.add(
				new GuiButton(999, sr.func_78326_a() / 2 - 100, sr.func_78328_b() / 2 + 60, 90, 20, "Restore"));
		field_146292_n.add(
				new GuiButton(998, sr.func_78326_a() / 2 - 100, sr.func_78328_b() / 2 + 30, 200, 20, "Login"));
		field_146292_n.add(
				new GuiButton(997, sr.func_78326_a() / 2 + 10, sr.func_78328_b() / 2 + 60, 90, 20, "Multiplayer"));

		super.func_73866_w_();
	}

	@Override
	public void func_146281_b() {
		Keyboard.enableRepeatEvents(false);

		super.func_146281_b();
	}

	@Override
	public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
		func_146276_q_();
		field_146297_k.field_71466_p.func_78276_b("username:token:uuid",
				sr.func_78326_a() / 2 - field_146297_k.field_71466_p.func_78256_a("Username:token:uuid") / 2,
				sr.func_78328_b() / 2 - 25, Color.RED.getRGB());
		String status = ChatFormatting.WHITE + "User: " + ChatFormatting.DARK_GREEN + field_146297_k.func_110432_I().func_111285_a()
				+ ChatFormatting.WHITE + " UUID: " + ChatFormatting.DARK_GREEN + field_146297_k.func_110432_I().func_148255_b();
		field_146297_k.field_71466_p.func_78276_b(status, sr.func_78326_a() / 2 - field_146297_k.field_71466_p.func_78256_a(status) / 2,
				sr.func_78328_b() / 2 - 50, Color.WHITE.getRGB());
		sessionField.func_146194_f();

		super.func_73863_a(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void func_146284_a(GuiButton button) throws IOException {
		if (button.field_146127_k == 998
				&& !(sessionField.func_146179_b().toString().equals("") || sessionField.func_146179_b().toString() == null)) {
			try {
				String[] args = sessionField.func_146179_b().split(":");
				if (args.length == 3) {
					String name = args[0];
					String uuid = args[2];
					String token = args[1];
					Main.setSession(args[0], args[2], args[1], "mojang");
				}
				sessionField.func_146180_a("");
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (button.field_146127_k == 999) {
			try {
				Main.setSession(Main.originalSession.func_111285_a(), Main.originalSession.func_148255_b(),
						Main.originalSession.func_148254_d(), "mojang");
				sessionField.func_146180_a("");
				return;
			} catch (Exception e) {
			}
		}
		if (button.field_146127_k == 997) {
			field_146297_k.func_147108_a(previousScreen);
		}
		super.func_146284_a(button);
	}

	@Override
	protected void func_73869_a(char typedChar, int keyCode) throws IOException {
		sessionField.func_146201_a(typedChar, keyCode);
		if (Keyboard.KEY_ESCAPE == keyCode)
			field_146297_k.func_147108_a(previousScreen);
		else
			super.func_73869_a(typedChar, keyCode);
	}
}
