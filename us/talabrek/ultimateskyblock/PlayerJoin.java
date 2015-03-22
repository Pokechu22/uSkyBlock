/*  1:   */ 
/*  2:   */ 
/*  3:   */ java.io.PrintStream
/*  4:   */ org.bukkit.ChatColor
/*  5:   */ org.bukkit.Material
/*  6:   */ org.bukkit.World
/*  7:   */ org.bukkit.block.Block
/*  8:   */ org.bukkit.entity.Player
/*  9:   */ org.bukkit.event.EventHandler
/* 10:   */ org.bukkit.event.EventPriority
/* 11:   */ org.bukkit.event.Listener
/* 12:   */ org.bukkit.event.block.Action
/* 13:   */ org.bukkit.event.entity.FoodLevelChangeEvent
/* 14:   */ org.bukkit.event.player.PlayerInteractEvent
/* 15:   */ org.bukkit.event.player.PlayerJoinEvent
/* 16:   */ org.bukkit.event.player.PlayerQuitEvent
/* 17:   */ org.bukkit.inventory.ItemStack
/* 18:   */ org.bukkit.inventory.PlayerInventory
/* 19:   */ 
/* 20:   */ PlayerJoin
/* 21:   */   
/* 22:   */ 
/* 23:18 */   hungerman = 
/* 24:   */   
/* 25:   */   NORMAL
/* 26:   */   onPlayerJoin
/* 27:   */   
/* 28:24 */      = getInstance()readPlayerFilegetPlayer()getName()
/* 29:25 */      (==
/* 30:   */     
/* 31:27 */       outprint"Creating a new skyblock file for "getPlayer()getName()
/* 32:28 */        = getPlayer()getName()
/* 33:29 */       getInstance()writePlayerFilegetPlayer()getName(), 
/* 34:   */     
/* 35:31 */      (getHasParty()getPartyIslandLocation()==
/* 36:   */     
/* 37:33 */        = getInstance()readPlayerFilegetPartyLeader()
/* 38:34 */       setPartyIslandLocationgetIslandLocation()
/* 39:35 */       getInstance()writePlayerFilegetPlayer()getName(), 
/* 40:   */     
/* 41:46 */     buildChallengeList()
/* 42:47 */     getInstance()addActivePlayergetPlayer()getName(), 
/* 43:48 */     outprint"Loaded player file for "getPlayer()getName()
/* 44:   */   
/* 45:   */   
/* 46:   */   NORMAL
/* 47:   */   onPlayerQuit
/* 48:   */   
/* 49:56 */     getInstance()removeActivePlayergetPlayer()getName()
/* 50:   */   
/* 51:   */   
/* 52:   */   NORMAL
/* 53:   */   onPlayerFoodChange
/* 54:   */   
/* 55:62 */      (getEntity()
/* 56:   */     
/* 57:64 */       hungerman = ((Player)event.getEntity());
/* 58:65 */       if (this.hungerman.getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/* 59:67 */         if (this.hungerman.getFoodLevel() > event.getFoodLevel()) {
/* 60:69 */           if (uSkyBlock.getInstance().playerIsOnIsland(this.hungerman)) {
/* 61:71 */             if (VaultHandler.checkPerk(this.hungerman.getName(), "usb.extra.hunger", this.hungerman.getWorld())) {
/* 62:73 */               event.setCancelled(true);
/* 63:   */             }
/* 64:   */           }
/* 65:   */         }
/* 66:   */       }
/* 67:   */     }
/* 68:   */   }
/* 69:   */   
/* 70:   */   @EventHandler(priority=EventPriority.NORMAL)
/* 71:   */   public void onPlayerInteract(PlayerInteractEvent event)
/* 72:   */   {
/* 73:83 */     if ((Settings.extras_obsidianToLava) && (uSkyBlock.getInstance().playerIsOnIsland(event.getPlayer())) && (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName))) {
/* 74:85 */       if ((event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && (event.getPlayer().getItemInHand().getTypeId() == 325) && 
/* 75:86 */         (event.getClickedBlock().getType() == Material.OBSIDIAN)) {
/* 76:88 */         if (!uSkyBlock.getInstance().testForObsidian(event.getClickedBlock()))
/* 77:   */         {
/* 78:90 */           event.getPlayer().sendMessage(ChatColor.YELLOW + "Changing your obsidian back into lava. Be careful!");
/* 79:91 */           event.getClickedBlock().setType(Material.AIR);
/* 80:92 */           event.getPlayer().getInventory().removeItem(new ItemStack[] { new ItemStack(325, 1) });
/* 81:93 */           event.getPlayer().getInventory().addItem(new ItemStack[] { new ItemStack(327, 1) });
/* 82:   */         }
/* 83:   */       }
/* 84:   */     }
/* 85:   */   }
/* 86:   */ }


/* Location:           C:\Pokechu22\TestServer\plugins\uSkyBlock.jar
 * Qualified Name:     us.talabrek.ultimateskyblock.PlayerJoin
 * JD-Core Version:    0.7.0.1
 */