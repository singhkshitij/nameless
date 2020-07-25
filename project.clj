(defproject nameless "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.immutant/immutant "2.1.10"]
                 [compojure "1.6.1"]
                 [ring/ring-core "1.7.1"]
                 [org.clojure/tools.logging "1.1.0"]
                 [yogthos/config "1.1.7"]
                 [mount "0.1.16"]
                 [ragtime "0.8.0"]
                 [hikari-cp "2.9.0"]
                 [org.clojure/java.jdbc "0.7.11"]
                 [org.postgresql/postgresql "42.2.8"]
                 [honeysql "0.9.10"]
                 [cheshire "5.10.0"]
                 [clj-time "0.15.2"]
                 [ring/ring-json "0.5.0"]
                 [ring/ring-defaults "0.3.2"]
                 [com.taoensso/nippy "2.14.0"]
                 [prismatic/schema "1.1.12"]
                 [ring-cors "0.1.13"]]
  :main ^:skip-aot nameless.core
  :uberjar-name "nameless-standalone.jar"
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :prod {:resource-paths ["config/prod"]}
             :dev  {:resource-paths ["config/dev"]}})
