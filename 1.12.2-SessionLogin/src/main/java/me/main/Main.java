package me.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.Session;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.awt.Color;
import java.lang.reflect.Field;

import org.apache.logging.log4j.Logger;

import com.mojang.authlib.GameProfile;

import me.gui.Gui;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main {
	public static final String MODID = "sessionloginlbozo";
	public static final String NAME = "Session Login";
	public static final String VERSION = "1.0";
	public static Minecraft mc = Minecraft.getMinecraft();
	public static Session originalSession = mc.getSession();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

	}

	@SubscribeEvent
	public void onInitGuiPost(GuiScreenEvent.InitGuiEvent.Post e) {
		if (e.getGui() instanceof GuiMultiplayer) {
			e.getButtonList().add(new GuiButton(999, 5, 5, 100, 20, "Session Login"));
		}
	}

	@SubscribeEvent
	public void onActionPerformedPre(GuiScreenEvent.ActionPerformedEvent.Pre e) {
		if (e.getGui() instanceof GuiMultiplayer) {
			if (e.getButton().id == 999) {
				Minecraft.getMinecraft().displayGuiScreen(new Gui(e.getGui()));
			}
		}
	}

	public static void setSession(String usernameIn, String playerIDIn, String tokenIn, String sessionTypeIn) {
		Field sessionField = null;
		try {
			sessionField = ObfuscationReflectionHelper.findField(Minecraft.class, "field_71449_j");
		} catch (Exception e) {
			try {
				sessionField = ObfuscationReflectionHelper.findField(Minecraft.class, "session");
			} catch (Exception ex) {}
		} finally {
			if (sessionField == null) {
				System.out.print("Error can't init the Session Field.");
				return;
			}
		}
		try {
			sessionField.setAccessible(true);
			Minecraft minecraft = Minecraft.getMinecraft();
			Session session = new Session(usernameIn, playerIDIn, tokenIn, sessionTypeIn);
			sessionField.set(Minecraft.getMinecraft(), session);
			minecraft.getProfileProperties().clear();
			minecraft.getProfileProperties();
		} catch (Exception e) {
		}
	}
}
