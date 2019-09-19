(ns mule-preview.client.js-export
  "Entry point for dependant Javascript projects.
   Exports the function names with camel case to fit Javascript conventions"
  (:require
   [reagent.core :as r]
   [schema.core :as s :include-macros true]
   [camel-snake-kebab.core :refer [->kebab-case]]
   [mule-preview.client.utils :refer [map-keys]]
   [mule-preview.client.core :refer [mule-preview-diff-url
                                     mule-preview-diff-content
                                     mule-preview-url
                                     mule-preview-content]]))

;; Prop Types
; Try to at least do some validation on the way in

(def diff-url-props
  {:contentUrls
   [(s/one s/Str "urlA")
    (s/one s/Str "urlB")]
   :contentRoot s/Str})

(def diff-content-props
  {:contentStrings
   [(s/one s/Str "contentA")
    (s/one s/Str "contentB")]
   :contentRoot s/Str})

(def url-props
  {:contentUrl s/Str
   :contentRoot s/Str})

(def content-props
  {:contentString s/Str
   :contentRoot s/Str})

(enable-console-print!)

; We are publishing this as a JS library on NPM so we should follow existing conventions
; and accept camel case props, then convert them to kebab so the ClojureScript side of
; the library can stay idiomatic
(defn clojurefy-props [props]
  (let [clj-props (js->clj props :keywordize-keys true)]
    (map-keys ->kebab-case clj-props)))

(def ^:export MulePreviewDiffUrl
  (r/reactify-component (fn [raw-props]
                          ((s/defn ^:always-validate MulePreviewDiffUrlComponent [props :- diff-url-props]
                             (mule-preview-diff-url (clojurefy-props props))) (js->clj raw-props)))))
(def ^:export MulePreviewDiffContent
  (r/reactify-component (fn [raw-props]
                          ((s/defn ^:always-validate MulePreviewDiffContentComponent [props :- diff-content-props]
                             (mule-preview-diff-content (clojurefy-props props))) (js->clj raw-props)))))
(def ^:export MulePreviewUrl
  (r/reactify-component (fn [raw-props]
                          ((s/defn ^:always-validate MulePreviewUrlComponent [props :- url-props]
                             (mule-preview-url (clojurefy-props props))) (js->clj raw-props)))))
(def ^:export MulePreviewContent
  (r/reactify-component (fn [raw-props]
                          ((s/defn ^:always-validate MulePreviewContentComponent [props :- content-props]
                             (mule-preview-content (clojurefy-props props))) (js->clj raw-props)))))
