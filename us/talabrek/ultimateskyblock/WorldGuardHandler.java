/*   1:    */ package us.talabrek.ultimateskyblock;
/*   2:    */ 
/*   3:    */ import com.sk89q.worldedit.BlockVector;
/*   4:    */ import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
/*   5:    */ import com.sk89q.worldguard.domains.DefaultDomain;
/*   6:    */ import com.sk89q.worldguard.protection.ApplicableRegionSet;
/*   7:    */ import com.sk89q.worldguard.protection.flags.DefaultFlag;
/*   8:    */ import com.sk89q.worldguard.protection.flags.StateFlag;
/*   9:    */ import com.sk89q.worldguard.protection.flags.StringFlag;
/*  10:    */ import com.sk89q.worldguard.protection.managers.RegionManager;
/*  11:    */ import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
/*  12:    */ import com.sk89q.worldguard.protection.regions.ProtectedRegion;
/*  13:    */ import java.io.PrintStream;
/*  14:    */ import java.util.Iterator;
/*  15:    */ import java.util.List;
/*  16:    */ import org.bukkit.Bukkit;
/*  17:    */ import org.bukkit.ChatColor;
/*  18:    */ import org.bukkit.Location;
/*  19:    */ import org.bukkit.OfflinePlayer;
/*  20:    */ import org.bukkit.Server;
/*  21:    */ import org.bukkit.command.CommandSender;
/*  22:    */ import org.bukkit.entity.Player;
/*  23:    */ import org.bukkit.plugin.Plugin;
/*  24:    */ import org.bukkit.plugin.PluginManager;
/*  25:    */ 
/*  26:    */ public class WorldGuardHandler
/*  27:    */ {
/*  28:    */   public static WorldGuardPlugin getWorldGuard()
/*  29:    */   {
/*  30: 25 */     Plugin plugin = uSkyBlock.getInstance().getServer().getPluginManager().getPlugin("WorldGuard");
/*  31: 28 */     if ((plugin == null) || (!(plugin instanceof WorldGuardPlugin))) {
/*  32: 29 */       return null;
/*  33:    */     }
/*  34: 32 */     return (WorldGuardPlugin)plugin;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public static void protectIsland(CommandSender sender, String player)
/*  38:    */   {
/*  39:    */     try
/*  40:    */     {
/*  41: 39 */       if (Settings.island_protectWithWorldGuard) {
/*  42: 41 */         if ((uSkyBlock.getInstance().readPlayerFile(player).getIslandLocation() != null) && (!getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).hasRegion(player + "Island")))
/*  43:    */         {
/*  44: 43 */           ProtectedRegion region = null;
/*  45: 44 */           DefaultDomain owners = new DefaultDomain();
/*  46: 45 */           region = new ProtectedCuboidRegion(player + "Island", getProtectionVectorLeft(uSkyBlock.getInstance().readPlayerFile(player).getIslandLocation()), getProtectionVectorRight(uSkyBlock.getInstance().readPlayerFile(player).getIslandLocation()));
/*  47: 46 */           owners.addPlayer(player);
/*  48: 47 */           region.setOwners(owners);
/*  49: 48 */           region.setParent(getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion("__Global__"));
/*  50: 49 */           region.setPriority(100);
/*  51: 50 */           region.setFlag(DefaultFlag.GREET_MESSAGE, DefaultFlag.GREET_MESSAGE.parseInput(getWorldGuard(), sender, "You are entering a protected island area. (" + player + ")"));
/*  52: 51 */           region.setFlag(DefaultFlag.FAREWELL_MESSAGE, DefaultFlag.FAREWELL_MESSAGE.parseInput(getWorldGuard(), sender, "You are leaving a protected island area. (" + player + ")"));
/*  53: 52 */           region.setFlag(DefaultFlag.PVP, DefaultFlag.PVP.parseInput(getWorldGuard(), sender, Settings.island_allowPvP));
/*  54: 53 */           region.setFlag(DefaultFlag.CHEST_ACCESS, DefaultFlag.CHEST_ACCESS.parseInput(getWorldGuard(), sender, "deny"));
/*  55: 54 */           region.setFlag(DefaultFlag.USE, DefaultFlag.USE.parseInput(getWorldGuard(), sender, "deny"));
/*  56: 55 */           ApplicableRegionSet set = getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getApplicableRegions(uSkyBlock.getInstance().readPlayerFile(player).getIslandLocation());
/*  57: 56 */           if (set.size() > 0) {
/*  58: 58 */             for (ProtectedRegion regions : set) {
/*  59: 59 */               if (!regions.getId().equalsIgnoreCase("__global__")) {
/*  60: 60 */                 getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).removeRegion(regions.getId());
/*  61:    */               }
/*  62:    */             }
/*  63:    */           }
/*  64: 63 */           getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).addRegion(region);
/*  65: 64 */           System.out.print("New protected region created for " + player + "'s Island by " + sender.getName());
/*  66: 65 */           getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).save();
/*  67:    */         }
/*  68:    */         else
/*  69:    */         {
/*  70: 68 */           sender.sendMessage("Player doesn't have an island or it's already protected!");
/*  71:    */         }
/*  72:    */       }
/*  73:    */     }
/*  74:    */     catch (Exception ex)
/*  75:    */     {
/*  76: 72 */       System.out.print("ERROR: Failed to protect " + player + "'s Island (" + sender.getName() + ")");
/*  77: 73 */       ex.printStackTrace();
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static void protectAllIslands(CommandSender sender)
/*  82:    */   {
/*  83: 79 */     String player = "";
/*  84: 80 */     int checkislands = 0;
/*  85:    */     try
/*  86:    */     {
/*  87: 85 */       if (Settings.island_protectWithWorldGuard)
/*  88:    */       {
/*  89: 87 */         Player[] players = Bukkit.getServer().getOnlinePlayers();
/*  90:    */         ProtectedRegion region;
/*  91: 88 */         for (Player playerx : players)
/*  92:    */         {
/*  93: 90 */           player = playerx.getName();
/*  94: 91 */           if ((uSkyBlock.getInstance().readPlayerFile(player).getIslandLocation() != null) && (!getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).hasRegion(player + "Island")))
/*  95:    */           {
/*  96: 93 */             region = null;
/*  97: 94 */             DefaultDomain owners = new DefaultDomain();
/*  98: 95 */             region = new ProtectedCuboidRegion(player + "Island", getProtectionVectorLeft(uSkyBlock.getInstance().readPlayerFile(player).getIslandLocation()), getProtectionVectorRight(uSkyBlock.getInstance().readPlayerFile(player).getIslandLocation()));
/*  99: 96 */             owners.addPlayer(player);
/* 100: 97 */             if (uSkyBlock.getInstance().hasParty(player))
/* 101:    */             {
/* 102: 99 */               PlayerInfo pi = uSkyBlock.getInstance().readPlayerFile(player);
/* 103:100 */               List<String> members = pi.getMembers();
/* 104:101 */               if (!members.isEmpty())
/* 105:    */               {
/* 106:103 */                 Iterator<String> memlist = members.iterator();
/* 107:104 */                 while (memlist.hasNext()) {
/* 108:106 */                   owners.addPlayer((String)memlist.next());
/* 109:    */                 }
/* 110:    */               }
/* 111:    */             }
/* 112:110 */             region.setOwners(owners);
/* 113:111 */             region.setParent(getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion("__Global__"));
/* 114:112 */             region.setPriority(100);
/* 115:113 */             region.setFlag(DefaultFlag.GREET_MESSAGE, DefaultFlag.GREET_MESSAGE.parseInput(getWorldGuard(), sender, "You are entering a protected island area. (" + player + ")"));
/* 116:114 */             region.setFlag(DefaultFlag.FAREWELL_MESSAGE, DefaultFlag.FAREWELL_MESSAGE.parseInput(getWorldGuard(), sender, "You are leaving a protected island area. (" + player + ")"));
/* 117:115 */             region.setFlag(DefaultFlag.PVP, DefaultFlag.PVP.parseInput(getWorldGuard(), sender, Settings.island_allowPvP));
/* 118:116 */             region.setFlag(DefaultFlag.CHEST_ACCESS, DefaultFlag.CHEST_ACCESS.parseInput(getWorldGuard(), sender, "deny"));
/* 119:117 */             region.setFlag(DefaultFlag.USE, DefaultFlag.USE.parseInput(getWorldGuard(), sender, "deny"));
/* 120:118 */             ApplicableRegionSet set = getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getApplicableRegions(uSkyBlock.getInstance().readPlayerFile(player).getIslandLocation());
/* 121:119 */             if (set.size() > 0) {
/* 122:121 */               for (ProtectedRegion regions : set) {
/* 123:122 */                 if (!regions.getId().equalsIgnoreCase("__global__")) {
/* 124:123 */                   getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).removeRegion(regions.getId());
/* 125:    */                 }
/* 126:    */               }
/* 127:    */             }
/* 128:126 */             getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).addRegion(region);
/* 129:127 */             System.out.print("New protected region created for " + player + "'s Island by " + sender.getName());
/* 130:128 */             checkislands++;
/* 131:    */           }
/* 132:    */         }
/* 133:131 */         OfflinePlayer[] players2 = Bukkit.getServer().getOfflinePlayers();
/* 134:132 */         for (OfflinePlayer playerx : players2)
/* 135:    */         {
/* 136:134 */           player = playerx.getName();
/* 137:135 */           if (uSkyBlock.getInstance().readPlayerFile(player) == null)
/* 138:    */           {
/* 139:137 */             System.out.print(player + " does not have an island file!");
/* 140:    */           }
/* 141:140 */           else if ((uSkyBlock.getInstance().readPlayerFile(player).getIslandLocation() != null) && (!getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).hasRegion(player + "Island")))
/* 142:    */           {
/* 143:142 */             ProtectedRegion region = null;
/* 144:143 */             DefaultDomain owners = new DefaultDomain();
/* 145:144 */             region = new ProtectedCuboidRegion(player + "Island", getProtectionVectorLeft(uSkyBlock.getInstance().readPlayerFile(player).getIslandLocation()), getProtectionVectorRight(uSkyBlock.getInstance().readPlayerFile(player).getIslandLocation()));
/* 146:145 */             owners.addPlayer(player);
/* 147:146 */             if (uSkyBlock.getInstance().hasParty(player))
/* 148:    */             {
/* 149:148 */               PlayerInfo pi = uSkyBlock.getInstance().readPlayerFile(player);
/* 150:149 */               List<String> members = pi.getMembers();
/* 151:150 */               if (!members.isEmpty())
/* 152:    */               {
/* 153:152 */                 Iterator<String> memlist = members.iterator();
/* 154:153 */                 while (memlist.hasNext()) {
/* 155:155 */                   owners.addPlayer((String)memlist.next());
/* 156:    */                 }
/* 157:    */               }
/* 158:    */             }
/* 159:159 */             region.setOwners(owners);
/* 160:160 */             region.setParent(getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion("__Global__"));
/* 161:161 */             region.setFlag(DefaultFlag.GREET_MESSAGE, DefaultFlag.GREET_MESSAGE.parseInput(getWorldGuard(), sender, "You are entering a protected island area. (" + player + ")"));
/* 162:162 */             region.setFlag(DefaultFlag.FAREWELL_MESSAGE, DefaultFlag.FAREWELL_MESSAGE.parseInput(getWorldGuard(), sender, "You are leaving a protected island area. (" + player + ")"));
/* 163:163 */             region.setFlag(DefaultFlag.PVP, DefaultFlag.PVP.parseInput(getWorldGuard(), sender, Settings.island_allowPvP));
/* 164:164 */             region.setFlag(DefaultFlag.CHEST_ACCESS, DefaultFlag.CHEST_ACCESS.parseInput(getWorldGuard(), sender, "deny"));
/* 165:165 */             region.setFlag(DefaultFlag.USE, DefaultFlag.USE.parseInput(getWorldGuard(), sender, "deny"));
/* 166:166 */             ApplicableRegionSet set = getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getApplicableRegions(uSkyBlock.getInstance().readPlayerFile(player).getIslandLocation());
/* 167:167 */             if (set.size() > 0) {
/* 168:169 */               for (ProtectedRegion regions : set) {
/* 169:170 */                 if (!regions.getId().equalsIgnoreCase("__global__")) {
/* 170:171 */                   getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).removeRegion(regions.getId());
/* 171:    */                 }
/* 172:    */               }
/* 173:    */             }
/* 174:174 */             getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).addRegion(region);
/* 175:175 */             System.out.print("New protected region created for " + player + "'s Island by " + sender.getName());
/* 176:176 */             checkislands++;
/* 177:    */           }
/* 178:    */         }
/* 179:179 */         System.out.print("Protected " + checkislands + " islands.");
/* 180:180 */         getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).save();
/* 181:    */       }
/* 182:    */     }
/* 183:    */     catch (Exception ex)
/* 184:    */     {
/* 185:183 */       System.out.print("ERROR: Failed to protect " + player + "'s Island (" + sender.getName() + ")");
/* 186:184 */       ex.printStackTrace();
/* 187:    */     }
/* 188:    */   }
/* 189:    */   
/* 190:    */   public static void islandLock(CommandSender sender, String player)
/* 191:    */   {
/* 192:    */     try
/* 193:    */     {
/* 194:192 */       if (getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).hasRegion(player + "Island"))
/* 195:    */       {
/* 196:194 */         getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion(player + "Island").setFlag(DefaultFlag.ENTRY, DefaultFlag.ENTRY.parseInput(getWorldGuard(), sender, "deny"));
/* 197:195 */         sender.sendMessage(ChatColor.YELLOW + "Your island is now locked. Only your party members may enter.");
/* 198:196 */         getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).save();
/* 199:    */       }
/* 200:    */       else
/* 201:    */       {
/* 202:199 */         sender.sendMessage(ChatColor.RED + "You must be the party leader to lock your island!");
/* 203:    */       }
/* 204:    */     }
/* 205:    */     catch (Exception ex)
/* 206:    */     {
/* 207:202 */       System.out.print("ERROR: Failed to lock " + player + "'s Island (" + sender.getName() + ")");
/* 208:203 */       ex.printStackTrace();
/* 209:    */     }
/* 210:    */   }
/* 211:    */   
/* 212:    */   public static void islandUnlock(CommandSender sender, String player)
/* 213:    */   {
/* 214:    */     try
/* 215:    */     {
/* 216:211 */       if (getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).hasRegion(player + "Island"))
/* 217:    */       {
/* 218:213 */         getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion(player + "Island").setFlag(DefaultFlag.ENTRY, DefaultFlag.ENTRY.parseInput(getWorldGuard(), sender, "allow"));
/* 219:214 */         sender.sendMessage(ChatColor.YELLOW + "Your island is unlocked and anyone may enter, however only you and your party members may build or remove blocks.");
/* 220:215 */         getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).save();
/* 221:    */       }
/* 222:    */       else
/* 223:    */       {
/* 224:218 */         sender.sendMessage(ChatColor.RED + "You must be the party leader to unlock your island!");
/* 225:    */       }
/* 226:    */     }
/* 227:    */     catch (Exception ex)
/* 228:    */     {
/* 229:221 */       System.out.print("ERROR: Failed to unlock " + player + "'s Island (" + sender.getName() + ")");
/* 230:222 */       ex.printStackTrace();
/* 231:    */     }
/* 232:    */   }
/* 233:    */   
/* 234:    */   public static BlockVector getProtectionVectorLeft(Location island)
/* 235:    */   {
/* 236:228 */     return new BlockVector(island.getX() + Settings.island_protectionRange / 2, 255.0D, island.getZ() + Settings.island_protectionRange / 2);
/* 237:    */   }
/* 238:    */   
/* 239:    */   public static BlockVector getProtectionVectorRight(Location island)
/* 240:    */   {
/* 241:233 */     return new BlockVector(island.getX() - Settings.island_protectionRange / 2, 0.0D, island.getZ() - Settings.island_protectionRange / 2);
/* 242:    */   }
/* 243:    */   
/* 244:    */   public static void removePlayerFromRegion(String owner, String player)
/* 245:    */   {
/* 246:238 */     if (getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).hasRegion(owner + "Island"))
/* 247:    */     {
/* 248:240 */       DefaultDomain owners = getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion(owner + "Island").getOwners();
/* 249:241 */       owners.removePlayer(player);
/* 250:242 */       getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion(owner + "Island").setOwners(owners);
/* 251:    */     }
/* 252:    */   }
/* 253:    */   
/* 254:    */   public static void addPlayerToOldRegion(String owner, String player)
/* 255:    */   {
/* 256:248 */     if (getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).hasRegion(owner + "Island"))
/* 257:    */     {
/* 258:250 */       DefaultDomain owners = getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion(owner + "Island").getOwners();
/* 259:251 */       owners.addPlayer(player);
/* 260:252 */       getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion(owner + "Island").setOwners(owners);
/* 261:    */     }
/* 262:    */   }
/* 263:    */   
/* 264:    */   public static void resetPlayerRegion(String owner)
/* 265:    */   {
/* 266:258 */     if (getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).hasRegion(owner + "Island"))
/* 267:    */     {
/* 268:260 */       DefaultDomain owners = new DefaultDomain();
/* 269:261 */       owners.addPlayer(owner);
/* 270:    */       
/* 271:263 */       getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion(owner + "Island").setOwners(owners);
/* 272:    */     }
/* 273:    */   }
/* 274:    */   
/* 275:    */   public static void transferRegion(String owner, String player, CommandSender sender)
/* 276:    */   {
/* 277:    */     try
/* 278:    */     {
/* 279:271 */       ProtectedRegion region2 = null;
/* 280:272 */       region2 = new ProtectedCuboidRegion(player + "Island", getWorldGuard().getRegionManager(Bukkit.getWorld("skyworld")).getRegion(owner + "Island").getMinimumPoint(), getWorldGuard().getRegionManager(Bukkit.getWorld(Settings.general_worldName)).getRegion(owner + "Island").getMaximumPoint());
/* 281:273 */       region2.setOwners(getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion(owner + "Island").getOwners());
/* 282:274 */       region2.setParent(getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).getRegion("__Global__"));
/* 283:275 */       region2.setFlag(DefaultFlag.GREET_MESSAGE, DefaultFlag.GREET_MESSAGE.parseInput(getWorldGuard(), sender, "You are entering a protected island area. (" + player + ")"));
/* 284:276 */       region2.setFlag(DefaultFlag.FAREWELL_MESSAGE, DefaultFlag.FAREWELL_MESSAGE.parseInput(getWorldGuard(), sender, "You are leaving a protected island area. (" + player + ")"));
/* 285:277 */       region2.setFlag(DefaultFlag.PVP, DefaultFlag.PVP.parseInput(getWorldGuard(), sender, "deny"));
/* 286:278 */       getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).removeRegion(owner + "Island");
/* 287:279 */       getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).addRegion(region2);
/* 288:    */     }
/* 289:    */     catch (Exception e)
/* 290:    */     {
/* 291:283 */       System.out.println("Error transferring WorldGuard Protected Region from (" + owner + ") to (" + player + ")");
/* 292:    */     }
/* 293:    */   }
/* 294:    */ }


/* Location:           C:\Pokechu22\TestServer\plugins\uSkyBlock.jar
 * Qualified Name:     us.talabrek.ultimateskyblock.WorldGuardHandler
 * JD-Core Version:    0.7.0.1
 */