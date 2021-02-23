package com.venomdevteam.venom.entity.member;

import com.venomdevteam.venom.entity.guild.VenomGuild;
import com.venomdevteam.venom.entity.member.settings.MemberSettings;
import com.venomdevteam.venom.entity.user.VenomUser;
import com.venomdevteam.venom.main.Venom;
import net.dv8tion.jda.api.entities.Member;

public class VenomMember {

    private final VenomGuild venomGuild;

    private final Venom venom;

    private final Member member;
    private final MemberSettings settings;

    private final VenomUser user;

    private final String id;

    public static VenomMember of(VenomGuild venomGuild, Member member) {
        return new VenomMember(venomGuild, member);
    }

    public VenomMember(VenomGuild venomGuild, Member member) {

        this.venom = venomGuild.getVenom();

        this.member = member;
        this.settings = new MemberSettings(this);

        this.venomGuild = venomGuild;

        this.user = new VenomUser(venom, member.getUser());

        this.id = member.getUser().getId();

    }

    public MemberSettings getSettings() {
        return settings;
    }

    public Venom getVenom() {
        return venom;
    }

    public Member getMember() {
        return member.getGuild().getMemberById(member.getUser().getId());
    }

    public VenomUser getVenomUser() {
        return user;
    }

    public VenomGuild getVenomGuild() {
        return venomGuild;
    }

    public String getId() {
        return id;
    }
}
