(ns mule-preview.client.components.shared
  "Fuctions shared between components"
  (:require
   [reagent.core :as r]
   [clojure.string :refer [split replace]]
   [lambdaisland.uri :refer [join]])
  (:require-macros [mule-preview.client.macros :as m]))

(def default-component-mapping {:image "UnknownNode-48x32.png"})
(def default-category-image "org.mule.tooling.ui.modules.core.miscellaneous.large.png")

(defn pluralise
  "Pluralise a given string value"
  [string]
  (str string "s"))

(defn normalise-name [name]
  (let [split-name (split name #":")
        first-component (first split-name)]
    (if (> (count split-name) 1)
      (str first-component ":*")
      first-component)))

(defn name-to-category-url [name mappings]
  (let [default-image default-category-image
        element-to-icon-map (:mappings mappings)
        mapping (get element-to-icon-map (keyword name))
        category (:category mapping)
        filename (if (some? category) (str category ".large.png") default-image)]
    (if-not (nil? filename)
      (str "img/icons/" filename)
      nil)))

(defn name-to-img-url [name is-nested mappings]
  (let [default-value default-component-mapping
        element-to-icon-map (:mappings mappings)
        mapping (get element-to-icon-map (keyword name) default-value)
        regular-filename (:image mapping)
        filename (if is-nested (get mapping :nested-image regular-filename) regular-filename)]
    (if-not (nil? filename)
      (str "img/icons/" filename)
      nil)))

(defn name-to-css-class [name]
  (when name (let [normalized-name (replace name #":" "_")]
               (str "mule-" normalized-name))))

(defn image
  ([url content-root] (image url "" content-root))
  ([url class content-root]
   (if-not (nil? url)
     [:img {:src (str (join content-root url)) :class class}]
     nil)))
