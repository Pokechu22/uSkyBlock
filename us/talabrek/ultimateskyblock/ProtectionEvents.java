/*   1:    */ package us.talabrek.ultimateskyblock;
/*   2:    */ 
/*   3:    */ import org.bukkit.ChatColor;
/*   4:    */ import org.bukkit.World;
/*   5:    */ import org.bukkit.block.Block;
/*   6:    */ import org.bukkit.entity.Hanging;
/*   7:    */ import org.bukkit.entity.Player;
/*   8:    */ import org.bukkit.entity.Vehicle;
/*   9:    */ import org.bukkit.event.EventHandler;
/*  10:    */ import org.bukkit.event.EventPriority;
/*  11:    */ import org.bukkit.event.Listener;
/*  12:    */ import org.bukkit.event.block.BlockBreakEvent;
/*  13:    */ import org.bukkit.event.block.BlockPlaceEvent;
/*  14:    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*  15:    */ import org.bukkit.event.hanging.HangingBreakByEntityEvent;
/*  16:    */ import org.bukkit.event.player.PlayerBedEnterEvent;
/*  17:    */ import org.bukkit.event.player.PlayerBucketEmptyEvent;
/*  18:    */ import org.bukkit.event.player.PlayerBucketFillEvent;
/*  19:    */ import org.bukkit.event.player.PlayerInteractEvent;
/*  20:    */ import org.bukkit.event.player.PlayerShearEntityEvent;
/*  21:    */ import org.bukkit.event.vehicle.VehicleDamageEvent;
/*  22:    */ 
/*  23:    */ public class ProtectionEvents
/*  24:    */   implements Listener
/*  25:    */ {
/*  26: 21 */   private Player attacker = null;
/*  27: 22 */   private Player breaker = null;
/*  28:    */   
/*  29:    */   @EventHandler(priority=EventPriority.HIGH)
/*  30:    */   public void onPlayerBlockBreak(BlockBreakEvent event)
/*  31:    */   {
/*  32: 26 */     if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/*  33: 28 */       if ((!uSkyBlock.getInstance().locationIsOnIsland(event.getPlayer(), event.getBlock().getLocation())) && 
/*  34: 29 */         (!VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld())) && (!event.getPlayer().isOp())) {
/*  35: 31 */         event.setCancelled(true);
/*  36:    */       }
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40:    */   @EventHandler(priority=EventPriority.HIGH)
/*  41:    */   public void onPlayerBlockPlace(BlockPlaceEvent event)
/*  42:    */   {
/*  43: 38 */     if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/*  44: 40 */       if ((!uSkyBlock.getInstance().locationIsOnIsland(event.getPlayer(), event.getBlock().getLocation())) && 
/*  45: 41 */         (!VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld())) && (!event.getPlayer().isOp())) {
/*  46: 43 */         event.setCancelled(true);
/*  47:    */       }
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   @EventHandler(priority=EventPriority.NORMAL)
/*  52:    */   public void onPlayerInteract(PlayerInteractEvent event)
/*  53:    */   {
/*  54: 50 */     if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/*  55: 52 */       if ((!uSkyBlock.getInstance().playerIsOnIsland(event.getPlayer())) && (!uSkyBlock.getInstance().playerIsInSpawn(event.getPlayer())) && 
/*  56: 53 */         (!VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld())) && (!event.getPlayer().isOp())) {
/*  57: 55 */         event.setCancelled(true);
/*  58:    */       }
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   @EventHandler(priority=EventPriority.NORMAL)
/*  63:    */   public void onPlayerBedEnter(PlayerBedEnterEvent event)
/*  64:    */   {
/*  65: 62 */     if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/*  66: 64 */       if ((!uSkyBlock.getInstance().playerIsOnIsland(event.getPlayer())) && 
/*  67: 65 */         (!VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld())) && (!event.getPlayer().isOp())) {
/*  68: 67 */         event.setCancelled(true);
/*  69:    */       }
/*  70:    */     }
/*  71:    */   }
/*  72:    */   
/*  73:    */   @EventHandler(priority=EventPriority.NORMAL)
/*  74:    */   public void onPlayerShearEntity(PlayerShearEntityEvent event)
/*  75:    */   {
/*  76: 74 */     if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/*  77: 76 */       if ((!uSkyBlock.getInstance().playerIsOnIsland(event.getPlayer())) && 
/*  78: 77 */         (!VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld())) && (!event.getPlayer().isOp()))
/*  79:    */       {
/*  80: 79 */         event.getPlayer().sendMessage(ChatColor.RED + "You can only do that on your island!");
/*  81: 80 */         event.setCancelled(true);
/*  82:    */       }
/*  83:    */     }
/*  84:    */   }
/*  85:    */   
/*  86:    */   @EventHandler(priority=EventPriority.NORMAL)
/*  87:    */   public void onPlayerBucketFill(PlayerBucketFillEvent event)
/*  88:    */   {
/*  89: 87 */     if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/*  90: 89 */       if ((!uSkyBlock.getInstance().locationIsOnIsland(event.getPlayer(), event.getBlockClicked().getLocation())) && 
/*  91: 90 */         (!VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld())) && (!event.getPlayer().isOp()))
/*  92:    */       {
/*  93: 92 */         event.getPlayer().sendMessage(ChatColor.RED + "You can only do that on your island!");
/*  94: 93 */         event.setCancelled(true);
/*  95:    */       }
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   @EventHandler(priority=EventPriority.NORMAL)
/* 100:    */   public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event)
/* 101:    */   {
/* 102:100 */     if (event.getPlayer().getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/* 103:102 */       if ((!uSkyBlock.getInstance().locationIsOnIsland(event.getPlayer(), event.getBlockClicked().getLocation())) && 
/* 104:103 */         (!VaultHandler.checkPerk(event.getPlayer().getName(), "usb.mod.bypassprotection", event.getPlayer().getWorld())) && (!event.getPlayer().isOp()))
/* 105:    */       {
/* 106:105 */         event.getPlayer().sendMessage(ChatColor.RED + "You can only do that on your island!");
/* 107:106 */         event.setCancelled(true);
/* 108:    */       }
/* 109:    */     }
/* 110:    */   }
/* 111:    */   
/* 112:    */   @EventHandler(priority=EventPriority.NORMAL)
/* 113:    */   public void onPlayerBreakHanging(HangingBreakByEntityEvent event)
/* 114:    */   {
/* 115:113 */     if ((event.getRemover() instanceof Player))
/* 116:    */     {
/* 117:115 */       this.breaker = ((Player)event.getRemover());
/* 118:116 */       if (this.breaker.getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/* 119:118 */         if ((!uSkyBlock.getInstance().locationIsOnIsland(this.breaker, event.getEntity().getLocation())) && 
/* 120:119 */           (!VaultHandler.checkPerk(this.breaker.getName(), "usb.mod.bypassprotection", this.breaker.getWorld())) && (!this.breaker.isOp())) {
/* 121:121 */           event.setCancelled(true);
/* 122:    */         }
/* 123:    */       }
/* 124:    */     }
/* 125:    */   }
/* 126:    */   
/* 127:    */   @EventHandler(priority=EventPriority.NORMAL)
/* 128:    */   public void onPlayerVehicleDamage(VehicleDamageEvent event)
/* 129:    */   {
/* 130:129 */     if ((event.getAttacker() instanceof Player))
/* 131:    */     {
/* 132:131 */       this.breaker = ((Player)event.getAttacker());
/* 133:132 */       if (this.breaker.getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/* 134:134 */         if ((!uSkyBlock.getInstance().locationIsOnIsland(this.breaker, event.getVehicle().getLocation())) && 
/* 135:135 */           (!VaultHandler.checkPerk(this.breaker.getName(), "usb.mod.bypassprotection", this.breaker.getWorld())) && (!this.breaker.isOp())) {
/* 136:137 */           event.setCancelled(true);
/* 137:    */         }
/* 138:    */       }
/* 139:    */     }
/* 140:    */   }
/* 141:    */   
/* 142:    */   @EventHandler(priority=EventPriority.HIGH)
/* 143:    */   public void onPlayerAttack(EntityDamageByEntityEvent event)
/* 144:    */   {
/* 145:146 */     if ((event.getDamager() instanceof Player))
/* 146:    */     {
/* 147:147 */       this.attacker = ((Player)event.getDamager());
/* 148:148 */       if (this.attacker.getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/* 149:150 */         if (!(event.getEntity() instanceof Player)) {
/* 150:155 */           if ((!uSkyBlock.getInstance().playerIsOnIsland(this.attacker)) && 
/* 151:156 */             (!VaultHandler.checkPerk(this.attacker.getName(), "usb.mod.bypassprotection", this.attacker.getWorld())) && (!this.attacker.isOp())) {
/* 152:158 */             event.setCancelled(true);
/* 153:    */           }
/* 154:    */         }
/* 155:    */       }
/* 156:    */     }
/* 157:    */   }
/* 158:    */ }


/* Location:           C:\Pokechu22\TestServer\plugins\uSkyBlock.jar
 * Qualified Name:     us.talabrek.ultimateskyblock.ProtectionEvents
 * JD-Core Version:    0.7.0.1
 */