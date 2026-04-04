# Assignment 1 Kotlin and Android Tutorial
<!-- Replace X and Title -->
Course : LEIC
Student (s): Martim Ceirão
Date : 14/03/2026
Repository URL : https://github.com/MarSilller/TP1.git
---

## 1. Introduction

Welcome to my GitHub repository. This project contains several branches with exercises developed using **Android Studio** and **IntelliJ IDEA**.

The exercises focus on learning the fundamentals of **Android development** using **Kotlin** and **XML**. They include basic introductory programs such as **Hello World**, as well as small applications designed to practice core concepts.

Some of the projects included in this repository are:

* A simple **System Information App**
* A **Virtual Library Management System** developed using the Kotlin programming language

The purpose of this repository is to demonstrate the basic concepts and practical exercises completed while learning Android development.

<!-- Purpose of the assignment , problem description , and objectives . -->

## 2. System Overview

### Kotlin Exercises

**Exercise 1 – Arrays**
This exercise was used to learn about arrays in Kotlin. An array containing the first **50 perfect squares** is created and printed, helping to understand array construction, iteration, and output.

**Exercise 2 – Calculator**
This project implements a simple calculator capable of performing arithmetic operations as well as **bitwise operations**, including **shift left**, **shift right**, and the boolean operations **AND**, **OR**, and **NOT**.
The exercise was useful for understanding how to work with different variable types, perform operations on them, print results, and improve the program using **exception handling**.

**Exercise 3 – Ball Bounce Simulation**
This program calculates the **number of bounces** and the **maximum height** of a bouncing ball using the `generateSequence` function. Each bounce reaches **60% of the previous height** (losing 40%), and the sequence continues until either **15 bounces occur** or the bounce height drops below **1 unit**.

**Exercise 4 – Virtual Library Management System**
This exercise implements a small Virtual Library Management System in Kotlin using object-oriented programming concepts.
It includes an abstract Book class, specialized subclasses for digital and physical books, and a Library class

The project was useful for getting more familiarized with the kotlin language as we transition from java and also to learn about comanion objects and data classes 

### Android Applications

**Hello World App**
This application was developed in **Android Studio** to learn the basic structure of an Android app. The interface includes an **image**, a **calendar**, and personalized **TextViews** with custom sizes and colors.

**System Information App**
This application displays system information about the device. It demonstrates how to manipulate interface elements from the layout using **Kotlin code**, and how to retrieve device information using the **Build** class, which provides details about the device’s system and configuration (as the name would suggest).

<!-- High - level description of the solution , main features , and use cases .-->

## 3. Architecture and Design

For simplicity and ease of understanding, the projects and applications mostly use the default configurations and structures provided by the development environments. 

Each exercise developed in IntelliJ IDEA is placed inside its own package so that the programs remain organized and easy to distinguish from one another. This helps avoid confusion between exercises and keeps the project structure clear.

Additionally, the GitHub repository organizes the exercises into separate branches. Each branch corresponds to a specific exercise or project, which further helps keep the work structured and makes it easier to navigate between the different implementations.

<!-- Architecture , folder structure , design patterns , and justification ofkey decisions . -->

## 4. Implementation

The implementation of this repository consists of three Kotlin console exercises and two Android applications. Each exercise focuses on practicing specific programming concepts such as arrays, control structures, exception handling, and sequence generation.

### Exercise 1 – Perfect Squares with Arrays

This program generates and prints the first **50 perfect squares** using three different approaches to demonstrate different ways of creating and populating arrays in Kotlin.

* Using an **IntArray with a loop**
* Using a **range and map function**
* Using an **Array with a lambda initializer**

This exercise demonstrates array indexing, iteration, and Kotlin collection functions such as `map()` and `joinToString()`.

### Exercise 2 – Calculator with Logical and Bitwise Operations

The second exercise implements a **console-based calculator** capable of performing arithmetic operations (`+`, `-`, `*`, `/`), boolean operations (`AND`, `OR`, `NOT`), and bitwise shift operations (`SHL`, `SHR`).

User input is validated using **exception handling** to ensure that values are numeric and non-negative. The program then uses a `when` statement to determine which operation to execute.

Boolean operations convert the numeric inputs (`0` or `1`) into Boolean values, while bitwise shift operations use Kotlin's `shl` and `shr` operators. Results are also displayed in **hexadecimal or binary format** where relevant.

### Exercise 3 – Ball Bounce Simulation

This program simulates the bouncing of a ball using Kotlin's `generateSequence` function. The user provides the initial height, and each bounce reaches **60% of the previous height**.

The resulting heights are formatted to two decimal places and printed as a list.

### Android Applications

Two Android applications were also implemented using **Kotlin for logic** and **XML for user interface layouts**.

The Hello World App was created to understand the basic structure of an Android project and how UI components such as images, calendars, and TextViews are defined and customized in XML.

During development, the application was slightly extended to explore simple UI interaction and configuration changes.

First, the application bar displaying the app name was enabled by modifying the theme configuration. This was done by removing the `NoActionBar` attribute from the theme settings (as I noticed from the provided study material it was enabled in one of the pictures of the app), which allowed the application's name to appear at the top of the screen.

A small interactive feature was also added. When the user clicks the main image, that image becomes invisible and three previously hidden images become visible.

The application then loads a list of PNG images stored in the `drawable` folder, shuffles the list, and assigns the images to the three visible ImageViews. This creates a simple slot-machine-like effect where each click produces a random combination of images.

One limitation of the current implementation is that the same image cannot appear more than once in a result. Because the program selects images from different indices of the shuffled list, duplicates are not possible, which slightly reduces the realism of the slot machine behavior.

The **System Information App** retrieves device information using the **Build class** and displays it on screen. This project demonstrates how Kotlin code interacts with elements defined in the XML layout and updates them dynamically.

### Exercise 4 – Virtual Library Management System

This program simulates a **basic library system** using Kotlin.

The abstract Book class defines common book information and includes a custom getter for the publication category and a setter to prevent invalid values for available copies.

Two subclasses extend this class:

* DigitalBook – adds file size and format information

* PhysicalBook – adds weight and hardcover information

The Library class stores books in a mutableList and provides functions to add, borrow, return, display, and search books.

A **companion object** is used to track the total number of books added to the library.

At the end of the code, I also included a simpler alternative implementation for the `showBooks()` and `searchByAuthor()` functions using the overridden `toString()` method as an honorable mention.

### AI-Generated Exercise

An additional exercise was developed using an AI-assisted approach. The project was created by providing a structured prompt that defined the requirements, expected functionality, user interface elements, and overall project structure.

Before generating the code, an implementation plan was requested and reviewed to ensure correctness and alignment with the intended design. After generation, the produced code contained some errors and inconsistencies, which were manually analyzed and corrected.

This exercise served as an exploration of how AI tools can assist in software development while still requiring human validation, debugging, and understanding.

<!-- Implementation details : main modules , components , algorithms , andrelevant code excerpts . -->

## 5. Testing and Validation

Testing for the exercises and applications in this repository was performed manually. Each program was executed multiple times with different inputs to verify that the expected behavior and results were produced.

During development, particular attention was given to identifying errors and unexpected behavior. I personally verified the correctness of each exercise by testing various scenarios and observing the program output.

For the Android applications, **Logcat** was used to monitor runtime feedback, error messages, and debugging information. This helped identify issues in the code and confirm that the applications were functioning correctly during execution.

This approach ensured that the programs handled normal inputs as well as potential error cases appropriately, such as invalid input values or incorrect operations.

<!-- Testing strategy , test cases , scenarios , edge cases , and knownlimitations . -->

## 6. Usage Instructions

To run or view the programs contained in this repository, first navigate to the **branch** corresponding to the desired exercise or application.

1. Go to the selected branch on the GitHub repository.
2. Click the **Code** button.
3. Choose **Download ZIP** to download the full project.

After downloading, extract the ZIP file and open the project in the appropriate development environment:

* **IntelliJ IDEA** for the Kotlin console exercises
* **Android Studio** for the Android applications

To run the slot machine game inside the Hello Android World app you simply need to press the picture and then to keep playing you can press the middle image.

Alternatively, if only a small section of code or a specific exercise is needed, you can browse the repository directly on GitHub and open individual folders or files to **copy the relevant code** without downloading the entire project.

In addition, the AI-generated project was tested following the same approach. After correcting the generated errors, the application was executed multiple times to verify that it behaved as expected and met the defined requirements.
<!-- How to run the project : requirements , setup , configuration , and
execution steps . -->

---

# Autonomous Software Engineering Sections - only for [ AC OK , AI OK ]
sections

## 7. Prompting Strategy

AI tools were used in a limited and supportive way during the development of this project. The prompts were generally kept simple and focused on solving specific problems or clarifying how certain tools work.

Most of the prompts were used for **troubleshooting issues**, understanding error messages, and improving the documentation of the repository. For example, AI was used to help analyze **Logcat output** when an Android application crashed after rotating the phone to landscape mode. By examining the error logs, it was possible to identify the cause of the crash and correct the problem.

AI tools were occasionally used to clarify some Kotlin features that I had difficulty finding or understanding on the virtual library exercise, particularly the use of companion objects.

ChatGPT was also able to suggest a simpler implementation for the `showBooks()` and `searchByAuthor()` functions. I kept my original implementation but included the alternative version at the end of the code as an honorable mention.

AI was also used for **guidance on working with GitHub**, particularly for understanding how to organize the repository, manage branches, and structure the documentation.

In addition, some prompts were used to assist with writing and improving the **README sections**, helping ensure that the documentation fit the expectations and find better wording where it was poorly written.

For example, one of the prompts used for the AI-generated project included detailed instructions about the expected functionality, UI elements, and project structure, ensuring that the generated program was up to standards.

Overall, AI was used as a support tool for debugging, documentation, and repository management, while the main implementation and development of the exercises were carried out independently.

<!-- Describe the prompts used with AI tools , their purpose , and how they evolved . Include representative examples . -->

## 8. Autonomous Agent Workflow

The planning and testing of the exercises and applications were done solely by me. This included deciding how each exercise would be implemented, organizing the structure of the programs, and manually verifying that the results produced by the programs were correct.

AI tools were used more frequently during debugging and documentation. When errors occurred, AI was sometimes used to help interpret error messages or analyze Logcat's output to better understand the cause of the issue.


The coding itself was mostly written independently with the help of programming guides and documentation found online. In some cases, the development environment autocomplete features were also used not only to guide me through these programs but also speed up the writing.
<!-- Explain how AI tools or agents contributed to development : planning , coding , debugging , testing , documentation , etc . -->

## 9. Verification of AI - Generated Artifacts

To ensure the correctness of any assistance provided by AI tools, all suggestions and generated content were carefully reviewed and tested before being used in the project.

Particular attention was given to the management of the GitHub repository to avoid losing work and to maintain a clear structure between the different exercises and branches. Changes were verified using Youtube guides before being committed to ensure that the repository remained organized and functional.
<!-- Describe how you verified correctness of AI - generated code / designs ( testing , manual review , static analysis , etc .) . -->

## 10. Human vs AI Contribution

The **Documentation** was mostly written independently altough AI tools were able to come up with better wording and fix spelling mistakes.
The **Coding** was also almost clear of AI tools except for some usage of the autocomplete feature that I were provided with.   
The **Troubleshooting** was only rellied on AI tools when I was completely helpless and couldn't find any posts discussing my problems.
The **AI-generated exercise** involved a higher level of AI assistance in generating the initial structure and code, but some corrections, validation, and final adjustments were performed manually.

<!-- Clearly state which parts were primarily human - developed and which were AI - assisted . -->

## 11. Ethical and Responsible Use

While AI tools are helpful these exercises were done with the intent of **learning**, and for that purpose, they **must** be used with a lot of **limitation**.
Any use of AI tools, specifically the autocomplete feature, was **not** just "copied and pasted," as I took the time to ensure everything was correct and that I **understood and learned** what was being done.
<!-- Reflect on risks , limitations , biases , or inappropriate outputs from AI tools and how they were handled . -->

---
# Development Process
## 12. Version Control and Commit History

After each program and app was completed and debugged it was added to the github as a seperate branch.

<!-- Describe how version control was used . The commit history mustreflect continuous work ( not only final commits ). -->

## 13. Difficulties and Lessons Learned

During this assignment, some of the main challenges involved configuring the development environment and organizing the workspace using IntelliJ IDEA, Android Studio, and GitHub. Learning how to manage branches and properly document the repository also required some time and practice.

Working with Kotlin and XML layouts helped improve my understanding of programming concepts and how Android applications connect interface elements with code.

One notable mistake occurred when the application crashed after rotating the device to landscape mode. Investigating the issue through Logcat helped identify the problem and reinforced the importance of testing applications in different scenarios.

One of the main challenges in the exercise of the virtual library was understanding and finding some Kotlin features, especially the companion object, where a lot of time was put into searching for youtube tutorials and basic guides online as well as some AI assistance as a last resort.
This exercise helped reinforce concepts already learned in object oriented programming as well as some new features that differ from **Java** to **Kotlin**.

The AI-generated exercise also highlighted that, while AI can accelerate development, it may introduce errors or incomplete implementations, reinforcing the importance of critical analysis and debugging skills. Even though it was a more hands-free project it still takes time and resources to make it happen, especially when the agent generating the code makes mistakes and is forced to start from step one.

<!-- Main challenges , mistakes , insights , and skills acquired during theassignment . -->

## 14. Future Improvements

One possible improvement would be expanding the System Information App, which is currently very bare bones. The application could benefit from better structuring, additional and a more organized user interface.

More features could use improvements such as the documentation, I believe the current github layout I came up with is not perfect.

The Hello Android World had a feature to simulate a slot machine game altought this one is pretty flawd seeing how it can't generate duplicate numbers, meaning the person shall never win.

For the remaining exercises, improvements could include refining the code structure and adding more features or options to further explore Kotlin programming concepts since what was coded was basically the bare minimum one could ask.
<!-- Possible extensions , optimizations , or features that could be addedin future work . -->

---
## 15. AI Usage Disclosure ( Mandatory )

The AI tools I used while working on this project were only ChatGPT, where I could paste Logcat's log for examination and the README entire file to find spelling mistakes, and the autocomplete feature of the programing spaces I used to fill in some lines of code.

It was also suggested by ChatGPT a simpler version of the `showBooks()` and `searchByAuthor()` functions in the Virtual Library program, although I decided to keep mine seeing how it wouldn't have been my idea, and because of that it was moved to a comment at the end of the code.

I confirm that I remain responsible for all content included in this project. While AI tools were occasionally used for assistance, all code, decisions, and final content were reviewed and validated by me before being included in the repository.

The AI-assisted exercise was included as an additional learning component to explore the role of AI in software development. Despite the use of AI tools, all generated content was reviewed, corrected, and fully understood before being accepted.

<!-- List all AI tools used (e.g., ChatGPT , Copilot , etc .) , how they were used , and confirmation that you remain responsible for all content . -->
# TP1 
