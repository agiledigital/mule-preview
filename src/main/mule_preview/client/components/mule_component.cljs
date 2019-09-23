(ns mule-preview.client.components.mule-component
  "A single Mule component"
  (:require
   [reagent.core :as r]
   [mule-preview.client.components.tooltip :refer [generate-tooltip-record popper]]
   [mule-preview.client.components.shared :refer
    [pluralise normalise-name name-to-category-url name-to-img-url name-to-css-class image]])
  (:require-macros [mule-preview.client.macros :as m]))

(defn- labels-to-diff-icon-url [labels]
  (cond
    (:added labels) "img/plus.svg"
    (:edited labels) "img/is-not-equal-to.svg"
    (:removed labels) "img/minus.svg"
    :else nil))

(defn mule-component-inner [{:keys [name description css-class content-root
                                    location change-record showing-atom labels mappings
                                    attributes]}]
  (let [anchor-el (clojure.core/atom nil)
        img-url (name-to-img-url name false mappings)
        diff-icon-url (labels-to-diff-icon-url labels)
        category-url (name-to-category-url name mappings)
        tooltip-record (generate-tooltip-record change-record attributes)
        should-show-tooltip (and (not= name "mule") (not-empty tooltip-record))]
    (fn []
      [:div {:ref #(reset! anchor-el %) :class (when @showing-atom "highlighted")}
       [:div {:class ["component-container" css-class]
              :on-mouse-over (m/handler-fn
                              (when should-show-tooltip
                                (reset! showing-atom should-show-tooltip)
                                (.stopPropagation event)))
              :on-mouse-out  (m/handler-fn (reset! showing-atom false))}
        (when diff-icon-url (image diff-icon-url "diff-icon" content-root))
        [:div
         {:class ["component" name]}
         (image category-url "category-frame" content-root)
         (image img-url "icon" content-root)
         [:div {:class "label"} description]]]
       (popper tooltip-record labels location showing-atom anchor-el)])))

(defn mule-component [props]
  (let [showing-atom (r/atom false)]
    (fn [] (mule-component-inner (assoc props :showing-atom showing-atom)))))

; Exports for testing with Jest
(def ^:export MuleComponent (r/reactify-component mule-component-inner))