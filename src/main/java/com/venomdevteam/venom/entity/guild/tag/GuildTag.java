package com.venomdevteam.venom.entity.guild.tag;

import com.venomdevteam.venom.entity.database.DatabaseConnection;
import com.venomdevteam.venom.entity.member.VenomMember;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class GuildTag {

    private final VenomMember author;

    private String name;
    private String content;

    private final Date createdAt;

    public GuildTag(VenomMember author, String name, String content, Date createdAt) {

        this.author = author;

        this.name = name;
        this.content = content;

        this.createdAt = createdAt;

    }

    public void save() {

        DatabaseConnection database = author.getVenom().getDatabase();

        Connection connection = database.getConnection();

        try {

            PreparedStatement statement = connection
                    .prepareStatement("SELECT exists (SELECT 1 FROM " +
                            database.TAG_DATA_TABLE +
                            " WHERE name = ? LIMIT 1);");

            statement.setString(1, name);

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                if (result.getBoolean("exists")) {

                    statement = connection
                            .prepareStatement("UPDATE " +
                                    database.TAG_DATA_TABLE +
                                    " SET name = ?, content = ?;");

                    statement.setString(1, name);
                    statement.setString(2, content);

                    statement.execute();

                } else {

                    statement = connection.prepareStatement("INSERT INTO " +
                            database.TAG_DATA_TABLE +
                            " VALUES (?, ?, ?, ?, ?);");

                    statement.setString(1, name);
                    statement.setString(2, author.getMember().getGuild().getId());
                    statement.setString(3, author.getId());
                    statement.setString(4, content);
                    statement.setDate(5, new java.sql.Date(createdAt.getTime()));

                    statement.execute();

                    author.getVenomGuild().getTagManager().getTags().add(this);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void rename(String newName) {
        this.name = newName;
    }

    public void edit(String content) {
        this.content = content;
    }

    public VenomMember getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GuildTag)) return false;

        GuildTag tag = (GuildTag) obj;

        return tag.getName().equals(name) &&
                tag.getAuthor() == author &&
                tag.getContent().equals(content) &&
                tag.getCreatedAt() == createdAt;
    }
}
