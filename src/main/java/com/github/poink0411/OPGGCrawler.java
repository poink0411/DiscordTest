package com.github.poink0411;

import org.javacord.api.entity.message.embed.Embed;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.javacord.api.entity.message.embed.EmbedBuilder;

interface OPGGCrawler {
    public static EmbedBuilder getOPGGinfo(String playerName){
        String url = "https://www.op.gg/summoner/userName="+playerName;
        Elements solowinloss, solotier;
        Elements freewinloss, freetier;
        Elements champions;
        int solounranked, freeunranked;
        Document doc;
        try{
            doc= Jsoup.connect(url).get();
            solowinloss=doc.getElementsByClass("css-1v663t e1x14w4w1").select("div.win-lose");
            solotier=doc.getElementsByClass("css-1v663t e1x14w4w1").select("div.tier");
            freewinloss=doc.getElementsByClass("css-1474l3c e1x14w4w1").select("div.win-lose");
            freetier=doc.getElementsByClass("css-1474l3c e1x14w4w1").select("div.tier");
            champions=doc.select("div.champion-box");
            solounranked=doc.getElementsByClass("css-1v663t e1x14w4w1").select("span.unranked").size();
            freeunranked=doc.getElementsByClass("css-1474l3c e1x14w4w1").select("span.unranked").size();
        } catch(IOException ex) {
            return new EmbedBuilder().setTitle("해당 플레이어는 존재하지 않습니다");
        }

        String[] championName=champions.select("div.name a").text().split(" ");
        String championNames="";
        for (int i=0;i<championName.length && i<3;i++){
            championNames+=championName[i]+" ";
        }
        EmbedBuilder embed=new EmbedBuilder()
                .setTitle(playerName+" 님의 전적입니다")
                .setAuthor(playerName, null, "https://cdn.discordapp.com/embed/avatars/0.png");
        if (solounranked==0) {
                embed.addField("개인/2인랭크", "")
                    .addInlineField("티어", solotier.first().text())
                    .addInlineField("전적", solowinloss.first().text());
        }
        else{
            embed.addField("개인/2인랭크를 플레이하지 않았음", "");
        }
        if (freeunranked==0){
            embed.addField("자유랭크", "")
                    .addInlineField("티어", freetier.first().text())
                    .addInlineField("전적", freewinloss.first().text());
        }
        else{
            embed.addField("자유랭크를 플레이하지 않았음", "");
        }

        embed.addField("최근 많이 플레이한 챔피언", championNames)
                .setColor(Color.cyan);
        return embed;
    }
}
