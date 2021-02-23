package com.venomdevteam.venom.entity.member.settings;

import com.venomdevteam.venom.entity.member.VenomMember;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberSettings {

    private boolean administrator;
    private boolean moderator;
    private boolean dj;

    public MemberSettings(VenomMember member) {

        Connection connection = member.getVenom().getDatabase().getConnection();

        try {

            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM " + member.getVenom().getDatabase().MEMBER_DATA_TABLE +
                            " WHERE id = ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            statement.setString(1, member.getId());

            ResultSet result = statement.executeQuery();

            if (!result.next()) {

                statement = connection
                        .prepareStatement("INSERT INTO " + member.getVenom().getDatabase().MEMBER_DATA_TABLE + " VALUES " +
                                "(?, ?, ?, ?, ?);", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                statement.setString(1, member.getId());
                statement.setString(2, member.getMember().getGuild().getId());
                statement.setBoolean(3, false);
                statement.setBoolean(4, false);
                statement.setBoolean(5, false);

                this.administrator = false;
                this.moderator = false;
                this.dj = false;

            } else {

                this.administrator = result.getBoolean("administrator");
                this.moderator = result.getBoolean("moderator");
                this.dj = result.getBoolean("dj");

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public boolean isAdministrator() {
        return administrator;
    }

    public boolean isModerator() {
        return moderator;
    }

    public boolean isDj() {
        return dj;
    }
}
