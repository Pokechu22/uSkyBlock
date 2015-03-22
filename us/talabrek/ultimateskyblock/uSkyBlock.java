/*    1:     */ package us.talabrek.ultimateskyblock;
/*    2:     */ 
/*    3:     */ import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
/*    4:     */ import com.sk89q.worldguard.protection.managers.RegionManager;
/*    5:     */ import java.io.File;
/*    6:     */ import java.io.FileInputStream;
/*    7:     */ import java.io.FileOutputStream;
/*    8:     */ import java.io.IOException;
/*    9:     */ import java.io.InputStream;
/*   10:     */ import java.io.ObjectInputStream;
/*   11:     */ import java.io.ObjectOutputStream;
/*   12:     */ import java.io.PrintStream;
/*   13:     */ import java.util.ArrayList;
/*   14:     */ import java.util.Calendar;
/*   15:     */ import java.util.Collections;
/*   16:     */ import java.util.HashMap;
/*   17:     */ import java.util.Iterator;
/*   18:     */ import java.util.LinkedHashMap;
/*   19:     */ import java.util.List;
/*   20:     */ import java.util.Set;
/*   21:     */ import java.util.Stack;
/*   22:     */ import java.util.logging.Logger;
/*   23:     */ import net.milkbowl.vault.economy.Economy;
/*   24:     */ import org.bukkit.Bukkit;
/*   25:     */ import org.bukkit.ChatColor;
/*   26:     */ import org.bukkit.Chunk;
/*   27:     */ import org.bukkit.Location;
/*   28:     */ import org.bukkit.Material;
/*   29:     */ import org.bukkit.OfflinePlayer;
/*   30:     */ import org.bukkit.Server;
/*   31:     */ import org.bukkit.World;
/*   32:     */ import org.bukkit.World.Environment;
/*   33:     */ import org.bukkit.WorldCreator;
/*   34:     */ import org.bukkit.WorldType;
/*   35:     */ import org.bukkit.block.Block;
/*   36:     */ import org.bukkit.block.BlockFace;
/*   37:     */ import org.bukkit.block.Chest;
/*   38:     */ import org.bukkit.block.Dispenser;
/*   39:     */ import org.bukkit.block.Furnace;
/*   40:     */ import org.bukkit.command.CommandSender;
/*   41:     */ import org.bukkit.command.PluginCommand;
/*   42:     */ import org.bukkit.configuration.ConfigurationSection;
/*   43:     */ import org.bukkit.configuration.file.FileConfiguration;
/*   44:     */ import org.bukkit.configuration.file.YamlConfiguration;
/*   45:     */ import org.bukkit.entity.Entity;
/*   46:     */ import org.bukkit.entity.EntityType;
/*   47:     */ import org.bukkit.entity.Player;
/*   48:     */ import org.bukkit.generator.ChunkGenerator;
/*   49:     */ import org.bukkit.inventory.FurnaceInventory;
/*   50:     */ import org.bukkit.inventory.Inventory;
/*   51:     */ import org.bukkit.inventory.ItemStack;
/*   52:     */ import org.bukkit.inventory.PlayerInventory;
/*   53:     */ import org.bukkit.plugin.PluginDescriptionFile;
/*   54:     */ import org.bukkit.plugin.PluginManager;
/*   55:     */ import org.bukkit.plugin.java.JavaPlugin;
/*   56:     */ import org.bukkit.scheduler.BukkitScheduler;
/*   57:     */ 
/*   58:     */ public class uSkyBlock
/*   59:     */   extends JavaPlugin
/*   60:     */ {
/*   61:     */   public PluginDescriptionFile pluginFile;
/*   62:     */   public Logger log;
/*   63:  57 */   public static World skyBlockWorld = null;
/*   64:     */   private static uSkyBlock instance;
/*   65:  59 */   public List<String> removeList = new ArrayList();
/*   66:     */   List<String> rankDisplay;
/*   67:     */   public FileConfiguration configPlugin;
/*   68:     */   public File filePlugin;
/*   69:     */   private Location lastIsland;
/*   70:  64 */   private Stack<Location> orphaned = new Stack();
/*   71:     */   public File directoryPlayers;
/*   72:     */   private File directorySchematics;
/*   73:     */   public File[] schemFile;
/*   74:     */   public String pName;
/*   75:  73 */   public Location islandTestLocation = null;
/*   76:     */   LinkedHashMap<String, Double> topTen;
/*   77:  75 */   HashMap<String, Long> infoCooldown = new HashMap();
/*   78:  76 */   HashMap<String, Long> restartCooldown = new HashMap();
/*   79:  77 */   HashMap<String, PlayerInfo> activePlayers = new HashMap();
/*   80:  78 */   LinkedHashMap<String, List<String>> challenges = new LinkedHashMap();
/*   81:  79 */   HashMap<Integer, Integer> requiredList = new HashMap();
/*   82:  80 */   public boolean purgeActive = false;
/*   83:  81 */   private FileConfiguration skyblockData = null;
/*   84:  82 */   private File skyblockDataFile = null;
/*   85:     */   private ArrayList<File> sfiles;
/*   86:     */   
/*   87:     */   public void onDisable()
/*   88:     */   {
/*   89:     */     try
/*   90:     */     {
/*   91:  87 */       unloadPlayerFiles();
/*   92:  89 */       if (this.lastIsland != null) {
/*   93:  91 */         setLastIsland(this.lastIsland);
/*   94:     */       }
/*   95:  94 */       File f2 = new File(getDataFolder(), "orphanedIslands.bin");
/*   96:  97 */       if (this.orphaned != null) {
/*   97:  99 */         if (!this.orphaned.isEmpty()) {
/*   98: 100 */           SLAPI.save(changeStackToFile(this.orphaned), f2);
/*   99:     */         }
/*  100:     */       }
/*  101:     */     }
/*  102:     */     catch (Exception e)
/*  103:     */     {
/*  104: 105 */       System.out.println("Something went wrong saving the island and/or party data!");
/*  105: 106 */       e.printStackTrace();
/*  106:     */     }
/*  107: 108 */     this.log.info(this.pluginFile.getName() + " v" + this.pluginFile.getVersion() + " disabled.");
/*  108:     */   }
/*  109:     */   
/*  110:     */   public void onEnable()
/*  111:     */   {
/*  112: 113 */     instance = this;
/*  113: 114 */     saveDefaultConfig();
/*  114: 115 */     this.pluginFile = getDescription();
/*  115: 116 */     this.log = getLogger();
/*  116: 117 */     this.pName = (ChatColor.WHITE + "[" + ChatColor.GREEN + this.pluginFile.getName() + ChatColor.WHITE + "] ");
/*  117:     */     try
/*  118:     */     {
/*  119: 119 */       Metrics metrics = new Metrics(this);
/*  120: 120 */       metrics.start();
/*  121:     */     }
/*  122:     */     catch (IOException localIOException) {}
/*  123: 125 */     VaultHandler.setupEconomy();
/*  124: 126 */     if (!getDataFolder().exists()) {
/*  125: 127 */       getDataFolder().mkdir();
/*  126:     */     }
/*  127: 130 */     this.configPlugin = getConfig();
/*  128: 131 */     this.filePlugin = new File(getDataFolder(), "config.yml");
/*  129: 132 */     loadPluginConfig();
/*  130: 133 */     registerEvents();
/*  131: 134 */     this.directoryPlayers = new File(getDataFolder() + File.separator + "players");
/*  132: 135 */     if (!this.directoryPlayers.exists())
/*  133:     */     {
/*  134: 136 */       this.directoryPlayers.mkdir();
/*  135: 137 */       loadPlayerFiles();
/*  136:     */     }
/*  137:     */     else
/*  138:     */     {
/*  139: 139 */       loadPlayerFiles();
/*  140:     */     }
/*  141: 142 */     this.directorySchematics = new File(getDataFolder() + File.separator + "schematics");
/*  142: 143 */     if (!this.directorySchematics.exists()) {
/*  143: 144 */       this.directorySchematics.mkdir();
/*  144:     */     }
/*  145: 146 */     this.schemFile = this.directorySchematics.listFiles();
/*  146: 147 */     if (this.schemFile == null) {
/*  147: 149 */       System.out.print("[uSkyBlock] No schematic file loaded.");
/*  148:     */     } else {
/*  149: 151 */       System.out.print("[uSkyBlock] " + this.schemFile.length + " schematics loaded.");
/*  150:     */     }
/*  151:     */     try
/*  152:     */     {
/*  153: 172 */       if (new File(getDataFolder(), "orphanedIslands.bin").exists())
/*  154:     */       {
/*  155: 174 */         Stack<SerializableLocation> load = (Stack)SLAPI.load(new File(getDataFolder(), "orphanedIslands.bin"));
/*  156: 175 */         if (load != null) {
/*  157: 177 */           if (!load.isEmpty()) {
/*  158: 178 */             this.orphaned = changestackfromfile(load);
/*  159:     */           }
/*  160:     */         }
/*  161:     */       }
/*  162:     */       else
/*  163:     */       {
/*  164: 182 */         System.out.print("Creating a new orphan file");
/*  165: 183 */         new File("orphanedIslands.bin");
/*  166:     */       }
/*  167:     */     }
/*  168:     */     catch (Exception e)
/*  169:     */     {
/*  170: 187 */       System.out.println("Could not load Island and/or Party data from disk.");
/*  171: 188 */       e.printStackTrace();
/*  172:     */     }
/*  173: 193 */     getCommand("island").setExecutor(new IslandCommand());
/*  174: 194 */     getCommand("challenges").setExecutor(new ChallengesCommand());
/*  175: 195 */     getCommand("dev").setExecutor(new DevCommand());
/*  176: 202 */     if (Settings.island_useTopTen) {
/*  177: 203 */       getInstance().updateTopTen(getInstance().generateTopTen());
/*  178:     */     }
/*  179: 204 */     populateChallengeList();
/*  180: 205 */     this.log.info(this.pluginFile.getName() + " v." + this.pluginFile.getVersion() + " enabled.");
/*  181: 206 */     getInstance().getServer().getScheduler().runTaskLater(getInstance(), new Runnable()
/*  182:     */     {
/*  183:     */       public void run()
/*  184:     */       {
/*  185: 210 */         if (Bukkit.getServer().getPluginManager().isPluginEnabled("Vault"))
/*  186:     */         {
/*  187: 212 */           System.out.print("[uSkyBlock] Using vault for permissions");
/*  188: 213 */           VaultHandler.setupPermissions();
/*  189:     */           try
/*  190:     */           {
/*  191: 215 */             uSkyBlock.this.lastIsland = new Location(uSkyBlock.getSkyBlockWorld(), uSkyBlock.this.getConfig().getInt("options.general.lastIslandX"), Settings.island_height, uSkyBlock.this.getConfig().getInt("options.general.lastIslandZ"));
/*  192:     */           }
/*  193:     */           catch (Exception e)
/*  194:     */           {
/*  195: 217 */             uSkyBlock.this.lastIsland = new Location(uSkyBlock.getSkyBlockWorld(), 0.0D, Settings.island_height, 0.0D);
/*  196:     */           }
/*  197: 219 */           if (uSkyBlock.this.lastIsland == null) {
/*  198: 221 */             uSkyBlock.this.lastIsland = new Location(uSkyBlock.getSkyBlockWorld(), 0.0D, Settings.island_height, 0.0D);
/*  199:     */           }
/*  200: 224 */           if ((Settings.island_protectWithWorldGuard) && (!Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard")))
/*  201:     */           {
/*  202: 226 */             PluginManager manager = uSkyBlock.getInstance().getServer().getPluginManager();
/*  203: 227 */             System.out.print("[uSkyBlock] WorldGuard not loaded! Using built in protection.");
/*  204: 228 */             manager.registerEvents(new ProtectionEvents(), uSkyBlock.getInstance());
/*  205:     */           }
/*  206:     */         }
/*  207:     */       }
/*  208: 233 */     }, 0L);
/*  209:     */   }
/*  210:     */   
/*  211:     */   public static uSkyBlock getInstance()
/*  212:     */   {
/*  213: 237 */     return instance;
/*  214:     */   }
/*  215:     */   
/*  216:     */   public void loadPlayerFiles()
/*  217:     */   {
/*  218: 242 */     int onlinePlayerCount = 0;
/*  219: 243 */     onlinePlayerCount = Bukkit.getServer().getOnlinePlayers().length;
/*  220: 244 */     Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
/*  221: 245 */     for (int i = 0; i < onlinePlayerCount; i++) {
/*  222: 247 */       if (onlinePlayers[i].isOnline())
/*  223:     */       {
/*  224: 249 */         PlayerInfo pi = getInstance().readPlayerFile(onlinePlayers[i].getName());
/*  225: 250 */         if (pi == null)
/*  226:     */         {
/*  227: 252 */           System.out.print("Creating a new skyblock file for " + onlinePlayers[i].getName());
/*  228: 253 */           pi = new PlayerInfo(onlinePlayers[i].getName());
/*  229: 254 */           getInstance().writePlayerFile(onlinePlayers[i].getName(), pi);
/*  230:     */         }
/*  231: 256 */         if ((pi.getHasParty()) && (pi.getPartyIslandLocation() == null))
/*  232:     */         {
/*  233: 258 */           PlayerInfo pi2 = getInstance().readPlayerFile(pi.getPartyLeader());
/*  234: 259 */           pi.setPartyIslandLocation(pi2.getIslandLocation());
/*  235: 260 */           getInstance().writePlayerFile(onlinePlayers[i].getName(), pi);
/*  236:     */         }
/*  237: 262 */         pi.buildChallengeList();
/*  238: 263 */         getInstance().addActivePlayer(onlinePlayers[i].getName(), pi);
/*  239:     */       }
/*  240:     */     }
/*  241:     */   }
/*  242:     */   
/*  243:     */   public void unloadPlayerFiles()
/*  244:     */   {
/*  245: 273 */     for (int i = 0; i < Bukkit.getServer().getOnlinePlayers().length; i++)
/*  246:     */     {
/*  247: 275 */       Player[] removedPlayers = Bukkit.getServer().getOnlinePlayers();
/*  248: 276 */       if (getActivePlayers().containsKey(removedPlayers[i].getName())) {
/*  249: 277 */         removeActivePlayer(removedPlayers[i].getName());
/*  250:     */       }
/*  251:     */     }
/*  252:     */   }
/*  253:     */   
/*  254:     */   public void registerEvents()
/*  255:     */   {
/*  256: 286 */     PluginManager manager = getServer().getPluginManager();
/*  257:     */     
/*  258:     */ 
/*  259: 289 */     manager.registerEvents(new PlayerJoin(), this);
/*  260: 290 */     if (!Settings.island_protectWithWorldGuard)
/*  261:     */     {
/*  262: 292 */       System.out.print("[uSkyBlock] Using built in protection.");
/*  263: 293 */       manager.registerEvents(new ProtectionEvents(), getInstance());
/*  264:     */     }
/*  265:     */   }
/*  266:     */   
/*  267:     */   public void loadPluginConfig()
/*  268:     */   {
/*  269:     */     try
/*  270:     */     {
/*  271: 306 */       getConfig();
/*  272:     */     }
/*  273:     */     catch (Exception e)
/*  274:     */     {
/*  275: 308 */       e.printStackTrace();
/*  276:     */     }
/*  277:     */     try
/*  278:     */     {
/*  279: 313 */       Settings.general_maxPartySize = getConfig().getInt("options.general.maxPartySize");
/*  280: 314 */       if (Settings.general_maxPartySize < 0) {
/*  281: 315 */         Settings.general_maxPartySize = 0;
/*  282:     */       }
/*  283:     */     }
/*  284:     */     catch (Exception e)
/*  285:     */     {
/*  286: 318 */       Settings.general_maxPartySize = 4;
/*  287:     */     }
/*  288:     */     try
/*  289:     */     {
/*  290: 321 */       Settings.island_distance = getConfig().getInt("options.island.distance");
/*  291: 322 */       if (Settings.island_distance < 50) {
/*  292: 323 */         Settings.island_distance = 50;
/*  293:     */       }
/*  294:     */     }
/*  295:     */     catch (Exception e)
/*  296:     */     {
/*  297: 326 */       Settings.island_distance = 110;
/*  298:     */     }
/*  299:     */     try
/*  300:     */     {
/*  301: 329 */       Settings.island_protectionRange = getConfig().getInt("options.island.protectionRange");
/*  302: 330 */       if (Settings.island_protectionRange > Settings.island_distance) {
/*  303: 331 */         Settings.island_protectionRange = Settings.island_distance;
/*  304:     */       }
/*  305:     */     }
/*  306:     */     catch (Exception e)
/*  307:     */     {
/*  308: 334 */       Settings.island_protectionRange = 100;
/*  309:     */     }
/*  310:     */     try
/*  311:     */     {
/*  312: 337 */       Settings.general_cooldownInfo = getConfig().getInt("options.general.cooldownInfo");
/*  313: 338 */       if (Settings.general_cooldownInfo < 0) {
/*  314: 339 */         Settings.general_cooldownInfo = 0;
/*  315:     */       }
/*  316:     */     }
/*  317:     */     catch (Exception e)
/*  318:     */     {
/*  319: 342 */       Settings.general_cooldownInfo = 60;
/*  320:     */     }
/*  321:     */     try
/*  322:     */     {
/*  323: 345 */       Settings.general_cooldownRestart = getConfig().getInt("options.general.cooldownRestart");
/*  324: 346 */       if (Settings.general_cooldownRestart < 0) {
/*  325: 347 */         Settings.general_cooldownRestart = 0;
/*  326:     */       }
/*  327:     */     }
/*  328:     */     catch (Exception e)
/*  329:     */     {
/*  330: 350 */       Settings.general_cooldownRestart = 60;
/*  331:     */     }
/*  332:     */     try
/*  333:     */     {
/*  334: 353 */       Settings.island_height = getConfig().getInt("options.island.height");
/*  335: 354 */       if (Settings.island_height < 20) {
/*  336: 355 */         Settings.island_height = 20;
/*  337:     */       }
/*  338:     */     }
/*  339:     */     catch (Exception e)
/*  340:     */     {
/*  341: 358 */       Settings.island_height = 120;
/*  342:     */     }
/*  343:     */     try
/*  344:     */     {
/*  345: 361 */       Settings.challenges_rankLeeway = getConfig().getInt("options.challenges.rankLeeway");
/*  346: 362 */       if (Settings.challenges_rankLeeway < 0) {
/*  347: 363 */         Settings.challenges_rankLeeway = 0;
/*  348:     */       }
/*  349:     */     }
/*  350:     */     catch (Exception e)
/*  351:     */     {
/*  352: 366 */       Settings.challenges_rankLeeway = 0;
/*  353:     */     }
/*  354: 369 */     if (!getConfig().contains("options.extras.obsidianToLava"))
/*  355:     */     {
/*  356: 371 */       getConfig().set("options.extras.obsidianToLava", Boolean.valueOf(true));
/*  357: 372 */       saveConfig();
/*  358:     */     }
/*  359: 374 */     if (!getConfig().contains("options.general.spawnSize"))
/*  360:     */     {
/*  361: 376 */       getConfig().set("options.general.spawnSize", Integer.valueOf(50));
/*  362: 377 */       saveConfig();
/*  363:     */     }
/*  364:     */     try
/*  365:     */     {
/*  366: 380 */       Settings.general_spawnSize = getConfig().getInt("options.general.spawnSize");
/*  367: 381 */       if (Settings.general_spawnSize < 50) {
/*  368: 382 */         Settings.general_spawnSize = 50;
/*  369:     */       }
/*  370:     */     }
/*  371:     */     catch (Exception e)
/*  372:     */     {
/*  373: 385 */       Settings.general_spawnSize = 50;
/*  374:     */     }
/*  375: 390 */     String[] chestItemString = getConfig().getString("options.island.chestItems").split(" ");
/*  376: 391 */     ItemStack[] tempChest = new ItemStack[chestItemString.length];
/*  377: 392 */     String[] amountdata = new String[2];
/*  378: 393 */     for (int i = 0; i < tempChest.length; i++)
/*  379:     */     {
/*  380: 395 */       amountdata = chestItemString[i].split(":");
/*  381: 396 */       tempChest[i] = new ItemStack(Integer.parseInt(amountdata[0]), Integer.parseInt(amountdata[1]));
/*  382:     */     }
/*  383: 398 */     Settings.island_chestItems = tempChest;
/*  384: 399 */     Settings.island_allowPvP = getConfig().getString("options.island.allowPvP");
/*  385: 400 */     Settings.island_schematicName = getConfig().getString("options.island.schematicName");
/*  386: 401 */     if (!Settings.island_allowPvP.equalsIgnoreCase("allow")) {
/*  387: 402 */       Settings.island_allowPvP = "deny";
/*  388:     */     }
/*  389: 403 */     Set<String> permissionList = getConfig().getConfigurationSection("options.island.extraPermissions").getKeys(true);
/*  390: 404 */     Settings.island_addExtraItems = getConfig().getBoolean("options.island.addExtraItems");
/*  391: 405 */     Settings.extras_obsidianToLava = getConfig().getBoolean("options.extras.obsidianToLava");
/*  392: 406 */     Settings.island_useIslandLevel = getConfig().getBoolean("options.island.useIslandLevel");
/*  393: 407 */     Settings.island_extraPermissions = (String[])permissionList.toArray(new String[0]);
/*  394: 408 */     Settings.island_protectWithWorldGuard = getConfig().getBoolean("options.island.protectWithWorldGuard");
/*  395: 409 */     Settings.extras_sendToSpawn = getConfig().getBoolean("options.extras.sendToSpawn");
/*  396: 410 */     Settings.island_useTopTen = getConfig().getBoolean("options.island.useTopTen");
/*  397:     */     
/*  398: 412 */     Settings.general_worldName = getConfig().getString("options.general.worldName");
/*  399: 413 */     Settings.island_removeCreaturesByTeleport = getConfig().getBoolean("options.island.removeCreaturesByTeleport");
/*  400: 414 */     Settings.island_allowIslandLock = getConfig().getBoolean("options.island.allowIslandLock");
/*  401: 415 */     Settings.island_useOldIslands = getConfig().getBoolean("options.island.useOldIslands");
/*  402:     */     
/*  403: 417 */     Set<String> challengeList = getConfig().getConfigurationSection("options.challenges.challengeList").getKeys(false);
/*  404: 418 */     Settings.challenges_challengeList = challengeList;
/*  405: 419 */     Settings.challenges_broadcastCompletion = getConfig().getBoolean("options.challenges.broadcastCompletion");
/*  406: 420 */     Settings.challenges_broadcastText = getConfig().getString("options.challenges.broadcastText");
/*  407: 421 */     Settings.challenges_challengeColor = getConfig().getString("options.challenges.challengeColor");
/*  408: 422 */     Settings.challenges_enableEconomyPlugin = getConfig().getBoolean("options.challenges.enableEconomyPlugin");
/*  409: 423 */     Settings.challenges_finishedColor = getConfig().getString("options.challenges.finishedColor");
/*  410: 424 */     Settings.challenges_repeatableColor = getConfig().getString("options.challenges.repeatableColor");
/*  411: 425 */     Settings.challenges_requirePreviousRank = getConfig().getBoolean("options.challenges.requirePreviousRank");
/*  412: 426 */     Settings.challenges_allowChallenges = getConfig().getBoolean("options.challenges.allowChallenges");
/*  413: 427 */     String[] rankListString = getConfig().getString("options.challenges.ranks").split(" ");
/*  414: 428 */     Settings.challenges_ranks = rankListString;
/*  415:     */   }
/*  416:     */   
/*  417:     */   public List<Party> readPartyFile()
/*  418:     */   {
/*  419: 433 */     File f = new File(getDataFolder(), "partylist.bin");
/*  420: 434 */     if (!f.exists()) {
/*  421: 435 */       return null;
/*  422:     */     }
/*  423:     */     try
/*  424:     */     {
/*  425: 439 */       FileInputStream fileIn = new FileInputStream(f);
/*  426: 440 */       ObjectInputStream in = new ObjectInputStream(fileIn);
/*  427:     */       
/*  428: 442 */       List<Party> p = (List)in.readObject();
/*  429: 443 */       in.close();
/*  430: 444 */       fileIn.close();
/*  431: 445 */       return p;
/*  432:     */     }
/*  433:     */     catch (Exception e)
/*  434:     */     {
/*  435: 447 */       e.printStackTrace();
/*  436:     */     }
/*  437: 449 */     return null;
/*  438:     */   }
/*  439:     */   
/*  440:     */   public void writePartyFile(List<Party> pi)
/*  441:     */   {
/*  442: 454 */     File f = new File(getDataFolder(), "partylist.bin");
/*  443:     */     try
/*  444:     */     {
/*  445: 457 */       FileOutputStream fileOut = new FileOutputStream(f);
/*  446: 458 */       ObjectOutputStream out = new ObjectOutputStream(fileOut);
/*  447: 459 */       out.writeObject(pi);
/*  448: 460 */       out.flush();
/*  449: 461 */       out.close();
/*  450: 462 */       fileOut.close();
/*  451:     */     }
/*  452:     */     catch (Exception e)
/*  453:     */     {
/*  454: 464 */       e.printStackTrace();
/*  455:     */     }
/*  456:     */   }
/*  457:     */   
/*  458:     */   public PlayerInfo readPlayerFile(String playerName)
/*  459:     */   {
/*  460: 470 */     File f = new File(this.directoryPlayers, playerName);
/*  461: 471 */     if (!f.exists()) {
/*  462: 472 */       return null;
/*  463:     */     }
/*  464:     */     try
/*  465:     */     {
/*  466: 476 */       FileInputStream fileIn = new FileInputStream(f);
/*  467: 477 */       ObjectInputStream in = new ObjectInputStream(fileIn);
/*  468: 478 */       PlayerInfo p = (PlayerInfo)in.readObject();
/*  469: 479 */       in.close();
/*  470: 480 */       fileIn.close();
/*  471: 481 */       return p;
/*  472:     */     }
/*  473:     */     catch (Exception e)
/*  474:     */     {
/*  475: 483 */       e.printStackTrace();
/*  476:     */     }
/*  477: 485 */     return null;
/*  478:     */   }
/*  479:     */   
/*  480:     */   public void writePlayerFile(String playerName, PlayerInfo pi)
/*  481:     */   {
/*  482: 490 */     File f = new File(this.directoryPlayers, playerName);
/*  483:     */     try
/*  484:     */     {
/*  485: 493 */       FileOutputStream fileOut = new FileOutputStream(f);
/*  486: 494 */       ObjectOutputStream out = new ObjectOutputStream(fileOut);
/*  487: 495 */       out.writeObject(pi);
/*  488: 496 */       out.flush();
/*  489: 497 */       out.close();
/*  490: 498 */       fileOut.close();
/*  491:     */     }
/*  492:     */     catch (Exception e)
/*  493:     */     {
/*  494: 500 */       e.printStackTrace();
/*  495:     */     }
/*  496:     */   }
/*  497:     */   
/*  498:     */   public boolean displayTopTen(Player player)
/*  499:     */   {
/*  500: 506 */     int i = 1;
/*  501: 507 */     int playerrank = 0;
/*  502: 508 */     player.sendMessage(ChatColor.YELLOW + "Displaying the top 10 islands:");
/*  503: 509 */     if (this.topTen == null)
/*  504:     */     {
/*  505: 511 */       player.sendMessage(ChatColor.RED + "Top ten list not generated yet!");
/*  506: 512 */       return false;
/*  507:     */     }
/*  508: 517 */     PlayerInfo pi2 = (PlayerInfo)getActivePlayers().get(player.getName());
/*  509: 518 */     for (String playerName : this.topTen.keySet())
/*  510:     */     {
/*  511: 520 */       if (i <= 10) {
/*  512: 523 */         if (hasParty(playerName))
/*  513:     */         {
/*  514: 525 */           PlayerInfo pix = readPlayerFile(playerName);
/*  515: 526 */           List<String> pMembers = pix.getMembers();
/*  516: 527 */           if (pMembers.contains(playerName)) {
/*  517: 528 */             pMembers.remove(playerName);
/*  518:     */           }
/*  519: 529 */           player.sendMessage(ChatColor.GREEN + "#" + i + ": " + playerName + pMembers.toString() + " - Island level " + ((Double)this.topTen.get(playerName)).intValue());
/*  520:     */         }
/*  521:     */         else
/*  522:     */         {
/*  523: 532 */           player.sendMessage(ChatColor.GREEN + "#" + i + ": " + playerName + " - Island level " + ((Double)this.topTen.get(playerName)).intValue());
/*  524:     */         }
/*  525:     */       }
/*  526: 534 */       if (playerName.equalsIgnoreCase(player.getName())) {
/*  527: 536 */         playerrank = i;
/*  528:     */       }
/*  529: 538 */       if (pi2.getHasParty()) {
/*  530: 540 */         if (playerName.equalsIgnoreCase(pi2.getPartyLeader())) {
/*  531: 541 */           playerrank = i;
/*  532:     */         }
/*  533:     */       }
/*  534: 543 */       i++;
/*  535:     */     }
/*  536: 545 */     player.sendMessage(ChatColor.YELLOW + "Your rank is: " + ChatColor.WHITE + playerrank);
/*  537: 546 */     return true;
/*  538:     */   }
/*  539:     */   
/*  540:     */   public void updateTopTen(LinkedHashMap<String, Double> map)
/*  541:     */   {
/*  542: 551 */     this.topTen = map;
/*  543:     */   }
/*  544:     */   
/*  545:     */   public Location getLocationString(String s)
/*  546:     */   {
/*  547: 561 */     if ((s == null) || (s.trim() == "")) {
/*  548: 562 */       return null;
/*  549:     */     }
/*  550: 564 */     String[] parts = s.split(":");
/*  551: 565 */     if (parts.length == 4)
/*  552:     */     {
/*  553: 566 */       World w = getServer().getWorld(parts[0]);
/*  554: 567 */       int x = Integer.parseInt(parts[1]);
/*  555: 568 */       int y = Integer.parseInt(parts[2]);
/*  556: 569 */       int z = Integer.parseInt(parts[3]);
/*  557: 570 */       return new Location(w, x, y, z);
/*  558:     */     }
/*  559: 572 */     return null;
/*  560:     */   }
/*  561:     */   
/*  562:     */   public String getStringLocation(Location l)
/*  563:     */   {
/*  564: 582 */     if (l == null) {
/*  565: 583 */       return "";
/*  566:     */     }
/*  567: 585 */     return l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
/*  568:     */   }
/*  569:     */   
/*  570:     */   public void setStringbyPath(FileConfiguration fc, File f, String path, Object value)
/*  571:     */   {
/*  572: 598 */     fc.set(path, value.toString());
/*  573:     */     try
/*  574:     */     {
/*  575: 600 */       fc.save(f);
/*  576:     */     }
/*  577:     */     catch (IOException e)
/*  578:     */     {
/*  579: 602 */       e.printStackTrace();
/*  580:     */     }
/*  581:     */   }
/*  582:     */   
/*  583:     */   public String getStringbyPath(FileConfiguration fc, File file, String path, Object stdValue, boolean addMissing)
/*  584:     */   {
/*  585: 616 */     if (!fc.contains(path))
/*  586:     */     {
/*  587: 617 */       if (addMissing) {
/*  588: 618 */         setStringbyPath(fc, file, path, stdValue);
/*  589:     */       }
/*  590: 620 */       return stdValue.toString();
/*  591:     */     }
/*  592: 622 */     return fc.getString(path);
/*  593:     */   }
/*  594:     */   
/*  595:     */   public static World getSkyBlockWorld()
/*  596:     */   {
/*  597: 631 */     if (skyBlockWorld == null)
/*  598:     */     {
/*  599: 632 */       skyBlockWorld = WorldCreator.name(Settings.general_worldName).type(WorldType.FLAT).environment(World.Environment.NORMAL).generator(new SkyBlockChunkGenerator()).createWorld();
/*  600: 633 */       if (Bukkit.getServer().getPluginManager().isPluginEnabled("Multiverse-Core")) {
/*  601: 635 */         Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mv import " + Settings.general_worldName + " normal -g uSkyBlock");
/*  602:     */       }
/*  603:     */     }
/*  604: 639 */     return skyBlockWorld;
/*  605:     */   }
/*  606:     */   
/*  607:     */   public void clearOrphanedIsland()
/*  608:     */   {
/*  609: 643 */     while (hasOrphanedIsland()) {
/*  610: 644 */       this.orphaned.pop();
/*  611:     */     }
/*  612:     */   }
/*  613:     */   
/*  614:     */   public void clearArmorContents(Player player)
/*  615:     */   {
/*  616: 653 */     player.getInventory().setArmorContents(new ItemStack[player.getInventory().getArmorContents().length]);
/*  617:     */   }
/*  618:     */   
/*  619:     */   public void getAllFiles(String path)
/*  620:     */   {
/*  621: 665 */     File dirpath = new File(path);
/*  622: 666 */     if (!dirpath.exists()) {
/*  623: 667 */       return;
/*  624:     */     }
/*  625: 670 */     for (File f : dirpath.listFiles()) {
/*  626:     */       try
/*  627:     */       {
/*  628: 672 */         if (!f.isDirectory()) {
/*  629: 673 */           this.sfiles.add(f);
/*  630:     */         } else {
/*  631: 675 */           getAllFiles(f.getAbsolutePath());
/*  632:     */         }
/*  633:     */       }
/*  634:     */       catch (Exception ex)
/*  635:     */       {
/*  636: 678 */         this.log.warning(ex.getMessage());
/*  637:     */       }
/*  638:     */     }
/*  639:     */   }
/*  640:     */   
/*  641:     */   public Location getYLocation(Location l)
/*  642:     */   {
/*  643: 690 */     for (int y = 0; y < 254; y++)
/*  644:     */     {
/*  645: 691 */       int px = l.getBlockX();
/*  646: 692 */       int py = y;
/*  647: 693 */       int pz = l.getBlockZ();
/*  648: 694 */       Block b1 = new Location(l.getWorld(), px, py, pz).getBlock();
/*  649: 695 */       Block b2 = new Location(l.getWorld(), px, py + 1, pz).getBlock();
/*  650: 696 */       Block b3 = new Location(l.getWorld(), px, py + 2, pz).getBlock();
/*  651: 697 */       if ((!b1.getType().equals(Material.AIR)) && (b2.getType().equals(Material.AIR)) && (b3.getType().equals(Material.AIR))) {
/*  652: 698 */         return b2.getLocation();
/*  653:     */       }
/*  654:     */     }
/*  655: 701 */     return l;
/*  656:     */   }
/*  657:     */   
/*  658:     */   public Location getSafeHomeLocation(PlayerInfo p)
/*  659:     */   {
/*  660: 706 */     Location home = null;
/*  661: 707 */     if (p.getHomeLocation() == null)
/*  662:     */     {
/*  663: 708 */       if ((p.getIslandLocation() == null) && (p.getHasParty())) {
/*  664: 710 */         home = p.getPartyIslandLocation();
/*  665: 711 */       } else if (p.getIslandLocation() != null) {
/*  666: 712 */         home = p.getIslandLocation();
/*  667:     */       }
/*  668:     */     }
/*  669:     */     else {
/*  670: 714 */       home = p.getHomeLocation();
/*  671:     */     }
/*  672: 717 */     if (isSafeLocation(home)) {
/*  673: 718 */       return home;
/*  674:     */     }
/*  675: 721 */     for (int y = home.getBlockY() + 25; y > 0; y--)
/*  676:     */     {
/*  677: 722 */       Location n = new Location(home.getWorld(), home.getBlockX(), y, home.getBlockZ());
/*  678: 723 */       if (isSafeLocation(n)) {
/*  679: 724 */         return n;
/*  680:     */       }
/*  681:     */     }
/*  682: 727 */     for (int y = home.getBlockY(); y < 255; y++)
/*  683:     */     {
/*  684: 728 */       Location n = new Location(home.getWorld(), home.getBlockX(), y, home.getBlockZ());
/*  685: 729 */       if (isSafeLocation(n)) {
/*  686: 730 */         return n;
/*  687:     */       }
/*  688:     */     }
/*  689: 733 */     if ((p.getHasParty()) && (!p.getPartyLeader().equalsIgnoreCase(p.getPlayer().getName()))) {
/*  690: 734 */       return p.getPartyIslandLocation();
/*  691:     */     }
/*  692: 736 */     Location island = p.getIslandLocation();
/*  693: 737 */     if (isSafeLocation(island)) {
/*  694: 738 */       return island;
/*  695:     */     }
/*  696: 741 */     for (int y = island.getBlockY() + 25; y > 0; y--)
/*  697:     */     {
/*  698: 742 */       Location n = new Location(island.getWorld(), island.getBlockX(), y, island.getBlockZ());
/*  699: 743 */       if (isSafeLocation(n)) {
/*  700: 744 */         return n;
/*  701:     */       }
/*  702:     */     }
/*  703: 747 */     for (int y = island.getBlockY(); y < 255; y++)
/*  704:     */     {
/*  705: 748 */       Location n = new Location(island.getWorld(), island.getBlockX(), y, island.getBlockZ());
/*  706: 749 */       if (isSafeLocation(n)) {
/*  707: 750 */         return n;
/*  708:     */       }
/*  709:     */     }
/*  710: 753 */     if ((p.getHasParty()) && (!p.getPartyLeader().equalsIgnoreCase(p.getPlayer().getName()))) {
/*  711: 754 */       return p.getPartyIslandLocation();
/*  712:     */     }
/*  713: 756 */     return p.getHomeLocation();
/*  714:     */   }
/*  715:     */   
/*  716:     */   public Location getSafeWarpLocation(PlayerInfo p)
/*  717:     */   {
/*  718: 761 */     Location warp = null;
/*  719: 762 */     if (p.getWarpLocation() == null)
/*  720:     */     {
/*  721: 763 */       if (p.getHomeLocation() == null)
/*  722:     */       {
/*  723: 765 */         if (p.getIslandLocation() != null) {
/*  724: 766 */           warp = p.getIslandLocation();
/*  725:     */         }
/*  726:     */       }
/*  727:     */       else {
/*  728: 768 */         warp = p.getHomeLocation();
/*  729:     */       }
/*  730:     */     }
/*  731:     */     else {
/*  732: 771 */       warp = p.getWarpLocation();
/*  733:     */     }
/*  734: 774 */     if (warp == null)
/*  735:     */     {
/*  736: 776 */       System.out.print("Error warping player to " + p.getPlayerName() + "'s island.");
/*  737: 777 */       return null;
/*  738:     */     }
/*  739: 780 */     if (isSafeLocation(warp)) {
/*  740: 781 */       return warp;
/*  741:     */     }
/*  742: 784 */     for (int y = warp.getBlockY() + 25; y > 0; y--)
/*  743:     */     {
/*  744: 785 */       Location n = new Location(warp.getWorld(), warp.getBlockX(), y, warp.getBlockZ());
/*  745: 786 */       if (isSafeLocation(n)) {
/*  746: 787 */         return n;
/*  747:     */       }
/*  748:     */     }
/*  749: 790 */     for (int y = warp.getBlockY(); y < 255; y++)
/*  750:     */     {
/*  751: 791 */       Location n = new Location(warp.getWorld(), warp.getBlockX(), y, warp.getBlockZ());
/*  752: 792 */       if (isSafeLocation(n)) {
/*  753: 793 */         return n;
/*  754:     */       }
/*  755:     */     }
/*  756: 796 */     return null;
/*  757:     */   }
/*  758:     */   
/*  759:     */   public boolean isSafeLocation(Location l)
/*  760:     */   {
/*  761: 800 */     if (l == null) {
/*  762: 801 */       return false;
/*  763:     */     }
/*  764: 804 */     Block ground = l.getBlock().getRelative(BlockFace.DOWN);
/*  765: 805 */     Block air1 = l.getBlock();
/*  766: 806 */     Block air2 = l.getBlock().getRelative(BlockFace.UP);
/*  767: 807 */     if (ground.getType().equals(Material.AIR)) {
/*  768: 808 */       return false;
/*  769:     */     }
/*  770: 809 */     if (ground.getType().equals(Material.LAVA)) {
/*  771: 810 */       return false;
/*  772:     */     }
/*  773: 811 */     if (ground.getType().equals(Material.STATIONARY_LAVA)) {
/*  774: 812 */       return false;
/*  775:     */     }
/*  776: 813 */     if (ground.getType().equals(Material.CACTUS)) {
/*  777: 814 */       return false;
/*  778:     */     }
/*  779: 815 */     if (((air1.getType().equals(Material.AIR)) || (air1.getType().equals(Material.CROPS)) || (air1.getType().equals(Material.LONG_GRASS)) || (air1.getType().equals(Material.RED_ROSE)) || (air1.getType().equals(Material.YELLOW_FLOWER)) || (air1.getType().equals(Material.DEAD_BUSH)) || (air1.getType().equals(Material.SIGN_POST)) || (air1.getType().equals(Material.SIGN))) && (air2.getType().equals(Material.AIR))) {
/*  780: 816 */       return true;
/*  781:     */     }
/*  782: 817 */     return false;
/*  783:     */   }
/*  784:     */   
/*  785:     */   public void removeCreatures(Location l)
/*  786:     */   {
/*  787: 826 */     if ((!Settings.island_removeCreaturesByTeleport) || (l == null)) {
/*  788: 827 */       return;
/*  789:     */     }
/*  790: 830 */     int px = l.getBlockX();
/*  791: 831 */     int py = l.getBlockY();
/*  792: 832 */     int pz = l.getBlockZ();
/*  793: 833 */     for (int x = -1; x <= 1; x++) {
/*  794: 834 */       for (int z = -1; z <= 1; z++)
/*  795:     */       {
/*  796: 835 */         Chunk c = l.getWorld().getChunkAt(new Location(l.getWorld(), px + x * 16, py, pz + z * 16));
/*  797: 836 */         for (Entity e : c.getEntities()) {
/*  798: 837 */           if ((e.getType() == EntityType.SPIDER) || (e.getType() == EntityType.CREEPER) || (e.getType() == EntityType.ENDERMAN) || (e.getType() == EntityType.SKELETON) || (e.getType() == EntityType.ZOMBIE)) {
/*  799: 838 */             e.remove();
/*  800:     */           }
/*  801:     */         }
/*  802:     */       }
/*  803:     */     }
/*  804:     */   }
/*  805:     */   
/*  806:     */   public void deletePlayerIsland(String player)
/*  807:     */   {
/*  808: 846 */     if (!getActivePlayers().containsKey(player))
/*  809:     */     {
/*  810: 848 */       PlayerInfo pi = readPlayerFile(player);
/*  811: 849 */       if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/*  812: 851 */         if (WorldGuardHandler.getWorldGuard().getRegionManager(getSkyBlockWorld()).hasRegion(player + "Island")) {
/*  813: 852 */           WorldGuardHandler.getWorldGuard().getRegionManager(getSkyBlockWorld()).removeRegion(player + "Island");
/*  814:     */         }
/*  815:     */       }
/*  816: 854 */       this.orphaned.push(pi.getIslandLocation());
/*  817: 855 */       removeIsland(pi.getIslandLocation());
/*  818: 856 */       pi.removeFromIsland();
/*  819: 857 */       saveOrphans();
/*  820: 858 */       updateOrphans();
/*  821: 859 */       writePlayerFile(player, pi);
/*  822:     */     }
/*  823:     */     else
/*  824:     */     {
/*  825: 862 */       if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/*  826: 864 */         if (WorldGuardHandler.getWorldGuard().getRegionManager(getSkyBlockWorld()).hasRegion(player + "Island")) {
/*  827: 865 */           WorldGuardHandler.getWorldGuard().getRegionManager(getSkyBlockWorld()).removeRegion(player + "Island");
/*  828:     */         }
/*  829:     */       }
/*  830: 867 */       this.orphaned.push(((PlayerInfo)getActivePlayers().get(player)).getIslandLocation());
/*  831: 868 */       removeIsland(((PlayerInfo)getActivePlayers().get(player)).getIslandLocation());
/*  832: 869 */       PlayerInfo pi = readPlayerFile(player);
/*  833: 870 */       pi.removeFromIsland();
/*  834:     */       
/*  835: 872 */       addActivePlayer(player, pi);
/*  836: 873 */       saveOrphans();
/*  837: 874 */       updateOrphans();
/*  838:     */     }
/*  839:     */   }
/*  840:     */   
/*  841:     */   public void devDeletePlayerIsland(String player)
/*  842:     */   {
/*  843: 881 */     if (!getActivePlayers().containsKey(player))
/*  844:     */     {
/*  845: 883 */       PlayerInfo pi = readPlayerFile(player);
/*  846: 884 */       if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/*  847: 886 */         if (WorldGuardHandler.getWorldGuard().getRegionManager(getSkyBlockWorld()).hasRegion(player + "Island")) {
/*  848: 887 */           WorldGuardHandler.getWorldGuard().getRegionManager(getSkyBlockWorld()).removeRegion(player + "Island");
/*  849:     */         }
/*  850:     */       }
/*  851: 890 */       pi = new PlayerInfo(player);
/*  852: 891 */       writePlayerFile(player, pi);
/*  853:     */     }
/*  854:     */     else
/*  855:     */     {
/*  856: 894 */       if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/*  857: 896 */         if (WorldGuardHandler.getWorldGuard().getRegionManager(getSkyBlockWorld()).hasRegion(player + "Island")) {
/*  858: 897 */           WorldGuardHandler.getWorldGuard().getRegionManager(getSkyBlockWorld()).removeRegion(player + "Island");
/*  859:     */         }
/*  860:     */       }
/*  861: 899 */       PlayerInfo pi = new PlayerInfo(player);
/*  862: 900 */       removeActivePlayer(player);
/*  863: 901 */       addActivePlayer(player, pi);
/*  864:     */     }
/*  865:     */   }
/*  866:     */   
/*  867:     */   public boolean devSetPlayerIsland(CommandSender sender, Location l, String player)
/*  868:     */   {
/*  869: 907 */     if (!getActivePlayers().containsKey(player))
/*  870:     */     {
/*  871: 909 */       PlayerInfo pi = readPlayerFile(player);
/*  872: 910 */       int px = l.getBlockX();
/*  873: 911 */       int py = l.getBlockY();
/*  874: 912 */       int pz = l.getBlockZ();
/*  875: 913 */       for (int x = -10; x <= 10; x++) {
/*  876: 914 */         for (int y = -10; y <= 10; y++) {
/*  877: 915 */           for (int z = -10; z <= 10; z++)
/*  878:     */           {
/*  879: 916 */             Block b = new Location(l.getWorld(), px + x, py + y, pz + z).getBlock();
/*  880: 917 */             if (b.getTypeId() == 7)
/*  881:     */             {
/*  882: 919 */               pi.setHomeLocation(new Location(l.getWorld(), px + x, py + y + 3, pz + z));
/*  883: 920 */               pi.setHasIsland(true);
/*  884: 921 */               pi.setIslandLocation(b.getLocation());
/*  885: 922 */               writePlayerFile(player, pi);
/*  886: 923 */               if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/*  887: 924 */                 WorldGuardHandler.protectIsland(sender, player);
/*  888:     */               }
/*  889: 925 */               return true;
/*  890:     */             }
/*  891:     */           }
/*  892:     */         }
/*  893:     */       }
/*  894:     */     }
/*  895:     */     else
/*  896:     */     {
/*  897: 932 */       int px = l.getBlockX();
/*  898: 933 */       int py = l.getBlockY();
/*  899: 934 */       int pz = l.getBlockZ();
/*  900: 935 */       for (int x = -10; x <= 10; x++) {
/*  901: 936 */         for (int y = -10; y <= 10; y++) {
/*  902: 937 */           for (int z = -10; z <= 10; z++)
/*  903:     */           {
/*  904: 938 */             Block b = new Location(l.getWorld(), px + x, py + y, pz + z).getBlock();
/*  905: 939 */             if (b.getTypeId() == 7)
/*  906:     */             {
/*  907: 941 */               ((PlayerInfo)getActivePlayers().get(player)).setHomeLocation(new Location(l.getWorld(), px + x, py + y + 3, pz + z));
/*  908: 942 */               ((PlayerInfo)getActivePlayers().get(player)).setHasIsland(true);
/*  909: 943 */               ((PlayerInfo)getActivePlayers().get(player)).setIslandLocation(b.getLocation());
/*  910: 944 */               PlayerInfo pi = (PlayerInfo)getActivePlayers().get(player);
/*  911: 945 */               removeActivePlayer(player);
/*  912: 946 */               addActivePlayer(player, pi);
/*  913: 947 */               if ((Settings.island_protectWithWorldGuard) && (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard"))) {
/*  914: 948 */                 WorldGuardHandler.protectIsland(sender, player);
/*  915:     */               }
/*  916: 949 */               return true;
/*  917:     */             }
/*  918:     */           }
/*  919:     */         }
/*  920:     */       }
/*  921:     */     }
/*  922: 955 */     return false;
/*  923:     */   }
/*  924:     */   
/*  925:     */   public int orphanCount()
/*  926:     */   {
/*  927: 960 */     return this.orphaned.size();
/*  928:     */   }
/*  929:     */   
/*  930:     */   public void removeIsland(Location loc)
/*  931:     */   {
/*  932: 969 */     if (loc != null)
/*  933:     */     {
/*  934: 970 */       Location l = loc;
/*  935: 971 */       int px = l.getBlockX();
/*  936: 972 */       int py = l.getBlockY();
/*  937: 973 */       int pz = l.getBlockZ();
/*  938: 974 */       for (int x = Settings.island_protectionRange / 2 * -1; x <= Settings.island_protectionRange / 2; x++) {
/*  939: 975 */         for (int y = 0; y <= 255; y++) {
/*  940: 976 */           for (int z = Settings.island_protectionRange / 2 * -1; z <= Settings.island_protectionRange / 2; z++)
/*  941:     */           {
/*  942: 977 */             Block b = new Location(l.getWorld(), px + x, py + y, pz + z).getBlock();
/*  943: 978 */             if (!b.getType().equals(Material.AIR))
/*  944:     */             {
/*  945: 979 */               if (b.getType().equals(Material.CHEST))
/*  946:     */               {
/*  947: 980 */                 Chest c = (Chest)b.getState();
/*  948: 981 */                 ItemStack[] items = new ItemStack[c.getInventory().getContents().length];
/*  949: 982 */                 c.getInventory().setContents(items);
/*  950:     */               }
/*  951: 983 */               else if (b.getType().equals(Material.FURNACE))
/*  952:     */               {
/*  953: 984 */                 Furnace f = (Furnace)b.getState();
/*  954: 985 */                 ItemStack[] items = new ItemStack[f.getInventory().getContents().length];
/*  955: 986 */                 f.getInventory().setContents(items);
/*  956:     */               }
/*  957: 987 */               else if (b.getType().equals(Material.DISPENSER))
/*  958:     */               {
/*  959: 988 */                 Dispenser d = (Dispenser)b.getState();
/*  960: 989 */                 ItemStack[] items = new ItemStack[d.getInventory().getContents().length];
/*  961: 990 */                 d.getInventory().setContents(items);
/*  962:     */               }
/*  963: 992 */               b.setType(Material.AIR);
/*  964:     */             }
/*  965:     */           }
/*  966:     */         }
/*  967:     */       }
/*  968:     */     }
/*  969:     */   }
/*  970:     */   
/*  971:     */   public void removeIslandBlocks(Location loc)
/*  972:     */   {
/*  973:1001 */     if (loc != null)
/*  974:     */     {
/*  975:1002 */       System.out.print("Removing blocks from an abandoned island.");
/*  976:1003 */       Location l = loc;
/*  977:1004 */       int px = l.getBlockX();
/*  978:1005 */       int py = l.getBlockY();
/*  979:1006 */       int pz = l.getBlockZ();
/*  980:1007 */       for (int x = -20; x <= 20; x++) {
/*  981:1008 */         for (int y = -20; y <= 20; y++) {
/*  982:1009 */           for (int z = -20; z <= 20; z++)
/*  983:     */           {
/*  984:1010 */             Block b = new Location(l.getWorld(), px + x, py + y, pz + z).getBlock();
/*  985:1011 */             if (!b.getType().equals(Material.AIR))
/*  986:     */             {
/*  987:1012 */               if (b.getType().equals(Material.CHEST))
/*  988:     */               {
/*  989:1013 */                 Chest c = (Chest)b.getState();
/*  990:1014 */                 ItemStack[] items = new ItemStack[c.getInventory().getContents().length];
/*  991:1015 */                 c.getInventory().setContents(items);
/*  992:     */               }
/*  993:1016 */               else if (b.getType().equals(Material.FURNACE))
/*  994:     */               {
/*  995:1017 */                 Furnace f = (Furnace)b.getState();
/*  996:1018 */                 ItemStack[] items = new ItemStack[f.getInventory().getContents().length];
/*  997:1019 */                 f.getInventory().setContents(items);
/*  998:     */               }
/*  999:1020 */               else if (b.getType().equals(Material.DISPENSER))
/* 1000:     */               {
/* 1001:1021 */                 Dispenser d = (Dispenser)b.getState();
/* 1002:1022 */                 ItemStack[] items = new ItemStack[d.getInventory().getContents().length];
/* 1003:1023 */                 d.getInventory().setContents(items);
/* 1004:     */               }
/* 1005:1025 */               b.setType(Material.AIR);
/* 1006:     */             }
/* 1007:     */           }
/* 1008:     */         }
/* 1009:     */       }
/* 1010:     */     }
/* 1011:     */   }
/* 1012:     */   
/* 1013:     */   public boolean hasParty(String playername)
/* 1014:     */   {
/* 1015:1049 */     if (getActivePlayers().containsKey(playername)) {
/* 1016:1051 */       return ((PlayerInfo)getActivePlayers().get(playername)).getHasParty();
/* 1017:     */     }
/* 1018:1054 */     PlayerInfo pi = getInstance().readPlayerFile(playername);
/* 1019:1055 */     if (pi == null) {
/* 1020:1056 */       return false;
/* 1021:     */     }
/* 1022:1057 */     return pi.getHasParty();
/* 1023:     */   }
/* 1024:     */   
/* 1025:     */   public Location getLastIsland()
/* 1026:     */   {
/* 1027:1073 */     if (this.lastIsland.getWorld().getName().equalsIgnoreCase(Settings.general_worldName)) {
/* 1028:1074 */       return this.lastIsland;
/* 1029:     */     }
/* 1030:1077 */     setLastIsland(new Location(getSkyBlockWorld(), 0.0D, Settings.island_height, 0.0D));
/* 1031:1078 */     return new Location(getSkyBlockWorld(), 0.0D, Settings.island_height, 0.0D);
/* 1032:     */   }
/* 1033:     */   
/* 1034:     */   public void setLastIsland(Location island)
/* 1035:     */   {
/* 1036:1084 */     getConfig();FileConfiguration.createPath(getConfig().getConfigurationSection("options.general"), "lastIslandX");
/* 1037:1085 */     getConfig();FileConfiguration.createPath(getConfig().getConfigurationSection("options.general"), "lastIslandZ");
/* 1038:1086 */     getConfig().set("options.general.lastIslandX", Integer.valueOf(island.getBlockX()));
/* 1039:1087 */     getConfig().set("options.general.lastIslandZ", Integer.valueOf(island.getBlockZ()));
/* 1040:1088 */     saveConfig();
/* 1041:1089 */     this.lastIsland = island;
/* 1042:     */   }
/* 1043:     */   
/* 1044:     */   public boolean hasOrphanedIsland()
/* 1045:     */   {
/* 1046:1098 */     return !this.orphaned.empty();
/* 1047:     */   }
/* 1048:     */   
/* 1049:     */   public Location checkOrphan()
/* 1050:     */   {
/* 1051:1103 */     return (Location)this.orphaned.peek();
/* 1052:     */   }
/* 1053:     */   
/* 1054:     */   public Location getOrphanedIsland()
/* 1055:     */   {
/* 1056:1107 */     if (hasOrphanedIsland()) {
/* 1057:1108 */       return (Location)this.orphaned.pop();
/* 1058:     */     }
/* 1059:1111 */     return null;
/* 1060:     */   }
/* 1061:     */   
/* 1062:     */   public void addOrphan(Location island)
/* 1063:     */   {
/* 1064:1116 */     this.orphaned.push(island);
/* 1065:     */   }
/* 1066:     */   
/* 1067:     */   public void removeNextOrphan()
/* 1068:     */   {
/* 1069:1121 */     this.orphaned.pop();
/* 1070:     */   }
/* 1071:     */   
/* 1072:     */   public void saveOrphans()
/* 1073:     */   {
/* 1074:     */     try
/* 1075:     */     {
/* 1076:1127 */       File f = new File(getDataFolder(), "orphanedIslands.bin");
/* 1077:1128 */       SLAPI.save(changeStackToFile(this.orphaned), f);
/* 1078:     */     }
/* 1079:     */     catch (Exception e)
/* 1080:     */     {
/* 1081:1130 */       System.out.print("Error saving orphan file!");
/* 1082:     */     }
/* 1083:     */   }
/* 1084:     */   
/* 1085:     */   public void updateOrphans()
/* 1086:     */   {
/* 1087:     */     try
/* 1088:     */     {
/* 1089:1137 */       File f = new File(getDataFolder(), "orphanedIslands.bin");
/* 1090:     */       
/* 1091:1139 */       Stack<SerializableLocation> load = (Stack)SLAPI.load(f);
/* 1092:1140 */       if (load != null) {
/* 1093:1141 */         this.orphaned = changestackfromfile(load);
/* 1094:     */       }
/* 1095:     */     }
/* 1096:     */     catch (Exception e)
/* 1097:     */     {
/* 1098:1143 */       System.out.print("Error saving orphan file!");
/* 1099:     */     }
/* 1100:     */   }
/* 1101:     */   
/* 1102:     */   public boolean homeTeleport(Player player)
/* 1103:     */   {
/* 1104:1148 */     Location homeSweetHome = null;
/* 1105:1149 */     if (getActivePlayers().containsKey(player.getName())) {
/* 1106:1151 */       homeSweetHome = getInstance().getSafeHomeLocation((PlayerInfo)getActivePlayers().get(player.getName()));
/* 1107:     */     }
/* 1108:1155 */     if (homeSweetHome == null)
/* 1109:     */     {
/* 1110:1156 */       player.performCommand("spawn");
/* 1111:1157 */       player.sendMessage(ChatColor.RED + "You are not part of an island. Returning you the spawn area!");
/* 1112:1158 */       return true;
/* 1113:     */     }
/* 1114:1161 */     getInstance().removeCreatures(homeSweetHome);
/* 1115:1162 */     player.teleport(homeSweetHome);
/* 1116:1163 */     player.sendMessage(ChatColor.GREEN + "Teleporting you to your island. (/island help for more info)");
/* 1117:1164 */     return true;
/* 1118:     */   }
/* 1119:     */   
/* 1120:     */   public boolean warpTeleport(Player player, PlayerInfo pi)
/* 1121:     */   {
/* 1122:1168 */     Location warpSweetWarp = null;
/* 1123:1169 */     if (pi == null)
/* 1124:     */     {
/* 1125:1171 */       player.sendMessage(ChatColor.RED + "That player does not exist!");
/* 1126:1172 */       return true;
/* 1127:     */     }
/* 1128:1174 */     warpSweetWarp = getInstance().getSafeWarpLocation(pi);
/* 1129:1178 */     if (warpSweetWarp == null)
/* 1130:     */     {
/* 1131:1179 */       player.sendMessage(ChatColor.RED + "Unable to warp you to that player's island!");
/* 1132:1180 */       return true;
/* 1133:     */     }
/* 1134:1184 */     player.teleport(warpSweetWarp);
/* 1135:1185 */     player.sendMessage(ChatColor.GREEN + "Teleporting you to " + pi.getPlayerName() + "'s island.");
/* 1136:1186 */     return true;
/* 1137:     */   }
/* 1138:     */   
/* 1139:     */   public boolean homeSet(Player player)
/* 1140:     */   {
/* 1141:1196 */     if (!player.getWorld().getName().equalsIgnoreCase(getSkyBlockWorld().getName()))
/* 1142:     */     {
/* 1143:1197 */       player.sendMessage(ChatColor.RED + "You must be closer to your island to set your skyblock home!");
/* 1144:1198 */       return true;
/* 1145:     */     }
/* 1146:1200 */     if (playerIsOnIsland(player))
/* 1147:     */     {
/* 1148:1202 */       if (getActivePlayers().containsKey(player.getName())) {
/* 1149:1204 */         ((PlayerInfo)getActivePlayers().get(player.getName())).setHomeLocation(player.getLocation());
/* 1150:     */       }
/* 1151:1207 */       player.sendMessage(ChatColor.GREEN + "Your skyblock home has been set to your current location.");
/* 1152:1208 */       return true;
/* 1153:     */     }
/* 1154:1210 */     player.sendMessage(ChatColor.RED + "You must be closer to your island to set your skyblock home!");
/* 1155:1211 */     return true;
/* 1156:     */   }
/* 1157:     */   
/* 1158:     */   public boolean warpSet(Player player)
/* 1159:     */   {
/* 1160:1216 */     if (!player.getWorld().getName().equalsIgnoreCase(getSkyBlockWorld().getName()))
/* 1161:     */     {
/* 1162:1217 */       player.sendMessage(ChatColor.RED + "You must be closer to your island to set your warp!");
/* 1163:1218 */       return true;
/* 1164:     */     }
/* 1165:1220 */     if (playerIsOnIsland(player))
/* 1166:     */     {
/* 1167:1222 */       if (getActivePlayers().containsKey(player.getName()))
/* 1168:     */       {
/* 1169:1224 */         ((PlayerInfo)getActivePlayers().get(player.getName())).setWarpLocation(player.getLocation());
/* 1170:1225 */         ((PlayerInfo)getActivePlayers().get(player.getName())).warpOn();
/* 1171:     */       }
/* 1172:1228 */       player.sendMessage(ChatColor.GREEN + "Your skyblock incoming warp has been set to your current location.");
/* 1173:1229 */       return true;
/* 1174:     */     }
/* 1175:1231 */     player.sendMessage(ChatColor.RED + "You must be closer to your island to set your warp!");
/* 1176:1232 */     return true;
/* 1177:     */   }
/* 1178:     */   
/* 1179:     */   public boolean homeSet(String player, Location loc)
/* 1180:     */   {
/* 1181:1237 */     if (getActivePlayers().containsKey(player))
/* 1182:     */     {
/* 1183:1239 */       ((PlayerInfo)getActivePlayers().get(player)).setHomeLocation(loc);
/* 1184:     */     }
/* 1185:     */     else
/* 1186:     */     {
/* 1187:1242 */       PlayerInfo pi = getInstance().readPlayerFile(player);
/* 1188:1243 */       pi.setHomeLocation(loc);
/* 1189:1244 */       getInstance().writePlayerFile(player, pi);
/* 1190:     */     }
/* 1191:1247 */     return true;
/* 1192:     */   }
/* 1193:     */   
/* 1194:     */   public boolean playerIsOnIsland(Player player)
/* 1195:     */   {
/* 1196:1252 */     if (getActivePlayers().containsKey(player.getName()))
/* 1197:     */     {
/* 1198:1254 */       if (((PlayerInfo)getActivePlayers().get(player.getName())).getHasIsland()) {
/* 1199:1256 */         this.islandTestLocation = ((PlayerInfo)getActivePlayers().get(player.getName())).getIslandLocation();
/* 1200:1257 */       } else if (((PlayerInfo)getActivePlayers().get(player.getName())).getHasParty()) {
/* 1201:1259 */         this.islandTestLocation = ((PlayerInfo)getActivePlayers().get(player.getName())).getPartyIslandLocation();
/* 1202:     */       }
/* 1203:1261 */       if (this.islandTestLocation == null) {
/* 1204:1262 */         return false;
/* 1205:     */       }
/* 1206:1263 */       if ((player.getLocation().getX() > this.islandTestLocation.getX() - Settings.island_protectionRange / 2) && (player.getLocation().getX() < this.islandTestLocation.getX() + Settings.island_protectionRange / 2) && 
/* 1207:1264 */         (player.getLocation().getZ() > this.islandTestLocation.getZ() - Settings.island_protectionRange / 2) && (player.getLocation().getZ() < this.islandTestLocation.getZ() + Settings.island_protectionRange / 2)) {
/* 1208:1265 */         return true;
/* 1209:     */       }
/* 1210:     */     }
/* 1211:1267 */     return false;
/* 1212:     */   }
/* 1213:     */   
/* 1214:     */   public boolean locationIsOnIsland(Player player, Location loc)
/* 1215:     */   {
/* 1216:1272 */     if (getActivePlayers().containsKey(player.getName()))
/* 1217:     */     {
/* 1218:1274 */       if (((PlayerInfo)getActivePlayers().get(player.getName())).getHasIsland()) {
/* 1219:1276 */         this.islandTestLocation = ((PlayerInfo)getActivePlayers().get(player.getName())).getIslandLocation();
/* 1220:1277 */       } else if (((PlayerInfo)getActivePlayers().get(player.getName())).getHasParty()) {
/* 1221:1279 */         this.islandTestLocation = ((PlayerInfo)getActivePlayers().get(player.getName())).getPartyIslandLocation();
/* 1222:     */       }
/* 1223:1281 */       if (this.islandTestLocation == null) {
/* 1224:1282 */         return false;
/* 1225:     */       }
/* 1226:1283 */       if ((loc.getX() > this.islandTestLocation.getX() - Settings.island_protectionRange / 2) && (loc.getX() < this.islandTestLocation.getX() + Settings.island_protectionRange / 2) && 
/* 1227:1284 */         (loc.getZ() > this.islandTestLocation.getZ() - Settings.island_protectionRange / 2) && (loc.getZ() < this.islandTestLocation.getZ() + Settings.island_protectionRange / 2)) {
/* 1228:1285 */         return true;
/* 1229:     */       }
/* 1230:     */     }
/* 1231:1287 */     return false;
/* 1232:     */   }
/* 1233:     */   
/* 1234:     */   public boolean playerIsInSpawn(Player player)
/* 1235:     */   {
/* 1236:1292 */     if ((player.getLocation().getX() > Settings.general_spawnSize * -1) && (player.getLocation().getX() < Settings.general_spawnSize) && (player.getLocation().getZ() > Settings.general_spawnSize * -1) && (player.getLocation().getZ() < Settings.general_spawnSize)) {
/* 1237:1293 */       return true;
/* 1238:     */     }
/* 1239:1294 */     return false;
/* 1240:     */   }
/* 1241:     */   
/* 1242:     */   public boolean hasIsland(String playername)
/* 1243:     */   {
/* 1244:1299 */     if (getActivePlayers().containsKey(playername)) {
/* 1245:1301 */       return ((PlayerInfo)getActivePlayers().get(playername)).getHasIsland();
/* 1246:     */     }
/* 1247:1304 */     PlayerInfo pi = getInstance().readPlayerFile(playername);
/* 1248:1305 */     if (pi == null) {
/* 1249:1306 */       return false;
/* 1250:     */     }
/* 1251:1307 */     return pi.getHasIsland();
/* 1252:     */   }
/* 1253:     */   
/* 1254:     */   public Location getPlayerIsland(String playername)
/* 1255:     */   {
/* 1256:1313 */     if (getActivePlayers().containsKey(playername)) {
/* 1257:1315 */       return ((PlayerInfo)getActivePlayers().get(playername)).getIslandLocation();
/* 1258:     */     }
/* 1259:1318 */     PlayerInfo pi = getInstance().readPlayerFile(playername);
/* 1260:1319 */     if (pi == null) {
/* 1261:1320 */       return null;
/* 1262:     */     }
/* 1263:1321 */     return pi.getIslandLocation();
/* 1264:     */   }
/* 1265:     */   
/* 1266:     */   public boolean transferIsland(String playerfrom, String playerto)
/* 1267:     */   {
/* 1268:1327 */     if ((!getActivePlayers().containsKey(playerfrom)) || (!getActivePlayers().containsKey(playerto))) {
/* 1269:1329 */       return false;
/* 1270:     */     }
/* 1271:1331 */     if (((PlayerInfo)getActivePlayers().get(playerfrom)).getHasIsland())
/* 1272:     */     {
/* 1273:1333 */       ((PlayerInfo)getActivePlayers().get(playerto)).setHasIsland(true);
/* 1274:1334 */       ((PlayerInfo)getActivePlayers().get(playerto)).setIslandLocation(((PlayerInfo)getActivePlayers().get(playerfrom)).getIslandLocation());
/* 1275:1335 */       ((PlayerInfo)getActivePlayers().get(playerto)).setIslandLevel(((PlayerInfo)getActivePlayers().get(playerfrom)).getIslandLevel());
/* 1276:1336 */       ((PlayerInfo)getActivePlayers().get(playerto)).setPartyIslandLocation(null);
/* 1277:1337 */       ((PlayerInfo)getActivePlayers().get(playerfrom)).setHasIsland(false);
/* 1278:1338 */       ((PlayerInfo)getActivePlayers().get(playerfrom)).setIslandLocation(null);
/* 1279:1339 */       ((PlayerInfo)getActivePlayers().get(playerfrom)).setIslandLevel(0);
/* 1280:1340 */       ((PlayerInfo)getActivePlayers().get(playerfrom)).setPartyIslandLocation(((PlayerInfo)getActivePlayers().get(playerto)).getIslandLocation());
/* 1281:1341 */       return true;
/* 1282:     */     }
/* 1283:1343 */     return false;
/* 1284:     */   }
/* 1285:     */   
/* 1286:     */   public boolean islandAtLocation(Location loc)
/* 1287:     */   {
/* 1288:1350 */     if (loc == null) {
/* 1289:1352 */       return true;
/* 1290:     */     }
/* 1291:1354 */     int px = loc.getBlockX();
/* 1292:1355 */     int py = loc.getBlockY();
/* 1293:1356 */     int pz = loc.getBlockZ();
/* 1294:1357 */     for (int x = -2; x <= 2; x++) {
/* 1295:1358 */       for (int y = -2; y <= 2; y++) {
/* 1296:1359 */         for (int z = -2; z <= 2; z++)
/* 1297:     */         {
/* 1298:1360 */           Block b = new Location(loc.getWorld(), px + x, py + y, pz + z).getBlock();
/* 1299:1361 */           if (b.getTypeId() != 0) {
/* 1300:1362 */             return true;
/* 1301:     */           }
/* 1302:     */         }
/* 1303:     */       }
/* 1304:     */     }
/* 1305:1366 */     return false;
/* 1306:     */   }
/* 1307:     */   
/* 1308:     */   public boolean islandInSpawn(Location loc)
/* 1309:     */   {
/* 1310:1371 */     if (loc == null) {
/* 1311:1373 */       return true;
/* 1312:     */     }
/* 1313:1375 */     if ((loc.getX() > Settings.general_spawnSize * -1 - Settings.island_protectionRange / 2) && (loc.getX() < Settings.general_spawnSize + Settings.island_protectionRange / 2) && (loc.getZ() > Settings.general_spawnSize * -1 - Settings.island_protectionRange / 2) && (loc.getZ() < Settings.general_spawnSize + Settings.island_protectionRange / 2)) {
/* 1314:1376 */       return true;
/* 1315:     */     }
/* 1316:1377 */     return false;
/* 1317:     */   }
/* 1318:     */   
/* 1319:     */   public ChunkGenerator getDefaultWorldGenerator(String worldName, String id)
/* 1320:     */   {
/* 1321:1382 */     return new SkyBlockChunkGenerator();
/* 1322:     */   }
/* 1323:     */   
/* 1324:     */   public Stack<SerializableLocation> changeStackToFile(Stack<Location> stack)
/* 1325:     */   {
/* 1326:1387 */     Stack<SerializableLocation> finishStack = new Stack();
/* 1327:1388 */     Stack<Location> tempStack = new Stack();
/* 1328:1389 */     while (!stack.isEmpty()) {
/* 1329:1390 */       tempStack.push((Location)stack.pop());
/* 1330:     */     }
/* 1331:1391 */     while (!tempStack.isEmpty()) {
/* 1332:1393 */       if (tempStack.peek() != null) {
/* 1333:1395 */         finishStack.push(new SerializableLocation((Location)tempStack.pop()));
/* 1334:     */       } else {
/* 1335:1397 */         tempStack.pop();
/* 1336:     */       }
/* 1337:     */     }
/* 1338:1399 */     return finishStack;
/* 1339:     */   }
/* 1340:     */   
/* 1341:     */   public Stack<Location> changestackfromfile(Stack<SerializableLocation> stack)
/* 1342:     */   {
/* 1343:1403 */     Stack<SerializableLocation> tempStack = new Stack();
/* 1344:1404 */     Stack<Location> finishStack = new Stack();
/* 1345:1405 */     while (!stack.isEmpty()) {
/* 1346:1406 */       tempStack.push((SerializableLocation)stack.pop());
/* 1347:     */     }
/* 1348:1407 */     while (!tempStack.isEmpty()) {
/* 1349:1409 */       if (tempStack.peek() != null) {
/* 1350:1410 */         finishStack.push(((SerializableLocation)tempStack.pop()).getLocation());
/* 1351:     */       } else {
/* 1352:1412 */         tempStack.pop();
/* 1353:     */       }
/* 1354:     */     }
/* 1355:1414 */     return finishStack;
/* 1356:     */   }
/* 1357:     */   
/* 1358:     */   public boolean largeIsland(Location l)
/* 1359:     */   {
/* 1360:1419 */     int blockcount = 0;
/* 1361:1420 */     int px = l.getBlockX();
/* 1362:1421 */     int py = l.getBlockY();
/* 1363:1422 */     int pz = l.getBlockZ();
/* 1364:1423 */     for (int x = -30; x <= 30; x++) {
/* 1365:1424 */       for (int y = -30; y <= 30; y++) {
/* 1366:1425 */         for (int z = -30; z <= 30; z++)
/* 1367:     */         {
/* 1368:1426 */           Block b = new Location(l.getWorld(), px + x, py + y, pz + z).getBlock();
/* 1369:1427 */           if ((b.getTypeId() != 0) && (b.getTypeId() != 8) && (b.getTypeId() != 10)) {
/* 1370:1429 */             if (blockcount > 200) {
/* 1371:1431 */               return true;
/* 1372:     */             }
/* 1373:     */           }
/* 1374:     */         }
/* 1375:     */       }
/* 1376:     */     }
/* 1377:1437 */     if (blockcount > 200) {
/* 1378:1439 */       return true;
/* 1379:     */     }
/* 1380:1441 */     return false;
/* 1381:     */   }
/* 1382:     */   
/* 1383:     */   public boolean clearAbandoned()
/* 1384:     */   {
/* 1385:1448 */     int numOffline = 0;
/* 1386:1449 */     OfflinePlayer[] oplayers = Bukkit.getServer().getOfflinePlayers();
/* 1387:1450 */     System.out.print("Attemping to add more orphans");
/* 1388:1451 */     for (int i = 0; i < oplayers.length; i++)
/* 1389:     */     {
/* 1390:1453 */       long offlineTime = oplayers[i].getLastPlayed();
/* 1391:1454 */       offlineTime = (System.currentTimeMillis() - offlineTime) / 3600000L;
/* 1392:1455 */       if ((offlineTime > 250L) && (getInstance().hasIsland(oplayers[i].getName())) && (offlineTime < 50000L))
/* 1393:     */       {
/* 1394:1457 */         PlayerInfo pi = getInstance().readPlayerFile(oplayers[i].getName());
/* 1395:1458 */         Location l = pi.getIslandLocation();
/* 1396:1459 */         int blockcount = 0;
/* 1397:1460 */         int px = l.getBlockX();
/* 1398:1461 */         int py = l.getBlockY();
/* 1399:1462 */         int pz = l.getBlockZ();
/* 1400:1463 */         for (int x = -30; x <= 30; x++) {
/* 1401:1464 */           for (int y = -30; y <= 30; y++) {
/* 1402:1465 */             for (int z = -30; z <= 30; z++)
/* 1403:     */             {
/* 1404:1466 */               Block b = new Location(l.getWorld(), px + x, py + y, pz + z).getBlock();
/* 1405:1467 */               if ((b.getTypeId() != 0) && (b.getTypeId() != 8) && (b.getTypeId() != 10)) {
/* 1406:1469 */                 blockcount++;
/* 1407:     */               }
/* 1408:     */             }
/* 1409:     */           }
/* 1410:     */         }
/* 1411:1474 */         if (blockcount < 200)
/* 1412:     */         {
/* 1413:1477 */           numOffline++;
/* 1414:1478 */           WorldGuardHandler.getWorldGuard().getRegionManager(getSkyBlockWorld()).removeRegion(oplayers[i].getName() + "Island");
/* 1415:1479 */           this.orphaned.push(pi.getIslandLocation());
/* 1416:     */           
/* 1417:1481 */           pi.setHomeLocation(null);
/* 1418:1482 */           pi.setHasIsland(false);
/* 1419:1483 */           pi.setIslandLocation(null);
/* 1420:1484 */           writePlayerFile(oplayers[i].getName(), pi);
/* 1421:     */         }
/* 1422:     */       }
/* 1423:     */     }
/* 1424:1489 */     if (numOffline > 0)
/* 1425:     */     {
/* 1426:1491 */       System.out.print("Added " + numOffline + " new orphans.");
/* 1427:1492 */       saveOrphans();
/* 1428:1493 */       updateOrphans();
/* 1429:1494 */       return true;
/* 1430:     */     }
/* 1431:1496 */     System.out.print("No new orphans to add!");
/* 1432:1497 */     return false;
/* 1433:     */   }
/* 1434:     */   
/* 1435:     */   public LinkedHashMap<String, Double> generateTopTen()
/* 1436:     */   {
/* 1437:1502 */     HashMap<String, Double> tempMap = new LinkedHashMap();
/* 1438:1503 */     File folder = this.directoryPlayers;
/* 1439:1504 */     File[] listOfFiles = folder.listFiles();
/* 1440:1507 */     for (int i = 0; i < listOfFiles.length; i++)
/* 1441:     */     {
/* 1442:     */       PlayerInfo pi;
/* 1443:1509 */       if ((pi = getInstance().readPlayerFile(listOfFiles[i].getName())) != null) {
/* 1444:1511 */         if ((pi.getIslandLevel() > 0) && ((!pi.getHasParty()) || (pi.getPartyLeader().equalsIgnoreCase(pi.getPlayerName())))) {
/* 1445:1513 */           tempMap.put(listOfFiles[i].getName(), Double.valueOf(pi.getIslandLevel()));
/* 1446:     */         }
/* 1447:     */       }
/* 1448:     */     }
/* 1449:1517 */     LinkedHashMap<String, Double> sortedMap = sortHashMapByValuesD(tempMap);
/* 1450:1518 */     return sortedMap;
/* 1451:     */   }
/* 1452:     */   
/* 1453:     */   public LinkedHashMap<String, Double> sortHashMapByValuesD(HashMap<String, Double> passedMap)
/* 1454:     */   {
/* 1455:1523 */     List<String> mapKeys = new ArrayList(passedMap.keySet());
/* 1456:1524 */     List<Double> mapValues = new ArrayList(passedMap.values());
/* 1457:1525 */     Collections.sort(mapValues);
/* 1458:1526 */     Collections.reverse(mapValues);
/* 1459:1527 */     Collections.sort(mapKeys);
/* 1460:1528 */     Collections.reverse(mapKeys);
/* 1461:     */     
/* 1462:1530 */     LinkedHashMap<String, Double> sortedMap = 
/* 1463:1531 */       new LinkedHashMap();
/* 1464:     */     
/* 1465:1533 */     Iterator<Double> valueIt = mapValues.iterator();
/* 1466:1534 */     while (valueIt.hasNext())
/* 1467:     */     {
/* 1468:1535 */       Double val = (Double)valueIt.next();
/* 1469:1536 */       Iterator<String> keyIt = mapKeys.iterator();
/* 1470:1538 */       while (keyIt.hasNext())
/* 1471:     */       {
/* 1472:1539 */         String key = (String)keyIt.next();
/* 1473:1540 */         String comp1 = ((Double)passedMap.get(key)).toString();
/* 1474:1541 */         String comp2 = val.toString();
/* 1475:1543 */         if (comp1.equals(comp2))
/* 1476:     */         {
/* 1477:1544 */           passedMap.remove(key);
/* 1478:1545 */           mapKeys.remove(key);
/* 1479:1546 */           sortedMap.put(key, val);
/* 1480:1547 */           break;
/* 1481:     */         }
/* 1482:     */       }
/* 1483:     */     }
/* 1484:1553 */     return sortedMap;
/* 1485:     */   }
/* 1486:     */   
/* 1487:     */   public boolean onInfoCooldown(Player player)
/* 1488:     */   {
/* 1489:1558 */     if (this.infoCooldown.containsKey(player.getName()))
/* 1490:     */     {
/* 1491:1560 */       if (((Long)this.infoCooldown.get(player.getName())).longValue() > Calendar.getInstance().getTimeInMillis()) {
/* 1492:1561 */         return true;
/* 1493:     */       }
/* 1494:1564 */       return false;
/* 1495:     */     }
/* 1496:1567 */     return false;
/* 1497:     */   }
/* 1498:     */   
/* 1499:     */   public boolean onRestartCooldown(Player player)
/* 1500:     */   {
/* 1501:1572 */     if (this.restartCooldown.containsKey(player.getName()))
/* 1502:     */     {
/* 1503:1574 */       if (((Long)this.restartCooldown.get(player.getName())).longValue() > Calendar.getInstance().getTimeInMillis()) {
/* 1504:1575 */         return true;
/* 1505:     */       }
/* 1506:1578 */       return false;
/* 1507:     */     }
/* 1508:1581 */     return false;
/* 1509:     */   }
/* 1510:     */   
/* 1511:     */   public long getInfoCooldownTime(Player player)
/* 1512:     */   {
/* 1513:1586 */     if (this.infoCooldown.containsKey(player.getName()))
/* 1514:     */     {
/* 1515:1588 */       if (((Long)this.infoCooldown.get(player.getName())).longValue() > Calendar.getInstance().getTimeInMillis()) {
/* 1516:1589 */         return ((Long)this.infoCooldown.get(player.getName())).longValue() - Calendar.getInstance().getTimeInMillis();
/* 1517:     */       }
/* 1518:1592 */       return 0L;
/* 1519:     */     }
/* 1520:1595 */     return 0L;
/* 1521:     */   }
/* 1522:     */   
/* 1523:     */   public long getRestartCooldownTime(Player player)
/* 1524:     */   {
/* 1525:1600 */     if (this.restartCooldown.containsKey(player.getName()))
/* 1526:     */     {
/* 1527:1602 */       if (((Long)this.restartCooldown.get(player.getName())).longValue() > Calendar.getInstance().getTimeInMillis()) {
/* 1528:1603 */         return ((Long)this.restartCooldown.get(player.getName())).longValue() - Calendar.getInstance().getTimeInMillis();
/* 1529:     */       }
/* 1530:1606 */       return 0L;
/* 1531:     */     }
/* 1532:1609 */     return 0L;
/* 1533:     */   }
/* 1534:     */   
/* 1535:     */   public void setInfoCooldown(Player player)
/* 1536:     */   {
/* 1537:1614 */     this.infoCooldown.put(player.getName(), Long.valueOf(Calendar.getInstance().getTimeInMillis() + Settings.general_cooldownInfo * 1000));
/* 1538:     */   }
/* 1539:     */   
/* 1540:     */   public void setRestartCooldown(Player player)
/* 1541:     */   {
/* 1542:1619 */     this.restartCooldown.put(player.getName(), Long.valueOf(Calendar.getInstance().getTimeInMillis() + Settings.general_cooldownRestart * 1000));
/* 1543:     */   }
/* 1544:     */   
/* 1545:     */   public File[] getSchemFile()
/* 1546:     */   {
/* 1547:1624 */     return this.schemFile;
/* 1548:     */   }
/* 1549:     */   
/* 1550:     */   public boolean testForObsidian(Block block)
/* 1551:     */   {
/* 1552:1630 */     for (int x = -3; x <= 3; x++) {
/* 1553:1631 */       for (int y = -3; y <= 3; y++) {
/* 1554:1632 */         for (int z = -3; z <= 3; z++)
/* 1555:     */         {
/* 1556:1634 */           Block testBlock = getSkyBlockWorld().getBlockAt(block.getX() + x, block.getY() + y, block.getZ() + z);
/* 1557:1635 */           if (((x != 0) || (y != 0) || (z != 0)) && (testBlock.getType() == Material.OBSIDIAN)) {
/* 1558:1637 */             return true;
/* 1559:     */           }
/* 1560:     */         }
/* 1561:     */       }
/* 1562:     */     }
/* 1563:1640 */     return false;
/* 1564:     */   }
/* 1565:     */   
/* 1566:     */   public void removeInactive(List<String> removePlayerList)
/* 1567:     */   {
/* 1568:1645 */     getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(getInstance(), new Runnable()
/* 1569:     */     {
/* 1570:     */       public void run()
/* 1571:     */       {
/* 1572:1648 */         if ((uSkyBlock.getInstance().getRemoveList().size() > 0) && (!uSkyBlock.getInstance().isPurgeActive()))
/* 1573:     */         {
/* 1574:1650 */           uSkyBlock.getInstance().deletePlayerIsland((String)uSkyBlock.getInstance().getRemoveList().get(0));
/* 1575:1651 */           System.out.print("[uSkyBlock] Purge: Removing " + (String)uSkyBlock.getInstance().getRemoveList().get(0) + "'s island");
/* 1576:1652 */           uSkyBlock.getInstance().deleteFromRemoveList();
/* 1577:     */         }
/* 1578:     */       }
/* 1579:1655 */     }, 0L, 200L);
/* 1580:     */   }
/* 1581:     */   
/* 1582:     */   public List<String> getRemoveList()
/* 1583:     */   {
/* 1584:1660 */     return this.removeList;
/* 1585:     */   }
/* 1586:     */   
/* 1587:     */   public void addToRemoveList(String string)
/* 1588:     */   {
/* 1589:1665 */     this.removeList.add(string);
/* 1590:     */   }
/* 1591:     */   
/* 1592:     */   public void deleteFromRemoveList()
/* 1593:     */   {
/* 1594:1670 */     this.removeList.remove(0);
/* 1595:     */   }
/* 1596:     */   
/* 1597:     */   public boolean isPurgeActive()
/* 1598:     */   {
/* 1599:1675 */     return this.purgeActive;
/* 1600:     */   }
/* 1601:     */   
/* 1602:     */   public void activatePurge()
/* 1603:     */   {
/* 1604:1680 */     this.purgeActive = true;
/* 1605:     */   }
/* 1606:     */   
/* 1607:     */   public void deactivatePurge()
/* 1608:     */   {
/* 1609:1685 */     this.purgeActive = false;
/* 1610:     */   }
/* 1611:     */   
/* 1612:     */   public HashMap<String, PlayerInfo> getActivePlayers()
/* 1613:     */   {
/* 1614:1690 */     return this.activePlayers;
/* 1615:     */   }
/* 1616:     */   
/* 1617:     */   public void addActivePlayer(String player, PlayerInfo pi)
/* 1618:     */   {
/* 1619:1695 */     this.activePlayers.put(player, pi);
/* 1620:     */   }
/* 1621:     */   
/* 1622:     */   public void removeActivePlayer(String player)
/* 1623:     */   {
/* 1624:1700 */     if (this.activePlayers.containsKey(player))
/* 1625:     */     {
/* 1626:1702 */       writePlayerFile(player, (PlayerInfo)this.activePlayers.get(player));
/* 1627:     */       
/* 1628:1704 */       this.activePlayers.remove(player);
/* 1629:1705 */       System.out.print("Removing player from memory: " + player);
/* 1630:     */     }
/* 1631:     */   }
/* 1632:     */   
/* 1633:     */   public void populateChallengeList()
/* 1634:     */   {
/* 1635:1711 */     List<String> templist = new ArrayList();
/* 1636:1712 */     for (int i = 0; i < Settings.challenges_ranks.length; i++)
/* 1637:     */     {
/* 1638:1714 */       this.challenges.put(Settings.challenges_ranks[i], templist);
/* 1639:1715 */       templist = new ArrayList();
/* 1640:     */     }
/* 1641:1717 */     Iterator<String> itr = Settings.challenges_challengeList.iterator();
/* 1642:1718 */     while (itr.hasNext())
/* 1643:     */     {
/* 1644:1720 */       String tempString = (String)itr.next();
/* 1645:1721 */       if (this.challenges.containsKey(getConfig().getString("options.challenges.challengeList." + tempString + ".rankLevel"))) {
/* 1646:1723 */         ((List)this.challenges.get(getConfig().getString("options.challenges.challengeList." + tempString + ".rankLevel"))).add(tempString);
/* 1647:     */       }
/* 1648:     */     }
/* 1649:     */   }
/* 1650:     */   
/* 1651:     */   public String getChallengesFromRank(Player player, String rank)
/* 1652:     */   {
/* 1653:1732 */     this.rankDisplay = ((List)this.challenges.get(rank));
/* 1654:1733 */     String fullString = "";
/* 1655:1734 */     PlayerInfo pi = (PlayerInfo)getActivePlayers().get(player.getName());
/* 1656:1735 */     Iterator<String> itr = this.rankDisplay.iterator();
/* 1657:1736 */     while (itr.hasNext())
/* 1658:     */     {
/* 1659:1738 */       String tempString = (String)itr.next();
/* 1660:1739 */       if (pi.checkChallenge(tempString))
/* 1661:     */       {
/* 1662:1741 */         if (getConfig().getBoolean("options.challenges.challengeList." + tempString + ".repeatable")) {
/* 1663:1743 */           fullString = fullString + Settings.challenges_repeatableColor.replace('&', '§') + tempString + ChatColor.DARK_GRAY + " - ";
/* 1664:     */         } else {
/* 1665:1745 */           fullString = fullString + Settings.challenges_finishedColor.replace('&', '§') + tempString + ChatColor.DARK_GRAY + " - ";
/* 1666:     */         }
/* 1667:     */       }
/* 1668:     */       else {
/* 1669:1748 */         fullString = fullString + Settings.challenges_challengeColor.replace('&', '§') + tempString + ChatColor.DARK_GRAY + " - ";
/* 1670:     */       }
/* 1671:     */     }
/* 1672:1751 */     if (fullString.length() > 3) {
/* 1673:1752 */       fullString = fullString.substring(0, fullString.length() - 2);
/* 1674:     */     }
/* 1675:1753 */     return fullString;
/* 1676:     */   }
/* 1677:     */   
/* 1678:     */   public int checkRankCompletion(Player player, String rank)
/* 1679:     */   {
/* 1680:1758 */     if (!Settings.challenges_requirePreviousRank) {
/* 1681:1759 */       return 0;
/* 1682:     */     }
/* 1683:1760 */     this.rankDisplay = ((List)this.challenges.get(rank));
/* 1684:1761 */     int ranksCompleted = 0;
/* 1685:1762 */     PlayerInfo pi = (PlayerInfo)getActivePlayers().get(player.getName());
/* 1686:1763 */     Iterator<String> itr = this.rankDisplay.iterator();
/* 1687:1764 */     while (itr.hasNext())
/* 1688:     */     {
/* 1689:1766 */       String tempString = (String)itr.next();
/* 1690:1767 */       if (pi.checkChallenge(tempString)) {
/* 1691:1769 */         ranksCompleted++;
/* 1692:     */       }
/* 1693:     */     }
/* 1694:1775 */     return this.rankDisplay.size() - Settings.challenges_rankLeeway - ranksCompleted;
/* 1695:     */   }
/* 1696:     */   
/* 1697:     */   public boolean isRankAvailable(Player player, String rank)
/* 1698:     */   {
/* 1699:1780 */     if (this.challenges.size() < 2) {
/* 1700:1782 */       return true;
/* 1701:     */     }
/* 1702:1786 */     for (int i = 0; i < Settings.challenges_ranks.length; i++) {
/* 1703:1788 */       if (Settings.challenges_ranks[i].equalsIgnoreCase(rank))
/* 1704:     */       {
/* 1705:1790 */         if (i == 0) {
/* 1706:1791 */           return true;
/* 1707:     */         }
/* 1708:1794 */         if (checkRankCompletion(player, Settings.challenges_ranks[(i - 1)]) <= 0) {
/* 1709:1795 */           return true;
/* 1710:     */         }
/* 1711:     */       }
/* 1712:     */     }
/* 1713:1801 */     return false;
/* 1714:     */   }
/* 1715:     */   
/* 1716:     */   public boolean checkIfCanCompleteChallenge(Player player, String challenge)
/* 1717:     */   {
/* 1718:1806 */     PlayerInfo pi = (PlayerInfo)getActivePlayers().get(player.getName());
/* 1719:1810 */     if (!isRankAvailable(player, getConfig().getString("options.challenges.challengeList." + challenge + ".rankLevel")))
/* 1720:     */     {
/* 1721:1812 */       player.sendMessage(ChatColor.RED + "You have not unlocked this challenge yet!");
/* 1722:1813 */       return false;
/* 1723:     */     }
/* 1724:1815 */     if (!pi.challengeExists(challenge))
/* 1725:     */     {
/* 1726:1817 */       player.sendMessage(ChatColor.RED + "Unknown challenge name (check spelling)!");
/* 1727:1818 */       return false;
/* 1728:     */     }
/* 1729:1820 */     if ((pi.checkChallenge(challenge)) && (!getConfig().getBoolean("options.challenges.challengeList." + challenge + ".repeatable")))
/* 1730:     */     {
/* 1731:1822 */       player.sendMessage(ChatColor.RED + "This challenge is not repeatable!");
/* 1732:1823 */       return false;
/* 1733:     */     }
/* 1734:1825 */     if ((pi.checkChallenge(challenge)) && ((getConfig().getString("options.challenges.challengeList." + challenge + ".type").equalsIgnoreCase("onIsland")) || (getConfig().getString("options.challenges.challengeList." + challenge + ".type").equalsIgnoreCase("onIsland"))))
/* 1735:     */     {
/* 1736:1827 */       player.sendMessage(ChatColor.RED + "This challenge is not repeatable!");
/* 1737:1828 */       return false;
/* 1738:     */     }
/* 1739:1830 */     if (getConfig().getString("options.challenges.challengeList." + challenge + ".type").equalsIgnoreCase("onPlayer"))
/* 1740:     */     {
/* 1741:1832 */       if (!hasRequired(player, challenge, "onPlayer"))
/* 1742:     */       {
/* 1743:1834 */         player.sendMessage(ChatColor.RED + getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".description").toString()));
/* 1744:1835 */         player.sendMessage(ChatColor.RED + "You don't have enough of the required item(s)!");
/* 1745:1836 */         return false;
/* 1746:     */       }
/* 1747:1838 */       return true;
/* 1748:     */     }
/* 1749:1839 */     if (getConfig().getString("options.challenges.challengeList." + challenge + ".type").equalsIgnoreCase("onIsland"))
/* 1750:     */     {
/* 1751:1841 */       if (!playerIsOnIsland(player)) {
/* 1752:1843 */         player.sendMessage(ChatColor.RED + "You must be on your island to do that!");
/* 1753:     */       }
/* 1754:1845 */       if (!hasRequired(player, challenge, "onIsland"))
/* 1755:     */       {
/* 1756:1847 */         player.sendMessage(ChatColor.RED + getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".description").toString()));
/* 1757:     */         
/* 1758:1849 */         player.sendMessage(ChatColor.RED + "You must be standing within 10 blocks of all required items.");
/* 1759:1850 */         return false;
/* 1760:     */       }
/* 1761:1852 */       return true;
/* 1762:     */     }
/* 1763:1853 */     if (getConfig().getString("options.challenges.challengeList." + challenge + ".type").equalsIgnoreCase("islandLevel"))
/* 1764:     */     {
/* 1765:1855 */       if (pi.getIslandLevel() >= getConfig().getInt("options.challenges.challengeList." + challenge + ".requiredItems")) {
/* 1766:1857 */         return true;
/* 1767:     */       }
/* 1768:1860 */       player.sendMessage(ChatColor.RED + "Your island must be level " + getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".requiredItems").toString()) + " to complete this challenge!");
/* 1769:1861 */       return false;
/* 1770:     */     }
/* 1771:1864 */     return false;
/* 1772:     */   }
/* 1773:     */   
/* 1774:     */   public boolean takeRequired(Player player, String challenge, String type)
/* 1775:     */   {
/* 1776:1869 */     if (type.equalsIgnoreCase("onPlayer"))
/* 1777:     */     {
/* 1778:1871 */       String[] reqList = getConfig().getString("options.challenges.challengeList." + challenge + ".requiredItems").split(" ");
/* 1779:     */       
/* 1780:1873 */       int reqItem = 0;
/* 1781:1874 */       int reqAmount = 0;
/* 1782:1875 */       int reqMod = -1;
/* 1783:1876 */       for (String s : reqList)
/* 1784:     */       {
/* 1785:1878 */         String[] sPart = s.split(":");
/* 1786:1879 */         if (sPart.length == 2)
/* 1787:     */         {
/* 1788:1881 */           reqItem = Integer.parseInt(sPart[0]);
/* 1789:1882 */           reqAmount = Integer.parseInt(sPart[1]);
/* 1790:1883 */           if (!player.getInventory().contains(reqItem, reqAmount)) {
/* 1791:1884 */             return false;
/* 1792:     */           }
/* 1793:1887 */           player.getInventory().removeItem(new ItemStack[] { new ItemStack(reqItem, reqAmount) });
/* 1794:     */         }
/* 1795:1889 */         else if (sPart.length == 3)
/* 1796:     */         {
/* 1797:1891 */           reqItem = Integer.parseInt(sPart[0]);
/* 1798:1892 */           reqAmount = Integer.parseInt(sPart[2]);
/* 1799:1893 */           reqMod = Integer.parseInt(sPart[1]);
/* 1800:1894 */           if (!player.getInventory().containsAtLeast(new ItemStack(reqItem, reqAmount, (short)reqMod), reqAmount)) {
/* 1801:1895 */             return false;
/* 1802:     */           }
/* 1803:1897 */           player.getInventory().removeItem(new ItemStack[] { new ItemStack(reqItem, reqAmount, (short)reqMod) });
/* 1804:     */         }
/* 1805:     */       }
/* 1806:1900 */       return true;
/* 1807:     */     }
/* 1808:1901 */     if (type.equalsIgnoreCase("onIsland")) {
/* 1809:1903 */       return true;
/* 1810:     */     }
/* 1811:1904 */     if (type.equalsIgnoreCase("islandLevel")) {
/* 1812:1906 */       return true;
/* 1813:     */     }
/* 1814:1908 */     return false;
/* 1815:     */   }
/* 1816:     */   
/* 1817:     */   public boolean hasRequired(Player player, String challenge, String type)
/* 1818:     */   {
/* 1819:1913 */     String[] reqList = getConfig().getString("options.challenges.challengeList." + challenge + ".requiredItems").split(" ");
/* 1820:1915 */     if (type.equalsIgnoreCase("onPlayer"))
/* 1821:     */     {
/* 1822:1917 */       int reqItem = 0;
/* 1823:1918 */       int reqAmount = 0;
/* 1824:1919 */       int reqMod = -1;
/* 1825:1920 */       for (String s : reqList)
/* 1826:     */       {
/* 1827:1922 */         String[] sPart = s.split(":");
/* 1828:1923 */         if (sPart.length == 2)
/* 1829:     */         {
/* 1830:1925 */           reqItem = Integer.parseInt(sPart[0]);
/* 1831:1926 */           reqAmount = Integer.parseInt(sPart[1]);
/* 1832:1928 */           if (!player.getInventory().containsAtLeast(new ItemStack(reqItem, reqAmount, (short)0), reqAmount)) {
/* 1833:1929 */             return false;
/* 1834:     */           }
/* 1835:     */         }
/* 1836:1930 */         else if (sPart.length == 3)
/* 1837:     */         {
/* 1838:1932 */           reqItem = Integer.parseInt(sPart[0]);
/* 1839:1933 */           reqAmount = Integer.parseInt(sPart[2]);
/* 1840:1934 */           reqMod = Integer.parseInt(sPart[1]);
/* 1841:1935 */           if (!player.getInventory().containsAtLeast(new ItemStack(reqItem, reqAmount, (short)reqMod), reqAmount)) {
/* 1842:1936 */             return false;
/* 1843:     */           }
/* 1844:     */         }
/* 1845:     */       }
/* 1846:1939 */       if (getConfig().getBoolean("options.challenges.challengeList." + challenge + ".takeItems")) {
/* 1847:1940 */         takeRequired(player, challenge, type);
/* 1848:     */       }
/* 1849:1941 */       return true;
/* 1850:     */     }
/* 1851:1942 */     if (type.equalsIgnoreCase("onIsland"))
/* 1852:     */     {
/* 1853:1944 */       int[][] neededItem = new int[reqList.length][2];
/* 1854:1945 */       for (int i = 0; i < reqList.length; i++)
/* 1855:     */       {
/* 1856:1947 */         String[] sPart = reqList[i].split(":");
/* 1857:1948 */         neededItem[i][0] = Integer.parseInt(sPart[0]);
/* 1858:1949 */         neededItem[i][1] = Integer.parseInt(sPart[1]);
/* 1859:     */       }
/* 1860:1951 */       Location l = player.getLocation();
/* 1861:1952 */       int px = l.getBlockX();
/* 1862:1953 */       int py = l.getBlockY();
/* 1863:1954 */       int pz = l.getBlockZ();
/* 1864:1955 */       for (int x = -10; x <= 10; x++) {
/* 1865:1956 */         for (int y = -3; y <= 10; y++) {
/* 1866:1957 */           for (int z = -10; z <= 10; z++)
/* 1867:     */           {
/* 1868:1958 */             Block b = new Location(l.getWorld(), px + x, py + y, pz + z).getBlock();
/* 1869:1959 */             for (int i = 0; i < neededItem.length; i++) {
/* 1870:1962 */               if (b.getTypeId() == neededItem[i][0]) {
/* 1871:1965 */                 neededItem[i][1] -= 1;
/* 1872:     */               }
/* 1873:     */             }
/* 1874:     */           }
/* 1875:     */         }
/* 1876:     */       }
/* 1877:1971 */       for (int i = 0; i < neededItem.length; i++) {
/* 1878:1973 */         if (neededItem[i][1] > 0) {
/* 1879:1975 */           return false;
/* 1880:     */         }
/* 1881:     */       }
/* 1882:1978 */       return true;
/* 1883:     */     }
/* 1884:1983 */     return true;
/* 1885:     */   }
/* 1886:     */   
/* 1887:     */   public boolean giveReward(Player player, String challenge)
/* 1888:     */   {
/* 1889:1989 */     String[] permList = getConfig().getString("options.challenges.challengeList." + challenge.toLowerCase() + ".permissionReward").split(" ");
/* 1890:1990 */     int rewCurrency = 0;
/* 1891:1991 */     player.sendMessage(ChatColor.GREEN + "You have completed the " + challenge + " challenge!");
/* 1892:     */     String[] rewList;
/* 1893:1992 */     if (!((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallenge(challenge))
/* 1894:     */     {
/* 1895:1994 */       String[] rewList = getConfig().getString("options.challenges.challengeList." + challenge.toLowerCase() + ".itemReward").split(" ");
/* 1896:1995 */       if ((Settings.challenges_enableEconomyPlugin) && (VaultHandler.econ != null)) {
/* 1897:1996 */         rewCurrency = getConfig().getInt("options.challenges.challengeList." + challenge.toLowerCase() + ".currencyReward");
/* 1898:     */       }
/* 1899:     */     }
/* 1900:     */     else
/* 1901:     */     {
/* 1902:2000 */       rewList = getConfig().getString("options.challenges.challengeList." + challenge.toLowerCase() + ".repeatItemReward").split(" ");
/* 1903:2001 */       if ((Settings.challenges_enableEconomyPlugin) && (VaultHandler.econ != null)) {
/* 1904:2002 */         rewCurrency = getConfig().getInt("options.challenges.challengeList." + challenge.toLowerCase() + ".repeatCurrencyReward");
/* 1905:     */       }
/* 1906:     */     }
/* 1907:2005 */     int rewItem = 0;
/* 1908:2006 */     int rewAmount = 0;
/* 1909:2007 */     int rewMod = -1;
/* 1910:2008 */     if ((Settings.challenges_enableEconomyPlugin) && (VaultHandler.econ != null))
/* 1911:     */     {
/* 1912:2010 */       VaultHandler.econ.depositPlayer(player.getName(), rewCurrency);
/* 1913:2011 */       if (((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallenge(challenge))
/* 1914:     */       {
/* 1915:2013 */         player.giveExp(getInstance().getConfig().getInt("options.challenges.challengeList." + challenge + ".repeatXpReward"));
/* 1916:2014 */         player.sendMessage(ChatColor.YELLOW + "Repeat reward(s): " + ChatColor.WHITE + getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".repeatRewardText").toString()).replace('&', '§'));
/* 1917:2015 */         player.sendMessage(ChatColor.YELLOW + "Repeat exp reward: " + ChatColor.WHITE + getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".repeatXpReward").toString()));
/* 1918:2016 */         player.sendMessage(ChatColor.YELLOW + "Repeat currency reward: " + ChatColor.WHITE + getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".repeatCurrencyReward").toString()) + " " + VaultHandler.econ.currencyNamePlural());
/* 1919:     */       }
/* 1920:     */       else
/* 1921:     */       {
/* 1922:2019 */         if (Settings.challenges_broadcastCompletion) {
/* 1923:2020 */           Bukkit.getServer().broadcastMessage(Settings.challenges_broadcastText.replace('&', '§') + player.getName() + " has completed the " + challenge + " challenge!");
/* 1924:     */         }
/* 1925:2021 */         player.giveExp(getInstance().getConfig().getInt("options.challenges.challengeList." + challenge + ".xpReward"));
/* 1926:2022 */         player.sendMessage(ChatColor.YELLOW + "Reward(s): " + ChatColor.WHITE + getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".rewardText").toString()).replace('&', '§'));
/* 1927:2023 */         player.sendMessage(ChatColor.YELLOW + "Exp reward: " + ChatColor.WHITE + getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".xpReward").toString()));
/* 1928:2024 */         player.sendMessage(ChatColor.YELLOW + "Currency reward: " + ChatColor.WHITE + getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".currencyReward").toString()) + " " + VaultHandler.econ.currencyNamePlural());
/* 1929:     */       }
/* 1930:     */     }
/* 1931:2028 */     else if (((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallenge(challenge))
/* 1932:     */     {
/* 1933:2030 */       player.giveExp(getInstance().getConfig().getInt("options.challenges.challengeList." + challenge + ".repeatXpReward"));
/* 1934:2031 */       player.sendMessage(ChatColor.YELLOW + "Repeat reward(s): " + ChatColor.WHITE + getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".repeatRewardText").toString()).replace('&', '§'));
/* 1935:2032 */       player.sendMessage(ChatColor.YELLOW + "Repeat exp reward: " + ChatColor.WHITE + getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".repeatXpReward").toString()));
/* 1936:     */     }
/* 1937:     */     else
/* 1938:     */     {
/* 1939:2035 */       if (Settings.challenges_broadcastCompletion) {
/* 1940:2036 */         Bukkit.getServer().broadcastMessage(Settings.challenges_broadcastText.replace('&', '§') + player.getName() + " has completed the " + challenge + " challenge!");
/* 1941:     */       }
/* 1942:2037 */       player.giveExp(getInstance().getConfig().getInt("options.challenges.challengeList." + challenge + ".xpReward"));
/* 1943:2038 */       player.sendMessage(ChatColor.YELLOW + "Reward(s): " + ChatColor.WHITE + getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".rewardText").toString()).replace('&', '§'));
/* 1944:2039 */       player.sendMessage(ChatColor.YELLOW + "Exp reward: " + ChatColor.WHITE + getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(challenge).append(".xpReward").toString()));
/* 1945:     */     }
/* 1946:2042 */     for (String s : permList) {
/* 1947:2044 */       if (!s.equalsIgnoreCase("none")) {
/* 1948:2046 */         if (!VaultHandler.checkPerk(player.getName(), s, player.getWorld())) {
/* 1949:2048 */           VaultHandler.addPerk(player, s);
/* 1950:     */         }
/* 1951:     */       }
/* 1952:     */     }
/* 1953:2052 */     for (String s : rewList)
/* 1954:     */     {
/* 1955:2054 */       String[] sPart = s.split(":");
/* 1956:2055 */       if (sPart.length == 2)
/* 1957:     */       {
/* 1958:2057 */         rewItem = Integer.parseInt(sPart[0]);
/* 1959:2058 */         rewAmount = Integer.parseInt(sPart[1]);
/* 1960:2059 */         player.getInventory().addItem(new ItemStack[] { new ItemStack(rewItem, rewAmount) });
/* 1961:     */       }
/* 1962:2060 */       else if (sPart.length == 3)
/* 1963:     */       {
/* 1964:2062 */         rewItem = Integer.parseInt(sPart[0]);
/* 1965:2063 */         rewAmount = Integer.parseInt(sPart[2]);
/* 1966:2064 */         rewMod = Integer.parseInt(sPart[1]);
/* 1967:2065 */         player.getInventory().addItem(new ItemStack[] { new ItemStack(rewItem, rewAmount, (short)rewMod) });
/* 1968:     */       }
/* 1969:     */     }
/* 1970:2068 */     if (!((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).checkChallenge(challenge))
/* 1971:     */     {
/* 1972:2070 */       ((PlayerInfo)getInstance().getActivePlayers().get(player.getName())).completeChallenge(challenge);
/* 1973:2071 */       getInstance().writePlayerFile(player.getName(), (PlayerInfo)getInstance().getActivePlayers().get(player.getName()));
/* 1974:     */     }
/* 1975:2075 */     return true;
/* 1976:     */   }
/* 1977:     */   
/* 1978:     */   public void reloadData()
/* 1979:     */   {
/* 1980:2079 */     if (this.skyblockDataFile == null) {
/* 1981:2080 */       this.skyblockDataFile = new File(getDataFolder(), "skyblockData.yml");
/* 1982:     */     }
/* 1983:2082 */     this.skyblockData = YamlConfiguration.loadConfiguration(this.skyblockDataFile);
/* 1984:     */     
/* 1985:     */ 
/* 1986:2085 */     InputStream defConfigStream = getResource("skyblockData.yml");
/* 1987:2086 */     if (defConfigStream != null)
/* 1988:     */     {
/* 1989:2087 */       YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
/* 1990:2088 */       this.skyblockData.setDefaults(defConfig);
/* 1991:     */     }
/* 1992:     */   }
/* 1993:     */   
/* 1994:     */   public FileConfiguration getData()
/* 1995:     */   {
/* 1996:2093 */     if (this.skyblockData == null) {
/* 1997:2094 */       reloadData();
/* 1998:     */     }
/* 1999:2096 */     return this.skyblockData;
/* 2000:     */   }
/* 2001:     */ }


/* Location:           C:\Pokechu22\TestServer\plugins\uSkyBlock.jar
 * Qualified Name:     us.talabrek.ultimateskyblock.uSkyBlock
 * JD-Core Version:    0.7.0.1
 */