(ns asgnx.core-test
  (:require [clojure.test :refer :all]
            [clojure.core.async :refer [<!!]]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojure.test.check.generators :as gen]
            [asgnx.core :refer :all]
            [asgnx.kvstore :as kvstore :refer [put! get!]]))



(deftest words-test
  (testing "that sentences can be split into their constituent words"
    (is (= ["a" "b" "c"] (words "a b c")))
    (is (= [] (words "   ")))
    (is (= [] (words nil)))
    (is (= ["a"] (words "a")))
    (is (= ["a"] (words "a ")))
    (is (= ["a" "b"] (words "a b")))))


(deftest cmd-test
  (testing "that commands can be parsed from text messages"
    (is (= "foo" (cmd "foo")))
    (is (= "foo" (cmd "foo x y")))
    (is (= nil   (cmd nil)))
    (is (= ""    (cmd "")))))


(deftest args-test
  (testing "that arguments can be parsed from text messages"
    (is (= ["x" "y"] (args "foo x y")))
    (is (= ["x"] (args "foo x")))
    (is (= [] (args "foo")))
    (is (= [] (args nil)))))


(deftest parsed-msg-test
  (testing "that text messages can be parsed into cmd/args data structures"
    (is (= {:cmd "foo"
            :args ["x" "y"]}
           (parsed-msg "foo x y")))
    (is (= {:cmd "foo"
            :args ["x"]}
           (parsed-msg "foo x")))
    (is (= {:cmd "foo"
            :args []}
           (parsed-msg "foo")))
    (is (= {:cmd "foo"
            :args ["x" "y" "z" "somereallylongthing"]}
           (parsed-msg "foo x y z somereallylongthing")))))

(deftest welcome-test
  (testing "that welcome messages are correctly formatted"
    (is (= "Welcome bob" (welcome {:cmd "welcome" :args ["bob"]})))
    (is (= "Welcome bob" (welcome {:cmd "welcome" :args ["bob" "smith"]})))
    (is (= "Welcome bob smith jr" (welcome {:cmd "welcome" :args ["bob smith jr"]})))))


(deftest homepage-test
  (testing "that the homepage is output correctly"
    (is (= cs4278-brightspace (homepage {:cmd "homepage" :args []})))))


(deftest format-hour-test
  (testing "that 0-23 hour times are converted to am/pm correctly"
    (is (= "1am" (format-hour 1)))
    (is (= "1pm" (format-hour 13)))
    (is (= "2pm" (format-hour 14)))
    (is (= "12am" (format-hour 0)))
    (is (= "12pm" (format-hour 12)))))


(deftest formatted-hours-test
  (testing "that the office hours data structure is correctly converted to a string"
    (is (= "From 8am to 10am in the chairs outside of the Wondry."
           (formatted-hours {:start 8 :end 10 :location "the chairs outside of the Wondry"})))
    (is (= "From 4am to 2pm in the chairs outside of the Wondry."
           (formatted-hours {:start 4 :end 14 :location "the chairs outside of the Wondry"})))
    (is (= "From 2pm to 10pm in the chairs outside of the Wondry."
           (formatted-hours {:start 14 :end 22 :location "the chairs outside of the Wondry"})))))


(deftest office-hours-for-day-test
  (testing "testing lookup of office hours on a specific day"
    (is (= "From 1pm to 3pm in FGH 201."
           (office-hours {:cmd "office" :args ["cs3251" "wednesday"]})))
    (is (= "From 2pm to 4pm in FGH 201."
           (office-hours {:cmd "office" :args ["cs2201" "tuesday"]})))))


(deftest create-router-test
  (testing "correct creation of a function to lookup a handler for a parsed message"
    (let [router (create-router {"hello" #(str (:cmd %) " " "test")
                                 "argc"  #(count (:args %))
                                 "echo"  identity
                                 "default" (fn [& a] "No!")})
          msg1   {:cmd "hello"}
          msg2   {:cmd "argc" :args [1 2 3]}
          msg3   {:cmd "echo" :args ["a" "z"]}
          msg4   {:cmd "echo2" :args ["a" "z"]}]
      (is (= "hello test" ((router msg1) msg1)))
      (is (= "No!" ((router msg4) msg4)))
      (is (= 3 ((router msg2) msg2)))
      (is (= msg3 ((router msg3) msg3))))))


(deftest action-insert-test
  (testing "That action insert returns a correctly formatted map"
    (is (= #{:action :ks :v}
           (into #{}(keys (action-insert [:a :b] {:foo 1})))))
    (is (= #{:assoc-in [:a :b] {:foo 1}}
           (into #{}(vals (action-insert [:a :b] {:foo 1})))))
    (is (= :assoc-in
           (:action (action-insert [:a :b] {:foo 1}))))
    (is (= {:foo 1}
           (:v (action-insert [:a :b] {:foo 1}))))
    (is (= [:a :b]
           (:ks (action-insert [:a :b] {:foo 1}))))))


(deftest action-remove-test
  (testing "That action remove returns a correctly formatted map"
    (is (= #{:action :ks}
         (into #{} (keys (action-remove [:a :b])))))
    (is (= #{:dissoc-in [:a :b]}
          (into #{}(vals (action-remove [:a :b])))))
    (is (= :dissoc-in
           (:action (action-remove [:a :b]))))
    (is (= [:a :b]
           (:ks (action-remove [:a :b]))))))


(deftest action-inserts-test
  (testing "That action inserts generates a list of inserts"
    (let [a (action-insert [:a :f :b] 1)
          b (action-insert [:a :f :d] 1)
          c (action-insert [:a :f :e] 1)
          d (action-insert [:a :f :c] 1)]
      (is (= [a b c d]
             (action-inserts [:a :f] [:b :d :e :c] 1))))))


(defn action-send [system {:keys [to msg]}]
  (put! (:state-mgr system) [:msgs to] msg))

(defn pending-send-msgs [system to]
  (get! (:state-mgr system) [:msgs to]))

(def send-action-handlers
  {:send action-send})

(deftest handle-message-test
  (testing "the integration and handling of messages"
    (let [ehdlrs (merge
                   send-action-handlers
                   kvstore/action-handlers)
          state  (atom {})
          smgr   (kvstore/create state)
          system {:state-mgr smgr
                  :effect-handlers ehdlrs}]
      (is (= "cs5555 is not a valid course."
             (<!! (handle-message
                    system
                    "test-user1"
                    "ta cs5555"))))

      (is (= "test-user1 is now a TA for cs3251."
             (<!! (handle-message
                    system
                    "test-user1"
                    "ta cs3251"))))

      (is (= "test-user1 is already a TA for cs3251."
             (<!! (handle-message
                    system
                    "test-user1"
                    "ta cs3251"))))

      (is (= "Invalid input; specify a valid course and day."
             (<!! (handle-message
                    system
                    "test-user1"
                    "office wednesday"))))

      (is (= "Invalid input; specify a valid course and day."
             (<!! (handle-message
                    system
                    "test-user1"
                    "office friday"))))

      (is (= "test-user2 is now a TA for cs3251."
             (<!! (handle-message
                    system
                    "test-user2"
                    "ta cs3251"))))

      (is (= "test-user2 is already a TA for cs3251."
             (<!! (handle-message
                    system
                    "test-user2"
                    "ta cs3251"))))

      (is (= "test-user3 is now a TA for cs1101."
             (<!! (handle-message
                    system
                    "test-user3"
                    "ta cs1101"))))

      (is (= "test-user3 is already a TA for cs1101."
             (<!! (handle-message
                    system
                    "test-user3"
                    "ta cs1101"))))

      (is (= "test-user3 is now on the queue for cs3251 behind 0 other(s)."
             (<!! (handle-message
                    system
                    "test-user3"
                    "join cs3251"))))

      (is (= "test-user3 is already on the queue for cs3251."
             (<!! (handle-message
                    system
                    "test-user3"
                    "join cs3251"))))

      (is (= "test-user3 is now on the queue for cs3250 behind 0 other(s)."
             (<!! (handle-message
                    system
                    "test-user3"
                    "join cs3250"))))

      (is (= "test-user4 is now on the queue for cs3251 behind 1 other(s)."
             (<!! (handle-message
                    system
                    "test-user4"
                    "join cs3251"))))

      (is (= "test-user4 is already on the queue for cs3251."
             (<!! (handle-message
                    system
                    "test-user4"
                    "join cs3251"))))

      (is (= "test-user5 is now on the queue for cs3251 behind 2 other(s)."
             (<!! (handle-message
                    system
                    "test-user5"
                    "join cs3251"))))

      (is (= "test-user5 is already on the queue for cs3251."
             (<!! (handle-message
                    system
                    "test-user5"
                    "join cs3251"))))

      (is (= "test-user6 is now on the queue for cs3251 behind 3 other(s)."
             (<!! (handle-message
                    system
                    "test-user6"
                    "join cs3251"))))

      (is (= "test-user6 is already on the queue for cs3251."
             (<!! (handle-message
                    system
                    "test-user6"
                    "join cs3251"))))

      (is (= "cs1234 is not a valid course."
             (<!! (handle-message
                    system
                    "test-user3"
                    "join cs1234"))))

      (is (= "0 student(s) precede test-user3 in the queue."
             (<!! (handle-message
                    system
                    "test-user3"
                    "status cs3251"))))

      (is (= "1 student(s) precede test-user4 in the queue."
             (<!! (handle-message
                    system
                    "test-user4"
                    "status cs3251"))))

      (is (= "2 student(s) precede test-user5 in the queue."
             (<!! (handle-message
                    system
                    "test-user5"
                    "status cs3251"))))

      (is (= "3 student(s) precede test-user6 in the queue."
             (<!! (handle-message
                    system
                    "test-user6"
                    "status cs3251"))))

      (is (= "test-user3 was popped off the queue for cs3251."
             (<!! (handle-message
                    system
                    "test-user1"
                    "pop cs3251"))))

      (is (= "2 student(s) precede test-user6 in the queue."
             (<!! (handle-message
                    system
                    "test-user6"
                    "status cs3251"))))

      (is (= "0 student(s) precede test-user4 in the queue."
             (<!! (handle-message
                    system
                    "test-user4"
                    "status cs3251"))))

      (is (= "The queue for cs2231 is empty."
             (<!! (handle-message
                    system
                    "test-user1"
                    "pop cs2231"))))

      (is (= "test-user4 was popped off the queue for cs3251."
             (<!! (handle-message
                    system
                    "test-user1"
                    "pop cs3251"))))

      (is (= "test-user7 is now on the queue for cs3251 behind 2 other(s)."
             (<!! (handle-message
                    system
                    "test-user7"
                    "join cs3251"))))

      (is (= "1 student(s) precede test-user6 in the queue."
             (<!! (handle-message
                    system
                    "test-user6"
                    "status cs3251"))))

      (is (= "test-user5 was popped off the queue for cs3251."
             (<!! (handle-message
                    system
                    "test-user1"
                    "pop cs3251"))))

      (is (= "test-user6 was popped off the queue for cs3251."
             (<!! (handle-message
                    system
                    "test-user1"
                    "pop cs3251"))))

      (is (= "0 student(s) precede test-user7 in the queue."
             (<!! (handle-message
                    system
                    "test-user7"
                    "status cs3251"))))

      (is (= "test-user7 was popped off the queue for cs3251."
             (<!! (handle-message
                    system
                    "test-user1"
                    "pop cs3251"))))

      (is (= "The queue for cs3251 is empty."
             (<!! (handle-message
                    system
                    "test-user1"
                    "pop cs3251"))))

      (is (= "test-user8 is now a TA for cs3251."
             (<!! (handle-message
                    system
                    "test-user8"
                    "ta cs3251"))))

      (is (= "test-user9 is now a TA for cs3251."
             (<!! (handle-message
                    system
                    "test-user9"
                    "ta cs3251"))))

      (is (= "test-user9 is already a TA for cs3251."
             (<!! (handle-message
                    system
                    "test-user9"
                    "ta cs3251")))))))
