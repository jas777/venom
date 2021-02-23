package com.venomdevteam.venom.util.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;

public class DiscordUtil {

    public static User getUser(JDA client, String id) {

        User user = null;

        try {
            Integer.parseInt(id);

            if (client.getUserById(id) != null) {
                user = client.getUserById(id);
            }
        } catch (Exception e) {
            if (client.getUsersByName(id, false).size() > 0) {
                user = client.getUsersByName(id, false).get(0);
            }
        }

        return user;

    }

}
