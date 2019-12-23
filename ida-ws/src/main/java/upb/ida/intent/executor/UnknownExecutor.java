package upb.ida.intent.executor;

import upb.ida.intent.exception.IntentExecutorException;
import upb.ida.intent.model.ChatbotContext;
import upb.ida.intent.model.Question;

public class UnknownExecutor extends AbstractExecutor implements IntentExecutor {

	public UnknownExecutor() {
		super(null);
	}

	@Override
	public Question getNextQuestion(ChatbotContext context) {
		return null;
	}

	@Override
	public void execute(ChatbotContext context) throws IntentExecutorException {
		context.addChatbotResponse("Sorry, I could not understand your message. Please try again.");
		context.resetOnNextRequest();
	}

	@Override
	public boolean needsMoreInformation(ChatbotContext context) {
		return false;
	}

	@Override
	public void processResponse(ChatbotContext context) {
	}


}
