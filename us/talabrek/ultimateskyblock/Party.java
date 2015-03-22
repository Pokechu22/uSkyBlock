/*   1:    */ package us.talabrek.ultimateskyblock;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.List;
/*   6:    */ 
/*   7:    */ public class Party
/*   8:    */   implements Serializable
/*   9:    */ {
/*  10:    */   private static final long serialVersionUID = 7L;
/*  11:    */   private String pLeader;
/*  12:    */   private SerializableLocation pIsland;
/*  13:    */   private int pSize;
/*  14:    */   private List<String> members;
/*  15:    */   
/*  16:    */   public Party(String leader, String member2, SerializableLocation island)
/*  17:    */   {
/*  18: 16 */     this.pLeader = leader;
/*  19: 17 */     this.pSize = 2;
/*  20: 18 */     this.pIsland = island;
/*  21: 19 */     this.members = new ArrayList();
/*  22: 20 */     this.members.add(leader);
/*  23: 21 */     this.members.add(member2);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public String getLeader()
/*  27:    */   {
/*  28: 25 */     return this.pLeader;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public SerializableLocation getIsland()
/*  32:    */   {
/*  33: 29 */     return this.pIsland;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public int getSize()
/*  37:    */   {
/*  38: 33 */     return this.pSize;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public boolean hasMember(String player)
/*  42:    */   {
/*  43: 39 */     if (this.members.contains(player.toLowerCase())) {
/*  44: 40 */       return true;
/*  45:    */     }
/*  46: 41 */     if (this.members.contains(player)) {
/*  47: 42 */       return true;
/*  48:    */     }
/*  49: 43 */     if (this.pLeader.equalsIgnoreCase(player)) {
/*  50: 44 */       return true;
/*  51:    */     }
/*  52: 45 */     return false;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public List<String> getMembers()
/*  56:    */   {
/*  57: 50 */     List<String> onlyMembers = this.members;
/*  58: 51 */     onlyMembers.remove(this.pLeader);
/*  59: 52 */     return onlyMembers;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public boolean changeLeader(String oLeader, String nLeader)
/*  63:    */   {
/*  64: 57 */     if (oLeader.equalsIgnoreCase(this.pLeader)) {
/*  65: 59 */       if ((this.members.contains(nLeader)) && (!oLeader.equalsIgnoreCase(nLeader)))
/*  66:    */       {
/*  67: 61 */         this.pLeader = nLeader;
/*  68: 62 */         this.members.remove(oLeader);
/*  69: 63 */         this.members.add(oLeader);
/*  70: 64 */         return true;
/*  71:    */       }
/*  72:    */     }
/*  73: 67 */     return false;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public int getMax()
/*  77:    */   {
/*  78: 72 */     if (VaultHandler.checkPerk(this.pLeader, "usb.extra.partysize", uSkyBlock.getSkyBlockWorld())) {
/*  79: 74 */       return Settings.general_maxPartySize * 2;
/*  80:    */     }
/*  81: 76 */     return Settings.general_maxPartySize;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public boolean addMember(String nMember)
/*  85:    */   {
/*  86: 81 */     if (VaultHandler.checkPerk(this.pLeader, "usb.extra.partysize", uSkyBlock.getSkyBlockWorld()))
/*  87:    */     {
/*  88: 83 */       if ((!this.members.contains(nMember)) && (getSize() < Settings.general_maxPartySize * 2))
/*  89:    */       {
/*  90: 85 */         this.members.add(nMember);
/*  91: 86 */         this.pSize += 1;
/*  92: 87 */         return true;
/*  93:    */       }
/*  94: 89 */       return false;
/*  95:    */     }
/*  96: 92 */     if ((!this.members.contains(nMember)) && (getSize() < Settings.general_maxPartySize))
/*  97:    */     {
/*  98: 94 */       this.members.add(nMember);
/*  99: 95 */       this.pSize += 1;
/* 100: 96 */       return true;
/* 101:    */     }
/* 102: 98 */     return false;
/* 103:    */   }
/* 104:    */   
/* 105:    */   public int removeMember(String oMember)
/* 106:    */   {
/* 107:104 */     if (oMember.equalsIgnoreCase(this.pLeader)) {
/* 108:106 */       return 0;
/* 109:    */     }
/* 110:108 */     if (this.members.contains(oMember))
/* 111:    */     {
/* 112:110 */       this.pSize -= 1;
/* 113:111 */       this.members.remove(oMember);
/* 114:112 */       return 2;
/* 115:    */     }
/* 116:114 */     return 1;
/* 117:    */   }
/* 118:    */ }


/* Location:           C:\Pokechu22\TestServer\plugins\uSkyBlock.jar
 * Qualified Name:     us.talabrek.ultimateskyblock.Party
 * JD-Core Version:    0.7.0.1
 */