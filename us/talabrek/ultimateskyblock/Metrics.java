/*   1:    */ package us.talabrek.ultimateskyblock;
/*   2:    */ 
/*   3:    */ import java.io.BufferedReader;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStreamReader;
/*   7:    */ import java.io.OutputStreamWriter;
/*   8:    */ import java.io.UnsupportedEncodingException;
/*   9:    */ import java.net.Proxy;
/*  10:    */ import java.net.URL;
/*  11:    */ import java.net.URLConnection;
/*  12:    */ import java.net.URLEncoder;
/*  13:    */ import java.util.UUID;
/*  14:    */ import java.util.logging.Level;
/*  15:    */ import java.util.logging.Logger;
/*  16:    */ import org.bukkit.Bukkit;
/*  17:    */ import org.bukkit.Server;
/*  18:    */ import org.bukkit.configuration.InvalidConfigurationException;
/*  19:    */ import org.bukkit.configuration.file.YamlConfiguration;
/*  20:    */ import org.bukkit.configuration.file.YamlConfigurationOptions;
/*  21:    */ import org.bukkit.plugin.Plugin;
/*  22:    */ import org.bukkit.plugin.PluginDescriptionFile;
/*  23:    */ import org.bukkit.scheduler.BukkitScheduler;
/*  24:    */ import org.bukkit.scheduler.BukkitTask;
/*  25:    */ 
/*  26:    */ public class Metrics
/*  27:    */ {
/*  28:    */   private static final int REVISION = 6;
/*  29:    */   private static final String BASE_URL = "http://mcstats.org";
/*  30:    */   private static final String REPORT_URL = "/report/%s";
/*  31:    */   private static final int PING_INTERVAL = 10;
/*  32:    */   private final Plugin plugin;
/*  33:    */   private final YamlConfiguration configuration;
/*  34:    */   private final File configurationFile;
/*  35:    */   private final String guid;
/*  36:    */   private final boolean debug;
/*  37:101 */   private final Object optOutLock = new Object();
/*  38:106 */   private volatile BukkitTask task = null;
/*  39:    */   
/*  40:    */   public Metrics(Plugin plugin)
/*  41:    */     throws IOException
/*  42:    */   {
/*  43:109 */     if (plugin == null) {
/*  44:110 */       throw new IllegalArgumentException("Plugin cannot be null");
/*  45:    */     }
/*  46:113 */     this.plugin = plugin;
/*  47:    */     
/*  48:    */ 
/*  49:116 */     this.configurationFile = getConfigFile();
/*  50:117 */     this.configuration = YamlConfiguration.loadConfiguration(this.configurationFile);
/*  51:    */     
/*  52:    */ 
/*  53:120 */     this.configuration.addDefault("opt-out", Boolean.valueOf(false));
/*  54:121 */     this.configuration.addDefault("guid", UUID.randomUUID().toString());
/*  55:122 */     this.configuration.addDefault("debug", Boolean.valueOf(false));
/*  56:125 */     if (this.configuration.get("guid", null) == null)
/*  57:    */     {
/*  58:126 */       this.configuration.options().header("http://mcstats.org").copyDefaults(true);
/*  59:127 */       this.configuration.save(this.configurationFile);
/*  60:    */     }
/*  61:131 */     this.guid = this.configuration.getString("guid");
/*  62:132 */     this.debug = this.configuration.getBoolean("debug", false);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public boolean start()
/*  66:    */   {
/*  67:143 */     synchronized (this.optOutLock)
/*  68:    */     {
/*  69:145 */       if (isOptOut()) {
/*  70:146 */         return false;
/*  71:    */       }
/*  72:150 */       if (this.task != null) {
/*  73:151 */         return true;
/*  74:    */       }
/*  75:155 */       this.task = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, new Runnable()
/*  76:    */       {
/*  77:157 */         private boolean firstPost = true;
/*  78:    */         
/*  79:    */         public void run()
/*  80:    */         {
/*  81:    */           try
/*  82:    */           {
/*  83:162 */             synchronized (Metrics.this.optOutLock)
/*  84:    */             {
/*  85:164 */               if ((Metrics.this.isOptOut()) && (Metrics.this.task != null))
/*  86:    */               {
/*  87:165 */                 Metrics.this.task.cancel();
/*  88:166 */                 Metrics.this.task = null;
/*  89:    */               }
/*  90:    */             }
/*  91:173 */             Metrics.this.postPlugin(!this.firstPost);
/*  92:    */             
/*  93:    */ 
/*  94:    */ 
/*  95:177 */             this.firstPost = false;
/*  96:    */           }
/*  97:    */           catch (IOException e)
/*  98:    */           {
/*  99:179 */             if (Metrics.this.debug) {
/* 100:180 */               Bukkit.getLogger().log(Level.INFO, "[Metrics] " + e.getMessage());
/* 101:    */             }
/* 102:    */           }
/* 103:    */         }
/* 104:184 */       }, 0L, 12000L);
/* 105:    */       
/* 106:186 */       return true;
/* 107:    */     }
/* 108:    */   }
/* 109:    */   
/* 110:    */   public boolean isOptOut()
/* 111:    */   {
/* 112:196 */     synchronized (this.optOutLock)
/* 113:    */     {
/* 114:    */       try
/* 115:    */       {
/* 116:199 */         this.configuration.load(getConfigFile());
/* 117:    */       }
/* 118:    */       catch (IOException ex)
/* 119:    */       {
/* 120:201 */         if (this.debug) {
/* 121:202 */           Bukkit.getLogger().log(Level.INFO, "[Metrics] " + ex.getMessage());
/* 122:    */         }
/* 123:204 */         return true;
/* 124:    */       }
/* 125:    */       catch (InvalidConfigurationException ex)
/* 126:    */       {
/* 127:206 */         if (this.debug) {
/* 128:207 */           Bukkit.getLogger().log(Level.INFO, "[Metrics] " + ex.getMessage());
/* 129:    */         }
/* 130:209 */         return true;
/* 131:    */       }
/* 132:211 */       return this.configuration.getBoolean("opt-out", false);
/* 133:    */     }
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void enable()
/* 137:    */     throws IOException
/* 138:    */   {
/* 139:222 */     synchronized (this.optOutLock)
/* 140:    */     {
/* 141:224 */       if (isOptOut())
/* 142:    */       {
/* 143:225 */         this.configuration.set("opt-out", Boolean.valueOf(false));
/* 144:226 */         this.configuration.save(this.configurationFile);
/* 145:    */       }
/* 146:230 */       if (this.task == null) {
/* 147:231 */         start();
/* 148:    */       }
/* 149:    */     }
/* 150:    */   }
/* 151:    */   
/* 152:    */   public void disable()
/* 153:    */     throws IOException
/* 154:    */   {
/* 155:243 */     synchronized (this.optOutLock)
/* 156:    */     {
/* 157:245 */       if (!isOptOut())
/* 158:    */       {
/* 159:246 */         this.configuration.set("opt-out", Boolean.valueOf(true));
/* 160:247 */         this.configuration.save(this.configurationFile);
/* 161:    */       }
/* 162:251 */       if (this.task != null)
/* 163:    */       {
/* 164:252 */         this.task.cancel();
/* 165:253 */         this.task = null;
/* 166:    */       }
/* 167:    */     }
/* 168:    */   }
/* 169:    */   
/* 170:    */   public File getConfigFile()
/* 171:    */   {
/* 172:269 */     File pluginsFolder = this.plugin.getDataFolder().getParentFile();
/* 173:    */     
/* 174:    */ 
/* 175:272 */     return new File(new File(pluginsFolder, "PluginMetrics"), "config.yml");
/* 176:    */   }
/* 177:    */   
/* 178:    */   private void postPlugin(boolean isPing)
/* 179:    */     throws IOException
/* 180:    */   {
/* 181:280 */     PluginDescriptionFile description = this.plugin.getDescription();
/* 182:281 */     String pluginName = description.getName();
/* 183:282 */     boolean onlineMode = Bukkit.getServer().getOnlineMode();
/* 184:283 */     String pluginVersion = description.getVersion();
/* 185:284 */     String serverVersion = Bukkit.getVersion();
/* 186:285 */     int playersOnline = Bukkit.getServer().getOnlinePlayers().length;
/* 187:    */     
/* 188:    */ 
/* 189:    */ 
/* 190:    */ 
/* 191:290 */     StringBuilder data = new StringBuilder();
/* 192:    */     
/* 193:    */ 
/* 194:293 */     data.append(encode("guid")).append('=').append(encode(this.guid));
/* 195:294 */     encodeDataPair(data, "version", pluginVersion);
/* 196:295 */     encodeDataPair(data, "server", serverVersion);
/* 197:296 */     encodeDataPair(data, "players", Integer.toString(playersOnline));
/* 198:297 */     encodeDataPair(data, "revision", String.valueOf(6));
/* 199:    */     
/* 200:    */ 
/* 201:300 */     String osname = System.getProperty("os.name");
/* 202:301 */     String osarch = System.getProperty("os.arch");
/* 203:302 */     String osversion = System.getProperty("os.version");
/* 204:303 */     String java_version = System.getProperty("java.version");
/* 205:304 */     int coreCount = Runtime.getRuntime().availableProcessors();
/* 206:307 */     if (osarch.equals("amd64")) {
/* 207:308 */       osarch = "x86_64";
/* 208:    */     }
/* 209:311 */     encodeDataPair(data, "osname", osname);
/* 210:312 */     encodeDataPair(data, "osarch", osarch);
/* 211:313 */     encodeDataPair(data, "osversion", osversion);
/* 212:314 */     encodeDataPair(data, "cores", Integer.toString(coreCount));
/* 213:315 */     encodeDataPair(data, "online-mode", Boolean.toString(onlineMode));
/* 214:316 */     encodeDataPair(data, "java_version", java_version);
/* 215:319 */     if (isPing) {
/* 216:320 */       encodeDataPair(data, "ping", "true");
/* 217:    */     }
/* 218:324 */     URL url = new URL("http://mcstats.org" + String.format("/report/%s", new Object[] { encode(pluginName) }));
/* 219:    */     URLConnection connection;
/* 220:    */     URLConnection connection;
/* 221:331 */     if (isMineshafterPresent()) {
/* 222:332 */       connection = url.openConnection(Proxy.NO_PROXY);
/* 223:    */     } else {
/* 224:334 */       connection = url.openConnection();
/* 225:    */     }
/* 226:337 */     connection.setDoOutput(true);
/* 227:    */     
/* 228:    */ 
/* 229:340 */     OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
/* 230:341 */     writer.write(data.toString());
/* 231:342 */     writer.flush();
/* 232:    */     
/* 233:    */ 
/* 234:345 */     BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
/* 235:346 */     String response = reader.readLine();
/* 236:    */     
/* 237:    */ 
/* 238:349 */     writer.close();
/* 239:350 */     reader.close();
/* 240:352 */     if ((response == null) || (response.startsWith("ERR"))) {
/* 241:353 */       throw new IOException(response);
/* 242:    */     }
/* 243:    */   }
/* 244:    */   
/* 245:    */   private boolean isMineshafterPresent()
/* 246:    */   {
/* 247:    */     try
/* 248:    */     {
/* 249:364 */       Class.forName("mineshafter.MineServer");
/* 250:365 */       return true;
/* 251:    */     }
/* 252:    */     catch (Exception e) {}
/* 253:367 */     return false;
/* 254:    */   }
/* 255:    */   
/* 256:    */   private static void encodeDataPair(StringBuilder buffer, String key, String value)
/* 257:    */     throws UnsupportedEncodingException
/* 258:    */   {
/* 259:385 */     buffer.append('&').append(encode(key)).append('=').append(encode(value));
/* 260:    */   }
/* 261:    */   
/* 262:    */   private static String encode(String text)
/* 263:    */     throws UnsupportedEncodingException
/* 264:    */   {
/* 265:395 */     return URLEncoder.encode(text, "UTF-8");
/* 266:    */   }
/* 267:    */ }


/* Location:           C:\Pokechu22\TestServer\plugins\uSkyBlock.jar
 * Qualified Name:     us.talabrek.ultimateskyblock.Metrics
 * JD-Core Version:    0.7.0.1
 */