package com.github.poink0411;

import com.vdurmont.emoji.EmojiParser;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageSet;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.message.component.HighLevelComponent;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.channel.TextChannel;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class followUP{
    private DiscordApi api;
    private TextChannel channel;
    private Message tmessage=null;
    private List<User> userList=new ArrayList<User>();
    public followUP(DiscordApi api, TextChannel channel, Message message){
        this.api=api;
        this.channel=channel;
        this.tmessage=message;
    }

    boolean check(String a, String b){
        char[] sa=a.toCharArray();
        char[] sb=b.toCharArray();
        return sa[0]==sb[sb.length-1];
    }
    public void game(){
        int players=userList.size();
        channel.sendMessage("<@"+userList.get(0).getId()+"> 님부터 시작해주세요");
        try{
            Thread.sleep(5000);
        } catch(Exception ex){}
        Message recent = null;
        try{
            int flag=0;
            MessageSet tmp = channel.getMessages(10).get();
            for (Message m : tmp){
                if (m.getAuthor().getId() == userList.get(0).getId() && m.getContent().startsWith("!")){
                    recent=m;
                    flag=1;
                    break;
                }
            }
            if (flag==0) {
                channel.sendMessage("아무것도 입력하지 않았습니다. 끝말잇기를 종료합니다...");
                return;
            }
        } catch(Exception ex){}
        for (int i=1; ;i++){
            int flag=0;
            int curPlayer=i%players;
            channel.sendMessage("지금은 <@"+userList.get(curPlayer).getId()+">님의 차례입니다");
            try{
                Thread.sleep(5000);
            } catch(Exception ex){}
            Message sentMessage;
            try{
                MessageSet tmp = channel.getMessagesAfter(10, recent).get();
                for (Message m : tmp){
                    if (m.getAuthor().getId() == userList.get(i).getId() && m.getContent().startsWith("!") && check(m.getContent().replace("!", ""), recent.getContent().replace("!", ""))){
                        channel.sendMessage("맞았습니다!");
                        recent=m;
                        flag=1;
                        break;
                    }
                }
            } catch(Exception ex){}
            if (flag==0) {
                channel.sendMessage("아무것도 입력하지 않았거나 틀렸습니다. 끝말잇기를 종료합니다...");
                return;
            }
        }
    }

    public void Setup(){
        EmbedBuilder embed=new EmbedBuilder().setTitle("지금부터 끝말잇기를 시작합니다");
        channel.sendMessage(embed);
        Optional<Message> recent=null;
        try{
            recent=channel.getMessagesAfter(10, tmessage.getId()).get().getNewestMessage();

        } catch(Exception ex){}
        if (recent.isPresent()){
            recent.get().addReaction(EmojiParser.parseToUnicode(":thumbsup:"));
            recent.get().addReaction(EmojiParser.parseToUnicode(":x:"));
        }
        embed.addField("", "끝말잇기를 원하시는 분은\n5초 안에 :thumbsup:를 눌러주세요\n취소하고 싶으시면 :x:를 눌러주세요")
                .setImage("https://em-content.zobj.net/source/skype/289/smiling-face-with-sunglasses_1f60e.png");
        recent.get().edit(embed);

        try{
            Thread.sleep(5000);
        }catch(Exception ex){}
        try{
            userList.addAll(recent.get().getReactions().get(0).getUsers().get());
            if (recent.get().getReactions().get(1).getUsers().get().size()>=2){
                recent.get().edit(embed.addField("끝말잇기를 종료합니다...", ""));
                return;
            }
        } catch(Exception ex){}
        userList.remove(1073839337105530880L);
        if (userList.size()==0){
            channel.sendMessage("아무도 참여 안했네 ㅅㅂ 종료");
            return;
        }
        for (int i=0;i<userList.size();i++){
            if (userList.get(i).getId()==1073839337105530880L){
                userList.remove(i);
            }
        }
        String participent="";
        for (int i=0;i<userList.size();i++){
            participent+="<@"+userList.get(i).getId()+">\n";
        }
        new MessageBuilder()
                .setEmbed(new EmbedBuilder().setTitle("총 참가자들은")
                        .addField("", participent)
                        .addField("입니다", ""))
                .send(channel);
        return;
    }
}
