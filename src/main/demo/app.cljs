(ns demo.app
  (:require [reagent.core :as reagent]
            ["react-spring/dist/react-spring.es.js" :refer (Spring Trail)]))


(defn a-component [{:keys [styles text]}]
  [:div {:style styles}
   (or text "Hello world")])

(def react-component (reagent/reactify-component a-component))

(defn render-fn [c props-fn]
  (fn [props]
    (-> c
        (reagent/reactify-component)
        (reagent/create-element (clj->js (props-fn props))))))

(def items
  (->> (range 10)
       (map (fn [n] {:key n :text "Clojurescript"}))
       (doall)))

(defn root []
  [:div
   [:> Spring
    {:from   {:opacity 0}
     :to     {:opacity 1}
     :render (render-fn a-component (fn [props] {:styles props}))}]
   [:> Trail
    {:from   {:opacity 0}
     :to     {:opacity 1}
     :keys   (map :key items)
     :render (map (fn [{:keys [text]}]
                    (render-fn a-component #(hash-map :styles % :text text)))
                  items)}]])

(defn ^:export mount-root []
  (reagent/render [root] (.getElementById js/document "app")))

(defn ^:export main []
  (mount-root))
