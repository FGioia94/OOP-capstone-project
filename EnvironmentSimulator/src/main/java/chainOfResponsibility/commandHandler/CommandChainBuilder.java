package chainOfResponsibility.commandHandler;

public class CommandChainBuilder {
    private CommandHandler head;
    private CommandHandler tail;

    public CommandChainBuilder add(CommandHandler handler) {
        if (head == null) {
            head = handler;
        } else {
            tail.setNext(handler);

        }
        tail = handler;
        return this;
    }

    public CommandHandler build() {
        return head;
    }
}
