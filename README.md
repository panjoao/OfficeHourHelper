# OfficeHourHelper

I've been working as a Teaching Assistant for one of the intermediate programming courses here at Vanderbilt
for a couple of years now. As a result, I interact with many students who come to me for help on difficult 
assignments that they receive throughout the semester. Vanderbilt uses an "office hours" system, where all TAs have 
weekly times where they sit in a designated location and students can stop by and ask questions. According to several 
students, many of them are frustrated when they either don't know where to go to get help, or must wait in line for a long
time to talk to a TA. This text message-based application solves both of these problems.  

The primary interface of the service is through text messages. This ensures that everyone can use it without having to 
download an app or consume a lot of data. In addition, a message-based interface makes the app more accessible and removes
clutter. This application allows students to join an "office hours queue" for a particular class, or ask about the status of office hours in general by sending text messages with commands. A student can use the command "join <COURSE-NUMBER>" to join the 
corresponding queue, or "info <COURSE-NUMBER>" to receive information on when and where office hours for a course are being
held. The system will give replies to indicate success or failure via SMS, and allow instructors to view the queue directly,
and pop people off as they are helped.

Students currently need to go to physical classrooms and computer labs just to wait in line to be helped. On busy days,
they might have to wait in line for upwards of 20 minutes to receive help. In addition, it can be difficult to physically locate
an instructor in a crowded office hours environment, and know who to ask. The environment itself can quickly become crowded
and unorganized, which disincentivizes students from going in the first place. The goal of this application is to streamline the
process of getting help in any class, although it is designed with Computer Science classes in mind. Phone numbers for this 
system are provided by Twilio, and the backend runs on AWS. 



# Questions:
  1. What are the hardest classes you're taking right now?
  2. How do you know typically get help when you're stuck on an assignment?
  3. How often do you go to office hours for any classes (times/week)?
  4. How long do you stay at an average office hours session?
  5. How many other students do you normally see at these sessions?
  6. How long do you usually wait to get help from an instructor?
  7. What's your least favorite part of coming in to get help?
  8. Has the office hours environment ever discouraged you from seeking help when you need it, and why?
  9. What changes should we make to the office hours helping system, if any?
  10. Would you be willing to get help from an online service rather than in person?


# Answers:

## Question 1: 
N: Intermediate Software Design, Digital Logic  
R: Intermediate Software Design, Discrete Structures  
M: Intermediate Software Design, Algorithms

## Question 2:
N: I usually try to look for general examples online, and come into office hours when I'm stuck for a long time.  
R: I come directly to the office hours for all my classes that are posted online, usually in a computer lab at Featheringill.  
M: Look through lecture notes or old coursework for a similar example. Also consult the textbook or come into office hours to 
make sure things are right.

## Question 3:
N: Usually 2 times when a large assignment is due that week.  
R: 2 times, or more when there's a big exam coming up.  
M: At most once a week, to clarify questions I have on my assignments.

## Question 4:
N: 1 hour, where I get help as soon as possible when I arrive but stay around in case I have other questions.  
R: 2 hours, which is enough to do a solid chunk of my assignments.  
M: 30-40 minutes, enough time to wait in line, then ask the questions I bring with me.

## Question 5:
N: 4-5 students who also stay around for a while.  
R: 10 students who come and go while I'm at the computer lab.  
M: Just a couple, unless it's right before an assignment is due but I usually avoid those times anyway.

## Question 6:
N: 5-10 minutes  
R: 10 minutes, but I've had to wait for 20 the day before an assignment is due.  
M: 0-5 minutes, there's usually no one else there.

## Question 7:
N: Instructors sometimes don't have good answers to my questions, and need to refer me to someone else.  
R: When it's really busy, I have to wait a long time to get help and talk to someone.  
M: Sometimes I stop by and find that either no one is scheduled to show up, or whoever is supposed to come never does.

## Question 8:
N: No, sometimes I really need help so I'm fine with working in the crowded computer lab.  
R: No, it's the same as what I had to deal with in 1101 and 2201 even if it's not ideal.  
M: Only when I see that the computer lab is completely full, and I know it will take a long time to get help.

## Question 9:
N: Make an app to sign up for office hours help. It would be helpful and streamline the process.  
R: I think it's good the way it is, but it can get chaotic when the line is very long.  
M: The computer lab can get loud when people from multiple classes are getting help at the same time, and hard to work in. You
should also list office hours in a better place. No one else seems to know when you guys are here. 

## Question 10:
N: No, online services rarely have relevant material and are presented in a difficult to understand way.  
R: No, I highly prefer being able to come in to ask about my specific problem, and easily ask follow-up questions.  
M: No, lots of problems I have are hard to explain over text and email already works just fine.


# Requirements

Note: These Requirements came from a blend of my own experiences with Office Hours, both as a student and instructor, and
students I've interviewed on the topic.

Identified goals for the product:  
  * Ability to physically come in and get help face-to-face
    * Students have stated that an entirely virtual service would be no better than existing online tools like StackOverflow
    * Courses want to discourage students from searching online for answers, which could lead to an honor code violation
    * Many issues with programming assignments are difficult to describe without being able to actually see the code
  * Viewing office hour schedules for many classes from the same interface
    * Most students will take multiple difficult classes at the same time
    * Students do not want to swap to a different service just to get the same result
    * Should be easy to add the information of more classes, as professors (hopefully) decide to use the service
  * Joining a "queue" to get help without having to be waiting in person
    * Queue based approach is generally fair; first-come-first-served
    * The current office hours system implicitly uses a queue, which is just not recorded anywhere
    * Allow students to view how many others are currently in the queue before joining
      * Students can decide whether queue is worth joining based on their time constraints
    * Conserves space in the smaller office hours rooms, which frequently get filled up
  * Notifications as the student's office hours appointment approaches
    * Minimizes the risk of someone not showing up or otherwise forgetting to get help
    * Saves the instructors' time having to track students down
  * Ability for instructors to monitor the current queue and make adjustments
    * Students will inevitably forget to show up or cancel their appointments
    * Someone might have a very simple issue, which can be handled while waiting on another student
    * Instructors should look out for abuse, or people using the system in bad faith (spam, etc.)
  * Ability for instructors to shut down the queue and prevent more students from joining
    * Office hour sessions will shut down at the end of each day
    * The queue could get excessively long, to the point that instructors are not able to help everyone
  * Ability for instructors to "pause" the queue for a selected period of time
    * There could be a gap in office hours, in which we shouldn't mislead students into joining the queue
  * Option for a student to gracefully exit the queue, and lose their spot
    * Students may simply have to go to class or any other scheduled event
    * Students may have an emergency come up which forces them to leave
    * Students might figure out their problem on their own, and not need help anymore
    * Helps save instructor resources; no need to manually remove everyone who wants to leave
  * Ability for students to get an update on their spot in the queue
    * Allows students to decide whether it's worth staying in line at a given time
    * Provides some peace of mind to users, with updates on their status

Other considered goals (which were ultimately rejected):  
  * Allowing students to sign up for a particular time (ex. 4:00 PM on Wednesday)
    * Would require a separate persistence structure from the underlying queue
    * Instructors would need to use extra effort to make their appointments on time
    * Students in the actual queue would be resentful at the appearance of others "jumping" in line
    * Hard to predict how long an individual will need to get help; can range from seconds to more than 10 minutes
  * Pulling information on office hours from Brightspace/Piazza
    * Would require a large effort to get persmissions for all classes and make proper API calls
    * Information is not presented in a persistent way between classes
    * Instructor schedules are subject to change throughout the year
    * System should be opt-in for a particular course, for which entering office hours can be a prerequisite
  * Creating special "profiles" for students and instructors
    * Unnecessary since phone numbers are unique and will not change
    * Difficult to verify identities
    * Would require significant, costly data storage as the system expands


# Development Approach
   
The primary development approach will be Scrum (agile framework). This was chosen because it is the development practice 
I am personally the most familiar with, having used it extensively in my summer internship. It also provides a nice division
of the larger project into smaller tasks, and ensures that the team will stay on track. "Standups," while a bit unintuitive with a 
team of only one developer, ensure that I will keep myself on track and not get lost in the details. Standups also provide a 
designated period of time to keep track of my progress, and readjust my goals/priorities as needed. While a typical sprint in 
a professional environment will last weeks, the sprints for this project will last just two days.  

The requirements gathering for this project came primarily from my own observations of the way office hours currently work. Over 
the past few years at Vanderbilt University, I've visited office hours to get help many times. Ever since last year when I started
working as a Teaching Assistant, I've also had the opportunity to view office hours from another angle, and see how it could be
improved for the instructors. In addition to my own experiences as a student, I had the opportunity to interview a few other 
students who frequent office hours. They provided new perspectives on the current system, and gave me some unique ideas on how the
system can be improved in the future.   

There are many different ways this type of service could be implemented. It could be presented as a simple web-app with an 
animated queue, allowing people to add themselves as needed. However, this would have introduced unnecessary complexity in 
designing a GUI and acquiring a domain name. It could also be created as a mobile app for various smartphone platforms. However,
mobile apps require larger development teams to create and maintain, and must be painstakingly configured for various sizes of 
screens. Mobile apps also have a relatively high barrier to entry - no one wants to open the app store and download a special 
app just to get a TA to explain a compiler error. The text-message based service is the best of both worlds, allowing users 
to access the queue directly from their cellphones, while also running directly on pre-installed messaging applications.   

Testing of this service will happen through a mixture of my own hard-coded stress tests, and asking various students to try
the service from their own cell phones. This ensures that I can both be sure the service is functional, and get quick feedback 
on any changes that should be made, even things as small as the phrasing on prompt messages. Testing will need to be done 
iteratively and separately for each of the functions of the service, and from both the student and instructor points of view. I 
will use the service on a small scale, with willing participants in my own office hours, to gather new test data every week and 
iterate on the product itself. The same people who are willing to help me test can also decide whether the service has value
to the average student. This constant feedback loop will prevent me from producing the wrong product, and allow me to (hopefully)
validate assumptions that I made earlier in the design process.  

Maintenance will occur iteratively as well. If the service is able to grow enough to become an integral part of the office hours
experience, other developers may be willing to step in and make fixes once I graduate. In any case, the system will be designed
so that each part of its functionality is distinct and well-commented. The use of a good architecture which provides clear 
separation of concerns will make it much easier to fix, even if the original developer (me) is no longer working on the project.
In particular, the parts of the system that parse and send messages should be separated, and the underlying queue should only
be accessible in safe and well-defined ways.  

In terms of estimates, this is an ambitious project to finish within two weeks. However, some aspects of it will be recycled
from previous assignments (such as the ability to "parse" and "send" messages), and others will be provided by Clojure itself
(such as the queue which underlies the entire system). As a result, my initial estimate is that the prototype will be 
functional by the deadline. As the implementation progresses and I receive positive and negative feedback from my user testing,
I will be prepared to reassess as needed.  
