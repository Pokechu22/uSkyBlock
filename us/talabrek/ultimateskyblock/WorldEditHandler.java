/*  1:   */ package us.talabrek.ultimateskyblock;
/*  2:   */ 
/*  3:   */ import com.sk89q.worldedit.CuboidClipboard;
/*  4:   */ import com.sk89q.worldedit.EditSession;
/*  5:   */ import com.sk89q.worldedit.MaxChangedBlocksException;
/*  6:   */ import com.sk89q.worldedit.Vector;
/*  7:   */ import com.sk89q.worldedit.bukkit.BukkitWorld;
/*  8:   */ import com.sk89q.worldedit.bukkit.WorldEditPlugin;
/*  9:   */ import com.sk89q.worldedit.data.DataException;
/* 10:   */ import com.sk89q.worldedit.schematic.SchematicFormat;
/* 11:   */ import java.io.File;
/* 12:   */ import java.io.IOException;
/* 13:   */ import org.bukkit.Location;
/* 14:   */ import org.bukkit.Server;
/* 15:   */ import org.bukkit.World;
/* 16:   */ import org.bukkit.plugin.Plugin;
/* 17:   */ import org.bukkit.plugin.PluginManager;
/* 18:   */ 
/* 19:   */ public class WorldEditHandler
/* 20:   */ {
/* 21:   */   public static WorldEditPlugin getWorldEdit()
/* 22:   */   {
/* 23:22 */     Plugin plugin = uSkyBlock.getInstance().getServer().getPluginManager().getPlugin("WorldEdit");
/* 24:25 */     if ((plugin == null) || (!(plugin instanceof WorldEditPlugin))) {
/* 25:26 */       return null;
/* 26:   */     }
/* 27:29 */     return (WorldEditPlugin)plugin;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public static boolean loadIslandSchematic(World world, File file, Location origin)
/* 31:   */     throws DataException, IOException, MaxChangedBlocksException
/* 32:   */   {
/* 33:33 */     Vector v = new Vector(origin.getBlockX(), origin.getBlockY(), origin.getBlockZ());
/* 34:34 */     SchematicFormat format = SchematicFormat.getFormat(file);
/* 35:35 */     if (format == null) {
/* 36:37 */       return false;
/* 37:   */     }
/* 38:39 */     EditSession es = new EditSession(new BukkitWorld(world), 999999999);
/* 39:40 */     CuboidClipboard cc = format.load(file);
/* 40:41 */     cc.paste(es, v, false);
/* 41:42 */     return true;
/* 42:   */   }
/* 43:   */ }


/* Location:           C:\Pokechu22\TestServer\plugins\uSkyBlock.jar
 * Qualified Name:     us.talabrek.ultimateskyblock.WorldEditHandler
 * JD-Core Version:    0.7.0.1
 */