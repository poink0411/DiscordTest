package com.github.poink0411;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;


public class Main implements OPGGCrawler {
    public static void main(String[] args){
        String token="MTA3MzgzOTMzNzEwNTUzMDg4MA.GTiWJB.TmatupdLto_dkhb3SHE9bg4J0C_drH1mcxJr98";
        DiscordApi api=new DiscordApiBuilder().setToken(token).setAllIntents().login().join();
        System.out.println("invitation link: "+api.createBotInvite());
        api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equalsIgnoreCase("/봇초대")) {

                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("봇 초대 링크")
                        .setDescription("다음링크를 통해 봇을 서버에 초대할 수 있습니다!")
                        .addField("", "https://bit.ly/3RQp1gy")
                        .setColor(Color.cyan);

                Messageable channel;
                event.getChannel().sendMessage(embed);
                //event.getChannel().sendMessage("다음 링크를 통해서 봇을 서버에 추가할수있어요!"+api.createBotInvite());
            }
            if (event.getMessageContent().startsWith("/전적검색")){
                String name=event.getMessageContent().replace("/전적검색", "");
                event.getChannel().sendMessage(OPGGCrawler.getOPGGinfo(name));
            }
        });
    }
}
