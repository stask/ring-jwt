(defproject ring-jwt "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.3.3"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/data.codec "0.1.0"]
                 [cheshire "5.3.1"]]
  :javac-options ^:replace ["-source" "1.7" "-target" "1.7"])
