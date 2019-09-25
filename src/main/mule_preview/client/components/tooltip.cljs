(ns mule-preview.client.components.tooltip
  "The tooltip component that renders information about a component"
  (:require
   [reagent.core :as r]
   ["react-popper" :refer [Manager Reference Popper]]))

(defn delta-text [name [previous current]]
  (cond
    (= current previous) [:<> {:key name}
                          [:dt name]
                          [:dd current]]
    (and (some? previous) (some? current)) [:<> {:key name}
                                            [:dt name]
                                            [:dd [:span {:class ["previous"]} previous]
                                             " > "
                                             [:span {:class ["current"]} current]]]
    (and (some? previous) (nil? current)) [:<> {:key name}
                                           [:dt name]
                                           [:dd {:class ["previous"]} previous
                                            " (Attribute Removed)"]]
    (and (nil? previous) (some? current)) [:<> {:key name}
                                           [:dt name]
                                           [:dd {:class ["current"]} current
                                            " (Attribute Added)"]]))

(defn tooltip-item [[name delta]]
  (if (#{"content-hash"} name)
    [:<>
     {:key name}
     [:dt "Content"]
     [:dd "Modified"]]
    (delta-text name delta)))

(defn generate-tooltip-record [change-record attributes]
  (let [regular-attribute-map (into {} (for [[k v] attributes] [(name k) [v v]]))
        diff-attribute-map (into {} (for [{:keys [name delta]} change-record] [name delta]))]
    (merge regular-attribute-map diff-attribute-map)))

(defn tooltip-content [tooltip-record]
  [:dl (->>
        (dissoc tooltip-record "hash" "description")
        (map tooltip-item))])

(defn tooltip-neutral [tooltip-record]
  [:div (tooltip-content tooltip-record)])

(defn tooltip-added [tooltip-record]
  [:div {:class "current"}
   (tooltip-content tooltip-record)])

(defn tooltip-removed [tooltip-record]
  [:div {:class "previous"}
   (tooltip-content tooltip-record)])

(defn tooltip [tooltip-record labels location placement]
  [:div {:class ["mp-popover" placement]}
   [:h3 {:class "mp-popover-title"}
    (str "Line " (:line location) ", Column " (:column location))]
   [:div {:class "mp-popover-content"}
    (cond
      (:added labels) (tooltip-added tooltip-record)
      (:removed labels) (tooltip-removed tooltip-record)
      :else (tooltip-neutral tooltip-record))]])

(defn popper [tooltip-record labels location showing-atom anchor-el]
  (when @showing-atom
    [:div {:class "mp-popover-root"}
     [:> Popper {:placement "auto" :reference-element @anchor-el}
      (fn [props]
        (let [{:keys [ref style placement]} (js->clj props :keywordize-keys true)]
          (r/as-element [:div {:ref ref :style style :data-placement placement :class placement}
                         (tooltip tooltip-record labels location placement)])))]]))
