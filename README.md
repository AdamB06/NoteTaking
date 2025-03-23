# Note Taking
Welcome to this application!
As a small introduction, this is a copy of a university group project I contributed to for 6 weeks. I did not code everything here, but I independently implemented the Automated Change Synchronization feature, and particiapted in the development of the Interconnected Content feature. Although it is not perfect, it was a great learning experience. Below is an explanation of the features and how to run the program yourself.

The note taking application that you are about to use is a simple but effective way to keep track of all your notes.
The extensions that we added:
- Live language switch, we have a language switch that consists of 4 languages (Dutch, German, English and Spanish)
- Interconnected content, through the use of tags you can mark your notes with #"keyword". Another addition
is the note reference functionality where you can refer to other notes using [["note title"]] inside your notes.
becomes a clickable link in the markdown which will refer you to the note.
- Automated change synchronization, with the implementation of websockets there is no need to manually refresh you notes,
this gets done for you while only receiving relevant updates.
- Shortcuts, the shortcuts that we added can be found back in the left top corner where you get an overview of all the
shortcuts.

To run the template project from the command line, you either need to have [Maven](https://maven.apache.org/install.html) installed on your local system (`mvn`) or you need to use the Maven wrapper (`mvnw`). You can then execute

	mvn clean install

to package and install the artifacts for the three subprojects. Afterwards, you can run ...

	cd server
	mvn spring-boot:run

to start the server or ...

	cd client
	mvn javafx:run

to run the client. Please note that the server needs to be running, before you can start the client.

Once this is working, you can try importing the project into your favorite IDE.
