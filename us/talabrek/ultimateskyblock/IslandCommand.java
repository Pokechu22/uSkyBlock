/*    1:     */ package us.talabrek.ultimateskyblock;
/*    2:     */ 
/*    3:     */ import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
/*    4:     */ import com.sk89q.worldguard.protection.managers.RegionManager;
/*    5:     */ import java.io.File;
/*    6:     */ import java.io.PrintStream;
/*    7:     */ import java.util.HashMap;
/*    8:     */ import java.util.Iterator;
/*    9:     */ import java.util.List;
/*   10:     */ import java.util.Map;
/*   11:     */ import java.util.Map.Entry;
/*   12:     */ import org.bukkit.Bukkit;
/*   13:     */ import org.bukkit.ChatColor;
/*   14:     */ import org.bukkit.Location;
/*   15:     */ import org.bukkit.OfflinePlayer;
/*   16:     */ import org.bukkit.Server;
/*   17:     */ import org.bukkit.World;
/*   18:     */ import org.bukkit.block.Block;
/*   19:     */ import org.bukkit.block.Chest;
/*   20:     */ import org.bukkit.command.Command;
/*   21:     */ import org.bukkit.command.CommandExecutor;
/*   22:     */ import org.bukkit.command.CommandSender;
/*   23:     */ import org.bukkit.configuration.file.FileConfiguration;
/*   24:     */ import org.bukkit.entity.Entity;
/*   25:     */ import org.bukkit.entity.Player;
/*   26:     */ import org.bukkit.inventory.EntityEquipment;
/*   27:     */ import org.bukkit.inventory.Inventory;
/*   28:     */ import org.bukkit.inventory.ItemStack;
/*   29:     */ import org.bukkit.inventory.PlayerInventory;
/*   30:     */ import org.bukkit.plugin.PluginManager;
/*   31:     */ import org.bukkit.scheduler.BukkitScheduler;
/*   32:     */ 
/*   33:     */ public class IslandCommand
/*   34:     */   implements CommandExecutor
/*   35:     */ {
/*   36:     */   public Location Islandlocation;
/*   37:     */   private List<String> tempParty;
/*   38:     */   private String tempLeader;
/*   39:     */   private String tempTargetPlayer;
/*   40:  29 */   public boolean allowInfo = true;
/*   41:  30 */   private HashMap<String, String> inviteList = new HashMap();
/*   42:     */   String tPlayer;
/*   43:     */   
/*   44:     */   public IslandCommand()
/*   45:     */   {
/*   46:  34 */     this.inviteList.put("NoInvited", "NoInviter");
/*   47:     */   }
/*   48:     */   
/*   49:     */   public boolean onCommand(CommandSender sender, Command command, String label, String[] split)
/*   50:     */   {
/*   51:  38 */     if (!(sender instanceof Player)) {
/*   52:  39 */       return false;
/*   53:     */     }
/*   54:  41 */     Player player = (Player)sender;
/*   55:  44 */     if (!VaultHandler.checkPerk(player.getName(), "usb.island.create", player.getWorld()))
/*   56:     */     {
/*   57:  45 */       player.sendMessage(ChatColor.RED + "You don't have permission to use that command!");
/*   58:  46 */       return true;
/*   59:     */     }
/*   60:  49 */     PlayerInfo pi = (PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player.getName());
/*   61:  50 */     if (pi == null)
/*   62:     */     {
/*   63:  52 */       player.sendMessage(ChatColor.RED + "Error: Couldn't read your player data!");
/*   64:  53 */       return true;
/*   65:     */     }
/*   66:  56 */     if (uSkyBlock.getInstance().hasParty(player.getName()))
/*   67:     */     {
/*   68:  59 */       this.tempLeader = pi.getPartyLeader();
/*   69:  60 */       this.tempParty = pi.getMembers();
/*   70:     */     }
/*   71:  63 */     if ((pi.getIslandLocation() != null) || (pi.getHasParty()))
/*   72:     */     {
/*   73:  64 */       if (split.length == 0)
/*   74:     */       {
/*   75:  65 */         if ((pi.getHomeLocation() != null) || (pi.getHasParty())) {
/*   76:  66 */           uSkyBlock.getInstance().homeTeleport(player);
/*   77:     */         } else {
/*   78:  69 */           ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player.getName())).setHomeLocation(pi.getIslandLocation());
/*   79:     */         }
/*   80:  72 */         return true;
/*   81:     */       }
/*   82:  73 */       if (split.length == 1)
/*   83:     */       {
/*   84:  75 */         if ((split[0].equals("restart")) || (split[0].equals("reset")))
/*   85:     */         {
/*   86:  77 */           if (pi.getHasParty())
/*   87:     */           {
/*   88:  79 */             if (!pi.getPartyLeader().equalsIgnoreCase(player.getName())) {
/*   89:  80 */               player.sendMessage(ChatColor.RED + "Only the owner may restart this island. Leave this island in order to start your own (/island leave).");
/*   90:     */             } else {
/*   91:  82 */               player.sendMessage(ChatColor.YELLOW + "You must remove all players from your island before you can restart it (/island kick <player>). See a list of players currently part of your island using /island party.");
/*   92:     */             }
/*   93:  83 */             return true;
/*   94:     */           }
/*   95:  85 */           if ((!uSkyBlock.getInstance().onRestartCooldown(player)) || (Settings.general_cooldownRestart == 0))
/*   96:     */           {
/*   97:  88 */             uSkyBlock.getInstance().deletePlayerIsland(player.getName());
/*   98:  89 */             uSkyBlock.getInstance().setRestartCooldown(player);
/*   99:  90 */             return createIsland(sender);
/*  100:     */           }
/*  101:  93 */           player.sendMessage(ChatColor.YELLOW + "You can restart your island in " + uSkyBlock.getInstance().getRestartCooldownTime(player) / 1000L + " seconds.");
/*  102:  94 */           return true;
/*  103:     */         }
/*  104:  96 */         if (((split[0].equals("sethome")) || (split[0].equals("tpset"))) && (VaultHandler.checkPerk(player.getName(), "usb.island.sethome", player.getWorld())))
/*  105:     */         {
/*  106:  97 */           uSkyBlock.getInstance().homeSet(player);
/*  107:  98 */           return true;
/*  108:     */         }
/*  109:  99 */         if (((split[0].equals("setwarp")) || (split[0].equals("warpset"))) && (VaultHandler.checkPerk(player.getName(), "usb.extra.addwarp", player.getWorld())))
/*  110:     */         {
/*  111: 100 */           uSkyBlock.getInstance().warpSet(player);
/*  112: 101 */           return true;
/*  113:     */         }
/*  114: 102 */         if ((split[0].equals("warp")) || (split[0].equals("w")))
/*  115:     */         {
/*  116: 104 */           if (VaultHandler.checkPerk(player.getName(), "usb.extra.addwarp", player.getWorld()))
/*  117:     */           {
/*  118: 106 */             if (pi.getWarpActive()) {
/*  119: 108 */               player.sendMessage(ChatColor.GREEN + "Your incoming warp is active, players may warp to your island.");
/*  120:     */             } else {
/*  121: 111 */               player.sendMessage(ChatColor.RED + "Your incoming warp is inactive, players may not warp to your island.");
/*  122:     */             }
/*  123: 113 */             player.sendMessage(ChatColor.WHITE + "Set incoming warp to your current location using " + ChatColor.YELLOW + "/island setwarp");
/*  124: 114 */             player.sendMessage(ChatColor.WHITE + "Toggle your warp on/off using " + ChatColor.YELLOW + "/island togglewarp");
/*  125:     */           }
/*  126:     */           else
/*  127:     */           {
/*  128: 117 */             player.sendMessage(ChatColor.RED + "You do not have permission to create a warp on your island!");
/*  129:     */           }
/*  130: 119 */           if (VaultHandler.checkPerk(player.getName(), "usb.island.warp", player.getWorld())) {
/*  131: 121 */             player.sendMessage(ChatColor.WHITE + "Warp to another island using " + ChatColor.YELLOW + "/island warp <player>");
/*  132:     */           } else {
/*  133: 124 */             player.sendMessage(ChatColor.RED + "You do not have permission to warp to other islands!");
/*  134:     */           }
/*  135: 126 */           return true;
/*  136:     */         }
/*  137: 127 */         if ((split[0].equals("togglewarp")) || (split[0].equals("tw")))
/*  138:     */         {
/*  139: 129 */           if (VaultHandler.checkPerk(player.getName(), "usb.extra.addwarp", player.getWorld()))
/*  140:     */           {
/*  141: 131 */             pi.toggleActive();
/*  142: 132 */             if (pi.getWarpActive()) {
/*  143: 134 */               player.sendMessage(ChatColor.GREEN + "Your incoming warp is active, players may warp to your island.");
/*  144:     */             } else {
/*  145: 137 */               player.sendMessage(ChatColor.RED + "Your incoming warp is inactive, players may not warp to your island.");
/*  146:     */             }
/*  147:     */           }
/*  148:     */           else
/*  149:     */           {
/*  150: 141 */             player.sendMessage(ChatColor.RED + "You do not have permission to create a warp on your island!");
/*  151:     */           }
/*  152: 143 */           uSkyBlock.getInstance().getActivePlayers().put(player.getName(), pi);
/*  153: 144 */           return true;
/*  154:     */         }
/*  155: 145 */         if ((split[0].equals("ban")) || (split[0].equals("banned")) || (split[0].equals("banlist")) || (split[0].equals("b")))
/*  156:     */         {
/*  157: 147 */           if (VaultHandler.checkPerk(player.getName(), "usb.island.ban", player.getWorld()))
/*  158:     */           {
/*  159: 149 */             player.sendMessage(ChatColor.YELLOW + "The following players are banned from warping to your island:");
/*  160: 150 */             player.sendMessage(ChatColor.RED + pi.getBanned().toString());
/*  161: 151 */             player.sendMessage(ChatColor.YELLOW + "To ban/unban from your island, use /island ban <player>");
/*  162:     */           }
/*  163:     */           else
/*  164:     */           {
/*  165: 154 */             player.sendMessage(ChatColor.RED + "You do not have permission to ban players from your island!");
/*  166:     */           }
/*  167: 156 */           return true;
/*  168:     */         }
/*  169: 157 */         if (split[0].equals("lock"))
/*  170:     */         {
/*  171: 158 */           if ((Settings.island_allowIslandLock) && (VaultHandler.checkPerk(player.getName(), "usb.lock", player.getWorld()))) {
/*  172: 159 */             WorldGuardHandler.islandLock(sender, player.getName());
/*  173:     */           } else {
/*  174: 161 */             player.sendMessage(ChatColor.RED + "You don't have access to this command!");
/*  175:     */           }
/*  176: 162 */           return true;
/*  177:     */         }
/*  178: 163 */         if (split[0].equals("unlock"))
/*  179:     */         {
/*  180: 164 */           if ((Settings.island_allowIslandLock) && (VaultHandler.checkPerk(player.getName(), "usb.lock", player.getWorld()))) {
/*  181: 165 */             WorldGuardHandler.islandUnlock(sender, player.getName());
/*  182:     */           } else {
/*  183: 167 */             player.sendMessage(ChatColor.RED + "You don't have access to this command!");
/*  184:     */           }
/*  185: 168 */           return true;
/*  186:     */         }
/*  187: 169 */         if (split[0].equals("help"))
/*  188:     */         {
/*  189: 170 */           player.sendMessage(ChatColor.GREEN + "[SkyBlock command usage]");
/*  190:     */           
/*  191: 172 */           player.sendMessage(ChatColor.YELLOW + "/island :" + ChatColor.WHITE + " start your island, or teleport back to one you have.");
/*  192: 173 */           player.sendMessage(ChatColor.YELLOW + "/island restart :" + ChatColor.WHITE + " delete your island and start a new one.");
/*  193: 174 */           player.sendMessage(ChatColor.YELLOW + "/island sethome :" + ChatColor.WHITE + " set your island teleport point.");
/*  194: 175 */           if (Settings.island_useIslandLevel)
/*  195:     */           {
/*  196: 177 */             player.sendMessage(ChatColor.YELLOW + "/island level :" + ChatColor.WHITE + " check your island level");
/*  197: 178 */             player.sendMessage(ChatColor.YELLOW + "/island level <player> :" + ChatColor.WHITE + " check another player's island level.");
/*  198:     */           }
/*  199: 180 */           if (VaultHandler.checkPerk(player.getName(), "usb.party.create", player.getWorld()))
/*  200:     */           {
/*  201: 182 */             player.sendMessage(ChatColor.YELLOW + "/island party :" + ChatColor.WHITE + " view your party information.");
/*  202: 183 */             player.sendMessage(ChatColor.YELLOW + "/island invite <player>:" + ChatColor.WHITE + " invite a player to join your island.");
/*  203: 184 */             player.sendMessage(ChatColor.YELLOW + "/island leave :" + ChatColor.WHITE + " leave another player's island.");
/*  204:     */           }
/*  205: 186 */           if (VaultHandler.checkPerk(player.getName(), "usb.party.kick", player.getWorld())) {
/*  206: 188 */             player.sendMessage(ChatColor.YELLOW + "/island kick <player>:" + ChatColor.WHITE + " remove a player from your island.");
/*  207:     */           }
/*  208: 190 */           if (VaultHandler.checkPerk(player.getName(), "usb.party.join", player.getWorld())) {
/*  209: 192 */             player.sendMessage(ChatColor.YELLOW + "/island [accept/reject]:" + ChatColor.WHITE + " accept/reject an invitation.");
/*  210:     */           }
/*  211: 194 */           if (VaultHandler.checkPerk(player.getName(), "usb.party.makeleader", player.getWorld())) {
/*  212: 196 */             player.sendMessage(ChatColor.YELLOW + "/island makeleader <player>:" + ChatColor.WHITE + " transfer the island to <player>.");
/*  213:     */           }
/*  214: 198 */           if (VaultHandler.checkPerk(player.getName(), "usb.island.warp", player.getWorld())) {
/*  215: 200 */             player.sendMessage(ChatColor.YELLOW + "/island warp <player> :" + ChatColor.WHITE + " warp to another player's island.");
/*  216:     */           }
/*  217: 202 */           if (VaultHandler.checkPerk(player.getName(), "usb.extra.addwarp", player.getWorld()))
/*  218:     */           {
/*  219: 204 */             player.sendMessage(ChatColor.YELLOW + "/island setwarp :" + ChatColor.WHITE + " set your island's warp location.");
/*  220: 205 */             player.sendMessage(ChatColor.YELLOW + "/island togglewarp :" + ChatColor.WHITE + " enable/disable warping to your island.");
/*  221:     */           }
/*  222: 207 */           if (VaultHandler.checkPerk(player.getName(), "usb.island.ban", player.getWorld())) {
/*  223: 209 */             player.sendMessage(ChatColor.YELLOW + "/island ban <player> :" + ChatColor.WHITE + " ban/unban a player from your island.");
/*  224:     */           }
/*  225: 211 */           player.sendMessage(ChatColor.YELLOW + "/island top :" + ChatColor.WHITE + " see the top ranked islands.");
/*  226: 212 */           if (Settings.island_allowIslandLock) {
/*  227: 214 */             if (!VaultHandler.checkPerk(player.getName(), "usb.lock", player.getWorld()))
/*  228:     */             {
/*  229: 216 */               player.sendMessage(ChatColor.DARK_GRAY + "/island lock :" + ChatColor.GRAY + " non-group members can't enter your island.");
/*  230: 217 */               player.sendMessage(ChatColor.DARK_GRAY + "/island unlock :" + ChatColor.GRAY + " allow anyone to enter your island.");
/*  231:     */             }
/*  232:     */             else
/*  233:     */             {
/*  234: 220 */               player.sendMessage(ChatColor.YELLOW + "/island lock :" + ChatColor.WHITE + " non-group members can't enter your island.");
/*  235: 221 */               player.sendMessage(ChatColor.YELLOW + "/island unlock :" + ChatColor.WHITE + " allow anyone to enter your island.");
/*  236:     */             }
/*  237:     */           }
/*  238: 226 */           if (Bukkit.getServer().getServerId().equalsIgnoreCase("UltimateSkyblock"))
/*  239:     */           {
/*  240: 228 */             player.sendMessage(ChatColor.YELLOW + "/dungeon :" + ChatColor.WHITE + " to warp to the dungeon world.");
/*  241: 229 */             player.sendMessage(ChatColor.YELLOW + "/fun :" + ChatColor.WHITE + " to warp to the Mini-Game/Fun world.");
/*  242: 230 */             player.sendMessage(ChatColor.YELLOW + "/pvp :" + ChatColor.WHITE + " join a pvp match.");
/*  243: 231 */             player.sendMessage(ChatColor.YELLOW + "/spleef :" + ChatColor.WHITE + " join spleef match.");
/*  244: 232 */             player.sendMessage(ChatColor.YELLOW + "/hub :" + ChatColor.WHITE + " warp to the world hub Sanconia.");
/*  245:     */           }
/*  246: 234 */           return true;
/*  247:     */         }
/*  248: 235 */         if ((split[0].equals("top")) && (VaultHandler.checkPerk(player.getName(), "usb.island.topten", player.getWorld())))
/*  249:     */         {
/*  250: 236 */           uSkyBlock.getInstance().displayTopTen(player);
/*  251: 237 */           return true;
/*  252:     */         }
/*  253: 238 */         if (((split[0].equals("info")) || (split[0].equals("level"))) && (VaultHandler.checkPerk(player.getName(), "usb.island.info", player.getWorld())) && (Settings.island_useIslandLevel))
/*  254:     */         {
/*  255: 240 */           if (uSkyBlock.getInstance().playerIsOnIsland(player))
/*  256:     */           {
/*  257: 242 */             if ((!uSkyBlock.getInstance().onInfoCooldown(player)) || (Settings.general_cooldownInfo == 0))
/*  258:     */             {
/*  259: 244 */               uSkyBlock.getInstance().setInfoCooldown(player);
/*  260: 245 */               if ((!pi.getHasParty()) && (!pi.getHasIsland())) {
/*  261: 247 */                 player.sendMessage(ChatColor.RED + "You do not have an island!");
/*  262:     */               } else {
/*  263: 249 */                 getIslandLevel(player, player.getName());
/*  264:     */               }
/*  265: 250 */               return true;
/*  266:     */             }
/*  267: 253 */             player.sendMessage(ChatColor.YELLOW + "You can use that command again in " + uSkyBlock.getInstance().getInfoCooldownTime(player) / 1000L + " seconds.");
/*  268: 254 */             return true;
/*  269:     */           }
/*  270: 258 */           player.sendMessage(ChatColor.YELLOW + "You must be on your island to use this command.");
/*  271: 259 */           return true;
/*  272:     */         }
/*  273: 261 */         if ((split[0].equals("invite")) && (VaultHandler.checkPerk(player.getName(), "usb.party.create", player.getWorld())))
/*  274:     */         {
/*  275: 263 */           player.sendMessage(ChatColor.YELLOW + "Use" + ChatColor.WHITE + " /island invite <playername>" + ChatColor.YELLOW + " to invite a player to your island.");
/*  276: 264 */           if (uSkyBlock.getInstance().hasParty(player.getName()))
/*  277:     */           {
/*  278: 266 */             if (this.tempLeader.equalsIgnoreCase(player.getName()))
/*  279:     */             {
/*  280: 268 */               if (VaultHandler.checkPerk(player.getName(), "usb.extra.partysize", player.getWorld()))
/*  281:     */               {
/*  282: 270 */                 if (this.tempParty.size() < Settings.general_maxPartySize * 2) {
/*  283: 272 */                   player.sendMessage(ChatColor.GREEN + "You can invite " + (Settings.general_maxPartySize * 2 - this.tempParty.size()) + " more players.");
/*  284:     */                 } else {
/*  285: 274 */                   player.sendMessage(ChatColor.RED + "You can't invite any more players.");
/*  286:     */                 }
/*  287: 275 */                 return true;
/*  288:     */               }
/*  289: 278 */               if (this.tempParty.size() < Settings.general_maxPartySize) {
/*  290: 280 */                 player.sendMessage(ChatColor.GREEN + "You can invite " + (Settings.general_maxPartySize - this.tempParty.size()) + " more players.");
/*  291:     */               } else {
/*  292: 282 */                 player.sendMessage(ChatColor.RED + "You can't invite any more players.");
/*  293:     */               }
/*  294: 283 */               return true;
/*  295:     */             }
/*  296: 287 */             player.sendMessage(ChatColor.RED + "Only the island's owner can invite!");
/*  297: 288 */             return true;
/*  298:     */           }
/*  299: 291 */           return true;
/*  300:     */         }
/*  301: 292 */         if ((split[0].equals("accept")) && (VaultHandler.checkPerk(player.getName(), "usb.party.join", player.getWorld())))
/*  302:     */         {
/*  303: 295 */           if ((uSkyBlock.getInstance().onInfoCooldown(player)) && (Settings.general_cooldownInfo > 0))
/*  304:     */           {
/*  305: 297 */             player.sendMessage(ChatColor.YELLOW + "You can't join an island for another " + uSkyBlock.getInstance().getRestartCooldownTime(player) / 1000L + " seconds.");
/*  306: 298 */             return true;
/*  307:     */           }
/*  308: 300 */           if ((!uSkyBlock.getInstance().hasParty(player.getName())) && (this.inviteList.containsKey(player.getName())))
/*  309:     */           {
/*  310: 302 */             if (!uSkyBlock.getInstance().hasParty((String)this.inviteList.get(player.getName())))
/*  311:     */             {
/*  312: 304 */               if (pi.getHasIsland()) {
/*  313: 306 */                 uSkyBlock.getInstance().deletePlayerIsland(player.getName());
/*  314:     */               }
/*  315: 310 */               addPlayertoParty(player.getName(), (String)this.inviteList.get(player.getName()));
/*  316: 311 */               addPlayertoParty((String)this.inviteList.get(player.getName()), (String)this.inviteList.get(player.getName()));
/*  317: 312 */               player.sendMessage(ChatColor.GREEN + "You have joined an island! Use /island party to see the other members.");
/*  318: 313 */               if (Bukkit.getPlayer((String)this.inviteList.get(player.getName())) != null) {
/*  319: 315 */                 Bukkit.getPlayer((String)this.inviteList.get(player.getName())).sendMessage(ChatColor.GREEN + player.getName() + " has joined your island!");
/*  320:     */               }
/*  321:     */             }
/*  322:     */             else
/*  323:     */             {
/*  324: 320 */               if (pi.getHasIsland()) {
/*  325: 322 */                 uSkyBlock.getInstance().deletePlayerIsland(player.getName());
/*  326:     */               }
/*  327: 323 */               player.sendMessage(ChatColor.GREEN + "You have joined an island! Use /island party to see the other members.");
/*  328: 324 */               addPlayertoParty(player.getName(), (String)this.inviteList.get(player.getName()));
/*  329: 325 */               if (Bukkit.getPlayer((String)this.inviteList.get(player.getName())) != null)
/*  330:     */               {
/*  331: 327 */                 Bukkit.getPlayer((String)this.inviteList.get(player.getName())).sendMessage(ChatColor.GREEN + player.getName() + " has joined your island!");
/*  332:     */               }
/*  333:     */               else
/*  334:     */               {
/*  335: 331 */                 player.sendMessage(ChatColor.RED + "You couldn't join the island, maybe it's full.");
/*  336:     */                 
/*  337: 333 */                 return true;
/*  338:     */               }
/*  339:     */             }
/*  340: 336 */             uSkyBlock.getInstance().setRestartCooldown(player);
/*  341:     */             
/*  342: 338 */             uSkyBlock.getInstance().homeTeleport(player);
/*  343:     */             
/*  344: 340 */             player.getInventory().clear();
/*  345: 341 */             player.getEquipment().clear();
/*  346: 345 */             if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/*  347: 347 */               if (WorldGuardHandler.getWorldGuard().getRegionManager(uSkyBlock.getSkyBlockWorld()).hasRegion((String)this.inviteList.get(player.getName()) + "Island")) {
/*  348: 349 */                 WorldGuardHandler.addPlayerToOldRegion((String)this.inviteList.get(player.getName()), player.getName());
/*  349:     */               }
/*  350:     */             }
/*  351: 352 */             this.inviteList.remove(player.getName());
/*  352: 353 */             return true;
/*  353:     */           }
/*  354: 356 */           player.sendMessage(ChatColor.RED + "You can't use that command right now.");
/*  355: 357 */           return true;
/*  356:     */         }
/*  357: 360 */         if (split[0].equals("reject"))
/*  358:     */         {
/*  359: 362 */           if (this.inviteList.containsKey(player.getName()))
/*  360:     */           {
/*  361: 364 */             player.sendMessage(ChatColor.YELLOW + "You have rejected the invitation to join an island.");
/*  362: 365 */             if (Bukkit.getPlayer((String)this.inviteList.get(player.getName())) != null) {
/*  363: 367 */               Bukkit.getPlayer((String)this.inviteList.get(player.getName())).sendMessage(ChatColor.RED + player.getName() + " has rejected your island invite!");
/*  364:     */             }
/*  365: 368 */             this.inviteList.remove(player.getName());
/*  366:     */           }
/*  367:     */           else
/*  368:     */           {
/*  369: 370 */             player.sendMessage(ChatColor.RED + "You haven't been invited.");
/*  370:     */           }
/*  371: 371 */           return true;
/*  372:     */         }
/*  373: 376 */         if (split[0].equalsIgnoreCase("partypurge"))
/*  374:     */         {
/*  375: 378 */           if (VaultHandler.checkPerk(player.getName(), "usb.mod.party", player.getWorld())) {
/*  376: 380 */             player.sendMessage(ChatColor.RED + "This command no longer functions!");
/*  377:     */           } else {
/*  378: 382 */             player.sendMessage(ChatColor.RED + "You can't access that command!");
/*  379:     */           }
/*  380: 383 */           return true;
/*  381:     */         }
/*  382: 384 */         if (split[0].equalsIgnoreCase("partyclean"))
/*  383:     */         {
/*  384: 386 */           if (VaultHandler.checkPerk(player.getName(), "usb.mod.party", player.getWorld())) {
/*  385: 388 */             player.sendMessage(ChatColor.RED + "This command no longer functions!");
/*  386:     */           } else {
/*  387: 390 */             player.sendMessage(ChatColor.RED + "You can't access that command!");
/*  388:     */           }
/*  389: 391 */           return true;
/*  390:     */         }
/*  391: 392 */         if (split[0].equalsIgnoreCase("purgeinvites"))
/*  392:     */         {
/*  393: 394 */           if (VaultHandler.checkPerk(player.getName(), "usb.mod.party", player.getWorld()))
/*  394:     */           {
/*  395: 396 */             player.sendMessage(ChatColor.RED + "Deleting all invites!");
/*  396: 397 */             invitePurge();
/*  397:     */           }
/*  398:     */           else
/*  399:     */           {
/*  400: 399 */             player.sendMessage(ChatColor.RED + "You can't access that command!");
/*  401:     */           }
/*  402: 400 */           return true;
/*  403:     */         }
/*  404: 401 */         if (split[0].equalsIgnoreCase("partylist"))
/*  405:     */         {
/*  406: 403 */           if (VaultHandler.checkPerk(player.getName(), "usb.mod.party", player.getWorld())) {
/*  407: 405 */             player.sendMessage(ChatColor.RED + "This command is currently not active.");
/*  408:     */           } else {
/*  409: 408 */             player.sendMessage(ChatColor.RED + "You can't access that command!");
/*  410:     */           }
/*  411: 409 */           return true;
/*  412:     */         }
/*  413: 410 */         if (split[0].equalsIgnoreCase("invitelist"))
/*  414:     */         {
/*  415: 412 */           if (VaultHandler.checkPerk(player.getName(), "usb.mod.party", player.getWorld()))
/*  416:     */           {
/*  417: 414 */             player.sendMessage(ChatColor.RED + "Checking Invites.");
/*  418: 415 */             inviteDebug(player);
/*  419:     */           }
/*  420:     */           else
/*  421:     */           {
/*  422: 417 */             player.sendMessage(ChatColor.RED + "You can't access that command!");
/*  423:     */           }
/*  424: 418 */           return true;
/*  425:     */         }
/*  426: 422 */         if ((split[0].equals("leave")) && (VaultHandler.checkPerk(player.getName(), "usb.party.join", player.getWorld())))
/*  427:     */         {
/*  428: 424 */           if (player.getWorld().getName().equalsIgnoreCase(uSkyBlock.getSkyBlockWorld().getName()))
/*  429:     */           {
/*  430: 426 */             if (uSkyBlock.getInstance().hasParty(player.getName()))
/*  431:     */             {
/*  432: 428 */               if (((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player.getName())).getPartyLeader().equalsIgnoreCase(player.getName()))
/*  433:     */               {
/*  434: 460 */                 player.sendMessage(ChatColor.YELLOW + "You are the leader, use /island remove <player> instead.");
/*  435: 461 */                 return true;
/*  436:     */               }
/*  437: 466 */               player.getInventory().clear();
/*  438: 467 */               player.getEquipment().clear();
/*  439: 468 */               if (Settings.extras_sendToSpawn) {
/*  440: 469 */                 player.performCommand("spawn");
/*  441:     */               } else {
/*  442: 471 */                 player.teleport(uSkyBlock.getSkyBlockWorld().getSpawnLocation());
/*  443:     */               }
/*  444: 474 */               removePlayerFromParty(player.getName(), this.tempLeader);
/*  445:     */               
/*  446: 476 */               player.sendMessage(ChatColor.YELLOW + "You have left the island and returned to the player spawn.");
/*  447: 477 */               if (Bukkit.getPlayer(this.tempLeader) != null) {
/*  448: 478 */                 Bukkit.getPlayer(this.tempLeader).sendMessage(ChatColor.RED + player.getName() + " has left your island!");
/*  449:     */               }
/*  450: 479 */               this.tempParty.remove(player.getName());
/*  451: 480 */               if (this.tempParty.size() < 2) {
/*  452: 482 */                 removePlayerFromParty(this.tempLeader, this.tempLeader);
/*  453:     */               }
/*  454: 486 */               if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/*  455: 488 */                 WorldGuardHandler.removePlayerFromRegion(this.tempLeader, player.getName());
/*  456:     */               }
/*  457:     */             }
/*  458:     */             else
/*  459:     */             {
/*  460: 493 */               player.sendMessage(ChatColor.RED + "You can't leave your island if you are the only person. Try using /island restart if you want a new one!");
/*  461: 494 */               return true;
/*  462:     */             }
/*  463:     */           }
/*  464:     */           else {
/*  465: 498 */             player.sendMessage(ChatColor.RED + "You must be in the skyblock world to leave your party!");
/*  466:     */           }
/*  467: 500 */           return true;
/*  468:     */         }
/*  469: 501 */         if (split[0].equals("party"))
/*  470:     */         {
/*  471: 503 */           if (VaultHandler.checkPerk(player.getName(), "usb.party.create", player.getWorld())) {
/*  472: 504 */             player.sendMessage(ChatColor.WHITE + "/island invite <playername>" + ChatColor.YELLOW + " to invite a player to join your island.");
/*  473:     */           }
/*  474: 505 */           if (uSkyBlock.getInstance().hasParty(player.getName()))
/*  475:     */           {
/*  476: 507 */             player.sendMessage(ChatColor.WHITE + "/island leave" + ChatColor.YELLOW + " leave your current island and return to spawn");
/*  477: 508 */             if (this.tempLeader.equalsIgnoreCase(sender.getName()))
/*  478:     */             {
/*  479: 510 */               if (VaultHandler.checkPerk(player.getName(), "usb.party.kick", player.getWorld())) {
/*  480: 511 */                 player.sendMessage(ChatColor.WHITE + "/island remove <playername>" + ChatColor.YELLOW + " remove <playername> from your island");
/*  481:     */               }
/*  482: 512 */               if (VaultHandler.checkPerk(player.getName(), "usb.party.makeleader", player.getWorld())) {
/*  483: 513 */                 player.sendMessage(ChatColor.WHITE + "/island makeleader <playername>" + ChatColor.YELLOW + " give ownership of the island to <playername>");
/*  484:     */               }
/*  485: 514 */               if (VaultHandler.checkPerk(player.getName(), "usb.extra.partysize", player.getWorld()))
/*  486:     */               {
/*  487: 516 */                 if (this.tempParty.size() < Settings.general_maxPartySize * 2) {
/*  488: 518 */                   player.sendMessage(ChatColor.GREEN + "You can invite " + (Settings.general_maxPartySize * 2 - this.tempParty.size()) + " more players.");
/*  489:     */                 } else {
/*  490: 520 */                   player.sendMessage(ChatColor.RED + "You can't invite any more players.");
/*  491:     */                 }
/*  492:     */               }
/*  493: 523 */               else if (this.tempParty.size() < Settings.general_maxPartySize) {
/*  494: 525 */                 player.sendMessage(ChatColor.GREEN + "You can invite " + (Settings.general_maxPartySize - this.tempParty.size()) + " more players.");
/*  495:     */               } else {
/*  496: 527 */                 player.sendMessage(ChatColor.RED + "You can't invite any more players.");
/*  497:     */               }
/*  498:     */             }
/*  499: 531 */             player.sendMessage(ChatColor.YELLOW + "Listing your island members:");
/*  500: 532 */             PlayerInfo tPi = uSkyBlock.getInstance().readPlayerFile(this.tempLeader);
/*  501: 533 */             player.sendMessage(ChatColor.WHITE + tPi.getMembers().toString());
/*  502:     */           }
/*  503: 534 */           else if (this.inviteList.containsKey(player.getName()))
/*  504:     */           {
/*  505: 536 */             player.sendMessage(ChatColor.YELLOW + (String)this.inviteList.get(player.getName()) + " has invited you to join their island.");
/*  506: 537 */             player.sendMessage(ChatColor.WHITE + "/island [accept/reject]" + ChatColor.YELLOW + " to accept or reject the invite.");
/*  507:     */           }
/*  508: 539 */           return true;
/*  509:     */         }
/*  510:     */       }
/*  511: 541 */       else if (split.length == 2)
/*  512:     */       {
/*  513: 542 */         if (((split[0].equals("info")) || (split[0].equals("level"))) && (VaultHandler.checkPerk(player.getName(), "usb.island.info", player.getWorld())) && (Settings.island_useIslandLevel))
/*  514:     */         {
/*  515: 544 */           if ((!uSkyBlock.getInstance().onInfoCooldown(player)) || (Settings.general_cooldownInfo == 0))
/*  516:     */           {
/*  517: 546 */             uSkyBlock.getInstance().setInfoCooldown(player);
/*  518: 547 */             if ((!pi.getHasParty()) && (!pi.getHasIsland())) {
/*  519: 549 */               player.sendMessage(ChatColor.RED + "You do not have an island!");
/*  520:     */             } else {
/*  521: 551 */               getIslandLevel(player, split[1]);
/*  522:     */             }
/*  523: 552 */             return true;
/*  524:     */           }
/*  525: 555 */           player.sendMessage(ChatColor.YELLOW + "You can use that command again in " + uSkyBlock.getInstance().getInfoCooldownTime(player) / 1000L + " seconds.");
/*  526: 556 */           return true;
/*  527:     */         }
/*  528: 558 */         if ((split[0].equals("warp")) || (split[0].equals("w")))
/*  529:     */         {
/*  530: 560 */           if (VaultHandler.checkPerk(player.getName(), "usb.island.warp", player.getWorld()))
/*  531:     */           {
/*  532: 562 */             if (player.getName().equalsIgnoreCase(split[1]))
/*  533:     */             {
/*  534: 564 */               uSkyBlock.getInstance().homeTeleport(player);
/*  535: 565 */               return true;
/*  536:     */             }
/*  537: 567 */             PlayerInfo wPi = null;
/*  538: 568 */             if (!uSkyBlock.getInstance().getActivePlayers().containsKey(Bukkit.getPlayer(split[1])))
/*  539:     */             {
/*  540: 570 */               if (!uSkyBlock.getInstance().getActivePlayers().containsKey(split[1]))
/*  541:     */               {
/*  542: 572 */                 wPi = uSkyBlock.getInstance().readPlayerFile(split[1]);
/*  543: 573 */                 if (wPi == null)
/*  544:     */                 {
/*  545: 575 */                   player.sendMessage(ChatColor.RED + "That player does not exist!");
/*  546: 576 */                   return true;
/*  547:     */                 }
/*  548:     */               }
/*  549:     */               else
/*  550:     */               {
/*  551: 580 */                 wPi = (PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(split[1]);
/*  552:     */               }
/*  553:     */             }
/*  554:     */             else {
/*  555: 584 */               wPi = (PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(Bukkit.getPlayer(split[1]));
/*  556:     */             }
/*  557: 586 */             if (wPi.getWarpActive())
/*  558:     */             {
/*  559: 588 */               if (!wPi.isBanned(player.getName())) {
/*  560: 590 */                 uSkyBlock.getInstance().warpTeleport(player, wPi);
/*  561:     */               } else {
/*  562: 593 */                 player.sendMessage(ChatColor.RED + "That player has forbidden you from warping to their island.");
/*  563:     */               }
/*  564:     */             }
/*  565:     */             else
/*  566:     */             {
/*  567: 597 */               player.sendMessage(ChatColor.RED + "That player does not have an active warp.");
/*  568: 598 */               return true;
/*  569:     */             }
/*  570:     */           }
/*  571:     */           else
/*  572:     */           {
/*  573: 602 */             player.sendMessage(ChatColor.RED + "You do not have permission to warp to other islands!");
/*  574:     */           }
/*  575: 604 */           return true;
/*  576:     */         }
/*  577: 605 */         if ((split[0].equals("ban")) || (split[0].equals("b")))
/*  578:     */         {
/*  579: 607 */           if (VaultHandler.checkPerk(player.getName(), "usb.island.ban", player.getWorld()))
/*  580:     */           {
/*  581: 609 */             if (!pi.isBanned(split[1]))
/*  582:     */             {
/*  583: 611 */               pi.addBan(split[1]);
/*  584: 612 */               player.sendMessage(ChatColor.YELLOW + "You have banned " + ChatColor.RED + split[1] + ChatColor.YELLOW + " from warping to your island.");
/*  585:     */             }
/*  586:     */             else
/*  587:     */             {
/*  588: 615 */               pi.removeBan(split[1]);
/*  589: 616 */               player.sendMessage(ChatColor.YELLOW + "You have unbanned " + ChatColor.GREEN + split[1] + ChatColor.YELLOW + " from warping to your island.");
/*  590:     */             }
/*  591:     */           }
/*  592:     */           else {
/*  593: 620 */             player.sendMessage(ChatColor.RED + "You do not have permission to ban players from your island!");
/*  594:     */           }
/*  595: 622 */           uSkyBlock.getInstance().getActivePlayers().put(player.getName(), pi);
/*  596: 623 */           return true;
/*  597:     */         }
/*  598: 624 */         if ((split[0].equalsIgnoreCase("invite")) && (VaultHandler.checkPerk(player.getName(), "usb.party.create", player.getWorld())))
/*  599:     */         {
/*  600: 631 */           if (Bukkit.getPlayer(split[1]) == null)
/*  601:     */           {
/*  602: 633 */             player.sendMessage(ChatColor.RED + "That player is offline or doesn't exist.");
/*  603: 634 */             return true;
/*  604:     */           }
/*  605: 636 */           if (!Bukkit.getPlayer(split[1]).isOnline())
/*  606:     */           {
/*  607: 638 */             player.sendMessage(ChatColor.RED + "That player is offline or doesn't exist.");
/*  608: 639 */             return true;
/*  609:     */           }
/*  610: 641 */           if (!uSkyBlock.getInstance().hasIsland(player.getName()))
/*  611:     */           {
/*  612: 643 */             player.sendMessage(ChatColor.RED + "You must have an island in order to invite people to it!");
/*  613: 644 */             return true;
/*  614:     */           }
/*  615: 646 */           if (player.getName().equalsIgnoreCase(Bukkit.getPlayer(split[1]).getName()))
/*  616:     */           {
/*  617: 648 */             player.sendMessage(ChatColor.RED + "You can't invite yourself!");
/*  618: 649 */             return true;
/*  619:     */           }
/*  620: 651 */           if (uSkyBlock.getInstance().hasParty(player.getName()))
/*  621:     */           {
/*  622: 653 */             if (this.tempLeader.equalsIgnoreCase(player.getName()))
/*  623:     */             {
/*  624: 655 */               if (!uSkyBlock.getInstance().hasParty(Bukkit.getPlayer(split[1]).getName()))
/*  625:     */               {
/*  626: 657 */                 if (VaultHandler.checkPerk(player.getName(), "usb.extra.partysize", player.getWorld()))
/*  627:     */                 {
/*  628: 659 */                   if (this.tempParty.size() < Settings.general_maxPartySize * 2)
/*  629:     */                   {
/*  630: 661 */                     if (this.inviteList.containsValue(player.getName()))
/*  631:     */                     {
/*  632: 663 */                       this.inviteList.remove(getKeyByValue(this.inviteList, player.getName()));
/*  633: 664 */                       player.sendMessage(ChatColor.YELLOW + "Removing your previous invite.");
/*  634:     */                     }
/*  635: 666 */                     this.inviteList.put(Bukkit.getPlayer(split[1]).getName(), player.getName());
/*  636: 667 */                     player.sendMessage(ChatColor.GREEN + "Invite sent to " + Bukkit.getPlayer(split[1]).getName());
/*  637:     */                     
/*  638: 669 */                     Bukkit.getPlayer(split[1]).sendMessage(player.getName() + " has invited you to join their island!");
/*  639: 670 */                     Bukkit.getPlayer(split[1]).sendMessage(ChatColor.WHITE + "/island [accept/reject]" + ChatColor.YELLOW + " to accept or reject the invite.");
/*  640: 671 */                     Bukkit.getPlayer(split[1]).sendMessage(ChatColor.RED + "WARNING: You will lose your current island if you accept!");
/*  641:     */                   }
/*  642:     */                   else
/*  643:     */                   {
/*  644: 673 */                     player.sendMessage(ChatColor.RED + "Your island is full, you can't invite anyone else.");
/*  645:     */                   }
/*  646:     */                 }
/*  647: 676 */                 else if (this.tempParty.size() < Settings.general_maxPartySize)
/*  648:     */                 {
/*  649: 678 */                   if (this.inviteList.containsValue(player.getName()))
/*  650:     */                   {
/*  651: 680 */                     this.inviteList.remove(getKeyByValue(this.inviteList, player.getName()));
/*  652: 681 */                     player.sendMessage(ChatColor.YELLOW + "Removing your previous invite.");
/*  653:     */                   }
/*  654: 683 */                   this.inviteList.put(Bukkit.getPlayer(split[1]).getName(), player.getName());
/*  655: 684 */                   player.sendMessage(ChatColor.GREEN + "Invite sent to " + Bukkit.getPlayer(split[1]).getName());
/*  656:     */                   
/*  657: 686 */                   Bukkit.getPlayer(split[1]).sendMessage(player.getName() + " has invited you to join their island!");
/*  658: 687 */                   Bukkit.getPlayer(split[1]).sendMessage(ChatColor.WHITE + "/island [accept/reject]" + ChatColor.YELLOW + " to accept or reject the invite.");
/*  659: 688 */                   Bukkit.getPlayer(split[1]).sendMessage(ChatColor.RED + "WARNING: You will lose your current island if you accept!");
/*  660:     */                 }
/*  661:     */                 else
/*  662:     */                 {
/*  663: 690 */                   player.sendMessage(ChatColor.RED + "Your island is full, you can't invite anyone else.");
/*  664:     */                 }
/*  665:     */               }
/*  666:     */               else {
/*  667: 693 */                 player.sendMessage(ChatColor.RED + "That player is already with a group on an island.");
/*  668:     */               }
/*  669:     */             }
/*  670:     */             else {
/*  671: 695 */               player.sendMessage(ChatColor.RED + "Only the island's owner may invite new players.");
/*  672:     */             }
/*  673:     */           }
/*  674:     */           else
/*  675:     */           {
/*  676: 696 */             if (!uSkyBlock.getInstance().hasParty(player.getName()))
/*  677:     */             {
/*  678: 698 */               if (!uSkyBlock.getInstance().hasParty(Bukkit.getPlayer(split[1]).getName()))
/*  679:     */               {
/*  680: 700 */                 if (this.inviteList.containsValue(player.getName()))
/*  681:     */                 {
/*  682: 702 */                   this.inviteList.remove(getKeyByValue(this.inviteList, player.getName()));
/*  683: 703 */                   player.sendMessage(ChatColor.YELLOW + "Removing your previous invite.");
/*  684:     */                 }
/*  685: 705 */                 this.inviteList.put(Bukkit.getPlayer(split[1]).getName(), player.getName());
/*  686:     */                 
/*  687:     */ 
/*  688: 708 */                 player.sendMessage(ChatColor.GREEN + "Invite sent to " + Bukkit.getPlayer(split[1]).getName());
/*  689: 709 */                 Bukkit.getPlayer(split[1]).sendMessage(player.getName() + " has invited you to join their island!");
/*  690: 710 */                 Bukkit.getPlayer(split[1]).sendMessage(ChatColor.WHITE + "/island [accept/reject]" + ChatColor.YELLOW + " to accept or reject the invite.");
/*  691: 711 */                 Bukkit.getPlayer(split[1]).sendMessage(ChatColor.RED + "WARNING: You will lose your current island if you accept!");
/*  692:     */               }
/*  693:     */               else
/*  694:     */               {
/*  695: 713 */                 player.sendMessage(ChatColor.RED + "That player is already with a group on an island.");
/*  696:     */               }
/*  697: 714 */               return true;
/*  698:     */             }
/*  699: 717 */             player.sendMessage(ChatColor.RED + "Only the island's owner may invite new players!");
/*  700: 718 */             return true;
/*  701:     */           }
/*  702: 720 */           return true;
/*  703:     */         }
/*  704: 721 */         if (((split[0].equalsIgnoreCase("remove")) || (split[0].equalsIgnoreCase("kick"))) && (VaultHandler.checkPerk(player.getName(), "usb.party.kick", player.getWorld())))
/*  705:     */         {
/*  706: 724 */           if ((Bukkit.getPlayer(split[1]) == null) && (Bukkit.getOfflinePlayer(split[1]) == null))
/*  707:     */           {
/*  708: 726 */             player.sendMessage(ChatColor.RED + "That player doesn't exist.");
/*  709: 727 */             return true;
/*  710:     */           }
/*  711: 729 */           if (Bukkit.getPlayer(split[1]) == null) {
/*  712: 731 */             this.tempTargetPlayer = Bukkit.getOfflinePlayer(split[1]).getName();
/*  713:     */           } else {
/*  714: 734 */             this.tempTargetPlayer = Bukkit.getPlayer(split[1]).getName();
/*  715:     */           }
/*  716: 736 */           if (this.tempParty.contains(split[1])) {
/*  717: 738 */             this.tempTargetPlayer = split[1];
/*  718:     */           }
/*  719: 740 */           if (uSkyBlock.getInstance().hasParty(player.getName()))
/*  720:     */           {
/*  721: 742 */             if (this.tempLeader.equalsIgnoreCase(player.getName()))
/*  722:     */             {
/*  723: 744 */               if (this.tempParty.contains(this.tempTargetPlayer))
/*  724:     */               {
/*  725: 746 */                 if (player.getName().equalsIgnoreCase(this.tempTargetPlayer))
/*  726:     */                 {
/*  727: 748 */                   player.sendMessage(ChatColor.RED + "Use /island leave to remove all people from your island");
/*  728: 749 */                   return true;
/*  729:     */                 }
/*  730: 751 */                 if (Bukkit.getPlayer(split[1]) != null)
/*  731:     */                 {
/*  732: 753 */                   if (Bukkit.getPlayer(split[1]).getWorld().getName().equalsIgnoreCase(uSkyBlock.getSkyBlockWorld().getName()))
/*  733:     */                   {
/*  734: 755 */                     Bukkit.getPlayer(split[1]).getInventory().clear();
/*  735: 756 */                     Bukkit.getPlayer(split[1]).getEquipment().clear();
/*  736: 757 */                     Bukkit.getPlayer(split[1]).sendMessage(ChatColor.RED + player.getName() + " has removed you from their island!");
/*  737:     */                   }
/*  738: 759 */                   if (Settings.extras_sendToSpawn) {
/*  739: 760 */                     Bukkit.getPlayer(split[1]).performCommand("spawn");
/*  740:     */                   } else {
/*  741: 762 */                     Bukkit.getPlayer(split[1]).teleport(uSkyBlock.getSkyBlockWorld().getSpawnLocation());
/*  742:     */                   }
/*  743:     */                 }
/*  744: 767 */                 if (Bukkit.getPlayer(this.tempLeader) != null) {
/*  745: 768 */                   Bukkit.getPlayer(this.tempLeader).sendMessage(ChatColor.RED + this.tempTargetPlayer + " has been removed from the island.");
/*  746:     */                 }
/*  747: 769 */                 removePlayerFromParty(this.tempTargetPlayer, this.tempLeader);
/*  748: 770 */                 this.tempParty.remove(this.tempTargetPlayer);
/*  749: 771 */                 if (this.tempParty.size() < 2) {
/*  750: 773 */                   removePlayerFromParty(player.getName(), this.tempLeader);
/*  751:     */                 }
/*  752: 779 */                 if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/*  753: 781 */                   WorldGuardHandler.removePlayerFromRegion(player.getName(), this.tempTargetPlayer);
/*  754:     */                 }
/*  755:     */               }
/*  756:     */               else
/*  757:     */               {
/*  758: 785 */                 System.out.print("Player " + player.getName() + " failed to remove " + this.tempTargetPlayer);
/*  759: 786 */                 player.sendMessage(ChatColor.RED + "That player is not part of your island group!");
/*  760:     */               }
/*  761:     */             }
/*  762:     */             else {
/*  763: 789 */               player.sendMessage(ChatColor.RED + "Only the island's owner may remove people from the island!");
/*  764:     */             }
/*  765:     */           }
/*  766:     */           else {
/*  767: 791 */             player.sendMessage(ChatColor.RED + "No one else is on your island, are you seeing things?");
/*  768:     */           }
/*  769: 792 */           return true;
/*  770:     */         }
/*  771: 793 */         if ((split[0].equalsIgnoreCase("makeleader")) && (VaultHandler.checkPerk(player.getName(), "usb.party.makeleader", player.getWorld())))
/*  772:     */         {
/*  773: 795 */           if (Bukkit.getPlayer(split[1]) == null)
/*  774:     */           {
/*  775: 797 */             player.sendMessage(ChatColor.RED + "That player must be online to transfer the island.");
/*  776: 798 */             return true;
/*  777:     */           }
/*  778: 801 */           if ((!uSkyBlock.getInstance().getActivePlayers().containsKey(player.getName())) || (!uSkyBlock.getInstance().getActivePlayers().containsKey(Bukkit.getPlayer(split[1]).getName())))
/*  779:     */           {
/*  780: 803 */             player.sendMessage(ChatColor.RED + "Both players must be online to transfer an island.");
/*  781: 804 */             return true;
/*  782:     */           }
/*  783: 807 */           if (!((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player.getName())).getHasParty())
/*  784:     */           {
/*  785: 809 */             player.sendMessage(ChatColor.RED + "You must be in a party to transfer your island.");
/*  786: 810 */             return true;
/*  787:     */           }
/*  788: 813 */           if (((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player.getName())).getMembers().size() > 2)
/*  789:     */           {
/*  790: 815 */             player.sendMessage(ChatColor.RED + "Remove all players from your party other than the player you are transferring to.");
/*  791: 816 */             System.out.print(player.getName() + " tried to transfer his island, but his party has too many people!");
/*  792: 817 */             return true;
/*  793:     */           }
/*  794: 820 */           this.tempTargetPlayer = Bukkit.getPlayer(split[1]).getName();
/*  795: 822 */           if (this.tempParty.contains(split[1])) {
/*  796: 824 */             this.tempTargetPlayer = split[1];
/*  797:     */           }
/*  798: 826 */           if (uSkyBlock.getInstance().hasParty(player.getName()))
/*  799:     */           {
/*  800: 828 */             if (this.tempLeader.equalsIgnoreCase(player.getName()))
/*  801:     */             {
/*  802: 830 */               if (this.tempParty.contains(this.tempTargetPlayer))
/*  803:     */               {
/*  804: 832 */                 if (Bukkit.getPlayer(split[1]) != null) {
/*  805: 833 */                   Bukkit.getPlayer(split[1]).sendMessage(ChatColor.GREEN + "You are now the owner of your island.");
/*  806:     */                 }
/*  807: 834 */                 player.sendMessage(ChatColor.GREEN + Bukkit.getPlayer(split[1]).getName() + " is now the owner of your island!");
/*  808: 835 */                 removePlayerFromParty(this.tempTargetPlayer, this.tempLeader);
/*  809: 836 */                 removePlayerFromParty(player.getName(), this.tempLeader);
/*  810: 837 */                 addPlayertoParty(player.getName(), this.tempTargetPlayer);
/*  811: 838 */                 addPlayertoParty(this.tempTargetPlayer, this.tempTargetPlayer);
/*  812:     */                 
/*  813:     */ 
/*  814:     */ 
/*  815:     */ 
/*  816:     */ 
/*  817:     */ 
/*  818:     */ 
/*  819:     */ 
/*  820:     */ 
/*  821:     */ 
/*  822:     */ 
/*  823:     */ 
/*  824: 851 */                 uSkyBlock.getInstance().transferIsland(player.getName(), this.tempTargetPlayer);
/*  825: 856 */                 if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/*  826: 858 */                   WorldGuardHandler.transferRegion(player.getName(), this.tempTargetPlayer, sender);
/*  827:     */                 }
/*  828: 860 */                 return true;
/*  829:     */               }
/*  830: 862 */               player.sendMessage(ChatColor.RED + "That player is not part of your island group!");
/*  831:     */             }
/*  832:     */             else
/*  833:     */             {
/*  834: 864 */               player.sendMessage(ChatColor.RED + "This isn't your island, so you can't give it away!");
/*  835:     */             }
/*  836:     */           }
/*  837:     */           else {
/*  838: 866 */             player.sendMessage(ChatColor.RED + "Could not change leaders.");
/*  839:     */           }
/*  840: 867 */           return true;
/*  841:     */         }
/*  842: 868 */         if (split[0].equalsIgnoreCase("checkparty"))
/*  843:     */         {
/*  844: 869 */           if (VaultHandler.checkPerk(player.getName(), "usb.mod.party", player.getWorld()))
/*  845:     */           {
/*  846: 871 */             player.sendMessage(ChatColor.YELLOW + "Checking Party of " + split[1]);
/*  847: 872 */             PlayerInfo pip = uSkyBlock.getInstance().readPlayerFile(split[1]);
/*  848: 873 */             if (pip == null) {
/*  849: 875 */               player.sendMessage(ChatColor.YELLOW + "That player doesn't exist!");
/*  850: 878 */             } else if (pip.getHasParty())
/*  851:     */             {
/*  852: 880 */               if (pip.getPartyLeader().equalsIgnoreCase(split[1]))
/*  853:     */               {
/*  854: 881 */                 player.sendMessage(pip.getMembers().toString());
/*  855:     */               }
/*  856:     */               else
/*  857:     */               {
/*  858: 884 */                 PlayerInfo pip2 = uSkyBlock.getInstance().readPlayerFile(pip.getPartyLeader());
/*  859: 885 */                 player.sendMessage(pip2.getMembers().toString());
/*  860:     */               }
/*  861:     */             }
/*  862:     */             else {
/*  863: 889 */               player.sendMessage(ChatColor.RED + "That player is not in an island party!");
/*  864:     */             }
/*  865:     */           }
/*  866:     */           else
/*  867:     */           {
/*  868: 893 */             player.sendMessage(ChatColor.RED + "You can't access that command!");
/*  869:     */           }
/*  870: 894 */           return true;
/*  871:     */         }
/*  872:     */       }
/*  873:     */     }
/*  874:     */     else
/*  875:     */     {
/*  876: 898 */       player.sendMessage(ChatColor.GREEN + "Creating a new island for you.");
/*  877: 899 */       return createIsland(sender);
/*  878:     */     }
/*  879: 901 */     return false;
/*  880:     */   }
/*  881:     */   
/*  882:     */   private boolean createIsland(CommandSender sender)
/*  883:     */   {
/*  884: 918 */     Player player = (Player)sender;
/*  885: 919 */     Location last = uSkyBlock.getInstance().getLastIsland();
/*  886: 920 */     last.setY(Settings.island_height);
/*  887:     */     try
/*  888:     */     {
/*  889:     */       do
/*  890:     */       {
/*  891: 930 */         uSkyBlock.getInstance().removeNextOrphan();
/*  892: 928 */         if (!uSkyBlock.getInstance().hasOrphanedIsland()) {
/*  893:     */           break;
/*  894:     */         }
/*  895: 928 */       } while (uSkyBlock.getInstance().islandAtLocation(uSkyBlock.getInstance().checkOrphan()));
/*  896: 948 */       while ((uSkyBlock.getInstance().hasOrphanedIsland()) && (!uSkyBlock.getInstance().checkOrphan().getWorld().getName().equalsIgnoreCase(Settings.general_worldName))) {
/*  897: 950 */         uSkyBlock.getInstance().removeNextOrphan();
/*  898:     */       }
/*  899:     */       Location next;
/*  900: 954 */       if ((uSkyBlock.getInstance().hasOrphanedIsland()) && (!uSkyBlock.getInstance().islandAtLocation(uSkyBlock.getInstance().checkOrphan())))
/*  901:     */       {
/*  902: 955 */         Location next = uSkyBlock.getInstance().getOrphanedIsland();
/*  903: 956 */         uSkyBlock.getInstance().saveOrphans();
/*  904: 957 */         uSkyBlock.getInstance().updateOrphans();
/*  905:     */       }
/*  906:     */       else
/*  907:     */       {
/*  908: 961 */         next = nextIslandLocation(last);
/*  909: 962 */         uSkyBlock.getInstance().setLastIsland(next);
/*  910: 964 */         while (uSkyBlock.getInstance().islandAtLocation(next)) {
/*  911: 967 */           next = nextIslandLocation(next);
/*  912:     */         }
/*  913: 970 */         while (uSkyBlock.getInstance().islandInSpawn(next)) {
/*  914: 973 */           next = nextIslandLocation(next);
/*  915:     */         }
/*  916: 976 */         uSkyBlock.getInstance().setLastIsland(next);
/*  917:     */       }
/*  918: 978 */       boolean hasIslandNow = false;
/*  919: 980 */       if ((uSkyBlock.getInstance().getSchemFile().length > 0) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldEdit")))
/*  920:     */       {
/*  921: 982 */         String cSchem = "";
/*  922: 983 */         for (int i = 0; i < uSkyBlock.getInstance().getSchemFile().length; i++) {
/*  923: 985 */           if (!hasIslandNow)
/*  924:     */           {
/*  925: 987 */             if (uSkyBlock.getInstance().getSchemFile()[i].getName().lastIndexOf('.') > 0) {
/*  926: 989 */               cSchem = uSkyBlock.getInstance().getSchemFile()[i].getName().substring(0, uSkyBlock.getInstance().getSchemFile()[i].getName().lastIndexOf('.'));
/*  927:     */             } else {
/*  928: 991 */               cSchem = uSkyBlock.getInstance().getSchemFile()[i].getName();
/*  929:     */             }
/*  930: 993 */             if (VaultHandler.checkPerk(player.getName(), "usb.schematic." + cSchem, uSkyBlock.getSkyBlockWorld())) {
/*  931: 995 */               if (WorldEditHandler.loadIslandSchematic(uSkyBlock.getSkyBlockWorld(), uSkyBlock.getInstance().getSchemFile()[i], next))
/*  932:     */               {
/*  933: 997 */                 setChest(next, player);
/*  934: 998 */                 hasIslandNow = true;
/*  935:     */               }
/*  936:     */             }
/*  937:     */           }
/*  938:     */         }
/*  939:1003 */         if (!hasIslandNow) {
/*  940:1005 */           for (int i = 0; i < uSkyBlock.getInstance().getSchemFile().length; i++)
/*  941:     */           {
/*  942:1007 */             if (uSkyBlock.getInstance().getSchemFile()[i].getName().lastIndexOf('.') > 0) {
/*  943:1009 */               cSchem = uSkyBlock.getInstance().getSchemFile()[i].getName().substring(0, uSkyBlock.getInstance().getSchemFile()[i].getName().lastIndexOf('.'));
/*  944:     */             } else {
/*  945:1011 */               cSchem = uSkyBlock.getInstance().getSchemFile()[i].getName();
/*  946:     */             }
/*  947:1012 */             if (cSchem.equalsIgnoreCase(Settings.island_schematicName)) {
/*  948:1014 */               if (WorldEditHandler.loadIslandSchematic(uSkyBlock.getSkyBlockWorld(), uSkyBlock.getInstance().getSchemFile()[i], next))
/*  949:     */               {
/*  950:1016 */                 setChest(next, player);
/*  951:1017 */                 hasIslandNow = true;
/*  952:     */               }
/*  953:     */             }
/*  954:     */           }
/*  955:     */         }
/*  956:     */       }
/*  957:1023 */       if (!hasIslandNow) {
/*  958:1025 */         if (!Settings.island_useOldIslands) {
/*  959:1026 */           generateIslandBlocks(next.getBlockX(), next.getBlockZ(), player, uSkyBlock.getSkyBlockWorld());
/*  960:     */         } else {
/*  961:1028 */           oldGenerateIslandBlocks(next.getBlockX(), next.getBlockZ(), player, uSkyBlock.getSkyBlockWorld());
/*  962:     */         }
/*  963:     */       }
/*  964:1031 */       setNewPlayerIsland(player, next);
/*  965:     */       
/*  966:1033 */       player.getInventory().clear();
/*  967:1034 */       player.getEquipment().clear();
/*  968:1035 */       Iterator<Entity> ents = player.getNearbyEntities(50.0D, 250.0D, 50.0D).iterator();
/*  969:1036 */       while (ents.hasNext())
/*  970:     */       {
/*  971:1038 */         Entity tempent = (Entity)ents.next();
/*  972:1039 */         if (!(tempent instanceof Player)) {
/*  973:1044 */           tempent.remove();
/*  974:     */         }
/*  975:     */       }
/*  976:1046 */       if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/*  977:1047 */         WorldGuardHandler.protectIsland(sender, sender.getName());
/*  978:     */       }
/*  979:     */     }
/*  980:     */     catch (Exception ex)
/*  981:     */     {
/*  982:1050 */       player.sendMessage("Could not create your Island. Pleace contact a server moderator.");
/*  983:1051 */       ex.printStackTrace();
/*  984:1052 */       return false;
/*  985:     */     }
/*  986:1054 */     return true;
/*  987:     */   }
/*  988:     */   
/*  989:     */   public void generateIslandBlocks(int x, int z, Player player, World world)
/*  990:     */   {
/*  991:1059 */     int y = Settings.island_height;
/*  992:1060 */     Block blockToChange = world.getBlockAt(x, y, z);
/*  993:1061 */     blockToChange.setTypeId(7);
/*  994:1062 */     islandLayer1(x, z, player, world);
/*  995:1063 */     islandLayer2(x, z, player, world);
/*  996:1064 */     islandLayer3(x, z, player, world);
/*  997:1065 */     islandLayer4(x, z, player, world);
/*  998:1066 */     islandExtras(x, z, player, world);
/*  999:     */   }
/* 1000:     */   
/* 1001:     */   public void oldGenerateIslandBlocks(int x, int z, Player player, World world)
/* 1002:     */   {
/* 1003:1070 */     int y = Settings.island_height;
/* 1004:1072 */     for (int x_operate = x; x_operate < x + 3; x_operate++) {
/* 1005:1073 */       for (int y_operate = y; y_operate < y + 3; y_operate++) {
/* 1006:1074 */         for (int z_operate = z; z_operate < z + 6; z_operate++)
/* 1007:     */         {
/* 1008:1075 */           Block blockToChange = world.getBlockAt(x_operate, y_operate, z_operate);
/* 1009:1076 */           blockToChange.setTypeId(2);
/* 1010:     */         }
/* 1011:     */       }
/* 1012:     */     }
/* 1013:1081 */     for (int x_operate = x + 3; x_operate < x + 6; x_operate++) {
/* 1014:1082 */       for (int y_operate = y; y_operate < y + 3; y_operate++) {
/* 1015:1083 */         for (int z_operate = z + 3; z_operate < z + 6; z_operate++)
/* 1016:     */         {
/* 1017:1084 */           Block blockToChange = world.getBlockAt(x_operate, y_operate, z_operate);
/* 1018:1085 */           blockToChange.setTypeId(2);
/* 1019:     */         }
/* 1020:     */       }
/* 1021:     */     }
/* 1022:1091 */     for (int x_operate = x + 3; x_operate < x + 7; x_operate++) {
/* 1023:1092 */       for (int y_operate = y + 7; y_operate < y + 10; y_operate++) {
/* 1024:1093 */         for (int z_operate = z + 3; z_operate < z + 7; z_operate++)
/* 1025:     */         {
/* 1026:1094 */           Block blockToChange = world.getBlockAt(x_operate, y_operate, z_operate);
/* 1027:1095 */           blockToChange.setTypeId(18);
/* 1028:     */         }
/* 1029:     */       }
/* 1030:     */     }
/* 1031:1101 */     for (int y_operate = y + 3; y_operate < y + 9; y_operate++)
/* 1032:     */     {
/* 1033:1102 */       Block blockToChange = world.getBlockAt(x + 5, y_operate, z + 5);
/* 1034:1103 */       blockToChange.setTypeId(17);
/* 1035:     */     }
/* 1036:1108 */     Block blockToChange = world.getBlockAt(x + 1, y + 3, z + 1);
/* 1037:1109 */     blockToChange.setTypeId(54);
/* 1038:1110 */     Chest chest = (Chest)blockToChange.getState();
/* 1039:1111 */     Inventory inventory = chest.getInventory();
/* 1040:1112 */     inventory.clear();
/* 1041:1113 */     inventory.setContents(Settings.island_chestItems);
/* 1042:1114 */     if (Settings.island_addExtraItems) {
/* 1043:1116 */       for (int i = 0; i < Settings.island_extraPermissions.length; i++) {
/* 1044:1118 */         if (VaultHandler.checkPerk(player.getName(), "usb." + Settings.island_extraPermissions[i], player.getWorld()))
/* 1045:     */         {
/* 1046:1120 */           String[] chestItemString = uSkyBlock.getInstance().getConfig().getString("options.island.extraPermissions." + Settings.island_extraPermissions[i]).split(" ");
/* 1047:1121 */           ItemStack[] tempChest = new ItemStack[chestItemString.length];
/* 1048:1122 */           String[] amountdata = new String[2];
/* 1049:1123 */           for (int j = 0; j < chestItemString.length; j++)
/* 1050:     */           {
/* 1051:1125 */             amountdata = chestItemString[j].split(":");
/* 1052:1126 */             tempChest[j] = new ItemStack(Integer.parseInt(amountdata[0]), Integer.parseInt(amountdata[1]));
/* 1053:1127 */             inventory.addItem(new ItemStack[] { tempChest[j] });
/* 1054:     */           }
/* 1055:     */         }
/* 1056:     */       }
/* 1057:     */     }
/* 1058:1133 */     blockToChange = world.getBlockAt(x, y, z);
/* 1059:1134 */     blockToChange.setTypeId(7);
/* 1060:     */     
/* 1061:     */ 
/* 1062:1137 */     blockToChange = world.getBlockAt(x + 2, y + 1, z + 1);
/* 1063:1138 */     blockToChange.setTypeId(12);
/* 1064:1139 */     blockToChange = world.getBlockAt(x + 2, y + 1, z + 2);
/* 1065:1140 */     blockToChange.setTypeId(12);
/* 1066:1141 */     blockToChange = world.getBlockAt(x + 2, y + 1, z + 3);
/* 1067:1142 */     blockToChange.setTypeId(12);
/* 1068:     */   }
/* 1069:     */   
/* 1070:     */   private Location nextIslandLocation(Location lastIsland)
/* 1071:     */   {
/* 1072:1149 */     int x = (int)lastIsland.getX();
/* 1073:1150 */     int z = (int)lastIsland.getZ();
/* 1074:1151 */     Location nextPos = lastIsland;
/* 1075:1152 */     if (x < z)
/* 1076:     */     {
/* 1077:1154 */       if (-1 * x < z)
/* 1078:     */       {
/* 1079:1156 */         nextPos.setX(nextPos.getX() + Settings.island_distance);
/* 1080:1157 */         return nextPos;
/* 1081:     */       }
/* 1082:1159 */       nextPos.setZ(nextPos.getZ() + Settings.island_distance);
/* 1083:1160 */       return nextPos;
/* 1084:     */     }
/* 1085:1162 */     if (x > z)
/* 1086:     */     {
/* 1087:1164 */       if (-1 * x >= z)
/* 1088:     */       {
/* 1089:1166 */         nextPos.setX(nextPos.getX() - Settings.island_distance);
/* 1090:1167 */         return nextPos;
/* 1091:     */       }
/* 1092:1169 */       nextPos.setZ(nextPos.getZ() - Settings.island_distance);
/* 1093:1170 */       return nextPos;
/* 1094:     */     }
/* 1095:1172 */     if (x <= 0)
/* 1096:     */     {
/* 1097:1174 */       nextPos.setZ(nextPos.getZ() + Settings.island_distance);
/* 1098:1175 */       return nextPos;
/* 1099:     */     }
/* 1100:1177 */     nextPos.setZ(nextPos.getZ() - Settings.island_distance);
/* 1101:1178 */     return nextPos;
/* 1102:     */   }
/* 1103:     */   
/* 1104:     */   private void islandLayer1(int x, int z, Player player, World world)
/* 1105:     */   {
/* 1106:1183 */     int y = Settings.island_height;
/* 1107:1184 */     y = Settings.island_height + 4;
/* 1108:1185 */     for (int x_operate = x - 3; x_operate <= x + 3; x_operate++) {
/* 1109:1187 */       for (int z_operate = z - 3; z_operate <= z + 3; z_operate++)
/* 1110:     */       {
/* 1111:1189 */         Block blockToChange = world.getBlockAt(x_operate, y, z_operate);
/* 1112:1190 */         blockToChange.setTypeId(2);
/* 1113:     */       }
/* 1114:     */     }
/* 1115:1193 */     Block blockToChange = world.getBlockAt(x - 3, y, z + 3);
/* 1116:1194 */     blockToChange.setTypeId(0);
/* 1117:1195 */     blockToChange = world.getBlockAt(x - 3, y, z - 3);
/* 1118:1196 */     blockToChange.setTypeId(0);
/* 1119:1197 */     blockToChange = world.getBlockAt(x + 3, y, z - 3);
/* 1120:1198 */     blockToChange.setTypeId(0);
/* 1121:1199 */     blockToChange = world.getBlockAt(x + 3, y, z + 3);
/* 1122:1200 */     blockToChange.setTypeId(0);
/* 1123:     */   }
/* 1124:     */   
/* 1125:     */   private void islandLayer2(int x, int z, Player player, World world)
/* 1126:     */   {
/* 1127:1204 */     int y = Settings.island_height;
/* 1128:1205 */     y = Settings.island_height + 3;
/* 1129:1206 */     for (int x_operate = x - 2; x_operate <= x + 2; x_operate++) {
/* 1130:1208 */       for (int z_operate = z - 2; z_operate <= z + 2; z_operate++)
/* 1131:     */       {
/* 1132:1210 */         Block blockToChange = world.getBlockAt(x_operate, y, z_operate);
/* 1133:1211 */         blockToChange.setTypeId(3);
/* 1134:     */       }
/* 1135:     */     }
/* 1136:1214 */     Block blockToChange = world.getBlockAt(x - 3, y, z);
/* 1137:1215 */     blockToChange.setTypeId(3);
/* 1138:1216 */     blockToChange = world.getBlockAt(x + 3, y, z);
/* 1139:1217 */     blockToChange.setTypeId(3);
/* 1140:1218 */     blockToChange = world.getBlockAt(x, y, z - 3);
/* 1141:1219 */     blockToChange.setTypeId(3);
/* 1142:1220 */     blockToChange = world.getBlockAt(x, y, z + 3);
/* 1143:1221 */     blockToChange.setTypeId(3);
/* 1144:1222 */     blockToChange = world.getBlockAt(x, y, z);
/* 1145:1223 */     blockToChange.setTypeId(12);
/* 1146:     */   }
/* 1147:     */   
/* 1148:     */   private void islandLayer3(int x, int z, Player player, World world)
/* 1149:     */   {
/* 1150:1228 */     int y = Settings.island_height;
/* 1151:1229 */     y = Settings.island_height + 2;
/* 1152:1230 */     for (int x_operate = x - 1; x_operate <= x + 1; x_operate++) {
/* 1153:1232 */       for (int z_operate = z - 1; z_operate <= z + 1; z_operate++)
/* 1154:     */       {
/* 1155:1234 */         Block blockToChange = world.getBlockAt(x_operate, y, z_operate);
/* 1156:1235 */         blockToChange.setTypeId(3);
/* 1157:     */       }
/* 1158:     */     }
/* 1159:1238 */     Block blockToChange = world.getBlockAt(x - 2, y, z);
/* 1160:1239 */     blockToChange.setTypeId(3);
/* 1161:1240 */     blockToChange = world.getBlockAt(x + 2, y, z);
/* 1162:1241 */     blockToChange.setTypeId(3);
/* 1163:1242 */     blockToChange = world.getBlockAt(x, y, z - 2);
/* 1164:1243 */     blockToChange.setTypeId(3);
/* 1165:1244 */     blockToChange = world.getBlockAt(x, y, z + 2);
/* 1166:1245 */     blockToChange.setTypeId(3);
/* 1167:1246 */     blockToChange = world.getBlockAt(x, y, z);
/* 1168:1247 */     blockToChange.setTypeId(12);
/* 1169:     */   }
/* 1170:     */   
/* 1171:     */   private void islandLayer4(int x, int z, Player player, World world)
/* 1172:     */   {
/* 1173:1252 */     int y = Settings.island_height;
/* 1174:1253 */     y = Settings.island_height + 1;
/* 1175:1254 */     Block blockToChange = world.getBlockAt(x - 1, y, z);
/* 1176:1255 */     blockToChange.setTypeId(3);
/* 1177:1256 */     blockToChange = world.getBlockAt(x + 1, y, z);
/* 1178:1257 */     blockToChange.setTypeId(3);
/* 1179:1258 */     blockToChange = world.getBlockAt(x, y, z - 1);
/* 1180:1259 */     blockToChange.setTypeId(3);
/* 1181:1260 */     blockToChange = world.getBlockAt(x, y, z + 1);
/* 1182:1261 */     blockToChange.setTypeId(3);
/* 1183:1262 */     blockToChange = world.getBlockAt(x, y, z);
/* 1184:1263 */     blockToChange.setTypeId(12);
/* 1185:     */   }
/* 1186:     */   
/* 1187:     */   private void islandExtras(int x, int z, Player player, World world)
/* 1188:     */   {
/* 1189:1268 */     int y = Settings.island_height;
/* 1190:     */     
/* 1191:1270 */     Block blockToChange = world.getBlockAt(x, y + 5, z);
/* 1192:1271 */     blockToChange.setTypeId(17);
/* 1193:1272 */     blockToChange = world.getBlockAt(x, y + 6, z);
/* 1194:1273 */     blockToChange.setTypeId(17);
/* 1195:1274 */     blockToChange = world.getBlockAt(x, y + 7, z);
/* 1196:1275 */     blockToChange.setTypeId(17);
/* 1197:1276 */     y = Settings.island_height + 8;
/* 1198:1277 */     for (int x_operate = x - 2; x_operate <= x + 2; x_operate++) {
/* 1199:1279 */       for (int z_operate = z - 2; z_operate <= z + 2; z_operate++)
/* 1200:     */       {
/* 1201:1281 */         blockToChange = world.getBlockAt(x_operate, y, z_operate);
/* 1202:1282 */         blockToChange.setTypeId(18);
/* 1203:     */       }
/* 1204:     */     }
/* 1205:1285 */     blockToChange = world.getBlockAt(x + 2, y, z + 2);
/* 1206:1286 */     blockToChange.setTypeId(0);
/* 1207:1287 */     blockToChange = world.getBlockAt(x + 2, y, z - 2);
/* 1208:1288 */     blockToChange.setTypeId(0);
/* 1209:1289 */     blockToChange = world.getBlockAt(x - 2, y, z + 2);
/* 1210:1290 */     blockToChange.setTypeId(0);
/* 1211:1291 */     blockToChange = world.getBlockAt(x - 2, y, z - 2);
/* 1212:1292 */     blockToChange.setTypeId(0);
/* 1213:1293 */     blockToChange = world.getBlockAt(x, y, z);
/* 1214:1294 */     blockToChange.setTypeId(17);
/* 1215:1295 */     y = Settings.island_height + 9;
/* 1216:1296 */     for (int x_operate = x - 1; x_operate <= x + 1; x_operate++) {
/* 1217:1298 */       for (int z_operate = z - 1; z_operate <= z + 1; z_operate++)
/* 1218:     */       {
/* 1219:1300 */         blockToChange = world.getBlockAt(x_operate, y, z_operate);
/* 1220:1301 */         blockToChange.setTypeId(18);
/* 1221:     */       }
/* 1222:     */     }
/* 1223:1304 */     blockToChange = world.getBlockAt(x - 2, y, z);
/* 1224:1305 */     blockToChange.setTypeId(18);
/* 1225:1306 */     blockToChange = world.getBlockAt(x + 2, y, z);
/* 1226:1307 */     blockToChange.setTypeId(18);
/* 1227:1308 */     blockToChange = world.getBlockAt(x, y, z - 2);
/* 1228:1309 */     blockToChange.setTypeId(18);
/* 1229:1310 */     blockToChange = world.getBlockAt(x, y, z + 2);
/* 1230:1311 */     blockToChange.setTypeId(18);
/* 1231:1312 */     blockToChange = world.getBlockAt(x, y, z);
/* 1232:1313 */     blockToChange.setTypeId(17);
/* 1233:1314 */     y = Settings.island_height + 10;
/* 1234:1315 */     blockToChange = world.getBlockAt(x - 1, y, z);
/* 1235:1316 */     blockToChange.setTypeId(18);
/* 1236:1317 */     blockToChange = world.getBlockAt(x + 1, y, z);
/* 1237:1318 */     blockToChange.setTypeId(18);
/* 1238:1319 */     blockToChange = world.getBlockAt(x, y, z - 1);
/* 1239:1320 */     blockToChange.setTypeId(18);
/* 1240:1321 */     blockToChange = world.getBlockAt(x, y, z + 1);
/* 1241:1322 */     blockToChange.setTypeId(18);
/* 1242:1323 */     blockToChange = world.getBlockAt(x, y, z);
/* 1243:1324 */     blockToChange.setTypeId(17);
/* 1244:1325 */     blockToChange = world.getBlockAt(x, y + 1, z);
/* 1245:1326 */     blockToChange.setTypeId(18);
/* 1246:     */     
/* 1247:1328 */     blockToChange = world.getBlockAt(x, Settings.island_height + 5, z + 1);
/* 1248:1329 */     blockToChange.setTypeId(54);
/* 1249:1330 */     Chest chest = (Chest)blockToChange.getState();
/* 1250:1331 */     Inventory inventory = chest.getInventory();
/* 1251:1332 */     inventory.clear();
/* 1252:1333 */     inventory.setContents(Settings.island_chestItems);
/* 1253:1334 */     if (Settings.island_addExtraItems) {
/* 1254:1336 */       for (int i = 0; i < Settings.island_extraPermissions.length; i++) {
/* 1255:1338 */         if (VaultHandler.checkPerk(player.getName(), "usb." + Settings.island_extraPermissions[i], player.getWorld()))
/* 1256:     */         {
/* 1257:1340 */           String[] chestItemString = uSkyBlock.getInstance().getConfig().getString("options.island.extraPermissions." + Settings.island_extraPermissions[i]).split(" ");
/* 1258:1341 */           ItemStack[] tempChest = new ItemStack[chestItemString.length];
/* 1259:1342 */           String[] amountdata = new String[2];
/* 1260:1343 */           for (int j = 0; j < chestItemString.length; j++)
/* 1261:     */           {
/* 1262:1345 */             amountdata = chestItemString[j].split(":");
/* 1263:1346 */             tempChest[j] = new ItemStack(Integer.parseInt(amountdata[0]), Integer.parseInt(amountdata[1]));
/* 1264:1347 */             inventory.addItem(new ItemStack[] { tempChest[j] });
/* 1265:     */           }
/* 1266:     */         }
/* 1267:     */       }
/* 1268:     */     }
/* 1269:     */   }
/* 1270:     */   
/* 1271:     */   private void setNewPlayerIsland(Player player, Location loc)
/* 1272:     */   {
/* 1273:1356 */     ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player.getName())).startNewIsland(loc);
/* 1274:     */     
/* 1275:     */ 
/* 1276:     */ 
/* 1277:     */ 
/* 1278:     */ 
/* 1279:     */ 
/* 1280:     */ 
/* 1281:     */ 
/* 1282:     */ 
/* 1283:     */ 
/* 1284:1367 */     player.teleport(getChestSpawnLoc(loc, player));
/* 1285:1368 */     uSkyBlock.getInstance().homeSet(player);
/* 1286:1369 */     uSkyBlock.getInstance().writePlayerFile(player.getName(), (PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player.getName()));
/* 1287:     */   }
/* 1288:     */   
/* 1289:     */   private void inviteDebug(Player player)
/* 1290:     */   {
/* 1291:1412 */     player.sendMessage(this.inviteList.toString());
/* 1292:     */   }
/* 1293:     */   
/* 1294:     */   private void invitePurge()
/* 1295:     */   {
/* 1296:1417 */     this.inviteList.clear();
/* 1297:1418 */     this.inviteList.put("NoInviter", "NoInvited");
/* 1298:     */   }
/* 1299:     */   
/* 1300:     */   public boolean addPlayertoParty(String playername, String partyleader)
/* 1301:     */   {
/* 1302:1440 */     if (!uSkyBlock.getInstance().getActivePlayers().containsKey(playername))
/* 1303:     */     {
/* 1304:1442 */       System.out.print("Failed to add player to party! (" + playername + ")");
/* 1305:1443 */       return false;
/* 1306:     */     }
/* 1307:1445 */     if (!uSkyBlock.getInstance().getActivePlayers().containsKey(partyleader))
/* 1308:     */     {
/* 1309:1447 */       System.out.print("Failed to add player to party! (" + playername + ")");
/* 1310:1448 */       return false;
/* 1311:     */     }
/* 1312:1450 */     System.out.print("Adding player: " + playername + " to party with leader: " + partyleader);
/* 1313:1451 */     ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(playername)).setJoinParty(partyleader, ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(partyleader)).getIslandLocation());
/* 1314:1452 */     if (!playername.equalsIgnoreCase(partyleader))
/* 1315:     */     {
/* 1316:1454 */       if (((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(partyleader)).getHomeLocation() != null) {
/* 1317:1455 */         ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(playername)).setHomeLocation(((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(partyleader)).getHomeLocation());
/* 1318:     */       } else {
/* 1319:1458 */         ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(playername)).setHomeLocation(((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(partyleader)).getIslandLocation());
/* 1320:     */       }
/* 1321:1461 */       if (!((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(partyleader)).getMembers().contains(playername))
/* 1322:     */       {
/* 1323:1463 */         ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(partyleader)).addMember(playername);
/* 1324:1464 */         System.out.print("Adding player: " + playername + " to " + partyleader + "'s member list.");
/* 1325:     */       }
/* 1326:     */       else
/* 1327:     */       {
/* 1328:1466 */         System.out.print("NOT adding player: " + playername + " to " + partyleader + "'s member list.");
/* 1329:     */       }
/* 1330:1467 */       if (!((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(partyleader)).getMembers().contains(partyleader))
/* 1331:     */       {
/* 1332:1469 */         ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(partyleader)).addMember(partyleader);
/* 1333:1470 */         System.out.print("Adding player: " + partyleader + " to their own member list.");
/* 1334:     */       }
/* 1335:     */       else
/* 1336:     */       {
/* 1337:1472 */         System.out.print("NOT Adding player: " + partyleader + " to their own member list.");
/* 1338:     */       }
/* 1339:     */     }
/* 1340:1474 */     uSkyBlock.getInstance().writePlayerFile(playername, (PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(playername));
/* 1341:1475 */     uSkyBlock.getInstance().writePlayerFile(partyleader, (PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(partyleader));
/* 1342:1477 */     if (!((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(playername)).getPartyLeader().equalsIgnoreCase(partyleader))
/* 1343:     */     {
/* 1344:1479 */       System.out.print("Error adding player to a new party!");
/* 1345:1480 */       return false;
/* 1346:     */     }
/* 1347:1482 */     return true;
/* 1348:     */   }
/* 1349:     */   
/* 1350:     */   public void removePlayerFromParty(String playername, String partyleader)
/* 1351:     */   {
/* 1352:1489 */     if (uSkyBlock.getInstance().getActivePlayers().containsKey(playername))
/* 1353:     */     {
/* 1354:1491 */       if (((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(playername)).getPartyLeader() != null) {
/* 1355:1493 */         if (!((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(playername)).getPartyLeader().equalsIgnoreCase(playername)) {
/* 1356:1494 */           ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(playername)).setHomeLocation(null);
/* 1357:     */         }
/* 1358:     */       }
/* 1359:1496 */       ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(playername)).setLeaveParty();
/* 1360:1497 */       uSkyBlock.getInstance().writePlayerFile(playername, (PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(playername));
/* 1361:     */     }
/* 1362:     */     else
/* 1363:     */     {
/* 1364:1500 */       PlayerInfo pi = uSkyBlock.getInstance().readPlayerFile(playername);
/* 1365:1501 */       if (!pi.getPartyLeader().equalsIgnoreCase(playername)) {
/* 1366:1502 */         pi.setHomeLocation(null);
/* 1367:     */       }
/* 1368:1503 */       pi.setLeaveParty();
/* 1369:1504 */       uSkyBlock.getInstance().writePlayerFile(playername, pi);
/* 1370:     */     }
/* 1371:1506 */     if (uSkyBlock.getInstance().getActivePlayers().containsKey(partyleader))
/* 1372:     */     {
/* 1373:1508 */       if (((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(partyleader)).getMembers().contains(playername)) {
/* 1374:1510 */         ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(partyleader)).removeMember(playername);
/* 1375:     */       }
/* 1376:1512 */       uSkyBlock.getInstance().writePlayerFile(partyleader, (PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(partyleader));
/* 1377:     */     }
/* 1378:     */     else
/* 1379:     */     {
/* 1380:1515 */       PlayerInfo pi = uSkyBlock.getInstance().readPlayerFile(partyleader);
/* 1381:1516 */       if (pi.getMembers().contains(playername)) {
/* 1382:1517 */         pi.removeMember(playername);
/* 1383:     */       }
/* 1384:1518 */       uSkyBlock.getInstance().writePlayerFile(partyleader, pi);
/* 1385:     */     }
/* 1386:     */   }
/* 1387:     */   
/* 1388:     */   public <T, E> T getKeyByValue(Map<T, E> map, E value)
/* 1389:     */   {
/* 1390:1523 */     for (Map.Entry<T, E> entry : map.entrySet()) {
/* 1391:1524 */       if (value.equals(entry.getValue())) {
/* 1392:1525 */         return entry.getKey();
/* 1393:     */       }
/* 1394:     */     }
/* 1395:1528 */     return null;
/* 1396:     */   }
/* 1397:     */   
/* 1398:     */   public boolean getIslandLevel(Player player, String islandPlayer)
/* 1399:     */   {
/* 1400:1533 */     if (this.allowInfo)
/* 1401:     */     {
/* 1402:1535 */       System.out.print("Preparing to calculate island level");
/* 1403:1536 */       this.allowInfo = false;
/* 1404:1537 */       final String playerx = player.getName();
/* 1405:1538 */       final String islandPlayerx = islandPlayer;
/* 1406:1539 */       if ((!uSkyBlock.getInstance().hasIsland(islandPlayer)) && (!uSkyBlock.getInstance().hasParty(islandPlayer)))
/* 1407:     */       {
/* 1408:1541 */         player.sendMessage(ChatColor.RED + "That player is invalid or does not have an island!");
/* 1409:1542 */         this.allowInfo = true;
/* 1410:1543 */         return false;
/* 1411:     */       }
/* 1412:1545 */       uSkyBlock.getInstance().getServer().getScheduler().runTaskAsynchronously(uSkyBlock.getInstance(), new Runnable()
/* 1413:     */       {
/* 1414:     */         public void run()
/* 1415:     */         {
/* 1416:1549 */           System.out.print("Calculating island level in async thread");
/* 1417:     */           try
/* 1418:     */           {
/* 1419:1551 */             String player = playerx;
/* 1420:1552 */             String islandPlayer = islandPlayerx;
/* 1421:     */             Location l;
/* 1422:     */             Location l;
/* 1423:1559 */             if (((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player)).getHasParty()) {
/* 1424:1561 */               l = ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player)).getPartyIslandLocation();
/* 1425:     */             } else {
/* 1426:1564 */               l = ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player)).getIslandLocation();
/* 1427:     */             }
/* 1428:1565 */             int blockcount = 0;
/* 1429:1566 */             if (player.equalsIgnoreCase(islandPlayer))
/* 1430:     */             {
/* 1431:1568 */               int cobblecount = 0;
/* 1432:1569 */               int endcount = 0;
/* 1433:1570 */               int px = l.getBlockX();
/* 1434:1571 */               int py = l.getBlockY();
/* 1435:1572 */               int pz = l.getBlockZ();
/* 1436:1573 */               for (int x = -50; x <= 50; x++) {
/* 1437:1574 */                 for (int y = Settings.island_height * -1; y <= 255 - Settings.island_height; y++) {
/* 1438:1575 */                   for (int z = -50; z <= 50; z++)
/* 1439:     */                   {
/* 1440:1577 */                     Block b = new Location(l.getWorld(), px + x, py + y, pz + z).getBlock();
/* 1441:1578 */                     if (b.getTypeId() == 57) {
/* 1442:1579 */                       blockcount += 300;
/* 1443:     */                     }
/* 1444:1580 */                     if ((b.getTypeId() == 41) || (b.getTypeId() == 116) || (b.getTypeId() == 122)) {
/* 1445:1581 */                       blockcount += 150;
/* 1446:     */                     }
/* 1447:1582 */                     if ((b.getTypeId() == 49) || (b.getTypeId() == 42)) {
/* 1448:1583 */                       blockcount += 10;
/* 1449:     */                     }
/* 1450:1584 */                     if ((b.getTypeId() == 47) || (b.getTypeId() == 84)) {
/* 1451:1585 */                       blockcount += 5;
/* 1452:     */                     }
/* 1453:1586 */                     if ((b.getTypeId() == 79) || (b.getTypeId() == 82) || (b.getTypeId() == 112) || (b.getTypeId() == 2) || (b.getTypeId() == 110)) {
/* 1454:1587 */                       blockcount += 3;
/* 1455:     */                     }
/* 1456:1588 */                     if ((b.getTypeId() == 45) || (b.getTypeId() == 35) || (b.getTypeId() == 24) || 
/* 1457:1589 */                       (b.getTypeId() == 121) || (b.getTypeId() == 108) || (b.getTypeId() == 109) || (b.getTypeId() == 43) || 
/* 1458:1590 */                       (b.getTypeId() == 20) || (b.getTypeId() == 89) || (b.getTypeId() == 155) || (b.getTypeId() == 156)) {
/* 1459:1591 */                       blockcount += 2;
/* 1460:     */                     }
/* 1461:1592 */                     if (((b.getTypeId() != 0) && (b.getTypeId() != 8) && (b.getTypeId() != 106) && (b.getTypeId() != 9) && (b.getTypeId() != 10) && (b.getTypeId() != 11) && (b.getTypeId() != 4)) || ((b.getTypeId() == 4) && (cobblecount < 10000)) || ((b.getTypeId() == 121) && (endcount < 5000)))
/* 1462:     */                     {
/* 1463:1594 */                       blockcount++;
/* 1464:1595 */                       if (b.getTypeId() == 4) {
/* 1465:1597 */                         cobblecount++;
/* 1466:     */                       }
/* 1467:1599 */                       if (b.getTypeId() == 121) {
/* 1468:1601 */                         endcount++;
/* 1469:     */                       }
/* 1470:     */                     }
/* 1471:     */                   }
/* 1472:     */                 }
/* 1473:     */               }
/* 1474:     */             }
/* 1475:1609 */             if (player.equalsIgnoreCase(islandPlayer)) {
/* 1476:1611 */               ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player)).setIslandLevel(blockcount / 100);
/* 1477:     */             }
/* 1478:     */           }
/* 1479:     */           catch (Exception e)
/* 1480:     */           {
/* 1481:1614 */             System.out.print("Error while calculating Island Level: " + e);
/* 1482:1615 */             IslandCommand.this.allowInfo = true;
/* 1483:     */           }
/* 1484:1617 */           System.out.print("Finished async info thread");
/* 1485:     */           
/* 1486:     */ 
/* 1487:1620 */           uSkyBlock.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(uSkyBlock.getInstance(), new Runnable()
/* 1488:     */           {
/* 1489:     */             public void run()
/* 1490:     */             {
/* 1491:1625 */               IslandCommand.this.allowInfo = true;
/* 1492:1626 */               System.out.print("Back to sync thread for info");
/* 1493:1627 */               if (Bukkit.getPlayer(this.val$playerx) != null)
/* 1494:     */               {
/* 1495:1629 */                 Bukkit.getPlayer(this.val$playerx).sendMessage(ChatColor.YELLOW + "Information about " + this.val$islandPlayerx + "'s Island:");
/* 1496:1630 */                 if (this.val$playerx.equalsIgnoreCase(this.val$islandPlayerx))
/* 1497:     */                 {
/* 1498:1632 */                   Bukkit.getPlayer(this.val$playerx).sendMessage(ChatColor.GREEN + "Island level is " + ChatColor.WHITE + ((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(this.val$playerx)).getIslandLevel());
/* 1499:     */                 }
/* 1500:     */                 else
/* 1501:     */                 {
/* 1502:1635 */                   PlayerInfo pi = uSkyBlock.getInstance().readPlayerFile(this.val$islandPlayerx);
/* 1503:1636 */                   if (pi != null) {
/* 1504:1637 */                     Bukkit.getPlayer(this.val$playerx).sendMessage(ChatColor.GREEN + "Island level is " + ChatColor.WHITE + pi.getIslandLevel());
/* 1505:     */                   } else {
/* 1506:1639 */                     Bukkit.getPlayer(this.val$playerx).sendMessage(ChatColor.RED + "Error: Invalid Player");
/* 1507:     */                   }
/* 1508:     */                 }
/* 1509:     */               }
/* 1510:1642 */               System.out.print("Finished with sync thread for info");
/* 1511:     */             }
/* 1512:1644 */           }, 0L);
/* 1513:     */         }
/* 1514:     */       });
/* 1515:     */     }
/* 1516:     */     else
/* 1517:     */     {
/* 1518:1649 */       player.sendMessage(ChatColor.RED + "Can't use that command right now! Try again in a few seconds.");
/* 1519:1650 */       System.out.print(player.getName() + " tried to use /island info but someone else used it first!");
/* 1520:1651 */       return false;
/* 1521:     */     }
/* 1522:1653 */     return true;
/* 1523:     */   }
/* 1524:     */   
/* 1525:     */   public void setChest(Location loc, Player player)
/* 1526:     */   {
/* 1527:1658 */     for (int x = -15; x <= 15; x++) {
/* 1528:1659 */       for (int y = -15; y <= 15; y++) {
/* 1529:1660 */         for (int z = -15; z <= 15; z++) {
/* 1530:1661 */           if (uSkyBlock.getSkyBlockWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z).getTypeId() == 54)
/* 1531:     */           {
/* 1532:1663 */             Block blockToChange = uSkyBlock.getSkyBlockWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z);
/* 1533:1664 */             Chest chest = (Chest)blockToChange.getState();
/* 1534:1665 */             Inventory inventory = chest.getInventory();
/* 1535:1666 */             inventory.clear();
/* 1536:1667 */             inventory.setContents(Settings.island_chestItems);
/* 1537:1668 */             if (Settings.island_addExtraItems) {
/* 1538:1670 */               for (int i = 0; i < Settings.island_extraPermissions.length; i++) {
/* 1539:1672 */                 if (VaultHandler.checkPerk(player.getName(), "usb." + Settings.island_extraPermissions[i], player.getWorld()))
/* 1540:     */                 {
/* 1541:1674 */                   String[] chestItemString = uSkyBlock.getInstance().getConfig().getString("options.island.extraPermissions." + Settings.island_extraPermissions[i]).split(" ");
/* 1542:1675 */                   ItemStack[] tempChest = new ItemStack[chestItemString.length];
/* 1543:1676 */                   String[] amountdata = new String[2];
/* 1544:1677 */                   for (int j = 0; j < chestItemString.length; j++)
/* 1545:     */                   {
/* 1546:1679 */                     amountdata = chestItemString[j].split(":");
/* 1547:1680 */                     tempChest[j] = new ItemStack(Integer.parseInt(amountdata[0]), Integer.parseInt(amountdata[1]));
/* 1548:1681 */                     inventory.addItem(new ItemStack[] { tempChest[j] });
/* 1549:     */                   }
/* 1550:     */                 }
/* 1551:     */               }
/* 1552:     */             }
/* 1553:     */           }
/* 1554:     */         }
/* 1555:     */       }
/* 1556:     */     }
/* 1557:     */   }
/* 1558:     */   
/* 1559:     */   public Location getChestSpawnLoc(Location loc, Player player)
/* 1560:     */   {
/* 1561:1695 */     for (int x = -15; x <= 15; x++) {
/* 1562:1696 */       for (int y = -15; y <= 15; y++) {
/* 1563:1697 */         for (int z = -15; z <= 15; z++) {
/* 1564:1698 */           if (uSkyBlock.getSkyBlockWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z).getTypeId() == 54)
/* 1565:     */           {
/* 1566:1700 */             if ((uSkyBlock.getSkyBlockWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + (z + 1)).getTypeId() == 0) && 
/* 1567:1701 */               (uSkyBlock.getSkyBlockWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + (y - 1), loc.getBlockZ() + (z + 1)).getTypeId() != 0)) {
/* 1568:1702 */               return new Location(uSkyBlock.getSkyBlockWorld(), loc.getBlockX() + x, loc.getBlockY() + (y + 1), loc.getBlockZ() + (z + 1));
/* 1569:     */             }
/* 1570:1703 */             if ((uSkyBlock.getSkyBlockWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + (z - 1)).getTypeId() == 0) && 
/* 1571:1704 */               (uSkyBlock.getSkyBlockWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + (y - 1), loc.getBlockZ() + (z - 1)).getTypeId() != 0)) {
/* 1572:1705 */               return new Location(uSkyBlock.getSkyBlockWorld(), loc.getBlockX() + x, loc.getBlockY() + (y + 1), loc.getBlockZ() + (z + 1));
/* 1573:     */             }
/* 1574:1706 */             if ((uSkyBlock.getSkyBlockWorld().getBlockAt(loc.getBlockX() + (x + 1), loc.getBlockY() + y, loc.getBlockZ() + z).getTypeId() == 0) && 
/* 1575:1707 */               (uSkyBlock.getSkyBlockWorld().getBlockAt(loc.getBlockX() + (x + 1), loc.getBlockY() + (y - 1), loc.getBlockZ() + z).getTypeId() != 0)) {
/* 1576:1708 */               return new Location(uSkyBlock.getSkyBlockWorld(), loc.getBlockX() + x, loc.getBlockY() + (y + 1), loc.getBlockZ() + (z + 1));
/* 1577:     */             }
/* 1578:1709 */             if ((uSkyBlock.getSkyBlockWorld().getBlockAt(loc.getBlockX() + (x - 1), loc.getBlockY() + y, loc.getBlockZ() + z).getTypeId() == 0) && 
/* 1579:1710 */               (uSkyBlock.getSkyBlockWorld().getBlockAt(loc.getBlockX() + (x - 1), loc.getBlockY() + (y - 1), loc.getBlockZ() + z).getTypeId() != 0)) {
/* 1580:1711 */               return new Location(uSkyBlock.getSkyBlockWorld(), loc.getBlockX() + x, loc.getBlockY() + (y + 1), loc.getBlockZ() + (z + 1));
/* 1581:     */             }
/* 1582:1712 */             return loc;
/* 1583:     */           }
/* 1584:     */         }
/* 1585:     */       }
/* 1586:     */     }
/* 1587:1717 */     return loc;
/* 1588:     */   }
/* 1589:     */ }


/* Location:           C:\Pokechu22\TestServer\plugins\uSkyBlock.jar
 * Qualified Name:     us.talabrek.ultimateskyblock.IslandCommand
 * JD-Core Version:    0.7.0.1
 */