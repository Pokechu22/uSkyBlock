/*   1:    */ package us.talabrek.ultimateskyblock;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.PrintStream;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.List;
/*   7:    */ import org.bukkit.Bukkit;
/*   8:    */ import org.bukkit.ChatColor;
/*   9:    */ import org.bukkit.Location;
/*  10:    */ import org.bukkit.OfflinePlayer;
/*  11:    */ import org.bukkit.Server;
/*  12:    */ import org.bukkit.command.Command;
/*  13:    */ import org.bukkit.command.CommandExecutor;
/*  14:    */ import org.bukkit.command.CommandSender;
/*  15:    */ import org.bukkit.entity.Player;
/*  16:    */ import org.bukkit.scheduler.BukkitScheduler;
/*  17:    */ 
/*  18:    */ public class DevCommand
/*  19:    */   implements CommandExecutor
/*  20:    */ {
/*  21:    */   public boolean onCommand(CommandSender sender, Command command, String label, String[] split)
/*  22:    */   {
/*  23: 24 */     if (!(sender instanceof Player)) {
/*  24: 25 */       return false;
/*  25:    */     }
/*  26: 27 */     Player player = (Player)sender;
/*  27: 28 */     if (split.length == 0)
/*  28:    */     {
/*  29: 29 */       if ((VaultHandler.checkPerk(player.getName(), "usb.mod.protect", player.getWorld())) || (VaultHandler.checkPerk(player.getName(), "usb.mod.protectall", player.getWorld())) || 
/*  30: 30 */         (VaultHandler.checkPerk(player.getName(), "usb.mod.topten", player.getWorld())) || (VaultHandler.checkPerk(player.getName(), "usb.mod.orphan", player.getWorld())) || 
/*  31: 31 */         (VaultHandler.checkPerk(player.getName(), "usb.admin.delete", player.getWorld())) || (VaultHandler.checkPerk(player.getName(), "usb.admin.remove", player.getWorld())) || 
/*  32: 32 */         (VaultHandler.checkPerk(player.getName(), "usb.admin.register", player.getWorld())) || (player.isOp()))
/*  33:    */       {
/*  34: 34 */         player.sendMessage("[dev usage]");
/*  35: 35 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.protect", player.getWorld())) || (player.isOp())) {
/*  36: 36 */           player.sendMessage(ChatColor.YELLOW + "/dev protect <player>:" + ChatColor.WHITE + " add protection to an island.");
/*  37:    */         }
/*  38: 37 */         if ((VaultHandler.checkPerk(player.getName(), "usb.admin.reload", player.getWorld())) || (player.isOp())) {
/*  39: 38 */           player.sendMessage(ChatColor.YELLOW + "/dev reload:" + ChatColor.WHITE + " reload configuration from file.");
/*  40:    */         }
/*  41: 39 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.protectall", player.getWorld())) || (player.isOp())) {
/*  42: 40 */           player.sendMessage(ChatColor.YELLOW + "/dev protectall:" + ChatColor.WHITE + " add island protection to unprotected islands.");
/*  43:    */         }
/*  44: 41 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.topten", player.getWorld())) || (player.isOp())) {
/*  45: 42 */           player.sendMessage(ChatColor.YELLOW + "/dev topten:" + ChatColor.WHITE + " manually update the top 10 list");
/*  46:    */         }
/*  47: 43 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.orphan", player.getWorld())) || (player.isOp())) {
/*  48: 44 */           player.sendMessage(ChatColor.YELLOW + "/dev orphancount:" + ChatColor.WHITE + " unused island locations count");
/*  49:    */         }
/*  50: 45 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.orphan", player.getWorld())) || (player.isOp())) {
/*  51: 46 */           player.sendMessage(ChatColor.YELLOW + "/dev clearorphan:" + ChatColor.WHITE + " remove any unused island locations.");
/*  52:    */         }
/*  53: 47 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.orphan", player.getWorld())) || (player.isOp())) {
/*  54: 48 */           player.sendMessage(ChatColor.YELLOW + "/dev saveorphan:" + ChatColor.WHITE + " save the list of old (empty) island locations.");
/*  55:    */         }
/*  56: 49 */         if ((VaultHandler.checkPerk(player.getName(), "usb.admin.delete", player.getWorld())) || (player.isOp())) {
/*  57: 50 */           player.sendMessage(ChatColor.YELLOW + "/dev delete <player>:" + ChatColor.WHITE + " delete an island (removes blocks).");
/*  58:    */         }
/*  59: 51 */         if ((VaultHandler.checkPerk(player.getName(), "usb.admin.remove", player.getWorld())) || (player.isOp())) {
/*  60: 52 */           player.sendMessage(ChatColor.YELLOW + "/dev remove <player>:" + ChatColor.WHITE + " remove a player from an island.");
/*  61:    */         }
/*  62: 53 */         if ((VaultHandler.checkPerk(player.getName(), "usb.admin.register", player.getWorld())) || (player.isOp())) {
/*  63: 54 */           player.sendMessage(ChatColor.YELLOW + "/dev register <player>:" + ChatColor.WHITE + " set a player's island to your location");
/*  64:    */         }
/*  65: 55 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.challenges", player.getWorld())) || (player.isOp())) {
/*  66: 56 */           player.sendMessage(ChatColor.YELLOW + "/dev completechallenge <challengename> <player>:" + ChatColor.WHITE + " marks a challenge as complete");
/*  67:    */         }
/*  68: 57 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.challenges", player.getWorld())) || (player.isOp())) {
/*  69: 58 */           player.sendMessage(ChatColor.YELLOW + "/dev resetchallenge <challengename> <player>:" + ChatColor.WHITE + " marks a challenge as incomplete");
/*  70:    */         }
/*  71: 59 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.challenges", player.getWorld())) || (player.isOp())) {
/*  72: 60 */           player.sendMessage(ChatColor.YELLOW + "/dev resetallchallenges <challengename>:" + ChatColor.WHITE + " resets all of the player's challenges");
/*  73:    */         }
/*  74: 61 */         if ((VaultHandler.checkPerk(player.getName(), "usb.admin.purge", player.getWorld())) || (player.isOp())) {
/*  75: 62 */           player.sendMessage(ChatColor.YELLOW + "/dev purge [TimeInDays]:" + ChatColor.WHITE + " delete inactive islands older than [TimeInDays].");
/*  76:    */         }
/*  77: 63 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.party", player.getWorld())) || (player.isOp())) {
/*  78: 64 */           player.sendMessage(ChatColor.YELLOW + "/dev buildpartylist:" + ChatColor.WHITE + " build a new party list (use this if parties are broken).");
/*  79:    */         }
/*  80: 65 */         if ((VaultHandler.checkPerk(player.getName(), "usb.mod.party", player.getWorld())) || (player.isOp())) {
/*  81: 66 */           player.sendMessage(ChatColor.YELLOW + "/dev info <player>:" + ChatColor.WHITE + " check the party information for the given player.");
/*  82:    */         }
/*  83:    */       }
/*  84:    */       else
/*  85:    */       {
/*  86: 68 */         player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
/*  87:    */       }
/*  88:    */     }
/*  89: 69 */     else if (split.length == 1)
/*  90:    */     {
/*  91: 70 */       if ((split[0].equals("clearorphan")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.orphan", player.getWorld())) || (player.isOp())))
/*  92:    */       {
/*  93: 72 */         player.sendMessage(ChatColor.YELLOW + "Clearing all old (empty) island locations.");
/*  94: 73 */         uSkyBlock.getInstance().clearOrphanedIsland();
/*  95:    */       }
/*  96: 74 */       else if ((split[0].equals("protectall")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.protectall", player.getWorld())) || (player.isOp())))
/*  97:    */       {
/*  98: 76 */         player.sendMessage(ChatColor.YELLOW + "Protecting all unprotected player Islands.");
/*  99: 77 */         if (Settings.island_protectWithWorldGuard)
/* 100:    */         {
/* 101: 79 */           player.sendMessage(ChatColor.YELLOW + "Protecting all unprotected player Islands.");
/* 102: 80 */           WorldGuardHandler.protectAllIslands(sender);
/* 103:    */         }
/* 104:    */         else
/* 105:    */         {
/* 106: 82 */           player.sendMessage(ChatColor.RED + "You must enable WorldGuard protection in the config.yml to use this!");
/* 107:    */         }
/* 108:    */       }
/* 109: 83 */       else if ((split[0].equals("buildpartylist")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.protectall", player.getWorld())) || (player.isOp())))
/* 110:    */       {
/* 111: 85 */         player.sendMessage(ChatColor.YELLOW + "Building party lists..");
/* 112: 86 */         buildPartyList();
/* 113:    */       }
/* 114: 87 */       else if ((split[0].equals("orphancount")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.orphan", player.getWorld())) || (player.isOp())))
/* 115:    */       {
/* 116: 89 */         player.sendMessage(ChatColor.YELLOW + uSkyBlock.getInstance().orphanCount() + " old island locations will be used before new ones.");
/* 117:    */       }
/* 118: 90 */       else if ((split[0].equals("reload")) && ((VaultHandler.checkPerk(player.getName(), "usb.admin.reload", player.getWorld())) || (player.isOp())))
/* 119:    */       {
/* 120: 92 */         uSkyBlock.getInstance().reloadConfig();
/* 121: 93 */         uSkyBlock.getInstance().loadPluginConfig();
/* 122: 94 */         player.sendMessage(ChatColor.YELLOW + "Configuration reloaded from file.");
/* 123:    */       }
/* 124: 95 */       else if ((split[0].equals("saveorphan")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.orphan", player.getWorld())) || (player.isOp())))
/* 125:    */       {
/* 126: 97 */         player.sendMessage(ChatColor.YELLOW + "Saving the orphan list.");
/* 127: 98 */         uSkyBlock.getInstance().saveOrphans();
/* 128:    */       }
/* 129: 99 */       else if ((split[0].equals("topten")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.topten", player.getWorld())) || (player.isOp())))
/* 130:    */       {
/* 131:101 */         player.sendMessage(ChatColor.YELLOW + "Generating the Top Ten list");
/* 132:102 */         uSkyBlock.getInstance().updateTopTen(uSkyBlock.getInstance().generateTopTen());
/* 133:103 */         player.sendMessage(ChatColor.YELLOW + "Finished generation of the Top Ten list");
/* 134:    */       }
/* 135:104 */       else if ((split[0].equals("purge")) && ((VaultHandler.checkPerk(player.getName(), "usb.admin.purge", player.getWorld())) || (player.isOp())))
/* 136:    */       {
/* 137:106 */         if (uSkyBlock.getInstance().isPurgeActive())
/* 138:    */         {
/* 139:108 */           player.sendMessage(ChatColor.RED + "A purge is already running, please wait for it to finish!");
/* 140:109 */           return true;
/* 141:    */         }
/* 142:111 */         player.sendMessage(ChatColor.YELLOW + "Usage: /dev purge [TimeInDays]");
/* 143:112 */         return true;
/* 144:    */       }
/* 145:    */     }
/* 146:114 */     else if (split.length == 2)
/* 147:    */     {
/* 148:115 */       if ((split[0].equals("purge")) && ((VaultHandler.checkPerk(player.getName(), "usb.admin.purge", player.getWorld())) || (player.isOp())))
/* 149:    */       {
/* 150:117 */         if (uSkyBlock.getInstance().isPurgeActive())
/* 151:    */         {
/* 152:119 */           player.sendMessage(ChatColor.RED + "A purge is already running, please wait for it to finish!");
/* 153:120 */           return true;
/* 154:    */         }
/* 155:122 */         uSkyBlock.getInstance().activatePurge();
/* 156:123 */         final int time = Integer.parseInt(split[1]) * 24;
/* 157:124 */         player.sendMessage(ChatColor.YELLOW + "Marking all islands inactive for more than " + split[1] + " days.");
/* 158:125 */         uSkyBlock.getInstance().getServer().getScheduler().runTaskAsynchronously(uSkyBlock.getInstance(), new Runnable()
/* 159:    */         {
/* 160:    */           public void run()
/* 161:    */           {
/* 162:129 */             File directoryPlayers = new File(uSkyBlock.getInstance().getDataFolder() + File.separator + "players");
/* 163:    */             
/* 164:131 */             long offlineTime = 0L;
/* 165:133 */             for (File child : directoryPlayers.listFiles()) {
/* 166:135 */               if ((Bukkit.getOfflinePlayer(child.getName()) != null) && (Bukkit.getPlayer(child.getName()) == null))
/* 167:    */               {
/* 168:137 */                 OfflinePlayer oplayer = Bukkit.getOfflinePlayer(child.getName());
/* 169:138 */                 offlineTime = oplayer.getLastPlayed();
/* 170:139 */                 offlineTime = (System.currentTimeMillis() - offlineTime) / 3600000L;
/* 171:140 */                 if ((offlineTime > time) && (uSkyBlock.getInstance().hasIsland(oplayer.getName())))
/* 172:    */                 {
/* 173:142 */                   PlayerInfo pi = uSkyBlock.getInstance().readPlayerFile(oplayer.getName());
/* 174:143 */                   if (pi != null) {
/* 175:145 */                     if (!pi.getHasParty()) {
/* 176:147 */                       if (pi.getIslandLevel() < 10) {
/* 177:149 */                         if (child.getName() != null) {
/* 178:150 */                           uSkyBlock.getInstance().addToRemoveList(child.getName());
/* 179:    */                         }
/* 180:    */                       }
/* 181:    */                     }
/* 182:    */                   }
/* 183:    */                 }
/* 184:    */               }
/* 185:    */             }
/* 186:155 */             System.out.print("Removing " + uSkyBlock.getInstance().getRemoveList().size() + " inactive islands.");
/* 187:156 */             uSkyBlock.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(uSkyBlock.getInstance(), new Runnable()
/* 188:    */             {
/* 189:    */               public void run()
/* 190:    */               {
/* 191:159 */                 if ((uSkyBlock.getInstance().getRemoveList().size() > 0) && (uSkyBlock.getInstance().isPurgeActive()))
/* 192:    */                 {
/* 193:161 */                   uSkyBlock.getInstance().deletePlayerIsland((String)uSkyBlock.getInstance().getRemoveList().get(0));
/* 194:162 */                   System.out.print("[uSkyBlock] Purge: Removing " + (String)uSkyBlock.getInstance().getRemoveList().get(0) + "'s island");
/* 195:163 */                   uSkyBlock.getInstance().deleteFromRemoveList();
/* 196:    */                 }
/* 197:166 */                 if ((uSkyBlock.getInstance().getRemoveList().size() == 0) && (uSkyBlock.getInstance().isPurgeActive()))
/* 198:    */                 {
/* 199:168 */                   uSkyBlock.getInstance().deactivatePurge();
/* 200:169 */                   System.out.print("[uSkyBlock] Finished purging marked inactive islands.");
/* 201:    */                 }
/* 202:    */               }
/* 203:173 */             }, 0L, 20L);
/* 204:    */           }
/* 205:    */         });
/* 206:    */       }
/* 207:176 */       else if ((split[0].equals("goto")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.goto", player.getWorld())) || (player.isOp())))
/* 208:    */       {
/* 209:178 */         PlayerInfo pi = uSkyBlock.getInstance().readPlayerFile(split[1]);
/* 210:179 */         if ((pi.getHasParty()) && (!pi.getPartyLeader().equalsIgnoreCase(split[1]))) {
/* 211:180 */           pi = uSkyBlock.getInstance().readPlayerFile(pi.getPartyLeader());
/* 212:    */         }
/* 213:181 */         if (pi == null)
/* 214:    */         {
/* 215:182 */           player.sendMessage(ChatColor.RED + "Error: Invalid Player (check spelling)");
/* 216:    */         }
/* 217:    */         else
/* 218:    */         {
/* 219:185 */           if (pi.getHomeLocation() != null)
/* 220:    */           {
/* 221:187 */             player.sendMessage(ChatColor.GREEN + "Teleporting to " + split[1] + "'s island.");
/* 222:188 */             player.teleport(pi.getIslandLocation());
/* 223:189 */             return true;
/* 224:    */           }
/* 225:191 */           if (pi.getIslandLocation() != null)
/* 226:    */           {
/* 227:193 */             player.sendMessage(ChatColor.GREEN + "Teleporting to " + split[1] + "'s island.");
/* 228:194 */             player.teleport(pi.getIslandLocation());
/* 229:195 */             return true;
/* 230:    */           }
/* 231:197 */           player.sendMessage("Error: That player does not have an island!");
/* 232:    */         }
/* 233:    */       }
/* 234:199 */       else if ((split[0].equals("remove")) && ((VaultHandler.checkPerk(player.getName(), "usb.admin.remove", player.getWorld())) || (player.isOp())))
/* 235:    */       {
/* 236:201 */         PlayerInfo pi = uSkyBlock.getInstance().readPlayerFile(split[1]);
/* 237:202 */         if (pi == null)
/* 238:    */         {
/* 239:203 */           player.sendMessage(ChatColor.RED + "Error: Invalid Player (check spelling)");
/* 240:    */         }
/* 241:    */         else
/* 242:    */         {
/* 243:206 */           if (pi.getIslandLocation() != null)
/* 244:    */           {
/* 245:208 */             player.sendMessage(ChatColor.YELLOW + "Removing " + split[1] + "'s island.");
/* 246:209 */             uSkyBlock.getInstance().devDeletePlayerIsland(split[1]);
/* 247:210 */             return true;
/* 248:    */           }
/* 249:212 */           player.sendMessage("Error: That player does not have an island!");
/* 250:    */         }
/* 251:    */       }
/* 252:214 */       else if ((split[0].equals("delete")) && ((VaultHandler.checkPerk(player.getName(), "usb.admin.delete", player.getWorld())) || (player.isOp())))
/* 253:    */       {
/* 254:216 */         PlayerInfo pi = uSkyBlock.getInstance().readPlayerFile(split[1]);
/* 255:217 */         if (pi == null)
/* 256:    */         {
/* 257:218 */           player.sendMessage(ChatColor.RED + "Error: Invalid Player (check spelling)");
/* 258:    */         }
/* 259:    */         else
/* 260:    */         {
/* 261:221 */           if (pi.getIslandLocation() != null)
/* 262:    */           {
/* 263:223 */             player.sendMessage(ChatColor.YELLOW + "Removing " + split[1] + "'s island.");
/* 264:224 */             uSkyBlock.getInstance().deletePlayerIsland(split[1]);
/* 265:225 */             return true;
/* 266:    */           }
/* 267:227 */           player.sendMessage("Error: That player does not have an island!");
/* 268:    */         }
/* 269:    */       }
/* 270:229 */       else if ((split[0].equals("register")) && ((VaultHandler.checkPerk(player.getName(), "usb.admin.register", player.getWorld())) || (player.isOp())))
/* 271:    */       {
/* 272:231 */         PlayerInfo pi = uSkyBlock.getInstance().readPlayerFile(split[1]);
/* 273:232 */         if (pi == null)
/* 274:    */         {
/* 275:233 */           player.sendMessage(ChatColor.RED + "Error: Invalid Player (check spelling)");
/* 276:    */         }
/* 277:    */         else
/* 278:    */         {
/* 279:236 */           if (pi.getHasIsland()) {
/* 280:237 */             uSkyBlock.getInstance().devDeletePlayerIsland(split[1]);
/* 281:    */           }
/* 282:238 */           if (uSkyBlock.getInstance().devSetPlayerIsland(sender, player.getLocation(), split[1])) {
/* 283:240 */             player.sendMessage(ChatColor.GREEN + "Set " + split[1] + "'s island to the bedrock nearest you.");
/* 284:    */           } else {
/* 285:242 */             player.sendMessage(ChatColor.RED + "Bedrock not found: unable to set the island!");
/* 286:    */           }
/* 287:    */         }
/* 288:    */       }
/* 289:244 */       else if ((split[0].equals("info")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.party", player.getWorld())) || (player.isOp())))
/* 290:    */       {
/* 291:246 */         PlayerInfo pi = uSkyBlock.getInstance().readPlayerFile(split[1]);
/* 292:247 */         if (pi == null)
/* 293:    */         {
/* 294:248 */           player.sendMessage(ChatColor.RED + "Error: Invalid Player (check spelling)");
/* 295:    */         }
/* 296:251 */         else if (pi.getHasParty())
/* 297:    */         {
/* 298:253 */           PlayerInfo piL = uSkyBlock.getInstance().readPlayerFile(pi.getPartyLeader());
/* 299:254 */           List<String> pList = piL.getMembers();
/* 300:255 */           if (pList.contains(split[1])) {
/* 301:257 */             if (split[1].equalsIgnoreCase(pi.getPartyLeader())) {
/* 302:258 */               pList.remove(split[1]);
/* 303:    */             } else {
/* 304:261 */               pList.remove(pi.getPartyLeader());
/* 305:    */             }
/* 306:    */           }
/* 307:264 */           player.sendMessage(ChatColor.GREEN + pi.getPartyLeader() + " " + ChatColor.WHITE + pList.toString());
/* 308:265 */           player.sendMessage(ChatColor.YELLOW + "Island Location:" + ChatColor.WHITE + " (" + pi.getPartyIslandLocation().getBlockX() + "," + pi.getPartyIslandLocation().getBlockY() + "," + pi.getPartyIslandLocation().getBlockZ() + ")");
/* 309:    */         }
/* 310:    */         else
/* 311:    */         {
/* 312:268 */           player.sendMessage(ChatColor.YELLOW + "That player is not a member of an island party.");
/* 313:269 */           if (pi.getHasIsland()) {
/* 314:271 */             player.sendMessage(ChatColor.YELLOW + "Island Location:" + ChatColor.WHITE + " (" + pi.getIslandLocation().getBlockX() + "," + pi.getIslandLocation().getBlockY() + "," + pi.getIslandLocation().getBlockZ() + ")");
/* 315:    */           }
/* 316:273 */           if (pi.getPartyLeader() != null) {
/* 317:275 */             player.sendMessage(ChatColor.RED + "Party leader: " + pi.getPartyLeader() + " should be null!");
/* 318:    */           }
/* 319:277 */           if (pi.getMembers() != null) {
/* 320:279 */             player.sendMessage(ChatColor.RED + "Player has party members, but shouldn't!");
/* 321:    */           }
/* 322:    */         }
/* 323:    */       }
/* 324:283 */       else if ((split[0].equals("resetallchallenges")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.challenges", player.getWorld())) || (player.isOp())))
/* 325:    */       {
/* 326:285 */         if (!uSkyBlock.getInstance().getActivePlayers().containsKey(split[1]))
/* 327:    */         {
/* 328:287 */           PlayerInfo pi = uSkyBlock.getInstance().readPlayerFile(split[1]);
/* 329:288 */           if (pi == null)
/* 330:    */           {
/* 331:289 */             player.sendMessage(ChatColor.RED + "Error: Invalid Player (check spelling)");
/* 332:290 */             return true;
/* 333:    */           }
/* 334:292 */           pi.resetAllChallenges();
/* 335:293 */           uSkyBlock.getInstance().writePlayerFile(split[1], pi);
/* 336:294 */           player.sendMessage(ChatColor.YELLOW + split[1] + " has had all challenges reset.");
/* 337:    */         }
/* 338:    */         else
/* 339:    */         {
/* 340:297 */           ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(split[1])).resetAllChallenges();
/* 341:298 */           player.sendMessage(ChatColor.YELLOW + split[1] + " has had all challenges reset.");
/* 342:    */         }
/* 343:    */       }
/* 344:    */     }
/* 345:301 */     else if (split.length == 3) {
/* 346:303 */       if ((split[0].equals("completechallenge")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.challenges", player.getWorld())) || (player.isOp())))
/* 347:    */       {
/* 348:305 */         if (!uSkyBlock.getInstance().getActivePlayers().containsKey(split[2]))
/* 349:    */         {
/* 350:307 */           PlayerInfo pi = uSkyBlock.getInstance().readPlayerFile(split[2]);
/* 351:308 */           if (pi == null)
/* 352:    */           {
/* 353:309 */             player.sendMessage(ChatColor.RED + "Error: Invalid Player (check spelling)");
/* 354:310 */             return true;
/* 355:    */           }
/* 356:312 */           if ((pi.checkChallenge(split[1].toLowerCase())) || (!pi.challengeExists(split[1].toLowerCase())))
/* 357:    */           {
/* 358:314 */             player.sendMessage(ChatColor.RED + "Challenge doesn't exist or is already completed");
/* 359:315 */             return true;
/* 360:    */           }
/* 361:317 */           pi.completeChallenge(split[1].toLowerCase());
/* 362:318 */           uSkyBlock.getInstance().writePlayerFile(split[2], pi);
/* 363:319 */           player.sendMessage(ChatColor.YELLOW + "challange: " + split[1].toLowerCase() + " has been completed for " + split[2]);
/* 364:    */         }
/* 365:    */         else
/* 366:    */         {
/* 367:322 */           if ((((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(split[2])).checkChallenge(split[1].toLowerCase())) || (!((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(split[2])).challengeExists(split[1].toLowerCase())))
/* 368:    */           {
/* 369:324 */             player.sendMessage(ChatColor.RED + "Challenge doesn't exist or is already completed");
/* 370:325 */             return true;
/* 371:    */           }
/* 372:327 */           ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(split[2])).completeChallenge(split[1].toLowerCase());
/* 373:328 */           player.sendMessage(ChatColor.YELLOW + "challange: " + split[1].toLowerCase() + " has been completed for " + split[2]);
/* 374:    */         }
/* 375:    */       }
/* 376:330 */       else if ((split[0].equals("resetchallenge")) && ((VaultHandler.checkPerk(player.getName(), "usb.mod.challenges", player.getWorld())) || (player.isOp()))) {
/* 377:332 */         if (!uSkyBlock.getInstance().getActivePlayers().containsKey(split[2]))
/* 378:    */         {
/* 379:334 */           PlayerInfo pi = uSkyBlock.getInstance().readPlayerFile(split[2]);
/* 380:335 */           if (pi == null)
/* 381:    */           {
/* 382:336 */             player.sendMessage(ChatColor.RED + "Error: Invalid Player (check spelling)");
/* 383:337 */             return true;
/* 384:    */           }
/* 385:339 */           if ((!pi.checkChallenge(split[1].toLowerCase())) || (!pi.challengeExists(split[1].toLowerCase())))
/* 386:    */           {
/* 387:341 */             player.sendMessage(ChatColor.RED + "Challenge doesn't exist or isn't yet completed");
/* 388:342 */             return true;
/* 389:    */           }
/* 390:344 */           pi.resetChallenge(split[1].toLowerCase());
/* 391:345 */           uSkyBlock.getInstance().writePlayerFile(split[2], pi);
/* 392:346 */           player.sendMessage(ChatColor.YELLOW + "challange: " + split[1].toLowerCase() + " has been reset for " + split[2]);
/* 393:    */         }
/* 394:    */         else
/* 395:    */         {
/* 396:349 */           if ((!((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(split[2])).checkChallenge(split[1].toLowerCase())) || (!((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(split[2])).challengeExists(split[1].toLowerCase())))
/* 397:    */           {
/* 398:351 */             player.sendMessage(ChatColor.RED + "Challenge doesn't exist or isn't yet completed");
/* 399:352 */             return true;
/* 400:    */           }
/* 401:354 */           ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(split[2])).resetChallenge(split[1].toLowerCase());
/* 402:355 */           player.sendMessage(ChatColor.YELLOW + "challange: " + split[1].toLowerCase() + " has been completed for " + split[2]);
/* 403:    */         }
/* 404:    */       }
/* 405:    */     }
/* 406:359 */     return true;
/* 407:    */   }
/* 408:    */   
/* 409:    */   public void buildPartyList()
/* 410:    */   {
/* 411:365 */     File folder = uSkyBlock.getInstance().directoryPlayers;
/* 412:366 */     File[] listOfFiles = folder.listFiles();
/* 413:    */     
/* 414:    */ 
/* 415:369 */     System.out.print(ChatColor.YELLOW + "[uSkyBlock] Building a new party list...");
/* 416:370 */     for (int i = 0; i < listOfFiles.length; i++)
/* 417:    */     {
/* 418:    */       PlayerInfo pi;
/* 419:372 */       if ((pi = uSkyBlock.getInstance().readPlayerFile(listOfFiles[i].getName())) != null) {
/* 420:374 */         if (pi.getHasParty())
/* 421:    */         {
/* 422:    */           PlayerInfo piL;
/* 423:    */           PlayerInfo piL;
/* 424:376 */           if (!pi.getPartyLeader().equalsIgnoreCase(listOfFiles[i].getName())) {
/* 425:377 */             piL = uSkyBlock.getInstance().readPlayerFile(pi.getPartyLeader());
/* 426:    */           } else {
/* 427:379 */             piL = pi;
/* 428:    */           }
/* 429:381 */           piL.getHasParty();
/* 430:385 */           if (!piL.getMembers().contains(listOfFiles[i].getName())) {
/* 431:387 */             piL.addMember(listOfFiles[i].getName());
/* 432:    */           }
/* 433:390 */           uSkyBlock.getInstance().writePlayerFile(pi.getPartyLeader(), piL);
/* 434:    */         }
/* 435:    */       }
/* 436:    */     }
/* 437:394 */     System.out.print(ChatColor.YELLOW + "[uSkyBlock] Party list completed.");
/* 438:    */   }
/* 439:    */ }


/* Location:           C:\Pokechu22\TestServer\plugins\uSkyBlock.jar
 * Qualified Name:     us.talabrek.ultimateskyblock.DevCommand
 * JD-Core Version:    0.7.0.1
 */