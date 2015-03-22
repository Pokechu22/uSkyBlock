/*   1:    */ package us.talabrek.ultimateskyblock;
/*   2:    */ 
/*   3:    */ import java.io.PrintStream;
/*   4:    */ import java.io.Serializable;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.Iterator;
/*   8:    */ import java.util.List;
/*   9:    */ import java.util.Set;
/*  10:    */ import org.bukkit.Bukkit;
/*  11:    */ import org.bukkit.Location;
/*  12:    */ import org.bukkit.Server;
/*  13:    */ import org.bukkit.World;
/*  14:    */ import org.bukkit.entity.Player;
/*  15:    */ 
/*  16:    */ public class PlayerInfo
/*  17:    */   implements Serializable
/*  18:    */ {
/*  19:    */   private static final long serialVersionUID = 1L;
/*  20:    */   private String playerName;
/*  21:    */   private boolean hasIsland;
/*  22:    */   private boolean hasParty;
/*  23:    */   private boolean warpActive;
/*  24:    */   private List<String> members;
/*  25:    */   private List<String> banned;
/*  26:    */   private String partyLeader;
/*  27:    */   private String partyIslandLocation;
/*  28:    */   private String islandLocation;
/*  29:    */   private String homeLocation;
/*  30:    */   private String warpLocation;
/*  31:    */   private String deathWorld;
/*  32:    */   private HashMap<String, Boolean> challengeList;
/*  33:    */   private float islandExp;
/*  34:    */   private int islandLevel;
/*  35:    */   
/*  36:    */   public PlayerInfo(String playerName)
/*  37:    */   {
/*  38: 37 */     this.playerName = playerName;
/*  39: 38 */     this.members = new ArrayList();
/*  40: 39 */     this.banned = new ArrayList();
/*  41: 40 */     this.hasIsland = false;
/*  42: 41 */     this.warpActive = false;
/*  43: 42 */     this.islandLocation = null;
/*  44: 43 */     this.homeLocation = null;
/*  45: 44 */     this.warpLocation = null;
/*  46: 45 */     this.deathWorld = null;
/*  47: 46 */     this.hasParty = false;
/*  48: 47 */     this.partyLeader = null;
/*  49: 48 */     this.partyIslandLocation = null;
/*  50: 49 */     this.islandExp = 0.0F;
/*  51: 50 */     this.challengeList = new HashMap();
/*  52: 51 */     this.islandLevel = 0;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void startNewIsland(Location l)
/*  56:    */   {
/*  57: 56 */     this.hasIsland = true;
/*  58: 57 */     setIslandLocation(l);
/*  59: 58 */     this.islandLevel = 0;
/*  60: 59 */     this.islandExp = 0.0F;
/*  61: 60 */     this.partyIslandLocation = null;
/*  62: 61 */     this.partyLeader = null;
/*  63: 62 */     this.hasParty = false;
/*  64: 63 */     this.homeLocation = null;
/*  65: 64 */     this.warpLocation = null;
/*  66: 65 */     this.warpActive = false;
/*  67: 66 */     this.members = new ArrayList();
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void removeFromIsland()
/*  71:    */   {
/*  72: 71 */     this.hasIsland = false;
/*  73: 72 */     setIslandLocation(null);
/*  74: 73 */     this.islandLevel = 0;
/*  75: 74 */     this.islandExp = 0.0F;
/*  76: 75 */     this.partyIslandLocation = null;
/*  77: 76 */     this.partyLeader = null;
/*  78: 77 */     this.hasParty = false;
/*  79: 78 */     this.homeLocation = null;
/*  80: 79 */     this.warpLocation = null;
/*  81: 80 */     this.warpActive = false;
/*  82: 81 */     this.members = new ArrayList();
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void setPlayerName(String s)
/*  86:    */   {
/*  87: 85 */     this.playerName = s;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void setPartyIslandLocation(Location l)
/*  91:    */   {
/*  92: 89 */     this.partyIslandLocation = getStringLocation(l);
/*  93:    */   }
/*  94:    */   
/*  95:    */   public Location getPartyIslandLocation()
/*  96:    */   {
/*  97: 93 */     return getLocationString(this.partyIslandLocation);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public Player getPlayer()
/* 101:    */   {
/* 102: 97 */     return Bukkit.getPlayer(this.playerName);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public String getPlayerName()
/* 106:    */   {
/* 107:101 */     return this.playerName;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public void setHasIsland(boolean b)
/* 111:    */   {
/* 112:105 */     this.hasIsland = b;
/* 113:    */   }
/* 114:    */   
/* 115:    */   public void toggleActive()
/* 116:    */   {
/* 117:109 */     if (!this.warpActive) {
/* 118:110 */       this.warpActive = true;
/* 119:    */     } else {
/* 120:112 */       this.warpActive = false;
/* 121:    */     }
/* 122:    */   }
/* 123:    */   
/* 124:    */   public void warpOn()
/* 125:    */   {
/* 126:116 */     this.warpActive = true;
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void warpOff()
/* 130:    */   {
/* 131:120 */     this.warpActive = true;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public boolean getHasIsland()
/* 135:    */   {
/* 136:124 */     return this.hasIsland;
/* 137:    */   }
/* 138:    */   
/* 139:    */   public boolean getWarpActive()
/* 140:    */   {
/* 141:128 */     return this.warpActive;
/* 142:    */   }
/* 143:    */   
/* 144:    */   public String getDeathWorld()
/* 145:    */   {
/* 146:132 */     return this.deathWorld;
/* 147:    */   }
/* 148:    */   
/* 149:    */   public void setDeathWorld(String dw)
/* 150:    */   {
/* 151:136 */     this.deathWorld = dw;
/* 152:    */   }
/* 153:    */   
/* 154:    */   public void setIslandLocation(Location l)
/* 155:    */   {
/* 156:140 */     this.islandLocation = getStringLocation(l);
/* 157:    */   }
/* 158:    */   
/* 159:    */   public Location getIslandLocation()
/* 160:    */   {
/* 161:144 */     return getLocationString(this.islandLocation);
/* 162:    */   }
/* 163:    */   
/* 164:    */   public void setHomeLocation(Location l)
/* 165:    */   {
/* 166:149 */     this.homeLocation = getStringLocation(l);
/* 167:    */   }
/* 168:    */   
/* 169:    */   public void setWarpLocation(Location l)
/* 170:    */   {
/* 171:154 */     this.warpLocation = getStringLocation(l);
/* 172:    */   }
/* 173:    */   
/* 174:    */   public Location getHomeLocation()
/* 175:    */   {
/* 176:159 */     return getLocationString(this.homeLocation);
/* 177:    */   }
/* 178:    */   
/* 179:    */   public Location getWarpLocation()
/* 180:    */   {
/* 181:164 */     return getLocationString(this.warpLocation);
/* 182:    */   }
/* 183:    */   
/* 184:    */   public boolean getHasParty()
/* 185:    */   {
/* 186:169 */     if (this.members == null) {
/* 187:171 */       this.members = new ArrayList();
/* 188:    */     }
/* 189:173 */     return this.hasParty;
/* 190:    */   }
/* 191:    */   
/* 192:    */   public void setJoinParty(String leader, Location l)
/* 193:    */   {
/* 194:178 */     this.hasParty = true;
/* 195:179 */     this.partyLeader = leader;
/* 196:180 */     this.partyIslandLocation = getStringLocation(l);
/* 197:    */   }
/* 198:    */   
/* 199:    */   public void setLeaveParty()
/* 200:    */   {
/* 201:185 */     this.hasParty = false;
/* 202:186 */     this.partyLeader = null;
/* 203:187 */     this.islandLevel = 0;
/* 204:188 */     this.partyIslandLocation = null;
/* 205:189 */     this.members = new ArrayList();
/* 206:    */   }
/* 207:    */   
/* 208:    */   public List<String> getMembers()
/* 209:    */   {
/* 210:194 */     return this.members;
/* 211:    */   }
/* 212:    */   
/* 213:    */   public List<String> getBanned()
/* 214:    */   {
/* 215:199 */     if (this.banned == null) {
/* 216:200 */       this.banned = new ArrayList();
/* 217:    */     }
/* 218:201 */     return this.banned;
/* 219:    */   }
/* 220:    */   
/* 221:    */   public void addBan(String player)
/* 222:    */   {
/* 223:206 */     if (this.banned == null) {
/* 224:207 */       this.banned = new ArrayList();
/* 225:    */     }
/* 226:208 */     this.banned.add(player);
/* 227:    */   }
/* 228:    */   
/* 229:    */   public void removeBan(String player)
/* 230:    */   {
/* 231:213 */     if (this.banned == null) {
/* 232:214 */       this.banned = new ArrayList();
/* 233:    */     }
/* 234:215 */     this.banned.remove(player);
/* 235:    */   }
/* 236:    */   
/* 237:    */   public boolean isBanned(String player)
/* 238:    */   {
/* 239:220 */     if (this.banned == null) {
/* 240:221 */       this.banned = new ArrayList();
/* 241:    */     }
/* 242:222 */     return this.banned.contains(player);
/* 243:    */   }
/* 244:    */   
/* 245:    */   public String getPartyLeader()
/* 246:    */   {
/* 247:226 */     return this.partyLeader;
/* 248:    */   }
/* 249:    */   
/* 250:    */   public void setPartyLeader(String leader)
/* 251:    */   {
/* 252:230 */     this.partyLeader = leader;
/* 253:    */   }
/* 254:    */   
/* 255:    */   public void setMembers(List<String> newMembers)
/* 256:    */   {
/* 257:234 */     this.members = newMembers;
/* 258:    */   }
/* 259:    */   
/* 260:    */   public void addMember(String member)
/* 261:    */   {
/* 262:239 */     this.members.add(member);
/* 263:    */   }
/* 264:    */   
/* 265:    */   public void removeMember(String member)
/* 266:    */   {
/* 267:244 */     this.members.remove(member);
/* 268:    */   }
/* 269:    */   
/* 270:    */   public void setIslandExp(float i)
/* 271:    */   {
/* 272:248 */     this.islandExp = i;
/* 273:    */   }
/* 274:    */   
/* 275:    */   public float getIslandExp()
/* 276:    */   {
/* 277:252 */     return this.islandExp;
/* 278:    */   }
/* 279:    */   
/* 280:    */   public void setIslandLevel(int i)
/* 281:    */   {
/* 282:256 */     this.islandLevel = i;
/* 283:    */   }
/* 284:    */   
/* 285:    */   public int getIslandLevel()
/* 286:    */   {
/* 287:260 */     return this.islandLevel;
/* 288:    */   }
/* 289:    */   
/* 290:    */   public void ListData()
/* 291:    */   {
/* 292:264 */     System.out.print("Player: " + getPlayerName());
/* 293:265 */     System.out.print("Has an island?: " + getHasIsland());
/* 294:266 */     System.out.print("Has a party?: " + getHasParty());
/* 295:267 */     if (getHasIsland()) {
/* 296:268 */       System.out.print("Island Location: " + getStringLocation(getIslandLocation()));
/* 297:    */     }
/* 298:269 */     if ((getHasParty()) && 
/* 299:270 */       (getPartyIslandLocation() != null)) {
/* 300:271 */       System.out.print("Island Location (party): " + getStringLocation(getPartyIslandLocation()));
/* 301:    */     }
/* 302:272 */     System.out.print("Island Level: " + this.islandLevel);
/* 303:    */   }
/* 304:    */   
/* 305:    */   private Location getLocationString(String s)
/* 306:    */   {
/* 307:276 */     if ((s == null) || (s.trim() == "")) {
/* 308:277 */       return null;
/* 309:    */     }
/* 310:279 */     String[] parts = s.split(":");
/* 311:280 */     if (parts.length == 4)
/* 312:    */     {
/* 313:281 */       World w = Bukkit.getServer().getWorld(parts[0]);
/* 314:282 */       int x = Integer.parseInt(parts[1]);
/* 315:283 */       int y = Integer.parseInt(parts[2]);
/* 316:284 */       int z = Integer.parseInt(parts[3]);
/* 317:285 */       return new Location(w, x, y, z);
/* 318:    */     }
/* 319:287 */     return null;
/* 320:    */   }
/* 321:    */   
/* 322:    */   public void completeChallenge(String challenge)
/* 323:    */   {
/* 324:292 */     if (this.challengeList.containsKey(challenge))
/* 325:    */     {
/* 326:294 */       this.challengeList.remove(challenge);
/* 327:295 */       this.challengeList.put(challenge, Boolean.valueOf(true));
/* 328:    */     }
/* 329:    */   }
/* 330:    */   
/* 331:    */   public void resetChallenge(String challenge)
/* 332:    */   {
/* 333:301 */     if (this.challengeList.containsKey(challenge))
/* 334:    */     {
/* 335:303 */       this.challengeList.remove(challenge);
/* 336:304 */       this.challengeList.put(challenge, Boolean.valueOf(false));
/* 337:    */     }
/* 338:    */   }
/* 339:    */   
/* 340:    */   public boolean checkChallenge(String challenge)
/* 341:    */   {
/* 342:310 */     if (this.challengeList.containsKey(challenge.toLowerCase())) {
/* 343:312 */       return ((Boolean)this.challengeList.get(challenge.toLowerCase())).booleanValue();
/* 344:    */     }
/* 345:315 */     return false;
/* 346:    */   }
/* 347:    */   
/* 348:    */   public boolean challengeExists(String challenge)
/* 349:    */   {
/* 350:320 */     if (this.challengeList.containsKey(challenge.toLowerCase())) {
/* 351:322 */       return true;
/* 352:    */     }
/* 353:324 */     return false;
/* 354:    */   }
/* 355:    */   
/* 356:    */   public void resetAllChallenges()
/* 357:    */   {
/* 358:329 */     this.challengeList = null;
/* 359:330 */     buildChallengeList();
/* 360:    */   }
/* 361:    */   
/* 362:    */   public void buildChallengeList()
/* 363:    */   {
/* 364:335 */     if (this.challengeList == null) {
/* 365:336 */       this.challengeList = new HashMap();
/* 366:    */     }
/* 367:337 */     Iterator<String> itr = Settings.challenges_challengeList.iterator();
/* 368:338 */     while (itr.hasNext())
/* 369:    */     {
/* 370:340 */       String current = (String)itr.next();
/* 371:341 */       if (!this.challengeList.containsKey(current.toLowerCase())) {
/* 372:342 */         this.challengeList.put(current.toLowerCase(), Boolean.valueOf(false));
/* 373:    */       }
/* 374:    */     }
/* 375:344 */     if (this.challengeList.size() > Settings.challenges_challengeList.size())
/* 376:    */     {
/* 377:346 */       Object[] challengeArray = this.challengeList.keySet().toArray();
/* 378:347 */       for (int i = 0; i < challengeArray.length; i++) {
/* 379:349 */         if (!Settings.challenges_challengeList.contains(challengeArray[i].toString())) {
/* 380:351 */           this.challengeList.remove(challengeArray[i].toString());
/* 381:    */         }
/* 382:    */       }
/* 383:    */     }
/* 384:    */   }
/* 385:    */   
/* 386:    */   public void displayChallengeList()
/* 387:    */   {
/* 388:360 */     Iterator<String> itr = this.challengeList.keySet().iterator();
/* 389:361 */     System.out.print("Displaying Challenge list for " + this.playerName);
/* 390:362 */     while (itr.hasNext())
/* 391:    */     {
/* 392:364 */       String current = (String)itr.next();
/* 393:365 */       System.out.print(current + ": " + this.challengeList.get(current));
/* 394:    */     }
/* 395:    */   }
/* 396:    */   
/* 397:    */   private String getStringLocation(Location l)
/* 398:    */   {
/* 399:370 */     if (l == null) {
/* 400:371 */       return "";
/* 401:    */     }
/* 402:373 */     return l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
/* 403:    */   }
/* 404:    */ }


/* Location:           C:\Pokechu22\TestServer\plugins\uSkyBlock.jar
 * Qualified Name:     us.talabrek.ultimateskyblock.PlayerInfo
 * JD-Core Version:    0.7.0.1
 */