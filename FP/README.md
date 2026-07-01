# Final Project "NeedHands"
<!-- Replace X and Title -->
Course : Computação Movél
Student (s): Martim Silva Ceirão (15316)
Date : 01/07/2026
Repository URL : https://github.com/MarSilller/CM.git
---
## 1. Introduction
  As the Final Project that was assigned we were tasked with creating our own Apps from scrach using coding skills learned throughout the semester aswell as using our own creative ideas to come up and build this unique App.
  I settled for a simple yet effective and high demanded App, whose idea originated from my father and I give a special thanks to him, where people who work in enviroments where the job is not guaranteed and always shifting from place to place and company to company can have a easier time finding their next shifts. For example, people who work at setting up events (event setup crews) sometimes can't get a shift for an entire week or more because the recruiters they know are limited and the amount of events they are assigned to are also limited. With this app we provide a direct contact between the recruiter and the worker, meaning these workers are no longer limited to the amount of recruiters they know but by the amount of recruiters the app possesses, which depending on how popular the App can become could completly wipe the idea of entire weeks with no job out of the mind of our workers.
    Outside of the worker side of view it can also be a problem for recruiters to find enough people for certain events/shifts since they also depend on the amount of contacts they possess, as such, this App is also highly beneficial for them since they mustn't have to worry about being short on hands ever again.
    
## 2. System Overview
<!-- High - level description of the solution , main features , and use cases .-->
  The App allows people to register themselves and login as either a worker or a recruiter using an email and password in which case after this step the App works as following:
  In the worker's side they have a digital card that displays their information and they are able to edit this card (including a Profile Picture, Name, Age, Location, Avaibility and Qualities), after that they simply have to wait for a recruiter to enter in contact with them, almost like putting themselves for sale on Ebay. There's a chat button where the worker can talk with various recruiters, establishing thhe shifts hour, place and pay. It is the responsibility of the worker to show up at the designated location at the designated time. It is also possible, via a special "Boost" button, to Boost their profile so it shows up at the top of the recruiter's page.
  As for the recruiter's side, they have a page with various worker cards, they can filter by qualities and location, search for the Name of someone they know, anything they find necessary to find the right person for the job, and they are able to scroll down these worker cards to look at every single one. After finding the correct person they can open a chat, allowing them to talk with the worker and edit the date and time of the shift. It is the responsibility of the recruiter to state the location and pay of the shift as well as making sure to pay this person at the end of the shift.
  The app also features some other minor details, such as a themes button (light and dark mode), a "help" button to guide users, an "about" button to inform users of the App's purpose and a settings button to reset the password.
  
## 3. Architecture and Design
<!-- Architecture , folder structure , design patterns , and justification of key decisions . -->
The application strictly implements the MVVM (Model-View-ViewModel) architectural pattern combined with the Repository Pattern and a Single-Activity Architecture:

Model (Data Layer): Represents the domain entities and data structures (e.g., Worker, Recruiter, ChatMessage, UserProfile). Models define the schema used for database serialization/deserialization without containing any business or presentation logic.
View (UI Layer): Fully built using Jetpack Compose, Android’s modern declarative UI toolkit. Views are stateless where possible and observe UI state changes reactively via Kotlin StateFlow.
ViewModel (Presentation Layer): Acts as the bridge between the UI and the Data Layer (e.g., WorkerHomeViewModel, JobChatViewModel). ViewModels survive configuration changes, handle user actions, execute business logic asynchronously using Kotlin Coroutines, and emit immutable state objects (UiState) to the Views via Unidirectional Data Flow (UDF).
Repository Pattern: The FirebaseRepository acts as a centralized single source of truth for data operations. It abstracts away the implementation details of Firebase Firestore, Authentication, and Cloud Storage from the ViewModels, making the application easier to test and maintain.
Single-Activity Architecture: The app runs entirely within MainActivity, utilizing Jetpack Compose Navigation (NavHost) to route users smoothly between composable screens without the memory and lifecycle overhead of multiple activities or fragments.
Displaying Images: Official Glide Compose integration was chosen for its mature caching engine, automated lifecycle awareness, and seamless placeholder handling during asynchronous Firebase Storage fetches.

## 4. Implementation
<!-- Implementation details : main modules , components , algorithms , and relevant code excerpts . -->

## 5. Testing and Validation
<!-- Testing strategy , test cases , scenarios , edge cases , and known limitations . -->
  For testing the App I created a single recruit email address, t1@gmail.com, and two worker email addresses, t2@gmail.com and t3@gmail.com (all with the same Password: 123456) . After registered I tried logging in with invalid values first before moving to exploring the entire app, that is chatting with the recruiter, confirming the shift, altering the date and time, editing the profile, reseting the Password (1234567), Boosting the profile among other things.
    Before the final product some bugs/overlooks I noticed were:
    -Saving the Date/Time for all workers when only setting it for one. This was due to various mistakes but primarly not having a unique ID and being saved localy instead of using the firebase, it currently creates a unique ID by simply joining both the recruiter's and the worker's IDs and saves all the information under a unique colection called "appointments".
    -Profile Pictures not saving correctly. Until today I'm unsure how this was happening, at some point I decided to use the "Coil" implementation to display image links directly from the internet but then I went back to using the storage from firebase and it started working again.
    -Buttons and UI out of place. A good amount of time was spent simply trying to fix the UI as it was constantly getting out of order, and eventualy I turned to depend on Antigravity to adjust the UI elements.

  There are some limitations that this App still has, and those are:
    -Payment of Shifts and rating. As of now there's no way for a recruiter to end the workers shift and pay them, yes, this can be done personaly, but I was hoping to implement this so that the recruiter could also give a rating to that worker depending on their performance, as of now the rating is locked at 5.0 stars since there's no way to affect it.
    -Validate email. I wanted to implement the email validation present in CM4's "NotesProXMLViews3" but seeing how complex the App was becoming and out of consideration to test registering users quickly in front of the teacher, I simply decided not to include this feature.
    
## 6. Usage Instructions
<!-- How to run the project : requirements , setup , configuration , andexecution steps . -->
  Running this app should be fairly simple, simply dowload the ZIP file through the github itself and open it inside Android Studio (I was personaly using Panda 2 | 2025.3.2), then after building and syncing the project press the green arrow button ("Run") and it launches the App.
  Inside the App you could use the users mentioned in point 5. (t1@gmail.com t2@gmail.com and t3@gmail.com with password 123456) or register a entirely new user. After that you login to the correct space, either a recruiter or worker, and follow the simple steps inside the help button on the top right corner.
  
# Autonomous Software Engineering Sections - only for [ AC OK , AI OK ]
sections
## 7. Prompting Strategy
<!-- Describe the prompts used with AI tools , their purpose , and how they evolved . Include representative examples . -->
  The prompts were written by me, upgraded by gemini AI, and then sent to antigravity where I would personaly review the Implementation Plan it provided and make a choice.
  Some prompts used:
    sobre o botao de editar a data, de momento ele não tem funcionalidade, vamos mudar isso:
    1. Abrir um DatePickerDialog nativo do Android configurado com a data atual.
    2. Guardar a data escolhida pelo utilizador numa variável local chamada 'dataSelecionada' no formato "DD/MM/AAAA".
    3. Atualizar o texto do próprio botão com essa data para o utilizador ver que funcionou.
    |
    I have just added a firebase, you are free to check the dependencies on the app and project. Now I want you to implement this firebase on the app:
    1. Register and log in both recruiters and workers using Firebase Authentication (Email/Password).
    2. Create a `UserProfile` data class (id, name, email, role: "recruiter" or "worker"). When a user registers, save this profile to a "Users" collection in              Firestore using their Auth UID.
    3. Create an `Appointment` data class (date, time, recruiterId, workerId) and a function to save it to an "Appointments" collection in Firestore.
    4. Create a `ChatMessage` data class and a function to save it to a "Chats" collection.
    Gera apenas o bloco do 'setOnClickListener' limpo para eu colar dentro do meu onCreate, preparado para mais tarde enviar essa variável para a Firebase
    
## 8. Autonomous Agent Workflow
<!-- Explain how AI tools or agents contributed to development : planning , coding , debugging , testing , documentation , etc . -->
  Most of the planning was done by myself, recycling ideas from previous assignemnts, debugging and testing was done by me as well.
  Coding was a mixed of both where antigravity would do most of the coding while I tried to understand how the code was functioning.
  Documentation was mostly written by me with some help from gemini AI.
  
## 9. Verification of AI - Generated Artifacts
<!-- Describe how you verified correctness of AI - generated code / designs ( testing , manual review , static analysis , etc .) . -->
  I would ask any suspicious lines of code to either antigravity or gemini AI as well as manualy testing the new implemented features and fixes to confirm everything was in order.
  
## 10. Human vs AI Contribution
<!-- Clearly state which parts were primarily human - developed and which were AI - assisted . -->
  All the documentation, strategy planning, App Idea, App features, Features implementation and testing was manmade while most of the Coding was assited by Antigravity.
  
## 11. Ethical and Responsible Use
<!-- Reflect on risks , limitations , biases , or inappropriate outputs from AI tools and how they were handled . -->
  There's always risks of bugs happening, and also theres a big risk an idea of mine was eventually thrown away by AI tools and I failed to notice it. AI tools are extremely biased to do things the way to want and I tried my hardest to provide sample codes and say "use this as an example", if not for that, Antigravity would have generated codes I and many others would have no idea how it worked.
  Some mistakes the AI made were pointed out and I tried to be as specific as possible to how it should work and how to fix them. For example one time it didn't know how the "Boost" feature would work and tried to do things localy, creating an entire new UI just for Boosted users, and I told it to simply save it as a variable inside the firebase and go from there.
  
## 12. Difficulties and Lessons Learned
<!-- Main challenges , mistakes , insights , and skills acquired during the assignment . -->
  The main challenge was the complexity and scale of the App, as it was easy to break something made in the past with reasonably simple and small lines of code. I had to make sure I had everything in mind before making a change and it was also hard to sometimes finds the code we used in previous assignments due to just how much was done in the entire semester.
  With all said and done I feel like I learned a lot of how the market for developing Apps works and just how hard it can be to create a simple App, not to mention how time consuming it can be.

## 14. Future Improvements
<!-- Possible extensions , optimizations , or features that could be added in future work . -->
  In the future I would like to make the UI look more professional, having feedback from people in the area of my target audience to test it themselves as well as include the features I meantioned in the limitations.

## 15. AI Usage Disclosure ( Mandatory )
<!-- List all AI tools used (e.g., ChatGPT , Copilot , etc .) , how they were used , and confirmation that you remain responsible for all content . -->
  In the entirety of thsi project I used only Gemini AI and Antigravity and I can confirm I am responsible for their decisions and mistakes.
