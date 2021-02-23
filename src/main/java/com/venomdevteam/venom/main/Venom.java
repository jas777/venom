package com.venomdevteam.venom.main;

import com.moandjiezana.toml.Toml;
import com.venomdevteam.venom.entity.config.Config;
import com.venomdevteam.venom.entity.database.DatabaseConnection;
import com.venomdevteam.venom.entity.event.waiter.EventWaiter;
import com.venomdevteam.venom.entity.locale.LocaleManager;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.entity.season.manager.SeasonManager;
import com.venomdevteam.venom.handlers.CommandHandler;
import com.venomdevteam.venom.locales.EnglishLocale;
import com.venomdevteam.venom.modules.core.CoreModule;
import com.venomdevteam.venom.modules.image.ImageModule;
import com.venomdevteam.venom.modules.math.MathModule;
import com.venomdevteam.venom.modules.music.MusicModule;
import com.venomdevteam.venom.modules.tag.TagModule;
import com.venomdevteam.venom.seasons.TestSeason;
import gnu.trove.map.hash.THashMap;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class Venom {

    // Core stuff

    private JDA client;
    private final Config config;
    private final boolean debug;
    private final Logger logger;

    // Managers

    private final CommandHandler commandHandler;
    private final LocaleManager localeManager;
    private final SeasonManager seasonManager;

    // Discord utils

    private final EventWaiter eventWaiter = new EventWaiter();

    // Data

    private final DatabaseConnection database;

    // Collections

    private final THashMap<String, Module> modules;

    public Venom(boolean debug) {
        this.debug = debug;
        this.config = new Config(this);
        this.logger = LoggerFactory.getLogger("venom");

        this.commandHandler = new CommandHandler(this);
        this.localeManager = new LocaleManager(this);
        this.seasonManager = new SeasonManager(this);

        this.database = new DatabaseConnection(this);

        this.modules = new THashMap<>();

        this.database.connect();

        if (this.debug) {
            this.logger.debug("Config path: " + this.config.getPath());
        }

        this.start(this.config.getConfig().getString("client.token"));
    }

    private void start(String token) {

        JDABuilder builder = JDABuilder
                .createDefault(token)
                .enableIntents(EnumSet.allOf(GatewayIntent.class))
                .setChunkingFilter(ChunkingFilter.ALL);

        configureMemoryUsage(builder);

        try {

            localeManager.registerLocales(
                    new EnglishLocale()
            );

            seasonManager.registerSeasons(
                    new TestSeason()
            );

            client = builder
                    .addEventListeners(
                            this.commandHandler,
                            this.eventWaiter
                    )
                    .build().awaitReady();

            registerModules(
                    new CoreModule(this),
                    // TODO: Awaiting optimization
                    new ImageModule(this),
                    new MusicModule(this),
                    new MathModule(this),
                    new TagModule(this)
            );

        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void registerModules(Module... modules) {
        for (Module m : modules) {
            this.modules.put(m.getName(), m);
            m.setup();
        }
    }

    public void configureMemoryUsage(JDABuilder builder) {
        // Disable cache for member activities (streaming/games/spotify)
        builder.disableCache(CacheFlag.ACTIVITY);

        // Only cache members who are either in a voice channel or owner of the guild
        builder.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER));

        // Disable member chunking on startup
        builder.setChunkingFilter(ChunkingFilter.NONE);

        // Disable presence updates and typing events
        builder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING);

        // Consider guilds with more than 50 members as "large".
        // Large guilds will only provide online members in their setup and thus reduce bandwidth if chunking is disabled.
        builder.setLargeThreshold(50);
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public JDA getClient() {
        return client;
    }

    public boolean isDebug() {
        return debug;
    }

    public Toml getConfig() {
        return config.getConfig();
    }

    public Logger getLogger() {
        return logger;
    }

    public String getRandomQuote() {
        List<String> quotes = this.getConfig().getList("other.quotes");
        return quotes.get(new Random().nextInt(quotes.size()));
    }

    public EmbedBuilder getBaseEmbed() {
        Color color = null;

        try {
            color = Color.decode(this.getConfig().getString("embed.color"));
        } catch (Exception e) {
            this.getLogger().error("Invalid embed color!");
        }
        return new EmbedBuilder()
                .setColor(color)
                .setFooter(this.getRandomQuote(), this.getClient().getSelfUser().getAvatarUrl())
                .setTimestamp(Instant.now());
    }

    public THashMap<String, Module> getModules() {
        return modules;
    }

    public DatabaseConnection getDatabase() {
        return database;
    }

    public EventWaiter getEventWaiter() {
        return eventWaiter;
    }
}
