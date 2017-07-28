(ns leiningen.util
  (:require [clojure.tools.nrepl.transport :as t]
            [clojure.tools.nrepl.misc :refer [response-for]]))

(defn answer
  [transport msg response]
  (-> response
      seq
      flatten
      (response-for msg)
      (t/send transport)))
