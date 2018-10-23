(ns asgnx.core
  (:require [clojure.string :as string]
            [clojure.core.async :as async :refer [go chan <! >!]]
            [asgnx.kvstore :as kvstore
             :refer [put! get! list! remove!]]))

;; Do not edit!
;; A def for the course home page URL.
(def cs4278-brightspace "https://brightspace.vanderbilt.edu/d2l/home/85892")

;; This is a helper function that you might want to use to implement
;; `cmd` and `args`.
(defn words [msg]
  (if msg
    (string/split msg #" ")
    []))

;; Asgn 1.
;;
;; @Todo: Fill in this function to return the first word in a text
;; message.
;;
;; Example: (cmd "foo bar") => "foo"
;;
;; See the cmd-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn cmd [msg]
  (first (words msg)))

;; Asgn 1.
;;
;; @Todo: Fill in this function to return the list of words following
;; the command in a text message.
;;
;; Example: (args "foo bar baz") => ("bar" "baz")
;;
;; See the args-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn args [msg]
  (rest (words msg)))

;; Asgn 1.
;;
;; @Todo: Fill in this function to return a map with keys for the
;; :cmd and :args parsed from the msg.
;;
;; Example:
;;
;; (parsed-msg "foo bar baz") => {:cmd "foo" :args ["bar" "baz"]}
;;
;; See the parsed-msg-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn parsed-msg [msg]
  {:cmd (cmd msg) :args (args msg)})

;; Asgn 1.
;;
;; @Todo: Fill in this function to prefix the first of the args
;; in a parsed message with "Welcome " and return the result.
;;
;; Example:
;;
;; (welcome {:cmd "welcome" :args ["foo"]}) => "Welcome foo"
;;
;; See the welcome-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn welcome [pmsg]
  (str "Welcome " (first (get pmsg :args))))

;; Asgn 1.
;;
;; @Todo: Fill in this function to return the CS 4278 home page.
;; Use the `cs4278-brightspace` def to produce the output.
;;
;; See the homepage-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn homepage [_]
  cs4278-brightspace)

;; Asgn 1.
;;
;; @Todo: Fill in this function to convert from 0-23hr format
;; to AM/PM format.
;;
;; Example: (format-hour 14) => "2pm"
;;
;; See the format-hour-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn format-hour [h]
  (let [res (mod h 12)]
    (cond
      (= 0 h) "12am"
      (= 12 h) "12pm"
      (> h 12) (str res "pm")
      :else (str res "am"))))

;; Asgn 1.
;;
;; @Todo: This function should take a map in the format of
;; the values in the `instructor-hours` map (e.g. {:start ... :end ... :location ...})
;; and convert it to a string format.
;;
;; Example:
;; (formatted-hours {:start 8 :end 10 :location "the chairs outside of the Wondry"}))
;; "from 8am to 10am in the chairs outside of the Wondry"
;;
;; You should use your format-hour function to implement this.
;;
;; See the formatted-hours-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn formatted-hours [hours]
  (str "From " (format-hour (get hours :start)) " to " (format-hour (get hours :end)) " in " (get hours :location) "."))


;; A def of office hours for various computer science courses
(def all-hours {"cs3251" {"monday" {:start 12 :end 16 :location "FGH 201"}
                          "wednesday" {:start 13 :end 15 :location "FGH 201"}
                          "friday" {:start 10 :end 12 :location "FGH 201"}}

                "cs2201" {"tuesday" {:start 14 :end 16 :location "FGH 201"}
                          "wednesday" {:start 11 :end 13 :location "FGH 201"}
                          "thursday" {:start 9 :end 15 :location "FGH 201"}}})

;; Asgn 1.
;;
;; @Todo: This function should lookup and see if the instructor
;; has office hours on the day specified by the first of the `args`
;; in the parsed message. If so, the function should return the
;; `formatted-hours` representation of the office hours. If not,
;; the function should return "there are no office hours on that day".
;; The office hours for the instructor should be obtained from the
;; `instructor-hours` map.
;;
;; You should use your formatted-hours function to implement this.
;;
;; See the office-hours-for-day-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn office-hours [{:keys [args cmd]}]
  (if (and (contains? all-hours (first args)) (< 1 (count args)))
    (if (contains? (get all-hours (first args)) (second args))
      (formatted-hours (get-in all-hours [(first args) (second args)]))
      "There are no office hours on that day.")
    "Invalid input; specify a valid course and day."))


;; Asgn 2.
;;
;; @Todo: Create a function called action-send-msg that takes
;; a destination for the msg in a parameter called `to`
;; and the message in a parameter called `msg` and returns
;; a map with the keys :to and :msg bound to each parameter.
;; The map should also have the key :action bound to the value
;; :send.
;;
(defn action-send-msg [to msg]
  {:to to :msg msg :action :send})

;; Asgn 2.
;;
;; @Todo: Create a function called action-send-msgs that takes
;; takes a list of people to receive a message in a `people`
;; parameter and a message to send them in a `msg` parmaeter
;; and returns a list produced by invoking the above `action-send-msg`
;; function on each person in the people list.
;;
;; java-like pseudo code:
;;
;; output = new list
;; for person in people:
;;   output.add( action-send-msg(person, msg) )
;; return output
;;
(defn action-send-msgs [people msg]
  (map #(action-send-msg % msg) people))


;; Asgn 2.
;;
;; @Todo: Create a function called action-insert that takes
;; a list of keys in a `ks` parameter, a value to bind to that
;; key path to in a `v` parameter, and returns a map with
;; the key :ks bound to the `ks` parameter value and the key :v
;; vound to the `v` parameter value.)
;; The map should also have the key :action bound to the value
;; :assoc-in.
;;
(defn action-insert [ks v]
  {:action :assoc-in :ks ks :v v})

;; Asgn 2.
;;
;; @Todo: Create a function called action-inserts that takes:
;; 1. a key prefix (e.g., [:a :b])
;; 2. a list of suffixes for the key (e.g., [:c :d])
;; 3. a value to bind
;;
;; and calls (action-insert combined-key value) for each possible
;; combined-key that can be produced by appending one of the suffixes
;; to the prefix.
;;
;; In other words, this invocation:
;;
;; (action-inserts [:foo :bar] [:a :b :c] 32)
;;
;; would be equivalent to this:
;;
;; [(action-insert [:foo :bar :a] 32)
;;  (action-insert [:foo :bar :b] 32)
;;  (action-insert [:foo :bar :c] 32)]
;;
(defn action-inserts [prefix ks v]
  (map #(action-insert (conj prefix %) v) ks))

;; Asgn 2.
;;
;; @Todo: Create a function called action-remove that takes
;; a list of keys in a `ks` parameter and returns a map with
;; the key :ks bound to the `ks` parameter value.
;; The map should also have the key :action bound to the value
;; :dissoc-in.
;;
(defn action-remove [ks]
  {:action :dissoc-in :ks ks})

;; Set of valid courses that can use this office hours system
;; Will be expanded in the future
(def valid-courses #{"cs1101" "cs2201" "cs3251" "cs2212" "cs2231" "cs3250" "cs3281" "cs3270"})

;; Registers the user as a TA for a given course
(defn ta-register [tas course id info]
  (action-insert [course :ta id] info))

; Takes parameters of the current list of TAs and a parsed message
; Adds the user to the list of TAs for the desired class if the class is valid
(defn add-ta [tas {:keys [args user-id]}]
  (if (contains? (into #{} tas) user-id)
    [nil (str user-id " is already a TA for " (first args) ".")]
    (if (contains? valid-courses (first args))
      ; add TAs to the set if it was already made
      [[(ta-register (conj tas user-id) (first args) user-id [])] (str user-id " is now a TA for " (first args) ".")]
      [nil (str (first args) " is not a valid course.")])))

; Adds the user to the queue for the desired class
(defn student-modify [queue course] (action-insert [course :queue] queue))

; Takes parameters of the current "queue" (as a list) for the given class and a parsed
; Receives message of the form {:cmd "join" :user-id "+15555555555" :args [course]}
(defn add-student [queue {:keys [args user-id]}]
  (if (contains? valid-courses (first args))
    (if (contains? (into #{} queue) user-id)
      [nil (str user-id " is already on the queue for " (first args) ".")]
      (if (= 0 (count queue))
        ; queue is stored as a vector, which is initialized if empty
        [[(student-modify [user-id] (first args))]
         (str user-id " is now on the queue for " (first args) " behind " (count queue) " other(s).")]
        ; append user to the end of the vector
        [[(student-modify (conj queue user-id) (first args))]
         (str user-id " is now on the queue for " (first args) " behind " (count queue) " other(s).")]))
    [nil (str (first args) " is not a valid course.")]))

; Note: by design, TAs are not allowed to intentionally skip a student in the queue
; Receives the current state of the queue for the desired class and a parsed message
; Receives message of the form {:cmd "pop" :user-id "+15555555555" :args [course]}
(defn pop-student [queue {:keys [args user-id]}]
  (if (< 0 (count queue))
    (let [front (get queue 0)]
      [[(student-modify (subvec queue 1) (first args))] (str front " was popped off the queue for " (first args) ".")])
    [nil (str "The queue for " (first args) " is empty.")]))

; Checks how many other people are on the queue in front of the user
(defn student-status [queue {:keys [args user-id]}]
  (if (contains? valid-courses (first args))
    ; check whether the user is already in the queue
    (if (and (< 0 (count queue)) (contains? (into #{} queue) user-id))
      [nil (str (.indexOf queue user-id) " student(s) precede " user-id " in the queue.")]
      [nil (str user-id " is not in the queue for " (first args) ".")])
    [nil (str (first args) " is not a valid course.")]))

;; Don't edit!
(defn stateless [f]
  (fn [_ & args]
    [[] (apply f args)]))

; Mappings of corresponding commands were added to the "routes" map
(def routes {"default"  (stateless (fn [& args] "Unknown command."))
             "welcome"  (stateless welcome)
             "homepage" (stateless homepage)
             "office"   (stateless office-hours)
             "ta"       add-ta
             "pop"      pop-student
             "join"     add-student
             "status"   student-status})

; Returns the list of TAs for a certain class
(defn ta-of-class-query [state-mgr pmsg]
  (let [[course] (:args pmsg)]
    (list! state-mgr [course :ta])))

; Returns the queue of a certain class
(defn queue-of-class-query [state-mgr pmsg]
  (let [[course] (:args pmsg)]
    (get! state-mgr [course :queue])))

;; Edited to read the correct state depending on the command
(def queries
  {
   "join"   queue-of-class-query
   "pop"    queue-of-class-query
   "ta"     ta-of-class-query
   "status" queue-of-class-query})

;; Don't edit!
(defn read-state [state-mgr pmsg]
  (go
    (if-let [qfn (get queries (:cmd pmsg))]
      (<! (qfn state-mgr pmsg))
      {})))

;; Asgn 1.
;;
;; @Todo: This function should return a function (<== pay attention to the
;; return type) that takes a parsed message as input and returns the
;; function in the `routes` map that is associated with a key matching
;; the `:cmd` in the parsed message. The returned function would return
;; `welcome` if invoked with `{:cmd "welcome"}`.
;;
;; Example:
;;
;; (let [msg {:cmd "welcome" :args ["bob"]}]
;;   (((create-router {"welcome" welcome}) msg) msg) => "Welcome bob"
;;
;; If there isn't a function in the routes map that is mapped to a
;; corresponding key for the command, you should return the function
;; mapped to the key "default".
;;
;; See the create-router-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn create-router [routes]
  (fn [pmsg] (get routes (get pmsg :cmd) (get routes "default"))))

;; Don't edit!
(defn output [o]
  (second o))

;; Don't edit!
(defn actions [o]
  (first o))

;; Don't edit!
(defn invoke [{:keys [effect-handlers] :as system} e]
  (go
    (println "    Invoke:" e)
    (if-let [action (get effect-handlers (:action e))]
      (do
        (println "    Invoking:" action "with" e)
        (<! (action system e))))))

;; Don't edit!
(defn process-actions [system actions]
  (go
    (println "  Processing actions:" actions)
    (let [results (atom [])]
      (doseq [action actions]
        (let [result (<! (invoke system action))]
          (swap! results conj result)))
      @results)))

;; Don't edit!
(defn handle-message
  "
    This function orchestrates the processing of incoming messages
    and glues all of the pieces of the processing pipeline together.

    The basic flow to handle a message is as follows:

    1. Create the router that will be used later to find the
       function to handle the message
    2. Parse the message
    3. Load any saved state that is going to be needed to process
       the message (e.g., querying the list of experts, etc.)
    4. Find the function that can handle the message
    5. Call the handler function with the state from #3 and
       the message
    6. Run the different actions that the handler returned...these actions
       will be bound to different implementations depending on the environemnt
       (e.g., in test, the actions aren't going to send real text messages)
    7. Return the string response to the message

  "
  [{:keys [state-mgr] :as system} src msg]
  (go
    (println "=========================================")
    (println "  Processing:\"" msg "\" from" src)
    (let [rtr    (create-router routes)
          _      (println "  Router:" rtr)
          pmsg   (assoc (parsed-msg msg) :user-id src)
          _      (println "  Parsed msg:" pmsg)
          state  (<! (read-state state-mgr pmsg))
          _      (println "  Read state:" state)
          hdlr   (rtr pmsg)
          _      (println "  Hdlr:" hdlr)
          [as o] (hdlr state pmsg)
          _      (println "  Hdlr result:" [as o])
          arslt  (<! (process-actions system as))
          _      (println "  Action results:" arslt)]
      (println "=========================================")
      o)))
