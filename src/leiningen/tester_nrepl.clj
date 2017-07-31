(ns leiningen.tester-nrepl
  (:require [leiningen.util :as u]
            [midje.repl :as midje]
            [midje.emission.plugins.default :as default]
            [midje.emission.state :as state]))

(defn tester-nrepl
  "I don't do a lot."
  [project & args]
  (println "Hi!"))

(def ^:dynamic cur-transport nil)
(def ^:dynamic cur-msg nil)

(defn pass
  [transport msg]
  (println "Enter: pass")
  (println cur-transport)
  (comment (u/answer cur-transport cur-msg { :type :test-result
                            :fact 123
                            :result-detail { :result :pass }})))

(defn fail
  [transport msg m]
  (println m)
  (case (:type m)
    :actual-result-did-not-match-expected-value
      (u/answer transport msg { :type :test-result
                                :fact 123
                                :result-detail { :result :fail
                                                 :reason :actual-result-did-not-match-expected-value
                                                 :expected-result (:expected-result m)
                                                 :actual (:actual m) }
                                })
    (throw (ex-info "Unknown failure type" { :input-arg m }))))

(def pass-f (partial pass cur-transport cur-msg))

(def emission-map (assoc default/emission-map
                         :pass (ns-resolve *ns* (symbol "pass-f"))))

(state/install-emission-map-wildly emission-map)

state/installation-ok?

state/emission-functions

midje.config/*config*

(midje.config/with-augmented-config { :emitter 'leiningen.tester-nrepl }
                                      ;:print-level :print-facts }
  (println (midje.config/running-in-repl?))
  (println midje.config/*config*)
  (midje/load-facts 'foo.core-test))

(defn wrap-tester
  [h]
  (fn [{:keys [op transport ns-test] :as msg}]
    (if (= "test-midje" op)
      (binding [cur-transport transport
                cur-msg msg]
        (println cur-transport cur-msg)
        (println (clojure.tools.nrepl.misc/response-for msg { :foo :bar }))
        (println ns-test)
        (u/answer cur-transport cur-msg { :foo :bar :value 3 })
        (u/answer cur-transport cur-msg { :value "done" }))
        ;(midje/load-facts (symbol ns-test)))
      (h msg))))

(let [a 'foo.core-test]
(midje/load-facts a))
(midje/load-facts (symbol "foo.core-test"))
(symbol "foo.core-test")
(midje/forget-facts :all)
(midje/check-facts :all)

(defn tt [{:keys [hi]}]
  hi)

(let [b 'joo]
;(= (tt { :hi 'joo}) `'~b))
  `'~b)

