(ns leiningen.tester-nrepl
  (:require [leiningen.util :as u]
            [midje.repl :as midje]
            [midje.emission.plugins.nrepl :as plugin :refer [*transport* *msg*]]
            [midje.emission.state :as state]
            [midje.data.fact :as fact]
            [midje.data.nested-facts :as nested-facts]))

(defn tester-nrepl
  "I don't do a lot."
  [project & args]
  (println "Hi!"))

;;--------

midje.config/*config*

(midje.config/with-augmented-config { :emitter 'midje.emission.plugins.nrepl }
                                      ;:print-level :print-facts }
  (println (midje.config/running-in-repl?))
  (println midje.config/*config*)
  (midje/load-facts 'foo.core-test))

(defn wrap-tester
  [h]
  (fn [{:keys [op transport ns-test] :as msg}]
    (if (= "test-midje" op)
      (binding [*transport* transport
                *msg* msg]
        (println *transport* *msg*)
        (println (clojure.tools.nrepl.misc/response-for msg { :foo :bar }))
        (println ns-test)
        (midje/load-facts (symbol ns-test)))
      (h msg))))

;(midje/forget-facts :all)
;(midje/check-facts :all)
