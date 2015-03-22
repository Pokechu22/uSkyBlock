/*  1:   */ package us.talabrek.ultimateskyblock;
/*  2:   */ 
/*  3:   */ import net.milkbowl.vault.economy.Economy;
/*  4:   */ import net.milkbowl.vault.permission.Permission;
/*  5:   */ import org.bukkit.Server;
/*  6:   */ import org.bukkit.World;
/*  7:   */ import org.bukkit.entity.Player;
/*  8:   */ import org.bukkit.plugin.PluginManager;
/*  9:   */ import org.bukkit.plugin.RegisteredServiceProvider;
/* 10:   */ import org.bukkit.plugin.ServicesManager;
/* 11:   */ 
/* 12:   */ public class VaultHandler
/* 13:   */ {
/* 14:12 */   public static Permission perms = null;
/* 15:13 */   public static Economy econ = null;
/* 16:   */   
/* 17:   */   public static void addPerk(Player player, String perk)
/* 18:   */   {
/* 19:17 */     perms.playerAdd(null, player.getName(), perk);
/* 20:   */   }
/* 21:   */   
/* 22:   */   public static void removePerk(Player player, String perk)
/* 23:   */   {
/* 24:22 */     perms.playerRemove(null, player.getName(), perk);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public static void addGroup(Player player, String perk)
/* 28:   */   {
/* 29:27 */     perms.playerAddGroup(null, player.getName(), perk);
/* 30:   */   }
/* 31:   */   
/* 32:   */   public static boolean checkPerk(String player, String perk, World world)
/* 33:   */   {
/* 34:32 */     if (perms.has(null, player, perk)) {
/* 35:33 */       return true;
/* 36:   */     }
/* 37:34 */     if (perms.has(world, player, perk)) {
/* 38:35 */       return true;
/* 39:   */     }
/* 40:36 */     return false;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public static boolean setupPermissions()
/* 44:   */   {
/* 45:40 */     RegisteredServiceProvider<Permission> rsp = uSkyBlock.getInstance().getServer().getServicesManager().getRegistration(Permission.class);
/* 46:41 */     if (rsp.getProvider() != null) {
/* 47:42 */       perms = (Permission)rsp.getProvider();
/* 48:   */     }
/* 49:43 */     return perms != null;
/* 50:   */   }
/* 51:   */   
/* 52:   */   public static boolean setupEconomy()
/* 53:   */   {
/* 54:47 */     if (uSkyBlock.getInstance().getServer().getPluginManager().getPlugin("Vault") == null) {
/* 55:48 */       return false;
/* 56:   */     }
/* 57:50 */     RegisteredServiceProvider<Economy> rsp = uSkyBlock.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
/* 58:51 */     if (rsp == null) {
/* 59:52 */       return false;
/* 60:   */     }
/* 61:54 */     econ = (Economy)rsp.getProvider();
/* 62:55 */     return econ != null;
/* 63:   */   }
/* 64:   */ }


/* Location:           C:\Pokechu22\TestServer\plugins\uSkyBlock.jar
 * Qualified Name:     us.talabrek.ultimateskyblock.VaultHandler
 * JD-Core Version:    0.7.0.1
 */