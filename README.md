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
    * Provides some peace of mind in general to users, with an update on their status

Other considered goals (which were ultimately rejected):  
  * Allowing students to sign up for a particular time (ex. 4:00 PM on Wednesday)

   
# Development Approach
   
.... your approach here ....
