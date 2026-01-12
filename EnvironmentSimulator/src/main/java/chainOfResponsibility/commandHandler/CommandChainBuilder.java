package chainOfResponsibility.commandHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandChainBuilder {

    private static final Logger logger = LogManager.getLogger(CommandChainBuilder.class);

    private CommandHandler head;
    private CommandHandler tail;

    public CommandChainBuilder add(CommandHandler handler) {

        if (handler == null) {
            logger.warn("Attempted to add a null CommandHandler to the chain");
            return this;
        }

        if (head == null) {
            head = handler;
            logger.debug("Head of command chain set to {}", handler.getClass().getSimpleName());
        } else {
            tail.setNext(handler);
            logger.debug("Added {} after {}",
                    handler.getClass().getSimpleName(),
                    tail.getClass().getSimpleName());
        }

        tail = handler;
        logger.trace("Tail of command chain is now {}", handler.getClass().getSimpleName());

        return this;
    }

    public CommandHandler build() {
        if (head == null) {
            logger.warn("Building command chain with no handlers");
        } else {
            logger.info("Command chain built starting with {}", head.getClass().getSimpleName());
        }
        return head;
    }
}