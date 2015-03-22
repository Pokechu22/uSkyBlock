/*  1:   */ package us.talabrek.ultimateskyblock;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import java.util.logging.Logger;
/*  5:   */ import org.bukkit.enchantments.Enchantment;
/*  6:   */ import org.bukkit.inventory.ItemStack;
/*  7:   */ import org.bukkit.material.MaterialData;
/*  8:   */ 
/*  9:   */ public class ItemParser
/* 10:   */ {
/* 11:   */   public static String parseItemStackToString(ItemStack item)
/* 12:   */   {
/* 13: 9 */     if (item == null) {
/* 14:10 */       return "";
/* 15:   */     }
/* 16:14 */     String s = "";
/* 17:15 */     s = s + "id:" + item.getTypeId() + ";";
/* 18:16 */     s = s + "amount:" + item.getAmount() + ";";
/* 19:17 */     s = s + "durab:" + item.getDurability() + ";";
/* 20:18 */     s = s + "data:" + item.getData().getData() + ";";
/* 21:20 */     if (item.getEnchantments().size() > 0)
/* 22:   */     {
/* 23:21 */       s = s + "ench:";
/* 24:22 */       for (Enchantment e : item.getEnchantments().keySet())
/* 25:   */       {
/* 26:23 */         s = s + "eid#" + e.getId() + " ";
/* 27:24 */         s = s + "elevel#" + item.getEnchantments().get(e) + " ";
/* 28:   */       }
/* 29:   */     }
/* 30:27 */     return s.trim();
/* 31:   */   }
/* 32:   */   
/* 33:   */   public static ItemStack getItemStackfromString(String s)
/* 34:   */   {
/* 35:31 */     if (s.equalsIgnoreCase("")) {
/* 36:32 */       return null;
/* 37:   */     }
/* 38:34 */     ItemStack x = new ItemStack(1);
/* 39:36 */     for (String thing : s.split(";"))
/* 40:   */     {
/* 41:37 */       String[] sp = thing.split(":");
/* 42:38 */       if (sp.length != 2) {
/* 43:39 */         uSkyBlock.getInstance().log.warning("error, wrong type size");
/* 44:   */       }
/* 45:40 */       String name = sp[0];
/* 46:42 */       if (name.equals("id"))
/* 47:   */       {
/* 48:43 */         x.setTypeId(Integer.parseInt(sp[1]));
/* 49:   */       }
/* 50:44 */       else if (name.equals("amount"))
/* 51:   */       {
/* 52:45 */         x.setAmount(Integer.parseInt(sp[1]));
/* 53:   */       }
/* 54:46 */       else if (name.equals("durab"))
/* 55:   */       {
/* 56:47 */         x.setDurability((short)Integer.parseInt(sp[1]));
/* 57:   */       }
/* 58:48 */       else if (name.equals("data"))
/* 59:   */       {
/* 60:49 */         x.getData().setData((byte)Integer.parseInt(sp[1]));
/* 61:   */       }
/* 62:50 */       else if (name.equals("ench"))
/* 63:   */       {
/* 64:51 */         int enchId = 0;
/* 65:52 */         int level = 0;
/* 66:53 */         for (String enchantment : sp[1].split(" "))
/* 67:   */         {
/* 68:54 */           String[] prop = enchantment.split("#");
/* 69:55 */           if (prop.length != 2) {
/* 70:56 */             uSkyBlock.getInstance().log.warning("error, wrong enchantmenttype length");
/* 71:   */           }
/* 72:57 */           if (prop[0].equals("eid"))
/* 73:   */           {
/* 74:58 */             enchId = Integer.parseInt(prop[1]);
/* 75:   */           }
/* 76:59 */           else if (prop[0].equals("elevel"))
/* 77:   */           {
/* 78:60 */             level = Integer.parseInt(prop[1]);
/* 79:61 */             x.addUnsafeEnchantment(Enchantment.getById(enchId), level);
/* 80:   */           }
/* 81:   */         }
/* 82:   */       }
/* 83:   */       else
/* 84:   */       {
/* 85:66 */         uSkyBlock.getInstance().log.warning("error, unknown itemvalue");
/* 86:   */       }
/* 87:   */     }
/* 88:69 */     return x;
/* 89:   */   }
/* 90:   */ }


/* Location:           C:\Pokechu22\TestServer\plugins\uSkyBlock.jar
 * Qualified Name:     us.talabrek.ultimateskyblock.ItemParser
 * JD-Core Version:    0.7.0.1
 */