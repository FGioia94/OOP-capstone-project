package chainOfResponsibility.commandHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Builder class for constructing a chain of {@link CommandHandler} instances.
 * <p>
 * This class allows handlers to be added sequentially, automatically linking them
 * in the order they are added. The resulting chain can then be used to process
 * commands using the Chain of Responsibility pattern.
 * </p>
 *
 * @see CommandHandler
 */
public class CommandChainBuilder {

    private static final Logger logger = LogManager.getLogger(CommandChainBuilder.class);

    private CommandHandler head;
    private CommandHandler tail;

    /**
     * Adds a {@link CommandHandler} to the end of the chain.
     *
     * @param handler the handler to add; if {@code null}, it is ignored
     * @return this builder instance for method chaining
     */
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

    /**
     * Builds and returns the head of the command handler chain.
     *
     * @return the first {@link CommandHandler} in the chain, or {@code null} if no handlers were added
     */
    public CommandHandler build() {
        if (head == null) {
            logger.warn("Building command chain with no handlers");
        } else {
            logger.info("Command chain built starting with {}", head.getClass().getSimpleName());
        }
        return head;
    }
}