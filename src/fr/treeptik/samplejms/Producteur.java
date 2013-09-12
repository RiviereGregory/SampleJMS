package fr.treeptik.samplejms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import fr.treeptik.samplejms.xml.Client;
import fr.treeptik.samplejms.xml.Personne;

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

		// Pour l'envoie de fichier xml
		JAXBContext jaxbContext = JAXBContext.newInstance("fr.treeptik.samplejms.xml");

		Client client = new Client();
		Personne personne = new Personne();
		personne.setNom("River");
		personne.setPrenom("Greg");
		client.getPersonne().add(personne);
		personne = new Personne();
		personne.setNom("Paul");
		personne.setPrenom("ET");
		client.getPersonne().add(personne);
		personne = new Personne();
		personne.setNom("Sky");
		personne.setPrenom("Luck");
		client.getPersonne().add(personne);
		// Création du fichier client
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.marshal(client, new File("client.xml"));

		File fileLecture = new File("client.xml");
		StringBuffer line = new StringBuffer();
		BufferedReader bufferedReader = new BufferedReader(new FileReader(fileLecture));
		while (bufferedReader.ready()) {
			line.append(bufferedReader.readLine());
		}
		System.out.println(line.toString());
		bufferedReader.close();

		TextMessage message = session.createTextMessage(line.toString());
		sender.send(message);

		session.close();
		connection.close();
		System.out.println("Message envoyé");

	}
}
