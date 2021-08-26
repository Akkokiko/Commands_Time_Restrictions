package me.akkokiko.commands_time_restrictions;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

public class Blocker implements Listener {
    private Plugin plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        Player player = event.getPlayer();
        if(!(player.hasPermission("ignoreTimeSchedule"))){

            String message = event.getMessage();
            String[] args = message.split(" ");

            String [] blockedCommends = plugin.getConfig().getKeys(true).toArray(new String[0]);

            //-----------Checking the commend -----------\\
            for(String msg : blockedCommends){
                if(message.toLowerCase().startsWith(msg)){
                    BlockCommand(msg,event,player);
                    return;
                }
            }
            if(Arrays.asList(blockedCommends).contains(args[0].toLowerCase())){
                BlockCommand(message,event,player);
                return;
            }
        }
    }

    void BlockCommand(String found,PlayerCommandPreprocessEvent event,Player player ){
        String timeCode = plugin.getConfig().getString(found+".Schedule");

        int dayValue = LocalDate.now().getDayOfWeek().getValue();
        Date date = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH");
        int currentHour = Integer.valueOf(timeFormat.format(date));

        int[] time = TimeAnalysis(timeCode,dayValue);


        if(!(currentHour >= time[0]  && currentHour < time[1])){
            // blocking the commend

            event.setCancelled(true);
            player.sendMessage(ChatColor.RED+plugin.getConfig().getString(found+".CustomMessage"));
        }
    }
    int[] TimeAnalysis (String s,int day){

        String[] result1 = s.split("\\|");
        String[] result2 = result1[day-1].split("-");
        int[] result3 = new int [result2.length];;
        for (int i = 0; i <result2.length; i ++) {
            result3 [i] = Integer.parseInt (result2 [i]);
        }
        return result3;
    }
}
