(ns leiningen.tester-nrepl
  (:require [midje.repl :as midje]
            [midje.emission.plugins.nrepl :as plugin :refer [*transport* *msg* *rtn-fmt*]]
            [clojure.tools.nrepl.middleware :as mw]))

(defn tester-nrepl
  "I don't do a lot."
  [project & args]
  (println "Hi!"))

;;--------

midje.config/*config*

(midje.config/with-augmented-config { :emitter 'midje.emission.plugins.nrepl
                                      :print-level :print-facts }
  (defn wrap-tester
    [h]
    (fn [{:keys [op transport ns-test rtn-fmt] :as msg}]
      (if (= "test-midje" op)
        (binding [*transport* transport
                  *msg* msg
                  *rtn-fmt* (keyword rtn-fmt)]
          (println *transport* *msg*)
          ;(println (clojure.tools.nrepl.misc/response-for msg { :foo :bar }))
          (println ns-test rtn-fmt)
          (Thread/sleep 200)
          (midje/load-facts (symbol ns-test))
          ;(Thread/sleep 1000)
          (println "done."))
        (h msg))))
)

;(midje/forget-facts :all)
;(midje/check-facts :all)

(mw/set-descriptor! #'wrap-tester
                    {:requires #{}
                     :expects #{}
                     :handles {"test-midje"
                               {:doc "Run Midje tests in specified namespace. Note that responses are streamed and their format can be configured at request-time."
                                :requires {"ns-test" "The root namespace for tests (string)"}
                                :optional {"rtn-fmt" "Control how responses are formatted. One of 'lighttable' or 'standard'. (string, default 'standard')"}
                                }}})
