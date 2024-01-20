package lc.chg.game.timers;

import lc.chg.LCCHG;
import org.bukkit.Bukkit;

public class PreGameTimer {
    private static Integer shed_id = null;

    public static boolean started = false;

    public PreGameTimer() {
        started = true;
        shed_id = Integer.valueOf(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(LCCHG.instance, new Runnable() {
            public void run() {
                // Byte code:
                //   0: getstatic main/BGMain.COUNTDOWN : Ljava/lang/Integer;
                //   3: invokevirtual intValue : ()I
                //   6: ifle -> 384
                //   9: getstatic main/BGMain.COUNTDOWN : Ljava/lang/Integer;
                //   12: invokevirtual intValue : ()I
                //   15: bipush #30
                //   17: if_icmplt -> 24
                //   20: iconst_1
                //   21: goto -> 25
                //   24: iconst_0
                //   25: getstatic main/BGMain.COUNTDOWN : Ljava/lang/Integer;
                //   28: invokevirtual intValue : ()I
                //   31: bipush #10
                //   33: irem
                //   34: ifne -> 41
                //   37: iconst_1
                //   38: goto -> 42
                //   41: iconst_0
                //   42: iand
                //   43: ifeq -> 149
                //   46: new java/lang/StringBuilder
                //   49: dup
                //   50: ldc 'El juego comienza en '
                //   52: invokespecial <init> : (Ljava/lang/String;)V
                //   55: getstatic main/BGMain.COUNTDOWN : Ljava/lang/Integer;
                //   58: invokestatic TIME : (Ljava/lang/Integer;)Ljava/lang/String;
                //   61: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
                //   64: ldc '.'
                //   66: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
                //   69: invokevirtual toString : ()Ljava/lang/String;
                //   72: invokestatic printTimeChat : (Ljava/lang/String;)V
                //   75: invokestatic getGamers : ()Ljava/util/LinkedList;
                //   78: invokevirtual iterator : ()Ljava/util/Iterator;
                //   81: astore_2
                //   82: goto -> 137
                //   85: aload_2
                //   86: invokeinterface next : ()Ljava/lang/Object;
                //   91: checkcast org/bukkit/entity/Player
                //   94: astore_1
                //   95: invokestatic getGamers : ()Ljava/util/LinkedList;
                //   98: invokevirtual size : ()I
                //   101: invokestatic getServer : ()Lorg/bukkit/Server;
                //   104: invokeinterface getMaxPlayers : ()I
                //   109: if_icmplt -> 137
                //   112: bipush #26
                //   114: invokestatic valueOf : (I)Ljava/lang/Integer;
                //   117: putstatic main/BGMain.COUNTDOWN : Ljava/lang/Integer;
                //   120: aload_1
                //   121: aload_1
                //   122: invokeinterface getLocation : ()Lorg/bukkit/Location;
                //   127: getstatic org/bukkit/Sound.LEVEL_UP : Lorg/bukkit/Sound;
                //   130: fconst_1
                //   131: fconst_2
                //   132: invokeinterface playSound : (Lorg/bukkit/Location;Lorg/bukkit/Sound;FF)V
                //   137: aload_2
                //   138: invokeinterface hasNext : ()Z
                //   143: ifne -> 85
                //   146: goto -> 367
                //   149: getstatic main/BGMain.COUNTDOWN : Ljava/lang/Integer;
                //   152: invokevirtual intValue : ()I
                //   155: bipush #25
                //   157: if_icmpgt -> 228
                //   160: getstatic main/BGMain.COUNTDOWN : Ljava/lang/Integer;
                //   163: invokevirtual intValue : ()I
                //   166: bipush #10
                //   168: if_icmple -> 175
                //   171: iconst_1
                //   172: goto -> 176
                //   175: iconst_0
                //   176: getstatic main/BGMain.COUNTDOWN : Ljava/lang/Integer;
                //   179: invokevirtual intValue : ()I
                //   182: iconst_5
                //   183: irem
                //   184: ifne -> 191
                //   187: iconst_1
                //   188: goto -> 192
                //   191: iconst_0
                //   192: iand
                //   193: ifeq -> 228
                //   196: new java/lang/StringBuilder
                //   199: dup
                //   200: ldc 'El juego comienza en '
                //   202: invokespecial <init> : (Ljava/lang/String;)V
                //   205: getstatic main/BGMain.COUNTDOWN : Ljava/lang/Integer;
                //   208: invokestatic TIME : (Ljava/lang/Integer;)Ljava/lang/String;
                //   211: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
                //   214: ldc '.'
                //   216: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
                //   219: invokevirtual toString : ()Ljava/lang/String;
                //   222: invokestatic printTimeChat : (Ljava/lang/String;)V
                //   225: goto -> 367
                //   228: getstatic main/BGMain.COUNTDOWN : Ljava/lang/Integer;
                //   231: invokevirtual intValue : ()I
                //   234: bipush #10
                //   236: if_icmpgt -> 281
                //   239: getstatic main/BGMain.COUNTDOWN : Ljava/lang/Integer;
                //   242: invokevirtual intValue : ()I
                //   245: iconst_3
                //   246: if_icmple -> 281
                //   249: new java/lang/StringBuilder
                //   252: dup
                //   253: ldc 'El juego comienza en '
                //   255: invokespecial <init> : (Ljava/lang/String;)V
                //   258: getstatic main/BGMain.COUNTDOWN : Ljava/lang/Integer;
                //   261: invokestatic TIME : (Ljava/lang/Integer;)Ljava/lang/String;
                //   264: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
                //   267: ldc '.'
                //   269: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
                //   272: invokevirtual toString : ()Ljava/lang/String;
                //   275: invokestatic printTimeChat : (Ljava/lang/String;)V
                //   278: goto -> 367
                //   281: getstatic main/BGMain.COUNTDOWN : Ljava/lang/Integer;
                //   284: invokevirtual intValue : ()I
                //   287: iconst_3
                //   288: if_icmpgt -> 367
                //   291: new java/lang/StringBuilder
                //   294: dup
                //   295: ldc 'El juego comienza en '
                //   297: invokespecial <init> : (Ljava/lang/String;)V
                //   300: getstatic main/BGMain.COUNTDOWN : Ljava/lang/Integer;
                //   303: invokestatic TIME : (Ljava/lang/Integer;)Ljava/lang/String;
                //   306: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
                //   309: ldc '.'
                //   311: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
                //   314: invokevirtual toString : ()Ljava/lang/String;
                //   317: invokestatic printTimeChat : (Ljava/lang/String;)V
                //   320: invokestatic getGamers : ()Ljava/util/LinkedList;
                //   323: invokevirtual iterator : ()Ljava/util/Iterator;
                //   326: astore_2
                //   327: goto -> 358
                //   330: aload_2
                //   331: invokeinterface next : ()Ljava/lang/Object;
                //   336: checkcast org/bukkit/entity/Player
                //   339: astore_1
                //   340: aload_1
                //   341: aload_1
                //   342: invokeinterface getLocation : ()Lorg/bukkit/Location;
                //   347: getstatic org/bukkit/Sound.NOTE_PLING : Lorg/bukkit/Sound;
                //   350: fconst_1
                //   351: ldc -1.0
                //   353: invokeinterface playSound : (Lorg/bukkit/Location;Lorg/bukkit/Sound;FF)V
                //   358: aload_2
                //   359: invokeinterface hasNext : ()Z
                //   364: ifne -> 330
                //   367: getstatic main/BGMain.COUNTDOWN : Ljava/lang/Integer;
                //   370: invokevirtual intValue : ()I
                //   373: iconst_1
                //   374: isub
                //   375: invokestatic valueOf : (I)Ljava/lang/Integer;
                //   378: putstatic main/BGMain.COUNTDOWN : Ljava/lang/Integer;
                //   381: goto -> 416
                //   384: invokestatic getGamers : ()Ljava/util/LinkedList;
                //   387: invokevirtual size : ()I
                //   390: getstatic main/BGMain.MINIMUM_PLAYERS : Ljava/lang/Integer;
                //   393: invokevirtual intValue : ()I
                //   396: if_icmpge -> 413
                //   399: ldc 'Esperando mas jugadores..'
                //   401: invokestatic printTimeChat : (Ljava/lang/String;)V
                //   404: getstatic main/BGMain.COUNTDOWN_SECONDS : Ljava/lang/Integer;
                //   407: putstatic main/BGMain.COUNTDOWN : Ljava/lang/Integer;
                //   410: goto -> 416
                //   413: invokestatic startgame : ()V
                //   416: return
                // Line number table:
                //   Java source line number -> byte code offset
                //   #21	-> 0
                //   #22	-> 9
                //   #23	-> 46
                //   #24	-> 55
                //   #23	-> 69
                //   #25	-> 75
                //   #26	-> 95
                //   #27	-> 112
                //   #28	-> 120
                //   #25	-> 137
                //   #31	-> 146
                //   #32	-> 196
                //   #33	-> 205
                //   #32	-> 219
                //   #34	-> 225
                //   #35	-> 228
                //   #36	-> 249
                //   #37	-> 258
                //   #36	-> 272
                //   #38	-> 278
                //   #39	-> 291
                //   #40	-> 300
                //   #39	-> 314
                //   #41	-> 320
                //   #42	-> 340
                //   #41	-> 358
                //   #46	-> 367
                //   #47	-> 381
                //   #48	-> 399
                //   #49	-> 404
                //   #50	-> 410
                //   #51	-> 413
                //   #53	-> 416
                // Local variable table:
                //   start	length	slot	name	descriptor
                //   0	417	0	this	Ltimers/PreGameTimer$1;
                //   95	42	1	pl	Lorg/bukkit/entity/Player;
                //   340	18	1	pl	Lorg/bukkit/entity/Player;
            }
        },  0L, 20L));
    }

    public static void cancel() {
        if (shed_id != null) {
            Bukkit.getServer().getScheduler().cancelTask(shed_id.intValue());
            shed_id = null;
        }
    }
}
