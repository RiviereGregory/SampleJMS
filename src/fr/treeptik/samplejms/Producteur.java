package fr.treeptik.samplejms;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class Producteur {

	public static void main(String[] args) throws Exception {
		// objet qui permet les recheches jndi grace au fichier conf à la racine
		InitialContext context = new InitialContext();
		// Permet de recherches dans le jndi spécifié
		Queue queue = (Queue) context.lookup("queue/TestQueue");

		// Connection jms
		QueueConnectionFactory factory = (QueueConnectionFactory) context
				.lookup("ConnectionFactory");
		QueueConnection connection = factory.createQueueConnection();
		QueueSession session = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
		// Objet qui permet d'envoyer les messages
		QueueSender sender = session.createSender(queue);

		// Envoie de message texte ou xml
		TextMessage message = session.createTextMessage("Hello JMS");
		sender.send(message);

		// Envoie de message Objet serialisable
		// ObjectMessage message = session.createObjectMessage(new Message("JMS MEssage",
		// "Detail message"));
		// sender.send(message);

		session.close();
		connection.close();
		System.out.println("Message envoyé");

	}

}
