package com.venomdevteam.venom.entity.command.dialogue;

import com.venomdevteam.venom.entity.command.Command;
import com.venomdevteam.venom.entity.module.Module;
import com.venomdevteam.venom.main.Venom;
import gnu.trove.map.hash.THashMap;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public abstract class DialogueCommand extends Command {

    protected DialogueStep[] dialogue;

    private final THashMap<String, Integer> indexes = new THashMap<>();

    public DialogueCommand(Module module, Venom venom) {
        super(module, venom);

        this.dialogue = new DialogueStep[]{};
    }

    @Override
    public void run(Member sender, TextChannel channel, String[] args, Message message) {
        if (indexes.containsKey(sender.getUser().getId())) {
            indexes.remove(sender.getUser().getId());
        } else {
            indexes.put(sender.getUser().getId(), 0);
            executeStep(message, 0);
        }
    }

    private void executeStep(Message message, int step) {
        dialogue[step].execute(message);
    }

    public void nextStep(Message message) {

        int index = indexes.get(message.getAuthor().getId());

        if (dialogue[index].isCancelled(message.getAuthor()) || !dialogue[index].isFinished(message.getAuthor())) return;

        if (dialogue[index].isDialogueEnd()) {

            finalize(message.getMember(), message.getTextChannel(), dialogue, message);

            indexes.remove(message.getAuthor().getId());

            for (DialogueStep dialogueStep : dialogue) {
                dialogueStep.getExecutors().remove(message.getAuthor().getId());
            }

            return;
        }

        indexes.replace(message.getAuthor().getId(), dialogue[index].getNextStep() == -1 ? ++index : dialogue[index].getNextStep());

        executeStep(message, indexes.get(message.getAuthor().getId()));

    }

    public abstract void finalize(Member sender, TextChannel channel, DialogueStep[] args, Message message);

    public DialogueStep[] getDialogue() {
        return dialogue;
    }

    public THashMap<String, Integer> getIndexes() {
        return indexes;
    }
}
