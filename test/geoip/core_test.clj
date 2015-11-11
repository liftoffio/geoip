(ns geoip.core-test
  (:require [clojure.test :refer [deftest is]]
            [geoip.core :refer :all])
  (:import java.io.File))

(defn ensure-db-exists [path]
  (when-not (-> (File. path) .exists)
    (println "Missing required file:" path)
    (println "Download from http://dev.maxmind.com/geoip/geolite")
    (System/exit 1)))

(def geoip-country-db "data/GeoIP.dat")
(def geoip-city-db "data/GeoLiteCity.dat")

(ensure-db-exists geoip-country-db)
(ensure-db-exists geoip-city-db)

(deftest lookup-country-test
  (init-geoip geoip-country-db)
  (is (= {:code "--" :name "N/A"}
         (lookup-country "127.0.0.1")))
  (is (= {:code "US" :name "United States"}
         (lookup-country "64.233.160.0"))))

(deftest lookup-location-test
  (init-geoip geoip-city-db)
  (is (nil? (lookup-location "127.0.0.1")))
  (let [result (lookup-location "64.233.160.0")]
    (is (= "US" (:country-code result)))
    (is (= "United States" (:country-name result)))
    (is (= "CA" (:region-code result)))
    (is (= "California" (:region-name result)))
    (is (= "Mountain View" (:city result)))
    (is (= "94043" (:postal-code result)))
    (is (= 807 (:dma-code result)))
    (is (number? (:latitude result)))
    (is (number? (:longitude result)))))
