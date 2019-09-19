(ns mule-preview.client.core
  "The entry point to the web application"
  (:require
   [reagent.core :as r]
   [mule-preview.client.views.preview :refer [start-preview start-preview-url]]
   [mule-preview.client.views.diff :refer [start-diff start-diff-url]]))

(defn view [root-component]
  [:div {:class "mp root-component"} @root-component])

(defn mule-preview-diff-url
  [{:keys [content-urls content-root]}]
  (let [root-component (r/atom [:div])
        [url-a url-b] content-urls]
    (r/create-class
     {:display-name  "MulePreviewDiffUrl"
      :component-did-mount
      (fn [this]
        (start-diff-url url-a
                        url-b
                        root-component
                        content-root))
      :reagent-render
      (partial view root-component)})))

(defn mule-preview-diff-content
  [{:keys [content-strings content-root]}]
  (let [root-component (r/atom [:div])
        [content-a content-b] content-strings]
    (r/create-class
     {:display-name  "MulePreviewDiffContent"
      :component-did-mount
      (fn [this]
        (start-diff content-a
                    content-b
                    root-component
                    content-root))
      :reagent-render
      (partial view root-component)})))

(defn mule-preview-url
  [{:keys [content-url content-root]}]
  (let [root-component (r/atom [:div])]
    (r/create-class
     {:display-name  "MulePreviewUrl"
      :component-did-mount
      (fn [this]
        (start-preview-url content-url
                           root-component
                           content-root))
      :reagent-render
      (partial view root-component)})))

(defn mule-preview-content
  [{:keys [content-string content-root]}]
  (let [root-component (r/atom [:div])]
    (r/create-class
     {:display-name  "MulePreviewContent"
      :component-did-mount
      (fn [this]
        (start-preview content-string
                       root-component
                       content-root))
      :reagent-render
      (partial view root-component)})))
