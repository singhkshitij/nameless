(ns nameless.chat.domain.core
  (:require [nameless.chat.db.core :as db]
            [immutant.web.async :as async]
            [nameless.cache :as cache]
            [clojure.walk :as wk]
            [cheshire.core :refer :all]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [config.core :refer [env]]
            [clojure.tools.logging :as log]))

(def channel-store (atom []))

(defn session->unique-id [channel]
  (-> (async/originating-request channel)
      (:uri)
      (subs 1)))

(defn broadcast-message [channel content]
  (let [chs (cache/get-connected-clients (session->unique-id channel))]
    (doseq [ch chs]
      (async/send! ch content))))

(defn prepare-message [channel type message]
  (->> (case type
         :entry {:type "entry", :message message}
         :default {:type "message", :message message})
       (generate-string)
       (broadcast-message channel)))

(defn save-session [channel]
  (let [uid (session->unique-id channel)
        start-time (c/to-long (t/now))]
    (cache/save-session uid start-time channel)))

(defn create-session [channel]
  (let [username (:query-string (async/originating-request channel))]
    (swap! channel-store conj channel)
    (save-session channel)
    (log/info "New client connected !")
    (prepare-message channel :entry (str username " joined the chat"))))

(defn retry-save-message [url message author]
  ;This has to be fixed, control comes till this point, use a retry lib
  (repeatedly (:save-retry-limit (:message env))
              (db/add! url message author)))

(defn save-message [channel m]
  (let [data (decode m)
        {:keys [message author url]} (wk/keywordize-keys data)
        content (db/add! url message author)]
    (if (= content :failure)
      (retry-save-message url message author)
      (broadcast-message channel message))))

(defn remove-session [channel]
  (let [uid (session->unique-id channel)]
    (swap! channel-store (fn [store] (remove #(= channel %) store)))
    (cache/delete-session uid)))

(defn create-meeting [url]
  {:status :success
   :body "ok"})

(defn active-room? [url]
  (let [active-clients (or (cache/get-connected-clients url) ())]
    (if (> (count active-clients) 0)
      {:status :success
       :data true}
      {:status :success
       :data false})))