(ns ring.middleware.external-assets
  (:require [ring.util.request :as req]
            [ring.util.response :refer [url-response]]
            [ring.middleware.head :refer [head-response]]
            [ring.util.codec :as codec]))

(defn- remove-prefix [s prefix]
  (when s
    (clojure.string/replace s (re-pattern (str "^" prefix)) "")))

(defn- assoc-asset [path m file]
  (assoc m (remove-prefix (.getPath file) path) (-> file .toURI .toURL)))

(defn- asset-map [path]
  (when path
    (->> (clojure.java.io/file path)
         file-seq
         (remove #(.isDirectory %))
         (reduce (partial assoc-asset path) {}))))

(defn- request-path [asset-prefix request]
  (-> request
      req/path-info
      codec/url-decode
      (remove-prefix asset-prefix)))

(defn wrap-external-resources
  "asset-path - specifies where the assets are located on the filesystem
   asset-prefix - specifies the URI prefix for the assets"
  [handler asset-path asset-prefix]
  (if-let [assets (asset-map asset-path)]
    (fn [request]
      (if (#{:head :get} (:request-method request))
        (if-let [asset (assets (request-path asset-prefix request))]
          (-> (url-response asset) (head-response request))
          (handler request))
        (handler request)))
    handler))
