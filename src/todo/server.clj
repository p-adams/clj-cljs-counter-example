(ns todo.server
    (:require [compojure.core :refer :all]
              [compojure.route :as route]
              [ring.util.response :refer [response resource-response]]
              [ring.middleware.json :refer [wrap-json-response]]))


(defonce counter (atom 0))

(defn counter-response [state]
  (response {:msg state}))

(defn handle-counter [f]
  (swap! counter f)
    (response {:msg @counter}))



(defroutes app-routes
  (GET "/counter" [] (counter-response @counter))
  (POST "/counter/inc" [] (handle-counter inc))
  (POST "/counter/dec" [] (handle-counter dec))
  (GET "/api" [] (resource-response "index.html" {:root "public"}))
(route/not-found "Not Found"))


(def handler
    (-> app-routes
        (wrap-json-response)))


