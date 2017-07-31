(ns leiningen.util
  (:require [clojure.tools.nrepl.transport :as t]
            [clojure.tools.nrepl.misc :refer [response-for]]))

(defn answer
  [transport msg response]
  (t/send transport (response-for msg response)))
