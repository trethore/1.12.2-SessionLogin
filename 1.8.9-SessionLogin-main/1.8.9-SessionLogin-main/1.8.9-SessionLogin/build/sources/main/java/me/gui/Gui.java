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
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		sr = new ScaledResolution(mc);

		sessionField = new GuiTextField(1, mc.fontRendererObj, sr.getScaledWidth() / 2 - 100, sr.getScaledHeight() / 2,
				200, 20);
		sessionField.setMaxStringLength(32767);
		sessionField.setFocused(true);
		buttonList.add(
				new GuiButton(999, sr.getScaledWidth() / 2 - 100, sr.getScaledHeight() / 2 + 60, 90, 20, "Restore"));
		buttonList.add(
				new GuiButton(998, sr.getScaledWidth() / 2 - 100, sr.getScaledHeight() / 2 + 30, 200, 20, "Login"));
		buttonList.add(
				new GuiButton(997, sr.getScaledWidth() / 2 + 10, sr.getScaledHeight() / 2 + 60, 90, 20, "Multiplayer"));

		super.initGui();
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);

		super.onGuiClosed();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		mc.fontRendererObj.drawString("username:token:uuid",
				sr.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth("Username:token:uuid") / 2,
				sr.getScaledHeight() / 2 - 25, Color.RED.getRGB());
		String status = ChatFormatting.WHITE + "User: " + ChatFormatting.DARK_GREEN + mc.getSession().getUsername()
				+ ChatFormatting.WHITE + " UUID: " + ChatFormatting.DARK_GREEN + mc.getSession().getPlayerID();
		mc.fontRendererObj.drawString(status, sr.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth(status) / 2,
				sr.getScaledHeight() / 2 - 50, Color.WHITE.getRGB());
		sessionField.drawTextBox();

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 998
				&& !(sessionField.getText().toString().equals("") || sessionField.getText().toString() == null)) {
			try {
				String[] args = sessionField.getText().split(":");
				if (args.length == 3) {
					String name = args[0];
					String uuid = args[2];
					String token = args[1];
					Main.setSession(args[0], args[2], args[1], "mojang");
				}
				sessionField.setText("");
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (button.id == 999) {
			try {
				Main.setSession(Main.originalSession.getUsername(), Main.originalSession.getPlayerID(),
						Main.originalSession.getToken(), "mojang");
				sessionField.setText("");
				return;
			} catch (Exception e) {
			}
		}
		if (button.id == 997) {
			mc.displayGuiScreen(previousScreen);
		}
		super.actionPerformed(button);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		sessionField.textboxKeyTyped(typedChar, keyCode);
		if (Keyboard.KEY_ESCAPE == keyCode)
			mc.displayGuiScreen(previousScreen);
		else
			super.keyTyped(typedChar, keyCode);
	}
}
