package com.venomdevteam.venom.entity.command.dialogue;

import com.venomdevteam.venom.entity.command.argument.Arguments;
import com.venomdevteam.venom.entity.command.argument.type.ArgumentType;
import gnu.trove.map.hash.THashMap;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.concurrent.TimeUnit;

public class DialogueStep {

    private final DialogueCommand parent;

    private String question;
    private String result;

    private final ArgumentType responseType;

    private final boolean finished = false;
    private final boolean cancelled = false;
    private boolean dialogueEnd = false;

    private final THashMap<String, StepProgress> executors = new THashMap<>();

    private final int nextStep = -1;

    public DialogueStep(DialogueCommand parent, String question, ArgumentType responseType, boolean finishing) {
        this.parent = parent;
        this.question = question;
        this.responseType = responseType;
        this.dialogueEnd = finishing;
    }

    public void execute(Message message) {

        executors.remove(message.getAuthor().getId());
        executors.put(message.getAuthor().getId(), new StepProgress(false, false));

        message.getChannel().sendMessage(question).queue(msg -> parent.getVenom().getEventWaiter().waitForEvent(
                MessageReceivedEvent.class,
                event ->
                        (event.getAuthor().equals(message.getAuthor()) &&
                            event.getTextChannel().equals(message.getTextChannel())) ||
                                (event.getAuthor().equals(message.getAuthor()) &&
                                        event.getMessage().getContentDisplay().equalsIgnoreCase("cancel")),
                messageReceivedEvent -> {

                    StepProgress progress = executors.get(messageReceivedEvent.getAuthor().getId());

                    if (messageReceivedEvent.getMessage().getContentDisplay().equalsIgnoreCase("cancel")) {
                        progress.setCancelled(true);
                        return;
                    }

                    progress.setResult(messageReceivedEvent.getMessage().getContentDisplay());
                    progress.setFinished(true);

                    assert progress.getResult() != null;

                    if (Arguments.of(this, progress.result).size() > 0) {
                        parent.nextStep(message);
                    }

                }, 30L, TimeUnit.SECONDS, () -> {
                    message.getChannel().sendMessage("Input timed out!").queue();
                    executors.get(message.getAuthor().getId()).setCancelled(true);
                }));

    }

    public DialogueCommand getParent() {
        return parent;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public ArgumentType getResponseType() {
        return responseType;
    }

    public boolean isFinished(User user) {
        return executors.containsKey(user.getId()) && executors.get(user.getId()).isFinished();
    }

    public boolean isCancelled(User user) {
        return executors.containsKey(user.getId()) && executors.get(user.getId()).isCancelled();
    }

    public String getResult(User user) {
        return executors.containsKey(user.getId()) ? executors.get(user.getId()).getResult() : null;
    }

    public int getNextStep() {
        return nextStep;
    }

    public boolean isDialogueEnd() {
        return dialogueEnd;
    }

    public THashMap<String, StepProgress> getExecutors() {
        return executors;
    }

    private static class StepProgress {

        private boolean finished;
        private boolean cancelled;

        private String result;

        public StepProgress(boolean finished, boolean cancelled) {
            this.finished = finished;
            this.cancelled = cancelled;
        }

        public boolean isFinished() {
            return finished;
        }

        public void setFinished(boolean finished) {
            this.finished = finished;
        }

        public boolean isCancelled() {
            return cancelled;
        }

        public void setCancelled(boolean cancelled) {
            this.cancelled = cancelled;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

}
