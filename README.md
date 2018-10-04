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
