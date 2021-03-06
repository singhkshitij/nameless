(ns nameless.chat.db.config
  (:require [mount.core :refer [defstate]]
            [config.core :refer [env]]
            [taoensso.timbre :as log]))

(defn db-jdbc-uri []
  (let [{:keys [type server port name user password]} (:db env)
        uri (format "jdbc:%s://%s:%s/%s?user=%s&password=%s"
                    type server port name user password)]
    (log/debug "Starting db connection with server" server "& database" name)
    uri))
