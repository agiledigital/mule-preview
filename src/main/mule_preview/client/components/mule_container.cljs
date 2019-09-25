(ns mule-preview.client.components.mule-container
  "A component that contains other Mule components"
  (:require
   [reagent.core :as r]
   [mule-preview.client.components.tooltip :refer [generate-tooltip-record popper]]
   [mule-preview.client.components.shared :refer
    [pluralise normalise-name name-to-category-url name-to-img-url name-to-css-class image]])
  (:require-macros [mule-preview.client.macros :as m]))

(defn- child-container [children]
  (into [] (concat [:div {:class "container-children"}] children)))

(defn- arrow [content-root]
  (image "img/arrow-right-2x.png" "flow-arrow" content-root))

(defn mule-container-inner [{:keys [name description children css-class content-root
                                    location change-record showing-atom labels mappings
                                    attributes]}]
  (let [anchor-el (clojure.core/atom nil)
        generated-css-class (name-to-css-class name)
        img-url (name-to-img-url name (some? children) mappings)
        category-url (name-to-category-url name mappings)
        interposed-children (interpose (arrow content-root) children)
        child-container-component (child-container interposed-children)
        tooltip-record (generate-tooltip-record change-record attributes)
        should-show-tooltip (and (not= name "mule") (not-empty tooltip-record))]
    (fn []
      [:div {:ref #(reset! anchor-el %)}
       [:div {:class ["container" generated-css-class css-class (when @showing-atom "highlighted")]
              :on-mouse-over (m/handler-fn
                              (when should-show-tooltip
                                (reset! showing-atom should-show-tooltip)
                                (.stopPropagation event)))
              :on-mouse-out  (m/handler-fn (reset! showing-atom false))}
        [:div {:class "container-title"} description]
        [:div {:class "container-inner"}
         [:div {:class "icon-container"}
          (image category-url "category-frame" content-root)
          (image img-url "icon container-image" content-root)]
         child-container-component]]
       (popper tooltip-record labels location showing-atom anchor-el)])))

(defn mule-container [props]
  (let [showing-atom (r/atom false)]
    (fn [] (mule-container-inner (assoc props :showing-atom showing-atom)))))

; Exports for testing with Jest
(def ^:export MuleContainer (r/reactify-component mule-container-inner))