package com.venomdevteam.venom.modules.music;

import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import com.venomdevteam.venom.modules.music.commands.*;
import com.venomdevteam.venom.modules.music.commands.queue.QueueCommand;
import com.venomdevteam.venom.modules.music.manager.PlayerManager;

public class MusicModule extends Module {

    private final PlayerManager playerManager;

    public MusicModule(Venom venom) {
        super(venom);

        this.name = "music";
        this.description = "Contains all music commands";
        this.enabled = true;

        this.playerManager = new PlayerManager(this.venom);

    }

    @Override
    public void setup() {

        venom.getCommandHandler().registerCommands(
                new JoinCommand(this, this.venom),
                new LeaveCommand(this, this.venom),
                new PlayCommand(this, this.venom),
                new PauseCommand(this, this.venom),
                new ResumeCommand(this, this.venom),
                new StopCommand(this, this.venom),
                new QueueCommand(this, this.venom),
                new SkipCommand(this, this.venom),
                new NowPlayingCommand(this, this.venom),
                new LoopCommand(this, this.venom)
        );

        venom.getLogger().info("Music module loaded!");
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
