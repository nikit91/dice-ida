package upb.ida.intent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;
import upb.ida.intent.exception.IntentExecutorException;
import upb.ida.intent.executor.IntentExecutor;
import upb.ida.intent.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SessionScope
@Component
public class Orchestrator {

	@Autowired
	private Environment env;

	// Externalize
	@Value("${intent.classifier.rest}")
	String restApiUrl;
	@Value("${intent.classifier.confidence.threshold}")
	double confidenceThreshold;

	private ChatbotContext context = new ChatbotContext(); // Initialized with GREETING intent by default

	public Orchestrator() {
		try {
			context.getCurrentExecutor().execute(context);
		} catch (IntentExecutorException e) {
			e.printStackTrace();
		}
	}

	private void prepareContext(String userMessage) {
		// Setting up context with the incoming message
		context.setCurrentMessage(userMessage);
		context.clearChatbotResponses();
		context.clearTips();
	}

	/**
	 * Entry point for processing messages through NLE
	 *
	 * @param message: incoming message from the user
	 * @return ChatbotContext which contains current state of the NLE
	 * @throws IntentExecutorException
	 */
	public ChatbotContext processMessage(String message) throws IntentExecutorException {
		if (context.isResetOnNextRequest()) {
			context.resetContext();
		}

		this.prepareContext(message);
		Intent messageIntent = getMessageIntent(message);

		// Clear the context if the user wants to restart
		if (messageIntent.equals(Intent.RESTART)) {
			context.resetContext();
			return context;
		}

		// Set a new intent only if current intent is GREETING. If the current intent is already set to something other
		// than GREETING, then NLE is in the middle of executing that intent and it should not be updated.
		if (context.getCurrentIntent().equals(Intent.GREETING)) {
			context.setCurrentIntent(messageIntent);
			context.setCurrentExecutor(IntentExecutorFactory.getExecutorFor(messageIntent));
		}

		// TODO these two if blocks are to make sure that new NLE implementation only handles the Intents which have
		//  been completely implemented, rest goes to rivescript. Remove them later when all implementations are working.
		if (context.getCurrentIntent().equals(Intent.UNKNOWN))
			context.resetOnNextRequest();

		if (!this.isImplemented(context.getCurrentIntent())) {
			context.resetOnNextRequest();
			return null;
		}

		// At this point, correct intent and executor should be set in the context
		IntentExecutor executor = context.getCurrentExecutor();
		executor.processResponse(context);

		// Decide if the executor needs more information to execute the final step
		if (executor.needsMoreInformation(context)) {
			Question nextQuestion = executor.getNextQuestion(context);
			context.addChatbotResponse(nextQuestion.getQuestion());
			context.setActiveQuestion(nextQuestion);
		} else {
			// Perform final action
			executor.execute(context);
//			context.resetOnNextRequest();
		}

		return context;
	}

	/**
	 * Get the intent of the incoming message
	 *
	 * @param message incoming message from the user
	 * @return
	 */
	private Intent getMessageIntent(String message) {

		Map<String, String> params = new HashMap<>();
		params.put("text", message);

		RestTemplate restTemplate = new RestTemplate();

		IntentResponseWrapper result = restTemplate.getForObject(restApiUrl, IntentResponseWrapper.class, params);
		List<IntentClassification> classifiedIntents = result.getIntents();

		boolean ambiguous = classifiedIntents.isEmpty()
				|| classifiedIntents.stream().filter(intentClassification -> intentClassification.getScore() > confidenceThreshold).count() != 1;

		if (ambiguous)
			return Intent.UNKNOWN;

		return Intent.getForKey(classifiedIntents.get(0).getIntent());
	}

	private boolean isImplemented(Intent intent) {
		String implementedIntents = env.getProperty("intent.executor.implemented");
		if (implementedIntents == null) {
			return false;
		}

		for (String implemented : implementedIntents.split(",")) {
			if (Intent.getForKey(implemented).equals(intent)) {
				return true;
			}
		}

		return false;
	}

	public static void main(String[] args) throws IntentExecutorException {
		Orchestrator o = new Orchestrator();
		System.out.println("Chatbot: " + o.context.getChatbotResponses());

//		"Cappuccino", "Cinema"
		o.processMessage("kem chho");
		System.out.println("Chatbot: " + o.context.getChatbotResponses());

		o.processMessage("bar graph");
		System.out.println("Chatbot: " + o.context.getChatbotResponses());

		o.processMessage("Cappuccino");
		System.out.println("Chatbot: " + o.context.getChatbotResponses());

		o.processMessage("Cinema");
		System.out.println("Chatbot: " + o.context.getChatbotResponses());

		o.processMessage("last 6");
		System.out.println("Chatbot: " + o.context.getChatbotResponses());

		o.processMessage("force directed");
		System.out.println("Chatbot: " + o.context.getChatbotResponses());

		o.processMessage("force directed");
		System.out.println("Chatbot: " + o.context.getChatbotResponses());

		o.processMessage("go back");
		System.out.println("Chatbot: " + o.context.getChatbotResponses());


//		ChatbotContext ctx = new ChatbotContext();
//		ctx.setCurrentMessage("last 60");
//		System.out.println(AnswerHandlingStrategy.BAR_CHART_SUBSET.extractAnswer(ctx));
//		ctx.setCurrentMessage("I want the last 60");
//		System.out.println(AnswerHandlingStrategy.BAR_CHART_SUBSET.extractAnswer(ctx));
//		ctx.setCurrentMessage("first 60 rows");
//		System.out.println(AnswerHandlingStrategy.BAR_CHART_SUBSET.extractAnswer(ctx));
//		ctx.setCurrentMessage("gimme first 60rows");
//		System.out.println(AnswerHandlingStrategy.BAR_CHART_SUBSET.extractAnswer(ctx));
//		ctx.setCurrentMessage("gimme first 60 rows");
//		System.out.println(AnswerHandlingStrategy.BAR_CHART_SUBSET.extractAnswer(ctx));


	}

}
