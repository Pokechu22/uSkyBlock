/*   1:    */ package us.talabrek.ultimateskyblock;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import net.milkbowl.vault.economy.Economy;
/*   5:    */ import org.bukkit.ChatColor;
/*   6:    */ import org.bukkit.Server;
/*   7:    */ import org.bukkit.World;
/*   8:    */ import org.bukkit.command.Command;
/*   9:    */ import org.bukkit.command.CommandExecutor;
/*  10:    */ import org.bukkit.command.CommandSender;
/*  11:    */ import org.bukkit.configuration.file.FileConfiguration;
/*  12:    */ import org.bukkit.entity.Player;
/*  13:    */ 
/*  14:    */ public class ChallengesCommand
/*  15:    */   implements CommandExecutor
/*  16:    */ {
/*  17:    */   public boolean onCommand(CommandSender sender, Command command, String label, String[] split)
/*  18:    */   {
/*  19: 17 */     if (!(sender instanceof Player)) {
/*  20: 18 */       return false;
/*  21:    */     }
/*  22: 21 */     Player player = sender.getServer().getPlayer(sender.getName());
/*  23: 22 */     if (!Settings.challenges_allowChallenges) {
/*  24: 24 */       return true;
/*  25:    */     }
/*  26: 26 */     if ((!VaultHandler.checkPerk(player.getName(), "usb.island.challenges", player.getWorld())) && (!player.isOp()))
/*  27:    */     {
/*  28: 27 */       player.sendMessage(ChatColor.RED + "You don't have access to this command!");
/*  29: 28 */       return true;
/*  30:    */     }
/*  31: 30 */     if (!player.getWorld().getName().equalsIgnoreCase(Settings.general_worldName))
/*  32:    */     {
/*  33: 31 */       player.sendMessage(ChatColor.RED + "You can only submit challenges in the skyblock world!");
/*  34: 32 */       return true;
/*  35:    */     }
/*  36: 35 */     if (split.length == 0)
/*  37:    */     {
/*  38: 36 */       int rankComplete = 0;
/*  39: 37 */       sender.sendMessage(ChatColor.GOLD + Settings.challenges_ranks[0] + ": " + uSkyBlock.getInstance().getChallengesFromRank(player, Settings.challenges_ranks[0]));
/*  40: 38 */       for (int i = 1; i < Settings.challenges_ranks.length; i++)
/*  41:    */       {
/*  42: 40 */         rankComplete = uSkyBlock.getInstance().checkRankCompletion(player, Settings.challenges_ranks[(i - 1)]);
/*  43: 41 */         if (rankComplete <= 0) {
/*  44: 43 */           sender.sendMessage(ChatColor.GOLD + Settings.challenges_ranks[i] + ": " + uSkyBlock.getInstance().getChallengesFromRank(player, Settings.challenges_ranks[i]));
/*  45:    */         } else {
/*  46: 46 */           sender.sendMessage(ChatColor.GOLD + Settings.challenges_ranks[i] + ChatColor.GRAY + ": Complete " + rankComplete + " more " + Settings.challenges_ranks[(i - 1)] + " challenges to unlock this rank!");
/*  47:    */         }
/*  48:    */       }
/*  49: 49 */       sender.sendMessage(ChatColor.YELLOW + "Use /c <name> to view information about a challenge.");
/*  50: 50 */       sender.sendMessage(ChatColor.YELLOW + "Use /c complete <name> to attempt to complete that challenge.");
/*  51:    */     }
/*  52: 51 */     else if (split.length == 1)
/*  53:    */     {
/*  54: 53 */       if ((split[0].equalsIgnoreCase("help")) || (split[0].equalsIgnoreCase("complete")) || (split[0].equalsIgnoreCase("c")))
/*  55:    */       {
/*  56: 55 */         sender.sendMessage(ChatColor.YELLOW + "Use /c <name> to view information about a challenge.");
/*  57: 56 */         sender.sendMessage(ChatColor.YELLOW + "Use /c complete <name> to attempt to complete that challenge.");
/*  58: 57 */         sender.sendMessage(ChatColor.YELLOW + "Challenges will have different colors depending on if they are:");
/*  59: 58 */         sender.sendMessage(Settings.challenges_challengeColor.replace('&', '§') + "Incomplete " + Settings.challenges_finishedColor.replace('&', '§') + "Completed(not repeatable) " + Settings.challenges_repeatableColor.replace('&', '§') + "Completed(repeatable) ");
/*  60:    */       }
/*  61: 59 */       else if (uSkyBlock.getInstance().isRankAvailable(player, uSkyBlock.getInstance().getConfig().getString("options.challenges.challengeList." + split[0].toLowerCase() + ".rankLevel")))
/*  62:    */       {
/*  63: 61 */         sender.sendMessage(ChatColor.YELLOW + "Challenge Name: " + ChatColor.WHITE + split[0].toLowerCase());
/*  64: 62 */         sender.sendMessage(ChatColor.YELLOW + uSkyBlock.getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".description").toString()));
/*  65: 63 */         if (uSkyBlock.getInstance().getConfig().getString("options.challenges.challengeList." + split[0].toLowerCase() + ".type").equalsIgnoreCase("onPlayer"))
/*  66:    */         {
/*  67: 65 */           if (uSkyBlock.getInstance().getConfig().getBoolean("options.challenges.challengeList." + split[0].toLowerCase() + ".takeItems")) {
/*  68: 67 */             sender.sendMessage(ChatColor.RED + "You will lose all required items when you complete this challenge!");
/*  69:    */           }
/*  70:    */         }
/*  71: 69 */         else if (uSkyBlock.getInstance().getConfig().getString("options.challenges.challengeList." + split[0].toLowerCase() + ".type").equalsIgnoreCase("onIsland")) {
/*  72: 71 */           sender.sendMessage(ChatColor.RED + "All required items must be placed on your island!");
/*  73:    */         }
/*  74: 74 */         if (Settings.challenges_ranks.length > 1) {
/*  75: 76 */           sender.sendMessage(ChatColor.YELLOW + "Rank: " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".rankLevel").toString()));
/*  76:    */         }
/*  77: 78 */         if ((((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player.getName())).checkChallenge(split[0].toLowerCase())) && ((!uSkyBlock.getInstance().getConfig().getString("options.challenges.challengeList." + split[0].toLowerCase() + ".type").equalsIgnoreCase("onPlayer")) || (!uSkyBlock.getInstance().getConfig().getBoolean("options.challenges.challengeList." + split[0].toLowerCase() + ".repeatable"))))
/*  78:    */         {
/*  79: 80 */           sender.sendMessage(ChatColor.RED + "This Challenge is not repeatable!");
/*  80: 81 */           return true;
/*  81:    */         }
/*  82: 83 */         if ((Settings.challenges_enableEconomyPlugin) && (VaultHandler.econ != null))
/*  83:    */         {
/*  84: 85 */           if (((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player.getName())).checkChallenge(split[0].toLowerCase()))
/*  85:    */           {
/*  86: 87 */             sender.sendMessage(ChatColor.YELLOW + "Repeat reward(s): " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".repeatRewardText").toString()).replace('&', '§'));
/*  87: 88 */             player.sendMessage(ChatColor.YELLOW + "Repeat exp reward: " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".repeatXpReward").toString()));
/*  88: 89 */             sender.sendMessage(ChatColor.YELLOW + "Repeat currency reward: " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".repeatCurrencyReward").toString()) + " " + VaultHandler.econ.currencyNamePlural());
/*  89:    */           }
/*  90:    */           else
/*  91:    */           {
/*  92: 92 */             sender.sendMessage(ChatColor.YELLOW + "Reward(s): " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".rewardText").toString()).replace('&', '§'));
/*  93: 93 */             player.sendMessage(ChatColor.YELLOW + "Exp reward: " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".xpReward").toString()));
/*  94: 94 */             sender.sendMessage(ChatColor.YELLOW + "Currency reward: " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".currencyReward").toString()) + " " + VaultHandler.econ.currencyNamePlural());
/*  95:    */           }
/*  96:    */         }
/*  97: 98 */         else if (((PlayerInfo)uSkyBlock.getInstance().getActivePlayers().get(player.getName())).checkChallenge(split[0].toLowerCase()))
/*  98:    */         {
/*  99:100 */           sender.sendMessage(ChatColor.YELLOW + "Repeat reward(s): " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".repeatRewardText").toString()).replace('&', '§'));
/* 100:101 */           player.sendMessage(ChatColor.YELLOW + "Repeat exp reward: " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".repeatXpReward").toString()));
/* 101:    */         }
/* 102:    */         else
/* 103:    */         {
/* 104:104 */           sender.sendMessage(ChatColor.YELLOW + "Reward(s): " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getString(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".rewardText").toString()).replace('&', '§'));
/* 105:105 */           player.sendMessage(ChatColor.YELLOW + "Exp reward: " + ChatColor.WHITE + uSkyBlock.getInstance().getConfig().getInt(new StringBuilder("options.challenges.challengeList.").append(split[0].toLowerCase()).append(".xpReward").toString()));
/* 106:    */         }
/* 107:108 */         sender.sendMessage(ChatColor.YELLOW + "To complete this challenge, use " + ChatColor.WHITE + "/c c " + split[0].toLowerCase());
/* 108:    */       }
/* 109:    */       else
/* 110:    */       {
/* 111:111 */         sender.sendMessage(ChatColor.RED + "Invalid challenge name! Use /c help for more information");
/* 112:    */       }
/* 113:    */     }
/* 114:113 */     else if (split.length == 2)
/* 115:    */     {
/* 116:115 */       if ((split[0].equalsIgnoreCase("complete")) || (split[0].equalsIgnoreCase("c"))) {
/* 117:117 */         if (uSkyBlock.getInstance().checkIfCanCompleteChallenge(player, split[1].toLowerCase())) {
/* 118:119 */           uSkyBlock.getInstance().giveReward(player, split[1].toLowerCase());
/* 119:    */         }
/* 120:    */       }
/* 121:    */     }
/* 122:123 */     return true;
/* 123:    */   }
/* 124:    */ }


/* Location:           C:\Pokechu22\TestServer\plugins\uSkyBlock.jar
 * Qualified Name:     us.talabrek.ultimateskyblock.ChallengesCommand
 * JD-Core Version:    0.7.0.1
 */