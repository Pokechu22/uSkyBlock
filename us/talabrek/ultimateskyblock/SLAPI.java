/*  1:   */ package us.talabrek.ultimateskyblock;
/*  2:   */ 
/*  3:   */ import java.io.File;
/*  4:   */ import java.io.FileInputStream;
/*  5:   */ import java.io.FileOutputStream;
/*  6:   */ import java.io.ObjectInputStream;
/*  7:   */ import java.io.ObjectOutputStream;
/*  8:   */ 
/*  9:   */ public class SLAPI
/* 10:   */ {
/* 11:   */   public static void save(Object obj, File path)
/* 12:   */     throws Exception
/* 13:   */   {
/* 14:17 */     ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
/* 15:18 */     oos.writeObject(obj);
/* 16:19 */     oos.flush();
/* 17:20 */     oos.close();
/* 18:   */   }
/* 19:   */   
/* 20:   */   public static Object load(File path)
/* 21:   */     throws Exception
/* 22:   */   {
/* 23:24 */     ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
/* 24:25 */     Object result = ois.readObject();
/* 25:26 */     ois.close();
/* 26:27 */     return result;
/* 27:   */   }
/* 28:   */ }


/* Location:           C:\Pokechu22\TestServer\plugins\uSkyBlock.jar
 * Qualified Name:     us.talabrek.ultimateskyblock.SLAPI
 * JD-Core Version:    0.7.0.1
 */