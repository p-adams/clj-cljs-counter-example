(ns ^:figwheel-hooks todo.app
  (:require
   [goog.dom :as gdom]
   [reagent.core :as reagent :refer [atom]]
   [ajax.core :refer [GET POST]]))

(println "This text is printed from src/todo/app.cljs. Go ahead and edit it and see reloading in action.")

(defn multiply [a b] (* a b))

(defonce message (atom "loading..."))

(defn set-message [msg]
  (println msg)
  (reset! message (:msg msg)))

(defn load-the-counter []
  (GET "/counter" {:handler set-message
                  :response-format :json
                  :keywords? true}))

(defn inc-it [c]
  (reset! message (:msg c)))

(defn dec-it [c]
  (reset! message (:msg c)))

(defn inc-handler []
  (POST "/counter/inc" {:params {}
                               :response-format :json
                               :keywords? true
                               :handler inc-it}))

(defn dec-handler []
  (POST "/counter/dec" {:params {}
                               :response-format :json
                               :keywords? true
                               :handler dec-it}))


;; define your app data so that it doesn't get over-written on reload
(defonce app-state (atom {:text "Hello world!"}))

(defn get-app-element []
  (gdom/getElement "app"))

(defn hello-world []
  [:div
   [:h1 (:text @app-state)]
   [:p @message]
   [:button {:on-click inc-handler} "+" ]
   [:button {:on-click dec-handler} "-"]])

(defn mount [el]
  (load-the-counter)
  (reagent/render-component [hello-world] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(mount-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
