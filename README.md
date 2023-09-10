<h1>Personal Trainer App <em>(FITMate)</em></h1>

<h2>Introduction</h2>
I have decided to create a version of the project idea: 
<em>"Personal Trainer Software - Manage clients, booking and session"</em> from the sample ideas document.
<br>The app takes the perspective of a client, who is able to navigate through a set of potential trainer connections. They are able to form and remove connections with these trainers, book sessions with them, as well as give them a rating. Unfortunately, there is nothing in place to create a new account, however there are some sample client accounts set up to experiment with. The valid usernames are as follows: 
* GymJunkie2023
* FitWarriorX
* ActiveLifePro

The trainers are able to "sell" programs to their connected clients. These programs include a list of exercises, instructions for the exercises, as well as an instructional video, which when clicked opens a video file. 

<h2>Running Instructions</h2>
* I generated an executable .jar file called "Personal Trainer". Ideally, you would be able to click this and it would work straight away, but I don't think that's going to be the case. 
* I have connected the code to a MySQL database running locally. This involved storing a dependency to connect the MySQL and Java files. This dependency is included in the executable package so hopefully you shouldn't need to download it but just in case it is available here: https://dev.mysql.com/downloads/connector/j/
* I tried to make it so when you open the app for the first time it will automatically create and populate the database, however, it didn't work because ScriptRunner didn't recognise the delimiter command. 
  * Unfortunately, before running the app, you will need to run the schema creation file "src/database/PT_schemaCreation.sql"
  * You will also likely need to change the connection information in "src/database/DBConnection".
    * Specifically the username and password attributes, which just need to be the username and password for a local MySQL connection.
* Ultimately, if you are having issues running the app please reach out to me and I can film a demonstration video of all the features. And I can demonstrate it working on my laptop during the interview. 

<h2>Other Information</h2>
* I have included some documentation (an ER database diagram and a class diagram)
  * The ER diagram shows an entity for messages, which was a feature I had planned initially but didn't get around to implementing. Instead of removing it from the diagram I decided it to leave it for modelling/design review purposes, but thought it would be worth mentioning in case you saw it and wondered why the feature isn't in the app.
* The UI is a bit dodgy. As far as I'm aware, it's not buggy, but it looks quite ugly. The biggest issue with it is that I couldn't get a scrolling panel to work properly, so the page sizes are static. The find new trainers page can fit all 6 trainers, however the client dashboard can only fit 4. Additionally, there are a plethora of UX design issues with the UI that I would fix given more time.
* Not all trainers have programs available to sell to clients (namely "Ethan" and "Mia")
  * The other trainers are suspect, as two of them seem to have stolen the same program off another trainer. Rude. What can you do..
  * Almost to make up for the above, the trainer's have committed their entire lives to the craft, and are available 24/7 for bookings with clients.