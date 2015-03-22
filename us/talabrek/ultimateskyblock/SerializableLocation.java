/*  1:   */ package us.talabrek.ultimateskyblock;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import org.bukkit.Bukkit;
/*  5:   */ import org.bukkit.Location;
/*  6:   */ import org.bukkit.World;
/*  7:   */ 
/*  8:   */ public class SerializableLocation
/*  9:   */   implements Serializable
/* 10:   */ {
/* 11:   */   private static final long serialVersionUID = 23L;
/* 12:   */   private double x;
/* 13:   */   private double y;
/* 14:   */   private double z;
/* 15:   */   private String world;
/* 16:   */   
/* 17:   */   public SerializableLocation(Location loc)
/* 18:   */   {
/* 19:12 */     this.x = loc.getX();
/* 20:13 */     this.y = loc.getY();
/* 21:14 */     this.z = loc.getZ();
/* 22:15 */     this.world = loc.getWorld().getName();
/* 23:   */   }
/* 24:   */   
/* 25:   */   public Location getLocation()
/* 26:   */   {
/* 27:18 */     World w = Bukkit.getWorld(this.world);
/* 28:19 */     if (w == null) {
/* 29:20 */       return null;
/* 30:   */     }
/* 31:21 */     Location toRet = new Location(w, this.x, this.y, this.z);
/* 32:22 */     return toRet;
/* 33:   */   }
/* 34:   */ }


/* Location:           C:\Pokechu22\TestServer\plugins\uSkyBlock.jar
 * Qualified Name:     us.talabrek.ultimateskyblock.SerializableLocation
 * JD-Core Version:    0.7.0.1
 */