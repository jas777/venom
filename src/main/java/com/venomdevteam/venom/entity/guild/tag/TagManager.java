package com.venomdevteam.venom.entity.guild.tag;

import com.venomdevteam.venom.entity.database.DatabaseConnection;
import com.venomdevteam.venom.entity.guild.VenomGuild;
import com.venomdevteam.venom.entity.member.VenomMember;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.sql.*;
import java.util.ArrayList;

public class TagManager {

    private final VenomGuild guild;

    private final ArrayList<GuildTag> tags;

    public TagManager(VenomGuild venomGuild) {

        this.guild = venomGuild;

        this.tags = new ArrayList<>();

        DatabaseConnection database = venomGuild.getVenom().getDatabase();

        Connection connection = database.getConnection();

        try {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + database.TAG_DATA_TABLE + ";");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                String name = resultSet.getString("name");
                String guildId = resultSet.getString("guild");
                String memberId = resultSet.getString("author");
                String content = resultSet.getString("content");

                Date createdAt = resultSet.getDate("createdat");

                Guild guild = venomGuild.getVenom().getClient().getGuildById(guildId);

                if(guild == null) return;

                Member member = guild.getMemberById(memberId);

                VenomMember author = null;

                if(member != null) {
                    author = VenomMember.of(venomGuild, member);
                }

                tags.add(new GuildTag(author, name, content, createdAt));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public VenomGuild getGuild() {
        return guild;
    }

    public ArrayList<GuildTag> getTags() {
        return tags;
    }
}
