(ns mule-preview.dev.init
  "Functions only used by dev tools when running locally"
  (:require
   [reagent.dom :as rd]
   [mule-preview.client.core :refer [mule-preview-diff-url]]))

(defn ^:dev/after-load init! []
  (let [component (mule-preview-diff-url
                   {:content-urls ["/example_xml/nice-example.xml"
                                   "/example_xml/nice-example-diff.xml"] 
                    :content-root "."})]
     (rd/render [component] (.getElementById js/document "app"))))
