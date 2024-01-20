package lc.chg.game.litesteners.habilidades;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lc.chg.LCCHG;
import lc.chg.configuration.LCConfig;
import lc.chg.configuration.Translation;
import lc.chg.game.BGKit;
import lc.chg.game.GameState;
import lc.chg.game.timers.BGCooldown;
import lc.chg.game.utils.BGChat;
import lc.chg.game.utils.BGTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class BGHabilidadesListener implements Listener {
    public static ArrayList<Player> cooldown = new ArrayList<>();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Action a = event.getAction();
        if ((a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) && BGKit.hasAbility(p, Habilidades.COMER_GALLETA)) {
            // Verificar si el jugador tiene una galleta en la mano
            ItemStack itemInHand = p.getItemInHand();
            if (itemInHand != null && itemInHand.getType() == Material.COOKIE) {
                p.addPotionEffect(new PotionEffect(
                        PotionEffectType.INCREASE_DAMAGE, LCConfig.abconf.getInt("AB.5.Duration") * 20, 0));
                p.getInventory().removeItem(new ItemStack(Material.COOKIE, 1));
                p.playSound(p.getLocation(), Sound.BURP, 1.0F, 1.0F);
            }
        }
        if ((a == Action.LEFT_CLICK_BLOCK || a == Action.LEFT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK || a == Action.RIGHT_CLICK_AIR) &&
                BGKit.hasAbility(p, Habilidades.LANZA_FUEGO) && p.getItemInHand() != null &&
                p.getItemInHand().getType().equals(Material.FIREBALL)) {
            Vector lookat = p.getLocation().getDirection().multiply(10);
            Fireball fire = (Fireball)p.getWorld().spawn(p.getLocation().add(lookat), Fireball.class);
            fire.setShooter((ProjectileSource)p);
            p.playSound(p.getLocation(), Sound.FIRE, 1.0F, 1.5F);
            p.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.FIREBALL, 1) });
        }
        try {
            if (BGKit.hasAbility(p, Habilidades.TRUENOS_CON_HACHA).booleanValue() && a == Action.RIGHT_CLICK_BLOCK && p.getItemInHand()
                    .getType() == Material.DIAMOND_AXE)
                if (!cooldown.contains(p)) {
                    cooldown.add(p);
                    BGCooldown.thorCooldown(p);
                    Block block = event.getClickedBlock();
                    Location loc = block.getLocation();
                    World world = Bukkit.getServer().getWorlds().get(0);
                    if (event.getClickedBlock().getType() != Material.BEDROCK)
                        event.getClickedBlock().setType(Material.NETHERRACK);
                    event.getClickedBlock().getRelative(BlockFace.UP).setType(Material.FIRE);
                    world.strikeLightning(loc);
                } else {
                    BGChat.printPlayerChat(p, LCConfig.abconf.getString("AB.11.Expired"));
                }
            if (BGKit.hasAbility(p, Habilidades.INVISIBLE_AL_COMER_MANZANA) && p.getItemInHand()
                    .getType() == Material.APPLE && (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK))
                if (!cooldown.contains(p)) {
                    cooldown.add(p);
                    BGCooldown.ghostCooldown(p);
                    p.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.APPLE, 1) });
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, LCConfig.abconf.getInt("AB.16.Duration") * 20, 1));
                    p.playSound(p.getLocation(), Sound.PORTAL_TRIGGER, 1.0F, 1.0F);
                    BGChat.printPlayerChat(p, LCConfig.abconf.getString("AB.16.invisible"));
                } else {
                    BGChat.printPlayerChat(p, LCConfig.abconf.getString("AB.16.Expired"));
                }
            if (BGKit.hasAbility(p, Habilidades.VISION_NOCTURNA_CON_PAPA) && p.getItemInHand()
                    .getType() == Material.POTATO && (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK)) {
                p.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.POTATO, 1) });
                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, LCConfig.abconf.getInt("AB.21.Duration") * 20, 1));
                p.playSound(p.getLocation(), Sound.ENDERMAN_STARE, 1.0F, 1.0F);
            }
            if (BGKit.hasAbility(p, Habilidades.CONGELAR_JUGADORES) && LCCHG.GAMESTATE == GameState.GAME
                    && p.getItemInHand().getType() == Material.WATCH && (
                    a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK))
                if (!cooldown.contains(p)) {
                    cooldown.add(p);
                    BGCooldown.timeCooldown(p);
                    p.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.WATCH, 1) });
                    int radius = LCConfig.abconf.getInt("AB.22.radius");
                    p.playSound(p.getLocation(), Sound.AMBIENCE_CAVE, 1.0F, 1.0F);
                    List<Entity> entities = p.getNearbyEntities((radius + 30), (radius + 30), (radius + 30));
                    for (Entity e : entities) {
                        if (!e.getType().equals(EntityType.PLAYER) || LCCHG.isSpectator((Player)e).booleanValue())
                            continue;
                        Player target = (Player)e;
                        if (BGTeam.isInTeam(p, target.getName()))
                            continue;
                        if (p.getLocation().distance(target.getLocation()) < radius) {
                            BGCooldown.freezeCooldown(target);
                            String text = LCConfig.abconf.getString("AB.22.target");
                            text = text.replace("<player>", p.getName());
                            BGChat.printPlayerChat(target, text);
                            BGCooldown.freezeCooldown(target);
                            LCCHG.frezee.add(target);
                            p.playSound(p.getLocation(), Sound.AMBIENCE_CAVE, 1.0F, -1.0F);
                            p.playSound(p.getLocation(), Sound.AMBIENCE_THUNDER, 1.0F, 2.0F);
                        }
                    }
                    BGChat.printPlayerChat(p, LCConfig.abconf.getString("AB.22.success"));
                } else {
                    BGChat.printPlayerChat(p, LCConfig.abconf.getString("AB.22.Expired"));
                }
        } catch (NullPointerException nullPointerException) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void Snowballs(ProjectileHitEvent e) {
        Projectile proj = e.getEntity();
        if (proj instanceof Snowball) {
            Snowball snow = (Snowball)proj;
            LivingEntity shooter = (LivingEntity)snow.getShooter();
            if (shooter instanceof Player) {
                Player p = (Player)shooter;
                if (BGKit.hasAbility(p, Habilidades.GENERAR_TELARANIA_CON_BOLAS_DE_NIEVE)) {
                    Location loc = snow.getLocation();
                    String world = snow.getWorld().getName();
                    Bukkit.getServer().getWorld(world).getBlockAt(loc).setType(Material.WEB);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile entity = event.getEntity();
        if (entity.getType() == EntityType.ARROW) {
            Arrow arrow = (Arrow)entity;
            LivingEntity shooter = (LivingEntity)arrow.getShooter();
            if (shooter.getType() == EntityType.PLAYER) {
                Player player = (Player)shooter;
                if (LCCHG.isSpectator(player).booleanValue())
                    return;
                if (BGKit.hasAbility(player, Habilidades.FLECHAS_EXPLOTAN)) {
                    ((World)Bukkit.getServer().getWorlds().get(0)).createExplosion(arrow.getLocation(), 2.0F, false);
                    arrow.remove();
                } else {
                    return;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null)
            return;
        Player p = e.getEntity().getKiller();
        if (BGKit.hasAbility(p, Habilidades.CHULETAS_POR_CERDOS) &&
                e.getEntityType() == EntityType.PIG) {
            e.getDrops().clear();
            e.getDrops().add(new ItemStack(Material.PORK, LCConfig.abconf.getInt("AB.7.Amount")));
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        if (BGKit.hasAbility(p, Habilidades.EXPLOTAR_AL_MORIR))
            ((World)Bukkit.getServer().getWorlds().get(0)).createExplosion(p.getLocation(), 2.0F, LCConfig.abconf.getBoolean("AB.23.Burn"));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        Player p = (Player)event.getEntity();
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (LCCHG.isSpectator(p).booleanValue())
                return;
            if (BGKit.hasAbility(p, Habilidades.GENERAR_TELARANIA_CON_BOLAS_DE_NIEVE)) {
                event.setCancelled(true);
            } else if (BGKit.hasAbility(p, Habilidades.DANO_AL_CAER)) {
                if (event.getDamage() > 4.0D) {
                    event.setCancelled(true);
                    p.damage(4.0D);
                    p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                    List<Entity> nearbyEntities = event.getEntity().getNearbyEntities(5.0D, 5.0D, 5.0D);
                    for (Entity target : nearbyEntities) {
                        if (target instanceof Player) {
                            Player t = (Player)target;
                            if (LCCHG.isSpectator(t).booleanValue())
                                continue;
                            if (t.getName() == p.getName())
                                continue;
                            if (t.isSneaking()) {
                                t.damage(event.getDamage() / 2.0D, event.getEntity());
                                continue;
                            }
                            t.damage(event.getDamage(), event.getEntity());
                        }
                    }
                } else if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                    if ((BGKit.hasAbility(p, Habilidades.GANAS_FUERZA_2_CON_FUEGO) &&
                            !p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE))) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, 1));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 320, 1));
                        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.2F);
                    } else if (BGKit.hasAbility(p, Habilidades.GANAS_FUERZA_CON_FUEGO) &&
                            !p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 260, 0));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 280, 1));
                        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.2F);
                    }
                }
                if (BGKit.hasAbility(p, Habilidades.PEGAR_CON_MANOS))
                    if (event.getDamage() > 2.0D) {
                        event.setDamage(event.getDamage() - 2.0D);
                    } else {
                        event.setDamage(event.getDamage() / 2.0D);
                    }
            }
        } else if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
            if ((BGKit.hasAbility(p, Habilidades.GANAS_FUERZA_2_CON_FUEGO) && !p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE))) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, 1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 320, 1));
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.2F);
            } else if ((BGKit.hasAbility(p, Habilidades.GANAS_FUERZA_CON_FUEGO) && !p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE))) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 260, 0));
                p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 280, 1));
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.2F);
            }
        }
        if (BGKit.hasAbility(p, Habilidades.PEGAR_CON_MANOS))
            if (event.getDamage() > 2.0D) {
                event.setDamage(event.getDamage() - 2.0D);
            } else {
                event.setDamage(event.getDamage() / 2.0D);
            }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        Block b = event.getBlock();
        if (BGKit.hasAbility(p, Habilidades.ROMP_ARBOLES_RAPIDO).booleanValue() && b.getType() == Material.LOG) {
            World w = Bukkit.getServer().getWorlds().get(0);
            Double y = Double.valueOf(b.getLocation().getY() + 1.0D);
            Location l = new Location(w, b.getLocation().getX(), y.doubleValue(), b
                    .getLocation().getZ());
            while (l.getBlock().getType() == Material.LOG) {
                l.getBlock().breakNaturally();
                y = Double.valueOf(y.doubleValue() + 1.0D);
                l.setY(y.doubleValue());
            }
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        Player p = event.getPlayer();
        if (BGKit.hasAbility(p, Habilidades.CULTIVOS_INMEDIATOS)) {
            if (block.getType() == Material.CROPS) {
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                block.setData(CropState.RIPE.getData());
            }
            if (block.getType() == Material.MELON_SEEDS) {
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                block.setData(CropState.RIPE.getData());
            }
            if (block.getType() == Material.PUMPKIN_SEEDS) {
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                block.setData(CropState.RIPE.getData());
            }
            if (block.getType() == Material.SAPLING) {
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                TreeType t = getTree(block.getData());
                ((World)Bukkit.getServer().getWorlds().get(0)).generateTree(block.getLocation(), t);
            }
        }
    }

    public TreeType getTree(int data) {
        TreeType tretyp = TreeType.TREE;
        switch (data) {
            case 0:
                tretyp = TreeType.TREE;
                return tretyp;
            case 1:
                tretyp = TreeType.REDWOOD;
                return tretyp;
            case 2:
                tretyp = TreeType.BIRCH;
                return tretyp;
            case 3:
                tretyp = TreeType.JUNGLE;
                return tretyp;
        }
        tretyp = TreeType.TREE;
        return tretyp;
    }

    @EventHandler
    public void onFlash(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (BGKit.hasAbility(p, Habilidades.TELETRANSPORTACION_CON_ANTORCHA).booleanValue() &&
                p.getInventory().getItemInHand().getType() == Material.REDSTONE_TORCH_ON &&
                (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            e.setCancelled(true);
            if (cooldown.contains(p)) {
                p.sendMessage(ChatColor.RED + "Necesitas esperar para volver a usarlo!");
            } else {
                Location loc = e.getPlayer().getTargetBlock((Set<Material>) null, 500).getLocation();
                if (loc.getBlock().getType() == Material.AIR) {
                    p.sendMessage(ChatColor.RED + "Necesitas mirar un bloque para teletransportarte");
                    return;
                }
                if (loc.distance(e.getPlayer().getLocation()) > LCConfig.abconf.getInt("AB.31.Distance")) {
                    p.sendMessage(ChatColor.RED + "No puedes teletransportarte tan lejos");
                    return;
                }
                cooldown.add(p);
                BGCooldown.flashCooldown(p);
                p.setFallDistance(0.0F);
                loc.add(0.0D, 1.0D, 0.0D);
                p.teleport(loc);
                p.setFallDistance(0.0F);
                int distance = (int)(p.getLocation().distance(loc) / 2.0D);
                p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, distance, 0));
                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, -1.0F);
            }
        } else if (BGKit.hasAbility(p, Habilidades.GRANDES_SALTOS_CON_FUEGO_ARTIFICIAL).booleanValue() && e.getPlayer().getItemInHand().getType() == Material.FIREWORK) {
            e.setCancelled(true);
            if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR) && e.getMaterial() == Material.FIREWORK && !p.isSneaking()) {
                Block b = p.getLocation().getBlock();
                if (b.getType() != Material.AIR || b.getRelative(BlockFace.DOWN).getType() != Material.AIR) {
                    p.setFallDistance(-5.0F);
                    Vector vector = p.getEyeLocation().getDirection();
                    vector.multiply(0.6F);
                    vector.setY(1);
                    p.setVelocity(vector);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity defender = event.getEntity();
        if (LCCHG.GAMESTATE == GameState.PREGAME && !(event.getEntity() instanceof Player))
            return;
        if (LCCHG.GAMESTATE != GameState.GAME && event.getEntity() instanceof Player)
            return;
        if (event.getEntity().isDead())
            return;
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow)event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player p = (Player)arrow.getShooter();
                LivingEntity victom = (LivingEntity)event.getEntity();
                if (event.getEntity() instanceof LivingEntity &&
                        victom instanceof Player) {
                    Player v = (Player)victom;
                    BGChat.printPlayerChat(p, "&aAhora &e" + v.getName() + " &atiene &4" + (Math.round(v.getHealth() * 100.0D) / 100L) + "HP");
                    if (!BGKit.hasAbility(p, Habilidades.MUERTE_INSTANTANEA))
                        return;
                    if (p.getLocation().distance(event.getEntity().getLocation()) >= LCConfig.abconf.getInt("AB.9.Distance")) {
                        ItemStack helmet = v.getInventory().getHelmet();
                        if (helmet == null) {
                            BGChat.printDeathChat(Translation.HEADSHOT_DEATH.t().replace("<victom>", v.getName()).replace("<player>", p.getName()));
                            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                            BGChat.printDeathChat(Translation.PLAYERS_REMAIN.t().replace("<amount>", (new StringBuilder(String.valueOf(LCCHG.getGamers().size() - 1))).toString()));
                            BGChat.printDeathChat("");
                            Location light = v.getLocation();
                            ((World)Bukkit.getServer()
                                    .getWorlds().get(0))
                                    .strikeLightningEffect(
                                            light.add(0.0D, 100.0D, 0.0D));
                            v.setHealth(0.0D);
                            v.kickPlayer(ChatColor.RED + Translation.HEADSHOT_DEATH.t().replace("<victom>", v.getName()).replace("<player>", p.getName()));
                        } else {
                            helmet.setDurability((short)(helmet.getDurability() + 20));
                            v.setHealth(2.0D);
                            v.getInventory().setHelmet(helmet);
                        }
                        BGChat.printPlayerChat(p, Translation.HEADSHOT.t());
                    }
                }
            }
        }
        if (damager instanceof Player) {
            Player dam = (Player)damager;
            if (BGKit.hasAbility(dam, Habilidades.ROBA_VIDA_AL_PEGAR).booleanValue()) {
                if (dam.getHealth() == dam.getMaxHealth())
                    return;
                BGCooldown.monkCooldown(dam);
                double damage = Double.valueOf(event.getDamage() - 4.0D).intValue();
                damage /= 2.0D;
                dam.setHealth(dam.getHealth() + 1.0D + damage);
            }
            if (defender.getType() == EntityType.PLAYER) {
                Player def = (Player)defender;
                if (BGKit.hasAbility(dam, Habilidades.ROBO_DE_OBJETOS).booleanValue() && dam.getItemInHand().getType() == Material.STICK && def.getItemInHand() != null)
                    if (!cooldown.contains(dam)) {
                        int random = (int)(Math.random() * (LCConfig.abconf.getInt("AB.13.Chance") - 1) + 1.0D);
                        if (random == 1) {
                            cooldown.add(dam);
                            BGCooldown.thiefCooldown(dam);
                            dam.getInventory().clear(dam.getInventory().getHeldItemSlot());
                            dam.getInventory().addItem(new ItemStack[] { def.getItemInHand() });
                            def.getInventory().clear(def.getInventory().getHeldItemSlot());
                            BGChat.printPlayerChat(dam, LCConfig.abconf.getString("AB.13.Success"));
                            BGChat.printPlayerChat(def, LCConfig.abconf.getString("AB.13.Success"));
                            dam.playSound(dam.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                        }
                    } else {
                        BGChat.printPlayerChat(dam, LCConfig.abconf.getString("AB.13.Expired"));
                    }
                if (BGKit.hasAbility(dam, Habilidades.ROBO_DE_OBJETOS_2).booleanValue() && dam.getItemInHand().getType() == Material.STICK && def.getItemInHand() != null)
                    if (!cooldown.contains(dam)) {
                        int random = (int)(Math.random() * (LCConfig.abconf.getInt("AB.15.Chance") - 1) + 1.0D);
                        if (random == 1) {
                            cooldown.add(dam);
                            BGCooldown.thiefCooldown(dam);
                            dam.getInventory().clear(dam.getInventory().getHeldItemSlot());
                            dam.getInventory().addItem(new ItemStack[] { def.getItemInHand() });
                            def.getInventory().clear(def.getInventory().getHeldItemSlot());
                            BGChat.printPlayerChat(dam, LCConfig.abconf.getString("AB.15.Success"));
                            BGChat.printPlayerChat(def, LCConfig.abconf.getString("AB.15.Success"));
                            dam.playSound(dam.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                        }
                    } else {
                        BGChat.printPlayerChat(dam, LCConfig.abconf.getString("AB.15.Expired"));
                    }
                if (BGKit.hasAbility(dam, Habilidades.ENVENENAR_JUGADORES).booleanValue()) {
                    int random = (int)(Math.random() * (LCConfig.abconf.getInt("AB.19.Chance") - 1) + 1.0D);
                    if (random == 1 && !cooldown.contains(def)) {
                        def.addPotionEffect(new PotionEffect(PotionEffectType.POISON, LCConfig.abconf.getInt("AB.19.Duration") * 20, 1));
                        cooldown.add(def);
                        BGChat.printPlayerChat(dam, LCConfig.abconf.getString("AB.19.Damager"));
                        BGChat.printPlayerChat(def, LCConfig.abconf.getString("AB.19.Defender"));
                        BGCooldown.viperCooldown(def);
                        dam.playSound(dam.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                    }
                }
                if (BGKit.hasAbility(dam, Habilidades.DEBILITAR_JUGADORES).booleanValue()) {
                    int random = (int)(Math.random() * (LCConfig.abconf.getInt("AB.35.Chance") - 1) + 1.0D);
                    if (random == 1 && !cooldown.contains(def)) {
                        def.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, LCConfig.abconf.getInt("AB.35.Duration") * 20, 0));
                        cooldown.add(def);
                        BGChat.printPlayerChat(dam, LCConfig.abconf.getString("AB.35.Damager"));
                        BGChat.printPlayerChat(def, LCConfig.abconf.getString("AB.35.Defender"));
                        BGCooldown.orcoCooldown(def);
                        dam.playSound(dam.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                    }
                }
                if (BGKit.hasAbility(dam, Habilidades.CEGAR_JUGADORES).booleanValue()) {
                    int random = (int)(Math.random() * (LCConfig.abconf.getInt("AB.36.Chance") - 1) + 1.0D);
                    if (random == 1 && !cooldown.contains(def)) {
                        def.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, LCConfig.abconf.getInt("AB.36.Duration") * 20, 0));
                        def.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, LCConfig.abconf.getInt("AB.36.Duration") * 20, 0));
                        def.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, LCConfig.abconf.getInt("AB.36.Duration") * 20, 0));
                        cooldown.add(def);
                        BGChat.printPlayerChat(dam, LCConfig.abconf.getString("AB.36.Damager"));
                        BGChat.printPlayerChat(def, LCConfig.abconf.getString("AB.36.Defender"));
                        BGCooldown.trollCooldown(def);
                        dam.playSound(dam.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                    }
                }
                if (BGKit.hasAbility(dam, Habilidades.PEGAR_CON_MANOS).booleanValue() && dam.getItemInHand().getType() == Material.AIR)
                    event.setDamage(event.getDamage() + 5.0D);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityTarget(EntityTargetEvent event) {
        Entity entity = event.getTarget();
        if (entity != null &&
                entity instanceof Player) {
            Player player = (Player)entity;
            if (BGKit.hasAbility(player, Habilidades.MOBS_NO_ATACAN).booleanValue() && event.getReason() == EntityTargetEvent.TargetReason.CLOSEST_PLAYER)
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void sopasCurandero(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            int heal = 6;
            int feed = 6;
            if (event.getPlayer().getItemInHand().getType() == Material.MUSHROOM_SOUP &&
                    BGKit.hasAbility(p, Habilidades.RECUPERAR_CORAZONES_CON_SOPAS_3).booleanValue()) {
                ItemStack bowl = new ItemStack(Material.BOWL, 1);
                ItemMeta meta = bowl.getItemMeta();
                if (event.getPlayer().getHealth() < event.getPlayer().getMaxHealth() - 1.0D) {
                    if (event.getPlayer().getHealth() < event.getPlayer().getMaxHealth() - heal + 1.0D) {
                        event.getPlayer().getItemInHand().setType(Material.BOWL);
                        event.getPlayer().getItemInHand().setItemMeta(meta);
                        event.getPlayer().setItemInHand(bowl);
                        event.getPlayer().setHealth(event.getPlayer().getHealth() + heal);
                    } else if (event.getPlayer().getHealth() < event.getPlayer().getMaxHealth() && event.getPlayer().getHealth() > event.getPlayer().getMaxHealth() - heal) {
                        event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
                        event.getPlayer().getItemInHand().setType(Material.BOWL);
                        event.getPlayer().getItemInHand().setItemMeta(meta);
                        event.getPlayer().setItemInHand(bowl);
                    }
                } else if (event.getPlayer().getHealth() == event.getPlayer().getMaxHealth() && event.getPlayer().getFoodLevel() < 20) {
                    if (event.getPlayer().getFoodLevel() < 20 - feed + 1) {
                        event.getPlayer().setFoodLevel(event.getPlayer().getFoodLevel() + feed);
                        event.getPlayer().getItemInHand().setType(Material.BOWL);
                        event.getPlayer().getItemInHand().setItemMeta(meta);
                        event.getPlayer().setItemInHand(bowl);
                    } else if (event.getPlayer().getFoodLevel() < 20 && event.getPlayer().getFoodLevel() > 20 - feed) {
                        event.getPlayer().setFoodLevel(20);
                        event.getPlayer().getItemInHand().setType(Material.BOWL);
                        event.getPlayer().getItemInHand().setItemMeta(meta);
                        event.getPlayer().setItemInHand(bowl);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (LCCHG.frezee.contains(e.getPlayer())) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10, 1, false));
            Location from = e.getFrom();
            Location to = e.getTo();
            double x = Math.floor(from.getX());
            double z = Math.floor(from.getZ());
            if (Math.floor(to.getX()) != x || Math.floor(to.getZ()) != z) {
                x += 0.5D;
                z += 0.5D;
                e.getPlayer().teleport(new Location(from.getWorld(), x, from.getY(), z, from.getYaw(), from.getPitch()));
            }
        }
    }
}

